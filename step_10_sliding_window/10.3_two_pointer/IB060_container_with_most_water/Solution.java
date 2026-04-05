/**
 * Problem: Container With Most Water (LeetCode 11)
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given n vertical lines at positions 0..n-1 with heights height[i],
 * find two lines that form a container holding the most water.
 * Area = min(height[left], height[right]) * (right - left).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n²)  |  Space: O(1)
    // Try all pairs (i, j) and compute area; track maximum.
    // ============================================================

    /**
     * Nested loops enumerate every pair of lines (i, j) where i < j.
     * area(i, j) = min(height[i], height[j]) * (j - i).
     * Return the global maximum. Simple but TLEs on large inputs.
     */
    public static int bruteForce(int[] height) {
        int maxWater = 0;
        int n = height.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int water = Math.min(height[i], height[j]) * (j - i);
                if (water > maxWater) maxWater = water;
            }
        }
        return maxWater;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (two pointers from both ends)
    // Time: O(n)  |  Space: O(1)
    // Start with widest container; always move the shorter-side pointer
    // inward. Greedy correctness: moving the taller side can only
    // decrease or maintain area, so we never lose the optimal pair.
    // ============================================================

    /**
     * Two pointers: left = 0, right = n-1.
     * At each step compute area = min(h[l], h[r]) * (r - l).
     * Move the pointer with the smaller height inward.
     * Rationale: the current short side is a bottleneck; moving it
     * is the only way to potentially find a taller replacement that
     * compensates for the reduced width.
     */
    public static int optimal(int[] height) {
        int left = 0, right = height.length - 1;
        int maxWater = 0;

        while (left < right) {
            int h = Math.min(height[left], height[right]);
            int area = h * (right - left);
            if (area > maxWater) maxWater = area;

            if (height[left] <= height[right]) left++;
            else                               right--;
        }
        return maxWater;
    }

    // ============================================================
    // APPROACH 3: BEST  (two pointers with skip optimisation)
    // Time: O(n)  |  Space: O(1)
    // Extend Approach 2: after choosing which side to move, skip
    // all immediately following lines that are no taller than the
    // current boundary — they cannot increase the area.
    // Same worst-case O(n) but faster in practice.
    // ============================================================

    /**
     * Same two-pointer strategy as Approach 2, with an inner skip:
     * after moving the shorter pointer, continue advancing it while
     * the next line is still no taller than the line we just left.
     * This prunes obviously suboptimal candidates.
     */
    public static int best(int[] height) {
        int left = 0, right = height.length - 1;
        int maxWater = 0;

        while (left < right) {
            int hl = height[left], hr = height[right];
            int area = Math.min(hl, hr) * (right - left);
            if (area > maxWater) maxWater = area;

            if (hl <= hr) {
                // Skip left lines not taller than hl
                while (left < right && height[left] <= hl) left++;
            } else {
                // Skip right lines not taller than hr
                while (left < right && height[right] <= hr) right--;
            }
        }
        return maxWater;
    }

    public static void main(String[] args) {
        System.out.println("=== Container With Most Water ===\n");

        Object[][] tests = {
            {new int[]{1,8,6,2,5,4,8,3,7}, 49},
            {new int[]{1,1},                 1},
            {new int[]{4,3,2,1,4},          16},
            {new int[]{1,2,1},               2},
            {new int[]{2,3,4,5,18,17,6},    17},
        };

        for (Object[] tc : tests) {
            int[] height   = (int[]) tc[0];
            int   expected = (int)   tc[1];

            int b   = bruteForce(height);
            int o   = optimal(height);
            int bst = best(height);

            String status = (b == expected && o == expected && bst == expected)
                            ? "PASS" : "FAIL";
            System.out.printf("[%s] height=%s%n", status,
                              java.util.Arrays.toString(height));
            System.out.printf("       Brute:   %d%n", b);
            System.out.printf("       Optimal: %d%n", o);
            System.out.printf("       Best:    %d%n", bst);
            System.out.printf("       Expect:  %d%n%n", expected);
        }
    }
}
