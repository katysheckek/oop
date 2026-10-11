package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import maksunova.card.Card;
import maksunova.card.Rank;
import maksunova.card.Suit;
import maksunova.hand.Hand;




class HandTest {

    private Card card(Rank rank) {
        return new Card(rank, Suit.HEARTS);
    }

    @Test
    void calculatesSimpleScore() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.TEN));
        hand.takeCard(card(Rank.SIX));

        assertEquals(16, hand.getScore());
    }

    @Test
    void aceCountsAsElevenWhenItFits() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.ACE));
        hand.takeCard(card(Rank.SIX));

        assertEquals(17, hand.getScore());
    }

    @Test
    void aceTurnsIntoOneWhenElevenWouldBeTooMany() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.ACE));
        hand.takeCard(card(Rank.TEN));
        hand.takeCard(card(Rank.SIX));

        assertEquals(17, hand.getScore());
    }

    @Test
    void handlesSeveralAces() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.ACE));
        hand.takeCard(card(Rank.ACE));
        hand.takeCard(card(Rank.NINE));

        assertEquals(21, hand.getScore());
    }

    @Test
    void allAcesCanBecomeOneIfNecessary() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.ACE));
        hand.takeCard(card(Rank.ACE));
        hand.takeCard(card(Rank.ACE));
        hand.takeCard(card(Rank.TEN));

        assertEquals(13, hand.getScore());
    }

    @Test
    void twoCardAceAndTenIsBlackjack() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.ACE));
        hand.takeCard(card(Rank.TEN));

        assertTrue(hand.isBlackjack());
    }

    @Test
    void aceAndKingIsBlackjack() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.ACE));
        hand.takeCard(card(Rank.KING));

        assertTrue(hand.isBlackjack());
    }

    @Test
    void threeCardsForTwentyOneIsBlackjack() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.SEVEN));
        hand.takeCard(card(Rank.SEVEN));
        hand.takeCard(card(Rank.SEVEN));

        assertEquals(21, hand.getScore());
        assertTrue(hand.isBlackjack());
    }

    @Test
    void detectsScoreOverTwentyOne() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.TEN));
        hand.takeCard(card(Rank.NINE));
        hand.takeCard(card(Rank.FIVE));

        assertTrue(hand.isOver21());
    }

    @Test
    void exactlyTwentyOneIsNotOver() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.TEN));
        hand.takeCard(card(Rank.SIX));
        hand.takeCard(card(Rank.FIVE));

        assertEquals(21, hand.getScore());
        assertFalse(hand.isOver21());
    }

    @Test
    void clearRemovesAllCards() {
        Hand hand = new Hand();
        hand.takeCard(card(Rank.TEN));
        hand.takeCard(card(Rank.FIVE));

        hand.clear();

        assertTrue(hand.getCards().isEmpty());
        assertEquals(0, hand.getScore());
    }
}
