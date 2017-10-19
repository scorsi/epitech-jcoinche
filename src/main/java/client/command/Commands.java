package client.command;

public enum Commands {

    Username(new String[]{"/username", "/name", "/u"}, Username.class),
    Team(new String[]{"/team", "/t"}, Team.class),
    Create(new String[]{"/create", "/c"}, Create.class),
    Join(new String[]{"/join", "/j"}, Join.class),
    ListChannel(new String[]{"/list-channel"}, ListChannel.class),
    Leave(new String[]{"/leave", "/l"}, Leave.class),
    ShowCards(new String[]{"/show-cards"}, ShowCards.class);

    private String[] aliases;
    private Class command;


    Commands(String[] aliases, Class command) {
        this.aliases = aliases;
        this.command = command;
    }

    public static Class from(String text) throws Exception {
        for (Commands command : Commands.values()) {
            for (String alias : command.getAliases()) {
                if (text.startsWith(alias)) {
                    return command.getCommand();
                }
            }
        }
        throw new Exception();
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public Class getCommand() {
        return this.command;
    }

}
