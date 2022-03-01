package ilya.mp.map;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RedBlackTreeMap<K extends Comparable<K>, V> {

    private static final boolean BLACK = true;
    private static final boolean RED = false;

    private Node<K, V> root;
    private int size;

    public RedBlackTreeMap() {
    }

    /**
     * Find node by specified key and return value.
     *
     * @return value if map contain given key, {@code null} otherwise.
     */
    public V get(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    /**
     * @return {@code true} if mat contain given key, {@code null} otherwise.
     */
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    /**
     * Adding new pair if map does not contain given key, otherwise update value.
     */
    public void put(K key, V value) {
        Node<K, V> freshNode = null;

        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null!");
        }
        if (root == null) {
            root = new Node<>(key, value, null);
            size++;
        } else {
            freshNode = putNode(root, key, value, null);
        }
        if (freshNode != null) {
            balanceAfterPut(freshNode);
            size++;
        }
    }

    /**
     * Returns this map's node for the given key, or {@code null} if the map
     * does not contain node for the key.
     *
     * @return tree node.
     * @throws IllegalArgumentException if the specified key is null.
     */
    private Node<K, V> getNode(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null!");
        }
        Node<K, V> node = root;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0)
                node = node.left;
            else if (cmp > 0)
                node = node.right;
            else
                return node;
        }
        return null;
    }

    /**
     * Recursively adding new pair [key:value] into the tree.
     *
     * @param node current node.
     * @param key key
     * @param value value
     * @param parent parent node for node param.
     * @return null if the key already exists. And a new node if the key is new.
     */
    private Node<K, V> putNode(Node<K, V> node, K key, V value, Node<K, V> parent) {
        if (node == null) {
            Node<K, V> freshNode = new Node<>(key, value, parent);
            if (key.compareTo(parent.key) < 0) {
                parent.left = freshNode;
            } else {
                parent.right = freshNode;
            }
            return freshNode;
        }

        if (key.compareTo(node.key) < 0) {
            return putNode(node.left, key, value, node);
        } else if (key.compareTo(node.key) > 0) {
            return putNode(node.right, key, value, node);
        } else {
            node.value = value;
            return null;
        }
    }

    private void balanceAfterPut(Node<K, V> freshNode) {
        freshNode.color = RED;

        while (freshNode != root && freshNode.parent != null && freshNode.parent.color == RED) {
            Node<K, V> father = freshNode.parent;
            Node<K, V> grandFather = father.parent;

            if (grandFather != null && grandFather.left == father) {
                Node<K, V> uncle = grandFather.right;

                // If node is null then it has BLACK color
                if (uncle != null && uncle.color == RED) {
                    grandFather.color = RED;
                    uncle.color = BLACK;
                    father.color = BLACK;
                    freshNode = grandFather;
                } else {
                    if (father.right == freshNode) {
                        leftRotate(father);
                        freshNode = father;
                    }
                    rightRotate(grandFather);
                }
            } else {
                Node<K, V> uncle = grandFather == null ? null : grandFather.left;

                // If node is null then it has BLACK color
                if (uncle != null && uncle.color == RED) {
                    grandFather.color = RED;
                    uncle.color = BLACK;
                    father.color = BLACK;
                    freshNode = grandFather;
                } else {
                    if (father.left == freshNode) {
                        rightRotate(father);
                        freshNode = father;
                    }
                    leftRotate(grandFather);
                }
            }
        }
        root.color = BLACK;
    }

    /**
     * Take the node as the rotation point and perform left rotation.
     *
     * @param node - rotation point.
     */
    private void leftRotate(Node<K, V> node) {
        if (node != null) {
            Node<K, V> right = node.right;

            right.color = node.color;
            node.color = RED;
            node.right = right.left;
            if (right.left != null) {
                right.left.parent = node;
            }
            right.parent = node.parent;
            if (node.parent == null) {
                root = right;
            } else if (node.parent.left == node) {
                node.parent.left = right;
            } else {
                node.parent.right = right;
            }
            right.left = node;
            node.parent = right;
        }
    }

    /**
     * Take the node as the rotation point and perform right rotation.
     *
     * @param node - rotation point.
     */
    private void rightRotate(Node<K, V> node) {
        Node<K, V> left = node.left;

        left.color = node.color;
        node.color = RED;
        node.left = left.right;
        if (left.right != null) {
            left.right.parent = node;
        }
        left.parent = node.parent;
        if (node.parent == null) {
            root = left;
        } else if (node.parent.left == node) {
            node.parent.left = left;
        } else {
            node.parent.right = left;
        }
        left.right = node;
        node.parent = left;
    }

    public EntryNode entryNode() {
        return new EntryNode();
    }

    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    public void clear() {
        size = 0;
        root = null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    /**
     * WARNING! USED ONLY FOR TEST PURPOSE.
     *
     * @return inorder string representation of the tree.
     */
    public String inorderTraversalString() {
        StringBuffer stringBuffer = new StringBuffer();
        inorderTraversal(root, stringBuffer);
        return stringBuffer.toString();
    }

    private void inorderTraversal(Node<K, V> node, StringBuffer stringBuffer) {
        if (node == null) {
            return;
        }

        inorderTraversal(node.left, stringBuffer);
        stringBuffer.append(node.key);
        inorderTraversal(node.right, stringBuffer);
    }

    // New node is always black
    static final class Node<K extends Comparable<K>, V> {
        private K key;
        private V value;
        private boolean color;
        private Node<K, V> parent;
        private Node<K, V> left;
        private Node<K, V> right;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.color = BLACK;
            this.parent = parent;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    class EntryNode implements Iterable<Node<K, V>> {
        @Override
        public Iterator<Node<K, V>> iterator() {
            return new InorderTreeIterator(root);
        }
    }

    abstract class TreeIterator<T> implements Iterator<T> {
        protected Node<K, V> next;

        public TreeIterator(Node<K, V> root) {
            this.next = root;

            if (next == null) {
                return;
            }
            while (next.left != null) {
                next = next.left;
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Currently not implemented.");
        }
    }

    final class InorderTreeIterator extends TreeIterator<Node<K, V>> {
        public InorderTreeIterator(Node<K, V> root) {
            super(root);
        }

        @Override
        public Node<K, V> next() {
            Node<K, V> e = next;

            if (e == null) {
                throw new NoSuchElementException();
            }
            // If you can walk right, walk right, then fully left.
            // otherwise, walk up until you come from left.
            if(next.right != null) {
                next = next.right;
                while (next.left != null)
                    next = next.left;
                return e;
            }

            while(true) {
                if(next.parent == null) {
                    next = null;
                    return e;
                }
                if(next.parent.left == next) {
                    next = next.parent;
                    return e;
                }
                next = next.parent;
            }
        }
    }
}
