package maksunova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Test
    void newDeckContains52Cards() {
        Deck deck = new Deck();
        assertEquals(52, deck.size());
    }

    @Test
    void pullingCardShouldDecreaseDeckSize() {
        Deck deck = new Deck();
        deck.pullout();

        assertEquals(51, deck.size());
    }

    @Test
    void pullingAllCardsShouldLeaveEmptyDeck() {
        Deck deck = new Deck();

        for (int i = 0; i < 52; i++) {
            deck.pullout();
        }
        assertEquals(0, deck.size());
    }

    @Test
    void pullingCardFromEmptyDeckShouldThrowException() {
        Deck deck = new Deck();

        for (int i = 0; i < 52; i++) {
            deck.pullout();
        }
        assertThrows( IllegalStateException.class, () -> deck.pullout() );
    }

    @Test
    void deckShouldContainAllUniqueCards() {
        Deck deck = new Deck();
        int sumcards = 0;

        int rankval = 0, suitval = 0;

        for (int i = 0; i < 52; i++) {
            Card card = deck.pullout();

            if (card.getRank() == Rank.TWO) { rankval = 0; }
            if (card.getRank() == Rank.THREE) { rankval = 1; }
            if (card.getRank() == Rank.FOUR) { rankval = 2; }
            if (card.getRank() == Rank.FIVE) { rankval = 3; }
            if (card.getRank() == Rank.SIX) { rankval = 4; }
            if (card.getRank() == Rank.SEVEN) { rankval = 5; }
            if (card.getRank() == Rank.EIGHT) { rankval = 6; }
            if (card.getRank() == Rank.NINE) { rankval = 7; }
            if (card.getRank() == Rank.TEN) { rankval = 8; }
            if (card.getRank() == Rank.JACK) { rankval = 9; }
            if (card.getRank() == Rank.QUEEN) { rankval = 10; }
            if (card.getRank() == Rank.KING) { rankval = 11; }
            if (card.getRank() == Rank.ACE) { rankval = 12; }

            if (card.getSuit() == Suit.HEARTS) { suitval = 0; }
            if (card.getSuit() == Suit.CLUBS) { suitval = 1; }
            if (card.getSuit() == Suit.SPADES) { suitval = 2; }
            if (card.getSuit() == Suit.DIAMONDS) { suitval = 3; }

            int cardID = rankval + ( 13 * suitval );
            sumcards += cardID;
        } // 0+1+2+...+51 = 1326
        assertEquals(1326, sumcards);
    }
}
