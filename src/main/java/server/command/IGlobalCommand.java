package server.command;

import io.netty.channel.Channel;
import proto.Command.GlobalCmd;
import server.command.CommandManager;

public interface IGlobalCommand {

    void run(Channel channel, GlobalCmd cmd, CommandManager commandManager) throws Exception;

}
