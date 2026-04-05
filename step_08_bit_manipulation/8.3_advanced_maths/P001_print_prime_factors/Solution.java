import java.util.*;

/**
 * Problem: Print Prime Factors
 * Difficulty: EASY | XP: 10
 *
 * Find all prime factors of a given number N (with multiplicity).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check Every Number up to N
    // Time: O(N) worst case  |  Space: O(log N)
    // For each i from 2 to N, while i divides N, it's a factor.
    // ============================================================
    static class BruteForce {
        public static List<Integer> primeFactors(int n) {
            List<Integer> factors = new ArrayList<>();
            for (int i = 2; i <= n; i++) {
                while (n % i == 0) {
                    factors.add(i);
                    n /= i;
                }
            }
            return factors;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Trial Division up to sqrt(N)
    // Time: O(sqrt(N))  |  Space: O(log N)
    // Only check divisors up to sqrt(N). If N > 1 after loop,
    // it is itself a prime factor.
    // ============================================================
    static class Optimal {
        public static List<Integer> primeFactors(int n) {
            List<Integer> factors = new ArrayList<>();
            for (int i = 2; (long) i * i <= n; i++) {
                while (n % i == 0) {
                    factors.add(i);
                    n /= i;
                }
            }
            if (n > 1) {
                factors.add(n); // remaining prime factor > sqrt(original N)
            }
            return factors;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Optimized Trial Division (Skip Evens)
    // Time: O(sqrt(N))  |  Space: O(log N)
    // Handle 2 separately, then only check odd numbers 3,5,7,...
    // Halves the number of iterations compared to Optimal.
    // ============================================================
    static class Best {
        public static List<Integer> primeFactors(int n) {
            List<Integer> factors = new ArrayList<>();

            // Handle factor 2
            while (n % 2 == 0) {
                factors.add(2);
                n /= 2;
            }

            // Check odd factors from 3 to sqrt(n)
            for (int i = 3; (long) i * i <= n; i += 2) {
                while (n % i == 0) {
                    factors.add(i);
                    n /= i;
                }
            }

            // If n > 1, then it is a remaining prime factor
            if (n > 1) {
                factors.add(n);
            }

            return factors;
        }
    }

    public static void main(String[] args) {
        int[] testCases = {60, 1, 13, 84, 100, 1024, 999999937};
        System.out.println("=== Print Prime Factors ===");

        for (int n : testCases) {
            System.out.printf("N = %d%n", n);
            System.out.printf("  Brute Force: %s%n", BruteForce.primeFactors(n));
            System.out.printf("  Optimal:     %s%n", Optimal.primeFactors(n));
            System.out.printf("  Best:        %s%n", Best.primeFactors(n));
        }
    }
}
