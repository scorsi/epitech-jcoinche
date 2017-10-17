package game;

import io.netty.channel.Channel;

import proto.Message.MessageWrapper;
import proto.Message.MessageChat;
import proto.Player.PlayerConnection;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map.Entry;

public class GamaManager {

    private static MessageWrapper.Builder messageWrapperBuilder = MessageWrapper.newBuilder();
    private static MessageChat.Builder messageChatBuilder = MessageChat.newBuilder();
    private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    private static int nbPlayer = 0;

    private LobbyManager lobbyManager = new LobbyManager();

    private HashMap<Channel, PlayerConnection> waitingPlayers = new HashMap<>();


    public GamaManager() {
    }

    public void handleAction(Channel channel, MessageWrapper msg) {
        if (msg.getType() == MessageWrapper.MessageType.PLAYER_CONNECTION) {
            this.changePlayer(channel, msg);
        } else if (msg.getType() == MessageWrapper.MessageType.CHAT) {
            this.lobbyManager.handleChat(channel, msg);
        }
    }

    public void connectPlayer(Channel channel) {
        ++nbPlayer;
        PlayerConnection player = PlayerConnection.newBuilder()
                .setTeam(PlayerConnection.TEAM.SPECTATOR)
                .setName("Player_" + nbPlayer)
                .build();

        this.waitingPlayers.put(channel, player);

        this.sendMsg(channel, "Welcome to the jCoinche server!\nYour name is " + player.getName() + ". You can change it by typing \"/username {name}\".");

        this.checkForAvailableLobby();
    }

    public void disconnectPlayer(Channel channel) {
        if (this.waitingPlayers.containsKey(channel))
            this.waitingPlayers.remove(channel);
        else
            this.lobbyManager.disconnectPlayer(channel);
    }

    private void sendMsg(Channel channel, String msg) {
        messageWrapperBuilder.setTimestamp(timestamp.getTime())
                .setCode(0)
                .setType(MessageWrapper.MessageType.CHAT)
                .setChat(messageChatBuilder.setText(msg).build());

        channel.writeAndFlush(messageWrapperBuilder.build());
    }

    private void changePlayer(Channel channel, MessageWrapper msg) {
        this.waitingPlayers.put(channel, msg.getPlayer());
        this.sendMsg(channel, "You're registered and you're waiting for a lobby.");
    }

    private void checkForAvailableLobby() {
        if (this.waitingPlayers.size() >= 4) {
            HashMap<Channel, PlayerConnection> map = new HashMap<>();

            for (Entry<Channel, PlayerConnection> player : this.waitingPlayers.entrySet()) {
                map.put(player.getKey(), player.getValue());
                if (map.size() == 4) break;
            }

            for (Channel channel : map.keySet()) {
                this.waitingPlayers.remove(channel);
            }

            this.lobbyManager.createLobby(map);
        }
    }

}
