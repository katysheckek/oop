package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;

import maksunova.card.Card;
import maksunova.card.Rank;
import maksunova.card.Suit;
import org.junit.jupiter.api.Test;

/**
 * tests cover Card class.
 */
public class CardTest {

    @Test
    void cardShouldStoreRank() {
        Card card = new Card(Rank.ACE, Suit.SPADES);
        assertEquals(Rank.ACE, card.getRank());
    }

    @Test
    void cardShouldStoreSuit() {
        Card card = new Card(Rank.ACE, Suit.SPADES);
        assertEquals(Suit.SPADES, card.getSuit());
    }

    @Test
    void numberCardShouldHaveCorrectName() {
        Card card = new Card(Rank.SEVEN, Suit.SPADES);
        assertEquals("Семерка Пики", card.name());
    }

    @Test
    void queenShouldHaveCorrectName() {
        Card card = new Card(Rank.QUEEN, Suit.SPADES);
        assertEquals("Пиковая королева", card.name());
    }

    @Test
    void jackShouldHaveCorrectName() {
        Card card = new Card(Rank.JACK, Suit.HEARTS);
        assertEquals("Червовый валет", card.name());
    }

    @Test
    void kingShouldHaveCorrectName() {
        Card card = new Card(Rank.KING, Suit.DIAMONDS);
        assertEquals("Бубновый король", card.name());
    }

    @Test
    void aceShouldHaveCorrectName() {
        Card card = new Card(Rank.ACE, Suit.CLUBS);
        assertEquals("Туз Трефы", card.name());
    }

    @Test
    void toStringShouldReturnCardName() {
        Card card = new Card(Rank.TEN, Suit.HEARTS);
        assertEquals("Десятка Червы", card.name());
    }

}
