// package WordNet.src;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wn;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wn = wordnet;
    } 
    
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDist = -1;
        int dist;
        String outcast = null;
        for (int i = 0; i < nouns.length; i++) {
            dist = 0;
            for (int j = 0; j < nouns.length; j++) {
                dist += wn.distance(nouns[i], nouns[j]);
            }
            if (dist > maxDist) {
                maxDist = dist;
                outcast = nouns[i];
            }
        }
        return outcast;
    }
    
    // see test client below
    public static void main(String[] args) {
        // String args0 = "/Users/diogobirra/Desktop/Coursera-Algorithms/WordNet/Files/synsets.txt";
        // String args1 = "/Users/diogobirra/Desktop/Coursera-Algorithms/WordNet/Files/hypernyms.txt";
        // String args2 = "/Users/diogobirra/Desktop/Coursera-Algorithms/WordNet/Files/outcast5.txt";
        // String args3 = "/Users/diogobirra/Desktop/Coursera-Algorithms/WordNet/Files/outcast8.txt";
        // String args4 = "/Users/diogobirra/Desktop/Coursera-Algorithms/WordNet/Files/outcast11.txt";

        // String[] arg = new String[5];
        // arg[0] = args0; arg[1] = args1; arg[2] = args2; arg[3] = args3; arg[4] = args4;

        WordNet wordnet = new WordNet(args[0], args[1]);
        // WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
