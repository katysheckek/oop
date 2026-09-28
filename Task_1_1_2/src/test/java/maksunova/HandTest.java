package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

import maksunova.card.Card;
import maksunova.card.Rank;
import maksunova.card.Suit;
import org.junit.jupiter.api.Test;


/**
 * tests cover the Hand class logic.
 */
public class HandTest {

    @Test
    void cardsShouldBeAddedToHand() {
        Hand hand = new Hand();
        Card card = new Card(Rank.ACE, Suit.HEARTS);
        hand.takeCard(card);

        assertEquals(1, hand.seeCards().size());
        assertEquals(card, hand.seeCards().get(0));
    }

    @Test
    void returnedCardsListShouldBeUnmodifiable() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));

        assertThrows(UnsupportedOperationException.class,
                () -> hand.seeCards().clear()); // predefined action
    }

    /// SCORE ///
    @Test
    void emptyHandEqualsZeroScore() {
        Hand hand = new Hand();
        assertEquals(0, hand.seeScore());
    }

    @Test
    void aceShouldCostElevenUnder21() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.takeCard(new Card(Rank.NINE, Suit.SPADES));

        assertEquals(20, hand.seeScore());
    }

    @Test
    void aceShouldCostOneWhenSumOver21() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));  // 1 (11)
        hand.takeCard(new Card(Rank.KING, Suit.SPADES)); // 10
        hand.takeCard(new Card(Rank.FIVE, Suit.CLUBS));  // 5

        assertEquals(16, hand.seeScore());
    }

    @Test
    void twoAcesCalculatedCorrectly() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.takeCard(new Card(Rank.ACE, Suit.SPADES));
        hand.takeCard(new Card(Rank.NINE, Suit.CLUBS));

        assertEquals(21, hand.seeScore());
    }

    @Test
    void handShouldBeOver21() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.KING, Suit.HEARTS));
        hand.takeCard(new Card(Rank.QUEEN, Suit.SPADES));
        hand.takeCard(new Card(Rank.FIVE, Suit.CLUBS));

        assertTrue(hand.isOver21());
    }


    @Test
    void aceShouldBeElevenWhenHandIsNotOver21() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.takeCard(new Card(Rank.NINE, Suit.SPADES));

        assertTrue(hand.hasAceAsEleven());
    }

    @Test
    void aceShouldBeOneWhenElevenWouldCauseBust() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.takeCard(new Card(Rank.KING, Suit.SPADES));
        hand.takeCard(new Card(Rank.FIVE, Suit.CLUBS));

        assertFalse(hand.hasAceAsEleven());
    }
}

