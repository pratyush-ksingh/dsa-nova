/**
 * Problem: Number of Nice Subarrays
 * Difficulty: MEDIUM | XP: 25
 * LeetCode #1248
 *
 * Key Insight: exactlyK = atMost(k) - atMost(k-1), each computed via sliding window.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Enumerate All Subarrays
    // Time: O(n^2)  |  Space: O(1)
    //
    // For each starting index, expand and count odds.
    // ============================================================
    public static int bruteForce(int[] nums, int k) {
        int n = nums.length;
        int result = 0;
        for (int i = 0; i < n; i++) {
            int oddCount = 0;
            for (int j = i; j < n; j++) {
                if (nums[j] % 2 != 0) oddCount++;
                if (oddCount == k) result++;
                if (oddCount > k) break;
            }
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Prefix Sum + HashMap
    // Time: O(n)  |  Space: O(n)
    //
    // Track running odd count. Use map to count prefix frequencies.
    // For each j, add freq of (oddCount - k) to result.
    // ============================================================
    public static int optimal(int[] nums, int k) {
        java.util.Map<Integer, Integer> prefixCount = new java.util.HashMap<>();
        prefixCount.put(0, 1);
        int oddCount = 0;
        int result = 0;

        for (int num : nums) {
            if (num % 2 != 0) oddCount++;
            result += prefixCount.getOrDefault(oddCount - k, 0);
            prefixCount.merge(oddCount, 1, Integer::sum);
        }

        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Sliding Window: atMost(k) - atMost(k-1)
    // Time: O(n)  |  Space: O(1)
    //
    // atMost(k) counts subarrays with <= k odds using sliding window.
    // exactlyK = atMost(k) - atMost(k-1).
    // ============================================================
    public static int best(int[] nums, int k) {
        return atMost(nums, k) - atMost(nums, k - 1);
    }

    private static int atMost(int[] nums, int k) {
        if (k < 0) return 0;
        int left = 0, oddCount = 0, result = 0;
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] % 2 != 0) oddCount++;

            while (oddCount > k) {
                if (nums[left] % 2 != 0) oddCount--;
                left++;
            }

            result += right - left + 1;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Number of Nice Subarrays ===");

        int[][] arrays = {{1,1,2,1,1}, {2,4,6}, {2,2,2,1,2,2,1,2,2,2}};
        int[] ks = {3, 1, 2};

        for (int t = 0; t < arrays.length; t++) {
            System.out.printf("nums=%s, k=%d%n", java.util.Arrays.toString(arrays[t]), ks[t]);
            System.out.printf("  Brute:   %d%n", bruteForce(arrays[t], ks[t]));
            System.out.printf("  Optimal: %d%n", optimal(arrays[t], ks[t]));
            System.out.printf("  Best:    %d%n", best(arrays[t], ks[t]));
            System.out.println();
        }
    }
}
