/**
 * Problem: Count Subarrays Where Max Appears K Times
 * Difficulty: HARD | XP: 50
 *
 * Given an array nums and integer k, count the number of subarrays where
 * the global maximum element of the entire array appears at least k times.
 *
 * Key insight: a subarray is valid if it contains the global max at least k times.
 * Use sliding window: maintain a window where count of max == k.
 * For each valid right end, number of valid starts = (position of (k-th max from right) + 1).
 *
 * @author DSA_Nova
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Check every subarray
    // Time: O(N^2)  |  Space: O(1)
    // ============================================================
    public static long bruteForce(int[] nums, int k) {
        int n = nums.length;
        int globalMax = Arrays.stream(nums).max().getAsInt();
        long count = 0;
        for (int i = 0; i < n; i++) {
            int maxCount = 0;
            for (int j = i; j < n; j++) {
                if (nums[j] == globalMax) maxCount++;
                if (maxCount >= k) count++;
            }
        }
        return count;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Sliding Window with positions list
    // Time: O(N)  |  Space: O(N)
    // Store positions of global max. For each right boundary, find the
    // position of the k-th max from the right. All starts from 0..pos[r-k]
    // form valid subarrays. Use prefix counting.
    // ============================================================
    public static long optimal(int[] nums, int k) {
        int n = nums.length;
        int globalMax = 0;
        for (int x : nums) globalMax = Math.max(globalMax, x);

        // Collect positions of globalMax
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (nums[i] == globalMax) positions.add(i);

        long count = 0;
        int m = positions.size();
        for (int r = k - 1; r < m; r++) {
            // When right boundary is at position positions[r],
            // and we have exactly k occurrences from positions[r-k+1]..positions[r],
            // valid left starts: 0 .. positions[r-k+1]
            int leftBound = positions.get(r - k + 1); // must include this position
            // Subarrays ending at any index >= positions[r] with left <= leftBound are valid.
            // We count subarrays ending exactly at positions[r] contributing:
            // (leftBound - (r > 0 ? positions[r-k] + 1 : 0) + 1) valid lefts
            // Actually simpler: total subarrays where right >= positions[r]
            // Let's use the standard approach: for each right end j in [positions[r], n-1],
            // number of valid lefts = leftBound + 1
            // We aggregate: for window of k maxes at [r-k+1..r]:
            // right end ranges from positions[r] to positions[r+1]-1 (or n-1)
            int rightEnd = (r + 1 < m) ? positions.get(r + 1) - 1 : n - 1;
            int leftStart = (r - k >= 0) ? positions.get(r - k) + 1 : 0;
            // For each right in [positions[r], rightEnd], valid lefts = [leftStart, leftBound]
            count += (long)(rightEnd - positions.get(r) + 1) * (leftBound - leftStart + 1);
        }
        return count;
    }

    // ============================================================
    // APPROACH 3: BEST - Two-pointer sliding window (cleaner)
    // Time: O(N)  |  Space: O(1) extra (beyond input)
    // Count subarrays where global max appears >= k times.
    // Equivalently: total - subarrays where count < k.
    // Use: count(>= k) = total subarrays - count(< k)
    //                  = n*(n+1)/2 - countLessThanK(k)
    // Or directly: for each right, maintain a sliding window where count == k,
    // counting valid left positions.
    // ============================================================
    public static long best(int[] nums, int k) {
        int n = nums.length;
        int globalMax = 0;
        for (int x : nums) globalMax = Math.max(globalMax, x);

        // Count subarrays with at least k occurrences of globalMax:
        // = total - count with < k occurrences
        // Use helper: count subarrays with < k occurrences
        return countAtLeast(nums, globalMax, k);
    }

    private static long countAtLeast(int[] nums, int maxVal, int k) {
        // Sliding window: count subarrays with count of maxVal >= k
        // For each right end, find smallest left such that count >= k.
        // All subarrays with left <= that smallest valid left are valid.
        int n = nums.length;
        long result = 0;
        int left = 0, cnt = 0;
        // We track: for each right, how many valid lefts
        // Keep a deque/window of last k positions of maxVal
        List<Integer> positions = new ArrayList<>();
        for (int right = 0; right < n; right++) {
            if (nums[right] == maxVal) positions.add(right);
            if (positions.size() >= k) {
                // Leftmost valid left = 0 .. positions[size-k]
                result += positions.get(positions.size() - k) + 1;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Count Subarrays Where Max Appears K Times ===");

        int[][] tests = {{1,3,2,3,1}, {1,4,2,1}};
        int[] ks = {3, 3};
        long[] expected = {6, 0};

        for (int t = 0; t < tests.length; t++) {
            long b = bruteForce(tests[t], ks[t]);
            long o = optimal(tests[t], ks[t]);
            long be = best(tests[t], ks[t]);
            System.out.printf("Test%d k=%d: brute=%d opt=%d best=%d (exp=%d) %s%n",
                t+1, ks[t], b, o, be, expected[t],
                (b==expected[t] && o==expected[t] && be==expected[t]) ? "OK" : "FAIL");
        }

        // Additional test
        int[] A = {1,3,2,3,3};
        System.out.println("brute=" + bruteForce(A, 2) + " opt=" + optimal(A, 2) + " best=" + best(A, 2));
    }
}
