package server.command;

import io.netty.channel.Channel;
import proto.Command.LobbyCmd;

public interface ILobbyCommand {

    void run(Channel channel, LobbyCmd cmd, CommandManager commandManager) throws Exception;

}
