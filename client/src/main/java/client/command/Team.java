package client.command;

import io.netty.channel.Channel;
import proto.Command.LobbyCmd;
import proto.Message;

import java.sql.Timestamp;

public class Team implements ICommand {

    public void run(String msg, Channel serverChannel) throws Exception {
        String team = msg.split("\\s+")[1].toUpperCase();
        if (team.equals("RED") || team.equals("BLUE"))
            serverChannel.writeAndFlush(
                Message.MessageWrapper.newBuilder()
                        .setType(Message.MessageWrapper.MessageType.LOBBY_CMD)
                        .setLobbyCmd(LobbyCmd.newBuilder()
                                .setCmdType(LobbyCmd.CmdType.TEAM)
                                .setValue(team)
                                .build())
                        .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .setCode(0)
                        .build()
            );
        else
            throw new Exception();
    }

}
