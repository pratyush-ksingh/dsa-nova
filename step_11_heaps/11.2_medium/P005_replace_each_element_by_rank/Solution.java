/**
 * Problem: Replace Each Element by Rank
 * Difficulty: MEDIUM | XP: 25
 *
 * Replace each element in the array with its rank (1-indexed).
 * Rank is determined by value: smallest element gets rank 1.
 * Equal elements get the same rank.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n²)  |  Space: O(n) for result
    // ============================================================
    /**
     * For each element, count distinct values strictly smaller than it.
     * rank(x) = count(distinct smaller values) + 1
     */
    public static int[] bruteForce(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            Set<Integer> smaller = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if (arr[j] < arr[i]) {
                    smaller.add(arr[j]);
                }
            }
            result[i] = smaller.size() + 1;
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * Sort elements with their original indices.
     * Walk sorted list assigning ranks; increment rank only on value change.
     * Map each value to its rank, then build result array.
     */
    public static int[] optimal(int[] arr) {
        int n = arr.length;
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;

        // Sort indices by their corresponding values
        Arrays.sort(indices, (a, b) -> arr[a] - arr[b]);

        int[] rankMap = new int[n]; // rankMap[original_index] = rank
        int rank = 1;
        for (int i = 0; i < n; i++) {
            if (i > 0 && arr[indices[i]] != arr[indices[i - 1]]) {
                rank++;
            }
            rankMap[indices[i]] = rank;
        }
        return rankMap;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * Use a TreeMap (sorted by key) to map each unique value to its rank.
     * First pass: insert all values into TreeMap.
     * Second pass: assign ranks in sorted order.
     * Third pass: build result using the map.
     */
    public static int[] best(int[] arr) {
        int n = arr.length;
        TreeMap<Integer, Integer> rankMap = new TreeMap<>();

        // Collect unique values
        for (int x : arr) rankMap.put(x, 0);

        // Assign ranks in sorted order
        int rank = 1;
        for (int key : rankMap.keySet()) {
            rankMap.put(key, rank++);
        }

        // Build result
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = rankMap.get(arr[i]);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Replace Each Element by Rank ===");

        int[] test1 = {100, 37, 15, 1, 900, 2, 2};
        int[] test2 = {40, 10, 20, 30};
        int[] test3 = {5, 5, 5};

        System.out.println("Input:   " + Arrays.toString(test1));
        System.out.println("Brute:   " + Arrays.toString(bruteForce(test1)));
        System.out.println("Optimal: " + Arrays.toString(optimal(test1)));
        System.out.println("Best:    " + Arrays.toString(best(test1)));
        System.out.println();
        System.out.println("Input:   " + Arrays.toString(test2));
        System.out.println("Best:    " + Arrays.toString(best(test2)));
        System.out.println();
        System.out.println("Input:   " + Arrays.toString(test3));
        System.out.println("Best:    " + Arrays.toString(best(test3)));
    }
}
