package server.lobby.state;

import proto.Command;
import server.game.Player;
import server.lobby.Lobby;

public class WaitingState extends AState {

    public WaitingState(Lobby lobby) {
        super("Waiting", lobby);
    }

    @Override
    public AState initialize() {
        return this;
    }

    @Override
    public boolean isFinished() {
        return this.getLobby().getPlayers().size() == 4;
    }

    @Override
    public AState getNextState() {
        return new TeamState(this.getLobby());
    }

    @Override
    public void handleAction(Player player, Command.LobbyCmd cmd) {

    }

}
