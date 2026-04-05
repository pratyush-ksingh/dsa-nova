/**
 * Problem: Fruits into Baskets (LeetCode #904)
 * Difficulty: MEDIUM | XP: 25
 *
 * Find the longest subarray with at most 2 distinct values.
 * Key insight: Sliding window + frequency map with K=2 distinct limit.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Check All Subarrays)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static int totalFruit(int[] fruits) {
        int n = fruits.length;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            Set<Integer> types = new HashSet<>();
            for (int j = i; j < n; j++) {
                types.add(fruits[j]);
                if (types.size() > 2) break;
                ans = Math.max(ans, j - i + 1);
            }
        }
        return ans;
    }
}

// ============================================================
// Approach 2: Optimal (Sliding Window -- Shrinking)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static int totalFruit(int[] fruits) {
        int n = fruits.length;
        Map<Integer, Integer> freq = new HashMap<>();
        int left = 0, ans = 0;

        for (int right = 0; right < n; right++) {
            freq.merge(fruits[right], 1, Integer::sum);

            // Shrink until at most 2 distinct types
            while (freq.size() > 2) {
                int leftFruit = fruits[left];
                freq.merge(leftFruit, -1, Integer::sum);
                if (freq.get(leftFruit) == 0) {
                    freq.remove(leftFruit);
                }
                left++;
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }
}

// ============================================================
// Approach 3: Best (Sliding Window -- Non-Shrinking)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static int totalFruit(int[] fruits) {
        int n = fruits.length;
        Map<Integer, Integer> freq = new HashMap<>();
        int left = 0;

        for (int right = 0; right < n; right++) {
            freq.merge(fruits[right], 1, Integer::sum);

            // If invalid, slide window by 1 (don't shrink)
            if (freq.size() > 2) {
                int leftFruit = fruits[left];
                freq.merge(leftFruit, -1, Integer::sum);
                if (freq.get(leftFruit) == 0) {
                    freq.remove(leftFruit);
                }
                left++;
            }
        }
        return n - left;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Fruits into Baskets ===\n");

        int[][] inputs = {
            {1, 2, 1},
            {0, 1, 2, 2},
            {1, 2, 3, 2, 2},
            {3, 3, 3, 1, 2, 1, 1, 2, 3, 3, 4},
            {1},
            {1, 1, 1, 1},
            {1, 2, 3, 4, 5}
        };
        int[] expected = {3, 3, 4, 5, 1, 4, 2};

        for (int t = 0; t < inputs.length; t++) {
            int[] fruits = inputs[t];
            int b = BruteForce.totalFruit(fruits);
            int o = Optimal.totalFruit(fruits);
            int r = Best.totalFruit(fruits);
            boolean pass = (b == expected[t] && o == expected[t] && r == expected[t]);

            System.out.println("Input:    " + Arrays.toString(fruits));
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + r);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
