import java.util.*;

/**
 * Problem: Counting Frequency of Elements
 * Difficulty: EASY | XP: 10
 *
 * Given an array, count the frequency of each element.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Nested Loop Counting
    // Time: O(N^2)  |  Space: O(N)
    // For each element, scan entire array to count. Track visited.
    // ============================================================
    static class BruteForce {
        public static Map<Integer, Integer> countFrequency(int[] arr) {
            Map<Integer, Integer> result = new LinkedHashMap<>();
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
                result.put(arr[i], count);
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- HashMap (Single Pass)
    // Time: O(N)  |  Space: O(K) where K = unique elements
    // One pass: for each element, increment its count in the map.
    // ============================================================
    static class Optimal {
        public static Map<Integer, Integer> countFrequency(int[] arr) {
            Map<Integer, Integer> freq = new LinkedHashMap<>();
            for (int x : arr) {
                freq.put(x, freq.getOrDefault(x, 0) + 1);
            }
            return freq;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Sorting + Linear Scan
    // Time: O(N log N)  |  Space: O(1) extra (in-place sort)
    // Sort, then count consecutive equal elements.
    // Trade-off: no extra space, but slower than HashMap.
    // ============================================================
    static class Best {
        public static Map<Integer, Integer> countFrequency(int[] arr) {
            Map<Integer, Integer> result = new LinkedHashMap<>();
            if (arr.length == 0) return result;

            int[] sorted = arr.clone(); // preserve original
            Arrays.sort(sorted);

            int count = 1;
            for (int i = 1; i < sorted.length; i++) {
                if (sorted[i] == sorted[i - 1]) {
                    count++;
                } else {
                    result.put(sorted[i - 1], count);
                    count = 1;
                }
            }
            // Don't forget the last group
            result.put(sorted[sorted.length - 1], count);
            return result;
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 2, 1, 3, 1, 2};
        System.out.println("=== Counting Frequency of Elements ===");
        System.out.println("Input: " + Arrays.toString(arr));

        System.out.println("--- Brute Force ---");
        System.out.println(BruteForce.countFrequency(arr));

        System.out.println("--- Optimal (HashMap) ---");
        System.out.println(Optimal.countFrequency(arr));

        System.out.println("--- Best (Sort + Scan) ---");
        System.out.println(Best.countFrequency(arr));
    }
}
