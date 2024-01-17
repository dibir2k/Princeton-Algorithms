import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] s;
    private int n = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot enqueue null item.");
        if (n == s.length) resize(2*s.length);
        s[n++] = item;
    }

    private void resize(int capacity) {
        if (capacity == 0) return;
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++)
        copy[i] = s[i];
        s = copy;
    }

    // remove and return a random item
    public Item dequeue() {
        if (n == 0) throw new NoSuchElementException("Queue is already empty");
        int idx = StdRandom.uniformInt(n);
        Item item = s[idx];
        s[idx] = s[n-1];
        s[--n] = null;
        if (n == s.length/4) resize(s.length/2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (n == 0) throw new NoSuchElementException("Queue is already empty");
        int idx = StdRandom.uniformInt(n);
       
        return s[idx];
    }

    private Item sampleAt(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException("i is out of bounds.");
        if (n == 0) throw new NoSuchElementException("Queue is empty");
        return s[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(this);
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private RandomizedQueue<Item> rq;
        private int[] idxs;
        private int i;

        public RandomizedQueueIterator(RandomizedQueue<Item> rq) {
            this.rq = rq;
            i = 0;
            idxs = new int[rq.size()];
            shuffle();
        }

        private void shuffle() {
            for (int k = 0; k < n; k++) {
                idxs[k] = k;
            }
            for (int j = 0; j < n; j++) {
                int r = j + StdRandom.uniformInt(n-j);     // between i and n-1
                int temp = idxs[j];
                idxs[j] = idxs[r];
                idxs[r] = temp;
            }
        }

        public boolean hasNext() { 
            return i < n; 
        }

        public void remove() { throw new UnsupportedOperationException("Method remove not supported."); }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more items to return.");
            Item item = rq.sampleAt(idxs[i++]);
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rqueue = new RandomizedQueue<String>();

        StdOut.printf("The list is empty: %b%n", rqueue.isEmpty());

        rqueue.enqueue("Car");
        
        StdOut.printf("Current Size = %d%n", rqueue.size());

        rqueue.enqueue("Bike");

        String sampledString = rqueue.sample();

        StdOut.printf("Sampled string = %s%n", sampledString);

        rqueue.enqueue("Train");

        rqueue.enqueue("Motorbike");

        StdOut.printf("Current Size = %d%n", rqueue.size());

        for (String string : rqueue) {
            StdOut.println(string);
        }

        String removedString = rqueue.dequeue();

        StdOut.printf("Current Size = %d%n", rqueue.size());
        StdOut.printf("Removed String = %s%n", removedString);

        for (String string : rqueue) {
            StdOut.println(string);
        }

        for (String string : rqueue) {
            StdOut.println("out iterator = " + string);
            for (String word : rqueue) {
                StdOut.println("in iterator = " + word);
            }
        }
    }

}