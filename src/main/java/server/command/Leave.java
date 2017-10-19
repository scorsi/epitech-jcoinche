package server.command;

import io.netty.channel.Channel;
import proto.Command;
import server.lobby.Lobby;

public class Leave implements ILobbyCommand {

    @Override
    public void run(Channel channel, Command.LobbyCmd cmd, CommandManager commandManager) throws Exception {
        Lobby lobbyToLeave = commandManager.getLobbyManager().getLobbyByChannel(channel);

        if (lobbyToLeave == null) {
            commandManager.sendMsg(channel, "[SERVER] You're not in a lobby.");
            return;
        }

        lobbyToLeave.removePlayer(channel);
        commandManager.sendMsg(channel, "[SERVER] You successfully left the channel " + lobbyToLeave.getName() + ".");
    }

}
