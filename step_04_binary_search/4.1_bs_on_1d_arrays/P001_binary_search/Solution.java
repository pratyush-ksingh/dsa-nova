/**
 * Problem: Binary Search (LeetCode #704)
 * Difficulty: EASY | XP: 10
 *
 * Given a sorted array and a target, return the index of target or -1.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Linear Scan)
// Time: O(n) | Space: O(1)
// ============================================================
class BruteForce {
    public static int search(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                return i;
            }
        }
        return -1;
    }
}

// ============================================================
// Approach 2: Optimal (Iterative Binary Search)
// Time: O(log n) | Space: O(1)
// ============================================================
class Optimal {
    public static int search(int[] nums, int target) {
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
        return -1;
    }
}

// ============================================================
// Approach 3: Best (Recursive Binary Search)
// Time: O(log n) | Space: O(log n) recursion stack
// ============================================================
class Best {
    public static int search(int[] nums, int target) {
        return binarySearch(nums, target, 0, nums.length - 1);
    }

    private static int binarySearch(int[] nums, int target, int lo, int hi) {
        if (lo > hi) return -1;
        int mid = lo + (hi - lo) / 2;
        if (nums[mid] == target) return mid;
        if (nums[mid] < target) return binarySearch(nums, target, mid + 1, hi);
        return binarySearch(nums, target, lo, mid - 1);
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Binary Search ===\n");

        int[][] arrays = {
            {-1, 0, 3, 5, 9, 12},
            {-1, 0, 3, 5, 9, 12},
            {5},
            {5},
            {2, 5},
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
        };
        int[] targets = {9, 2, 5, -1, 5, 1};
        int[] expected = {4, -1, 0, -1, 1, 0};

        for (int t = 0; t < arrays.length; t++) {
            int b = BruteForce.search(arrays[t], targets[t]);
            int o = Optimal.search(arrays[t], targets[t]);
            int r = Best.search(arrays[t], targets[t]);
            boolean pass = (b == expected[t] && o == expected[t] && r == expected[t]);

            System.out.println("Input:    " + Arrays.toString(arrays[t]) + ", target=" + targets[t]);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Recurse:  " + r);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
