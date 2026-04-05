import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// Check every subarray — no early exit (array may have negatives)
// ============================================================
class BruteForce {
    public static int solve(int[] arr, int k) {
        int n = arr.length, maxLen = 0;
        for (int i = 0; i < n; i++) {
            long sum = 0;
            for (int j = i; j < n; j++) {
                sum += arr[j];
                if (sum == k) maxLen = Math.max(maxLen, j - i + 1);
            }
        }
        return maxLen;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Prefix Sum + HashMap)
// Time: O(n)  |  Space: O(n)
// Key insight: if prefixSum[j] - prefixSum[i] = k,
// then subarray (i+1..j) has sum k.
// Store FIRST occurrence of each prefix sum (for max length).
// ============================================================
class Optimal {
    public static int solve(int[] arr, int k) {
        Map<Long, Integer> map = new HashMap<>();
        map.put(0L, -1);
        long sum = 0;
        int maxLen = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (map.containsKey(sum - k))
                maxLen = Math.max(maxLen, i - map.get(sum - k));
            map.putIfAbsent(sum, i);  // store only first occurrence
        }
        return maxLen;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(n)
// Same as Optimal — for arrays with negatives this IS the best.
// Variation: handle int overflow by using long prefix sums
// and cast to long carefully.
// ============================================================
class Best {
    public static int solve(int[] arr, int k) {
        int n = arr.length, maxLen = 0;
        Map<Long, Integer> firstSeen = new HashMap<>();
        firstSeen.put(0L, -1);
        long prefix = 0;
        for (int i = 0; i < n; i++) {
            prefix += arr[i];
            long target = prefix - k;
            Integer idx = firstSeen.get(target);
            if (idx != null) maxLen = Math.max(maxLen, i - idx);
            firstSeen.putIfAbsent(prefix, i);
        }
        return maxLen;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Longest Subarray with Sum K (With Negatives) ===");

        int[][] arrays = {
            {10, 5, 2, 7, 1, -10},
            {-5, 8, -14, 2, 4, 12},
            {0, 0, 5, 5, 0, 0},
            {1, -1, 5, -2, 3},
        };
        int[] ks = {15, -5, 5, 3};
        int[] expected = {6, 5, 6, 4};

        for (int t = 0; t < arrays.length; t++) {
            int bf = BruteForce.solve(arrays[t], ks[t]);
            int op = Optimal.solve(arrays[t], ks[t]);
            int bst = Best.solve(arrays[t], ks[t]);
            System.out.println("arr=" + Arrays.toString(arrays[t]) + " k=" + ks[t]
                + "  BF=" + bf + " OPT=" + op + " BEST=" + bst + " EXP=" + expected[t]);
        }
    }
}
