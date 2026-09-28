package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import maksunova.card.Card;
import maksunova.card.Rank;
import maksunova.card.Suit;
import org.junit.jupiter.api.Test;

class Tests {

    @Test
    void testCard() {
        Card card = new Card(Rank.QUEEN, Suit.SPADES);
        assertEquals(Rank.QUEEN, card.getRank());
        assertEquals(Suit.SPADES, card.getSuit());
        assertEquals("Пиковая королева", card.name());
    }

    @Test
    void testDeck() {
        Deck deck = new Deck();
        assertEquals(52, deck.size());
        deck.pullout();
        assertEquals(51, deck.size());
    }

    @Test
    void testHand() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.takeCard(new Card(Rank.NINE, Suit.SPADES));
        assertEquals(20, hand.seeScore());
        assertFalse(hand.isOver21());
    }

    @Test
    void testBlackjack() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.takeCard(new Card(Rank.KING, Suit.SPADES));
        assertTrue(hand.isBlackjack());
    }

    @Test
    void testAceBecomesOne() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.takeCard(new Card(Rank.KING, Suit.SPADES));
        hand.takeCard(new Card(Rank.FIVE, Suit.CLUBS));
        assertEquals(16, hand.seeScore());
    }

    @Test
    void testDealer() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.takeCard(new Card(Rank.FIVE, Suit.SPADES));
        assertEquals(15, dealer.seeHand().seeScore());
        assertTrue(dealer.shouldTakeCard());
    }

    @Test
    void testDealerStopsAt17() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.takeCard(new Card(Rank.SEVEN, Suit.SPADES));
        assertEquals(17, dealer.seeHand().seeScore());
        assertFalse(dealer.shouldTakeCard());
    }

    @Test
    void testHandBust() {
        Hand hand = new Hand();
        hand.takeCard(new Card(Rank.KING, Suit.HEARTS));
        hand.takeCard(new Card(Rank.QUEEN, Suit.SPADES));
        hand.takeCard(new Card(Rank.FIVE, Suit.CLUBS));
        assertTrue(hand.isOver21());
    }
}

/*
значение карт
значение мастей

туз 11/1
несколько тузов

blackjack
перебор

стандартная колода 52 карты
отсутствие дубликатов
исчерпание колоды

правило дилера < 17
остановка дилера на 17+
*/
