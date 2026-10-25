package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;
import maksunova.card.Card;
import maksunova.game.Deck;
import org.junit.jupiter.api.Test;

class DeckTest {

    @Test
    void freshDeckContainsExactly52Cards() {
        Deck deck = new Deck();

        assertEquals(52, deck.size());
    }

    @Test
    void drawingCardsShrinksDeckOneByOneUntilEmpty() {
        Deck deck = new Deck();
        Set<String> uniqueCards = new HashSet<>();

        for (int i = 0; i < 52; i++) {
            Card card = deck.pullout();
            assertNotNull(card);
            uniqueCards.add(card.getRank().name() + ":" + card.getSuit().name());
            assertEquals(51 - i, deck.size());
        }

        assertEquals(52, uniqueCards.size());
        assertEquals(0, deck.size());
    }

    @Test
    void drawingFromEmptyDeckFailsInsteadOfReturningNull() {
        Deck deck = new Deck();

        for (int i = 0; i < 52; i++) {
            deck.pullout();
        }

        IllegalStateException ex =
                assertThrows(IllegalStateException.class, deck::pullout);

        assertEquals("Пустая колода", ex.getMessage());
        assertEquals(0, deck.size());
    }
}
