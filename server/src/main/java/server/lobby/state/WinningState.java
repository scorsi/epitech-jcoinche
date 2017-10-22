package server.lobby.state;

import io.netty.channel.Channel;
import proto.Command;
import server.game.Team;
import server.lobby.Lobby;

public class WinningState extends AState {

    public WinningState(Lobby lobby) {
        super("Winning", lobby);
    }

    @Override
    public AState initialize() {
        int redPoints = this.getLobby().getPoints().get(Team.Red);
        int bluePoints = this.getLobby().getPoints().get(Team.Blue);
        if (bluePoints > redPoints)
            this.getLobby().broadcast("Team Blue win with " + bluePoints + " points!\nTeam Red had " +
                    redPoints + " points.", null);
        else
            this.getLobby().broadcast("Team Red win with " + redPoints + " points!\nTeam Blue had " +
                    bluePoints + " points.", null);
        return this;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public AState getNextState() {
        return null;
    }

    @Override
    public void handleAction(Channel channel, Command.LobbyCmd cmd) {

    }
}
