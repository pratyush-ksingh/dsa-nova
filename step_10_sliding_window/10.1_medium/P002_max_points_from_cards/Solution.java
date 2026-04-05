/**
 * Problem: Max Points from Cards (LeetCode #1423)
 * Difficulty: MEDIUM | XP: 25
 *
 * Pick k cards from either end to maximize sum.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Prefix-Suffix Split
// Time: O(k) | Space: O(1)
// ============================================================
class PrefixSuffix {
    public static int maxScore(int[] cardPoints, int k) {
        int n = cardPoints.length;

        // Start by taking all k from the left
        int leftSum = 0;
        for (int i = 0; i < k; i++) {
            leftSum += cardPoints[i];
        }

        int maxScore = leftSum;
        int rightSum = 0;

        // Gradually move cards from left to right
        for (int i = 0; i < k; i++) {
            leftSum -= cardPoints[k - 1 - i];
            rightSum += cardPoints[n - 1 - i];
            maxScore = Math.max(maxScore, leftSum + rightSum);
        }
        return maxScore;
    }
}

// ============================================================
// Approach 2: Optimal (Sliding Window on Minimum Subarray)
// Time: O(n) | Space: O(1)
// ============================================================
class SlidingWindow {
    public static int maxScore(int[] cardPoints, int k) {
        int n = cardPoints.length;

        // Total sum of all cards
        int totalSum = 0;
        for (int val : cardPoints) totalSum += val;

        // If we take all cards
        if (k == n) return totalSum;

        // Find minimum sum subarray of length (n - k)
        int windowSize = n - k;
        int windowSum = 0;
        for (int i = 0; i < windowSize; i++) {
            windowSum += cardPoints[i];
        }

        int minWindowSum = windowSum;
        for (int right = windowSize; right < n; right++) {
            windowSum += cardPoints[right] - cardPoints[right - windowSize];
            minWindowSum = Math.min(minWindowSum, windowSum);
        }

        return totalSum - minWindowSum;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Max Points from Cards ===\n");

        int[][] inputs = {
            {1, 2, 3, 4, 5, 6, 1},
            {2, 2, 2},
            {9, 7, 7, 9, 7, 7, 9},
            {1, 1000, 1},
            {1, 79, 80, 1, 1, 1, 200, 1},
        };
        int[] ks =       {3, 2, 7, 1, 3};
        int[] expected = {12, 4, 55, 1, 202};

        boolean allPass = true;
        for (int t = 0; t < inputs.length; t++) {
            int ps = PrefixSuffix.maxScore(inputs[t], ks[t]);
            int sw = SlidingWindow.maxScore(inputs[t], ks[t]);

            boolean pass = ps == expected[t] && sw == expected[t];
            allPass &= pass;

            System.out.printf("Input: %-25s k=%d | PrefSuf=%3d SlidWin=%3d | Expected=%3d [%s]%n",
                Arrays.toString(inputs[t]), ks[t], ps, sw, expected[t], pass ? "PASS" : "FAIL");
        }
        System.out.println("\nAll pass: " + allPass);
    }
}
