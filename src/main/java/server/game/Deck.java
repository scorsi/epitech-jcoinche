package server.game;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards = new ArrayList<>();

    public List<Card> getCards() {
        return this.cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

}
