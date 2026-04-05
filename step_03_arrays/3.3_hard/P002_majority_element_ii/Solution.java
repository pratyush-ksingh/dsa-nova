/**
 * Problem: Majority Element II (LeetCode #229)
 * Difficulty: HARD | XP: 40
 *
 * Given an integer array of size n, find all elements that appear more than
 * floor(n/3) times. There can be at most 2 such elements.
 *
 * @author DSA_Nova
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// ============================================================
// Approach 1: Brute Force (Count each element)
// Time: O(n^2) | Space: O(n) for seen set
// ============================================================
class BruteForce {
    public static List<Integer> majorityElement(int[] nums) {
        int n = nums.length;
        List<Integer> result = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();

        for (int i = 0; i < n; i++) {
            if (seen.contains(nums[i])) continue;
            seen.add(nums[i]);
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (nums[j] == nums[i]) count++;
            }
            if (count > n / 3) result.add(nums[i]);
        }
        Collections.sort(result);
        return result;
    }
}

// ============================================================
// Approach 2: Optimal (Boyer-Moore Extended Voting - 2 candidates)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static List<Integer> majorityElement(int[] nums) {
        int n = nums.length;
        int cand1 = Integer.MIN_VALUE, cand2 = Integer.MIN_VALUE;
        int cnt1 = 0, cnt2 = 0;

        // Phase 1: Candidate selection
        for (int num : nums) {
            if (num == cand1) {
                cnt1++;
            } else if (num == cand2) {
                cnt2++;
            } else if (cnt1 == 0) {
                cand1 = num; cnt1 = 1;
            } else if (cnt2 == 0) {
                cand2 = num; cnt2 = 1;
            } else {
                cnt1--;
                cnt2--;
            }
        }

        // Phase 2: Verify by counting actual frequencies
        cnt1 = 0; cnt2 = 0;
        for (int num : nums) {
            if (num == cand1) cnt1++;
            else if (num == cand2) cnt2++;
        }

        List<Integer> result = new ArrayList<>();
        if (cnt1 > n / 3) result.add(cand1);
        if (cnt2 > n / 3) result.add(cand2);
        Collections.sort(result);
        return result;
    }
}

// ============================================================
// Approach 3: Best (Same Boyer-Moore, cleanest form)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static List<Integer> majorityElement(int[] nums) {
        int n = nums.length;
        // Use boxed type to allow null as "no candidate yet"
        Integer cand1 = null, cand2 = null;
        int cnt1 = 0, cnt2 = 0;

        for (int num : nums) {
            if (cand1 != null && num == cand1) {
                cnt1++;
            } else if (cand2 != null && num == cand2) {
                cnt2++;
            } else if (cnt1 == 0) {
                cand1 = num; cnt1 = 1;
            } else if (cnt2 == 0) {
                cand2 = num; cnt2 = 1;
            } else {
                cnt1--;
                cnt2--;
            }
        }

        // Verification pass
        cnt1 = 0; cnt2 = 0;
        for (int num : nums) {
            if (cand1 != null && num == cand1) cnt1++;
            else if (cand2 != null && num == cand2) cnt2++;
        }

        List<Integer> result = new ArrayList<>();
        if (cnt1 > n / 3) result.add(cand1);
        if (cnt2 > n / 3) result.add(cand2);
        Collections.sort(result);
        return result;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Majority Element II ===\n");

        int[][] inputs = {
            {3, 2, 3},
            {1},
            {1, 2},
            {1, 1, 1, 3, 3, 2, 2, 2},
            {1, 2, 3, 4},
            {0, 0, 0},
            {1, 1, 2, 2, 3, 3, 4}
        };
        int[][] expected = {
            {3},
            {1},
            {1, 2},
            {1, 2},
            {},
            {0},
            {}
        };

        for (int t = 0; t < inputs.length; t++) {
            List<Integer> b = BruteForce.majorityElement(inputs[t]);
            List<Integer> o = Optimal.majorityElement(inputs[t]);
            List<Integer> n = Best.majorityElement(inputs[t]);

            // Convert expected to list for comparison
            List<Integer> exp = new ArrayList<>();
            for (int e : expected[t]) exp.add(e);
            Collections.sort(exp);

            boolean pass = b.equals(exp) && o.equals(exp) && n.equals(exp);
            System.out.println("Input:    " + Arrays.toString(inputs[t]));
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + n);
            System.out.println("Expected: " + exp + "  [" + (pass ? "PASS" : "FAIL") + "]");
            System.out.println();
        }
    }
}
