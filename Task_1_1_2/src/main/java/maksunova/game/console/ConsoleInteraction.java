package maksunova.game.console;

import java.util.Scanner;

/**
 * Отвечает за взаимодействие с пользователем через консоль.
 */
public class ConsoleInteraction {

    private final Scanner scanner = new Scanner(System.in);
    String action;
    /**
     * ask for action.
     *
     * @return 1, player want to take another card, 0, stop
     */
    public boolean askAction() {
        while (true) {
            System.out.println(
                    "Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться"
            );
            System.out.print(". ");

            action = scanner.next();

            if (action.equals("1") || action.equals("0")) {
                return action.equals("1");
            }
        }
    }

    /**
     * ask if player want to start another round.
     *
     * @return true, player want to continue
     */
    public boolean askNewGame() {
        while (true) {
            System.out.println("\"1\", чтобы продолжить, \"0\", чтобы выйти");
            System.out.print(". ");

            action = scanner.next();

            if (action.equals("1") || action.equals("0")) {
                return action.equals("1");
            }

            System.out.println("Введите \"1\" или \"0\".");
        }
    }
}