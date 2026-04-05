/**
 * Problem: Word Search (LeetCode 79)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an m x n grid of characters and a word, return true if the word
 * exists in the grid. Adjacent means horizontally or vertically neighboring.
 * The same cell may not be used more than once.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(m*n * 4^L)  |  Space: O(L) for visited set + call stack
    //
    // Try starting DFS from every cell. Track visited cells using a
    // HashSet of encoded coordinates to avoid reuse.
    // ============================================================
    public static boolean bruteForce(char[][] board, String word) {
        int rows = board.length, cols = board[0].length;
        Set<Integer> visited = new HashSet<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (dfsBrute(board, word, r, c, 0, visited, rows, cols)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfsBrute(char[][] board, String word, int r, int c,
                                     int idx, Set<Integer> visited, int rows, int cols) {
        if (idx == word.length()) return true;
        if (r < 0 || r >= rows || c < 0 || c >= cols) return false;
        int key = r * cols + c;
        if (visited.contains(key)) return false;
        if (board[r][c] != word.charAt(idx)) return false;

        visited.add(key);
        boolean found = dfsBrute(board, word, r + 1, c, idx + 1, visited, rows, cols)
                     || dfsBrute(board, word, r - 1, c, idx + 1, visited, rows, cols)
                     || dfsBrute(board, word, r, c + 1, idx + 1, visited, rows, cols)
                     || dfsBrute(board, word, r, c - 1, idx + 1, visited, rows, cols);
        visited.remove(key);
        return found;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(m*n * 4^L)  |  Space: O(L) call stack
    //
    // Same DFS but mark visited cells in-place by replacing char with '#'.
    // Avoids overhead of a HashSet; restore on backtrack.
    // ============================================================
    public static boolean optimal(char[][] board, String word) {
        int rows = board.length, cols = board[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (dfsOptimal(board, word, r, c, 0, rows, cols)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfsOptimal(char[][] board, String word, int r, int c,
                                       int idx, int rows, int cols) {
        if (idx == word.length()) return true;
        if (r < 0 || r >= rows || c < 0 || c >= cols) return false;
        if (board[r][c] != word.charAt(idx)) return false;

        char temp = board[r][c];
        board[r][c] = '#';

        boolean found = dfsOptimal(board, word, r + 1, c, idx + 1, rows, cols)
                     || dfsOptimal(board, word, r - 1, c, idx + 1, rows, cols)
                     || dfsOptimal(board, word, r, c + 1, idx + 1, rows, cols)
                     || dfsOptimal(board, word, r, c - 1, idx + 1, rows, cols);

        board[r][c] = temp;
        return found;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(m*n * 4^L)  |  Space: O(L) call stack
    //
    // In-place DFS + frequency pruning:
    // 1. Count characters on board; if word needs more of any char than
    //    available, return false immediately.
    // 2. If word[0] is more frequent on board than word[last], reverse
    //    the word to start searching from the rarer end (fewer starting
    //    points = fewer DFS trees).
    // ============================================================
    public static boolean best(char[][] board, String word) {
        int rows = board.length, cols = board[0].length;

        // Frequency check
        int[] boardFreq = new int[128];
        for (char[] row : board)
            for (char c : row)
                boardFreq[c]++;

        int[] wordFreq = new int[128];
        for (char c : word.toCharArray()) wordFreq[c]++;

        for (int i = 0; i < 128; i++) {
            if (wordFreq[i] > boardFreq[i]) return false;
        }

        // Direction optimization: reverse word if first char is more common
        char[] w = word.toCharArray();
        if (boardFreq[w[0]] > boardFreq[w[w.length - 1]]) {
            // Reverse
            for (int l = 0, r = w.length - 1; l < r; l++, r--) {
                char tmp = w[l]; w[l] = w[r]; w[r] = tmp;
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (dfsBest(board, w, r, c, 0, rows, cols)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfsBest(char[][] board, char[] word, int r, int c,
                                    int idx, int rows, int cols) {
        if (idx == word.length) return true;
        if (r < 0 || r >= rows || c < 0 || c >= cols) return false;
        if (board[r][c] != word[idx]) return false;

        char temp = board[r][c];
        board[r][c] = '#';

        boolean found = dfsBest(board, word, r + 1, c, idx + 1, rows, cols)
                     || dfsBest(board, word, r - 1, c, idx + 1, rows, cols)
                     || dfsBest(board, word, r, c + 1, idx + 1, rows, cols)
                     || dfsBest(board, word, r, c - 1, idx + 1, rows, cols);

        board[r][c] = temp;
        return found;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Word Search ===\n");

        Object[][][] tests = {
            {new char[][]{{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}}, "ABCCED", true},
            {new char[][]{{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}}, "SEE",    true},
            {new char[][]{{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}}, "ABCB",   false},
            {new char[][]{{'a'}},                                                 "a",      true},
        };

        for (Object[][] test : tests) {
            char[][] board = (char[][]) test[0];
            String word = (String) test[1];
            boolean expected = (boolean) test[2];

            // Deep copy for each method
            char[][] b1 = copyBoard(board);
            char[][] b2 = copyBoard(board);
            char[][] b3 = copyBoard(board);

            boolean r1 = bruteForce(b1, word);
            boolean r2 = optimal(b2, word);
            boolean r3 = best(b3, word);

            String status = (r1 == expected && r2 == expected && r3 == expected) ? "PASS" : "FAIL";
            System.out.printf("Word: %-10s Expected: %-6b Brute: %-6b Optimal: %-6b Best: %-6b [%s]%n",
                word, expected, r1, r2, r3, status);
        }
    }

    private static char[][] copyBoard(char[][] board) {
        char[][] copy = new char[board.length][];
        for (int i = 0; i < board.length; i++) copy[i] = board[i].clone();
        return copy;
    }
}
