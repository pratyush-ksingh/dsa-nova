/**
 * Problem: Search Insert Position (LeetCode #35)
 * Difficulty: EASY | XP: 10
 *
 * Given a sorted array of distinct integers and a target, return the index
 * if found, else return the index where it would be inserted.
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
     * Walk through the array and return the first index where nums[i] >= target.
     * If no such index exists, target goes at the end.
     */
    public static int searchInsert(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= target) {
                return i;
            }
        }
        return nums.length;
    }
}

// ============================================================
// Approach 2: Optimal (Binary Search with ans tracking)
// Time: O(log n) | Space: O(1)
// ============================================================
class Optimal {
    /**
     * Lower-bound binary search: find the first index where nums[i] >= target.
     * Track the answer explicitly.
     */
    public static int searchInsert(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        int ans = nums.length; // default: insert at end

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] >= target) {
                ans = mid;       // candidate answer
                hi = mid - 1;   // look for earlier position
            } else {
                lo = mid + 1;
            }
        }
        return ans;
    }
}

// ============================================================
// Approach 3: Best (Simplified -- lo converges to answer)
// Time: O(log n) | Space: O(1)
// ============================================================
class Best {
    /**
     * Standard binary search. When loop ends, lo IS the insertion point.
     * No extra variable needed.
     */
    public static int searchInsert(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return lo; // lo is the correct insertion index
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Search Insert Position ===\n");

        int[][] arrays = {
            {1, 3, 5, 6},
            {1, 3, 5, 6},
            {1, 3, 5, 6},
            {1, 3, 5, 6},
            {1},
            {1},
            {1, 3},
        };
        int[] targets  = {5, 2, 7, 0, 1, 0, 2};
        int[] expected  = {2, 1, 4, 0, 0, 0, 1};

        for (int t = 0; t < arrays.length; t++) {
            int b = BruteForce.searchInsert(arrays[t], targets[t]);
            int o = Optimal.searchInsert(arrays[t], targets[t]);
            int s = Best.searchInsert(arrays[t], targets[t]);
            boolean pass = (b == expected[t] && o == expected[t] && s == expected[t]);

            System.out.println("Input:    " + Arrays.toString(arrays[t]) + ", target=" + targets[t]);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + s);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
