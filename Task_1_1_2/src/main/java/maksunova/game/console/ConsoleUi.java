package maksunova.game.console;

import maksunova.card.Card;
import maksunova.hand.Dealer;
import maksunova.hand.Hand;
import maksunova.hand.Player;

/**
 * console game presentation 4 user.
 */
public class ConsoleUi {

    /**
     * print set of cards, player and dealer hand (closed card).
     */
    public static void showHandsHiddenDealer(Player player, Dealer dealer) {
        System.out.println(
                "Ваши карты: "
                        + formatHand(player.getHand())
                        + " > "
                        + player.getHand().getScore()
        );

        Card dealerFirstCard = dealer.getHand().getCards().get(0);

        System.out.println(
                "Карты дилера: ["
                        + dealerFirstCard.name()
                        + ", <закрытая карта]"
        );
    }

    /**
     * print set of cards, player and dealer hand (full open).
     */
    public static void showHandsOpenDealer(Player player, Dealer dealer) {
        System.out.println(
                "Ваши карты: "
                        + formatHand(player.getHand())
                        + " > "
                        + player.getHand().getScore()
        );

        System.out.println(
                "Карты дилера: "
                        + formatHand(dealer.getHand())
                        + " > "
                        + dealer.getHand().getScore()
        );
    }

    /**
     * open closed dealer card.
     */
    public static void revealDealerCard(Player player, Dealer dealer) {
        Card hiddenCard = dealer.getHand().getCards().get(1);
        System.out.println("Дилер открывает закрытую карту " + hiddenCard.name());

        // showHandsOpenDealer(player, dealer);
    }

    /**
     * hand description by the words.
     */
    private static String formatHand(Hand hand) {
        StringBuilder result = new StringBuilder("[");

        for (int i = 0; i < hand.getCards().size(); i++) {
            Card card = hand.getCards().get(i);

            result.append(card.name());

            if (i < hand.getCards().size() - 1) {
                result.append(", ");
            }
        }

        result.append("]");
        return result.toString();
    }
}
