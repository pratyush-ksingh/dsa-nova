/**
 * Problem: Median of Two Sorted Arrays
 * Difficulty: HARD | XP: 50
 *
 * Given two sorted arrays nums1 and nums2, find the median of the combined
 * sorted array. Time complexity must be O(log(min(m, n))).
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O((m+n) log(m+n))  |  Space: O(m+n)
    // ============================================================
    /**
     * Merge both arrays, sort, then find median directly.
     * Real-life: Combining sorted patient records to find median age/reading.
     */
    public static double bruteForce(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[] merged = new int[m + n];
        System.arraycopy(nums1, 0, merged, 0, m);
        System.arraycopy(nums2, 0, merged, m, n);
        Arrays.sort(merged);
        int total = m + n;
        if (total % 2 == 1) return merged[total / 2];
        return (merged[total / 2 - 1] + merged[total / 2]) / 2.0;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Two-pointer merge)
    // Time: O(m+n)  |  Space: O(1)
    // ============================================================
    /**
     * Use two pointers to virtually merge until reaching the median position.
     * No extra space needed (beyond pointer variables).
     * Real-life: Finding median in sorted streaming data from two sources.
     */
    public static double optimal(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int total = m + n;
        int target = total / 2;
        int i = 0, j = 0;
        int prev = -1, curr = -1;

        for (int step = 0; step <= target; step++) {
            prev = curr;
            if (i < m && (j >= n || nums1[i] <= nums2[j])) {
                curr = nums1[i++];
            } else {
                curr = nums2[j++];
            }
        }
        if (total % 2 == 1) return curr;
        return (prev + curr) / 2.0;
    }

    // ============================================================
    // APPROACH 3: BEST  (Binary Search — O(log(min(m,n))))
    // ============================================================
    /**
     * Binary search on the smaller array. Partition both arrays such that
     * all elements in left partition <= all in right. The median is then
     * determined by the boundary values.
     * Real-life: Distributed statistics computation where merging full arrays is too costly.
     */
    public static double best(int[] nums1, int[] nums2) {
        // Ensure nums1 is the smaller array
        if (nums1.length > nums2.length) return best(nums2, nums1);

        int m = nums1.length, n = nums2.length;
        int lo = 0, hi = m;
        int halfLen = (m + n + 1) / 2;

        while (lo <= hi) {
            int cut1 = (lo + hi) / 2;
            int cut2 = halfLen - cut1;

            int left1  = (cut1 == 0) ? Integer.MIN_VALUE : nums1[cut1 - 1];
            int right1 = (cut1 == m) ? Integer.MAX_VALUE : nums1[cut1];
            int left2  = (cut2 == 0) ? Integer.MIN_VALUE : nums2[cut2 - 1];
            int right2 = (cut2 == n) ? Integer.MAX_VALUE : nums2[cut2];

            if (left1 <= right2 && left2 <= right1) {
                // Found correct partition
                if ((m + n) % 2 == 1) return Math.max(left1, left2);
                return (Math.max(left1, left2) + Math.min(right1, right2)) / 2.0;
            } else if (left1 > right2) {
                hi = cut1 - 1;
            } else {
                lo = cut1 + 1;
            }
        }
        return 0.0; // never reached with valid inputs
    }

    public static void main(String[] args) {
        System.out.println("=== Median of Two Sorted Arrays ===");

        int[][] a = {{1, 3},   {1, 2},  {0, 0}, {2}};
        int[][] b = {{2},      {3, 4},  {0, 0}, {}};
        double[] expected = {2.0, 2.5, 0.0, 2.0};

        for (int t = 0; t < a.length; t++) {
            System.out.println("\nnums1=" + Arrays.toString(a[t]) + "  nums2=" + Arrays.toString(b[t]));
            System.out.printf("Expected: %.5f%n", expected[t]);
            System.out.printf("Brute:   %.5f%n", bruteForce(a[t], b[t]));
            System.out.printf("Optimal: %.5f%n", optimal(a[t], b[t]));
            System.out.printf("Best:    %.5f%n", best(a[t], b[t]));
        }
    }
}
