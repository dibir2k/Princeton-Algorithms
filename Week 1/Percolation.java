// import edu.princeton.cs.algs4.StdRandom;
// import edu.princeton.cs.algs4.StdStats;
// import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// import QuickUnionUF;

public class Percolation {
    private int n;
    private boolean[][] grid;
    private int openCounts;
    private int virtualTop;
    private int virtualBottom;
    private WeightedQuickUnionUF quf;
    private int[] rootLastRow;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be greater than zero!");

        this.n = n;
        openCounts = 0;
        grid = new boolean[n+1][n+1];
        virtualTop = 0;
        virtualBottom = 1;
        quf = new WeightedQuickUnionUF((n+1)*(n+1));
        
        for (int i = 0; i < n+1; i++) {
            for (int j = 0; j < n; j++) 
                grid[i][j] = false;
        } 
     }

    private int indexOf(int row, int col) {
        return row*(n+1) + col;
    }
    // private int root(int i){
    //     while(i != id[i]) i = id[i];
    //     return i;
    // }

    // private boolean connected(int p, int q) {
    //     return root(p) == root(q);
    // }

    // private void union(int p, int q){
    //     int i = root(p);
    //     int j = root(q);
    //     id[i] = j;
    // }

     // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) throw new IllegalArgumentException("row, col must be greater in range (1,n)!");
        
        if (!grid[row][col]) {
            grid[row][col] = true;
            openCounts++;
            int q = this.indexOf(row, col);
            int p;
            
            if (row > 1 && this.isOpen(row-1, col)) {
                p = this.indexOf(row-1, col); 
                quf.union(q, p);
            }
            if (row < n && this.isOpen(row+1, col)) {
                p = this.indexOf(row+1, col); 
                quf.union(p, q);
            }
            if (col > 1 && this.isOpen(row, col-1)) {
                p = this.indexOf(row, col-1); 
                quf.union(q, p);
            }
            if (col < n && this.isOpen(row, col+1)) {
                p = this.indexOf(row, col+1); 
                quf.union(p, q);
            }
            if (row == n) {
                quf.union(virtualBottom, q);
            }

            if (row == 1) {
                quf.union(q, virtualTop);
            }
        }
    }
    
 
     // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) throw new IllegalArgumentException("row, col must be greater in range (1,n)!");

        return grid[row][col];
     }
 
    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) throw new IllegalArgumentException("row, col must be greater in range (1,n)!");

        int p = indexOf(row, col);
        return (quf.find(p) == quf.find(virtualTop));
    }
 
    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCounts;
    }
 
    // does the system percolate?
    public boolean percolates() {
        return quf.find(virtualBottom) == quf.find(virtualTop);

    }
 
    // test client (optional)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);

        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            percolation.open(row, col);

            if (percolation.percolates()) {
                StdOut.printf("%nThe System percolates %n");

            }

            if (!percolation.percolates()) {
                StdOut.printf("Does not percolate %n");
            }
        }
        StdOut.println(percolation.isFull(3, 1));
    }
}