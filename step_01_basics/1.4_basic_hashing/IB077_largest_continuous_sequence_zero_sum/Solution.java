/**
 * Problem: Largest Continuous Sequence Zero Sum
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Find the longest subarray whose elements sum to 0.
 * Return the subarray itself (empty array if none found).
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    /**
     * Try every subarray, compute its sum, track the longest zero-sum one.
     * Real-life: Auditing financial transactions to find periods that cancel out.
     */
    public static int[] bruteForce(int[] A) {
        int n = A.length;
        int maxLen = 0;
        int start = -1;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += A[j];
                if (sum == 0 && (j - i + 1) > maxLen) {
                    maxLen = j - i + 1;
                    start = i;
                }
            }
        }
        if (start == -1) return new int[]{};
        return Arrays.copyOfRange(A, start, start + maxLen);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Prefix sum + HashMap: if prefixSum[i] == prefixSum[j] then subarray (i+1..j) sums to 0.
     * Store the first occurrence index of each prefix sum.
     * Real-life: Balance sheet analysis — finding date ranges with net-zero cash flow.
     */
    public static int[] optimal(int[] A) {
        int n = A.length;
        // map from prefix sum -> earliest index where it occurred
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1); // prefix sum 0 occurs before index 0
        int sum = 0;
        int maxLen = 0;
        int start = -1;
        for (int i = 0; i < n; i++) {
            sum += A[i];
            if (map.containsKey(sum)) {
                int prevIdx = map.get(sum);
                int len = i - prevIdx;
                if (len > maxLen) {
                    maxLen = len;
                    start = prevIdx + 1;
                }
            } else {
                map.put(sum, i);
            }
        }
        if (start == -1) return new int[]{};
        return Arrays.copyOfRange(A, start, start + maxLen);
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Same as Optimal — this is already the best achievable complexity.
     * Slight refinement: always update the answer immediately upon finding a match
     * (same logic, provided here for completeness with explicit edge-case handling).
     * Real-life: Signal processing — finding the longest interval of balanced positive/negative signals.
     */
    public static int[] best(int[] A) {
        // Edge case: entire array is zero
        int n = A.length;
        Map<Integer, Integer> firstSeen = new HashMap<>();
        firstSeen.put(0, -1);
        int prefixSum = 0;
        int bestStart = -1, bestEnd = -1, bestLen = 0;
        for (int i = 0; i < n; i++) {
            prefixSum += A[i];
            if (firstSeen.containsKey(prefixSum)) {
                int len = i - firstSeen.get(prefixSum);
                if (len > bestLen) {
                    bestLen = len;
                    bestStart = firstSeen.get(prefixSum) + 1;
                    bestEnd = i;
                }
            } else {
                firstSeen.put(prefixSum, i);
            }
        }
        if (bestStart == -1) return new int[]{};
        return Arrays.copyOfRange(A, bestStart, bestEnd + 1);
    }

    public static void main(String[] args) {
        System.out.println("=== Largest Continuous Sequence Zero Sum ===");

        int[][] tests = {
            {1, 2, -2, 4, -4},
            {1, 0, -1},
            {0},
            {1, 2, 3},
            {3, -3, 1, -1, 2, -2},
            {0, 0, 0},
        };
        String[] expected = {"[2,-2,4,-4]", "[1,0,-1]", "[0]", "[]", "[3,-3,1,-1,2,-2]", "[0,0,0]"};

        for (int t = 0; t < tests.length; t++) {
            int[] arr = tests[t];
            System.out.println("\nInput: " + Arrays.toString(arr));
            System.out.println("Expected: " + expected[t]);
            System.out.println("Brute:   " + Arrays.toString(bruteForce(arr.clone())));
            System.out.println("Optimal: " + Arrays.toString(optimal(arr.clone())));
            System.out.println("Best:    " + Arrays.toString(best(arr.clone())));
        }
    }
}
