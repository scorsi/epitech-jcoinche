package server;

import proto.Message.MessageWrapper;
import proto.Message.MessageChat;

import io.netty.channel.Channel;
import server.game.Player;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Collection;

public class Lobby {

    private LobbyManager lobbyManager;

    private HashMap<Channel, Player> players;
    private boolean isShuttingDown = false;

    Lobby(LobbyManager lobbyManager, HashMap<Channel, Player> incomingPlayers) {
        this.lobbyManager = lobbyManager;
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

        this.broadcast("You have been moved to the waiting list because a player left the game.", disconnectedChannel);

        this.getLobbyManager().putPlayersToWaitingList(this.players);

        this.players.clear();
    }

    public void broadcast(String msg, Channel incomingChannel) {
        String name = "Server";
        if (incomingChannel != null)
            name = this.players.get(incomingChannel).getName();

        for (Channel channel : this.players.keySet()) {
            if (incomingChannel == null || channel.remoteAddress() != incomingChannel.remoteAddress()) {
                channel.writeAndFlush(
                        MessageWrapper.newBuilder()
                                .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                                .setType(MessageWrapper.MessageType.CHAT)
                                .setChat(MessageChat.newBuilder()
                                        .setText("[" + name + "] " + msg)
                                        .build())
                                .setCode(0)
                                .build()
                );
            }
        }
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }

    public Player getPlayer(Channel channel) {
        return this.players.get(channel);
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }
}
