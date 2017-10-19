package server.command;

import io.netty.channel.Channel;
import proto.Command;
import server.game.Card;
import server.game.Deck;
import server.game.Player;

public class ShowCards implements ILobbyCommand {

    public void run(Channel channel, Command.LobbyCmd cmd, CommandManager commandManager) throws Exception {
        if (commandManager.getLobbyManager().getWaitingPlayers().containsKey(channel)) {
            throw new Exception();
        }
        else {
            Deck deck = commandManager.getLobbyManager().getLobbyByChannel(channel).getPlayer().getDeck();

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("[SERVER] List of your cards:");
            for (Card card : deck.getCards()) {
                stringBuilder.append("\n-> ")
                        .append(card.getFaceName() + " of " + card.getColorName());
            }

            commandManager.sendMsg(channel, stringBuilder.toString());
        }
    }

}
