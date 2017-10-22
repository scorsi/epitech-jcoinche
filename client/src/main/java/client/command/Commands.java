package client.command;

public enum Commands {

    Username(new String[]{"/username", "/name", "/u", "/n"}, Username.class),
    Team(new String[]{"/team", "/t"}, Team.class),
    Contract(new String[]{"/put-contract", "/contract"}, Contract.class),
    Create(new String[]{"/create", "/c"}, Create.class),
    Join(new String[]{"/join", "/j"}, Join.class),
    ListPlayers(new String[]{"/list-players", "/players"}, ListPlayers.class),
    ListChannel(new String[]{"/list-channels", "/channels"}, ListChannel.class),
    Leave(new String[]{"/leave", "/l"}, Leave.class),
    ShowCards(new String[]{"/show-cards", "/cards"}, ShowCards.class),
    ShowTable(new String[]{"/show-table", "/table"}, ShowTable.class),
    PlayCard(new String[]{"/play", "/p"}, PlayCard.class),
    ShowContract(new String[]{"/show-contract"}, ShowContract.class),
    ShowPoints(new String[]{"/show-points"}, ShowPoints.class);

    private String[] aliases;
    private Class command;


    Commands(String[] aliases, Class command) {
        this.aliases = aliases;
        this.command = command;
    }

    public static Class from(String text) throws Exception {
        for (Commands command : Commands.values()) {
            for (String alias : command.getAliases()) {
                if (text.split("\\s+")[0].equals(alias)) {
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
