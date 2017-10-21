package server.game;

public class Card {

    private CardFace face;
    private CardColor color;

    Card(CardFace face, CardColor color) {
        this.face = face;
        this.color = color;
    }

    public CardFace getFace() {
        return this.face;
    }

    public CardColor getColor() {
        return this.color;
    }

    public String getColorName() {
        return this.getColor().getName();
    }

    public String getFaceName() {
        return this.getFace().getName();
    }

    public int getPointAllTrump() {
        return this.getFace().getPointAllTrump();
    }

    public int getPointNoTrump() {
        return this.getFace().getPointNoTrump();
    }

    public int getPointOneTrump() {
        return this.getFace().getPointOneTrump();
    }

    public int getPointIsNotTrump() {
        return this.getFace().getPointIsNotTrump();
    }

}
