package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import maksunova.card.Card;
import maksunova.card.Rank;
import maksunova.card.Suit;
import maksunova.hand.Player;
import org.junit.jupiter.api.Test;

/**
 * test 4 player operations logic.
 */
public class PlayerTest {
    @Test
    void newPlayer_hasEmptyHand() {
        Player p = new Player();
        assertTrue(p.getHand().getCards().isEmpty());
    }

    @Test
    void takeCard_addsToHand() {
        Player p = new Player();
        Card c = new Card(Rank.EIGHT, Suit.DIAMONDS);
        p.takeCard(c);
        assertEquals(1, p.getHand().getCards().size());
        assertTrue(p.getHand().getCards().contains(c));
    }

    @Test
    void takeCard_multiple() {
        Player p = new Player();
        p.takeCard(new Card(Rank.EIGHT, Suit.DIAMONDS));
        p.takeCard(new Card(Rank.EIGHT, Suit.DIAMONDS));
        assertEquals(2, p.getHand().getCards().size());
    }
}
