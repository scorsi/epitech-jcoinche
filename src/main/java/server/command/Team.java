package server.command;

import io.netty.channel.Channel;
import proto.Command.LobbyCmd;
import server.lobby.Lobby;

public class Team implements ILobbyCommand {

    @Override
    public void run(Channel channel, LobbyCmd cmd, CommandManager commandManager) throws Exception {
        if (commandManager.getLobbyManager().getWaitingPlayers().containsKey(channel)) {
            throw new Exception();
        }
        Lobby lobby = commandManager.getLobbyManager().getLobbyByChannel(channel);
    }

}
