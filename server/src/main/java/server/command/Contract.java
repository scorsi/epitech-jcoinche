package server.command;

import io.netty.channel.Channel;
import proto.Command;
import server.lobby.Lobby;

public class Contract implements ILobbyCommand {

    public void run(Channel channel, Command.LobbyCmd cmd, CommandManager commandManager) throws Exception {
        if (commandManager.getLobbyManager().getWaitingPlayers().containsKey(channel)) {
            throw new Exception();
        }
        Lobby lobby = commandManager.getLobbyManager().getLobbyByChannel(channel);
        lobby.getActualState().handleAction(channel, cmd);
    }

}
