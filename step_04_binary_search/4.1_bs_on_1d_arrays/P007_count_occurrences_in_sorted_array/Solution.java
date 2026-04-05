/**
 * Problem: Count Occurrences in Sorted Array
 * Difficulty: EASY | XP: 10
 *
 * Count how many times target appears in a sorted array.
 * Key insight: find first and last position via binary search, then subtract.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Linear Scan)
// Time: O(n) | Space: O(1)
// ============================================================
class BruteForce {
    public static int countOccurrences(int[] nums, int target) {
        int count = 0;
        for (int num : nums) {
            if (num == target) count++;
        }
        return count;
    }
}

// ============================================================
// Approach 2: Optimal (Two Binary Searches)
// Time: O(log n) | Space: O(1)
// ============================================================
class Optimal {
    public static int countOccurrences(int[] nums, int target) {
        int first = findFirst(nums, target);
        if (first == -1) return 0;
        int last = findLast(nums, target);
        return last - first + 1;
    }

    private static int findFirst(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        int ans = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) {
                ans = mid;
                hi = mid - 1; // search left for first occurrence
            } else if (nums[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return ans;
    }

    private static int findLast(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        int ans = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) {
                ans = mid;
                lo = mid + 1; // search right for last occurrence
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
// Approach 3: Best (Using Arrays.binarySearch boundaries)
// Time: O(log n) | Space: O(1)
// ============================================================
class Best {
    public static int countOccurrences(int[] nums, int target) {
        // lowerBound: first index where nums[i] >= target
        int left = lowerBound(nums, target);
        // upperBound: first index where nums[i] > target
        int right = lowerBound(nums, target + 1);
        return right - left;
    }

    private static int lowerBound(int[] nums, int target) {
        int lo = 0, hi = nums.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Count Occurrences in Sorted Array ===\n");

        int[][] arrays = {
            {1, 1, 2, 2, 2, 2, 3},
            {1, 1, 2, 2, 2, 2, 3},
            {8, 9, 10, 12, 12, 12},
            {5},
            {5},
            {2, 2, 2, 2, 2},
            {1, 3, 5, 7}
        };
        int[] targets =  {2, 4, 12, 5, 3, 2, 1};
        int[] expected = {4, 0, 3,  1, 0, 5, 1};

        for (int t = 0; t < arrays.length; t++) {
            int[] nums = arrays[t];
            int target = targets[t];
            int b = BruteForce.countOccurrences(nums, target);
            int o = Optimal.countOccurrences(nums, target);
            int r = Best.countOccurrences(nums, target);
            boolean pass = (b == expected[t] && o == expected[t] && r == expected[t]);

            System.out.println("Input:    " + Arrays.toString(nums) + ", target=" + target);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + r);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
