package server.command;

import io.netty.channel.Channel;
import proto.Command;
import server.game.Player;
import server.game.Team;

public class ListPlayers implements ILobbyCommand {

    public void run(Channel channel, Command.LobbyCmd cmd, CommandManager commandManager) throws Exception {
        if (commandManager.getLobbyManager().getWaitingPlayers().containsKey(channel)) {
            throw new Exception();
        }
        else {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("[SERVER] List of players:");
            for (Player player : commandManager.getLobbyManager().getLobbyByChannel(channel).getPlayers()) {
                if (player.getTeam() == Team.Undifined) {
                    stringBuilder.append("\n-> ")
                            .append(player.getName())
                            .append(": No team");

                } else {
                    stringBuilder.append("\n-> ")
                            .append(player.getName())
                            .append(": Team ")
                            .append(player.getTeam());
                }
            }

            commandManager.sendMsg(channel, stringBuilder.toString());
        }
    }

}
