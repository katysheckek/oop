package maksunova;

import java.util.Scanner;

/**
 * Отвечает за взаимодействие с пользователем через консоль.
 */
public class ConsoleInteraction {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * ask for action.
     *
     * @return 1, player want to take another card, 0, stop
     */
    public int askAction() {
        while (true) {
            System.out.println(
                    "Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться"
            );
            System.out.print(". ");

            int action = scanner.nextInt();

            if (action == 0 || action == 1) {
                return action;
            }
            System.out.println("Введите 1 или 0.");
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

            int answer = scanner.nextInt();

            if (answer == 1) {
                return true;
            }

            if (answer == 0) {
                return false;
            }

            System.out.println("Введите 1 или 0.");
        }
    }

}