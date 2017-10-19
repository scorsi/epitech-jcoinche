package server.game;

public enum Team {
    Undefined(""),
    Spectator("Spectator"),
    Red("Red"),
    Blue("Blue");

    private String name;

    Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
