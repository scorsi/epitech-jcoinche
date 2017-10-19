package server.command;

import io.netty.channel.Channel;
import proto.Command;

public class ListPlayer implements ILobbyCommand {

    @Override
    public void run(Channel channel, Command.LobbyCmd cmd, CommandManager commandManager) throws Exception {

    }

}
