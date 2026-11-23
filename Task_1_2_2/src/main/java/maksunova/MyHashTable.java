package maksunova;

import java.util.Objects;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;


/**
 * хеш-таблица.
 * размер массива бакетов зависит от коэффициента заполения.
 * коллизии ключей разрешаются связным списком внутри бакета.
 *
 * @param <K> ключ
 * @param <V> значение
 */
public class MyHashTable<K, V> {

    // начальный размер массива AND коэффициент заполнения
    private final int DEFAULT_INITIAL_CAPACITY = 16;
    private final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Когда количество элементов в таблице превышает
    //      capacity × load factor
    // (то есть заполняется на 75%),
    // запускается процесс Resize (расширение).

    private Node<K, V>[] buckets;
    private int size;
    private int modCount;

    public MyHashTable() {
        buckets = setBuckets(DEFAULT_INITIAL_CAPACITY);
    }


    /**
     * инициализировать бакеты в таблице.
     *
     * @param capacity вмещаемость
     * @return инициализированный пустой массив {@code Node<K,V>}
     * @param <K> тип ключ
     * @param <V> тип значение
     */
    private static <K,V> Node<K,V>[] setBuckets(int capacity) {
        return (Node<K,V>[]) new Node[capacity];
    }

    /**
     *
     * @param key
     * @return
     */
    private static int hash(Object key) {
        if(key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        int ha = key.hashCode();
        return ha ^ (ha >>> 16);
    }

    /**
     * находит ноду по ключу.
     *
     * @param key ключ для поиска
     * @return ноду, если ключа не оказалось, null
     */
    public Node<K,V> getNode(K key) {
        if (key == null) {
            throw new NullPointerException("Ключ не может быть null");
        }
        int hash = hash(key);
        int len = buckets.length;
        for (Node<K, V> node = buckets[hash & (len - 1)]; node != null; node = node.next) {
            if (node.hash == hash && node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    /**
     * проверяет, есть ли в таблице пара с таким ключом.
     *
     * @throws NullPointerException если ключ равен {@code null}
     */
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException("Key must be not null");
        }
        int index = hash(key) & (buckets.length - 1);
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            if (key.equals(node.key)) return true;   // или Objects.equals
        }
        return false;
    }

    /**
     * добавление пары ключ-значение (k, v).
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        Node<K,V> node = getNode(key);
        if (node != null) {
            node.setValue(value);
            return;
        }

        int hash = hash(key);
        int index = hash & (buckets.length - 1);
        buckets[index] = new Node<>(hash, key, value, buckets[index]);
        size++;
        modCount++;
        if (size > buckets.length * DEFAULT_LOAD_FACTOR) {
            resize(buckets.length * 2);
        }
    }

    /**
     * Возвращает значение по ключу или {@code null}, если ключа в таблице нет.
     *
     * @throws NullPointerException если ключ равен {@code null}
     */
    public V get(Object key) {
        if (key == null) {
            throw new NullPointerException("Key must be not null");
        }
        int index = hash(key) & (buckets.length - 1);
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            if (key.equals(node.key)) return node.value;
        }
        return null;
    }

    /**
     * удаление пары ключ-значение (k, v).
     */
    public boolean remove(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Ключ не может быть null");
        }
        int hash = hash(key);
        int index = hash & (buckets.length - 1);
        Node<K, V> prev = null;
        for (Node<K, V> node = buckets[index]; node != null; prev = node, node = node.next) {
            if (node.hash == hash && node.key.equals(key)) {
                if (!Objects.equals(node.value, value)) {
                    return false;
                }
                if (prev == null) {
                    buckets[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                modCount++;
                if (buckets.length > DEFAULT_INITIAL_CAPACITY
                        && size <= buckets.length / 4) {
                    resize(buckets.length / 2);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * обновление значения по ключу.
     *
     * @param key ключ
     * @param value значение
     */
    public void update(K key, V value) {
        Node<K,V> node = getNode(key);
        if (node == null) {
            throw new NoSuchElementException("Ключ не найден: " + key);
        }
        node.setValue(value);
    }

    /**
     * возвращает размер таблицы.
     *
     * @return целое число
     */
    public int size() {
        return size;
    }

    /**
     * итерирование по элементам коллекции.
     * по парам (k, v).
     *
     * @throws ConcurrentModificationException с обработкой исключительной ситуации
     */
    public Iterator<Map.Entry<K, V>> iterator() {
        return new EntryIterator();
    }

    /**
     * Обходит бакеты по порядку, внутри идёт по цепочке.
     * Запоминает {@code modCount} при создании и сверяет его в {@code next()}.
     */
    private final class EntryIterator implements Iterator<Map.Entry<K, V>> {

        private final int expectedModCount = modCount;
        private int bucketIndex;
        private Node<K, V> next = nextBucketHead();

        /**
         * проверка: есть ли значение в связном списке дальше.
         *
         * @return есть ли следующее значение в связном списке?
         */
        @Override
        public boolean hasNext() {
            return next != null;
        }

        /**
         * находит следующий узел ноды в связном списке.
         *
         * @return //
         */
        @Override
        public Map.Entry<K, V> next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (next == null) {
                throw new NoSuchElementException();
            }
            Node<K, V> current = next;
            next = current.next != null ? current.next : nextBucketHead();
            return (Map.Entry<K,V>) current;
        }

        /**
         * переход к сдледующей не пустой записи.
         *
         * @return //
         */
        private Node<K, V> nextBucketHead() {
            while (bucketIndex < buckets.length) {
                Node<K, V> head = buckets[bucketIndex++];
                if (head != null) {
                    return head;
                }
            }
            return null;
        }
    }


    /**
     * изменение размера таблицы при критическом значении.
     */
    private void resize(int newCapacity) {
        // количество элементов в таблице заполняется на 75%
        // > (capacity × load factor)
        Node<K,V>[] saved = buckets;
        buckets = setBuckets(newCapacity);
        for(Node<K,V> node : saved) {
            while (node != null) {
                Node<K,V> next = node.next;

                int index = node.hash & (newCapacity - 1);
                node.next = buckets[index];
                buckets[index] = node;

                node = next;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyHashTable<?, ?> other = (MyHashTable<?, ?>) o;
        if (this.size != other.size) return false;

        Iterator<Map.Entry<K, V>> it = iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> e = it.next();
            Object key = e.getKey();
            Object value = e.getValue();

            if (!other.containsKey(key)) return false;
            if (!Objects.equals(value, other.get(key))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        Iterator<Map.Entry<K, V>> it = iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> e = it.next();
            h += Objects.hashCode(e.getKey()) ^ Objects.hashCode(e.getValue());
        }
        return h;
    }





    /**
     * Звено цепочки коллизий. Хеш хранится, чтобы не пересчитывать его при изменении.
     */
    private static class Node<K, V> implements Map.Entry<K,V> {

        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * геттер ключа карты.
         *
         * @return ключ
         */
        public K getKey() {
            return key;
        }

        /**
         * геттер значения карты.
         *
         * @return значение
         */
        public V getValue() {
            return value;
        }

        /**
         * установить значение ноды.
         *
         * @param newValue новое значение
         * @return возвращается старое
         */
        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        /**
         * хешкод ноды.
         * берет хеш ключа и значения, побитово сравнивает
         *
         * @return 32-битное число
         */
        public final int hashCode() {
            return Objects.hash(key) ^ Objects.hash(value);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            return o instanceof Map.Entry<?,?> en
                    && Objects.equals(key, en.getKey())
                    && Objects.equals(value, en.getValue());
        }

        /**
         * отобразить данные карты в строке.
         *
         * @return строку-представление карты
         */
        public String toString() {
            return key + " = " + value;
        }
    }

}