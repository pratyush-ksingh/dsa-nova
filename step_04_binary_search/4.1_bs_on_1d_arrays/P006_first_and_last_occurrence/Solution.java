/**
 * Problem: First and Last Occurrence (LC #34)
 * Difficulty: MEDIUM | XP: 25
 *
 * Find the starting and ending position of target in a sorted array.
 * Key insight: two binary searches -- one biased left, one biased right.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Linear Scan)
// Time: O(n) | Space: O(1)
// ============================================================
class BruteForce {
    public static int[] searchRange(int[] nums, int target) {
        int first = -1, last = -1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                if (first == -1) first = i;
                last = i;
            }
        }
        return new int[]{first, last};
    }
}

// ============================================================
// Approach 2: Optimal (Two Binary Searches)
// Time: O(log n) | Space: O(1)
// ============================================================
class Optimal {
    public static int[] searchRange(int[] nums, int target) {
        int first = findFirst(nums, target);
        if (first == -1) return new int[]{-1, -1};
        int last = findLast(nums, target);
        return new int[]{first, last};
    }

    private static int findFirst(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1, ans = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) {
                ans = mid;
                hi = mid - 1; // keep searching left
            } else if (nums[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return ans;
    }

    private static int findLast(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1, ans = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) {
                ans = mid;
                lo = mid + 1; // keep searching right
            } else if (nums[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return ans;
    }
}

// ============================================================
// Approach 3: Best (Lower/Upper Bound)
// Time: O(log n) | Space: O(1)
// ============================================================
class Best {
    public static int[] searchRange(int[] nums, int target) {
        int left = lowerBound(nums, target);
        if (left == nums.length || nums[left] != target) return new int[]{-1, -1};
        int right = lowerBound(nums, target + 1) - 1;
        return new int[]{left, right};
    }

    private static int lowerBound(int[] nums, int target) {
        int lo = 0, hi = nums.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] < target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== First and Last Occurrence ===\n");

        int[][] arrays = {
            {5, 7, 7, 8, 8, 10},
            {5, 7, 7, 8, 8, 10},
            {1},
            {2, 2, 2, 2, 2}
        };
        int[] targets =  {8, 6, 1, 2};
        int[][] expected = {{3,4}, {-1,-1}, {0,0}, {0,4}};

        for (int t = 0; t < arrays.length; t++) {
            int[] nums = arrays[t];
            int target = targets[t];
            int[] b = BruteForce.searchRange(nums, target);
            int[] o = Optimal.searchRange(nums, target);
            int[] r = Best.searchRange(nums, target);
            boolean pass = Arrays.equals(b, expected[t]) &&
                           Arrays.equals(o, expected[t]) &&
                           Arrays.equals(r, expected[t]);

            System.out.println("Input:    " + Arrays.toString(nums) + ", target=" + target);
            System.out.println("Brute:    " + Arrays.toString(b));
            System.out.println("Optimal:  " + Arrays.toString(o));
            System.out.println("Best:     " + Arrays.toString(r));
            System.out.println("Expected: " + Arrays.toString(expected[t]));
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
