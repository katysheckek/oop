package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

/**
 * Проверяет класс {@link MyHashTable}.
 */
class MyHashTableTest {

    /** Ключ с одинаковым хешем у всех экземпляров: все пары попадают в одну цепочку. */
    private record BadKey(int id) {
        @Override
        public int hashCode() {
            return 42;
        }
    }

    private static MyHashTable<Integer, String> tableOf(int count) {
        MyHashTable<Integer, String> table = new MyHashTable<>();
        for (int i = 0; i < count; i++) {
            table.put(i, "v" + i);
        }
        return table;
    }

    /** Обходит таблицу итератором и проверяет, что ключи не повторяются. */
    private static <K, V> Map<K, V> collect(MyHashTable<K, V> table) {
        Map<K, V> result = new HashMap<>();
        Iterator<Map.Entry<K, V>> it = table.iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> entry = it.next();
            assertFalse(result.containsKey(entry.getKey()), "Повторяющийся ключ: " + entry.getKey());
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /** Итератор создан и сдвинут на один элемент, затем таблицу меняют: ждём исключение. */
    private static void assertNextFailsAfter(int size, Consumer<MyHashTable<Integer, String>> change) {
        MyHashTable<Integer, String> table = tableOf(size);
        Iterator<Map.Entry<Integer, String>> it = table.iterator();
        it.next();
        change.accept(table);
        assertTrue(it.hasNext());
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    /** То же, но изменение не структурное: обход должен продолжиться. */
    private static void assertNextWorksAfter(Consumer<MyHashTable<Integer, String>> change) {
        MyHashTable<Integer, String> table = tableOf(5);
        Iterator<Map.Entry<Integer, String>> it = table.iterator();
        it.next();
        change.accept(table);
        assertNotNull(it.next());
    }

    @Test
    void basicOperations() {
        MyHashTable<String, Number> table = new MyHashTable<>();
        assertEquals(0, table.size());
        assertNull(table.get("one"));
        assertFalse(table.containsKey("one"));

        table.put("one", 1);
        assertEquals(1, table.get("one"));
        assertTrue(table.containsKey("one"));

        table.update("one", 1.0);
        assertEquals(Double.valueOf(1.0), table.get("one"));

        table.put("one", 2);
        assertEquals(2, table.get("one"));
        assertEquals(1, table.size());

        assertFalse(table.remove("one", 100));
        assertFalse(table.remove("two", 2));
        assertTrue(table.remove("one", 2));
        assertFalse(table.containsKey("one"));
        assertEquals(0, table.size());
    }

    @Test
    void nullValuesAreAllowedButNullKeysAreNot() {
        MyHashTable<String, String> table = new MyHashTable<>();
        table.put("a", null);
        assertTrue(table.containsKey("a"));
        assertNull(table.get("a"));
        assertFalse(table.remove("a", "x"));

        table.update("a", "x");
        table.update("a", null);
        assertTrue(table.remove("a", null));
        assertEquals(0, table.size());

        assertThrows(NullPointerException.class, () -> table.put(null, "x"));
        assertThrows(NullPointerException.class, () -> table.get(null));
        assertThrows(NullPointerException.class, () -> table.containsKey(null));
        assertThrows(NullPointerException.class, () -> table.update(null, "x"));
        assertThrows(NullPointerException.class, () -> table.remove(null, "x"));
        assertThrows(NoSuchElementException.class, () -> table.update("missing", "x"));
        assertEquals(0, table.size());
    }

    @Test
    void keysWithEqualHashFormWorkingChain() {
        MyHashTable<BadKey, String> table = new MyHashTable<>();
        for (int i = 1; i <= 3; i++) {
            table.put(new BadKey(i), "v" + i);
        }
        assertEquals(3, table.size());
        for (int i = 1; i <= 3; i++) {
            assertEquals("v" + i, table.get(new BadKey(i)));
        }

        assertNull(table.get(new BadKey(99)));
        assertFalse(table.remove(new BadKey(99), "v99"));
        assertFalse(table.remove(new BadKey(2), "wrong"));
        assertEquals(3, table.size());

        table.update(new BadKey(2), "new");
        assertTrue(table.remove(new BadKey(2), "new"));
        assertTrue(table.remove(new BadKey(3), "v3"));
        assertEquals("v1", table.get(new BadKey(1)));
        assertEquals(1, table.size());
        assertTrue(table.remove(new BadKey(1), "v1"));
        assertEquals(0, table.size());
    }

    @Test
    void keysSharingBucketWithDifferentHashes() {
        MyHashTable<Integer, String> table = new MyHashTable<>();
        // при начальных 16 корзинах 1, 17 и 33 попадают в одну корзину, хеши у них разные
        table.put(1, "a");
        table.put(17, "b");
        table.put(33, "c");

        assertEquals("a", table.get(1));
        assertEquals("b", table.get(17));
        assertEquals("c", table.get(33));
        assertNull(table.get(49));
        assertTrue(table.remove(17, "b"));
        assertEquals("a", table.get(1));
        assertEquals("c", table.get(33));
        assertEquals(2, table.size());
    }

    @Test
    void survivesGrowingAndShrinking() {
        MyHashTable<Integer, Integer> table = new MyHashTable<>();
        for (int i = 0; i < 1000; i++) {
            table.put(i, i * 2);
            assertEquals(i + 1, table.size());
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(i * 2, table.get(i));
        }

        for (int i = 0; i < 1000; i++) {
            assertTrue(table.remove(i, i * 2));
            assertFalse(table.containsKey(i));
            assertEquals(1000 - i - 1, table.size());
            if (i < 999) {
                assertEquals((i + 1) * 2, table.get(i + 1));
                assertEquals(1998, table.get(999));
            }
        }

        table.put(7, 70);
        assertEquals(70, table.get(7));
        assertEquals(1, table.size());
    }

    @Test
    void iteratorVisitsEveryPairOnce() {
        Map<Integer, String> expected = new HashMap<>();
        for (int i = 0; i < 50; i++) {
            expected.put(i, "v" + i);
        }
        assertEquals(expected, collect(tableOf(50)));

        MyHashTable<BadKey, Integer> chained = new MyHashTable<>();
        Map<BadKey, Integer> expectedChained = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            chained.put(new BadKey(i), i);
            expectedChained.put(new BadKey(i), i);
        }
        assertEquals(expectedChained, collect(chained));

        MyHashTable<String, String> withNull = new MyHashTable<>();
        withNull.put("k", null);
        assertEquals(1, collect(withNull).size());
        assertTrue(collect(withNull).containsKey("k"));
    }

    @Test
    void iteratorBoundaries() {
        Iterator<Map.Entry<Integer, String>> empty = new MyHashTable<Integer, String>().iterator();
        assertFalse(empty.hasNext());
        assertThrows(NoSuchElementException.class, empty::next);

        Iterator<Map.Entry<Integer, String>> single = tableOf(1).iterator();
        assertTrue(single.hasNext());
        single.next();
        assertFalse(single.hasNext());
        assertThrows(NoSuchElementException.class, single::next);
        assertThrows(UnsupportedOperationException.class, single::remove);
    }

    @Test
    void iteratorFailsAfterStructuralChange() {
        assertNextFailsAfter(5, table -> table.put(100, "new"));
        assertNextFailsAfter(12, table -> table.put(100, "new")); // 13-я пара запускает расширение
        assertNextFailsAfter(5, table -> table.remove(0, "v0"));
    }

    @Test
    void iteratorSurvivesNonStructuralChange() {
        assertNextWorksAfter(table -> table.update(1, "changed"));
        assertNextWorksAfter(table -> table.put(1, "changed"));
        assertNextWorksAfter(table -> table.remove(100, "v100"));
        assertNextWorksAfter(table -> table.remove(1, "wrong"));

        MyHashTable<Integer, String> table = tableOf(3);
        Iterator<Map.Entry<Integer, String>> it = table.iterator();
        it.next().setValue("changed");
        assertNotNull(it.next());
    }

    @Test
    void equalsAndHashCode() {
        MyHashTable<Integer, String> first = tableOf(20);
        assertEquals(first, first);

        MyHashTable<Integer, String> reversed = new MyHashTable<>();
        for (int i = 19; i >= 0; i--) {
            reversed.put(i, "v" + i);
        }
        assertEquals(first, reversed);
        assertEquals(first.hashCode(), reversed.hashCode());

        // то же содержимое, но массив корзин другого размера (вырос и сжался)
        MyHashTable<Integer, String> shrunk = tableOf(100);
        for (int i = 20; i < 100; i++) {
            shrunk.remove(i, "v" + i);
        }
        assertEquals(first, shrunk);
        assertEquals(first.hashCode(), shrunk.hashCode());

        reversed.update(5, "other");
        assertNotEquals(first, reversed);
        reversed.update(5, null);
        assertNotEquals(first, reversed);

        MyHashTable<Integer, String> shifted = new MyHashTable<>();
        for (int i = 1; i <= 20; i++) {
            shifted.put(i, "v" + (i - 1));
        }
        assertNotEquals(first, shifted);
        assertNotEquals(first, tableOf(19));
        assertNotEquals(first, null);

        MyHashTable<String, String> withNull = new MyHashTable<>();
        MyHashTable<String, String> withNullToo = new MyHashTable<>();
        withNull.put("k", null);
        withNullToo.put("k", null);
        assertEquals(withNull, withNullToo);
        assertEquals(withNull.hashCode(), withNullToo.hashCode());
    }
}