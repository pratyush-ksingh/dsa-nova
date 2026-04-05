/**
 * Problem: Counting Triangles (InterviewBit)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array of integers, count the number of triplets that can form
 * a valid triangle (sum of any two sides > third side).
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Check All Triplets)
// Time: O(n^3) | Space: O(1)
// ============================================================
class BruteForce {
    public static int solve(int[] A) {
        int n = A.length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    int a = A[i], b = A[j], c = A[k];
                    if (a + b > c && a + c > b && b + c > a) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}

// ============================================================
// Approach 2: Optimal (Sort + Two Pointer)
// Time: O(n^2) | Space: O(1) if in-place sort
// ============================================================
class Optimal {
    public static int solve(int[] A) {
        Arrays.sort(A);
        int n = A.length;
        int count = 0;

        for (int k = 2; k < n; k++) {
            int left = 0, right = k - 1;
            while (left < right) {
                if (A[left] + A[right] > A[k]) {
                    count += right - left;
                    right--;
                } else {
                    left++;
                }
            }
        }
        return count;
    }
}

// ============================================================
// Approach 3: Best (Same Sort + Two Pointer, cleanest form)
// Time: O(n^2) | Space: O(1) if in-place sort
// ============================================================
class Best {
    public static int solve(int[] A) {
        Arrays.sort(A);
        int n = A.length;
        int count = 0;

        for (int i = n - 1; i >= 2; i--) {
            int lo = 0, hi = i - 1;
            while (lo < hi) {
                if (A[lo] + A[hi] > A[i]) {
                    count += hi - lo;
                    hi--;
                } else {
                    lo++;
                }
            }
        }
        return count;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Counting Triangles ===\n");

        int[][][] tests = {
            {{3, 4, 6, 7}, {3}},
            {{10, 21, 22, 100, 101, 200, 300}, {6}},
            {{1, 1, 1, 1}, {4}},
            {{1, 2, 3}, {0}},
            {{4, 6, 3, 7}, {3}},
        };

        for (int[][] t : tests) {
            int[] A = t[0];
            int expected = t[1][0];

            int b = BruteForce.solve(A.clone());
            int o = Optimal.solve(A.clone());
            int h = Best.solve(A.clone());
            boolean pass = b == expected && o == expected && h == expected;

            System.out.println("Input:    A=" + Arrays.toString(A));
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + h);
            System.out.println("Expected: " + expected);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
