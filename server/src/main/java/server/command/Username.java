package server.command;

import io.netty.channel.Channel;
import proto.Command.GlobalCmd;
import server.lobby.Lobby;
import server.game.Player;

public class Username implements IGlobalCommand {

    public void run(Channel channel, GlobalCmd cmd, CommandManager commandManager) throws Exception {
        if (commandManager.getLobbyManager().getWaitingPlayers().containsKey(channel)) {
            commandManager.getLobbyManager().getWaitingPlayers().get(channel).setName(cmd.getValue());
            commandManager.sendMsg(channel, "[Server] Your username has been changed to " + cmd.getValue() + ".");
        } else {
            Lobby lobby = commandManager.getLobbyManager().getLobbyByChannel(channel);
            if (lobby == null) return;
            Player player = lobby.getPlayer(channel);
            if (player == null) return;
            lobby.broadcast(player.getName() + " has changed his username to " + cmd.getValue() + ".", null);
            player.setName(cmd.getValue());
        }
    }

}
