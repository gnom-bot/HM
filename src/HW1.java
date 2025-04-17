public class HW1< K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public HW1() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }



    private Entry< K, V>[] buckets;
    private int size;
    private float loadFactor;

    public HW1(int capacity, float loadFactor) {
        this.buckets = new Entry[capacity];
        this.loadFactor = loadFactor;
        this.size = 0;
    }

    public V get(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> current = buckets[index];
        while (current != null) {
            if (equalsKey(key, current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public void put(K key, V value) {
        if (size >= buckets.length * loadFactor) {
            resize();
        }

        int index = getBucketIndex(key);
        Entry<K, V> newEntry = new Entry<>(key, value, null);

        if (buckets[index] == null) {
            buckets[index] = newEntry;
        } else {
            Entry<K, V> current = buckets[index];
            Entry<K, V> previous = null;
            while (current != null) {
                if (equalsKey(key, current.key)) {
                    current.value = value; // Замена существующего значения
                    return;
                }
                previous = current;
                current = current.next;
            }
            if (previous != null) {
                previous.next = newEntry;
            }
        }
        size++;
    }

    public V remove(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> current = buckets[index];
        Entry<K, V> previous = null;

        while (current != null) {
            if (equalsKey(key, current.key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = new Entry[oldBuckets.length * 2];
        size = 0;
        for (Entry<K, V> entry : oldBuckets) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private boolean equalsKey(K k1, K k2) {
        return (k1 == k2) || (k1 != null && k1.equals(k2));
    }

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        HW1<String, Integer> balls = new HW1<>();
        balls.put("yellow", 5);
        balls.put("red", 4);
        balls.put("blue", 2);
        System.out.println("В корзине красных шаров "+ balls.get("red")); //вызываем get по ключу для вывода значения
        System.out.println(balls.size());//проверяем количество пар ключ/значение
        balls.remove("yellow");//удаляем одну пару
        System.out.println(balls.size());//проверяем удаление

    }
}