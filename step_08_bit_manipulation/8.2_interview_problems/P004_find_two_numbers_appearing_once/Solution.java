/**
 * Problem: Find Two Numbers Appearing Once
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array where every number appears exactly twice except for two
 * numbers which appear exactly once, find those two numbers.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — HashMap frequency count
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        public int[] findTwo(int[] nums) {
            Map<Integer, Integer> freq = new HashMap<>();
            for (int n : nums)
                freq.merge(n, 1, Integer::sum);

            int[] result = new int[2];
            int idx = 0;
            for (Map.Entry<Integer, Integer> e : freq.entrySet())
                if (e.getValue() == 1) result[idx++] = e.getKey();
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — XOR + partition by differentiating bit
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Step 1: XOR all elements -> xorAll = a XOR b.
         *         (duplicate pairs cancel: x XOR x = 0)
         * Step 2: Isolate the rightmost set bit of xorAll.
         *         This bit differs between a and b.
         * Step 3: Partition numbers by that bit; XOR each partition
         *         separately to recover a and b.
         */
        public int[] findTwo(int[] nums) {
            int xorAll = 0;
            for (int n : nums) xorAll ^= n;  // xorAll = a XOR b

            // Isolate lowest set bit using two's complement trick
            int diffBit = xorAll & (-xorAll);

            int a = 0, b = 0;
            for (int n : nums) {
                if ((n & diffBit) != 0) a ^= n;
                else                    b ^= n;
            }
            return new int[]{a, b};
        }
    }

    // ============================================================
    // APPROACH 3: BEST — XOR + explicit bit scan
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Same XOR partition as Approach 2 but locates the differentiating
         * bit by scanning positions 0-31 explicitly — avoids two's-complement
         * trick and is clearer for bit-manipulation beginners.
         */
        public int[] findTwo(int[] nums) {
            int xorAll = 0;
            for (int n : nums) xorAll ^= n;

            // Find position of any set bit
            int bitPos = 0;
            while ((xorAll >> bitPos & 1) == 0) bitPos++;

            int a = 0, b = 0;
            for (int n : nums) {
                if ((n >> bitPos & 1) == 1) a ^= n;
                else                        b ^= n;
            }
            int[] res = {a, b};
            Arrays.sort(res);
            return res;
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Find Two Numbers Appearing Once ===");
        int[] nums = {1, 2, 3, 2, 1, 4}; // unique: 3, 4

        System.out.println("Brute:   " + Arrays.toString(new BruteForce().findTwo(nums)));
        System.out.println("Optimal: " + Arrays.toString(new Optimal().findTwo(nums)));
        System.out.println("Best:    " + Arrays.toString(new Best().findTwo(nums)));

        int[] nums2 = {4, 1, 2, 1, 2, 3}; // unique: 3, 4
        System.out.println("\nBrute:   " + Arrays.toString(new BruteForce().findTwo(nums2)));
        System.out.println("Optimal: " + Arrays.toString(new Optimal().findTwo(nums2)));
        System.out.println("Best:    " + Arrays.toString(new Best().findTwo(nums2)));
    }
}
