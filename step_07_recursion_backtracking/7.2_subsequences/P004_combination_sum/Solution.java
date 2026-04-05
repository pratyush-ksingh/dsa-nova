/**
 * Problem: Combination Sum (LeetCode 39)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array of distinct integers candidates and a target integer target,
 * return a list of all unique combinations of candidates where the chosen
 * numbers sum to target. The same number may be chosen an unlimited number
 * of times. The answer may be returned in any order.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — All combinations without pruning
    // Time: O(2^t * k)  |  Space: O(2^t * k)
    // ============================================================
    static class BruteForce {
        private List<List<Integer>> results;

        private void generate(int[] candidates, int start,
                              List<Integer> current, int remaining) {
            if (remaining == 0) {
                results.add(new ArrayList<>(current));
                return;
            }
            if (remaining < 0) return;
            for (int i = start; i < candidates.length; i++) {
                current.add(candidates[i]);
                generate(candidates, i, current, remaining - candidates[i]);
                current.remove(current.size() - 1);
            }
        }

        public List<List<Integer>> combinationSum(int[] candidates, int target) {
            results = new ArrayList<>();
            generate(candidates, 0, new ArrayList<>(), target);
            return results;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Backtracking with sort + early break
    // Time: O(2^(t/min))  |  Space: O(t/min) recursion depth
    // ============================================================
    static class Optimal {
        private List<List<Integer>> results;

        private void backtrack(int[] candidates, int start,
                               List<Integer> current, int remaining) {
            if (remaining == 0) {
                results.add(new ArrayList<>(current));
                return;
            }
            for (int i = start; i < candidates.length; i++) {
                if (candidates[i] > remaining) break; // sorted: prune rest
                current.add(candidates[i]);
                backtrack(candidates, i, current, remaining - candidates[i]);
                current.remove(current.size() - 1);
            }
        }

        public List<List<Integer>> combinationSum(int[] candidates, int target) {
            Arrays.sort(candidates);
            results = new ArrayList<>();
            backtrack(candidates, 0, new ArrayList<>(), target);
            return results;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Backtracking (pick/skip decision tree)
    // Time: O(2^(t/min))  |  Space: O(t/min)
    // ============================================================
    static class Best {
        private List<List<Integer>> results;

        /**
         * At each index: either PICK candidates[idx] (stay at same index)
         * or SKIP it (advance to next index). No duplicates possible because
         * we never revisit earlier indices.
         */
        private void backtrack(int[] candidates, int idx,
                               List<Integer> current, int remaining) {
            if (remaining == 0) {
                results.add(new ArrayList<>(current));
                return;
            }
            if (idx == candidates.length || remaining < 0) return;

            // Pick
            current.add(candidates[idx]);
            backtrack(candidates, idx, current, remaining - candidates[idx]);
            current.remove(current.size() - 1);

            // Skip
            backtrack(candidates, idx + 1, current, remaining);
        }

        public List<List<Integer>> combinationSum(int[] candidates, int target) {
            Arrays.sort(candidates);
            results = new ArrayList<>();
            backtrack(candidates, 0, new ArrayList<>(), target);
            return results;
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Combination Sum ===");
        int[] candidates = {2, 3, 6, 7};
        int target = 7;

        System.out.println("Brute:   " + new BruteForce().combinationSum(candidates.clone(), target));
        System.out.println("Optimal: " + new Optimal().combinationSum(candidates.clone(), target));
        System.out.println("Best:    " + new Best().combinationSum(candidates.clone(), target));

        int[] c2 = {2, 3, 5};
        int t2 = 8;
        System.out.println("\nBrute:   " + new BruteForce().combinationSum(c2.clone(), t2));
        System.out.println("Optimal: " + new Optimal().combinationSum(c2.clone(), t2));
        System.out.println("Best:    " + new Best().combinationSum(c2.clone(), t2));
    }
}
