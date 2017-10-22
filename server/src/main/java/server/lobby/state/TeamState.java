package server.lobby.state;

import io.netty.channel.Channel;
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
        this.getLobby().broadcast("The lobby is complete, you can choose your teams.", null);
        for (Player player : this.getLobby().getPlayers()) {
            player.setTeam(Team.Undifined);
        }

        this.getLobby().getPoints().put(Team.Red, 0);
        this.getLobby().getPoints().put(Team.Blue, 0);
        return this;
    }

    @Override
    public boolean isFinished() {
        for (Player player : this.getLobby().getPlayers()) {
            if (player.getTeam() == Team.Undifined)
                return false;
        }
        return true;
    }

    @Override
    public AState getNextState() {
        this.getLobby().broadcast("All teams are complete.", null);
        return new DrawState(this.getLobby());
    }

    @Override
    public void handleAction(Channel channel, LobbyCmd cmd) {
        Player player = this.getLobby().getPlayer(channel);
        if (cmd.getCmdType().equals(LobbyCmd.CmdType.TEAM)) {
            this.handleTeam(channel, player, cmd.getValue());
        }
    }

    private boolean isTeamFull(Player playerToNotCheck, Team teamToCheck) {
        int i = 0;

        for (Player player : this.getLobby().getPlayers()) {
            if (!player.equals(playerToNotCheck)) {
                if (player.getTeam().equals(teamToCheck)) {
                    ++i;
                }
            }
        }
        return i >= 2;
    }

    private void handleTeam(Channel channel, Player player, String team) {
        if (team.toUpperCase().equals("RED")) {
            if (!this.isTeamFull(player, Team.Red)) {
                this.getLobby().sendMsg("[SERVER] You changed your team to Red.", channel);
                this.getLobby().broadcast(player.getName() + " joined team Red", null);
                player.setTeam(Team.Red);
            } else {
                this.getLobby().sendMsg("[SERVER] This team is full, choose the other.", channel);
            }
        } else if (team.toUpperCase().equals("BLUE")) {
            if (!this.isTeamFull(player, Team.Blue)) {
                this.getLobby().sendMsg("[SERVER] You changed your team to Blue.", channel);
                this.getLobby().broadcast(player.getName() + " joined team Blue", null);
                player.setTeam(Team.Blue);
            } else {
                this.getLobby().sendMsg("[SERVER] This team is full, choose the other.", channel);
            }
        } else {
            this.getLobby().sendMsg("[SERVER] Team: Invalid parameter.", channel);
        }
    }

}
