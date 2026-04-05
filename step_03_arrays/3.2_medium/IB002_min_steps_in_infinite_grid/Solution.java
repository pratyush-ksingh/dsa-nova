/**
 * Problem: Min Steps in Infinite Grid
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given a sequence of points on a 2D grid, find the minimum number of steps
 * to visit all points in order. Movement allowed in 8 directions.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Simulate Step-by-Step)
// Time: O(sum of distances) | Space: O(1)
// Too slow for large coordinates but demonstrates the idea.
// ============================================================
class BruteForce {
    public static long minSteps(int[] X, int[] Y) {
        long totalSteps = 0;
        for (int i = 0; i < X.length - 1; i++) {
            int cx = X[i], cy = Y[i];
            int tx = X[i + 1], ty = Y[i + 1];
            while (cx != tx || cy != ty) {
                // Move toward target: adjust each axis by at most 1
                if (cx < tx) cx++;
                else if (cx > tx) cx--;
                if (cy < ty) cy++;
                else if (cy > ty) cy--;
                totalSteps++;
            }
        }
        return totalSteps;
    }
}

// ============================================================
// Approach 2: Optimal (Chebyshev Distance Formula)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static long minSteps(int[] X, int[] Y) {
        long totalSteps = 0;
        for (int i = 0; i < X.length - 1; i++) {
            long dx = Math.abs((long) X[i + 1] - X[i]);
            long dy = Math.abs((long) Y[i + 1] - Y[i]);
            totalSteps += Math.max(dx, dy);
        }
        return totalSteps;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Min Steps in Infinite Grid ===\n");

        // Test case format: {X[], Y[], expected}
        int[][][] testX = {
            {{0, 1, 1}},
            {{0, 3}},
            {{0, 0}},
            {{0}},
            {{-3, 3}},
            {{0, 5, 5, 0}}
        };
        int[][][] testY = {
            {{0, 1, 5}},
            {{0, 4}},
            {{0, 0}},
            {{0}},
            {{-3, 3}},
            {{0, 0, 5, 5}}
        };
        long[] expected = {5, 4, 0, 0, 6, 15};

        for (int t = 0; t < expected.length; t++) {
            int[] X = testX[t][0];
            int[] Y = testY[t][0];

            // Only run brute force for small distances
            long bruteResult = BruteForce.minSteps(X, Y);
            long optimalResult = Optimal.minSteps(X, Y);

            System.out.println("X=" + Arrays.toString(X) + ", Y=" + Arrays.toString(Y));
            System.out.println("  Brute:    " + bruteResult);
            System.out.println("  Optimal:  " + optimalResult);
            System.out.println("  Expected: " + expected[t]);
            System.out.println("  Pass:     " + (bruteResult == expected[t] && optimalResult == expected[t]));
            System.out.println();
        }
    }
}
