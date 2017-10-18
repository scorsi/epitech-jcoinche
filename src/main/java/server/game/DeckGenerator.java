package server.game;

import java.util.ArrayList;
import java.util.List;

public class DeckGenerator {

    public List<Deck> generateAllDecks() {
        List<Deck> decks = new ArrayList<>();

        for (int i = 0; i < 4; ++i) {
            decks.add(this.generateOneDeck(decks));
        }
        return decks;
    }

    private boolean isCardAvailable(List<Deck> pastDecks, Deck actualDeck, Card cardToCheck) {
        for (Deck deck : pastDecks) {
            for (Card card : deck.getCards()) {
                if (card.getColorName().equals(cardToCheck.getColorName()) &&
                        card.getFaceName().equals(cardToCheck.getFaceName())) {
                    return true;
                }
            }
        }
        for (Card card : actualDeck.getCards()) {
            if (card.getColorName().equals(cardToCheck.getColorName()) &&
                    card.getFaceName().equals(cardToCheck.getFaceName())) {
                return true;
            }
        }
        return false;
    }

    private Deck generateOneDeck(List<Deck> pastDecks) {
        Deck deck = new Deck();
        for (int i = 0; i < 8; ++i) {
            Card card = null;
            boolean isAvailable = true;
            while (isAvailable) {
                int faceId = (int) ((Math.random() * 100) % 8);
                int colorId = (int) ((Math.random() * 100) % 4);
                CardFace face = CardFace.from(faceId);
                CardColor color = CardColor.from(colorId);
                card = new Card(face, color);
                isAvailable = isCardAvailable(pastDecks, deck, card);
            }
            deck.addCard(card);
        }
        return deck;
    }

}
