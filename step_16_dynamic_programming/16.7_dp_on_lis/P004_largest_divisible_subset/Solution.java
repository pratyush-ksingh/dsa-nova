/**
 * Problem: Largest Divisible Subset (LeetCode #368)
 * Difficulty: MEDIUM | XP: 25
 *
 * Find largest subset where every pair (a,b) satisfies a%b==0 or b%a==0.
 * Sort + LIS variant with divisibility condition.
 * All 4 DP approaches.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Plain Recursion (Generate all subsets)
// Time: O(2^n * n) | Space: O(n)
// ============================================================
class Recursive {
    private List<Integer> best;

    public List<Integer> largestDivisibleSubset(int[] nums) {
        Arrays.sort(nums);
        best = new ArrayList<>();
        solve(nums, 0, -1, new ArrayList<>());
        return best;
    }

    private void solve(int[] nums, int idx, int prevIdx, List<Integer> current) {
        if (current.size() > best.size()) {
            best = new ArrayList<>(current);
        }
        if (idx == nums.length) return;

        // Not take
        solve(nums, idx + 1, prevIdx, current);

        // Take: if divisible by previous element (or first element)
        if (prevIdx == -1 || nums[idx] % nums[prevIdx] == 0) {
            current.add(nums[idx]);
            solve(nums, idx + 1, idx, current);
            current.remove(current.size() - 1);
        }
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n^2) | Space: O(n^2)
// ============================================================
class Memoization {
    public List<Integer> largestDivisibleSubset(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int[][] dp = new int[n][n + 1];
        for (int[] row : dp) Arrays.fill(row, -1);

        // Find the length first
        int maxLen = 0;
        int startIdx = -1;
        int startPrev = -1;

        // We'll use tabulation for reconstruction (more practical)
        // Memo is shown for the length computation
        for (int i = 0; i < n; i++) {
            int len = solve(nums, i, -1, dp);
            if (len > maxLen) {
                maxLen = len;
            }
        }

        // For actual reconstruction, use tabulation approach
        return tabulationReconstruct(nums);
    }

    private int solve(int[] nums, int idx, int prevIdx, int[][] dp) {
        if (idx == nums.length) return 0;
        if (dp[idx][prevIdx + 1] != -1) return dp[idx][prevIdx + 1];

        int notTake = solve(nums, idx + 1, prevIdx, dp);
        int take = 0;
        if (prevIdx == -1 || nums[idx] % nums[prevIdx] == 0) {
            take = 1 + solve(nums, idx + 1, idx, dp);
        }

        dp[idx][prevIdx + 1] = Math.max(take, notTake);
        return dp[idx][prevIdx + 1];
    }

    private List<Integer> tabulationReconstruct(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dp, 1);
        Arrays.fill(parent, -1);

        int maxLen = 1, maxIdx = 0;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] % nums[j] == 0 && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                maxIdx = i;
            }
        }

        List<Integer> result = new ArrayList<>();
        int idx = maxIdx;
        while (idx != -1) {
            result.add(nums[idx]);
            idx = parent[idx];
        }
        Collections.reverse(result);
        return result;
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP -- LIS Style)
// Time: O(n^2) | Space: O(n)
// ============================================================
class Tabulation {
    public List<Integer> largestDivisibleSubset(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;

        int[] dp = new int[n];     // dp[i] = size of largest divisible subset ending at i
        int[] parent = new int[n]; // parent[i] = previous index in the subset
        Arrays.fill(dp, 1);
        Arrays.fill(parent, -1);

        int maxLen = 1, maxIdx = 0;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] % nums[j] == 0 && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                maxIdx = i;
            }
        }

        // Reconstruct the subset by following parent pointers
        List<Integer> result = new ArrayList<>();
        int idx = maxIdx;
        while (idx != -1) {
            result.add(nums[idx]);
            idx = parent[idx];
        }
        Collections.reverse(result);
        return result;
    }
}

// ============================================================
// Approach 4: Optimized Tabulation (same complexity, cleaner)
// Time: O(n^2) | Space: O(n)
// ============================================================
class SpaceOptimized {
    /**
     * Same O(n^2) approach but with early termination and
     * cleaner reconstruction.
     *
     * O(n log n) is NOT possible for this problem because
     * divisibility doesn't maintain the sorted-tails property
     * needed for binary search LIS.
     */
    public List<Integer> largestDivisibleSubset(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        if (n == 0) return new ArrayList<>();

        int[] dp = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dp, 1);
        Arrays.fill(parent, -1);

        int maxLen = 1, maxIdx = 0;

        for (int i = 1; i < n; i++) {
            for (int j = i - 1; j >= 0; j--) {
                // Only check if nums[j] divides nums[i]
                if (nums[i] % nums[j] == 0) {
                    if (dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                        parent[i] = j;
                    }
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                maxIdx = i;
            }
        }

        List<Integer> result = new ArrayList<>();
        int idx = maxIdx;
        while (idx != -1) {
            result.add(nums[idx]);
            idx = parent[idx];
        }
        Collections.reverse(result);
        return result;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Largest Divisible Subset (LeetCode #368) ===\n");

        Recursive rec = new Recursive();
        Tabulation tab = new Tabulation();
        SpaceOptimized space = new SpaceOptimized();

        int[][] tests = {
            {1, 2, 3},
            {1, 2, 4, 8},
            {3, 4, 16, 8},
            {1},
            {2, 3, 5, 7, 11},
            {4, 8, 10, 240},
            {1, 2, 4, 8, 16, 32},
        };
        int[] expectedSizes = {2, 4, 3, 1, 1, 3, 6};

        System.out.printf("%-25s %-8s %-25s %-25s %-6s%n",
                "nums", "ExpSize", "Tab Result", "Space Result", "Pass");
        System.out.println("-".repeat(100));

        for (int t = 0; t < tests.length; t++) {
            List<Integer> recRes = rec.largestDivisibleSubset(tests[t].clone());
            List<Integer> tabRes = tab.largestDivisibleSubset(tests[t].clone());
            List<Integer> spaceRes = space.largestDivisibleSubset(tests[t].clone());

            boolean pass = (recRes.size() == expectedSizes[t]) &&
                           (tabRes.size() == expectedSizes[t]) &&
                           (spaceRes.size() == expectedSizes[t]);

            System.out.printf("%-25s %-8d %-25s %-25s %-6s%n",
                    Arrays.toString(tests[t]), expectedSizes[t],
                    tabRes.toString(), spaceRes.toString(),
                    pass ? "PASS" : "FAIL");
        }

        // Verify divisibility of results
        System.out.println("\n--- Verification ---");
        List<Integer> result = tab.largestDivisibleSubset(new int[]{1, 2, 4, 8, 16, 32});
        System.out.println("Subset: " + result);
        boolean valid = true;
        for (int i = 0; i < result.size() && valid; i++) {
            for (int j = i + 1; j < result.size(); j++) {
                int a = result.get(i), b = result.get(j);
                if (a % b != 0 && b % a != 0) {
                    valid = false;
                    System.out.println("INVALID pair: " + a + ", " + b);
                }
            }
        }
        System.out.println("All pairs divisible: " + valid);

        // Show dp array
        System.out.println("\n--- DP Table: [3, 4, 16, 8] -> sorted [3, 4, 8, 16] ---");
        int[] sorted = {3, 4, 8, 16};
        System.out.println("i=0 (3):  dp=1, parent=-1");
        System.out.println("i=1 (4):  4%3!=0 -> dp=1, parent=-1");
        System.out.println("i=2 (8):  8%3!=0, 8%4=0 -> dp=2, parent=1");
        System.out.println("i=3 (16): 16%3!=0, 16%4=0(dp=2), 16%8=0(dp=3) -> dp=3, parent=2");
        System.out.println("Backtrack from 3: [16] -> [8,16] -> [4,8,16]");
    }
}
