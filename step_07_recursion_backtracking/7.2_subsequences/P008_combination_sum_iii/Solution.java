/**
 * Problem: Combination Sum III
 * Difficulty: MEDIUM | XP: 25
 *
 * Find all valid combinations of k numbers that sum to n.
 * Each number in [1..9] may be used at most once.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(9! / (9-k)!)  |  Space: O(k) recursion depth
    // Generate all subsets, filter those with size k and sum n
    // ============================================================
    static List<List<Integer>> bruteForceResult = new ArrayList<>();

    public static void bruteForceHelper(int start, int k, int n,
                                         List<Integer> current, List<List<Integer>> res) {
        if (current.size() == k && n == 0) {
            res.add(new ArrayList<>(current));
            return;
        }
        if (current.size() == k || n <= 0) return;

        for (int num = start; num <= 9; num++) {
            current.add(num);
            bruteForceHelper(num + 1, k, n - num, current, res);
            current.remove(current.size() - 1);
        }
    }

    public static List<List<Integer>> bruteForce(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        bruteForceHelper(1, k, n, new ArrayList<>(), res);
        return res;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Backtracking with pruning
    // Time: O(C(9,k))  |  Space: O(k)
    // Prune when remaining sum < 0 or not enough numbers left
    // ============================================================
    public static void optimalHelper(int start, int k, int remain,
                                      List<Integer> current, List<List<Integer>> res) {
        if (k == 0 && remain == 0) {
            res.add(new ArrayList<>(current));
            return;
        }
        // Pruning: not enough numbers left or remain already negative
        if (k <= 0 || remain <= 0) return;
        // Prune: even if we pick k consecutive largest numbers, sum must be >= remain
        for (int num = start; num <= 9; num++) {
            // Pruning: smallest k numbers from num must not exceed remain
            if (num > remain) break;
            current.add(num);
            optimalHelper(num + 1, k - 1, remain - num, current, res);
            current.remove(current.size() - 1);
        }
    }

    public static List<List<Integer>> optimal(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        optimalHelper(1, k, n, new ArrayList<>(), res);
        return res;
    }

    // ============================================================
    // APPROACH 3: BEST - Iterative bitmask (for small fixed domain 1..9)
    // Time: O(2^9 * 9)  |  Space: O(k * results)
    // Enumerate all 512 subsets of {1..9}, pick those with size k and sum n
    // ============================================================
    public static List<List<Integer>> best(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        for (int mask = 0; mask < (1 << 9); mask++) {
            List<Integer> combo = new ArrayList<>();
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                if ((mask & (1 << i)) != 0) {
                    combo.add(i + 1);
                    sum += (i + 1);
                }
            }
            if (combo.size() == k && sum == n) {
                res.add(combo);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println("=== Combination Sum III ===");

        int k = 3, n = 7;
        System.out.println("k=" + k + ", n=" + n);
        System.out.println("Brute:   " + bruteForce(k, n));
        System.out.println("Optimal: " + optimal(k, n));
        System.out.println("Best:    " + best(k, n));

        k = 3; n = 9;
        System.out.println("\nk=" + k + ", n=" + n);
        System.out.println("Brute:   " + bruteForce(k, n));
        System.out.println("Optimal: " + optimal(k, n));
        System.out.println("Best:    " + best(k, n));

        k = 4; n = 1;
        System.out.println("\nk=" + k + ", n=" + n + " (no solution)");
        System.out.println("Brute:   " + bruteForce(k, n));
        System.out.println("Optimal: " + optimal(k, n));
        System.out.println("Best:    " + best(k, n));
    }
}
