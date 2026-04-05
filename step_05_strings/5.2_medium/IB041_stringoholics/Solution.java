import java.util.*;
import java.math.BigInteger;

/**
 * Problem: Stringoholics
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given a list of strings, find the minimum number of rotation steps
 * such that ALL strings simultaneously return to their original form.
 *
 * A string S of length L returns to original after L rotations.
 * But due to internal period (KMP failure function), the minimal
 * rotation period is: period = L - kmpFail[L-1] (if it divides L,
 * else period = L).
 *
 * The minimum rotations for all strings to simultaneously be original
 * = LCM of individual periods, but each period must be a MULTIPLE of
 * the rotation step applied. Since we rotate one step at a time,
 * each string needs period_i steps to return.
 *
 * Answer = LCM(period_1, period_2, ..., period_k) mod (10^9 + 7).
 * LCM is computed as BigInteger to avoid overflow.
 *
 * @author DSA_Nova
 */
public class Solution {

    static final long MOD = 1_000_000_007L;

    // KMP failure function
    static int[] kmpFailure(String s) {
        int n = s.length();
        int[] fail = new int[n];
        for (int i = 1; i < n; i++) {
            int j = fail[i - 1];
            while (j > 0 && s.charAt(i) != s.charAt(j)) j = fail[j - 1];
            if (s.charAt(i) == s.charAt(j)) j++;
            fail[i] = j;
        }
        return fail;
    }

    // Minimal rotation period of a string
    static int minPeriod(String s) {
        int n = s.length();
        int[] fail = kmpFailure(s);
        int period = n - fail[n - 1];
        // period divides n iff n % period == 0
        return (n % period == 0) ? period : n;
    }

    static BigInteger lcm(BigInteger a, BigInteger b) {
        return a.divide(a.gcd(b)).multiply(b);
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(sum(L_i^2))  |  Space: O(max_L)
    // For each string, simulate rotations until back to original.
    // Count steps. Then compute LCM of all counts.
    // ============================================================
    public static long bruteForce(String[] A) {
        BigInteger result = BigInteger.ONE;
        for (String s : A) {
            String rotated = s;
            int steps = 0;
            do {
                // Rotate left by 1
                rotated = rotated.substring(1) + rotated.charAt(0);
                steps++;
            } while (!rotated.equals(s));
            result = lcm(result, BigInteger.valueOf(steps));
        }
        return result.mod(BigInteger.valueOf(MOD)).longValue();
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — KMP Period + LCM
    // Time: O(sum(L_i))  |  Space: O(max_L)
    // Use KMP failure function to find minimal rotation period.
    // Compute LCM of all periods.
    // ============================================================
    public static long optimal(String[] A) {
        BigInteger result = BigInteger.ONE;
        for (String s : A) {
            int period = minPeriod(s);
            result = lcm(result, BigInteger.valueOf(period));
        }
        return result.mod(BigInteger.valueOf(MOD)).longValue();
    }

    // ============================================================
    // APPROACH 3: BEST — Same as Optimal, explicit modular LCM
    // Time: O(sum(L_i) + k * log(max_period))  |  Space: O(max_L)
    // For very large sets where BigInteger is slow, we compute
    // LCM via prime factorisation. Here we keep BigInteger for
    // correctness and just show cleaner code organisation.
    // ============================================================
    public static long best(String[] A) {
        BigInteger result = BigInteger.ONE;
        for (String s : A) {
            if (s.isEmpty()) continue;
            int n = s.length();
            int[] fail = kmpFailure(s);
            int candidate = n - fail[n - 1];
            int period = (n % candidate == 0) ? candidate : n;
            result = lcm(result, BigInteger.valueOf(period));
        }
        return result.mod(BigInteger.valueOf(MOD)).longValue();
    }

    public static void main(String[] args) {
        System.out.println("=== Stringoholics ===");
        String[][] tests = {
            {"a", "ab", "abc"},       // periods: 1,2,3 -> LCM=6
            {"abab", "ababab"},        // periods: 2,2 -> LCM=2
            {"aaa"},                   // period: 1 -> LCM=1
            {"abc", "abcabc"},         // periods: 3,3 -> LCM=3
        };
        for (String[] t : tests) {
            long bf = bruteForce(t);
            long op = optimal(t);
            long be = best(t);
            System.out.printf("A=%s -> Brute=%d, Optimal=%d, Best=%d%n",
                    Arrays.toString(t), bf, op, be);
        }
    }
}
