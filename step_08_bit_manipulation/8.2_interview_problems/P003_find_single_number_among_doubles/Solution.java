/**
 * Problem: Find Single Number Among Doubles (LeetCode #136)
 * Difficulty: EASY | XP: 10
 *
 * Every element appears twice except one. Find the single one.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;
import java.util.HashMap;

// ============================================================
// Approach 1: Brute Force (Nested Loop)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static int singleNumber(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            boolean found = false;
            for (int j = 0; j < nums.length; j++) {
                if (i != j && nums[i] == nums[j]) {
                    found = true;
                    break;
                }
            }
            if (!found) return nums[i];
        }
        return -1; // should never reach here
    }
}

// ============================================================
// Approach 2: Sorting
// Time: O(n log n) | Space: O(1)
// ============================================================
class Sorting {
    public static int singleNumber(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        for (int i = 0; i < sorted.length - 1; i += 2) {
            if (sorted[i] != sorted[i + 1]) {
                return sorted[i];
            }
        }
        return sorted[sorted.length - 1];
    }
}

// ============================================================
// Approach 3: Hash Map
// Time: O(n) | Space: O(n)
// ============================================================
class HashMapApproach {
    public static int singleNumber(int[] nums) {
        HashMap<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        for (var entry : freq.entrySet()) {
            if (entry.getValue() == 1) return entry.getKey();
        }
        return -1;
    }
}

// ============================================================
// Approach 4: Best (XOR Cancellation)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Find Single Number Among Doubles ===\n");

        int[][] inputs = {
            {2, 2, 1},
            {4, 1, 2, 1, 2},
            {1},
            {-1, 3, 3},
            {5, 7, 5, 9, 7},
        };
        int[] expected = {1, 4, 1, -1, 9};

        boolean allPass = true;
        for (int t = 0; t < inputs.length; t++) {
            int b = BruteForce.singleNumber(inputs[t]);
            int s = Sorting.singleNumber(inputs[t]);
            int h = HashMapApproach.singleNumber(inputs[t]);
            int x = Best.singleNumber(inputs[t]);

            boolean pass = b == expected[t] && s == expected[t] && h == expected[t] && x == expected[t];
            allPass &= pass;

            System.out.printf("Input: %-20s | Brute=%2d Sort=%2d Hash=%2d XOR=%2d | Expected=%2d [%s]%n",
                Arrays.toString(inputs[t]), b, s, h, x, expected[t], pass ? "PASS" : "FAIL");
        }
        System.out.println("\nAll pass: " + allPass);
    }
}
