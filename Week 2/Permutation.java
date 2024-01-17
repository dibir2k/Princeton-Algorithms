import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        int k = 0;
        int index = 0;
        double extra = 1.;
        if (args.length > 0) k = Integer.parseInt(args[0]);

        while (index < k) {
            String word = StdIn.readString();
            rq.enqueue(word);
            index++;
        }

        while (k > 0 && !StdIn.isEmpty()) {
            String word = StdIn.readString();
            boolean accept = StdRandom.bernoulli(k/(k + extra));
            if (accept) {
                rq.dequeue();
                rq.enqueue(word);
            }
            extra = extra + 1.;
        }

        for (String string : rq) {
            StdOut.println(string);
        }
    }
    
}
