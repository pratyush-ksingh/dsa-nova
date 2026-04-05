/**
 * Problem: N Queen Problem
 * Difficulty: HARD | XP: 50
 *
 * Place N queens on an N×N chessboard so no two queens attack each other
 * (no shared row, column, or diagonal). Return all distinct solutions.
 * Each solution is a list of N strings, each string representing a board row.
 *
 * @author DSA_Nova
 */
import java.util.ArrayList;
import java.util.List;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Backtracking with linear conflict checks
    // Time: O(N! * N)  |  Space: O(N^2)
    // Place queen row by row; for each column check all previous queens.
    // ============================================================
    public static List<List<String>> bruteForce(int n) {
        List<List<String>> results = new ArrayList<>();
        int[] cols = new int[n]; // cols[row] = column of queen in that row
        solveBrute(n, 0, cols, results);
        return results;
    }

    private static void solveBrute(int n, int row, int[] cols, List<List<String>> results) {
        if (row == n) {
            results.add(buildBoard(cols, n));
            return;
        }
        for (int col = 0; col < n; col++) {
            if (isSafe(cols, row, col)) {
                cols[row] = col;
                solveBrute(n, row + 1, cols, results);
            }
        }
    }

    private static boolean isSafe(int[] cols, int row, int col) {
        for (int r = 0; r < row; r++) {
            if (cols[r] == col) return false;
            if (Math.abs(cols[r] - col) == Math.abs(r - row)) return false;
        }
        return true;
    }

    private static List<String> buildBoard(int[] cols, int n) {
        List<String> board = new ArrayList<>();
        for (int r = 0; r < n; r++) {
            char[] row = new char[n];
            for (int c = 0; c < n; c++) row[c] = (c == cols[r]) ? 'Q' : '.';
            board.add(new String(row));
        }
        return board;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Backtracking with bitset tracking
    // Time: O(N!)  |  Space: O(N)
    // Track conflicts with 3 boolean arrays: cols, diag1 (r-c), diag2 (r+c).
    // ============================================================
    public static List<List<String>> optimal(int n) {
        List<List<String>> results = new ArrayList<>();
        boolean[] colUsed = new boolean[n];
        boolean[] diag1 = new boolean[2 * n]; // row - col + n
        boolean[] diag2 = new boolean[2 * n]; // row + col
        int[] cols = new int[n];
        solveOpt(n, 0, cols, colUsed, diag1, diag2, results);
        return results;
    }

    private static void solveOpt(int n, int row, int[] cols,
                                  boolean[] colUsed, boolean[] diag1, boolean[] diag2,
                                  List<List<String>> results) {
        if (row == n) {
            results.add(buildBoard(cols, n));
            return;
        }
        for (int col = 0; col < n; col++) {
            int d1 = row - col + n;
            int d2 = row + col;
            if (!colUsed[col] && !diag1[d1] && !diag2[d2]) {
                cols[row] = col;
                colUsed[col] = true; diag1[d1] = true; diag2[d2] = true;
                solveOpt(n, row + 1, cols, colUsed, diag1, diag2, results);
                colUsed[col] = false; diag1[d1] = false; diag2[d2] = false;
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Bitmask backtracking (ultimate O(N!) pruning)
    // Time: O(N!)  |  Space: O(N)
    // Use integer bitmasks for cols, left-diagonal, right-diagonal.
    // At each row, available = ~(cols | ld | rd) & ((1<<n)-1).
    // Iterate over set bits of available to place queen.
    // ============================================================
    public static List<List<String>> best(int n) {
        List<List<String>> results = new ArrayList<>();
        int[] placement = new int[n];
        solveBitmask(n, 0, 0, 0, 0, placement, results);
        return results;
    }

    private static void solveBitmask(int n, int row, int cols, int ld, int rd,
                                      int[] placement, List<List<String>> results) {
        if (row == n) {
            results.add(buildBoard(placement, n));
            return;
        }
        int all = (1 << n) - 1;
        int available = all & ~(cols | ld | rd);
        while (available != 0) {
            int bit = available & (-available); // lowest set bit
            int col = Integer.numberOfTrailingZeros(bit);
            placement[row] = col;
            solveBitmask(n, row + 1,
                    cols | bit,
                    (ld | bit) << 1,
                    (rd | bit) >> 1,
                    placement, results);
            available &= available - 1; // clear lowest set bit
        }
    }

    public static void main(String[] args) {
        System.out.println("=== N Queen Problem ===");

        for (int n = 1; n <= 6; n++) {
            int b = bruteForce(n).size();
            int o = optimal(n).size();
            int be = best(n).size();
            System.out.printf("n=%d: brute=%d optimal=%d best=%d%n", n, b, o, be);
        }

        System.out.println("\nSolutions for n=4:");
        List<List<String>> sol = best(4);
        for (List<String> board : sol) {
            for (String row : board) System.out.println(row);
            System.out.println();
        }
    }
}
