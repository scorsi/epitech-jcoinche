package client.command;

import io.netty.channel.Channel;
import proto.Command.GlobalCmd;
import proto.Message;

import java.sql.Timestamp;

public class Create implements ICommand {

    public void run(String msg, Channel serverChannel) throws Exception {
        serverChannel.writeAndFlush(
                Message.MessageWrapper.newBuilder()
                        .setType(Message.MessageWrapper.MessageType.GLOBAL_CMD)
                        .setGlobalCmd(GlobalCmd.newBuilder()
                                .setCmdType(GlobalCmd.CmdType.CREATE)
                                .setValue(msg.split("\\s+")[1])
                                .build())
                        .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .setCode(0)
                        .build()
        );
    }

}
