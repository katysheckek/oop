package maksunova;

/**
 * Player.
 */
public class Player {
    public final Hand hand = new Hand();

    public Hand seeHand() {
        return hand;
    }

    public void takeCard(Card card) {
        hand.takeCard(card);
    }

}
