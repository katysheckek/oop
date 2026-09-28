package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import maksunova.card.Card;
import maksunova.card.Rank;
import maksunova.card.Suit;
import org.junit.jupiter.api.Test;


/**
 * tests covers the dealer logic.
 */
public class DealerTest {

    @Test
    void dealerShouldTakeCardWhenScoreIsLessThan17() {
        Dealer dealer = new Dealer();

        dealer.takeCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.takeCard(new Card(Rank.FIVE, Suit.SPADES));

        assertEquals(15, dealer.seeHand().seeScore());
        assertTrue(dealer.shouldTakeCard());
    }

    @Test
    void dealerShouldStopWhenScoreIs17() {
        Dealer dealer = new Dealer();

        dealer.takeCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.takeCard(new Card(Rank.SEVEN, Suit.SPADES));

        assertEquals(17, dealer.seeHand().seeScore());
        assertFalse(dealer.shouldTakeCard());
    }

    @Test
    void dealerShouldStopWhenScoreIsGreaterThan17() {
        Dealer dealer = new Dealer();

        dealer.takeCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.takeCard(new Card(Rank.NINE, Suit.SPADES));

        assertEquals(19, dealer.seeHand().seeScore());
        assertFalse(dealer.shouldTakeCard());
    }

    @Test
    void dealerShouldStopWithBlackjack() {
        Dealer dealer = new Dealer();

        dealer.takeCard(new Card(Rank.ACE, Suit.HEARTS));
        dealer.takeCard(new Card(Rank.KING, Suit.SPADES));

        assertTrue(dealer.seeHand().isBlackjack());
        assertFalse(dealer.shouldTakeCard());
    }
}
