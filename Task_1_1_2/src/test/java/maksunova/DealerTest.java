package maksunova;

import maksunova.card.Card;
import maksunova.card.Rank;
import maksunova.card.Suit;
import maksunova.hand.Dealer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {

    private Card card(Rank rank) {
        return new Card(rank, Suit.HEARTS);
    }

    @Test
    void dealerTakesCardBelowSeventeen() {
        Dealer dealer = new Dealer();
        dealer.takeCard(card(Rank.TEN));
        dealer.takeCard(card(Rank.SIX));

        assertEquals(16, dealer.getHand().getScore());
        assertTrue(dealer.shouldTakeCard());
    }

    @Test
    void dealerStopsAtSeventeen() {
        Dealer dealer = new Dealer();
        dealer.takeCard(card(Rank.TEN));
        dealer.takeCard(card(Rank.SEVEN));

        assertFalse(dealer.shouldTakeCard());
    }

    @Test
    void dealerStopsAboveSeventeen() {
        Dealer dealer = new Dealer();
        dealer.takeCard(card(Rank.TEN));
        dealer.takeCard(card(Rank.EIGHT));

        assertFalse(dealer.shouldTakeCard());
    }

    @Test
    void dealerStopsOnSoftSeventeenWithCurrentRules() {
        Dealer dealer = new Dealer();
        dealer.takeCard(card(Rank.ACE));
        dealer.takeCard(card(Rank.SIX));

        assertEquals(17, dealer.getHand().getScore());
        assertFalse(dealer.shouldTakeCard());
    }
}
