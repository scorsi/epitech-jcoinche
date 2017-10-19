package server.lobby.state;

import io.netty.channel.Channel;
import proto.Command;
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

    }

}
