package maksunova;

import maksunova.card.Card;
import maksunova.card.Rank;

/**
 * one round frame.
 */
public class Round {

    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private final ConsoleInteraction console;

    /**
     * round structure.
     *
     * @param deck    deck
     * @param player  player
     * @param dealer  dealer
     * @param console console interaction
     */
    public Round(
            Deck deck,
            Player player,
            Dealer dealer,
            ConsoleInteraction console
    ) {
        this.deck = deck;
        this.player = player;
        this.dealer = dealer;
        this.console = console;
    }

    /**
     * start a round.
     *
     * @return result:
     *     1 player wins,
     *     0 equal,
     *     -1 diller wins
     */
    public int play() {

        giveCards();

        System.out.println("Дилер раздал карты");
        showHandsHiddenDealer();

        // check blackjack from the start.
        if (player.seeHand().isBlackjack()) {
            System.out.println("У вас блэкджек! Вы выиграли раунд!");
            return 1;
        }

        if (dealer.seeHand().isBlackjack()) {
            System.out.println("У дилера блэкджек! Вы проиграли раунд!");
            return -1;
        }

        // player move.
        System.out.println();
        System.out.println("Ваш ход");
        System.out.println("-------");

        boolean playerStopped = false;

        while (!playerStopped) {

            int action = console.askAction();

            if (action == 0) {
                playerStopped = true;
                break;
            }

            Card card = deck.pullout();
            player.takeCard(card);

            System.out.println("Вы открыли карту "
                    + card.name()
                    + " (" + getCardScore(card, player.seeHand()) + ")");

            showHandsHiddenDealer();

            if (player.seeHand().isOver21()) {
                System.out.println(
                        "Вы набрали больше 21. Вы проиграли раунд!");
                return -1;
            }
        }

        // dealler move.
        System.out.println();
        System.out.println("Ход дилера");
        System.out.println("-------");

        revealDealerCard();

        if (dealer.seeHand().isOver21()) {
            return 1;
        }

        while (dealer.shouldTakeCard()) {
            Card card = deck.pullout();
            dealer.takeCard(card);

            System.out.println();
            System.out.println("Дилер открывает карту "
                    + card.name()
                    + " (" + getCardScore(card, dealer.seeHand()) + ")");

            showHandsOpenDealer();
        }

        // dealler has a score over 21.
        if (dealer.seeHand().isOver21()) {
            System.out.println();
            System.out.println(
                    "Дилер набрал больше 21. Вы выиграли раунд!");
            return 1;
        }

        int playerScore = player.seeHand().seeScore();
        int dealerScore = dealer.seeHand().seeScore();

        System.out.println();

        if (playerScore > dealerScore) {
            System.out.println("Вы выиграли раунд!");
            return 1;
        }

        if (playerScore < dealerScore) {
            System.out.println("Вы проиграли раунд!");
            return -1;
        }

        System.out.println("Ничья!");
        return 0;
    }

    /**
     * initial deal of cards.
     */
    public void giveCards() {
        player.takeCard(deck.pullout());
        dealer.takeCard(deck.pullout());

        player.takeCard(deck.pullout());
        dealer.takeCard(deck.pullout());
    }

    /**
     * print set of cards, player and dealer hand (closed card).
     */
    private void showHandsHiddenDealer() {
        System.out.println(
                "Ваши карты: "
                        + formatHand(player.seeHand())
                        + " > "
                        + player.seeHand().seeScore()
        );

        Card dealerFirstCard = dealer.seeHand().seeCards().get(0);

        System.out.println(
                "Карты дилера: ["
                        + dealerFirstCard.name()
                        + " ("
                        + getCardScore(dealerFirstCard, dealer.seeHand()) + "), "
                        + "<закрытая карта]"
        );
    }

    /**
     * print set of cards, player and dealer hand (full open).
     */
    private void showHandsOpenDealer() {
        System.out.println(
                "Ваши карты: "
                        + formatHand(player.seeHand())
                        + " > "
                        + player.seeHand().seeScore()
        );

        System.out.println(
                "Карты дилера: "
                        + formatHand(dealer.seeHand())
                        + " > "
                        + dealer.seeHand().seeScore()
        );
    }

    /**
     * open closed dealer card.
     */
    private void revealDealerCard() {
        System.out.println(
                "Дилер открывает закрытую карту "
                        + dealer.seeHand().seeCards().get(1).name()
                        + " ("
                        + getCardScore(
                        dealer.seeHand().seeCards().get(1),
                        dealer.seeHand()
                )
                        + ")"
        );

        showHandsOpenDealer();
    }

    /**
     * hand description by the words.
     */
    private String formatHand(Hand hand) {
        StringBuilder result = new StringBuilder("[");

        for (int i = 0; i < hand.seeCards().size(); i++) {
            Card card = hand.seeCards().get(i);

            result.append(card.name())
                    .append(" (")
                    .append(getCardScore(card, hand))
                    .append(")");

            if (i < hand.seeCards().size() - 1) {
                result.append(", ");
            }
        }

        result.append("]");
        return result.toString();
    }

    /**
     * return score of the hand.
     *
     * <p>for the aces:
     * value 11 or 1.</p>
     */
    private int getCardScore(Card card, Hand hand) {
        if (card.getRank() != Rank.ACE) {
            return card.getRank().getScore();
        }

        return hand.hasAceAsEleven() ? 11 : 1;
    }
}

