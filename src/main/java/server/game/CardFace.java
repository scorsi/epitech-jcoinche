package server.game;

public enum CardFace {

    Seven("7"),
    Eight("8"),
    Nine("9"),
    Ten("10"),
    Jack("Jack"),
    Queen("Queen"),
    King("King"),
    As("As");

    private String name;

    CardFace(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
