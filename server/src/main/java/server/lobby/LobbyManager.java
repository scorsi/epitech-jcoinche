package server.lobby;

import io.netty.channel.Channel;

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
        Lobby lobbyToUpdate = this.getLobbyByChannel(channel);
        if (lobbyToUpdate != null) {
            lobbyToUpdate.checkState();
        }
    }

    public void putPlayerToWaitingList(Channel channel, Player player) {
        this.getWaitingPlayers().put(channel, player);
    }

    public void putPlayersToWaitingList(HashMap<Channel, Player> players) {
        this.getWaitingPlayers().putAll(players);
    }

    public void connectPlayer(Channel channel) {
        ++nbPlayer;
        Player player = new Player("Player_" + nbPlayer);

        this.getWaitingPlayers().put(channel, player);

        this.sendMsg(channel, "[SERVER] Welcome to the jCoinche server!\nYour name is " + player.getName() + ". You can change it by typing \"/username {name}\".");
    }

    public void disconnectPlayer(Channel channel) {
        if (this.getWaitingPlayers().containsKey(channel))
            this.getWaitingPlayers().remove(channel);
        else {
            Lobby lobby = this.getLobbyByChannel(channel);

            if (lobby == null)
                return;

            lobby.removePlayer(channel);
        }
    }

    public Lobby getLobbyByChannel(Channel channel) {
        Lobby lobbyToReturn = null;

        for (Lobby lobby : this.getLobbies()) {
            if (lobby.has(channel)) {
                lobbyToReturn = lobby;
                break;
            }
        }

        return lobbyToReturn;
    }

    public Lobby getLobbyByName(String name) {
        Lobby lobbyToReturn = null;

        for (Lobby lobby : this.getLobbies()) {
            if (lobby.getName().toLowerCase().equals(name.toLowerCase())) {
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

    public Lobby createLobby(String name) {
        this.getLobbies().add(new Lobby(this, name));
        return this.getLobbyByName(name);
    }

    public boolean movePlayer(Channel channel, Lobby lobby) {
        Player playerToMove = this.getWaitingPlayers().get(channel);
        if (playerToMove != null) {
            lobby.addPlayer(channel, playerToMove);
            this.getWaitingPlayers().remove(channel);
            return true;
        }
        return false;
    }

    public HashMap<Channel, Player> getWaitingPlayers() {
        return this.waitingPlayers;
    }

    public List<Lobby> getLobbies() {
        return this.lobbies;
    }

}
