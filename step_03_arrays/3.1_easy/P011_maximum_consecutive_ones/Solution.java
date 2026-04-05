/**
 * Problem: Maximum Consecutive Ones (LeetCode #485)
 * Difficulty: EASY | XP: 10
 *
 * Given a binary array, find the maximum number of consecutive 1s.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check All Starting Points
    // Time: O(n^2)  |  Space: O(1)
    // For each index, count consecutive 1s from that index.
    // ============================================================
    static class BruteForce {
        public static int findMaxConsecutiveOnes(int[] nums) {
            int maxCount = 0;
            int n = nums.length;

            for (int i = 0; i < n; i++) {
                if (nums[i] == 1) {
                    int count = 0;
                    int j = i;
                    while (j < n && nums[j] == 1) {
                        count++;
                        j++;
                    }
                    maxCount = Math.max(maxCount, count);
                }
            }
            return maxCount;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Single Pass with Running Counter
    // Time: O(n)  |  Space: O(1)
    // Increment on 1, reset on 0, track max.
    // ============================================================
    static class Optimal {
        public static int findMaxConsecutiveOnes(int[] nums) {
            int count = 0;
            int maxCount = 0;

            for (int num : nums) {
                if (num == 1) {
                    count++;
                } else {
                    count = 0;
                }
                maxCount = Math.max(maxCount, count);
            }
            return maxCount;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Single Pass (Same as Optimal)
    // Time: O(n)  |  Space: O(1)
    // Compact version using ternary operator.
    // ============================================================
    static class Best {
        public static int findMaxConsecutiveOnes(int[] nums) {
            int count = 0, maxCount = 0;
            for (int num : nums) {
                count = (num == 1) ? count + 1 : 0;
                maxCount = Math.max(maxCount, count);
            }
            return maxCount;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Maximum Consecutive Ones ===");

        int[] test1 = {1, 1, 0, 1, 1, 1};
        int[] test2 = {1, 0, 1, 1, 0, 1};
        int[] test3 = {0, 0, 0};
        int[] test4 = {1};
        int[] test5 = {1, 1, 1, 1};
        int[] test6 = {1, 0, 1, 0, 1};

        System.out.println("--- Brute Force ---");
        System.out.println(BruteForce.findMaxConsecutiveOnes(test1));  // 3
        System.out.println(BruteForce.findMaxConsecutiveOnes(test2));  // 2
        System.out.println(BruteForce.findMaxConsecutiveOnes(test3));  // 0

        System.out.println("--- Optimal ---");
        System.out.println(Optimal.findMaxConsecutiveOnes(test1));     // 3
        System.out.println(Optimal.findMaxConsecutiveOnes(test2));     // 2
        System.out.println(Optimal.findMaxConsecutiveOnes(test3));     // 0

        System.out.println("--- Best ---");
        System.out.println(Best.findMaxConsecutiveOnes(test4));        // 1
        System.out.println(Best.findMaxConsecutiveOnes(test5));        // 4
        System.out.println(Best.findMaxConsecutiveOnes(test6));        // 1
    }
}
