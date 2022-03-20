package ilya.mp.hash_map;

import org.junit.Assert;
import org.junit.Test;

public class HashMapTest {

    @Test
    public void putTest() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put("First", 1);
        Assert.assertEquals(1, map.getSize());
        map.put("Second", 2);
        Assert.assertEquals(2, map.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNullTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(null, 3);
    }

    @Test
    public void removeTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("First", 1);
        map.put("Second", 2);
        map.put("Third", 3);

        Assert.assertEquals(3, map.getSize());
        Integer secondValue = map.remove("Second");
        Assert.assertEquals(Integer.valueOf(2), secondValue);
        Assert.assertEquals(2, map.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("First", 1);
        map.put("Second", 2);
        map.put("Third", 3);

        map.remove(null);
    }

    @Test
    public void removeNotExistingTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("First", 1);
        map.put("Second", 2);
        map.put("Third", 3);

        Integer result = map.remove("Not Existing");
        Assert.assertNull(result);
        Assert.assertEquals(3, map.getSize());
    }

    @Test
    public void getTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("First", 1);
        map.put("Second", 2);
        map.put("Third", 3);

        Assert.assertEquals(Integer.valueOf(3), map.get("Third"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.get(null);
    }

    @Test
    public void getNotExistingTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("First", 1);
        map.put("Second", 2);
        map.put("Third", 3);

        Assert.assertNull(map.get("Not Existing"));
    }

    @Test
    public void clearTest() {
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            map.put(Integer.toString(i), i);
        }

        Assert.assertEquals(3, map.getSize());
        map.clear();
        Assert.assertEquals(0, map.getSize());
    }

    @Test
    public void resizeTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.setLoadFactor(0.25f);
        map.put("First", 1);
        map.put("Second", 2);
        map.put("Third", 3);

        float beforeResizeLoadFactor = map.getCurrentLoadFactor();
        map.put("Fourth", 4);
        map.put("Fifth", 5);
        float afterResizeLoadFactor = map.getCurrentLoadFactor();
        Assert.assertTrue(Float.compare(beforeResizeLoadFactor, afterResizeLoadFactor) > 0);
    }
}