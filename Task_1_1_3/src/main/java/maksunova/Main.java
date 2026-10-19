package maksunova;

import maksunova.expression.Expression;
import maksunova.expression.Parser;
import maksunova.expression.Variable;
import maksunova.expression.Number;
import maksunova.operation.Div;
import maksunova.operation.Mul;
import maksunova.operation.Add;
import maksunova.operation.Sub;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

            // Печать выражения в файл/консоль.
            // Создание выражения по заданной в файле/консоли строке.
            //
            // input   e.print();
            // output  (3+(2*x))
                Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
                System.out.println("Созданное выражение: " + e.print());


            // Символьное дифференцирование выражений по заданной переменной.
            //
            // input   de.print();
            // output  (0+((0*x)+(2*1)))
                Expression de = e.derivative("x");
                System.out.println("Продефференцированное выражение: " + de.print());
            // изначальное выражение не меняется, а создается новое
            // соответствующее результату дифференцирования.
            //
            // названия переменных могут быть многобуквенными.



            // Вычисление значения выражения
            // при заданном означивании всех переменных.
            //
            // input   System.out.println(result);
            // output  23
                // (3+(2*x))
                e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
                int result = e.eval("x = 10; y = 13");
                System.out.println("Вычисленное выражение при заданных переменных: " + result);
            // Функция eval в качестве аргумента принимает строку,
            // в которой через разделитель “;” указано означивание переменных.


                /*
                    В качестве дополнительного задания реализуйте:
            1. Считывание выражения со строки, работающее со строкой без скобок вокруг каждого выражения.

                2. Функцию для упрощения выражений по следующим правилам:
                    a. Если выражение можно вычислить без какого-либо означивания (т.е. в нем нет переменных),
                    то выражение заменяется на результат вычисления.
                        b. Если выражение представляет из себя умножение на 0, то оно заменяется на константу 0.
                          c. Если выражение представляет из себя умножение на 1, то оно заменяется на второй множитель.
                            d. Если выражение представляет из себя вычитание двух одинаковых подвыражений, то оно заменяется на константу 0.

                    Упрощение не меняет исходное выражение, а создает новое (упрощенное) выражение по заданному.
                 */

        boolean isWorking = true;
        String choose;
        String expressionStr = "";

        Path fpath = Path.of("output.txt");
        // Path fpath = Paths.get("output.txt");

        // Если файл называется config.json и лежит прямо в корне папки resources
        // InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("/output.txt");
        //Path filePath = Path.of("C:\\userr\\intellij projects\\oop_26-27\\oop\\Task_1_1_3\\src\\main\\output.txt");
    // прописать путь с ресурсов, встроенные методы

        Scanner scan = new Scanner(System.in);
        Parser parser = new Parser();

        while(isWorking) {
            // 1. вписать выражение из файла или строки
            while (true) {
                System.out.println("Выберите способ ввода: 1>Файл 0>Коноль");
                choose = scan.nextLine();
                if (choose.equals("0")) {                           // Консоль
                    System.out.println("Введите выражение: ");
                    expressionStr = scan.nextLine();
                    break;
                } else if (choose.equals("1")) {                    // Файл
                    try {
                        expressionStr = Files.readString(fpath);
                    } catch (IOException exptn) {
                        System.out.println("Файл не найден");
                        break;
                    }

                    // пустое или состоящее из пробелов/переводов трок выражение
                    if (expressionStr == null || expressionStr.isBlank()) {
                        System.out.println("Файл пуст: " + fpath);
                        break;
                    }

                    System.out.println("Выражение считано из файла");
                    break;
                } else {
                    System.out.println("! Нераспознанная команда !");
                }
            }
            // парспарс
            e = parser.parse(expressionStr);

            // 2. выписать в строку или файл
            while (true) {
                System.out.println("Выберите способ выводв: 1>Файл 0>Коноль");
                choose = scan.nextLine();

                if (choose.equals("0")) {                                        // Консоль
                    System.out.println("Ваше выражение: " + e.print());
                    break;
                } else if (choose.equals("1")) {
                    try {                                                  // Файл
                        Files.writeString(fpath, e.print());
                    } catch (IOException exptn) {
                        System.out.println("Не удалось записать в " + fpath + ": " + exptn.getMessage());
                        break;
                    }

                    System.out.println("Выражение записано в файл");
                    break;
                } else {
                    System.out.println("! Нераспознанная команда !");
                }
            }


            System.out.println("Чтобы выйти, введите \"0\"");
            if(scan.nextLine().equals("0")) {
                break;
            }
        }
    }
}