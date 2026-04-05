import java.util.*;

/**
 * Problem: Subarray with B Odd Numbers
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Count the number of subarrays of array A that contain exactly B odd numbers.
 *
 * Key insight: exactly(B) = atMost(B) - atMost(B-1)
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // Check every subarray by counting odd numbers inside it.
    // ============================================================
    public static long bruteForce(int[] A, int B) {
        int n = A.length;
        long count = 0;
        for (int i = 0; i < n; i++) {
            int odds = 0;
            for (int j = i; j < n; j++) {
                if (A[j] % 2 != 0) odds++;
                if (odds == B) count++;
                else if (odds > B) break;
            }
        }
        return count;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Prefix Count + HashMap
    // Time: O(n)  |  Space: O(n)
    // Track prefix count of odd numbers. For each index j,
    // count prefixes ending at j with exactly B odds:
    //   prefixOdds[j] - prefixOdds[i] = B  =>  prefixOdds[i] = prefixOdds[j] - B
    // Use a HashMap to count frequency of each prefix odd count.
    // ============================================================
    public static long optimal(int[] A, int B) {
        int n = A.length;
        long count = 0;
        int oddCount = 0;
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1); // empty prefix

        for (int x : A) {
            if (x % 2 != 0) oddCount++;
            count += freq.getOrDefault(oddCount - B, 0);
            freq.merge(oddCount, 1, Integer::sum);
        }
        return count;
    }

    // ============================================================
    // APPROACH 3: BEST — Sliding Window: atMost(B) - atMost(B-1)
    // Time: O(n)  |  Space: O(1)
    // atMost(k): number of subarrays with AT MOST k odd numbers.
    // exactly(B) = atMost(B) - atMost(B-1)
    // Uses two-pointer / sliding window, no extra space needed.
    // ============================================================
    private static long atMost(int[] A, int k) {
        if (k < 0) return 0;
        long count = 0;
        int left = 0, odds = 0;
        for (int right = 0; right < A.length; right++) {
            if (A[right] % 2 != 0) odds++;
            while (odds > k) {
                if (A[left] % 2 != 0) odds--;
                left++;
            }
            count += (right - left + 1);
        }
        return count;
    }

    public static long best(int[] A, int B) {
        return atMost(A, B) - atMost(A, B - 1);
    }

    public static void main(String[] args) {
        System.out.println("=== Subarray with B Odd Numbers ===");

        int[][] testArrays = {
            {1, 2, 3, 4, 5},  // B=2 -> subarrays with exactly 2 odds
            {2, 2, 2, 2},     // B=1 -> 0 odd subarrays
            {1, 1, 1},        // B=2 -> subarrays: [1,1], [1,1,1] = ...
            {1, 2, 1, 2},     // B=1
        };
        int[] bVals = {2, 1, 2, 1};

        for (int i = 0; i < testArrays.length; i++) {
            int[] A = testArrays[i];
            int B = bVals[i];
            System.out.printf("A=%s, B=%d -> Brute=%d, Optimal=%d, Best=%d%n",
                    Arrays.toString(A), B,
                    bruteForce(A, B), optimal(A, B), best(A, B));
        }
    }
}
