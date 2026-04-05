/**
 * Problem: Pascal's Triangle (LeetCode #118)
 * Difficulty: EASY | XP: 10
 *
 * Given an integer numRows, return the first numRows rows of Pascal's Triangle.
 *
 * @author DSA_Nova
 */
import java.util.ArrayList;
import java.util.List;

// ============================================================
// Approach 1: Brute Force (Using nCr formula)
// Time: O(numRows^2) | Space: O(numRows^2)
// ============================================================
class BruteForce {
    public static List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();

        for (int r = 0; r < numRows; r++) {
            List<Integer> row = new ArrayList<>();
            for (int c = 0; c <= r; c++) {
                row.add(nCr(r, c));
            }
            result.add(row);
        }
        return result;
    }

    // Compute C(n, k) using multiplicative formula to avoid factorial overflow
    private static int nCr(int n, int k) {
        if (k > n - k) {
            k = n - k; // Optimization: C(n, k) = C(n, n-k)
        }
        long result = 1;
        for (int i = 0; i < k; i++) {
            result = result * (n - i) / (i + 1);
        }
        return (int) result;
    }
}

// ============================================================
// Approach 2: Optimal (Iterative Row Construction)
// Time: O(numRows^2) | Space: O(numRows^2)
// ============================================================
class Optimal {
    public static List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();

        for (int r = 0; r < numRows; r++) {
            List<Integer> row = new ArrayList<>();
            row.add(1); // First element is always 1

            if (r > 0) {
                List<Integer> prevRow = result.get(r - 1);
                for (int j = 1; j < r; j++) {
                    row.add(prevRow.get(j - 1) + prevRow.get(j));
                }
                row.add(1); // Last element is always 1
            }

            result.add(row);
        }
        return result;
    }
}

// ============================================================
// Approach 3: Best (Row-wise nCr without previous row reference)
// Time: O(numRows^2) | Space: O(numRows^2)
// Each element derived from previous element in same row:
//   C(r, c) = C(r, c-1) * (r - c + 1) / c
// ============================================================
class Best {
    public static List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();

        for (int r = 0; r < numRows; r++) {
            List<Integer> row = new ArrayList<>();
            long val = 1;
            for (int c = 0; c <= r; c++) {
                row.add((int) val);
                val = val * (r - c) / (c + 1);
            }
            result.add(row);
        }
        return result;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Pascal's Triangle ===\n");

        int[] testCases = {1, 2, 3, 5, 7};

        for (int numRows : testCases) {
            System.out.println("numRows = " + numRows);

            List<List<Integer>> bruteResult = BruteForce.generate(numRows);
            List<List<Integer>> optimalResult = Optimal.generate(numRows);
            List<List<Integer>> bestResult = Best.generate(numRows);

            System.out.println("Brute:   " + bruteResult);
            System.out.println("Optimal: " + optimalResult);
            System.out.println("Best:    " + bestResult);

            boolean match = bruteResult.equals(optimalResult)
                         && optimalResult.equals(bestResult);
            System.out.println("All match: " + match);

            // Pretty print the triangle
            System.out.println("Visual:");
            for (int r = 0; r < optimalResult.size(); r++) {
                // Indentation for pyramid shape
                StringBuilder sb = new StringBuilder();
                for (int s = 0; s < numRows - r - 1; s++) {
                    sb.append("  ");
                }
                for (int c = 0; c < optimalResult.get(r).size(); c++) {
                    sb.append(String.format("%4d", optimalResult.get(r).get(c)));
                }
                System.out.println(sb.toString());
            }
            System.out.println();
        }
    }
}
