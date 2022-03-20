package ilya.mp.hash_map;

import java.util.LinkedList;
import java.util.List;

public class Bucket<K, V> {
    private final List<Node<K, V>> nodes;

    public Bucket() {
        nodes = new LinkedList<>();
    }

    public List<Node<K, V>> getNodes() {
        return nodes;
    }

    public void add(Node<K, V> freshNode) {
        nodes.add(freshNode);
    }

    public void remove(Node<K, V> toRemove) {
        nodes.remove(toRemove);
    }
}
