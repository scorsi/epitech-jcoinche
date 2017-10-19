package client.command;

import io.netty.channel.Channel;

public interface ICommand {

    void run(String msg, Channel serverChannel) throws Exception;

}
