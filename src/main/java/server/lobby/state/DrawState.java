package server.lobby.state;

import proto.Command;
import server.game.Player;
import server.lobby.Lobby;

import io.netty.channel.Channel;

public class DrawState extends AState {

    public DrawState(Lobby lobby) {
        super("Draw", lobby);
    }

    @Override
    public AState initialize() {
        this.getLobby().broadcast("All teams are complete. Distribution of cards.", null);
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
