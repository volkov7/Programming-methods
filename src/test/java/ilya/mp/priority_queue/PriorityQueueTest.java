package ilya.mp.priority_queue;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PriorityQueueTest {

    @Test
    public void insertTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.insert(2);
        queue.insert(8);
        queue.insert(12);
        queue.insert(3);
        queue.insert(5);

        assertFalse(queue.isEmpty());
        assertEquals(5, queue.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertNullTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.insert(null);
    }

    @Test
    public void popMaxTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.insert(2);
        queue.insert(8);
        queue.insert(12);
        queue.insert(3);
        queue.insert(5);

        assertEquals(Integer.valueOf(12), queue.popMax());
        assertEquals(Integer.valueOf(8), queue.popMax());
        assertEquals(3, queue.size());
    }

    @Test(expected = IllegalStateException.class)
    public void popMaxWithEmptyQueueTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.popMax();
    }

    @Test
    public void getMaxTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.insert(2);
        queue.insert(8);
        queue.insert(12);
        queue.insert(3);
        queue.insert(5);

        assertEquals(Integer.valueOf(12), queue.getMax());
    }

    @Test(expected = IllegalStateException.class)
    public void getMaxWithEmptyQueueTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.getMax();
    }

    @Test
    public void isEmptyTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void isNotEmptyTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.insert(2);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void sizeEmptyTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        assertEquals(0, queue.size());
    }

    @Test
    public void sizeNotEmptyTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.insert(2);
        assertEquals(1, queue.size());
    }


}