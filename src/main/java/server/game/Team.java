package server.game;

public enum Team {
    Undifined(""),
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
