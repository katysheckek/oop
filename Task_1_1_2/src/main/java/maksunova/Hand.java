package maksunova;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All cards that gamer have in his hand.
 */
public class Hand {

    private final List<Card> hand = new ArrayList<>();

    /**
     * take card to the hand.
     */
    public void takeCard(Card card) {
        hand.add(card);
    }

    /**
     * return list of cards.
     */
    public List<Card> seeCards() {
        return Collections.unmodifiableList(hand);
    }

    /**
     * calculate the hand score.
     *
     * <p> Ace = 11 points.
     * if sum is grater than 21, Aces turn to 1
     * by the queue.</p>
     */
    public int seeScore() {
        int mainScore = 0;
        int aces = 0;

        for (Card card : hand) {
            mainScore += card.getRank().getScore();

            if (card.getRank() == Rank.ACE) {
                aces++;
            }
        }

        while (aces > 0 && mainScore > 21) {
            mainScore -= 10;
            aces--;
        }

        return mainScore;
    }

    /**
     * is there a black jack?.
     *
     * @return true, black jack from the start
     */
    public boolean isBlackjack() {
        return hand.size() == 2 && seeScore() == 21;
    }

    /**
     * check for loose.
     */
    public boolean isOver21() {
        return seeScore() > 21;
    }

    /**
     * how to expose the ace
     * @return the way how to expose, as 1 or as 11
     */
    public boolean hasAceAsEleven() {
        int score = 0;
        int aces = 0;

        for (Card card : hand) {
            score += card.getRank().getScore();

            if (card.getRank() == Rank.ACE) {
                aces++;
            }
        }

        while (aces > 0 && score > 21) {
            score -= 10;
            aces--;
        }

        return aces > 0;
    }
}
