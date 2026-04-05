import java.util.*;

/**
 * Problem: Capture Regions on Board
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a 2D board with 'X' and 'O', capture all 'O' regions not connected to the border.
 * Surrounded 'O' regions (not touching any border) become 'X'.
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// BFS/DFS from each border 'O' to mark safe cells, then flip remaining 'O' to 'X'
// Time: O(M*N)  |  Space: O(M*N)
// ============================================================
class BruteForce {
    static int rows, cols;

    static void dfs(char[][] board, int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || board[r][c] != 'O') return;
        board[r][c] = 'S'; // safe
        dfs(board, r + 1, c);
        dfs(board, r - 1, c);
        dfs(board, r, c + 1);
        dfs(board, r, c - 1);
    }

    static void solve(char[][] board) {
        if (board == null || board.length == 0) return;
        rows = board.length;
        cols = board[0].length;

        // Mark safe O's from borders
        for (int r = 0; r < rows; r++) {
            dfs(board, r, 0);
            dfs(board, r, cols - 1);
        }
        for (int c = 0; c < cols; c++) {
            dfs(board, 0, c);
            dfs(board, rows - 1, c);
        }

        // Flip: O->X (captured), S->O (restore safe)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 'O') board[r][c] = 'X';
                else if (board[r][c] == 'S') board[r][c] = 'O';
            }
        }
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// BFS from border O's — iterative, avoids deep recursion
// Time: O(M*N)  |  Space: O(M*N)
// ============================================================
class Optimal {
    static void solve(char[][] board) {
        if (board == null || board.length == 0) return;
        int rows = board.length, cols = board[0].length;
        int[] dr = {0, 0, 1, -1};
        int[] dc = {1, -1, 0, 0};

        Queue<int[]> queue = new LinkedList<>();

        // Add all border O's to queue
        for (int r = 0; r < rows; r++) {
            if (board[r][0] == 'O') { queue.offer(new int[]{r, 0}); board[r][0] = 'S'; }
            if (board[r][cols-1] == 'O') { queue.offer(new int[]{r, cols-1}); board[r][cols-1] = 'S'; }
        }
        for (int c = 0; c < cols; c++) {
            if (board[0][c] == 'O') { queue.offer(new int[]{0, c}); board[0][c] = 'S'; }
            if (board[rows-1][c] == 'O') { queue.offer(new int[]{rows-1, c}); board[rows-1][c] = 'S'; }
        }

        // BFS to mark all connected safe O's
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int d = 0; d < 4; d++) {
                int nr = cur[0] + dr[d], nc = cur[1] + dc[d];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && board[nr][nc] == 'O') {
                    board[nr][nc] = 'S';
                    queue.offer(new int[]{nr, nc});
                }
            }
        }

        // Flip remaining
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 'O') board[r][c] = 'X';
                else if (board[r][c] == 'S') board[r][c] = 'O';
            }
    }
}

// ============================================================
// APPROACH 3: BEST
// Union-Find — connect border O's to dummy node; safe cells share root with dummy
// Time: O(M*N * alpha)  |  Space: O(M*N)
// ============================================================
class Best {
    static int[] parent;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static void union(int x, int y) {
        int px = find(x), py = find(y);
        if (px != py) parent[px] = py;
    }

    static void solve(char[][] board) {
        if (board == null || board.length == 0) return;
        int rows = board.length, cols = board[0].length;
        int dummy = rows * cols; // virtual border node
        parent = new int[rows * cols + 1];
        for (int i = 0; i <= rows * cols; i++) parent[i] = i;

        int[] dr = {0, 0, 1, -1};
        int[] dc = {1, -1, 0, 0};

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 'O') {
                    int idx = r * cols + c;
                    // Border cells connect to dummy
                    if (r == 0 || r == rows-1 || c == 0 || c == cols-1) {
                        union(idx, dummy);
                    }
                    // Connect to neighboring O's
                    for (int d = 0; d < 4; d++) {
                        int nr = r + dr[d], nc = c + dc[d];
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && board[nr][nc] == 'O') {
                            union(idx, nr * cols + nc);
                        }
                    }
                }
            }
        }

        int dummyRoot = find(dummy);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (board[r][c] == 'O' && find(r * cols + c) != dummyRoot)
                    board[r][c] = 'X';
    }
}

public class Solution {
    static char[][] makeBoard() {
        return new char[][]{
            {'X','X','X','X'},
            {'X','O','O','X'},
            {'X','X','O','X'},
            {'X','O','X','X'}
        };
    }

    static void print(char[][] b) {
        for (char[] row : b) System.out.println(new String(row));
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== Capture Regions on Board ===");

        char[][] b1 = makeBoard();
        System.out.println("Before:"); print(b1);
        BruteForce.solve(b1);
        System.out.println("BruteForce After:"); print(b1);

        char[][] b2 = makeBoard();
        Optimal.solve(b2);
        System.out.println("Optimal After:"); print(b2);

        char[][] b3 = makeBoard();
        Best.solve(b3);
        System.out.println("Best After:"); print(b3);
    }
}
