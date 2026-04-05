/**
 * Problem: Kth Permutation Sequence
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given n and k, find the kth permutation of digits 1..n in lexicographic order.
 * Permutations are 1-indexed.
 *
 * @author DSA_Nova
 */
import java.util.ArrayList;
import java.util.List;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Generate all permutations, return kth
    // Time: O(N! * N)  |  Space: O(N!)
    // ============================================================
    private static List<String> allPerms = new ArrayList<>();

    public static String bruteForce(int n, int k) {
        allPerms.clear();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) nums[i] = i + 1;
        generatePerms(nums, 0);
        return allPerms.get(k - 1);
    }

    private static void generatePerms(int[] nums, int start) {
        if (start == nums.length) {
            StringBuilder sb = new StringBuilder();
            for (int x : nums) sb.append(x);
            allPerms.add(sb.toString());
            return;
        }
        for (int i = start; i < nums.length; i++) {
            int tmp = nums[start]; nums[start] = nums[i]; nums[i] = tmp;
            generatePerms(nums, start + 1);
            tmp = nums[start]; nums[start] = nums[i]; nums[i] = tmp;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Factorial Number System
    // Time: O(N^2)  |  Space: O(N)
    // k -= 1 (0-indexed). At each step: idx = k / (n-1)!, k %= (n-1)!
    // Pick idx-th remaining digit from available list.
    // ============================================================
    public static String optimal(int n, int k) {
        int[] fact = new int[n];
        fact[0] = 1;
        for (int i = 1; i < n; i++) fact[i] = fact[i - 1] * i;

        List<Integer> digits = new ArrayList<>();
        for (int i = 1; i <= n; i++) digits.add(i);

        k--; // 0-indexed
        StringBuilder sb = new StringBuilder();
        for (int i = n; i >= 1; i--) {
            int idx = k / fact[i - 1];
            sb.append(digits.get(idx));
            digits.remove(idx);
            k %= fact[i - 1];
        }
        return sb.toString();
    }

    // ============================================================
    // APPROACH 3: BEST - Factorial system with boolean used[] array
    // Time: O(N^2)  |  Space: O(N)
    // Avoids ArrayList removal cost; instead scans used[] to find kth free digit.
    // ============================================================
    public static String best(int n, int k) {
        int[] fact = new int[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) fact[i] = fact[i - 1] * i;

        boolean[] used = new boolean[n + 1];
        k--; // 0-indexed
        StringBuilder sb = new StringBuilder();

        for (int i = n; i >= 1; i--) {
            int cnt = k / fact[i - 1];
            k %= fact[i - 1];
            int found = 0;
            for (int num = 1; num <= n; num++) {
                if (!used[num]) {
                    if (found == cnt) {
                        sb.append(num);
                        used[num] = true;
                        break;
                    }
                    found++;
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Kth Permutation Sequence ===");
        // n=3: [123,132,213,231,312,321]
        System.out.println("n=3,k=3 brute=" + bruteForce(3, 3) + " opt=" + optimal(3, 3) + " best=" + best(3, 3)); // 213
        System.out.println("n=3,k=6 brute=" + bruteForce(3, 6) + " opt=" + optimal(3, 6) + " best=" + best(3, 6)); // 321
        System.out.println("n=4,k=9 brute=" + bruteForce(4, 9) + " opt=" + optimal(4, 9) + " best=" + best(4, 9)); // 2314
        System.out.println("n=1,k=1 brute=" + bruteForce(1, 1) + " opt=" + optimal(1, 1) + " best=" + best(1, 1)); // 1
    }
}
