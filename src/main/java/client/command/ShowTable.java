package client.command;

import io.netty.channel.Channel;
import proto.Command;
import proto.Message;

import java.sql.Timestamp;

public class ShowTable implements ICommand {

    public void run(String msg, Channel serverChannel) throws Exception {
            serverChannel.writeAndFlush(
                    Message.MessageWrapper.newBuilder()
                            .setType(Message.MessageWrapper.MessageType.LOBBY_CMD)
                            .setLobbyCmd(Command.LobbyCmd.newBuilder()
                                    .setCmdType(Command.LobbyCmd.CmdType.SHOW_TABLE)
                                    .setValue("")
                                    .build())
                            .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                            .setCode(0)
                            .build()
            );
    }
}
