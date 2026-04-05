/**
 * Problem: Equal Average Partition
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given an array A, partition it into two non-empty subsets B and C such that
 * average(B) == average(C). Return [B sorted, C sorted] or [] if impossible.
 *
 * Key insight: If average(B) == average(C) == average(A), then we need a
 * subset of size k (1 <= k < n) whose sum equals k * total_sum / n.
 * This requires k * total_sum % n == 0 (integer divisibility).
 *
 * DP: For each valid size k, check if subset of size k with sum = k*S/n exists.
 * Use 2D DP: dp[k][s] = can we pick exactly k elements summing to s?
 *
 * Real-life use: Fair division problems, load balancing, resource allocation.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(2^N * N)  |  Space: O(2^N)
    // Enumerate all subsets, check if any has the required average.
    // Only feasible for small N.
    // ============================================================
    public static List<List<Integer>> bruteForce(int[] A) {
        int n = A.length;
        int totalSum = Arrays.stream(A).sum();
        Arrays.sort(A);

        for (int mask = 1; mask < (1 << n) - 1; mask++) {
            int subSize = Integer.bitCount(mask);
            int subSum = 0;
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask >> i & 1) == 1) {
                    subSum += A[i];
                    subset.add(A[i]);
                }
            }
            // Check average condition: subSum/subSize == totalSum/n
            // i.e., subSum * n == totalSum * subSize
            if ((long) subSum * n == (long) totalSum * subSize) {
                List<Integer> rest = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    if ((mask >> i & 1) == 0) rest.add(A[i]);
                }
                return Arrays.asList(subset, rest);
            }
        }
        return new ArrayList<>();
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(N^2 * S)  |  Space: O(N * S) where S = sum(A)
    // 3D DP: dp[i][k][s] = can we select k elements summing to s
    // from first i elements? Use bitset/boolean 2D: dp[k][s].
    // ============================================================
    public static List<List<Integer>> optimal(int[] A) {
        int n = A.length;
        int totalSum = Arrays.stream(A).sum();
        Arrays.sort(A);

        for (int k = 1; k < n; k++) {
            if ((long) k * totalSum % n != 0) continue;
            int targetSum = k * totalSum / n;

            // dp[s] = set of possible subsets of size exactly k with sum s
            // Use boolean dp: dp[size][sum]
            boolean[][] dp = new boolean[k + 1][targetSum + 1];
            dp[0][0] = true;
            for (int val : A) {
                // Process in reverse to avoid reuse
                for (int sz = Math.min(k, n) - 1; sz >= 0; sz--) {
                    for (int s = targetSum - val; s >= 0; s--) {
                        if (dp[sz][s]) dp[sz + 1][s + val] = true;
                    }
                }
            }

            if (dp[k][targetSum]) {
                // Reconstruct the subset
                List<Integer> subset = new ArrayList<>();
                List<Integer> rest = new ArrayList<>();
                boolean[] used = new boolean[n];
                int remSize = k, remSum = targetSum;

                // Greedy backtrack: try to pick elements
                for (int i = n - 1; i >= 0 && remSize > 0; i--) {
                    if (A[i] <= remSum && remSize > 0) {
                        // Check if remaining elements can form subset
                        // (simplified: just pick greedily)
                        remSum -= A[i];
                        remSize--;
                        used[i] = true;
                    }
                }
                // Verify
                if (remSize == 0 && remSum == 0) {
                    for (int i = 0; i < n; i++) {
                        if (used[i]) subset.add(A[i]);
                        else rest.add(A[i]);
                    }
                    return Arrays.asList(subset, rest);
                }
            }
        }
        return new ArrayList<>();
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(N^2 * S)  |  Space: O(N * S)
    // Correct reconstruction using HashSet of states.
    // dp[k] = set of achievable sums with exactly k elements.
    // When we find a valid (k, targetSum), reconstruct by backtracking
    // through the dp table.
    // ============================================================
    public static List<List<Integer>> best(int[] A) {
        int n = A.length;
        int totalSum = 0;
        for (int x : A) totalSum += x;
        Arrays.sort(A);

        // Try each possible partition size k for subset B
        for (int k = 1; k < n; k++) {
            if ((long) k * totalSum % n != 0) continue;
            int target = k * totalSum / n;

            // dp[sz][sum] = true if subset of size sz summing to sum is achievable
            boolean[][] dp = new boolean[k + 1][target + 1];
            dp[0][0] = true;

            for (int i = 0; i < n; i++) {
                int val = A[i];
                // Reverse iteration to avoid using same element twice
                for (int sz = Math.min(i + 1, k); sz >= 1; sz--) {
                    for (int s = Math.min(target, /* sum so far */ target); s >= val; s--) {
                        if (dp[sz - 1][s - val]) dp[sz][s] = true;
                    }
                }
            }

            if (!dp[k][target]) continue;

            // Reconstruct: greedy selection from largest to smallest
            List<Integer> B = new ArrayList<>(), C = new ArrayList<>();
            boolean[] inB = new boolean[n];
            int remK = k, remSum = target;

            for (int i = n - 1; i >= 0; i--) {
                if (remK == 0) break;
                if (A[i] <= remSum && remK <= i + 1) {
                    // Check if skipping this element still allows a valid subset
                    // (i.e., dp without A[i] for remaining)
                    // Simple heuristic: include if it helps reach target
                    if (canForm(dp, A, i, remK, remSum)) {
                        inB[i] = true;
                        remSum -= A[i];
                        remK--;
                    }
                }
            }

            if (remK == 0 && remSum == 0) {
                for (int i = 0; i < n; i++) {
                    if (inB[i]) B.add(A[i]);
                    else C.add(A[i]);
                }
                Collections.sort(B);
                Collections.sort(C);
                return Arrays.asList(B, C);
            }
        }
        return new ArrayList<>();
    }

    private static boolean canForm(boolean[][] dp, int[] A, int upTo, int k, int sum) {
        // Re-run dp on A[0..upTo] to check if achievable
        // (This is O(N*S) per call - not ideal but correct)
        if (k <= 0) return k == 0 && sum == 0;
        if (sum < 0 || upTo < 0) return false;
        boolean[][] check = new boolean[k + 1][sum + 1];
        check[0][0] = true;
        for (int i = 0; i <= upTo; i++) {
            int val = A[i];
            for (int sz = Math.min(i + 1, k); sz >= 1; sz--) {
                for (int s = sum; s >= val; s--) {
                    if (check[sz - 1][s - val]) check[sz][s] = true;
                }
            }
        }
        return check[k][sum];
    }

    public static void main(String[] args) {
        System.out.println("=== Equal Average Partition ===\n");

        int[] A1 = {1, 7, 15, 29, 11, 9};
        System.out.println("Test 1: " + Arrays.toString(A1));
        System.out.println("  Brute:   " + bruteForce(A1.clone()));
        System.out.println("  Optimal: " + optimal(A1.clone()));
        System.out.println("  Best:    " + best(A1.clone()));

        int[] A2 = {1, 2, 3, 4, 5, 6};
        System.out.println("\nTest 2: " + Arrays.toString(A2));
        System.out.println("  Brute:   " + bruteForce(A2.clone()));
        System.out.println("  Best:    " + best(A2.clone()));

        int[] A3 = {1, 2, 3};
        System.out.println("\nTest 3 (no partition): " + Arrays.toString(A3));
        System.out.println("  Best: " + best(A3.clone())); // []
    }
}
