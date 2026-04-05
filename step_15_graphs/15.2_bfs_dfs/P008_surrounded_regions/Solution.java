import java.util.*;

/**
 * Problem: Surrounded Regions (LeetCode #130)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a board of 'X' and 'O', flip all 'O's that are completely
 * surrounded by 'X's. Any 'O' on the border or connected to a border
 * 'O' must remain unchanged.
 *
 * @author DSA_Nova
 */
public class Solution {

    static final int[] DR = {-1, 1, 0, 0};
    static final int[] DC = {0, 0, -1, 1};

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- BFS per 'O' component, check border
    // Time: O((m*n)^2)  |  Space: O(m*n)
    // For each unvisited 'O', BFS its entire component. If no cell
    // in the component is on the border, flip them all.
    // ============================================================
    public static void bruteForce(char[][] board) {
        int rows = board.length, cols = board[0].length;
        boolean[][] processed = new boolean[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 'O' && !processed[r][c]) {
                    List<int[]> component = new ArrayList<>();
                    boolean safe = false;
                    Queue<int[]> queue = new LinkedList<>();
                    queue.offer(new int[]{r, c});
                    processed[r][c] = true;

                    while (!queue.isEmpty()) {
                        int[] cell = queue.poll();
                        int cr = cell[0], cc = cell[1];
                        component.add(cell);

                        if (cr == 0 || cr == rows - 1 || cc == 0 || cc == cols - 1)
                            safe = true;

                        for (int d = 0; d < 4; d++) {
                            int nr = cr + DR[d], nc = cc + DC[d];
                            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                                    && !processed[nr][nc] && board[nr][nc] == 'O') {
                                processed[nr][nc] = true;
                                queue.offer(new int[]{nr, nc});
                            }
                        }
                    }

                    if (!safe) {
                        for (int[] cell : component)
                            board[cell[0]][cell[1]] = 'X';
                    }
                }
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- BFS from border O's, mark safe, flip rest
    // Time: O(m*n)  |  Space: O(m*n)
    // Step 1: BFS from every border 'O', marking reachable O's as '#'.
    // Step 2: In one scan, flip remaining 'O' -> 'X', restore '#' -> 'O'.
    // ============================================================
    public static void optimal(char[][] board) {
        int rows = board.length, cols = board[0].length;

        // Mark border-connected O's as safe ('#')
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((r == 0 || r == rows - 1 || c == 0 || c == cols - 1)
                        && board[r][c] == 'O') {
                    bfsMark(board, r, c, rows, cols);
                }
            }
        }

        // Flip captured O->X, restore safe #->O
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 'O') board[r][c] = 'X';
                else if (board[r][c] == '#') board[r][c] = 'O';
            }
        }
    }

    private static void bfsMark(char[][] board, int sr, int sc, int rows, int cols) {
        Queue<int[]> queue = new LinkedList<>();
        board[sr][sc] = '#';
        queue.offer(new int[]{sr, sc});
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && board[nr][nc] == 'O') {
                    board[nr][nc] = '#';
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative DFS from border O's
    // Time: O(m*n)  |  Space: O(m*n)
    // Identical to Approach 2 in complexity. Uses an explicit stack
    // instead of BFS queue. Often slightly faster in practice due to
    // better cache locality on deep traversals.
    // ============================================================
    public static void best(char[][] board) {
        int rows = board.length, cols = board[0].length;

        // Seed DFS from all border O's
        for (int r = 0; r < rows; r++) {
            if (board[r][0] == 'O')       dfsMark(board, r, 0, rows, cols);
            if (board[r][cols-1] == 'O')  dfsMark(board, r, cols-1, rows, cols);
        }
        for (int c = 0; c < cols; c++) {
            if (board[0][c] == 'O')       dfsMark(board, 0, c, rows, cols);
            if (board[rows-1][c] == 'O')  dfsMark(board, rows-1, c, rows, cols);
        }

        // Flip captured O->X, restore safe #->O
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 'O') board[r][c] = 'X';
                else if (board[r][c] == '#') board[r][c] = 'O';
            }
        }
    }

    private static void dfsMark(char[][] board, int sr, int sc, int rows, int cols) {
        Deque<int[]> stack = new ArrayDeque<>();
        board[sr][sc] = '#';
        stack.push(new int[]{sr, sc});
        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int r = cell[0], c = cell[1];
            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && board[nr][nc] == 'O') {
                    board[nr][nc] = '#';
                    stack.push(new int[]{nr, nc});
                }
            }
        }
    }

    private static void printBoard(char[][] board) {
        for (char[] row : board) System.out.println(new String(row));
    }

    private static char[][] copy(char[][] b) {
        char[][] c = new char[b.length][];
        for (int i = 0; i < b.length; i++) c[i] = b[i].clone();
        return c;
    }

    public static void main(String[] args) {
        System.out.println("=== Surrounded Regions ===\n");

        char[][] b = {
            {'X','X','X','X'},
            {'X','O','O','X'},
            {'X','X','O','X'},
            {'X','O','X','X'}
        };

        char[][] b1 = copy(b), b2 = copy(b), b3 = copy(b);

        System.out.println("Brute:");
        bruteForce(b1); printBoard(b1);

        System.out.println("\nOptimal:");
        optimal(b2); printBoard(b2);

        System.out.println("\nBest:");
        best(b3); printBoard(b3);
        // Expected: inner O's flipped; (3,1) stays O (border-connected)

        System.out.println();
        char[][] single = {{'O'}};
        optimal(single);
        System.out.println("Single O (safe): " + single[0][0]); // O
    }
}
