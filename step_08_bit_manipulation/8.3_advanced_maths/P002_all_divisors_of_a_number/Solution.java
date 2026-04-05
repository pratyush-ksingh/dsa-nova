import java.util.*;

/**
 * Problem: All Divisors of a Number
 * Difficulty: EASY | XP: 10
 *
 * Given a positive integer N, print all divisors of N in sorted order.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check Every Number 1 to N
    // Time: O(N)  |  Space: O(D) where D = number of divisors
    // Iterate from 1 to N, check if each divides N.
    // ============================================================
    static class BruteForce {
        public static List<Integer> allDivisors(int n) {
            List<Integer> divisors = new ArrayList<>();
            for (int d = 1; d <= n; d++) {
                if (n % d == 0) {
                    divisors.add(d);
                }
            }
            return divisors;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- sqrt(N) Pair Collection + Sort
    // Time: O(sqrt(N) + D log D)  |  Space: O(D)
    // For each d up to sqrt(N), collect d and N/d, then sort.
    // ============================================================
    static class Optimal {
        public static List<Integer> allDivisors(int n) {
            List<Integer> divisors = new ArrayList<>();
            for (int d = 1; (long) d * d <= n; d++) {
                if (n % d == 0) {
                    divisors.add(d);
                    if (d != n / d) {
                        divisors.add(n / d);
                    }
                }
            }
            Collections.sort(divisors);
            return divisors;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- sqrt(N) Two-List Trick (No Sort)
    // Time: O(sqrt(N))  |  Space: O(D)
    // Collect small divisors ascending, large divisors descending,
    // then concatenate for a sorted result without sorting.
    // ============================================================
    static class Best {
        public static List<Integer> allDivisors(int n) {
            List<Integer> small = new ArrayList<>();
            List<Integer> large = new ArrayList<>();

            for (int d = 1; (long) d * d <= n; d++) {
                if (n % d == 0) {
                    small.add(d);
                    if (d != n / d) {
                        large.add(n / d);
                    }
                }
            }

            // large is in descending order, reverse it
            Collections.reverse(large);
            small.addAll(large);
            return small;
        }
    }

    public static void main(String[] args) {
        int[] tests = {36, 1, 12, 7, 100};

        System.out.println("=== All Divisors of a Number ===");
        for (int n : tests) {
            System.out.println("\nN = " + n);
            System.out.println("  Brute Force: " + BruteForce.allDivisors(n));
            System.out.println("  Optimal:     " + Optimal.allDivisors(n));
            System.out.println("  Best:        " + Best.allDivisors(n));
        }
    }
}
