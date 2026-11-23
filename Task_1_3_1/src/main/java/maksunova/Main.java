package maksunova;

import java.io.IOException;
import java.io.PrintStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println(new FinderSubString().find("input.txt", "бра"));
        } catch (IOException ioex) {
            throw new RuntimeException("ВСЕ ПОШЛО НАПЕРЕКОСЯК!!!\n" + ioex);
        }
    }

    static final int EXIT_BAD_USAGE = 1;
    static final int EXIT_FAILURE = 2;

    static int run(String[] args, PrintStream out, PrintStream err) {
        if (args.length != 2) {
            err.println("Использование: Main <файл> <подстрока>");
            return EXIT_BAD_USAGE;
        }
        try {
            out.println(new FinderSubString().find(args[0], args[1]));
            return 0;
        } catch (IOException | IllegalArgumentException e) {
            err.println("Ошибка: " + e);
            return EXIT_FAILURE;
        }
    }
}