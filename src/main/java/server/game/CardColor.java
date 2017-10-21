package server.game;

import proto.Command;

public enum CardColor {

    Club("Club"),
    Diamond("Diamond"),
    Heart("Heart"),
    Spade("Spade");


    private String name;

    CardColor(String name) {
        this.name = name;
    }

    public static CardColor from(Command.Card.Color color) {
        switch (color) {
            case CLUB:
                return Club;
            case HEART:
                return Heart;
            case SPADE:
                return Spade;
            case DIAMOND:
                return Diamond;
        }
        return null;
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
