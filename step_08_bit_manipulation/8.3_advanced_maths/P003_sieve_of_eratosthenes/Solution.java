/**
 * Problem: Sieve of Eratosthenes
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Trial division for each number
    // Time: O(n * sqrt(n))  |  Space: O(n)
    // ============================================================
    public static List<Integer> bruteForce(int n) {
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) primes.add(i);
        }
        return primes;
    }

    private static boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Classic Sieve of Eratosthenes
    // Time: O(n log log n)  |  Space: O(n)
    // ============================================================
    public static List<Integer> optimal(int n) {
        boolean[] isComposite = new boolean[n + 1];
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (!isComposite[i]) {
                primes.add(i);
                for (long j = (long) i * i; j <= n; j += i) {
                    isComposite[(int) j] = true;
                }
            }
        }
        return primes;
    }

    // ============================================================
    // APPROACH 3: BEST - Segmented sieve (bitwise optimized)
    // Time: O(n log log n)  |  Space: O(sqrt(n))
    // ============================================================
    public static List<Integer> best(int n) {
        if (n < 2) return new ArrayList<>();
        int limit = (int) Math.sqrt(n) + 1;
        // Step 1: small primes via simple sieve
        boolean[] isComposite = new boolean[limit + 1];
        List<Integer> smallPrimes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (!isComposite[i]) {
                smallPrimes.add(i);
                for (int j = i * i; j <= limit; j += i) {
                    isComposite[j] = true;
                }
            }
        }
        // Step 2: segmented sieve
        List<Integer> primes = new ArrayList<>(smallPrimes);
        int segSize = Math.max(limit, 1);
        for (int low = limit + 1; low <= n; low += segSize) {
            int high = Math.min(low + segSize - 1, n);
            boolean[] seg = new boolean[high - low + 1];
            for (int p : smallPrimes) {
                int start = ((low + p - 1) / p) * p;
                if (start == p) start += p; // skip p itself
                for (int j = start; j <= high; j += p) {
                    seg[j - low] = true;
                }
            }
            for (int i = 0; i < seg.length; i++) {
                if (!seg[i]) primes.add(low + i);
            }
        }
        return primes;
    }

    public static void main(String[] args) {
        System.out.println("=== Sieve of Eratosthenes ===");
        int n = 50;
        System.out.println("Brute Force: " + bruteForce(n));
        System.out.println("Optimal:     " + optimal(n));
        System.out.println("Best:        " + best(n));
    }
}
