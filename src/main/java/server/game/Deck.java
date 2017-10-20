package server.game;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards = new ArrayList<>();

    public int foundCard(CardFace face, CardColor color) {
        int i = 0;
        for (Card card : this.getCards()) {
            if (card.getFace().equals(face) && card.getColor().equals(color)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

}
