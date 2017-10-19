package server.lobby;

import proto.Message.MessageWrapper;
import proto.Message.MessageChat;

import io.netty.channel.Channel;
import server.game.Player;
import server.lobby.state.AState;
import server.lobby.state.WaitingState;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Collection;

public class Lobby {

    private LobbyManager lobbyManager;

    private HashMap<Channel, Player> players;
    private boolean isShuttingDown = false;
    private AState actualState = null;
    private String name;

    Lobby(LobbyManager lobbyManager, String name) {
        this.lobbyManager = lobbyManager;
        this.name = name;
        this.players = new HashMap<>();
        this.actualState = new WaitingState(this).initialize();
    }

    public void checkState() {
        if (this.actualState.isFinished()) {
            this.actualState = this.actualState.getNextState().initialize();
            this.checkState();
        }
    }

    public boolean has(Channel channel) {
        return this.players.containsKey(channel);
    }

    public void addPlayer(Channel channel, Player player) {
        this.broadcast(player.getName() + " joined the lobby.", null);
        this.players.put(channel, player);
        this.checkState();
    }

    public void removePlayer(Channel channel) {
        Player playerToRemove = this.getPlayer(channel);
        if (playerToRemove == null)
            return;
        this.getLobbyManager().putPlayerToWaitingList(channel, playerToRemove);
        this.players.remove(channel);

        if (this.players.size() == 0) {
            this.shutdown();
            this.getLobbyManager().getLobbies().remove(this);
        } else {
            this.broadcast(playerToRemove.getName() + " left the game.", null);
        }
    }

    public void shutdown() {
        if (this.isShuttingDown)
            return;
        this.isShuttingDown = true;

        this.getLobbyManager().putPlayersToWaitingList(this.players);

        this.players.clear();

        System.out.println("Lobby " + this.getName() + " has been automatically destroyed.");
    }

    private void sendMsg(String msg, Channel channel) {
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

    public void broadcast(String msg, Channel incomingChannel) {
        String name = "Server";
        if (incomingChannel != null)
            name = this.players.get(incomingChannel).getName();

        for (Channel channel : this.players.keySet()) {
            if (incomingChannel == null || channel.remoteAddress() != incomingChannel.remoteAddress()) {
                this.sendMsg("[" + name + "] " + msg, channel);
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

    public AState getActualState() {
        return actualState;
    }

    public String getName() {
        return name;
    }
}
