/*
Roman Pavlov <roman.goward@gmail.com>
*/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
  * PercolationStats class implements Monte Carso simulation.
  */
public class PercolationStats {

    /**
      * 95th percentile.
      */
    private static final double PERCENTILE95 = 1.96;

    /**
      * Mean.
      */
    private final double mean;

    /**
      * Std deviation.
      */
    private final double stddev;

    /**
      * Confidence High.
      */
    private final double confidenceHi;

    /**
      * Confidence Low.
      */
    private final double confidenceLo;

    /**
      * Perform trials independent experiments on an n-by-n grid.
      * @param n **grid size**
      * @param trials **number of trials**
      */
    public PercolationStats(final int n, final int trials) {
      if (n <= 0 || trials <= 0) {
         throw new IllegalArgumentException("both params should be > 0");
      }

      int gridSize = n * n;
      double[] openSitesfractions = new double[trials];

      for (int i = 0; i < trials; i++) {
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
          int randRow = StdRandom.uniform(1, n + 1);
          int randCol = StdRandom.uniform(1, n + 1);
          p.open(randRow, randCol);
        }
        openSitesfractions[i] = p.numberOfOpenSites() / (double) gridSize;
      }

      mean = StdStats.mean(openSitesfractions);
      stddev = StdStats.stddev(openSitesfractions);

      double confidenceFraction = PERCENTILE95 * stddev / Math.sqrt(trials);

      confidenceLo = mean - confidenceFraction;
      confidenceHi = mean + confidenceFraction;
    }

    /**
      * Sample mean of percolation threshold.
      */
    public double mean() {
      return mean;
    }

    /**
     * Sample standard deviation of percolation threshold.
     */
    public double stddev() {
      return stddev;
    }

    /**
      * Low  endpoint of 95% confidence interval.
      */
    public double confidenceLo() {
      return confidenceLo;
    }

    /**
      * High endpoint of 95% confidence interval.
      */
    public double confidenceHi() {
      return confidenceHi;
    }

    /**
      * Exec Monte Carlo Simulation.
      * @param args **cli passed arguments**
      */
    public static void main(final String[] args) {
      PercolationStats ps = new PercolationStats(
                                  Integer.parseInt(args[0]),
                                  Integer.parseInt(args[1])
      );
      System.out.println("mean\t\t\t = " + ps.mean());
      System.out.println("stddev\t\t\t = " + ps.stddev());
      System.out.println("95% confidence interval\t = ["
                        + ps.confidenceHi() + ","
                        + ps.confidenceLo() + "]"
      );
    }
}
