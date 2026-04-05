import java.util.*;

/**
 * Problem: Maximum Consecutive Gap
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given an unsorted array, find the maximum difference between successive
 * elements in its sorted form. Must run in linear time and space (O(n)).
 *
 * Key insight (Pigeonhole): If n elements span [min, max], the maximum gap
 * is at least ceil((max-min)/(n-1)). So we create (n-1) buckets of that
 * width — any gap larger than the bucket size MUST cross a bucket boundary.
 * We only track min and max per bucket; the answer is the max over all
 * consecutive non-empty bucket pairs of (nextBucketMin - prevBucketMax).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // Sort the array naively (insertion sort style), then scan.
    // ============================================================
    public static int bruteForce(int[] A) {
        if (A == null || A.length < 2) return 0;
        int[] arr = A.clone();
        // Insertion sort
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        int maxGap = 0;
        for (int i = 1; i < arr.length; i++) {
            maxGap = Math.max(maxGap, arr[i] - arr[i - 1]);
        }
        return maxGap;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Sort then scan
    // Time: O(n log n)  |  Space: O(n)
    // Sort using library sort, then find max consecutive difference.
    // ============================================================
    public static int optimal(int[] A) {
        if (A == null || A.length < 2) return 0;
        int[] arr = A.clone();
        Arrays.sort(arr);
        int maxGap = 0;
        for (int i = 1; i < arr.length; i++) {
            maxGap = Math.max(maxGap, arr[i] - arr[i - 1]);
        }
        return maxGap;
    }

    // ============================================================
    // APPROACH 3: BEST — Bucket Sort (Pigeonhole Principle)
    // Time: O(n)  |  Space: O(n)
    // Place elements in buckets of size ceil((max-min)/(n-1)).
    // The maximum gap must cross at least one empty bucket boundary.
    // Track only min/max per bucket. Scan consecutive non-empty buckets.
    // ============================================================
    public static int best(int[] A) {
        if (A == null || A.length < 2) return 0;
        int n = A.length;

        int minVal = A[0], maxVal = A[0];
        for (int x : A) {
            minVal = Math.min(minVal, x);
            maxVal = Math.max(maxVal, x);
        }

        if (minVal == maxVal) return 0;

        // Bucket size (at least 1 to avoid division by zero)
        int bucketSize = Math.max(1, (maxVal - minVal) / (n - 1));
        int bucketCount = (maxVal - minVal) / bucketSize + 1;

        int[] bucketMin = new int[bucketCount];
        int[] bucketMax = new int[bucketCount];
        boolean[] occupied = new boolean[bucketCount];
        Arrays.fill(bucketMin, Integer.MAX_VALUE);
        Arrays.fill(bucketMax, Integer.MIN_VALUE);

        for (int x : A) {
            int idx = (x - minVal) / bucketSize;
            occupied[idx] = true;
            bucketMin[idx] = Math.min(bucketMin[idx], x);
            bucketMax[idx] = Math.max(bucketMax[idx], x);
        }

        int maxGap = 0;
        int prevMax = minVal;
        for (int i = 0; i < bucketCount; i++) {
            if (!occupied[i]) continue;
            maxGap = Math.max(maxGap, bucketMin[i] - prevMax);
            prevMax = bucketMax[i];
        }
        return maxGap;
    }

    public static void main(String[] args) {
        System.out.println("=== Maximum Consecutive Gap ===");
        int[][] tests = {
            {1, 10, 5},           // sorted: 1,5,10 -> max gap = 5
            {3, 6, 9, 1},         // sorted: 1,3,6,9 -> max gap = 3
            {1, 2},               // gap = 1
            {1, 1000000},         // gap = 999999
            {9, 3, 1, 7, 2, 6},   // sorted: 1,2,3,6,7,9 -> max gap = 3
        };
        for (int[] t : tests) {
            System.out.printf("A=%s -> Brute=%d, Optimal=%d, Best=%d%n",
                    Arrays.toString(t), bruteForce(t), optimal(t), best(t));
        }
    }
}
