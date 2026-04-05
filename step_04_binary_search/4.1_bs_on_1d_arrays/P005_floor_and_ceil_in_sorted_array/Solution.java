/**
 * Problem: Floor and Ceil in Sorted Array
 * Difficulty: EASY | XP: 10
 *
 * Find the floor (largest <= target) and ceil (smallest >= target)
 * in a sorted array using binary search.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Linear Scan)
// Time: O(n) | Space: O(1)
// ============================================================
class BruteForce {
    /**
     * Scan left to right. Last element <= target is floor.
     * First element >= target is ceil.
     */
    public static int[] floorCeil(int[] nums, int target) {
        int floor = -1, ceil = -1;
        for (int num : nums) {
            if (num <= target) {
                floor = num;  // keep overwriting -- last one is largest <= target
            }
            if (num >= target && ceil == -1) {
                ceil = num;   // first one is smallest >= target
            }
        }
        return new int[]{floor, ceil};
    }
}

// ============================================================
// Approach 2: Optimal (Two Separate Binary Searches)
// Time: O(log n) | Space: O(1)
// ============================================================
class Optimal {
    /**
     * Run one binary search for floor and one for ceil.
     * Floor: record and go right. Ceil: record and go left.
     */
    public static int[] floorCeil(int[] nums, int target) {
        return new int[]{findFloor(nums, target), findCeil(nums, target)};
    }

    private static int findFloor(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        int floor = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] <= target) {
                floor = nums[mid];  // candidate -- try to find larger
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return floor;
    }

    private static int findCeil(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        int ceil = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] >= target) {
                ceil = nums[mid];  // candidate -- try to find smaller
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return ceil;
    }
}

// ============================================================
// Approach 3: Best (Single-Pass Binary Search)
// Time: O(log n) | Space: O(1)
// ============================================================
class Best {
    /**
     * One binary search pass that updates both floor and ceil candidates.
     * Elements < target are floor candidates; elements > target are ceil candidates.
     * If we hit target exactly, both floor and ceil = target.
     */
    public static int[] floorCeil(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        int floor = -1, ceil = -1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) {
                return new int[]{target, target};  // exact match
            } else if (nums[mid] < target) {
                floor = nums[mid];  // best floor candidate so far
                lo = mid + 1;
            } else {
                ceil = nums[mid];   // best ceil candidate so far
                hi = mid - 1;
            }
        }
        return new int[]{floor, ceil};
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Floor and Ceil in Sorted Array ===\n");

        int[][] arrays = {
            {1, 2, 8, 10, 10, 12, 19},
            {1, 2, 8, 10, 10, 12, 19},
            {1, 2, 8, 10, 10, 12, 19},
            {1, 2, 8, 10, 10, 12, 19},
            {5},
            {5},
            {3, 7},
        };
        int[] targets  = {5, 10, 0, 25, 5, 3, 5};
        int[][] expected = {
            {2, 8},
            {10, 10},
            {-1, 1},
            {19, -1},
            {5, 5},
            {-1, 5},
            {3, 7},
        };

        for (int t = 0; t < arrays.length; t++) {
            int[] b = BruteForce.floorCeil(arrays[t], targets[t]);
            int[] o = Optimal.floorCeil(arrays[t], targets[t]);
            int[] s = Best.floorCeil(arrays[t], targets[t]);
            boolean pass = Arrays.equals(b, expected[t])
                        && Arrays.equals(o, expected[t])
                        && Arrays.equals(s, expected[t]);

            System.out.println("Input:    " + Arrays.toString(arrays[t]) + ", target=" + targets[t]);
            System.out.println("Brute:    floor=" + b[0] + ", ceil=" + b[1]);
            System.out.println("Optimal:  floor=" + o[0] + ", ceil=" + o[1]);
            System.out.println("Best:     floor=" + s[0] + ", ceil=" + s[1]);
            System.out.println("Expected: floor=" + expected[t][0] + ", ceil=" + expected[t][1]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
