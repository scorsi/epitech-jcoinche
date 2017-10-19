package server.command;

import io.netty.channel.Channel;
import proto.Command;

public class ShowCards implements ILobbyCommand {

    @Override
    public void run(Channel channel, Command.LobbyCmd cmd, CommandManager commandManager) throws Exception {

    }

}
