package server.game;

import io.netty.channel.socket.nio.NioServerSocketChannel;
import proto.Command;
import server.Server;

public enum CardFace {

    Seven("7", 0, 0, 0, 0),
    Eight("8", 0, 0, 0, 0),
    Nine("9", 9, 0, 14, 0),
    Ten("10", 5, 10, 10, 10),
    Jack("Jack", 14, 2, 20, 2),
    Queen("Queen", 2, 3, 3, 3),
    King("King", 3, 4, 4, 4),
    As("As", 7, 19, 11, 11);


    private String name;
    private int pointAllTrump;
    private int pointNoTrump;
    private int pointOneTrump;
    private int pointIsNotTrump;

    CardFace(String name, int pointAllTrump, int pointNoTrump, int pointOneTrump, int pointIsNotTrump) {
        this.name = name;
        this.pointAllTrump = pointAllTrump;
        this.pointNoTrump = pointNoTrump;
        this.pointOneTrump = pointOneTrump;
        this.pointIsNotTrump = pointIsNotTrump;
    }

    public static CardFace from(Command.Card.Face face) {
        switch (face) {
            case AS:
                return As;
            case SEVEN:
                return Seven;
            case EIGHT:
                return Eight;
            case NINE:
                return Nine;
            case TEN:
                return Ten;
            case JACK:
                return Jack;
            case QUEEN:
                return Queen;
            case KING:
                return King;
        }
        return null;
    }

    public static CardFace from(int i) {
        int j = 0;

        for (CardFace card : values()) {
            if (j == i)
                return card;
            j++;
        }
        return As;
    }

    public String getName() {
        return name;
    }

    public int getPointAllTrump() {
        return pointAllTrump;
    }

    public int getPointNoTrump() {
        return pointNoTrump;
    }

    public int getPointOneTrump() {
        return pointOneTrump;
    }

    public int getPointIsNotTrump() {
        return pointIsNotTrump;
    }

}
