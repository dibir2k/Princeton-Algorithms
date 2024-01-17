import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int n = 0;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }
    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null item");
        Node newItem = new Node();
        newItem.item = item;
        newItem.next = null;
        if (n == 0) {
            first = newItem;
            last = newItem;
        }
        else {
            first.prev = newItem;
            newItem.next = first;
            first = newItem;
        }
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null item");
        Node newItem = new Node();
        newItem.item = item;
        newItem.next = null;
        if (n == 0) {
            first = newItem;
            last = newItem;
        }
        else {
            last.next = newItem;
            newItem.prev = last;
            last = newItem;
        }
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (n == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        Item item = first.item;
        first = first.next;
        if (isEmpty()) last = null;
        else first.prev = null;
        n--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (n == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        Item item = last.item;
        last = last.prev;
        if (last != null) {
            last.next = null;
        } else {
            first = null;
        }
        n--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() { 
            return current != null; 
        }

        public void remove() { throw new UnsupportedOperationException("Method remove not supported."); }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more items to return.");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> myDeque = new Deque<String>();

        StdOut.printf("The list is empty: %b%n", myDeque.isEmpty());

        myDeque.addFirst("Car");
        
        StdOut.printf("Current Size of deque = %d%n", myDeque.size());

        myDeque.addFirst("Bike");

        myDeque.addLast("Plane");

        myDeque.addLast("Train");

        StdOut.printf("Current Size of deque = %d%n", myDeque.size());

        for (String string : myDeque) {
            StdOut.println(string);
        }

        myDeque.removeFirst();

        StdOut.printf("Current Size of deque = %d%n", myDeque.size());

        for (String string : myDeque) {
            StdOut.println(string);
        }

        myDeque.removeLast();

        StdOut.printf("Current Size of deque = %d%n", myDeque.size());

        for (String string : myDeque) {
            StdOut.println(string);
        }

        myDeque.removeFirst();

        myDeque.removeLast();

        StdOut.printf("Current Size of deque = %d%n", myDeque.size());

        for (String string : myDeque) {
            StdOut.println(string);
        }

    }
}
