import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// Try all subarrays; track longest one with sum == 0.
// ============================================================
class BruteForce {
    public static int solve(int[] arr) {
        int n = arr.length, maxLen = 0;
        for (int i = 0; i < n; i++) {
            long sum = 0;
            for (int j = i; j < n; j++) {
                sum += arr[j];
                if (sum == 0) maxLen = Math.max(maxLen, j - i + 1);
            }
        }
        return maxLen;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Prefix Sum + HashMap)
// Time: O(n)  |  Space: O(n)
// If prefix[j] == prefix[i], then subarray (i+1..j) sums to 0.
// Store the FIRST index where each prefix sum appeared to maximise length.
// ============================================================
class Optimal {
    public static int solve(int[] arr) {
        Map<Long, Integer> firstSeen = new HashMap<>();
        firstSeen.put(0L, -1);
        long sum = 0;
        int maxLen = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (firstSeen.containsKey(sum))
                maxLen = Math.max(maxLen, i - firstSeen.get(sum));
            else
                firstSeen.put(sum, i);
        }
        return maxLen;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(n)
// Same as Optimal — uses get() to avoid double map lookup.
// ============================================================
class Best {
    public static int solve(int[] arr) {
        Map<Long, Integer> map = new HashMap<>();
        map.put(0L, -1);
        long prefix = 0;
        int maxLen = 0;
        for (int i = 0; i < arr.length; i++) {
            prefix += arr[i];
            Integer prev = map.get(prefix);
            if (prev != null) maxLen = Math.max(maxLen, i - prev);
            else map.put(prefix, i);
        }
        return maxLen;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Largest Subarray with Sum 0 ===");
        int[][] tests = {
            {15, -2, 2, -8, 1, 7, 10, 23},
            {1, 2, 3},
            {0, 0, 0},
            {1, -1, 3, -3, 2},
            {3, -3, 1, -1, 2},
        };
        int[] expected = {5, 0, 3, 4, 4};

        for (int t = 0; t < tests.length; t++) {
            int bf  = BruteForce.solve(tests[t].clone());
            int op  = Optimal.solve(tests[t].clone());
            int bst = Best.solve(tests[t].clone());
            System.out.println("arr=" + Arrays.toString(tests[t])
                + "  BF=" + bf + "  OPT=" + op + "  BEST=" + bst + "  EXP=" + expected[t]);
        }
    }
}
