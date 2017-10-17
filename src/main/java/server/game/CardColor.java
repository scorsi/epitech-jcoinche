package server.game;

public enum CardColor {

    Club("Club"),
    Diamond("Diamond"),
    Heart("Heart"),
    Spade("Spade");

    private String name;

    CardColor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
