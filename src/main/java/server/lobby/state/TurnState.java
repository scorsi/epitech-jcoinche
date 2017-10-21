package server.lobby.state;

import io.netty.channel.Channel;
import proto.Command;
import server.game.*;
import server.lobby.Lobby;

import java.util.HashMap;
import java.util.Map;

public class TurnState extends AState {

    private HashMap<Player, Card> table;
    int playerTurn;

    public TurnState(Lobby lobby) {
        super("Turn", lobby);
    }

    @Override
    public AState initialize() {
        this.table = new HashMap<>();
        this.playerTurn = 0;
        this.getLobby().broadcast("The game is ready. Let's play.", null);
        this.displayTurnMessage();
        return this;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public AState getNextState() {
        return null;
    }

    @Override
    public void handleAction(Channel channel, Command.LobbyCmd cmd) {
        if (cmd.getCmdType().equals(Command.LobbyCmd.CmdType.SHOW_CARDS)) {
            this.handleShowCards(channel, cmd);
        } else if (cmd.getCmdType().equals(Command.LobbyCmd.CmdType.PLAY_CARD)) {
            this.handlePlayCard(channel, cmd);
        } else if (cmd.getCmdType().equals(Command.LobbyCmd.CmdType.SHOW_TABLE)) {
            this.handleShowTable(channel, cmd);
        }
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

    private void displayTurnMessage() {
        Player player = (Player) this.getLobby().getPlayers().toArray()[this.playerTurn];
        this.getLobby().broadcast("This is the turn of " + player.getName() + ".", null);
        this.getLobby().sendMsg("[SERVER] This is your turn.",
                this.getLobby().getChannelByPlayer(player));
    }

    private void nextPlayer() {
        this.playerTurn++;
        this.playerTurn %= 4;
        System.out.println(this.playerTurn);
        this.displayTurnMessage();
    }

}
