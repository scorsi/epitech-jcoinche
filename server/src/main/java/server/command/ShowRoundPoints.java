package server.command;

import io.netty.channel.Channel;
import proto.Command;
import server.game.Card;
import server.game.Deck;
import server.game.Player;
import server.lobby.Lobby;

public class ShowRoundPoints implements ILobbyCommand {

    public void run(Channel channel, Command.LobbyCmd cmd, CommandManager commandManager) throws Exception {
        if (commandManager.getLobbyManager().getWaitingPlayers().containsKey(channel)) {
            throw new Exception();
        }
        Lobby lobby = commandManager.getLobbyManager().getLobbyByChannel(channel);
        lobby.getActualState().handleAction(channel, cmd);
    }

}
