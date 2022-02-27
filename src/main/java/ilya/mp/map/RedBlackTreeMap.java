package ilya.mp.map;

public class RedBlackTreeMap<K extends Comparable<K>, V> {

    private static final boolean BLACK = true;
    private static final boolean RED = false;

    private Node<K, V> root;
    private int size;

    public RedBlackTreeMap() {
    }

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
    static class Node<K extends Comparable<K>, V> {
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
    }
}
