/**
 * Problem: Sudoku Solver
 * Difficulty: HARD | XP: 50
 *
 * Fill a 9×9 Sudoku board with digits 1-9 such that each row, column,
 * and 3×3 box contains each digit exactly once. '.' represents empty cells.
 * Exactly one valid solution exists.
 *
 * @author DSA_Nova
 */

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Backtracking with linear scan checks
    // Time: O(9^(81))  |  Space: O(81) = O(1) (fixed 9x9 board)
    // For each empty cell, try digits 1-9, validate by scanning row/col/box.
    // ============================================================
    public static boolean bruteForce(char[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == '.') {
                    for (char d = '1'; d <= '9'; d++) {
                        if (isValidBrute(board, r, c, d)) {
                            board[r][c] = d;
                            if (bruteForce(board)) return true;
                            board[r][c] = '.';
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValidBrute(char[][] board, int row, int col, char d) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == d) return false;
            if (board[i][col] == d) return false;
            int br = 3 * (row / 3) + i / 3;
            int bc = 3 * (col / 3) + i % 3;
            if (board[br][bc] == d) return false;
        }
        return true;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Backtracking with bitmask tracking
    // Time: O(9^(empty cells))  |  Space: O(1)
    // Precompute which digits are used in each row, col, box using int bitmasks.
    // O(1) validity check; update/undo in O(1).
    // ============================================================
    static int[] rowMask = new int[9];
    static int[] colMask = new int[9];
    static int[] boxMask = new int[9];

    public static boolean optimal(char[][] board) {
        // Initialize masks
        for (int[] a : new int[][]{rowMask, colMask, boxMask}) java.util.Arrays.fill(a, 0);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != '.') {
                    int bit = 1 << (board[r][c] - '1');
                    rowMask[r] |= bit;
                    colMask[c] |= bit;
                    boxMask[(r / 3) * 3 + c / 3] |= bit;
                }
            }
        }
        return solveOpt(board, 0, 0);
    }

    private static boolean solveOpt(char[][] board, int r, int c) {
        if (r == 9) return true;
        int nr = (c == 8) ? r + 1 : r;
        int nc = (c == 8) ? 0 : c + 1;
        if (board[r][c] != '.') return solveOpt(board, nr, nc);
        int box = (r / 3) * 3 + c / 3;
        int used = rowMask[r] | colMask[c] | boxMask[box];
        for (int d = 0; d < 9; d++) {
            if ((used & (1 << d)) == 0) {
                board[r][c] = (char) ('1' + d);
                rowMask[r] |= (1 << d);
                colMask[c] |= (1 << d);
                boxMask[box] |= (1 << d);
                if (solveOpt(board, nr, nc)) return true;
                board[r][c] = '.';
                rowMask[r] &= ~(1 << d);
                colMask[c] &= ~(1 << d);
                boxMask[box] &= ~(1 << d);
            }
        }
        return false;
    }

    // ============================================================
    // APPROACH 3: BEST - Constraint Propagation + Backtracking (MRV heuristic)
    // Time: O(9^(empty cells)) with strong pruning  |  Space: O(81)
    // At each step, pick the cell with fewest valid candidates (MRV).
    // Reduces branching factor significantly for hard puzzles.
    // ============================================================
    public static boolean best(char[][] board) {
        int[] rowM = new int[9], colM = new int[9], boxM = new int[9];
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (board[r][c] != '.') {
                    int bit = 1 << (board[r][c] - '1');
                    rowM[r] |= bit; colM[c] |= bit; boxM[(r/3)*3+c/3] |= bit;
                }
        return solveMRV(board, rowM, colM, boxM);
    }

    private static boolean solveMRV(char[][] board, int[] rowM, int[] colM, int[] boxM) {
        // Find cell with fewest candidates (MRV)
        int minCount = 10, minR = -1, minC = -1;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == '.') {
                    int used = rowM[r] | colM[c] | boxM[(r/3)*3+c/3];
                    int cnt = Integer.bitCount(~used & 0x1FF); // 9-bit
                    if (cnt < minCount) { minCount = cnt; minR = r; minC = c; }
                    if (cnt == 0) return false; // no candidates -> dead end
                }
            }
        }
        if (minR == -1) return true; // solved
        int r = minR, c = minC, box = (r/3)*3+c/3;
        int avail = ~(rowM[r] | colM[c] | boxM[box]) & 0x1FF;
        while (avail != 0) {
            int bit = avail & (-avail);
            int d = Integer.numberOfTrailingZeros(bit);
            board[r][c] = (char)('1' + d);
            rowM[r] |= bit; colM[c] |= bit; boxM[box] |= bit;
            if (solveMRV(board, rowM, colM, boxM)) return true;
            board[r][c] = '.';
            rowM[r] &= ~bit; colM[c] &= ~bit; boxM[box] &= ~bit;
            avail &= avail - 1;
        }
        return false;
    }

    private static char[][] makeBoard(String[] rows) {
        char[][] b = new char[9][9];
        for (int i = 0; i < 9; i++) b[i] = rows[i].toCharArray();
        return b;
    }

    private static void printBoard(char[][] b) {
        for (char[] row : b) System.out.println(new String(row));
    }

    public static void main(String[] args) {
        System.out.println("=== Sudoku Solver ===");
        String[] puzzle = {
            "53..7....",
            "6..195...",
            ".98....6.",
            "8...6...3",
            "4..8.3..1",
            "7...2...6",
            ".6....28.",
            "...419..5",
            "....8..79"
        };

        char[][] b1 = makeBoard(puzzle);
        System.out.println("--- Brute Force ---");
        bruteForce(b1);
        printBoard(b1);

        char[][] b2 = makeBoard(puzzle);
        System.out.println("--- Optimal ---");
        optimal(b2);
        printBoard(b2);

        char[][] b3 = makeBoard(puzzle);
        System.out.println("--- Best (MRV) ---");
        best(b3);
        printBoard(b3);
    }
}
