/**
 * Problem: Print All Subsequences with Sum K
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Generate all 2^n subsets using bitmask
    // Time: O(2^n * n)  |  Space: O(n)
    // ============================================================
    public static List<List<Integer>> bruteForce(int[] arr, int k) {
        List<List<Integer>> result = new ArrayList<>();
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
            if (sum == k) result.add(subset);
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Backtracking with pruning
    // Time: O(2^n)  |  Space: O(n)
    // ============================================================
    public static List<List<Integer>> optimal(int[] arr, int k) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(arr); // sort to enable pruning
        backtrack(arr, 0, k, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(int[] arr, int idx, int k, int sum,
                                  List<Integer> current, List<List<Integer>> result) {
        if (sum == k) {
            result.add(new ArrayList<>(current));
            // Don't return; there might be zeros ahead
        }
        if (idx == arr.length || sum > k) return;
        for (int i = idx; i < arr.length; i++) {
            if (sum + arr[i] > k) break; // pruning (array is sorted)
            current.add(arr[i]);
            backtrack(arr, i + 1, k, sum + arr[i], current, result);
            current.remove(current.size() - 1);
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Pick / not-pick recursion (classic)
    // Time: O(2^n)  |  Space: O(n)
    // ============================================================
    public static List<List<Integer>> best(int[] arr, int k) {
        List<List<Integer>> result = new ArrayList<>();
        pickNotPick(arr, 0, k, 0, new ArrayList<>(), result);
        return result;
    }

    private static void pickNotPick(int[] arr, int idx, int k, int sum,
                                    List<Integer> current, List<List<Integer>> result) {
        if (idx == arr.length) {
            if (sum == k) result.add(new ArrayList<>(current));
            return;
        }
        // Pick current element
        current.add(arr[idx]);
        pickNotPick(arr, idx + 1, k, sum + arr[idx], current, result);
        current.remove(current.size() - 1);
        // Don't pick current element
        pickNotPick(arr, idx + 1, k, sum, current, result);
    }

    public static void main(String[] args) {
        System.out.println("=== Print All Subsequences with Sum K ===");
        int[] arr = {1, 2, 3, 4, 5};
        int k = 5;
        System.out.println("Brute Force: " + bruteForce(arr, k));
        System.out.println("Optimal:     " + optimal(arr, k));
        System.out.println("Best:        " + best(arr, k));
    }
}
