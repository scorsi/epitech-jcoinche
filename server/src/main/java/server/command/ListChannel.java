package server.command;

import io.netty.channel.Channel;
import proto.Command;
import server.lobby.Lobby;

public class ListChannel implements IGlobalCommand {

    public void run(Channel channel, Command.GlobalCmd cmd, CommandManager commandManager) throws Exception {
        if (commandManager.getLobbyManager().getLobbies().size() <= 0) {
            commandManager.sendMsg(channel, "[SERVER] There is no channel.");
        } else {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("[SERVER] List of channels:");
            for (Lobby lobby : commandManager.getLobbyManager().getLobbies()) {
                stringBuilder.append("\n-> ")
                        .append(lobby.getName())
                        .append(" (")
                        .append(lobby.getNumberOfPlayers())
                        .append("/4)");
            }

            commandManager.sendMsg(channel, stringBuilder.toString());
        }
    }

}
