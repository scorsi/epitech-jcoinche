package server;

import io.netty.channel.Channel;

import proto.Command.GlobalCmd;
import proto.Command.LobbyCmd;
import proto.Message.MessageWrapper;
import proto.Message.MessageChat;
import server.game.Player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class LobbyManager {

    private static int nbPlayer = 0;

    private HashMap<Channel, Player> waitingPlayers = new HashMap<>();
    private List<Lobby> lobbies = new ArrayList<>();


    public LobbyManager() {
    }

    public void handleAction(Channel channel, MessageWrapper msg) {
        if (msg.getType() == MessageWrapper.MessageType.GLOBAL_CMD && msg.hasGlobalCmd()) {
            this.handleGlobalCmd(channel, msg.getGlobalCmd());
        } else if (msg.getType() == MessageWrapper.MessageType.LOBBY_CMD && msg.hasLobbyCmd()) {
            this.handleLobbyCmd(channel, msg.getLobbyCmd());
        } else if (msg.getType() == MessageWrapper.MessageType.CHAT && msg.hasChat()) {
            this.handleChat(channel, msg.getChat());
        }
    }

    private void handleChat(Channel channel, MessageChat chat) {
        if (this.waitingPlayers.containsKey(channel)) {
            this.sendMsg(channel, "[Server] You can't chat when you're not in a lobby. Please wait.");
        } else {
            for (Lobby lobby : lobbies) {
                if (lobby.has(channel)) {
                    lobby.broadcast(chat.getText(), channel);
                    break;
                }
            }
        }
    }

    private void handleLobbyCmd(Channel channel, LobbyCmd cmd) {

    }

    private void handleGlobalCmd(Channel channel, GlobalCmd cmd) {
        if (cmd.getCmd() == GlobalCmd.Cmd.USERNAME) {
            this.changePlayer(channel, cmd.getValue());
        }
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

    private Lobby getLobbyByChannel(Channel channel) {
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

    private void changePlayer(Channel channel, String name) {
        if (this.waitingPlayers.containsKey(channel)) {
            this.waitingPlayers.get(channel).setName(name);
            this.sendMsg(channel, "[Server] Your username has been changed to " + name + ".");
        } else {
            Lobby lobby = this.getLobbyByChannel(channel);
            if (lobby == null) return;
            Player player = lobby.getPlayer(channel);
            if (player == null) return;
            lobby.broadcast(player.getName() + " has changed his username to " + name + ".", null);
            player.setName(name);
        }
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

}
