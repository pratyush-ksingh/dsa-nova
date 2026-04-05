/**
 * Problem: Sort Array of 0s 1s 2s (LeetCode #75)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array containing only 0s, 1s, and 2s, sort it in-place in ascending
 * order without using any library sort function.
 * Also known as the Dutch National Flag problem.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Count and overwrite)
// Time: O(2n) | Space: O(1)
// ============================================================
class BruteForce {
    public static void sortColors(int[] nums) {
        int[] count = new int[3];
        for (int x : nums) count[x]++;

        int idx = 0;
        for (int val = 0; val < 3; val++) {
            for (int i = 0; i < count[val]; i++) {
                nums[idx++] = val;
            }
        }
    }
}

// ============================================================
// Approach 2: Optimal (Dutch National Flag - three pointers)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;

        while (mid <= high) {
            if (nums[mid] == 0) {
                int tmp = nums[low]; nums[low] = nums[mid]; nums[mid] = tmp;
                low++;
                mid++;
            } else if (nums[mid] == 1) {
                mid++;
            } else { // nums[mid] == 2
                int tmp = nums[mid]; nums[mid] = nums[high]; nums[high] = tmp;
                high--;
                // Do NOT increment mid
            }
        }
    }
}

// ============================================================
// Approach 3: Best (Dutch National Flag - most readable form)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static void sortColors(int[] nums) {
        int lo = 0, mid = 0, hi = nums.length - 1;

        while (mid <= hi) {
            switch (nums[mid]) {
                case 0:
                    // Place 0 into left partition
                    int t0 = nums[lo]; nums[lo] = nums[mid]; nums[mid] = t0;
                    lo++;
                    mid++;
                    break;
                case 1:
                    // 1 is already in correct position
                    mid++;
                    break;
                default: // case 2
                    // Place 2 into right partition
                    int t2 = nums[mid]; nums[mid] = nums[hi]; nums[hi] = t2;
                    hi--;
                    // mid stays: swapped element is unknown
                    break;
            }
        }
    }
}

// Main driver
public class Solution {
    private static int[] copy(int[] a) { return Arrays.copyOf(a, a.length); }

    public static void main(String[] args) {
        System.out.println("=== Sort Array of 0s 1s 2s ===\n");

        int[][] inputs = {
            {2, 0, 2, 1, 1, 0},
            {2, 0, 1},
            {0},
            {1, 1, 1},
            {0, 0, 0},
            {2, 2, 2},
            {1, 0, 2, 1, 0, 2}
        };
        int[][] expected = {
            {0, 0, 1, 1, 2, 2},
            {0, 1, 2},
            {0},
            {1, 1, 1},
            {0, 0, 0},
            {2, 2, 2},
            {0, 0, 1, 1, 2, 2}
        };

        for (int t = 0; t < inputs.length; t++) {
            int[] b = copy(inputs[t]); BruteForce.sortColors(b);
            int[] o = copy(inputs[t]); Optimal.sortColors(o);
            int[] n = copy(inputs[t]); Best.sortColors(n);

            boolean pass = Arrays.equals(b, expected[t])
                        && Arrays.equals(o, expected[t])
                        && Arrays.equals(n, expected[t]);

            System.out.println("Input:    " + Arrays.toString(inputs[t]));
            System.out.println("Brute:    " + Arrays.toString(b));
            System.out.println("Optimal:  " + Arrays.toString(o));
            System.out.println("Best:     " + Arrays.toString(n));
            System.out.println("Expected: " + Arrays.toString(expected[t])
                             + "  [" + (pass ? "PASS" : "FAIL") + "]");
            System.out.println();
        }
    }
}
