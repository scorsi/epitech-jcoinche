package server.lobby.state;

import proto.Command.LobbyCmd;
import server.game.Player;
import server.lobby.Lobby;

public abstract class AState {

    private String name;
    private Lobby lobby;

    AState(String name, Lobby lobby) {
        this.name = name;
        this.lobby = lobby;
    }

    abstract public AState initialize();
    abstract public boolean isFinished();
    abstract public AState getNextState();
    abstract public void handleAction(Player player, LobbyCmd cmd);

    protected Lobby getLobby() {
        return this.lobby;
    }

    public String getName() {
        return this.name;
    }

}
