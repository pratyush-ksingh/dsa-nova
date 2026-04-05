import java.util.*;

/**
 * Problem: Longest Bitonic Subsequence
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Try all subsequences
    // Time: O(2^n * n)  |  Space: O(n)
    // ============================================================
    public static int bruteForce(int[] nums) {
        int n = nums.length;
        int[] best = {1};

        backtrack(nums, n, 0, new ArrayList<>(), best);
        return best[0];
    }

    private static void backtrack(int[] nums, int n, int idx,
                                   List<Integer> current, int[] best) {
        if (current.size() >= 3 && isBitonic(current)) {
            best[0] = Math.max(best[0], current.size());
        }
        for (int i = idx; i < n; i++) {
            current.add(nums[i]);
            backtrack(nums, n, i + 1, current, best);
            current.remove(current.size() - 1);
        }
    }

    private static boolean isBitonic(List<Integer> seq) {
        int i = 0;
        while (i + 1 < seq.size() && seq.get(i) < seq.get(i + 1)) i++;
        if (i == 0 || i == seq.size() - 1) return false;
        while (i + 1 < seq.size() && seq.get(i) > seq.get(i + 1)) i++;
        return i == seq.size() - 1;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- LIS from left + LIS from right
    // Time: O(n^2)  |  Space: O(n)
    // lis[i] = LIS length ending at i; lds[i] = LIS starting at i.
    // Answer = max(lis[i] + lds[i] - 1) where both > 1.
    // ============================================================
    public static int optimal(int[] nums) {
        int n = nums.length;
        int[] lis = new int[n];
        int[] lds = new int[n];
        Arrays.fill(lis, 1);
        Arrays.fill(lds, 1);

        // LIS ending at each index
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    lis[i] = Math.max(lis[i], lis[j] + 1);
                }
            }
        }

        // LDS starting at each index (= LIS from right)
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (nums[j] < nums[i]) {
                    lds[i] = Math.max(lds[i], lds[j] + 1);
                }
            }
        }

        int ans = 1;
        for (int i = 0; i < n; i++) {
            if (lis[i] > 1 && lds[i] > 1) {
                ans = Math.max(ans, lis[i] + lds[i] - 1);
            }
        }
        return ans;
    }

    // ============================================================
    // APPROACH 3: BEST -- Same O(n^2) written concisely
    // Time: O(n^2)  |  Space: O(n)
    // (O(n log n) via patience sorting for LIS is possible but
    // reconstruction is complex -- O(n^2) is the interview standard.)
    // ============================================================
    public static int best(int[] nums) {
        return optimal(nums); // identical algorithm
    }

    public static void main(String[] args) {
        System.out.println("=== Longest Bitonic Subsequence ===\n");

        int[] nums1 = {1, 11, 2, 10, 4, 5, 2, 1};
        System.out.println("Brute:   " + bruteForce(nums1));   // 6
        System.out.println("Optimal: " + optimal(nums1));       // 6
        System.out.println("Best:    " + best(nums1));          // 6

        int[] nums2 = {12, 11, 40, 5, 3, 1};
        System.out.println("\nBrute:   " + bruteForce(nums2));  // 5
        System.out.println("Optimal: " + optimal(nums2));       // 5
        System.out.println("Best:    " + best(nums2));          // 5

        int[] nums3 = {80, 60, 30};
        System.out.println("\nPure decreasing: " + best(nums3));  // 1

        int[] nums4 = {1, 2, 3, 4, 5};
        System.out.println("Pure increasing: " + best(nums4));    // 1
    }
}
