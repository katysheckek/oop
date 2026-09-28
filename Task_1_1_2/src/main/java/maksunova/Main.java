package maksunova;



/**
 * enter the progrm.
 */
public class Main {

    /**
     * start the game.
     *
     * @param args standard arguments
     */
    public static void main(String[] args) {
        System.out.println("Добро пожаловать в Блэкджек!");

        ConsoleInteraction console = new ConsoleInteraction();
        Game game = new Game(console);

        game.start();
    }
}
