package ilya.mp.priority_queue;

import java.util.ArrayList;
import java.util.List;

/**
 * PriorityQueue implementation based on binary heap.
 */
public class PriorityQueue<T extends Comparable<T>> {
    private final List<T> heap;

    public PriorityQueue() {
        this.heap = new ArrayList<>();
    }

    /**
     * Add new element to the end then place this element to the right place in the heap.
     *
     * @param element - new element.
     * @throws IllegalArgumentException in case if element is null.
     */
    public void insert(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null!");
        }

        heap.add(element);
        upHeap(heap.size() - 1);
    }

    /**
     * Remove max element from the heap and restore heap.
     *
     * @return max element
     */
    public T popMax() {
        checkEmptyQueue();
        if (heap.size() == 1) {
            return heap.remove(0);
        }
        T max = heap.get(0);
        T last = heap.remove(heap.size() - 1);
        heap.set(0, last);
        downHeap(0);
        return max;
    }

    /**
     * Max element is always root of the heap.
     *
     * @return max element.
     */
    public T getMax() {
        checkEmptyQueue();
        return heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    /**
     * Raises element at index i up to right position so that heap property is restored.
     *
     * @param i - index of the element in the heap to rise.
     */
    private void upHeap(int i) {
        while (i >= 1) {
            int parent = (i - 1) / 2;
            if (heap.get(i).compareTo(heap.get(parent)) > 0) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Moves element down by index to right position so that heap property is restored.
     *
     * @param i index of the element in the heap to move down.
     */
    private void downHeap(int i) {
        int greatest = i;
        while (i < heap.size()) {
            int lChild = 2 * i + 1;
            int rChild = 2 * i + 2;

            if (lChild < heap.size() && heap.get(lChild).compareTo(heap.get(i)) > 0) {
                greatest = lChild;
            }
            if (rChild < heap.size() && heap.get(rChild).compareTo(heap.get(greatest)) > 0) {
                greatest = rChild;
            }
            if (greatest == i) {
                return;
            }
            swap(i, greatest);
            i = greatest;
        }
    }

    private void swap(int i, int j) {
        T tmp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, tmp);
    }

    private void checkEmptyQueue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty!");
        }
    }
}
