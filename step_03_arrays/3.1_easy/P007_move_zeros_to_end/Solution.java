/**
 * Problem: Move Zeros to End (LeetCode #283)
 * Difficulty: EASY | XP: 10
 *
 * Move all zeros to the end of the array while maintaining
 * the relative order of non-zero elements. In-place.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Extra Array)
// Time: O(n) | Space: O(n)
// ============================================================
class BruteForce {
    public static void moveZeroes(int[] nums) {
        int n = nums.length;
        int[] temp = new int[n]; // defaults to all zeros
        int idx = 0;

        // Copy non-zeros
        for (int num : nums) {
            if (num != 0) {
                temp[idx++] = num;
            }
        }
        // remaining positions are already 0

        System.arraycopy(temp, 0, nums, 0, n);
    }
}

// ============================================================
// Approach 2: Optimal (Two-Pointer Write + Backfill)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static void moveZeroes(int[] nums) {
        int writeIdx = 0;

        // Place all non-zeros at the front
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[writeIdx++] = nums[i];
            }
        }

        // Fill the rest with zeros
        while (writeIdx < nums.length) {
            nums[writeIdx++] = 0;
        }
    }
}

// ============================================================
// Approach 3: Best (Swap Variant -- single pass, no backfill)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static void moveZeroes(int[] nums) {
        int writeIdx = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                // Swap nums[i] with nums[writeIdx]
                int temp = nums[i];
                nums[i] = nums[writeIdx];
                nums[writeIdx] = temp;
                writeIdx++;
            }
        }
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Move Zeros to End ===\n");

        int[][] testCases = {
            {0, 1, 0, 3, 12},
            {0},
            {1, 2, 3},
            {0, 0, 0, 1},
            {0, 0, 0},
            {-1, 0, 3, 0, -5}
        };
        int[][] expected = {
            {1, 3, 12, 0, 0},
            {0},
            {1, 2, 3},
            {1, 0, 0, 0},
            {0, 0, 0},
            {-1, 3, -5, 0, 0}
        };

        for (int t = 0; t < testCases.length; t++) {
            int[] a1 = testCases[t].clone();
            int[] a2 = testCases[t].clone();
            int[] a3 = testCases[t].clone();

            BruteForce.moveZeroes(a1);
            Optimal.moveZeroes(a2);
            Best.moveZeroes(a3);

            boolean pass = Arrays.equals(a1, expected[t])
                        && Arrays.equals(a2, expected[t])
                        && Arrays.equals(a3, expected[t]);

            System.out.println("Input:    " + Arrays.toString(testCases[t]));
            System.out.println("Brute:    " + Arrays.toString(a1));
            System.out.println("Optimal:  " + Arrays.toString(a2));
            System.out.println("Best:     " + Arrays.toString(a3));
            System.out.println("Expected: " + Arrays.toString(expected[t]));
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
