package maksunova;

/**
 * Демонстрация работы {@link MyHashTable} на примере из задания.
 */
public class Main {

    /**
     * Добавляет значение {@code 1}, заменяет его на {@code 1.0} и печатает результат.
     */
    public static void main(String[] args) {
        MyHashTable<String, Number> hashTable = new MyHashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));
    }
}