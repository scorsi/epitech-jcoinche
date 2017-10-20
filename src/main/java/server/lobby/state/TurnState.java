package server.lobby.state;

import io.netty.channel.Channel;
import proto.Command;
import server.game.Card;
import server.game.Deck;
import server.lobby.Lobby;

public class TurnState extends AState {

    public TurnState(Lobby lobby) {
        super("Turn", lobby);
    }

    @Override
    public AState initialize() {
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

}
