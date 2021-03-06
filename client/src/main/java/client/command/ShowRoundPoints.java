package client.command;

import io.netty.channel.Channel;
import proto.Command.LobbyCmd;
import proto.Message;

import java.sql.Timestamp;

public class ShowRoundPoints implements ICommand {

    public void run(String msg, Channel serverChannel) throws Exception {
        serverChannel.writeAndFlush(
                Message.MessageWrapper.newBuilder()
                        .setType(Message.MessageWrapper.MessageType.LOBBY_CMD)
                        .setLobbyCmd(LobbyCmd.newBuilder()
                                .setCmdType(LobbyCmd.CmdType.SHOW_ROUND_POINTS)
                                .setValue("")
                                .build())
                        .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .setCode(0)
                        .build()
        );
    }

}
