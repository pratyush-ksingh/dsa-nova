/**
 * Problem: Subset Sum II
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array (may contain duplicates) and a target,
 * find all UNIQUE subsets that sum to the target.
 * Real-life use: Combinatorial search, resource allocation with duplicate items.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Generate all subsets, compute their sums, use a Set to deduplicate.
    // Time: O(2^N * N)  |  Space: O(2^N * N)
    // ============================================================
    static class BruteForce {
        public static List<List<Integer>> findSubsets(int[] arr, int target) {
            Arrays.sort(arr);
            Set<List<Integer>> unique = new HashSet<>();
            int n = arr.length;
            for (int mask = 0; mask < (1 << n); mask++) {
                List<Integer> subset = new ArrayList<>();
                int sum = 0;
                for (int i = 0; i < n; i++) {
                    if ((mask & (1 << i)) != 0) {
                        subset.add(arr[i]);
                        sum += arr[i];
                    }
                }
                if (sum == target) unique.add(subset);
            }
            return new ArrayList<>(unique);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Backtracking with sorting + skip-duplicates at the same level.
    // Sort first so duplicates are adjacent; skip arr[i] if arr[i]==arr[i-1]
    // at the same recursion depth (i > start).
    // Time: O(2^N)  |  Space: O(N) recursion stack
    // ============================================================
    static class Optimal {
        public static List<List<Integer>> findSubsets(int[] arr, int target) {
            Arrays.sort(arr);
            List<List<Integer>> result = new ArrayList<>();
            backtrack(arr, 0, target, new ArrayList<>(), result);
            return result;
        }

        private static void backtrack(int[] arr, int start, int remaining,
                                      List<Integer> current, List<List<Integer>> result) {
            if (remaining == 0) {
                result.add(new ArrayList<>(current));
                return;
            }
            for (int i = start; i < arr.length; i++) {
                // Skip duplicate at same level
                if (i > start && arr[i] == arr[i - 1]) continue;
                if (arr[i] > remaining) break; // pruning (array is sorted)
                current.add(arr[i]);
                backtrack(arr, i + 1, remaining - arr[i], current, result);
                current.remove(current.size() - 1);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Same backtracking but with explicit sum tracking (avoids repeated subtraction).
    // Also returns results sorted lexicographically (natural result of sorted input).
    // Time: O(2^N)  |  Space: O(N)
    // ============================================================
    static class Best {
        public static List<List<Integer>> findSubsets(int[] arr, int target) {
            Arrays.sort(arr);
            List<List<Integer>> result = new ArrayList<>();
            backtrack(arr, 0, 0, target, new ArrayList<>(), result);
            return result;
        }

        private static void backtrack(int[] arr, int start, int currentSum, int target,
                                      List<Integer> current, List<List<Integer>> result) {
            if (currentSum == target) {
                result.add(new ArrayList<>(current));
                return;
            }
            for (int i = start; i < arr.length; i++) {
                if (i > start && arr[i] == arr[i - 1]) continue;
                if (currentSum + arr[i] > target) break;
                current.add(arr[i]);
                backtrack(arr, i + 1, currentSum + arr[i], target, current, result);
                current.remove(current.size() - 1);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Subset Sum II ===");

        int[][] inputs = {{1, 1, 2, 2}, {10, 1, 2, 7, 6, 1, 5}, {2, 5, 2, 1, 2}};
        int[] targets = {3, 8, 5};

        for (int i = 0; i < inputs.length; i++) {
            int t = targets[i];
            System.out.printf("%narr=%s  target=%d%n", Arrays.toString(inputs[i]), t);
            System.out.println("  Brute  : " + BruteForce.findSubsets(inputs[i].clone(), t));
            System.out.println("  Optimal: " + Optimal.findSubsets(inputs[i].clone(), t));
            System.out.println("  Best   : " + Best.findSubsets(inputs[i].clone(), t));
        }
    }
}
