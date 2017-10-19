package server.command;

import io.netty.channel.Channel;
import proto.Command.GlobalCmd;
import server.lobby.Lobby;

public class Create implements IGlobalCommand {

    @Override
    public void run(Channel channel, GlobalCmd cmd, CommandManager commandManager) throws Exception {
        if (!commandManager.getLobbyManager().getWaitingPlayers().containsKey(channel)) {
            commandManager.sendMsg(channel, "[SERVER] You must leave your lobby before you can create one.");
            return;
        }
        if (commandManager.getLobbyManager().getLobbyByName(cmd.getValue()) != null) {
            commandManager.sendMsg(channel, "[SERVER] The channel " + cmd.getValue() + " is already created.");
        } else {
            Lobby lobby = commandManager.getLobbyManager().createLobby(cmd.getValue());
            if (commandManager.getLobbyManager().movePlayer(channel, lobby)) {
                System.out.println(channel.remoteAddress() + " created the channel " + cmd.getValue());
                commandManager.sendMsg(channel, "[SERVER] You created and joined the channel " + cmd.getValue());
            } else {
                commandManager.sendMsg(channel, "[SERVER] You cant join the channel " + cmd.getValue() + ".");
            }
        }
    }

}
