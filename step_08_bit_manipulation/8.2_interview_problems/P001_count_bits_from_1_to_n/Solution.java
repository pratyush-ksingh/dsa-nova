/**
 * Problem: Count Total Set Bits from 1 to N
 * Difficulty: MEDIUM | XP: 25
 *
 * Count the total number of set bits (1s) in the binary representations
 * of all integers from 1 to N (inclusive).
 * E.g., N=3: 1(1) + 2(1) + 3(2) = 4 total set bits.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n * log n)  |  Space: O(1)
    //
    // For each number i from 1 to N, count its set bits using
    // Brian Kernighan's algorithm (clear lowest set bit repeatedly).
    // Sum all counts.
    // ============================================================
    public static long bruteForce(int n) {
        long total = 0;
        for (int i = 1; i <= n; i++) {
            int x = i;
            while (x != 0) {
                total++;
                x &= (x - 1); // clear lowest set bit
            }
        }
        return total;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(log^2 n)  |  Space: O(log n) recursive stack
    //
    // Use the formula: for N = 2^k + r (0 <= r < 2^k):
    //   total(N) = k * 2^(k-1)   <- set bits in 1..2^k-1
    //            + (r + 1)        <- MSB is set for 2^k..N
    //            + total(r)       <- lower bits in 2^k..N mirror 1..r
    // ============================================================
    public static long optimal(int n) {
        if (n <= 0) return 0;

        int k = 31 - Integer.numberOfLeadingZeros(n); // floor(log2(n))
        long power = 1L << k;

        if (n == power - 1) {
            // Exact 2^k - 1: formula k * 2^(k-1)
            return (long) k * (power / 2);
        }

        long r = n - power;
        long fullBlock = (long) k * (power / 2);  // bits in 1..2^k-1
        long msbContrib = r + 1;                   // bit k set for 2^k..n
        long lowerBits = optimal((int) r);          // mirrors 0..r

        return fullBlock + msbContrib + lowerBits;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(log n)  |  Space: O(1)
    //
    // Iterative version of the same formula. Loop: peel off the
    // highest bit of n each iteration, accumulate contributions,
    // then continue with the remainder. No recursion overhead.
    // ============================================================
    public static long best(int n) {
        long total = 0;
        while (n > 0) {
            int k = 31 - Integer.numberOfLeadingZeros(n); // floor(log2(n))
            long power = 1L << k;
            long r = n - power;

            // Full block 1..2^k - 1: k * 2^(k-1) set bits
            total += (long) k * (power / 2);
            // MSB column: numbers power..n have bit k set, that's r+1 numbers
            total += r + 1;
            // Remainder mirrors 0..r; continue loop with n = r
            n = (int) r;
        }
        return total;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Count Total Set Bits from 1 to N ===\n");

        int[][] tests = {
            {1, 1}, {2, 2}, {3, 4}, {4, 5}, {5, 7},
            {6, 9}, {7, 12}, {8, 13}, {17, 35}, {100, 319}
        };

        for (int[] test : tests) {
            int n = test[0];
            long expected = test[1];
            long b = bruteForce(n);
            long o = optimal(n);
            long r = best(n);
            String status = (b == expected && o == expected && r == expected) ? "PASS" : "FAIL";
            System.out.printf("N=%-5d Expected=%-5d Brute=%-5d Optimal=%-5d Best=%-5d [%s]%n",
                n, expected, b, o, r, status);
        }
    }
}
