/* *****************************************************************************
 *  Name: Tarun Punhani
 *  Date: 12/04/2020
 *  Description: Percolation assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] site;
    private final int number;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private int noOfOpenSites = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        number = n;
        weightedQuickUnionUF = new WeightedQuickUnionUF(n*n+2);
        site = new int[n+2][n+1];
        for (int i = 0; i < n+2; i++) {
            for (int j = 0; j < n+1; j++) {
                site[i][j] = -1; // value -1 means site is blocked
            }
        }
        site[0][0] = n*n; // virtual site top
        site[n+1][0] = n*n+1; // virtual site bottom

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateRowAndColumnValues(row, col);
        // open the site
        site[row][col] = (row - 1) * number + col - 1;
        noOfOpenSites++;

        if (row == 1 && !weightedQuickUnionUF.connected(site[row][col], site[0][0])) {
            weightedQuickUnionUF.union(site[row][col], site[0][0]);
        }

        // check for top cell
        if (row > 1) {
            if (isOpen(row - 1, col) && !weightedQuickUnionUF.connected(site[row][col], site[row - 1][col])) {
                weightedQuickUnionUF.union(site[row ][col], site[row - 1][col]);
            }
        }

        // check for left cell
        if (col > 1) {
            if (isOpen(row, col-1) && !weightedQuickUnionUF.connected(site[row][col], site[row][col-1]))
                weightedQuickUnionUF.union(site[row][col], site[row][col-1]);
        }

        // check for right cell
        if (col < number) {
            if (isOpen(row, col+1) && !weightedQuickUnionUF.connected(site[row][col], site[row][col+1]))
                weightedQuickUnionUF.union(site[row][col], site[row][col+1]);
        }

        // check for bottom cell
        if (row < number) {
            if (isOpen(row+1, col) && !weightedQuickUnionUF.connected(site[row][col], site[row+1][col]))
                weightedQuickUnionUF.union(site[row][col], site[row+1][col]);
        }

        if (row == number && !weightedQuickUnionUF.connected(site[row][col], site[number+1][0])) {
            weightedQuickUnionUF.union(site[row][col], site[number+1][0]);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateRowAndColumnValues(row, col);
        if (site[row][col] == -1) {
            return false;
        }

        return true;
    }

    private void validateRowAndColumnValues(int row, int col) {
        if (row < 1 || row > number || col < 1 || col > number) {
            throw new IllegalArgumentException();
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateRowAndColumnValues(row, col);
        if (!isOpen(row, col)) {
            return false;
        }

        if (weightedQuickUnionUF.connected(site[row][col], site[0][col])) {
            return true;
        }

        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return noOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (weightedQuickUnionUF.connected(site[0][0], site[number+1][0]))
            return true;

        return false;
    }


    public static void main(String[] args) {

        int n = StdIn.readInt();
        System.out.println("N is "+n);
        Percolation percolation = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            if (p == 0)
                break;
            int q = StdIn.readInt();
            System.out.println("P is "+p+" and q is "+q);
            if (!percolation.isOpen(p, q)) {
                percolation.open(p, q);
                continue;
            }
            StdOut.println(p + " " + q);
        }

        System.out.println("Percolates ? "+percolation.percolates());

    }
}
