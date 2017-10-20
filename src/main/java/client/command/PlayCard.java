package client.command;

import io.netty.channel.Channel;
import proto.Command;
import proto.Message;

import java.sql.Timestamp;

public class PlayCard implements ICommand {

    public void run(String msg, Channel serverChannel) throws Exception {
        String[] args = msg.split("\\s+");
        if (args.length < 3) {
            return; // ERROR
        }

        Command.Card.Face face;
        switch (args[1].toUpperCase()) {
            case "AS" :
                face = Command.Card.Face.AS;
                break;
            case "KING" :
                face = Command.Card.Face.KING;
                break;
            case "QUEEN" :
                face = Command.Card.Face.QUEEN;
                break;
            case "JACK" :
                face = Command.Card.Face.JACK;
                break;
            case "TEN" :
                face = Command.Card.Face.TEN;
                break;
            case "NINE" :
                face = Command.Card.Face.NINE;
                break;
            case "EIGHT" :
                face = Command.Card.Face.EIGHT;
                break;
            case "SEVEN" :
                face = Command.Card.Face.SEVEN;
                break;
            default:
                return; // ERROR
        }

        Command.Card.Color color;
        switch (args[2].toUpperCase()) {
            case "SPADE" :
                color = Command.Card.Color.SPADE;
                break;
            case "HEART" :
                color = Command.Card.Color.HEART;
                break;
            case "CLUB" :
                color = Command.Card.Color.CLUB;
                break;
            case "DIAMOND" :
                color = Command.Card.Color.DIAMOND;
                break;
            default:
                return; // ERROR
        }

        serverChannel.writeAndFlush(
                Message.MessageWrapper.newBuilder()
                        .setType(Message.MessageWrapper.MessageType.LOBBY_CMD)
                        .setLobbyCmd(Command.LobbyCmd.newBuilder()
                                .setCmdType(Command.LobbyCmd.CmdType.PLAY_CARD)
                                .setCard(Command.Card.newBuilder()
                                        .setFace(face)
                                        .setColor(color)
                                        .build())
                                .build())
                        .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .setCode(0)
                        .build()
        );
    }
}
