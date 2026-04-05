/**
 * Problem: Snake Ladder Problem
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a snake-and-ladder board of size N*N (cells 1 to N*N), with snakes
 * and ladders, find the minimum number of dice throws to reach the last cell.
 *
 * Approach: BFS where each cell is a node. From cell i, you can move to
 * i+1, i+2, ..., i+6. If the destination has a snake/ladder, follow it.
 * BFS guarantees minimum throws.
 *
 * Real-life use: Board game AI, shortest path in state machines.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(N^2)  |  Space: O(N^2)
    // BFS on the board. moves[i] = final position from cell i
    // (after applying snake or ladder if any). Start from cell 1.
    // ============================================================
    public static int bruteForce(int n, int[] moves) {
        // moves[i] = i means no snake/ladder at cell i
        // moves[i] = j (j != i) means snake/ladder goes from i to j
        int total = n * n;
        int[] dist = new int[total + 1];
        Arrays.fill(dist, -1);
        dist[1] = 0;

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);

        while (!queue.isEmpty()) {
            int cell = queue.poll();
            for (int dice = 1; dice <= 6; dice++) {
                int next = cell + dice;
                if (next > total) break;
                // Apply snake or ladder
                int dest = moves[next - 1] != -1 ? moves[next - 1] : next;
                if (dest == total) return dist[cell] + 1;
                if (dist[dest] == -1) {
                    dist[dest] = dist[cell] + 1;
                    queue.offer(dest);
                }
            }
        }
        return -1; // Unreachable
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(N^2)  |  Space: O(N^2)
    // Same BFS but uses a HashMap for snakes and ladders separately.
    // Clearer to understand with explicit snake/ladder maps.
    // ============================================================
    public static int optimal(int boardSize, Map<Integer, Integer> snakes,
                               Map<Integer, Integer> ladders) {
        int total = boardSize * boardSize;
        int[] dist = new int[total + 1];
        Arrays.fill(dist, -1);
        dist[1] = 0;

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);

        while (!queue.isEmpty()) {
            int cell = queue.poll();
            for (int dice = 1; dice <= 6; dice++) {
                int next = cell + dice;
                if (next > total) break;
                // Apply ladder first, then snake (or just combined map)
                if (ladders.containsKey(next)) next = ladders.get(next);
                else if (snakes.containsKey(next)) next = snakes.get(next);

                if (next == total) return dist[cell] + 1;
                if (dist[next] == -1) {
                    dist[next] = dist[cell] + 1;
                    queue.offer(next);
                }
            }
        }
        return -1;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(N^2)  |  Space: O(N^2)
    // Clean BFS with combined movement array (standard LeetCode 909 style).
    // board[i] = -1 means no snake/ladder, otherwise it's the destination.
    // Handles the snaking row order of a real board.
    // ============================================================
    public static int best(int[][] board) {
        int n = board.length;
        int total = n * n;

        // Flatten board (cell 1 = board[n-1][0], handles snaking)
        int[] cells = new int[total + 1];
        int idx = 1;
        for (int row = n - 1; row >= 0; row--) {
            boolean leftToRight = ((n - 1 - row) % 2 == 0);
            if (leftToRight) {
                for (int col = 0; col < n; col++) cells[idx++] = board[row][col];
            } else {
                for (int col = n - 1; col >= 0; col--) cells[idx++] = board[row][col];
            }
        }

        int[] dist = new int[total + 1];
        Arrays.fill(dist, -1);
        dist[1] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);

        while (!queue.isEmpty()) {
            int cell = queue.poll();
            for (int dice = 1; dice <= 6; dice++) {
                int next = cell + dice;
                if (next > total) break;
                int dest = cells[next] != -1 ? cells[next] : next;
                if (dest == total) return dist[cell] + 1;
                if (dist[dest] == -1) {
                    dist[dest] = dist[cell] + 1;
                    queue.offer(dest);
                }
            }
        }
        return dist[total] == -1 ? -1 : dist[total];
    }

    public static void main(String[] args) {
        System.out.println("=== Snake Ladder Problem ===\n");

        // Test 1: Simple 6x6 board with snakes and ladders
        // Using moves[] array (0-indexed, -1 = no snake/ladder)
        int boardN = 6;
        int total = boardN * boardN; // 36 cells
        int[] moves = new int[total];
        Arrays.fill(moves, -1);
        // Ladders: 3->22, 5->8, 11->26, 20->29
        moves[2] = 22; moves[4] = 8; moves[10] = 26; moves[19] = 29;
        // Snakes: 27->1, 21->9, 17->4, 35->26  (0-indexed)
        moves[26] = 1; moves[20] = 9; moves[16] = 4; moves[34] = 26; // wait, snake/ladder may overlap
        // Reset conflicting
        moves[19] = 29; // ladder at 20 -> 29

        System.out.println("Test 1 (6x6 board):");
        System.out.println("  Brute: " + bruteForce(boardN, moves));

        // Test 2: Using explicit maps
        Map<Integer, Integer> snakes = new HashMap<>();
        Map<Integer, Integer> ladders = new HashMap<>();
        snakes.put(17, 7); snakes.put(54, 34); snakes.put(62, 19);
        snakes.put(64, 60); snakes.put(87, 24); snakes.put(93, 73);
        snakes.put(95, 75); snakes.put(99, 78);
        ladders.put(4, 14); ladders.put(9, 31); ladders.put(20, 38);
        ladders.put(28, 84); ladders.put(40, 59); ladders.put(51, 67);
        ladders.put(63, 81); ladders.put(71, 91);

        System.out.println("Test 2 (10x10 board):");
        System.out.println("  Optimal: " + optimal(10, snakes, ladders)); // expected: small number

        // Test 3: LeetCode 909 style board
        int[][] board = {
            {-1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1},
            {-1, 35, -1, -1, 13, -1},
            {-1, -1, -1, -1, -1, -1},
            {-1, 15, -1, -1, -1, -1}
        };
        System.out.println("Test 3 (6x6 2D board):");
        System.out.println("  Best: " + best(board)); // LeetCode expects 4
    }
}
