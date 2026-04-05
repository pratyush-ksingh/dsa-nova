/**
 * Problem: Zigzag String
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Write the string in zigzag pattern across numRows rows, then read row by row.
 * Example: "PAYPALISHIRING" with 3 rows:
 *   P   A   H   N
 *   A P L S I I G
 *   Y   I   R
 * => "PAHNAPLSIIGYIR"
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  (2D matrix simulation)
    // Time: O(n)  |  Space: O(numRows * n)
    // ============================================================
    /**
     * Simulate the zigzag path on a 2D grid, then read left-to-right, row-by-row.
     * Real-life: Encoding text for visual display patterns (e.g., LED matrix scrolling).
     */
    public static String bruteForce(String s, int numRows) {
        if (numRows == 1 || numRows >= s.length()) return s;
        int n = s.length();
        char[][] grid = new char[numRows][n];
        for (char[] row : grid) Arrays.fill(row, ' ');

        int row = 0, col = 0;
        boolean goingDown = true;
        for (char c : s.toCharArray()) {
            grid[row][col] = c;
            if (goingDown) {
                if (row == numRows - 1) { goingDown = false; row--; col++; }
                else row++;
            } else {
                if (row == 0) { goingDown = true; row++; }
                else { row--; col++; }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char[] r : grid) for (char c : r) if (c != ' ') sb.append(c);
        return sb.toString();
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Row-by-row string builders)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Maintain one StringBuilder per row. Iterate characters, assign to current row
     * while tracking direction. Concatenate all rows at the end.
     * Real-life: Cipher encoding for simple substitution-like transformations.
     */
    public static String optimal(String s, int numRows) {
        if (numRows == 1 || numRows >= s.length()) return s;
        StringBuilder[] rows = new StringBuilder[numRows];
        for (int i = 0; i < numRows; i++) rows[i] = new StringBuilder();

        int curRow = 0;
        boolean goingDown = false;
        for (char c : s.toCharArray()) {
            rows[curRow].append(c);
            if (curRow == 0 || curRow == numRows - 1) goingDown = !goingDown;
            curRow += goingDown ? 1 : -1;
        }
        StringBuilder result = new StringBuilder();
        for (StringBuilder row : rows) result.append(row);
        return result.toString();
    }

    // ============================================================
    // APPROACH 3: BEST  (Direct index computation)
    // Time: O(n)  |  Space: O(n) for output only
    // ============================================================
    /**
     * Compute exactly which characters land in each row mathematically.
     * Cycle length = 2*(numRows-1). Row r gets characters at indices:
     *   r, r+cycle, r+2*cycle, ...  (first diagonal)
     *   plus cycle-r, cycle-r+cycle, ... for r != 0 and r != numRows-1 (second diagonal)
     * Real-life: Zero-allocation zigzag encoding in performance-critical systems.
     */
    public static String best(String s, int numRows) {
        if (numRows == 1 || numRows >= s.length()) return s;
        int n = s.length();
        int cycle = 2 * (numRows - 1);
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < numRows; row++) {
            for (int j = 0; j + row < n; j += cycle) {
                sb.append(s.charAt(j + row));
                // For non-first and non-last rows, there's a second character in the cycle
                if (row != 0 && row != numRows - 1) {
                    int idx2 = j + cycle - row;
                    if (idx2 < n) sb.append(s.charAt(idx2));
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Zigzag String ===");

        String[][] tests = {
            {"PAYPALISHIRING", "3"},
            {"PAYPALISHIRING", "4"},
            {"A", "1"},
            {"AB", "1"},
        };
        String[] expected = {"PAHNAPLSIIGYIR", "PINALSIGYAHRPI", "A", "AB"};

        for (int t = 0; t < tests.length; t++) {
            String s = tests[t][0];
            int rows = Integer.parseInt(tests[t][1]);
            System.out.println("\nInput: \"" + s + "\"  rows=" + rows + "  =>  expected: \"" + expected[t] + "\"");
            System.out.println("Brute:   \"" + bruteForce(s, rows) + "\"");
            System.out.println("Optimal: \"" + optimal(s, rows) + "\"");
            System.out.println("Best:    \"" + best(s, rows) + "\"");
        }
    }
}
