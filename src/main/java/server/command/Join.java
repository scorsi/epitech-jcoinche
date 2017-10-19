package server.command;

import io.netty.channel.Channel;
import proto.Command.GlobalCmd;
import server.lobby.Lobby;

public class Join implements IGlobalCommand {

    public void run(Channel channel, GlobalCmd cmd, CommandManager commandManager) {
        if (!commandManager.getLobbyManager().getWaitingPlayers().containsKey(channel)) {
            commandManager.sendMsg(channel, "[SERVER] You must leave your lobby before you can join another.");
            return;
        }
        Lobby lobbyToJoin = commandManager.getLobbyManager().getLobbyByName(cmd.getValue());
        if (lobbyToJoin == null) {
            commandManager.sendMsg(channel, "[SERVER] The channel " + cmd.getValue() + " do not exists.");
            return;
        }
        if (lobbyToJoin.getNumberOfPlayers() >= 4) {
            commandManager.sendMsg(channel, "[SERVER] The channel " + cmd.getValue() + " is full, you can't join it.");
            return;
        }
        System.out.println(channel.remoteAddress() + " has joined the channel " + cmd.getValue());
        commandManager.sendMsg(channel, "[SERVER] You joined the channel " + cmd.getValue() + ".");
        commandManager.getLobbyManager().movePlayer(channel, lobbyToJoin);
    }

}
