/**
 * Problem: Valid Sudoku
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Determine if a 9x9 Sudoku board is valid (partially filled is OK).
 * Each row, column, and 3x3 box must contain digits 1-9 with no repeats.
 * Empty cells are represented by '.'.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(1) [81 cells fixed]  |  Space: O(1) [bounded sets]
    // ============================================================
    /**
     * Three separate passes: validate rows, then columns, then 3x3 boxes.
     * Real-life: Input validation for puzzle games and constraint-satisfaction solvers.
     */
    public static boolean bruteForce(char[][] board) {
        // Validate rows
        for (int r = 0; r < 9; r++) {
            Set<Character> seen = new HashSet<>();
            for (int c = 0; c < 9; c++) {
                char ch = board[r][c];
                if (ch == '.') continue;
                if (!seen.add(ch)) return false;
            }
        }
        // Validate columns
        for (int c = 0; c < 9; c++) {
            Set<Character> seen = new HashSet<>();
            for (int r = 0; r < 9; r++) {
                char ch = board[r][c];
                if (ch == '.') continue;
                if (!seen.add(ch)) return false;
            }
        }
        // Validate 3x3 boxes
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                Set<Character> seen = new HashSet<>();
                for (int r = boxRow * 3; r < boxRow * 3 + 3; r++) {
                    for (int c = boxCol * 3; c < boxCol * 3 + 3; c++) {
                        char ch = board[r][c];
                        if (ch == '.') continue;
                        if (!seen.add(ch)) return false;
                    }
                }
            }
        }
        return true;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(1) [81 cells]  |  Space: O(1) [bounded set]
    // ============================================================
    /**
     * Single pass: encode (row,digit), (col,digit), (box,digit) tuples into one HashSet.
     * Box index = (row/3)*3 + (col/3).
     * Real-life: Real-time game-state validation in Sudoku apps.
     */
    public static boolean optimal(char[][] board) {
        Set<String> seen = new HashSet<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                char ch = board[r][c];
                if (ch == '.') continue;
                String rowKey = "r" + r + ch;
                String colKey = "c" + c + ch;
                String boxKey = "b" + (r / 3) + (c / 3) + ch;
                if (!seen.add(rowKey) || !seen.add(colKey) || !seen.add(boxKey)) {
                    return false;
                }
            }
        }
        return true;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(1) [81 cells]  |  Space: O(1) [27 integers]
    // ============================================================
    /**
     * Bit-mask approach: use int[9] for rows, int[9] for cols, int[9] for boxes.
     * Each bit position represents whether a digit (1-9) has been placed.
     * Avoids string allocation entirely — fastest in practice.
     * Real-life: High-performance constraint checking in embedded Sudoku solvers.
     */
    public static boolean best(char[][] board) {
        int[] rows  = new int[9];
        int[] cols  = new int[9];
        int[] boxes = new int[9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                char ch = board[r][c];
                if (ch == '.') continue;
                int bit    = 1 << (ch - '1');
                int boxIdx = (r / 3) * 3 + (c / 3);
                if ((rows[r]       & bit) != 0) return false;
                if ((cols[c]       & bit) != 0) return false;
                if ((boxes[boxIdx] & bit) != 0) return false;
                rows[r]       |= bit;
                cols[c]       |= bit;
                boxes[boxIdx] |= bit;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Valid Sudoku ===");

        char[][] valid = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };

        char[][] invalid = {
            {'8','3','.','.','7','.','.','.','.'},  // duplicate 8 in column 0
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };

        System.out.println("\nValid board:");
        System.out.println("Brute:   " + bruteForce(valid)  + " (expected true)");
        System.out.println("Optimal: " + optimal(valid)     + " (expected true)");
        System.out.println("Best:    " + best(valid)        + " (expected true)");

        System.out.println("\nInvalid board (duplicate 8 in first col):");
        System.out.println("Brute:   " + bruteForce(invalid) + " (expected false)");
        System.out.println("Optimal: " + optimal(invalid)    + " (expected false)");
        System.out.println("Best:    " + best(invalid)       + " (expected false)");
    }
}
