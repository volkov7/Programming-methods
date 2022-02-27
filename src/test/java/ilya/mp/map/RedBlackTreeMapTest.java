package ilya.mp.map;

import org.junit.Assert;
import org.junit.Test;

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
        RedBlackTreeMap<String, Integer> tree = new RedBlackTreeMap<>();

        tree.put("t", 21);
        tree.put("g", 21);
        tree.put("r", 21);
        tree.put("y", 21);
        tree.put("a", 21);
        tree.put("q", 21);
        tree.put("b", 21);
        tree.put("c", 21);
        tree.put("w", 21);
        tree.put("h", 21);
        tree.put("m", 21);
        tree.put("f", 21);

        String treeRepresentation = tree.inorderTraversalString();
        Assert.assertEquals(expected, treeRepresentation);
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
}