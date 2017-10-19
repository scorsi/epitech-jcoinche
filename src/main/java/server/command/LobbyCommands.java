package server.command;

import proto.Command.LobbyCmd.CmdType;

public enum LobbyCommands {

    Team(CmdType.TEAM, Team.class),
    Leave(CmdType.LEAVE, Leave.class),
    ListPlayers(CmdType.LIST_PLAYERS, ListPlayers.class),
    ShowCards(CmdType.SHOW_CARDS, ShowCards.class);



    private CmdType cmdType;
    private Class command;


    LobbyCommands(CmdType cmdType, Class command) {
        this.cmdType = cmdType;
        this.command = command;
    }

    public static Class from(CmdType cmdType) throws Exception {
        for (LobbyCommands command : LobbyCommands.values()) {
            if (command.getCmdType().equals(cmdType))
                return command.getCommand();
        }
        throw new Exception();
    }

    public CmdType getCmdType() {
        return this.cmdType;
    }

    public Class getCommand() {
        return this.command;
    }

}
