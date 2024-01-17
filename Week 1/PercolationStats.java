// import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


// import java.lang.Math;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private int trialCount;
    private double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n and trials must be greater than zero!");

        int gridSize = n;
        int totalSites = n*n;
        trialCount = trials;
        results = new double[trials];

        for (int i = 0; i < trialCount; i++) {
            Percolation percolation = new Percolation(gridSize);
            while (!percolation.percolates() && percolation.numberOfOpenSites() < totalSites) {
                int newRow = StdRandom.uniformInt(1, gridSize+1);
                int newCol = StdRandom.uniformInt(1, gridSize+1);
                percolation.open(newRow, newCol);
            }
            results[i] = (double) percolation.numberOfOpenSites() / totalSites;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        // double sumResults = 0;
        // for(int i = 0; i < trialCount; i++) {
        //     sumResults += results[i];   
        // }
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        // double mean = mean();
        // double s2 = 0;
        // for(int i = 0; i < trialCount; i++) {
        //     s2 += (results[i] - mean)*(results[i] - mean);
        // } 
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double s = Math.sqrt(stddev());
        double trialCountSqrt = Math.sqrt(trialCount);

        return mean() - CONFIDENCE_95 * s / trialCountSqrt;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double s = Math.sqrt(stddev());
        double trialCountSqrt = Math.sqrt(trialCount);

        return mean() + CONFIDENCE_95 * s / trialCountSqrt;
    }

   // test client (see below)
   public static void main(String[] args) {
        // if (args.length != 2) {
        //     System.out.println("Please provide two integers: grid size and number of trials");
        //     System.exit(0);
        // }
        int n = 20; 
        int trials = 10;

        if (args.length >= 2) {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        }

        PercolationStats percolationStats = new PercolationStats(n, trials);

        double mean = percolationStats.mean();
        double stddev = percolationStats.stddev();
        double confidenceLow = percolationStats.confidenceLo();
        double confidenceHigh = percolationStats.confidenceHi();

        StdOut.printf("Mean = %f%n", mean);
        StdOut.printf("Stddev = %f%n", stddev);
        StdOut.printf("95%% confidence interval = [%f , %f]%n", confidenceLow, confidenceHigh);
        StdOut.println();
            
        
   }
}
