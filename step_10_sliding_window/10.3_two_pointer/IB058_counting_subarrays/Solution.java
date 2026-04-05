/**
 * Problem: Counting Subarrays (InterviewBit)
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an array A of N non-negative integers and integer B,
 * count the number of subarrays having sum strictly less than B.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    //
    // For each starting index i, accumulate sum going right.
    // Count subarrays where sum < B. Break early since elements
    // are non-negative (sum only grows).
    // ============================================================
    public static long bruteForce(int[] A, int B) {
        int n = A.length;
        long count = 0;
        for (int i = 0; i < n; i++) {
            long currSum = 0;
            for (int j = i; j < n; j++) {
                currSum += A[j];
                if (currSum < B) {
                    count++;
                } else {
                    break; // non-negative: sum won't decrease
                }
            }
        }
        return count;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Sliding Window (Two Pointers)
    // Time: O(n)  |  Space: O(1)
    //
    // Maintain a sliding window [left..right]. For each right,
    // shrink from left while window sum >= B. Then all subarrays
    // ending at right with start in [left..right] are valid.
    // Count = right - left + 1.
    // ============================================================
    public static long optimal(int[] A, int B) {
        int n = A.length;
        long count = 0;
        int left = 0;
        long windowSum = 0;

        for (int right = 0; right < n; right++) {
            windowSum += A[right];
            // Shrink from left while sum >= B
            while (left <= right && windowSum >= B) {
                windowSum -= A[left];
                left++;
            }
            // Valid subarrays ending at 'right': (right - left + 1) of them
            count += (right - left + 1);
        }
        return count;
    }

    // ============================================================
    // APPROACH 3: BEST -- Same Two Pointer with Edge Case Handling
    // Time: O(n)  |  Space: O(1)
    //
    // Identical to Approach 2 but handles B <= 0 explicitly and
    // uses cleaner variable names. The two-pointer approach is
    // already optimal for non-negative arrays.
    // ============================================================
    public static long best(int[] A, int B) {
        if (B <= 0) return 0;

        int n = A.length;
        long count = 0;
        int left = 0;
        long windowSum = 0;

        for (int right = 0; right < n; right++) {
            windowSum += A[right];

            while (windowSum >= B) {
                windowSum -= A[left++];
            }

            // Subarrays ending at right with start in [left..right]: (right-left+1)
            count += (right - left + 1);
        }
        return count;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Counting Subarrays (Sum < B) ===\n");

        Object[][] tests = {
            {new int[]{2, 5, 6},    10, 4L},
            {new int[]{1, 2, 3},     5, 4L},
            {new int[]{1, 11, 2},    5, 2L},
            {new int[]{1, 2, 3, 4},  5, 5L},
            {new int[]{0, 0, 0},     1, 6L},
            {new int[]{5, 6, 7},     5, 0L},
            {new int[]{1},           2, 1L},
        };

        for (Object[] test : tests) {
            int[] A = (int[]) test[0];
            int B = (int) test[1];
            long expected = (long) test[2];

            long b = bruteForce(A, B);
            long o = optimal(A, B);
            long r = best(A, B);
            String status = (b == expected && o == expected && r == expected) ? "PASS" : "FAIL";
            System.out.printf("A=%s B=%d Expected=%d Brute=%d Optimal=%d Best=%d [%s]%n",
                Arrays.toString(A), B, expected, b, o, r, status);
        }
    }
}
