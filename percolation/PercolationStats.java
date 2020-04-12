/* *****************************************************************************
 *  Name: Tarun Punhani
 *  Date: 12/04/2020
 *  Description: Percolation assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96d;
    private final double [] openSitesFractions;
    private final int noOfTrials;
    private double mean;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        noOfTrials = trials;
        openSitesFractions = new double [trials];

        for (int i = 0; i < trials; i++) {
            int counter = 0;
            Percolation percolation = new Percolation(n);
            boolean flag = false;
            while (!flag) {
                int randomNumber1 = StdRandom.uniform(1, n+1);
                int randomNumber2 = StdRandom.uniform(1, n+1);
                if (!percolation.isOpen(randomNumber1, randomNumber2)) {
                    percolation.open(randomNumber1, randomNumber2);
                    counter++;
                }
                flag = percolation.percolates();
            }
            openSitesFractions[i] = (double) counter / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        mean = StdStats.mean(openSitesFractions);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSitesFractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - ((PercolationStats.CONFIDENCE_95 * stddev()) / Math.sqrt(noOfTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + ((PercolationStats.CONFIDENCE_95 * stddev()) / Math.sqrt(noOfTrials));
    }


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);

        StdOut.println("mean                    = "+percolationStats.mean());
        StdOut.println("stddev                  = "+percolationStats.stddev());
        StdOut.println("95% confidence interval = ["+percolationStats.confidenceLo()+", "+percolationStats.confidenceHi()+"]");
    }
}
