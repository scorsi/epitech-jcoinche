package server.lobby.state;

import io.netty.channel.Channel;
import proto.Command;
import server.game.*;
import server.lobby.Lobby;

public class TurnState extends AState {

    int playerTurn;

    public TurnState(Lobby lobby) {
        super("Turn", lobby);
    }

    @Override
    public AState initialize() {
        playerTurn = 0;
        this.getLobby().broadcast("The game is ready. Let's play.", null);
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
        } else if (cmd.getCmdType().equals(Command.LobbyCmd.CmdType.PLAY_CARD)) {
            Player player = this.getLobby().getPlayer(channel);
            Deck deck = player.getDeck();
            int cardIndex = deck.foundCard(CardFace.from(cmd.getCard().getFace()), CardColor.from(cmd.getCard().getColor()));
            if (cardIndex == -1) {
                return;
            }
            this.getLobby().broadcast(player.getName() + " played a " +
                    CardFace.from(cmd.getCard().getFace()).getName() + " of " +
                    CardColor.from(cmd.getCard().getColor()).getName() + ".", null);
            deck.getCards().remove(cardIndex);
            this.nextPlayer();
        }

    }

    private void nextPlayer() {
        this.playerTurn++;
        this.playerTurn %= 4;
        System.out.println(this.playerTurn);
    }

}
