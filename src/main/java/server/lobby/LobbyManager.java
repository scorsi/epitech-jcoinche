package server;

import io.netty.channel.Channel;

import proto.Command.GlobalCmd;
import proto.Command.LobbyCmd;
import proto.Message.MessageWrapper;
import proto.Message.MessageChat;
import server.command.CommandManager;
import server.game.Player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class LobbyManager {

    private CommandManager commandManager;
    private static int nbPlayer = 0;

    private HashMap<Channel, Player> waitingPlayers = new HashMap<>();
    private List<Lobby> lobbies = new ArrayList<>();


    public LobbyManager() {
        this.commandManager = new CommandManager(this);
    }

    public void handleAction(Channel channel, MessageWrapper msg) {
        this.commandManager.handle(channel, msg);
    }

    public void putPlayersToWaitingList(HashMap<Channel, Player> players) {
        this.waitingPlayers.putAll(players);
    }

    public void connectPlayer(Channel channel) {
        ++nbPlayer;
        Player player = new Player("Player_" + nbPlayer);

        this.waitingPlayers.put(channel, player);

        this.sendMsg(channel, "[Server] Welcome to the jCoinche server!\nYour name is " + player.getName() + ". You can change it by typing \"/username {name}\".");

        this.checkForAvailableLobby();
    }

    public void disconnectPlayer(Channel channel) {
        if (this.waitingPlayers.containsKey(channel))
            this.waitingPlayers.remove(channel);
        else {
            Lobby lobbyToShutdown = this.getLobbyByChannel(channel);

            if (lobbyToShutdown == null)
                return;

            lobbyToShutdown.shutdown(channel);
            lobbies.remove(lobbyToShutdown);
        }
    }

    public Lobby getLobbyByChannel(Channel channel) {
        Lobby lobbyToReturn = null;

        for (Lobby lobby : lobbies) {
            if (lobby.has(channel)) {
                lobbyToReturn = lobby;
                break;
            }
        }

        return lobbyToReturn;
    }

    private void sendMsg(Channel channel, String msg) {
        channel.writeAndFlush(
                MessageWrapper.newBuilder()
                        .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .setType(MessageWrapper.MessageType.CHAT)
                        .setChat(MessageChat.newBuilder()
                                .setText(msg)
                                .build())
                        .setCode(0)
                        .build()
        );
    }

    private void checkForAvailableLobby() {
        if (this.waitingPlayers.size() >= 4) {
            HashMap<Channel, Player> map = new HashMap<>();

            for (Entry<Channel, Player> player : this.waitingPlayers.entrySet()) {
                map.put(player.getKey(), player.getValue());
                if (map.size() == 4) break;
            }

            for (Channel channel : map.keySet()) {
                this.waitingPlayers.remove(channel);
            }

            this.createLobby(map);
        }
    }

    public void createLobby(HashMap<Channel, Player> map) {
        this.lobbies.add(new Lobby(this, map));
    }

    public HashMap<Channel, Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }
}
