import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// Try all subarrays, track the one with sum == k and max length
// ============================================================
class BruteForce {
    public static int solve(int[] arr, int k) {
        int n = arr.length, maxLen = 0;
        for (int i = 0; i < n; i++) {
            long sum = 0;
            for (int j = i; j < n; j++) {
                sum += arr[j];
                if (sum == k) maxLen = Math.max(maxLen, j - i + 1);
                if (sum > k) break; // positives only: no need to go further
            }
        }
        return maxLen;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Prefix Sum + HashMap)
// Time: O(n)  |  Space: O(n)
// Store prefix sums in map; if prefixSum-k seen, update length.
// Works for all arrays (including negatives).
// ============================================================
class Optimal {
    public static int solve(int[] arr, int k) {
        Map<Long, Integer> prefixMap = new HashMap<>();
        prefixMap.put(0L, -1);
        long sum = 0;
        int maxLen = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (prefixMap.containsKey(sum - k))
                maxLen = Math.max(maxLen, i - prefixMap.get(sum - k));
            prefixMap.putIfAbsent(sum, i);
        }
        return maxLen;
    }
}

// ============================================================
// APPROACH 3: BEST (Two Pointers — works only for positives)
// Time: O(n)  |  Space: O(1)
// Sliding window: expand right, shrink left when sum > k
// ============================================================
class Best {
    public static int solve(int[] arr, int k) {
        int n = arr.length, lo = 0, maxLen = 0;
        long sum = 0;
        for (int hi = 0; hi < n; hi++) {
            sum += arr[hi];
            while (sum > k && lo <= hi) sum -= arr[lo++];
            if (sum == k) maxLen = Math.max(maxLen, hi - lo + 1);
        }
        return maxLen;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Longest Subarray with Sum K (Positives) ===");

        int[][] arrays = {
            {1, 2, 3, 1, 1, 1, 1},
            {1, 6, 2, 5, 1},
            {3, 3, 3, 3},
            {1, 2, 3},
        };
        int[] ks = {3, 7, 6, 6};
        int[] expected = {3, 4, 2, 1};  // Note: {2,5} len=2, {1,2,3} is also len=3 -> 3 expected

        // Re-check: arr={1,6,2,5,1}, k=7: subarray [2,5]=2 or [6]=1 -> max=3 for [2,4,1] no
        // [1,6] sum=7 len=2; [2,5] sum=7 len=2; [1,2,4] not exist. max=2 -> correction
        int[] correctedExpected = {3, 2, 2, 1};

        for (int t = 0; t < arrays.length; t++) {
            int bf = BruteForce.solve(arrays[t], ks[t]);
            int op = Optimal.solve(arrays[t], ks[t]);
            int bst = Best.solve(arrays[t], ks[t]);
            System.out.println("arr=" + Arrays.toString(arrays[t]) + " k=" + ks[t]
                + " BF=" + bf + " OPT=" + op + " BEST=" + bst);
        }
    }
}
