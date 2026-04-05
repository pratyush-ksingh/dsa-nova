import java.util.*;

/**
 * Problem: Colorful Number
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * A number is colorful if the products of every contiguous subsequence
 * of its digits are all distinct.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Nested Loops with List Scan
    // Time: O(D^3)  |  Space: O(D^2)
    // Generate all contiguous subsequences, compute products,
    // check duplicates via list contains (O(n) per check).
    // ============================================================
    static class BruteForce {
        public static int colorful(int n) {
            String s = String.valueOf(n);
            int d = s.length();
            int[] digits = new int[d];
            for (int i = 0; i < d; i++) digits[i] = s.charAt(i) - '0';

            List<Long> products = new ArrayList<>();
            for (int i = 0; i < d; i++) {
                for (int j = i + 1; j <= d; j++) {
                    long prod = 1;
                    for (int k = i; k < j; k++) prod *= digits[k];
                    if (products.contains(prod)) return 0;
                    products.add(prod);
                }
            }
            return 1;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- HashSet for O(1) Lookup
    // Time: O(D^2)  |  Space: O(D^2)
    // Running product + set for duplicate detection.
    // ============================================================
    static class Optimal {
        public static int colorful(int n) {
            String s = String.valueOf(n);
            int d = s.length();
            int[] digits = new int[d];
            for (int i = 0; i < d; i++) digits[i] = s.charAt(i) - '0';

            Set<Long> seen = new HashSet<>();
            for (int i = 0; i < d; i++) {
                long product = 1;
                for (int j = i; j < d; j++) {
                    product *= digits[j];
                    if (!seen.add(product)) return 0;
                }
            }
            return 1;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same Set Approach, Cleanest Code
    // Time: O(D^2)  |  Space: O(D^2)
    // Identical logic to Optimal; set.add returns false on dup.
    // ============================================================
    static class Best {
        public static int colorful(int n) {
            char[] chars = String.valueOf(n).toCharArray();
            int d = chars.length;
            Set<Long> seen = new HashSet<>();
            for (int i = 0; i < d; i++) {
                long product = 1;
                for (int j = i; j < d; j++) {
                    product *= (chars[j] - '0');
                    if (!seen.add(product)) return 0;
                }
            }
            return 1;
        }
    }

    public static void main(String[] args) {
        int[] tests = {3245, 23, 99, 263, 0, 1};
        System.out.println("=== Colorful Number ===");
        for (int t : tests) {
            System.out.printf("  %d: brute=%d, optimal=%d, best=%d%n",
                t, BruteForce.colorful(t), Optimal.colorful(t), Best.colorful(t));
        }
    }
}
