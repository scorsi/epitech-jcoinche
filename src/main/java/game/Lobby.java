package game;

import proto.Message.MessageWrapper;
import proto.Message.MessageChat;
import proto.Player.PlayerConnection;

import io.netty.channel.Channel;

import java.sql.Timestamp;
import java.util.HashMap;

public class Lobby {

    private static MessageWrapper.Builder messageWrapperBuilder = MessageWrapper.newBuilder();
    private static MessageChat.Builder messageChatBuilder = MessageChat.newBuilder();
    private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    private HashMap<Channel, PlayerConnection> players;
    private boolean isShuttingDown = false;

    Lobby(HashMap<Channel, PlayerConnection> incomingPlayers) {
        this.players = incomingPlayers;

        this.broadcast("You have been moved to a lobby. The game will start shortly.", null);
    }

    public boolean has(Channel channel) {
        return this.players.containsKey(channel);
    }

    public void shutdown(Channel disconnectedChannel) {
        if (this.isShuttingDown)
            return;
        this.isShuttingDown = true;

        this.broadcast("You have been kicked from the server because a player left the game.", disconnectedChannel);

        for (Channel channel : this.players.keySet()) {
            try {
                channel.close().sync();
            } catch (Exception e) {
                // Do something
            }
        }
        this.players.clear();
    }

    public void broadcast(String msg, Channel incomingChannel) {
        timestamp.setTime(System.currentTimeMillis());

        for (Channel channel : this.players.keySet()) {
            if (incomingChannel == null || channel.remoteAddress() != incomingChannel.remoteAddress()) {
                messageWrapperBuilder.setTimestamp(timestamp.getTime())
                        .setType(MessageWrapper.MessageType.CHAT)
                        .setChat(messageChatBuilder.setText(msg).build())
                        .setCode(0);

                channel.writeAndFlush(messageWrapperBuilder.build());
            }
        }
    }

}
