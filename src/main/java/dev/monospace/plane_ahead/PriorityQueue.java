package dev.monospace.plane_ahead;

public class PriorityQueue<T, S extends Comparable<? super S>> {

    private Object[] array;
    private int count;


    public PriorityQueue() {
        this(10);
    }

    public PriorityQueue(int initSize) {
        array = new Object[initSize];
        count = 0;
    }

    public void enqueue(T item, S priority) {
        ensureCapacity();
        Entry<T, S> entry = new Entry<>(item, priority);
        array[count] = entry;
        int curr = count;
        while (curr > 0) {
            int parent = (curr - 1) / 2;
            if (entry.compareTo(get(parent)) > 0) {
                // entry > parent
                array[curr] = array[parent];
                curr = parent;
            } else break;
        }
        array[curr] = entry;
        count++;
    }

    public T dequeue() {
        if (count == 0) {
            return null;
        }
        Entry<T, S> max = get(0);
        Entry<T, S> last = get(--count);
        array[count] = null;
        array[0] = last;
        int curr = 0;
        while (curr < count) {
            int child = curr * 2 + 1;
            if (indexFilled(child)) {
                if (indexFilled(child + 1) && get(child).compareTo(get(child + 1)) < 0) {
                    // left < right
                    child += 1;
                }
                if (get(child).compareTo(last) > 0) {
                    array[curr] = array[child];
                    curr = child;
                } else break;
            } else break;
        }
        array[curr] = last;
        return max.item;
    }

    public int size() {
        return count;
    }

    private boolean indexFilled(int index) {
        return index < array.length && array[index] != null;
    }

    @SuppressWarnings("unchecked")
    private Entry<T, S> get(int index) {
        return (Entry<T, S>) array[index];
    }

    private void ensureCapacity() {
        if (count >= array.length) {
            Object[] newArray = new Object[array.length * 2];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
    }

    public static class Entry<T, S extends Comparable<? super S>> implements Comparable<Entry<T, S>> {
        private final T item;
        private final S priority;

        public Entry(T item, S priority) {
            this.item = item;
            this.priority = priority;
        }

        @Override
        public int compareTo(Entry<T, S> o) {
            return this.priority.compareTo(o.priority);
        }

        @Override
        public String toString() {
            return "(" + item + ", " + priority + ')';
        }
    }
}
