package client.command;

import io.netty.channel.Channel;
import proto.Command;
import proto.Message.MessageWrapper;

import java.sql.Timestamp;

public class Username implements ICommand {

    public void run(String msg, Channel serverChannel) {
        serverChannel.writeAndFlush(
                MessageWrapper.newBuilder()
                        .setType(MessageWrapper.MessageType.GLOBAL_CMD)
                        .setGlobalCmd(Command.GlobalCmd.newBuilder()
                                .setCmd(Command.GlobalCmd.Cmd.USERNAME)
                                .setValue(msg.split("\\s+")[1])
                                .build())
                        .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .setCode(0)
                        .build()
        );
    }

}
