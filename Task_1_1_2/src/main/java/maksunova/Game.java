package maksunova;

/**
 * control the whole game.
 */
public class Game {

    private final ConsoleInteraction console;

    private int playerWins;
    private int dealerWins;

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
            System.out.println();
            System.out.println("Раунд " + roundNum);

            Deck deck = new Deck();
            Player player = new Player();
            Dealer dealer = new Dealer();

            Round round = new Round(
                    deck,
                    player,
                    dealer,
                    console
            );

            int result = round.play();

            if (result > 0) {
                playerWins++;
            } else if (result < 0) {
                dealerWins++;
            }

            System.out.println();
            System.out.print(
                    "Счет "
                            + playerWins
                            + ":"
                            + dealerWins
            );
            if (playerWins > dealerWins) {
                System.out.println(" в вашу пользу."); }
            else if (playerWins < dealerWins) {
                System.out.println(" в пользу диллера.");
            }
            else { System.out.println(); }

            roundNum++;

            playAgain = console.askNewGame();
        }
    }
}
