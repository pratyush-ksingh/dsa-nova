/**
 * Problem: GCD or HCF
 * Difficulty: EASY | XP: 10
 *
 * Find the Greatest Common Divisor (GCD) / Highest Common Factor (HCF)
 * of two positive integers.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Iterate from min(a,b) down to 1
    // Time: O(min(a,b))  |  Space: O(1)
    // Check every number from min(a,b) down to 1; first that divides
    // both is the GCD.
    // ============================================================
    static class BruteForce {
        public static int gcd(int a, int b) {
            if (a == 0) return b;
            if (b == 0) return a;
            int result = 1;
            for (int i = Math.min(a, b); i >= 1; i--) {
                if (a % i == 0 && b % i == 0) {
                    result = i;
                    break;
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Euclidean Algorithm (Iterative)
    // Time: O(log(min(a,b)))  |  Space: O(1)
    // Repeatedly replace the larger number with (larger % smaller)
    // until one becomes 0. The other is the GCD.
    // ============================================================
    static class Optimal {
        public static int gcd(int a, int b) {
            while (b != 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Euclidean Algorithm (Recursive)
    // Time: O(log(min(a,b)))  |  Space: O(log(min(a,b))) call stack
    // Same logic as iterative but expressed recursively for clarity.
    // ============================================================
    static class Best {
        public static int gcd(int a, int b) {
            if (b == 0) return a;
            return gcd(b, a % b);
        }
    }

    public static void main(String[] args) {
        int[][] testCases = {{12, 8}, {54, 24}, {7, 13}, {0, 5}, {100, 100}, {48, 18}, {1, 1000000}};
        System.out.println("=== GCD or HCF ===");

        for (int[] tc : testCases) {
            int a = tc[0], b = tc[1];
            System.out.printf("GCD(%d, %d)%n", a, b);
            System.out.printf("  Brute Force: %d%n", BruteForce.gcd(a, b));
            System.out.printf("  Optimal:     %d%n", Optimal.gcd(a, b));
            System.out.printf("  Best:        %d%n", Best.gcd(a, b));
        }
    }
}
