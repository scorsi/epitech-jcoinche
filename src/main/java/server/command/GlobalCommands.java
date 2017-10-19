package server.command;

import proto.Command.GlobalCmd.CmdType;

public enum GlobalCommands {

    Username(CmdType.USERNAME, Username.class),
    Create(CmdType.CREATE, Create.class),
    Join(CmdType.JOIN, Join.class),
    ListChannel(CmdType.LIST_CHANNEL, ListChannel.class);


    private CmdType cmdType;
    private Class command;


    GlobalCommands(CmdType cmdType, Class command) {
        this.cmdType = cmdType;
        this.command = command;
    }

    public static Class from(CmdType cmdType) throws Exception {
        for (GlobalCommands command : GlobalCommands.values()) {
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
