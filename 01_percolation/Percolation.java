/**
  * Roman Pavlov
  * @romangoward
  */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation class implements WeightedQuickUnionUF algorithm over n-by-n grid.
 */
public class Percolation {

    /**
     * siteStatus: 0 - BLOCKED.
     */
    private static final byte BLOCKED = 0;

    /**
     * siteStatus: 1 - OPENED.
     */
    private static final byte OPENED = 1;

    /**
     * siteStatus: 2 - CONNECTED TO BOTTOM.
     */
    private static final byte CONNECTED_BOTTOM = 2;

    /**
     * siteStatus: 3 - CONNECTED TO TOP.
     */
    private static final byte CONNECTED_TOP = 3;

    /**
      * Array represents status per site.
      */
    private final byte[] siteStatus;

    /**
      * Implements WeightedQuickUnionUF structure.
      */
    private final WeightedQuickUnionUF grid;

    /**
      * System percolation status.
      */
    private boolean isPercolates = false;

    /**
      * Grid size.
      */
    private final int gridSize;

    /**
      * Number of open sites.
      */
    private int numberOfOpenSites = 0;

    /**
      * Percolation constructor.
      * Creates n-by-n grid, with all sites blocked.
      * @param n **size of grid**
      */
    public Percolation(final int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("n should be > 0 ");
        }
        gridSize = n;

        int gridMatrixSize = gridSize * gridSize + 1;

        grid = new WeightedQuickUnionUF(gridMatrixSize);

        siteStatus = new byte[gridMatrixSize];
        siteStatus[0] = CONNECTED_TOP;
    }

    /**
      * Converts index form matrix[i,j] to array[i] format.
      * @param row **row index**
      * @param col **col index**
      */
    private int getIndex(final int row, final int col) {
        return (gridSize * (row - 1) + col);
    }

    /**
     * Connects site to other sites.
      * @param srcSite **source site**
      * @param row **row index**
      * @param col **col index**
     */
    private void connect(final int srcSite, final int row, final int col) {
      if (row < 1 || row > gridSize || col < 1 || col > gridSize) {
        return;
      }

      int dstSite = getIndex(row, col);

      if (!isOpen(row, col)) {
        // System.out.printf("[!isOpen]: f=%d, t=%d\n", srcSite, dstSite);
        return;
      }

      int srcRoot = grid.find(srcSite);
      int dstRoot = grid.find(dstSite);

      // System.out.printf("connect: f=%d, t=%d\n", srcSite, dstSite);

      if (srcRoot != dstRoot) {
        byte srcRootStatus = siteStatus[srcRoot];
        byte dstRootStatus = siteStatus[dstRoot];
        if ((srcRootStatus == CONNECTED_TOP && dstRootStatus == CONNECTED_BOTTOM)
            || (srcRootStatus == CONNECTED_BOTTOM && dstRootStatus == CONNECTED_TOP)) {
            // System.out.printf("[PERC]: f=%d, t=%d\n", srcSite, dstSite);
            isPercolates = true;
          }

          // System.out.printf("union: (%d, %d)\n", srcSite, dstSite);
          grid.union(srcSite, dstSite);

          // Since grid.union() breaks our siteStatus system
          // We save all statuses mannualy.
          if (srcRootStatus > dstRootStatus) {
            siteStatus[srcRoot] = srcRootStatus;
            siteStatus[dstRoot] = srcRootStatus;
          }
          if (srcRootStatus < dstRootStatus) {
            siteStatus[srcRoot] = dstRootStatus;
            siteStatus[dstRoot] = dstRootStatus;
          }
      } else {
        return;
      }
    }

    /**
      * Open site (row, col) if it is not open already.
      * @param row **row index**
      * @param col **col index**
      */
    public void open(final int row, final int col) {
      if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
        throw new java.lang.IllegalArgumentException();
      }
      if (isOpen(row, col)) {
        return;
      }

      int idx = getIndex(row, col);
      numberOfOpenSites += 1;

      // definde default status for a new site.
      if (row == 1) {
        siteStatus[idx] = CONNECTED_TOP;
      } else if (row == gridSize) {
        siteStatus[idx] = CONNECTED_BOTTOM;
      } else {
        siteStatus[idx] = OPENED;
      }

      if (gridSize == 1) {
        isPercolates = true;
        return;
      }

      connect(idx, row - 1, col);
      connect(idx, row + 1, col);
      connect(idx, row, col - 1);
      connect(idx, row, col + 1);
      }

    /**
      * Checks if site (row, col) is open.
      * @param row **row index**
      * @param col **col index**
      */
    public boolean isOpen(final int row, final int col) {
        if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
            throw new java.lang.IllegalArgumentException();
        }
        int idx = getIndex(row, col);
        boolean isOpen = false;
        if (siteStatus[idx] != BLOCKED) {
          isOpen = true;
        }
        return isOpen;
       // [ERROR] Avoid inline conditionals. [AvoidInlineConditionals]
       // return ((siteStatus[idx] != BLOCKED)  ? true : false);
      }

    /**
      * Checks if site (row, col) is full (connected to the top).
      * @param row **row index**
      * @param col **col index**
      */
    public boolean isFull(final int row, final int col) {
        if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
            throw new java.lang.IllegalArgumentException();
        }
        int idx = getIndex(row, col);
        int rootIdx = grid.find(idx);
        boolean isFull = false;
        if (siteStatus[rootIdx] == CONNECTED_TOP) {
          isFull = true;
        }
        return isFull;
        // [ERROR] Avoid inline conditionals. [AvoidInlineConditionals]
        // return ((siteStatus[rootIdx] == CONNECTED_TOP)  ? true : false);
    }

    /**
      * Shows number of open sites.
      */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /**
      * Checks if the system percolates.
      */
    public boolean percolates() {
        return isPercolates;
    }

    /**
      * Percolation Unit Tests.
      * @param args **passed cli arguments**
      */
    public static void main(final String[] args) {
    /*
      Percolation p = new Percolation(2);

      System.out.println("open(2, 1)");  p.open(2, 1);
      System.out.println("open(2, 2)");  p.open(2, 2);
      System.out.println("open(1, 1)");  p.open(1, 1);


      System.out.println("----------------");

      System.out.println("isFull(1, 1) -> " + p.isFull(1, 1));
      System.out.println("isFull(2, 1) -> " + p.isFull(2, 1));
      System.out.println("isFull(1, 2) -> " + p.isFull(1, 2));
      System.out.println("isFull(2, 2) -> " + p.isFull(2, 2));

      System.out.println("isOpen(1, 1) -> " + p.isOpen(1, 1));
      System.out.println("isOpen(2, 1) -> " + p.isOpen(2, 1));
      System.out.println("isOpen(1, 2) -> " + p.isOpen(1, 2));
      System.out.println("isOpen(2, 2) -> " + p.isOpen(2, 2));

      System.out.println("numberOfOpenSites -> " + p.numberOfOpenSites());
      System.out.println("isPercolates -> " + p.percolates());
    */
    }
 }
