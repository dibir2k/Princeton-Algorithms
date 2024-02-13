// package WordNet.src;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private final List<String> sets;
    private final SAP sap;
    private final HashMap<String, List<Integer>> map;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException("Null arguments provided.");

        sets = new ArrayList<>();
        map = new HashMap<>();

        List<Integer> ids = new ArrayList<>();

        List<Integer> roots = new ArrayList<>();
        In in = new In(synsets);

        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] entry = line.split(",");
            sets.add(entry[1]);
            String[] strs = entry[1].split(" ");
            for (int i = 0; i < strs.length; i++) {
                ids = map.get(strs[i]);
                if (ids == null) ids = new ArrayList<>();
                ids.add(Integer.parseInt(entry[0]));
                ids = map.put(strs[i], ids);
            }
        }
        int v = sets.size();

        Digraph wnGraph = new Digraph(v);

        In in2 = new In(hypernyms);

        while (!in2.isEmpty()) {
            String line = in2.readLine();
            String[] edges = line.split(",");
            if (edges.length == 1) roots.add(Integer.parseInt(edges[0]));
            
            for (int i = 1; i < edges.length; i++) {
                int v1 = Integer.parseInt(edges[0]);
                int v2 = Integer.parseInt(edges[i]);
                wnGraph.addEdge(v1, v2);
            }
        }
        DirectedCycle cGraph = new DirectedCycle(wnGraph);

        if (cGraph.hasCycle() || roots.size() != 1) throw new IllegalArgumentException("Not a rooted DAG"); 

        sap = new SAP(wnGraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Null argument provided.");
        if (map.get(word) != null) return true;
        return false;
    }

    private List<Integer> getVs(String noun) {
        return map.get(noun);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Null or not in WordNet");

        List<Integer> vA = getVs(nounA);
        List<Integer> vB = getVs(nounB);

        return sap.length(vA, vB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Null or not in WordNet");

        List<Integer> vA = getVs(nounA);
        List<Integer> vB = getVs(nounB);

        int ancestor = sap.ancestor(vA, vB);

        return sets.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = "WordNet/Files/synsets.txt";
        String hypernyms = "WordNet/Files/hypernyms.txt";

        WordNet wn = new WordNet(synsets, hypernyms);

        // for (String noun : wn.nouns()) {
        //     StdOut.println(noun + "\n");
        // }

        String word = "yolk";
        boolean isThere = wn.isNoun(word);

        StdOut.printf("Word: %s is in WordNet? %b\n", word, isThere);

        String nounA = "yolk";

        String nounB = "century";

        int dist = wn.distance(nounA, nounB);
        
        StdOut.printf("Distance between %s and %s is: %d\n", nounA, nounB, dist);

        String sap = wn.sap(nounA, nounB);

        StdOut.printf("SAP between %s and %s is: %s\n", nounA, nounB, sap);

    }
}
