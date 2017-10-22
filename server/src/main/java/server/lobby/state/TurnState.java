package server.lobby.state;

import io.netty.channel.Channel;
import proto.Command;
import server.game.*;
import server.lobby.Lobby;

import java.util.HashMap;
import java.util.Map;

public class TurnState extends AState {

    private HashMap<Player, Card> table;
    private int playerTurn;
    private Contract contract;
    private HashMap<Team, Integer> points;

    public TurnState(Lobby lobby, Contract contract) {
        super("Turn", lobby);
        this.contract = contract;
    }

    @Override
    public AState initialize() {
        this.points = new HashMap<>();
        this.points.put(Team.Red, 0);
        this.points.put(Team.Blue, 0);
        this.table = new HashMap<>();
        this.playerTurn = 0;
        this.getLobby().broadcast("The game is ready.", null);
        this.displayTurnMessage();
        return this;
    }

    @Override
    public boolean isFinished() {
        boolean check = true;

        for (Player player : this.getLobby().getPlayers()) {
            if (player.getDeck().getCards().size() != 0) {
                check = false;
            }
        }
        return check;
    }

    @Override
    public AState getNextState() {
        this.getLobby().broadcast("New turn.", null);
        return new DrawState(this.getLobby());
    }

    @Override
    public void handleAction(Channel channel, Command.LobbyCmd cmd) {
        if (cmd.getCmdType().equals(Command.LobbyCmd.CmdType.SHOW_CARDS)) {
            this.handleShowCards(channel, cmd);
        } else if (cmd.getCmdType().equals(Command.LobbyCmd.CmdType.PLAY_CARD)) {
            this.handlePlayCard(channel, cmd);
        } else if (cmd.getCmdType().equals(Command.LobbyCmd.CmdType.SHOW_TABLE)) {
            this.handleShowTable(channel, cmd);
        } else if (cmd.getCmdType().equals(Command.LobbyCmd.CmdType.SHOW_CONTRACT)) {
            this.handleShowContract(channel, cmd);
        } else if (cmd.getCmdType().equals(Command.LobbyCmd.CmdType.SHOW_POINTS)) {
            this.handleShowPoints(channel, cmd);
        }
    }

    private void handleShowContract(Channel channel, Command.LobbyCmd cmd) {

        this.getLobby().sendMsg("[SERVER] The contract is " + this.contract.getType() + " " + this.contract.getValue(), channel);
    }

    private void handleShowTable(Channel channel, Command.LobbyCmd cmd) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[SERVER] List of cards in the table:");
        for (Map.Entry<Player, Card> entry : this.table.entrySet()) {
            stringBuilder.append("\n-> ")
                    .append(entry.getKey().getName())
                    .append("[")
                    .append(entry.getKey().getTeam().getName())
                    .append("]: ")
                    .append(entry.getValue().getFaceName())
                    .append(" of ")
                    .append(entry.getValue().getColorName());
        }

        this.getLobby().sendMsg(stringBuilder.toString(), channel);
    }

    private void handleShowCards(Channel channel, Command.LobbyCmd cmd) {
        Deck deck = this.getLobby().getPlayer(channel).getDeck();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[SERVER] List of your cards:");
        for (Card card : deck.getCards()) {
            stringBuilder.append("\n-> ")
                    .append(card.getFaceName())
                    .append(" of ")
                    .append(card.getColorName());
        }

        this.getLobby().sendMsg(stringBuilder.toString(), channel);
    }

    private void handlePlayCard(Channel channel, Command.LobbyCmd cmd) {
        Player player = this.getLobby().getPlayer(channel);
        if (this.getLobby().getPlayers().toArray()[this.playerTurn].equals(player)) {
            Deck deck = player.getDeck();
            int cardIndex = deck.foundCard(CardFace.from(cmd.getCard().getFace()), CardColor.from(cmd.getCard().getColor()));
            if (cardIndex == -1) {
                this.getLobby().sendMsg("[SERVER] You can't play this card.", channel);
                return;
            }
            this.getLobby().broadcast(player.getName() + " played a " +
                    CardFace.from(cmd.getCard().getFace()).getName() + " of " +
                    CardColor.from(cmd.getCard().getColor()).getName() + ".", null);
            this.table.put(player, deck.getCards().get(cardIndex));
            deck.getCards().remove(cardIndex);
            this.nextPlayer();
        } else {
            this.getLobby().sendMsg("[SERVER] This is not your turn.", channel);
        }
    }

    private void handleShowPoints(Channel channel, Command.LobbyCmd cmd) {
        System.out.println("toto");
        this.getLobby().sendMsg("[SERVER] Team points:\n-> Red: " + this.points.get(Team.Red) +
                "\n-> Blue: " + this.points.get(Team.Blue), channel);
    }

    private void displayTeamPoints() {
        this.getLobby().broadcast("Team points:\n-> Red: " + this.points.get(Team.Red) +
                "\n-> Blue: " + this.points.get(Team.Blue), null);
    }

    private void displayTurnMessage() {
        Player player = (Player) this.getLobby().getPlayers().toArray()[this.playerTurn];
        this.getLobby().broadcast("This is the turn of " + player.getName() + ".", null);
        this.getLobby().sendMsg("[SERVER] This is your turn.",
                this.getLobby().getChannelByPlayer(player));
    }

    private void calculateFoldWinner() {
        Team teamWinner;
        switch (this.contract.getType()) {
            case SPADE:
            case CLUB:
            case HEART:
            case DIAMOND:
                teamWinner = this.calculateFoldWinnerWithColorTrump();
                break;
            case ALL_TRUMP:
                teamWinner = this.calculateFoldWinnerWithAllTrump();
                break;
            case NO_TRUMP:
            default:
                teamWinner = this.calculateFoldWinnerWithNoTrump();
                break;
        }
        int foldPoint = this.calculateFoldPoints();
        this.points.put(teamWinner, this.points.get(teamWinner) + foldPoint);

        this.table.clear();
        this.displayTeamPoints();
    }

    private int calculateFoldPoints() {
        int foldPoints = 0;

        for (Card card : this.table.values()) {
            switch (this.contract.getType()) {
                case SPADE:
                    if (card.getColorName().equals(CardColor.Spade.getName())) {
                        foldPoints += card.getPointOneTrump();
                    } else {
                        foldPoints += card.getPointIsNotTrump();
                    }
                    break;
                case CLUB:
                    if (card.getColorName().equals(CardColor.Club.getName())) {
                        foldPoints += card.getPointOneTrump();
                    } else {
                        foldPoints += card.getPointIsNotTrump();
                    }
                    break;
                case HEART:
                    if (card.getColorName().equals(CardColor.Heart.getName())) {
                        foldPoints += card.getPointOneTrump();
                    } else {
                        foldPoints += card.getPointIsNotTrump();
                    }
                    break;
                case DIAMOND:
                    if (card.getColorName().equals(CardColor.Diamond.getName())) {
                        foldPoints += card.getPointOneTrump();
                    } else {
                        foldPoints += card.getPointIsNotTrump();
                    }
                    break;
                case NO_TRUMP:
                    foldPoints += card.getPointNoTrump();
                    break;
                case ALL_TRUMP:
                    foldPoints += card.getPointAllTrump();
                    break;
            }
        }
        return foldPoints;
    }

    private Team calculateFoldWinnerWithColorTrump() {
        CardColor color;
        switch (this.contract.getType()) {
            case DIAMOND:
                color = CardColor.Diamond;
                break;
            case HEART:
                color = CardColor.Heart;
                break;
            case CLUB:
                color = CardColor.Club;
                break;
            case SPADE:
            default:
                color = CardColor.Spade;
                break;
        }

        int higherPoint = -1;
        Team teamWinner = null;

        for (Map.Entry<Player, Card> entry : this.table.entrySet()) {
            if (color.getName().equals(entry.getValue().getColorName()) &&
                    entry.getValue().getPointOneTrump() > higherPoint) {
                teamWinner = entry.getKey().getTeam();
                higherPoint = entry.getValue().getPointOneTrump();
            }
        }

        if (higherPoint == -1) {
            color = ((Card) (this.table.values().toArray()[0])).getColor();

            for (Map.Entry<Player, Card> entry : this.table.entrySet()) {
                if (color.getName().equals(entry.getValue().getColorName()) &&
                        entry.getValue().getPointIsNotTrump() > higherPoint) {
                    teamWinner = entry.getKey().getTeam();
                    higherPoint = entry.getValue().getPointIsNotTrump();
                }
            }
        }

        return teamWinner;
    }

    private Team calculateFoldWinnerWithAllTrump() {
        CardColor color = ((Card) (this.table.values().toArray()[0])).getColor();

        int higherPoint = -1;
        Team teamWinner = null;

        for (Map.Entry<Player, Card> entry : this.table.entrySet()) {
            if (color.getName().equals(entry.getValue().getColorName()) &&
                    entry.getValue().getPointAllTrump() > higherPoint) {
                teamWinner = entry.getKey().getTeam();
                higherPoint = entry.getValue().getPointAllTrump();
            }
        }

        return teamWinner;
    }

    private Team calculateFoldWinnerWithNoTrump() {
        CardColor color = ((Card) (this.table.values().toArray()[0])).getColor();

        int higherPoint = -1;
        Team teamWinner = null;

        for (Map.Entry<Player, Card> entry : this.table.entrySet()) {
            if (color.getName().equals(entry.getValue().getColorName()) &&
                    entry.getValue().getPointNoTrump() > higherPoint) {
                teamWinner = entry.getKey().getTeam();
                higherPoint = entry.getValue().getPointNoTrump();
            }
        }

        return teamWinner;
    }

    private void nextPlayer() {
        this.playerTurn++;
        if (this.playerTurn == 4) {
            this.playerTurn = 0;
            this.calculateFoldWinner();
        }
        System.out.println(this.playerTurn);
        this.displayTurnMessage();
    }

}
