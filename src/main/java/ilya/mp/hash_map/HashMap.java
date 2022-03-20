package ilya.mp.hash_map;

import java.util.Arrays;

public class HashMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR =  2.0f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private Bucket<K, V>[] table;
    private int size;
    private int capacity;
    private float loadFactor;

    @SuppressWarnings({"unchecked"})
    public HashMap() {
        this.table = new Bucket[DEFAULT_INITIAL_CAPACITY];
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    /**
     * Adding new value into the hash map if it not exist
     * otherwise update previous value. And depending on load factor
     * resize the table.
     */
    public void put(K key, V value) {
        putNode(key, value);
        checkResizeNeeded();
    }

    /**
     * Remove node by the key and return the value of the key and null otherwise.
     */
    public V remove(K key) {
        Node<K, V> node;
        return (node = removeNode(key)) == null ? null : node.getValue();
    }

    /**
     * Get value by provided key or null in other case.
     */
    public V get(K key) {
        Node<K, V> node;
        return (node = getNode(key)) == null ? null : node.getValue();
    }

    /**
     * Decrease table capacity to smallest possible.
     */
    public void decreaseTableCapacity() {
        int smallestPossibleCapacity = findSmallestPossibleCapacity();
        resize(smallestPossibleCapacity);
    }

    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    public void clear() {
        if (size > 0) {
            size = 0;
            Arrays.fill(table, null);
        }
    }

    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    /**
     * @return the number of key-value mappings in this map.
     */
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public float getLoadFactor() {
        return loadFactor;
    }

    public void setLoadFactor(float loadFactor) {
        this.loadFactor = loadFactor;
    }

    public float getCurrentLoadFactor() {
        return (float) size / capacity;
    }

    private void putNode(K key, V value) {
        Node<K, V> node = getNode(key);

        if (node != null) {
            node.setValue(value);
        } else {
            int hash = getHash(key);
            Bucket<K, V> bucket = table[hash];
            if (bucket == null) {
                bucket = new Bucket<>();
                table[hash] = bucket;
            }
            bucket.add(new Node<>(key, value));
            size++;
        }
    }

    private Node<K, V> removeNode(K key) {
        Node<K, V> node = getNode(key);

        if (node != null) {
            int hash = getHash(key);
            Bucket<K, V> bucket = table[hash];
            bucket.remove(node);
            size--;
            return node;
        }
        return null;
    }

    /**
     * Return node by provided key if exists and null in other case.
     */
    private Node<K, V> getNode(K key) {
        checkKey(key);
        int hash = getHash(key);

        Bucket<K, V> bucket = table[hash];
        if (bucket != null) {
            for (Node<K, V> node : bucket.getNodes()) {
                if (node.getKey().equals(key)) {
                    return node;
                }
            }
        }
        return null;
    }

    private void checkResizeNeeded() {
        if (Float.compare(getCurrentLoadFactor(), loadFactor) >= 0) {
            int newCapacity = 2 * capacity + 1;
            resize(newCapacity);
        }
    }

    private int findSmallestPossibleCapacity() {
        int smallestCapacity = capacity;
        float newLoadFactor = (float) size / smallestCapacity;

        while (Float.compare(newLoadFactor, loadFactor) < 0) {
            smallestCapacity--;
            newLoadFactor = (float) size / smallestCapacity;
        }
        return smallestCapacity;
    }

    @SuppressWarnings({"unchecked"})
    private void resize(int newCapacity) {
        Bucket<K, V>[] oldTable = table;
        int oldSize = size;
        table = (Bucket<K, V>[]) new Bucket[newCapacity];

        for (Bucket<K, V> oldBucket : oldTable) {
            if (oldBucket != null) {
                for (Node<K, V> oldNode : oldBucket.getNodes()) {
                    putNode(oldNode.getKey(), oldNode.getValue());
                }
            }
        }
        capacity = newCapacity;
        size = oldSize;
    }

    private void checkKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
    }

    /**
     * Use 0xfffffff to get positive hash.
     */
    private int getHash(K key) {
        return (key.hashCode() & 0xfffffff) % capacity;
    }
}
