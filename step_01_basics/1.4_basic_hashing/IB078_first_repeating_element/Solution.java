import java.util.*;

/**
 * Problem: First Repeating Element
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given an array, find the first element (by first occurrence index)
 * that appears more than once. Return -1 if none.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // For each element, scan the rest for a duplicate.
    // ============================================================
    static class BruteForce {
        public static int firstRepeating(int[] arr) {
            int n = arr.length;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (arr[i] == arr[j]) {
                        return arr[i];
                    }
                }
            }
            return -1;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- HashMap Frequency Count
    // Time: O(N)  |  Space: O(N)
    // Count frequencies, then scan left-to-right for first with freq > 1.
    // ============================================================
    static class Optimal {
        public static int firstRepeating(int[] arr) {
            Map<Integer, Integer> freq = new HashMap<>();
            for (int x : arr) {
                freq.put(x, freq.getOrDefault(x, 0) + 1);
            }
            for (int x : arr) {
                if (freq.get(x) > 1) return x;
            }
            return -1;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Reverse Scan with HashSet
    // Time: O(N)  |  Space: O(N)
    // Scan right-to-left; if element already in set, update answer.
    // Last update = leftmost repeating element. Single pass.
    // ============================================================
    static class Best {
        public static int firstRepeating(int[] arr) {
            Set<Integer> seen = new HashSet<>();
            int result = -1;
            for (int i = arr.length - 1; i >= 0; i--) {
                if (seen.contains(arr[i])) {
                    result = arr[i];
                } else {
                    seen.add(arr[i]);
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        int[][] tests = {
            {10, 5, 3, 4, 3, 5, 6},
            {6, 10, 5, 4, 9, 120},
            {1, 2, 3, 1, 2},
            {1}
        };
        System.out.println("=== First Repeating Element ===");
        for (int[] t : tests) {
            System.out.printf("  %s: brute=%d, optimal=%d, best=%d%n",
                Arrays.toString(t),
                BruteForce.firstRepeating(t),
                Optimal.firstRepeating(t),
                Best.firstRepeating(t));
        }
    }
}
