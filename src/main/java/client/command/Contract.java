package client.command;

import io.netty.channel.Channel;
import proto.Command;
import proto.Message;

import java.sql.Timestamp;

public class Contract implements ICommand {

    public void run(String msg, Channel serverChannel) throws Exception {
        String[] args = msg.split("\\s+");
        if (args.length < 2) {
            throw new Exception();
        }

        Command.Contract.Type type;
        switch (args[1].toUpperCase()) {
            case "SPADE" :
                type = Command.Contract.Type.SPADE;
                break;
            case "HEART" :
                type = Command.Contract.Type.HEART;
                break;
            case "CLUB" :
                type = Command.Contract.Type.CLUB;
                break;
            case "DIAMOND" :
                type = Command.Contract.Type.DIAMOND;
                break;
            case "COINCHE" :
                type = Command.Contract.Type.COINCHE;
                break;
            case "ALLTRUMP" :
            case "ALL_TRUMP" :
                type = Command.Contract.Type.ALL_TRUMP;
                break;
            case "NOTRUMP" :
            case "NO_TRUMP" :
                type = Command.Contract.Type.NO_TRUMP;
                break;
            case "PASS" :
                type = Command.Contract.Type.PASS;
                break;
            default:
                throw new Exception();
        }

        int value = 0;
        if (type != Command.Contract.Type.PASS) {
            value = (Integer.parseInt(args[2]) / 10) * 10;
            if (value < 80 || value > 160) {
                throw new Exception();
            }
        }

        serverChannel.writeAndFlush(
                Message.MessageWrapper.newBuilder()
                        .setType(Message.MessageWrapper.MessageType.LOBBY_CMD)
                        .setLobbyCmd(Command.LobbyCmd.newBuilder()
                                .setCmdType(Command.LobbyCmd.CmdType.CONTRACT)
                                .setContract(Command.Contract.newBuilder()
                                        .setType(type)
                                        .setValue(value)
                                        .build())
                                .build())
                        .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .setCode(0)
                        .build()
        );
    }

}
