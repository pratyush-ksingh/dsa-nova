import java.util.*;

/**
 * Greedy Algorithm to Find Minimum Coins
 *
 * Indian denomination system: {1, 2, 5, 10, 20, 50, 100, 500, 2000}
 * Find minimum coins/notes for a given amount.
 */
public class Solution {

    static final int[] DENOMINATIONS = {2000, 500, 100, 50, 20, 10, 5, 2, 1};

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursive enumeration
    // Time: O(V^D) worst  |  Space: O(D) recursion depth
    // ============================================================
    public static List<Integer> bruteForce(int V) {
        List<Integer> result = new ArrayList<>();
        if (V == 0) return result;
        List<Integer> best = bruteHelper(V, 0);
        return best != null ? best : result;
    }

    private static List<Integer> bruteHelper(int remaining, int idx) {
        if (remaining == 0) return new ArrayList<>();
        if (idx >= DENOMINATIONS.length) return null;

        int denom = DENOMINATIONS[idx];
        int maxCount = remaining / denom;
        List<Integer> best = null;

        for (int count = maxCount; count >= 0; count--) {
            List<Integer> sub = bruteHelper(remaining - count * denom, idx + 1);
            if (sub != null) {
                List<Integer> candidate = new ArrayList<>();
                for (int i = 0; i < count; i++) candidate.add(denom);
                candidate.addAll(sub);
                if (best == null || candidate.size() < best.size()) {
                    best = candidate;
                }
            }
        }
        return best;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DP (general coin change)
    // Time: O(V * D)  |  Space: O(V)
    // ============================================================
    public static List<Integer> optimal(int V) {
        List<Integer> result = new ArrayList<>();
        if (V == 0) return result;

        int[] dp = new int[V + 1];
        int[] parent = new int[V + 1]; // which coin was used
        Arrays.fill(dp, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dp[0] = 0;

        for (int i = 1; i <= V; i++) {
            for (int d : DENOMINATIONS) {
                if (d <= i && dp[i - d] != Integer.MAX_VALUE && dp[i - d] + 1 < dp[i]) {
                    dp[i] = dp[i - d] + 1;
                    parent[i] = d;
                }
            }
        }

        // Reconstruct solution
        int amount = V;
        while (amount > 0 && parent[amount] != -1) {
            result.add(parent[amount]);
            amount -= parent[amount];
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Greedy largest-first
    // Time: O(D)  |  Space: O(D) for result
    // ============================================================
    public static List<Integer> best(int V) {
        List<Integer> result = new ArrayList<>();

        for (int denom : DENOMINATIONS) {
            while (V >= denom) {
                result.add(denom);
                V -= denom;
            }
        }

        return result;
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        int[] testAmounts = {49, 93, 2000, 1, 0};

        System.out.println("=== Minimum Coins (Indian Denominations) ===");
        for (int V : testAmounts) {
            List<Integer> res = best(V);
            System.out.printf("V=%4d -> %d coins: %s%n", V, res.size(), res);
        }
        // V=  49 -> 5 coins: [20, 20, 5, 2, 2]
        // V=  93 -> 5 coins: [50, 20, 20, 2, 1]
        // V=2000 -> 1 coins: [2000]
        // V=   1 -> 1 coins: [1]
        // V=   0 -> 0 coins: []

        System.out.println("\n--- Verification (all approaches for V=49) ---");
        System.out.println("Brute:   " + bruteForce(49));
        System.out.println("Optimal: " + optimal(49));
        System.out.println("Best:    " + best(49));
    }
}
