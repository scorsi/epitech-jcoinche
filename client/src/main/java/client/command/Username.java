package client.command;

import io.netty.channel.Channel;
import proto.Command.GlobalCmd;
import proto.Message.MessageWrapper;

import java.sql.Timestamp;

public class Username implements ICommand {

    public void run(String msg, Channel serverChannel) throws Exception {
        serverChannel.writeAndFlush(
                MessageWrapper.newBuilder()
                        .setType(MessageWrapper.MessageType.GLOBAL_CMD)
                        .setGlobalCmd(GlobalCmd.newBuilder()
                                .setCmdType(GlobalCmd.CmdType.USERNAME)
                                .setValue(msg.split("\\s+")[1])
                                .build())
                        .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .setCode(0)
                        .build()
        );
    }

}
