import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(2^n * n)  |  Space: O(2^n * n)
// Generate all subsets; filter those that sum to target;
// deduplicate using a Set. Less efficient due to Set overhead.
// ============================================================
class BruteForce {
    public static List<List<Integer>> solve(int[] candidates, int target) {
        Arrays.sort(candidates);
        Set<List<Integer>> resultSet = new HashSet<>();
        List<Integer> current = new ArrayList<>();
        generateSubsets(candidates, 0, target, current, resultSet);
        return new ArrayList<>(resultSet);
    }

    private static void generateSubsets(int[] candidates, int start, int remaining,
                                         List<Integer> current, Set<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remaining) break;
            current.add(candidates[i]);
            generateSubsets(candidates, i + 1, remaining - candidates[i], current, result);
            current.remove(current.size() - 1);
        }
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(2^n)  |  Space: O(n) recursion stack
// Sort + backtracking with duplicate skipping at each level.
// Skip candidates[i] == candidates[i-1] at the same recursion level
// to avoid duplicate combinations.
// ============================================================
class Optimal {
    public static List<List<Integer>> solve(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<>();
        backtrack(candidates, 0, target, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(int[] candidates, int start, int remaining,
                                   List<Integer> current, List<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remaining) break;
            // Skip duplicates at the same level
            if (i > start && candidates[i] == candidates[i - 1]) continue;
            current.add(candidates[i]);
            backtrack(candidates, i + 1, remaining - candidates[i], current, result);
            current.remove(current.size() - 1);
        }
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(2^n)  |  Space: O(n)
// Same as Optimal. Both approaches are the canonical solution;
// this version uses explicit include/exclude branching for clarity.
// ============================================================
class Best {
    public static List<List<Integer>> solve(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<>();
        backtrack(candidates, 0, target, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(int[] cands, int idx, int rem,
                                   List<Integer> path, List<List<Integer>> result) {
        if (rem == 0) { result.add(new ArrayList<>(path)); return; }
        if (idx == cands.length || cands[idx] > rem) return;

        // Include cands[idx]
        path.add(cands[idx]);
        backtrack(cands, idx + 1, rem - cands[idx], path, result);
        path.remove(path.size() - 1);

        // Skip all duplicates of cands[idx] before moving to next distinct
        int next = idx + 1;
        while (next < cands.length && cands[next] == cands[idx]) next++;
        backtrack(cands, next, rem, path, result);
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Combination Sum II ===");

        int[] c1 = {10, 1, 2, 7, 6, 1, 5};
        int t1 = 8;
        System.out.println("candidates=" + Arrays.toString(c1) + ", target=" + t1);
        System.out.println("Optimal: " + Optimal.solve(c1, t1));
        System.out.println("Best:    " + Best.solve(c1, t1));
        System.out.println("Expected: [[1,1,6],[1,2,5],[1,7],[2,6]]");

        int[] c2 = {2, 5, 2, 1, 2};
        int t2 = 5;
        System.out.println("\ncandidates=" + Arrays.toString(c2) + ", target=" + t2);
        System.out.println("Optimal: " + Optimal.solve(c2, t2));
        System.out.println("Best:    " + Best.solve(c2, t2));
        System.out.println("Expected: [[1,2,2],[5]]");

        // All duplicates
        int[] c3 = {1, 1, 1, 1};
        int t3 = 2;
        System.out.println("\ncandidates=" + Arrays.toString(c3) + ", target=" + t3);
        System.out.println("Optimal: " + Optimal.solve(c3, t3));
        System.out.println("Expected: [[1,1]]");
    }
}
