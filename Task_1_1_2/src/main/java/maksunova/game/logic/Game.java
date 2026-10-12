package maksunova.game.logic;

import maksunova.game.Deck;
import maksunova.game.ResOfTheGame;
import maksunova.game.console.ConsoleInteraction;
import maksunova.game.console.ConsoleUi;
import maksunova.hand.Dealer;
import maksunova.hand.Player;

/**
 * control the whole game.
 */
public class Game {

    private final ConsoleInteraction console;
    private int playerWins = 0;
    private int dealerWins = 0;

    private final Player player = new Player();
    private final Dealer dealer = new Dealer();

    public Game(ConsoleInteraction console) {
        this.console = console;
    }

    /**
     * start.
     */
    public void start() {


        boolean playAgain = true;
        int roundNum = 1;

        while (playAgain) {

            player.getHand().clear();
            dealer.getHand().clear();

            System.out.println();
            System.out.println("Раунд " + roundNum);

            Deck deck = new Deck();
            Round round = new Round(deck, player, dealer);

            //
            System.out.println("Дилер раздал карты");
            ConsoleUi.showHandsHiddenDealer(player, dealer);

            ResOfTheGame result = round.checkInitialBlackjack();

            if (result == ResOfTheGame.LOSE) {
                ConsoleUi.revealDealerCard(player, dealer);
            }

            if (result == ResOfTheGame.UNDEF) {
                // ход игрока
                boolean playerStopped = false;
                while (!playerStopped) {
                    boolean wantsHit = console.askAction();
                    if (wantsHit) {
                        round.playerHit();
                        ConsoleUi.showHandsHiddenDealer(player, dealer);

                        if (player.getHand().isOver21()) {
                            System.out.println("Вы набрали больше 21. Вы проиграли раунд!");
                            result = ResOfTheGame.LOSE;
                            playerStopped = true;
                        }
                    } else {
                        playerStopped = true;
                    }
                }

                // ход дилера (если игрок не перебрал)
                if (result == ResOfTheGame.UNDEF) {
                    System.out.println("\nХод дилера\n-------");
                    ConsoleUi.revealDealerCard(player, dealer);

                    round.dealerTurn();
                    ConsoleUi.showHandsOpenDealer(player, dealer);

                    result = round.evaluateResult();
                }
            }

            // Обработка результатов
            if (result == ResOfTheGame.WIN) {
                playerWins++;
                System.out.println("Вы выиграли раунд!");
            } else if (result == ResOfTheGame.LOSE) {
                dealerWins++;
                System.out.println("Дилер выиграл раунд!");
            } else {
                System.out.println("Ничья!");
            }

            System.out.println();
            System.out.print("Счет " + playerWins + ":" + dealerWins);

            if (playerWins > dealerWins) {
                System.out.println(" в вашу пользу.");
            } else if (playerWins < dealerWins) {
                System.out.println(" в пользу диллера.");
            }

            roundNum++;
            playAgain = console.askNewGame();
        }
    }
}
