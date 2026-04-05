/**
 * Problem: Trapping Rain Water
 * Difficulty: HARD | XP: 50
 *
 * Given n non-negative integers representing an elevation map where the width
 * of each bar is 1, compute how much water it can trap after raining.
 *
 * Classic problem: water at index i = min(maxLeft[i], maxRight[i]) - height[i]
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - For each bar find left and right max
    // Time: O(n^2)  |  Space: O(1)
    // For each position scan left and right to find max heights; compute water
    // ============================================================
    public static int bruteForce(int[] height) {
        int n = height.length, water = 0;
        for (int i = 1; i < n - 1; i++) {
            int maxLeft = 0, maxRight = 0;
            for (int l = 0; l <= i; l++)       maxLeft  = Math.max(maxLeft,  height[l]);
            for (int r = i; r < n; r++)         maxRight = Math.max(maxRight, height[r]);
            water += Math.min(maxLeft, maxRight) - height[i];
        }
        return water;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Precompute left/right max arrays
    // Time: O(n)  |  Space: O(n)
    // Two prefix/suffix passes then a single computation pass
    // ============================================================
    public static int optimal(int[] height) {
        int n = height.length;
        if (n == 0) return 0;
        int[] maxLeft  = new int[n];
        int[] maxRight = new int[n];
        maxLeft[0]     = height[0];
        maxRight[n-1]  = height[n-1];
        for (int i = 1; i < n; i++)
            maxLeft[i]  = Math.max(maxLeft[i-1],  height[i]);
        for (int i = n-2; i >= 0; i--)
            maxRight[i] = Math.max(maxRight[i+1], height[i]);
        int water = 0;
        for (int i = 0; i < n; i++)
            water += Math.min(maxLeft[i], maxRight[i]) - height[i];
        return water;
    }

    // ============================================================
    // APPROACH 3: BEST - Two-pointer O(1) space
    // Time: O(n)  |  Space: O(1)
    // Move the pointer with smaller max inward; water determined by smaller side
    // ============================================================
    public static int best(int[] height) {
        int left = 0, right = height.length - 1;
        int maxLeft = 0, maxRight = 0, water = 0;
        while (left < right) {
            if (height[left] <= height[right]) {
                if (height[left] >= maxLeft) maxLeft = height[left];
                else water += maxLeft - height[left];
                left++;
            } else {
                if (height[right] >= maxRight) maxRight = height[right];
                else water += maxRight - height[right];
                right--;
            }
        }
        return water;
    }

    public static void main(String[] args) {
        System.out.println("=== Trapping Rain Water ===");

        int[] h1 = {0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println("height = " + Arrays.toString(h1));
        System.out.println("Brute:   " + bruteForce(h1));   // 6
        System.out.println("Optimal: " + optimal(h1));       // 6
        System.out.println("Best:    " + best(h1));          // 6

        int[] h2 = {4,2,0,3,2,5};
        System.out.println("\nheight = " + Arrays.toString(h2));
        System.out.println("Brute:   " + bruteForce(h2));   // 9
        System.out.println("Optimal: " + optimal(h2));       // 9
        System.out.println("Best:    " + best(h2));          // 9

        int[] h3 = {3,0,2,0,4};
        System.out.println("\nheight = " + Arrays.toString(h3));
        System.out.println("Brute:   " + bruteForce(h3));   // 7
        System.out.println("Optimal: " + optimal(h3));
        System.out.println("Best:    " + best(h3));
    }
}
