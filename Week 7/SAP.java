// package WordNet.src;

import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;
import java.util.List;

// import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph sap;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Null argument provided.");

        sap = new Digraph(G);

        // DirectedCycle cGraph = new DirectedCycle(sap);

        // boolean isRoot = false;

        // for (int v = 0; v < sap.V(); v++) {
        //     if (sap.outdegree(v) == 0) isRoot = true;
        // }

        // if (cGraph.hasCycle() && !isRoot) throw new IllegalArgumentException("Does not correspond to a rooted DAG");
    }

    private int[] calcLengthDist(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfdsA = new BreadthFirstDirectedPaths(sap, v);
        BreadthFirstDirectedPaths bfdsB = new BreadthFirstDirectedPaths(sap, w);

        int dist = sap.E() + 1;
        int newDist = -1;
        int sca = sap.V() + 1;

        for (int i = 0; i < sap.V(); i++) {
            newDist = bfdsA.distTo(i) + bfdsB.distTo(i);
            if (newDist >= 0 && newDist < dist) {
                dist = newDist;
                sca = i;
            }
        }

        return new int[]{dist, sca};
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= sap.V() || w < 0 || w >= sap.V()) throw new IllegalArgumentException("Out of range");

        List<Integer> vList = new ArrayList<>();
        vList.add(v);
        List<Integer> wList = new ArrayList<>();
        wList.add(w);

        int length = calcLengthDist(vList, wList)[0];

        if (length == sap.E() + 1) return -1;
        else return length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= sap.V() || w < 0 || w >= sap.V()) throw new IllegalArgumentException("Out of range");

        List<Integer> vList = new ArrayList<>();
        vList.add(v);
        List<Integer> wList = new ArrayList<>();
        wList.add(w);
        int sca = calcLengthDist(vList, wList)[1];

        if (sca == sap.V() + 1) return -1;
        else return sca;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("Null");
        int counterv = 0;
        int counterw = 0;

        for (Object ob : v) {
            if (ob == null) throw new IllegalArgumentException("Null");
        }

        for (Object obj : w) {
            if (obj == null) throw new IllegalArgumentException("Null");
        }

        for (int i : v) {
            counterv += 1;
            if (i < 0 || i >= sap.V()) throw new IllegalArgumentException("Out of range");
        }

        for (int j : w) {
            counterw += 1;
            if (j < 0 || j >= sap.V()) throw new IllegalArgumentException("Out of range");
        }

        if (counterv == 0 || counterw == 0) return -1;

        int length = calcLengthDist(v, w)[0];

        if (length == sap.E() + 1) return -1;
        else return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("Null");
        int counterv = 0;
        int counterw = 0;

        for (Object o : v) {
            if (o == null) throw new IllegalArgumentException("Null");
        }

        for (Object obj : w) {
            if (obj == null) throw new IllegalArgumentException("Null");
        }


        for (int i : v) {
            counterv++;
            if (i < 0 || i >= sap.V()) throw new IllegalArgumentException("Out of range");
        }

        for (int j : w) {
            counterw++;
            if (j < 0 || j >= sap.V()) throw new IllegalArgumentException("Out of range");
        }

        if (counterv == 0 || counterw == 0) return -1;

        int sca = calcLengthDist(v, w)[1];

        if (sca == sap.V() + 1) return -1;
        else return sca;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        // In in = new In("/Users/diogobirra/Desktop/Coursera-Algorithms/WordNet/Files/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
