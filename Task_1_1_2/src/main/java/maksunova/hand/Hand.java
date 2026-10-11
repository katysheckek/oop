package maksunova.hand;

import maksunova.card.Card;
import maksunova.card.Rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All cards that gamer have in his hand.
 */
public class Hand {

    private final List<Card> cards = new ArrayList<>();

    /**
     * take card to the hand.
     */
    public void takeCard(Card card) {
        cards.add(card);
    }

    /**
     * return list of cards.
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void clear() { cards.clear(); }
    /**
     * calculate the hand score.
     *
     * <p>Ace = 11 points.
     * if sum is grater than 21, Aces turn to 1
     * by the queue.</p>
     */
    public int getScore() {
        int score = 0;
        int aces = 0;

        for (Card card : cards) {
            score += card.getRank().getScore();

            if (card.getRank() == Rank.ACE) {
                aces++;
            }
        }

        while (aces > 0 && score > 21) {
            score -= 10;
            aces--;
        }

        return score;
    }

    /**
     * is there a black jack?.
     *
     * @return true, the hand has a black jack
     */
    public boolean isBlackjack() {
        return getScore() == 21;
    }

    /**
     * check for loose.
     */
    public boolean isOver21() {
        return getScore() > 21;
    }
}
