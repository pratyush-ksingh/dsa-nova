/**
 * Problem: Wave Array
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Sort array in wave form: a[0] >= a[1] <= a[2] >= a[3] ...
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Sort + Swap Adjacent Pairs)
// Time: O(n log n) | Space: O(1) extra
// ============================================================
class BruteForce {
    public static int[] waveArray(int[] arr) {
        int[] a = arr.clone();
        Arrays.sort(a);

        // Swap adjacent pairs: (0,1), (2,3), (4,5), ...
        for (int i = 0; i < a.length - 1; i += 2) {
            int temp = a[i];
            a[i] = a[i + 1];
            a[i + 1] = temp;
        }
        return a;
    }
}

// ============================================================
// Approach 2: Optimal (Sort + Swap -- Same, Simple & Correct)
// Time: O(n log n) | Space: O(1) extra
// ============================================================
class Optimal {
    public static int[] waveArray(int[] arr) {
        int[] a = arr.clone();
        Arrays.sort(a);

        for (int i = 0; i < a.length - 1; i += 2) {
            int temp = a[i];
            a[i] = a[i + 1];
            a[i + 1] = temp;
        }
        return a;
    }
}

// ============================================================
// Approach 3: Best (Single Pass -- Compare with Neighbors)
// Time: O(n) | Space: O(1) extra
// ============================================================
class Best {
    public static int[] waveArray(int[] arr) {
        int[] a = arr.clone();
        int n = a.length;

        for (int i = 0; i < n; i += 2) {
            // If current is less than previous, swap
            if (i > 0 && a[i] < a[i - 1]) {
                int temp = a[i];
                a[i] = a[i - 1];
                a[i - 1] = temp;
            }
            // If current is less than next, swap
            if (i < n - 1 && a[i] < a[i + 1]) {
                int temp = a[i];
                a[i] = a[i + 1];
                a[i + 1] = temp;
            }
        }
        return a;
    }
}

// Main driver
public class Solution {
    private static boolean isWave(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (i % 2 == 0) {
                if (i > 0 && arr[i] < arr[i - 1]) return false;
                if (i < arr.length - 1 && arr[i] < arr[i + 1]) return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Wave Array ===\n");

        int[][] tests = {
            {1, 2, 3, 4, 5},
            {10, 5, 6, 3, 2, 20, 100, 80},
            {1, 2, 3, 4},
            {5},
            {3, 3, 3, 3},
            {1, 2},
            {20, 10, 8, 6, 4, 2}
        };

        for (int[] arr : tests) {
            int[] b = BruteForce.waveArray(arr);
            int[] o = Optimal.waveArray(arr);
            int[] r = Best.waveArray(arr);

            boolean bOk = isWave(b);
            boolean oOk = isWave(o);
            boolean rOk = isWave(r);
            String status = (bOk && oOk && rOk) ? "PASS" : "FAIL";

            System.out.println("Input:    " + Arrays.toString(arr));
            System.out.println("  Brute:    " + Arrays.toString(b) + "  valid=" + bOk);
            System.out.println("  Optimal:  " + Arrays.toString(o) + "  valid=" + oOk);
            System.out.println("  Best:     " + Arrays.toString(r) + "  valid=" + rOk);
            System.out.println("  [" + status + "]");
            System.out.println();
        }
    }
}
