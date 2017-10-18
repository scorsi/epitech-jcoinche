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

    public static CardColor from(int i) {
        int j = 0;

        for (CardColor card : values()) {
            if (j == i)
                return card;
            j++;
        }
        return Spade;
    }

    public String getName() {
        return name;
    }
}
