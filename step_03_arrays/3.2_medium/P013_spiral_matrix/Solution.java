/**
 * Problem: Spiral Matrix
 * LeetCode 54 | Difficulty: MEDIUM | XP: 25
 *
 * Given an m x n matrix, return all elements in spiral order.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  -  Simulation with visited matrix
    // Time: O(m*n)  |  Space: O(m*n)
    // ============================================================
    static class BruteForce {
        /**
         * Simulate the spiral walk.  Use a visited[][] array to know when
         * to turn.  Direction cycle: right -> down -> left -> up.
         */
        public List<Integer> spiralOrder(int[][] matrix) {
            int m = matrix.length, n = matrix[0].length;
            boolean[][] visited = new boolean[m][n];
            List<Integer> result = new ArrayList<>();

            // dr, dc: right, down, left, up
            int[] dr = {0, 1, 0, -1};
            int[] dc = {1, 0, -1, 0};
            int dir = 0;
            int r = 0, c = 0;

            for (int i = 0; i < m * n; i++) {
                result.add(matrix[r][c]);
                visited[r][c] = true;

                int nr = r + dr[dir];
                int nc = c + dc[dir];

                if (nr < 0 || nr >= m || nc < 0 || nc >= n || visited[nr][nc]) {
                    dir = (dir + 1) % 4;   // turn clockwise
                    nr = r + dr[dir];
                    nc = c + dc[dir];
                }
                r = nr;
                c = nc;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  -  4 shrinking boundaries, O(1) extra space
    // Time: O(m*n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Maintain top/bottom/left/right boundaries.
         * In each iteration: traverse the top row, right col, bottom row, left col;
         * then shrink each boundary inward.
         */
        public List<Integer> spiralOrder(int[][] matrix) {
            List<Integer> result = new ArrayList<>();
            int top = 0, bottom = matrix.length - 1;
            int left = 0, right = matrix[0].length - 1;

            while (top <= bottom && left <= right) {
                // Right across top
                for (int col = left; col <= right; col++)
                    result.add(matrix[top][col]);
                top++;

                // Down along right
                for (int row = top; row <= bottom; row++)
                    result.add(matrix[row][right]);
                right--;

                // Left across bottom (guard: top may have surpassed bottom)
                if (top <= bottom) {
                    for (int col = right; col >= left; col--)
                        result.add(matrix[bottom][col]);
                    bottom--;
                }

                // Up along left (guard: left may have surpassed right)
                if (left <= right) {
                    for (int row = bottom; row >= top; row--)
                        result.add(matrix[row][left]);
                    left++;
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  -  Same boundary approach, slightly refactored
    // Time: O(m*n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Identical algorithm to Optimal; factored into a helper addRow/addCol
         * style to make each direction step obvious during an interview walkthrough.
         */
        public List<Integer> spiralOrder(int[][] matrix) {
            List<Integer> result = new ArrayList<>();
            int top = 0, bottom = matrix.length - 1;
            int left = 0, right = matrix[0].length - 1;

            while (top <= bottom && left <= right) {
                for (int c = left;  c <= right;  c++) result.add(matrix[top][c]);
                top++;
                for (int r = top;   r <= bottom; r++) result.add(matrix[r][right]);
                right--;
                if (top <= bottom) {
                    for (int c = right; c >= left;  c--) result.add(matrix[bottom][c]);
                    bottom--;
                }
                if (left <= right) {
                    for (int r = bottom; r >= top; r--) result.add(matrix[r][left]);
                    left++;
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Spiral Matrix ===");
        int[][] m1 = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] m2 = {{1,2,3,4},{5,6,7,8},{9,10,11,12}};

        System.out.println("Brute   (3x3): " + new BruteForce().spiralOrder(m1));
        System.out.println("Optimal (3x4): " + new Optimal().spiralOrder(m2));
        System.out.println("Best    (3x3): " + new Best().spiralOrder(m1));
    }
}
