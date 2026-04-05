/**
 * Problem: Find Prime Factorization using Sieve
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Trial division
    // Time: O(sqrt(n))  |  Space: O(log n)
    // ============================================================
    public static List<int[]> bruteForce(int n) {
        // Returns list of [prime, exponent] pairs
        List<int[]> factors = new ArrayList<>();
        for (int i = 2; i * i <= n; i++) {
            int count = 0;
            while (n % i == 0) {
                count++;
                n /= i;
            }
            if (count > 0) factors.add(new int[]{i, count});
        }
        if (n > 1) factors.add(new int[]{n, 1});
        return factors;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - SPF (Smallest Prime Factor) sieve + factorize
    // Time: O(n log log n) precompute, O(log n) per query  |  Space: O(n)
    // ============================================================
    static int[] spf; // smallest prime factor

    public static void buildSPF(int limit) {
        spf = new int[limit + 1];
        for (int i = 0; i <= limit; i++) spf[i] = i;
        for (int i = 2; (long) i * i <= limit; i++) {
            if (spf[i] == i) { // i is prime
                for (int j = i * i; j <= limit; j += i) {
                    if (spf[j] == j) spf[j] = i;
                }
            }
        }
    }

    public static List<int[]> optimal(int n) {
        if (spf == null || spf.length <= n) buildSPF(n);
        List<int[]> factors = new ArrayList<>();
        while (n > 1) {
            int p = spf[n];
            int count = 0;
            while (n % p == 0) {
                count++;
                n /= p;
            }
            factors.add(new int[]{p, count});
        }
        return factors;
    }

    // ============================================================
    // APPROACH 3: BEST - SPF sieve with log-based factorization
    // Time: O(n) linear sieve precompute, O(log n) per query  |  Space: O(n)
    // ============================================================
    static int[] linearSpf;

    public static void buildLinearSieve(int limit) {
        linearSpf = new int[limit + 1];
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (linearSpf[i] == 0) {
                linearSpf[i] = i;
                primes.add(i);
            }
            for (int p : primes) {
                if (p > linearSpf[i] || (long) i * p > limit) break;
                linearSpf[i * p] = p;
            }
        }
    }

    public static List<int[]> best(int n) {
        if (linearSpf == null || linearSpf.length <= n) buildLinearSieve(n);
        List<int[]> factors = new ArrayList<>();
        while (n > 1) {
            int p = linearSpf[n];
            int count = 0;
            while (n % p == 0) {
                count++;
                n /= p;
            }
            factors.add(new int[]{p, count});
        }
        return factors;
    }

    private static String formatFactors(List<int[]> factors) {
        StringBuilder sb = new StringBuilder();
        for (int[] f : factors) {
            if (sb.length() > 0) sb.append(" * ");
            sb.append(f[0]);
            if (f[1] > 1) sb.append("^").append(f[1]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Find Prime Factorization using Sieve ===");
        int n = 360;
        System.out.println("Brute Force: " + n + " = " + formatFactors(bruteForce(n)));
        System.out.println("Optimal:     " + n + " = " + formatFactors(optimal(n)));
        System.out.println("Best:        " + n + " = " + formatFactors(best(n)));
    }
}
