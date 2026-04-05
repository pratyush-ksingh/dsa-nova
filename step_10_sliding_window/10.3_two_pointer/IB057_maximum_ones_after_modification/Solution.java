/**
 * Problem: Maximum Ones After Modification (InterviewBit)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a binary array A and an integer B, find the maximum number of
 * consecutive 1s if you can flip at most B zeros.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Try Every Subarray)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static int solve(int[] A, int B) {
        int n = A.length;
        int maxLen = 0;

        for (int i = 0; i < n; i++) {
            int zeros = 0;
            for (int j = i; j < n; j++) {
                if (A[j] == 0) zeros++;
                if (zeros > B) break;
                maxLen = Math.max(maxLen, j - i + 1);
            }
        }
        return maxLen;
    }
}

// ============================================================
// Approach 2: Optimal (Sliding Window)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static int solve(int[] A, int B) {
        int n = A.length;
        int left = 0, zeros = 0, maxLen = 0;

        for (int right = 0; right < n; right++) {
            if (A[right] == 0) zeros++;

            while (zeros > B) {
                if (A[left] == 0) zeros--;
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}

// ============================================================
// Approach 3: Best (Sliding Window -- Non-Shrinking)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static int solve(int[] A, int B) {
        int n = A.length;
        int left = 0, zeros = 0;

        for (int right = 0; right < n; right++) {
            if (A[right] == 0) zeros++;

            if (zeros > B) {
                if (A[left] == 0) zeros--;
                left++;
            }
        }
        return n - left;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Maximum Ones After Modification ===\n");

        int[][][] tests = {
            {{1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1}, {2}, {8}},
            {{1, 0, 0, 1, 0, 1, 0, 1, 1, 1}, {2}, {7}},
            {{0, 0, 0, 0}, {2}, {2}},
            {{1, 1, 1, 1}, {0}, {4}},
            {{1}, {1}, {1}},
            {{0}, {0}, {0}},
        };

        for (int[][] t : tests) {
            int[] A = t[0];
            int B = t[1][0];
            int expected = t[2][0];

            int b = BruteForce.solve(A.clone(), B);
            int o = Optimal.solve(A.clone(), B);
            int h = Best.solve(A.clone(), B);
            boolean pass = b == expected && o == expected && h == expected;

            System.out.println("Input:    A=" + Arrays.toString(A) + ", B=" + B);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + h);
            System.out.println("Expected: " + expected);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
