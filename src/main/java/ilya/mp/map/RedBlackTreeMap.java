package ilya.mp.map;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RedBlackTreeMap<K extends Comparable<K>, V> implements Iterable<RedBlackTreeMap.Node<K, V>> {

    private static final boolean BLACK = true;
    private static final boolean RED = false;

    private Node<K, V> root;
    private int size;

    public RedBlackTreeMap() {
    }

    /**
     * Copy constructor. Order of the previous tree is not saved.
     */
    public RedBlackTreeMap(RedBlackTreeMap<K, V> source) {
        for (Node<K, V> kvNode : source) {
            put(kvNode.getKey(), kvNode.getValue());
        }
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
     * @return {@code true} if map contain given key, {@code null} otherwise.
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
     * Removing node by given key.
     *
     * @return value by key.
     */
    public V remove(K key) {
        Node<K, V> toRemove = getNode(key);

        if (toRemove == null) {
            return null;
        }
        V oldValue = toRemove.value;
        deleteNode(toRemove);
        size--;
        return oldValue;
    }

    private void deleteNode(Node<K, V> node) {
        // node is always not null
        if (node.left != null && node.right != null) {
            Node<K, V> precursor = precursorOf(node);
            node.key = precursor.key;
            node.value = precursor.value;
            node = precursor;
        }
        Node<K, V> replace = node.left != null ? node.left : node.right;
        if (replace != null) {
            replace.parent = node.parent;
            if (node.parent == null) {
                root = replace;
            } else if (node.parent.left == node) {
                node.parent.left = replace;
            } else {
                node.parent.right = replace;
            }
            node.left = null;
            node.right = null;
            node.parent = null;
            if (node.color == BLACK) {
                balanceAfterRemove(replace);
            }
        } else {
            if (node.color == BLACK) {
                balanceAfterRemove(node);
            }
            if (node.parent != null) {
                if (node.parent.left == node) {
                    node.parent.left = null;
                } else {
                    node.parent.right = null;
                }
                node.parent = null;
            } else {
                root = null;
            }
        }
    }

    private void balanceAfterRemove(Node<K,V> node) {
        while (node != root && getColor(node) == BLACK) {
            // Delete the left node whose node is the parent node
            if (leftOf(parentOf(node)) == node) {
                Node<K, V> rBro = rightOf(parentOf(node));
                // brother is red, situation 4
                if (getColor(rBro) == RED) {
                    leftRotate(parentOf(node));
                    rBro = rightOf(parentOf(node));
                }
                // Brother is black and there is no red node, situation 5
                if (getColor(leftOf(rBro)) == BLACK && getColor(rightOf(rBro)) == BLACK) {
                    setColor(rBro, RED);
                    node = parentOf(node);
                } else {
                    // Brothers are black with red nodes 1-3
                    if (getColor(rightOf(rBro)) == BLACK) {
                        rightRotate(rBro);
                        rBro = rightOf(parentOf(node));
                    }
                    leftRotate(parentOf(node));
                    setColor(parentOf(node), BLACK);
                    setColor(rightOf(rBro), BLACK);
                    break;
                }
            } else {
                // Delete the right node whose node is the parent node
                Node<K, V> lBro = leftOf(parentOf(node));
                if (getColor(lBro) == RED) {
                    rightRotate(parentOf(node));
                    lBro = leftOf(node);
                }
                if (getColor(rightOf(lBro)) == BLACK && getColor(leftOf(lBro)) == BLACK) {
                    setColor(lBro, RED);
                    node = parentOf(node);
                } else {
                    // Two red or one red
                    if (getColor(leftOf(lBro)) == BLACK) {
                        leftRotate(lBro);
                        lBro = leftOf(parentOf(node));
                    }
                    rightRotate(parentOf(node));
                    setColor(parentOf(node), BLACK);
                    setColor(leftOf(lBro), BLACK);
                    break;
                }
            }
        }
        //The alternative borrowing point is red and directly turns black. There must be no node below this node
        setColor(node, BLACK);
    }

    // It is black when the node is empty because the last layer is black.
    private boolean getColor(Node<K, V> node) {
        return node == null ? BLACK : node.color;
    }

    private void setColor(Node<K, V> node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }

    private Node<K, V> parentOf(Node<K, V> child) {
        return child == null ? null : child.parent;
    }

    private Node<K, V> leftOf(Node<K, V> node) {
        return node == null ? null : node.left;
    }

    private Node<K, V> rightOf(Node<K, V> node) {
        return node == null ? null : node.right;
    }

    /**
     * Find precursor of the given node.
     *
     * @param node node to delete.
     * @return precursor.
     */
    private Node<K, V> precursorOf(Node<K, V> node) {
        // Since the node has two child nodes, there is no need to judge
        // whether the left node is empty
        Node<K, V> left = node.left;

        while (left.right != null) {
            left = left.right;
        }
        return left;
    }

    /**
     * Find the successor of the current node.
     *
     * @param node - node to delete.
     * @return successor.
     */
    private Node<K, V> successorOf(Node<K, V> node) {
        // Since the node node has two child nodes, there is no need to judge
        // whether the right side of the node is empty
        Node<K, V> right = node.right;

        while (right.left != null) {
            right = right.left;
        }
        return right;
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

    @Override
    public Iterator<Node<K, V>> iterator() {
        return new InorderTreeIterator(root);
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
    static final class Node<K, V> {
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

    /**
     * Base class for TreeMap iterators.
     */
    abstract class TreeIterator<T> implements Iterator<T> {
        protected Node<K, V> next;
        protected Node<K, V> lastReturned;

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
            if (lastReturned == null) {
                throw new IllegalStateException("Last returned element is null");
            }
            deleteNode(lastReturned);
            lastReturned = null;
            size--;
        }
    }

    final class InorderTreeIterator extends TreeIterator<RedBlackTreeMap.Node<K, V>> {
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
                lastReturned = e;
                return e;
            }

            while(true) {
                if(next.parent == null) {
                    next = null;
                    lastReturned = e;
                    return e;
                }
                if(next.parent.left == next) {
                    next = next.parent;
                    lastReturned = e;
                    return e;
                }
                next = next.parent;
            }
        }
    }
}
