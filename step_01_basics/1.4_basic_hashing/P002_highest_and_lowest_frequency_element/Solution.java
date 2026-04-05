import java.util.*;

/**
 * Problem: Highest and Lowest Frequency Element
 * Difficulty: EASY | XP: 10
 *
 * Given an array, find the elements with the highest and lowest frequency.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Nested Loop Counting
    // Time: O(N^2)  |  Space: O(N)
    // For each unique element, scan entire array to count.
    // Track max/min frequency as we go.
    // ============================================================
    static class BruteForce {
        public static int[] findHighestAndLowest(int[] arr) {
            int maxFreq = 0, minFreq = Integer.MAX_VALUE;
            int maxElem = arr[0], minElem = arr[0];
            boolean[] visited = new boolean[arr.length];

            for (int i = 0; i < arr.length; i++) {
                if (visited[i]) continue;

                int count = 0;
                for (int j = i; j < arr.length; j++) {
                    if (arr[j] == arr[i]) {
                        count++;
                        visited[j] = true;
                    }
                }

                if (count > maxFreq) {
                    maxFreq = count;
                    maxElem = arr[i];
                }
                if (count < minFreq) {
                    minFreq = count;
                    minElem = arr[i];
                }
            }
            return new int[]{maxElem, minElem};
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- HashMap + Linear Scan
    // Time: O(N)  |  Space: O(K) where K = unique elements
    // Build frequency map in one pass, then scan map for max/min.
    // ============================================================
    static class Optimal {
        public static int[] findHighestAndLowest(int[] arr) {
            Map<Integer, Integer> freq = new HashMap<>();
            for (int x : arr) {
                freq.put(x, freq.getOrDefault(x, 0) + 1);
            }

            int maxFreq = 0, minFreq = Integer.MAX_VALUE;
            int maxElem = arr[0], minElem = arr[0];

            for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
                int count = entry.getValue();
                if (count > maxFreq) {
                    maxFreq = count;
                    maxElem = entry.getKey();
                }
                if (count < minFreq) {
                    minFreq = count;
                    minElem = entry.getKey();
                }
            }
            return new int[]{maxElem, minElem};
        }
    }

    public static void main(String[] args) {
        int[][] tests = {
            {10, 5, 10, 15, 10, 5},
            {1, 1, 1, 1},
            {7},
            {3, 2, 3, 2},
            {1, 2, 3, 4, 5}
        };

        System.out.println("=== Highest and Lowest Frequency Element ===");
        for (int[] arr : tests) {
            System.out.println("\nInput: " + Arrays.toString(arr));

            int[] bf = BruteForce.findHighestAndLowest(arr);
            System.out.println("  Brute Force -> Highest: " + bf[0] + ", Lowest: " + bf[1]);

            int[] opt = Optimal.findHighestAndLowest(arr);
            System.out.println("  Optimal     -> Highest: " + opt[0] + ", Lowest: " + opt[1]);
        }
    }
}
