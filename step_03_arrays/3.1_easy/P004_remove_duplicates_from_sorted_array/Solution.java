/**
 * Problem: Remove Duplicates from Sorted Array (LeetCode #26)
 * Difficulty: EASY | XP: 10
 *
 * Remove duplicates in-place from a sorted array. Return new length.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;
import java.util.LinkedHashSet;

// ============================================================
// Approach 1: Brute Force (Extra Space with LinkedHashSet)
// Time: O(n) | Space: O(n)
// ============================================================
class BruteForce {
    public static int removeDuplicates(int[] nums) {
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int i = 0;
        for (int num : set) {
            nums[i++] = num;
        }
        return set.size();
    }
}

// ============================================================
// Approach 2: Optimal (Two Pointers In-Place)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int w = 1; // write pointer; first element always stays
        for (int r = 1; r < nums.length; r++) {
            if (nums[r] != nums[r - 1]) {
                nums[w] = nums[r];
                w++;
            }
        }
        return w;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Remove Duplicates from Sorted Array ===\n");

        int[][] testCases = {
            {1, 1, 2},
            {0, 0, 1, 1, 1, 2, 2, 3, 3, 4},
            {1},
            {3, 3, 3, 3},
            {1, 2, 3, 4},
            {-1, -1, 0, 0, 1}
        };
        int[] expected = {2, 5, 1, 1, 4, 3};

        for (int t = 0; t < testCases.length; t++) {
            // Test brute force on a clone
            int[] arr1 = testCases[t].clone();
            int bruteK = BruteForce.removeDuplicates(arr1);

            // Test optimal on a clone
            int[] arr2 = testCases[t].clone();
            int optimalK = Optimal.removeDuplicates(arr2);

            System.out.println("Input:    " + Arrays.toString(testCases[t]));
            System.out.println("  Brute:    k=" + bruteK + ", arr=" + Arrays.toString(Arrays.copyOf(arr1, bruteK)));
            System.out.println("  Optimal:  k=" + optimalK + ", arr=" + Arrays.toString(Arrays.copyOf(arr2, optimalK)));
            System.out.println("  Expected: k=" + expected[t]);
            System.out.println("  Pass:     " + (bruteK == expected[t] && optimalK == expected[t]));
            System.out.println();
        }
    }
}
