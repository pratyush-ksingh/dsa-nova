/**
 * Problem: Prime Sum (Goldbach's Conjecture)
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given an even integer n >= 4, return two primes p, q such that p + q = n.
 * Goldbach's conjecture guarantees a solution always exists.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Trial Division for All Pairs
    // Time: O(n * sqrt(n))  |  Space: O(1)
    // For each i in [2, n-2], check if i and (n-i) are both prime
    // using trial division; return first valid pair.
    // ============================================================
    static class BruteForce {
        private static boolean isPrime(int num) {
            if (num < 2) return false;
            if (num == 2) return true;
            if (num % 2 == 0) return false;
            for (int i = 3; (long) i * i <= num; i += 2) {
                if (num % i == 0) return false;
            }
            return true;
        }

        public static int[] primeSum(int n) {
            for (int p = 2; p <= n - 2; p++) {
                int q = n - p;
                if (isPrime(p) && isPrime(q)) {
                    return new int[]{p, q};
                }
            }
            return new int[]{};
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Sieve of Eratosthenes + Single Scan
    // Time: O(n log log n)  |  Space: O(n)
    // Build boolean sieve up to n, then scan from 2 upward for
    // the first i where both i and (n-i) are marked prime.
    // ============================================================
    static class Optimal {
        private static boolean[] buildSieve(int limit) {
            boolean[] sieve = new boolean[limit + 1];
            Arrays.fill(sieve, true);
            sieve[0] = sieve[1] = false;
            for (int p = 2; (long) p * p <= limit; p++) {
                if (sieve[p]) {
                    for (int mult = p * p; mult <= limit; mult += p) {
                        sieve[mult] = false;
                    }
                }
            }
            return sieve;
        }

        public static int[] primeSum(int n) {
            boolean[] sieve = buildSieve(n);
            for (int p = 2; p <= n - 2; p++) {
                if (sieve[p] && sieve[n - p]) {
                    return new int[]{p, n - p};
                }
            }
            return new int[]{};
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Sieve + Half-Range Scan (No Duplicates)
    // Time: O(n log log n)  |  Space: O(n)
    // Same sieve; only scan up to n/2 since (p,q) and (q,p)
    // are the same pair. Slightly fewer iterations, same Big-O.
    // ============================================================
    static class Best {
        private static boolean[] buildSieve(int limit) {
            boolean[] sieve = new boolean[limit + 1];
            Arrays.fill(sieve, true);
            sieve[0] = sieve[1] = false;
            for (int p = 2; (long) p * p <= limit; p++) {
                if (sieve[p]) {
                    for (int mult = p * p; mult <= limit; mult += p) {
                        sieve[mult] = false;
                    }
                }
            }
            return sieve;
        }

        public static int[] primeSum(int n) {
            boolean[] sieve = buildSieve(n);
            for (int p = 2; p <= n / 2; p++) {
                int q = n - p;
                if (sieve[p] && sieve[q]) {
                    return new int[]{p, q};
                }
            }
            return new int[]{};
        }
    }

    public static void main(String[] args) {
        int[] tests = {4, 10, 28, 100};
        System.out.println("=== Prime Sum (Goldbach's Conjecture) ===");
        for (int n : tests) {
            System.out.printf("n=%-4d  Brute: %s  Optimal: %s  Best: %s%n",
                n,
                Arrays.toString(BruteForce.primeSum(n)),
                Arrays.toString(Optimal.primeSum(n)),
                Arrays.toString(Best.primeSum(n)));
        }
    }
}
