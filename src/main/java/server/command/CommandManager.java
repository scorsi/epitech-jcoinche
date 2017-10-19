package server.command;

import proto.Command.LobbyCmd;
import proto.Command.GlobalCmd;
import proto.Message.MessageChat;
import proto.Message.MessageWrapper;
import server.lobby.Lobby;
import server.lobby.LobbyManager;

import io.netty.channel.Channel;

import java.sql.Timestamp;

public class CommandManager {

    private LobbyManager lobbyManager;

    public CommandManager(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    public void sendMsg(Channel channel, String msg) {
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

    public void handle(Channel channel, MessageWrapper msg) {
        if (msg.getType().equals(MessageWrapper.MessageType.GLOBAL_CMD) && msg.hasGlobalCmd()) {
            this.handleGlobalCmd(channel, msg.getGlobalCmd());
        } else if (msg.getType().equals(MessageWrapper.MessageType.LOBBY_CMD) && msg.hasLobbyCmd()) {
            this.handleLobbyCmd(channel, msg.getLobbyCmd());
        } else if (msg.getType().equals(MessageWrapper.MessageType.CHAT) && msg.hasChat()) {
            this.handleChat(channel, msg.getChat());
        }
    }

    private void handleChat(Channel channel, MessageChat chat) {
        if (this.getLobbyManager().getWaitingPlayers().containsKey(channel)) {
            this.sendMsg(channel, "[Server] You can't chat when you're not in a lobby. Please wait.");
        } else {
            for (Lobby lobby : this.getLobbyManager().getLobbies()) {
                if (lobby.has(channel)) {
                    lobby.broadcast(chat.getText(), channel);
                    break;
                }
            }
        }
    }

    private void handleLobbyCmd(Channel channel, LobbyCmd cmd) {
        try {
            ((ILobbyCommand)(LobbyCommands.from(cmd.getCmdType()).newInstance())).run(channel, cmd, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGlobalCmd(Channel channel, GlobalCmd cmd) {
        try {
            ((IGlobalCommand)(GlobalCommands.from(cmd.getCmdType()).newInstance())).run(channel, cmd, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LobbyManager getLobbyManager() {
        return this.lobbyManager;
    }

}
