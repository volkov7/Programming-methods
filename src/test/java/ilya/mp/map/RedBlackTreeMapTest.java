package ilya.mp.map;

import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

public class RedBlackTreeMapTest {

    /**
     * Finally tree view:
     *              r
     *            /   \
     *          g      t
     *
     * Tree without balancing:
     *              t
     *            /
     *          g
     *           \
     *            r
     *
     */
    @Test
    public void putLittleTreeTest() {
        String expected = "grt";
        RedBlackTreeMap<String, Integer> tree = new RedBlackTreeMap<>();

        tree.put("t", 21);
        tree.put("g", 21);
        tree.put("r", 21);

        String treeRepresentation = tree.inorderTraversalString();
        Assert.assertEquals(expected, treeRepresentation);
    }

    /**
     * Finally tree view:
     *             --------------g---------------
     *             |                            |
     *          ---b---                     ----r----
     *          |     |                     |       |
     *          a     c---                --m--   --w--
     *                   |                |   |   |   |
     *                   f                h   q   t   y
     */
    @Test
    public void putBigTreeTest() {
        String expected = "abcfghmqrtwy";
        RedBlackTreeMap<String, Integer> tree = generateBigTree();

        String treeRepresentation = tree.inorderTraversalString();
        Assert.assertEquals(expected, treeRepresentation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNullTest() {
        RedBlackTreeMap<String, Integer> tree = new RedBlackTreeMap<>();
        tree.put(null, 21);
    }

    @Test
    public void isEmptyTest() {
        RedBlackTreeMap<String, Integer> tree = new RedBlackTreeMap<>();

        Assert.assertTrue(tree.isEmpty());
    }

    @Test
    public void isNotEmptyTest() {
        RedBlackTreeMap<String, Integer> tree = new RedBlackTreeMap<>();

        tree.put("s", 21);

        Assert.assertFalse(tree.isEmpty());
    }

    @Test
    public void clearTest() {
        RedBlackTreeMap<String, Integer> tree = new RedBlackTreeMap<>();

        tree.put("s", 21);
        Assert.assertFalse(tree.isEmpty());

        tree.clear();
        Assert.assertTrue(tree.isEmpty());
    }

    @Test
    public void containsExistingKeyTest() {
        RedBlackTreeMap<String, Integer> tree = new RedBlackTreeMap<>();
        String key = "s";

        tree.put(key, 21);

        Assert.assertTrue(tree.containsKey(key));
    }

    @Test
    public void containsNonExistingKeyTest() {
        RedBlackTreeMap<String, Integer> tree = new RedBlackTreeMap<>();
        String key = "s";
        String nonExistingKey = "whoami";

        tree.put(key, 21);

        Assert.assertFalse(tree.containsKey(nonExistingKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsNullTest() {
        RedBlackTreeMap<String, Integer> tree = new RedBlackTreeMap<>();
        tree.containsKey(null);
    }

    @Test
    public void getExistingTest() {
        RedBlackTreeMap<String, Integer> tree = generateBigTree();
        String key = "b";
        Integer expectedValue = 27;

        Assert.assertEquals(expectedValue, tree.get(key));
    }

    @Test
    public void getNonExistingTest() {
        RedBlackTreeMap<String, Integer> tree = generateBigTree();
        String nonExistingKey = "non-existing-key";

        Assert.assertNull(tree.get(nonExistingKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullKeyTest() {
        RedBlackTreeMap<String, Integer> tree = generateBigTree();
        tree.get(null);
    }

    @Test
    public void iterationTest() {
        String expected = "abcfghmqrtwy";
        RedBlackTreeMap<String, Integer> tree = generateBigTree();

        StringBuilder sb = new StringBuilder();
        for(RedBlackTreeMap.Node<String, Integer> node : tree.entryNode()) {
            sb.append(node.getKey());
        }
        String result = sb.toString();

        Assert.assertEquals(expected, result);
    }

    private RedBlackTreeMap<String, Integer> generateBigTree() {
        RedBlackTreeMap<String, Integer> tree = new RedBlackTreeMap<>();

        tree.put("t", 21);
        tree.put("g", 22);
        tree.put("r", 23);
        tree.put("y", 24);
        tree.put("a", 25);
        tree.put("q", 26);
        tree.put("b", 27);
        tree.put("c", 28);
        tree.put("w", 29);
        tree.put("h", 30);
        tree.put("m", 31);
        tree.put("f", 32);
        return tree;
    }
}