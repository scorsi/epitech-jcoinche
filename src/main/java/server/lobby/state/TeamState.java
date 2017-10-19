package server.lobby.state;

import proto.Command.LobbyCmd;
import server.game.Player;
import server.lobby.Lobby;
import server.game.Team;

public class TeamState extends AState {

    public TeamState(Lobby lobby) {
        super("Team", lobby);
    }

    @Override
    public AState initialize() {
        for (Player player : this.getLobby().getPlayers()) {
            player.setTeam(Team.Undefined);
        }
        return this;
    }

    @Override
    public boolean isFinished() {
        for (Player player : this.getLobby().getPlayers()) {
            if (player.getTeam() == Team.Undefined)
                return false;
        }
        return true;
    }

    @Override
    public AState getNextState() {
        return null;
    }

    @Override
    public void handleAction(Player player, LobbyCmd cmd) {

    }

}
