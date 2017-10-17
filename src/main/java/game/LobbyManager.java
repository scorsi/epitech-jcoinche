package game;

import proto.Message.MessageWrapper;
import proto.Player.PlayerConnection;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LobbyManager {

    private List<Lobby> lobbies = new ArrayList<>();


    LobbyManager() {
    }

    public void createLobby(HashMap<Channel, PlayerConnection> map) {
        this.lobbies.add(new Lobby(map));
    }

    public void handleChat(Channel channel, MessageWrapper msg) {
        for (Lobby lobby : lobbies) {
            if (lobby.has(channel)) {
                lobby.broadcast(msg.getChat().getText(), channel);
                break;
            }
        }
    }

    public void disconnectPlayer(Channel channel) {
        Lobby lobbyToShutdown = null;

        for (Lobby lobby : lobbies) {
            if (lobby.has(channel)) {
                lobbyToShutdown = lobby;
                break;
            }
        }

        if (lobbyToShutdown == null)
            return;

        lobbyToShutdown.shutdown(channel);
        lobbies.remove(lobbyToShutdown);
    }

}
