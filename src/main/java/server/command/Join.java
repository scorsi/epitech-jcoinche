package server.command;

import io.netty.channel.Channel;
import proto.Command.GlobalCmd;
import server.lobby.Lobby;

public class Join implements IGlobalCommand {

    @Override
    public void run(Channel channel, GlobalCmd cmd, CommandManager commandManager) throws Exception {
        Lobby lobbyToJoin = commandManager.getLobbyManager().getLobbyByName(cmd.getValue());
        if (lobbyToJoin == null) {
            commandManager.sendMsg(channel, "[SERVER] The channel " + cmd.getValue() + " do not exists.");
            throw new Exception();
        }
        commandManager.getLobbyManager().movePlayer(channel, lobbyToJoin);
        System.out.println(channel.remoteAddress() + " has joined the channel " + cmd.getValue());
        commandManager.sendMsg(channel, "[SERVER] You joined the channel " + cmd.getValue());
    }

}
