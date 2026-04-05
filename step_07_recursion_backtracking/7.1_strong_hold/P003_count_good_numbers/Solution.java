/**
 * Problem: Count Good Numbers (LeetCode #1922)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    private static final long MOD = 1_000_000_007L;

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion (exponential, tiny n only)
    // Time: O(5^(n/2) * 4^(n/2))  |  Space: O(n) stack
    //
    // At each position decide: even index -> 5 choices, odd -> 4.
    // Multiply choices recursively. Only practical for n <= 15.
    // ============================================================
    static class BruteForce {
        public long countGoodNumbers(long n) {
            return recurse(0, n);
        }

        private long recurse(long pos, long n) {
            if (pos == n) return 1;
            long choices = (pos % 2 == 0) ? 5 : 4;
            return (choices * recurse(pos + 1, n)) % MOD;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Math + built-in modular exponentiation
    // Time: O(log n)  |  Space: O(1)
    //
    // evenCount = ceil(n/2) positions with 5 choices (0,2,4,6,8)
    // oddCount  = floor(n/2) positions with 4 choices (2,3,5,7)
    // Answer = 5^evenCount * 4^oddCount  (mod 10^9+7)
    // ============================================================
    static class Optimal {
        public long countGoodNumbers(long n) {
            long evenCount = (n + 1) / 2;
            long oddCount  = n / 2;
            return modPow(5, evenCount, MOD) * modPow(4, oddCount, MOD) % MOD;
        }

        private long modPow(long base, long exp, long mod) {
            long result = 1;
            base %= mod;
            while (exp > 0) {
                if ((exp & 1) == 1) result = result * base % mod;
                base = base * base % mod;
                exp >>= 1;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same formula, explicit fast-power helper
    // Time: O(log n)  |  Space: O(1)
    //
    // Identical to Optimal; helper is shown explicitly for clarity
    // and portability to languages without built-in modpow.
    // ============================================================
    static class Best {
        public long countGoodNumbers(long n) {
            long evenCount = (n + 1) / 2;
            long oddCount  = n / 2;
            return fastPow(5, evenCount) * fastPow(4, oddCount) % MOD;
        }

        private long fastPow(long base, long exp) {
            long result = 1;
            base %= MOD;
            while (exp > 0) {
                if ((exp & 1L) == 1L) result = result * base % MOD;
                base = base * base % MOD;
                exp >>>= 1;
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Count Good Numbers ===\n");

        long[] ns  = {1,  4,   50,        3,   2 };
        long[] exp = {5, 400, 564908303L, 100, 20};

        for (int t = 0; t < ns.length; t++) {
            long n = ns[t];
            // Brute only for small n to avoid stack overflow
            long b  = (n <= 20) ? new BruteForce().countGoodNumbers(n) : -1L;
            long o  = new Optimal().countGoodNumbers(n);
            long bt = new Best().countGoodNumbers(n);
            boolean ok = (o == exp[t] && bt == exp[t]);
            System.out.printf("n=%-12d  Expected=%-12d  Brute=%-12s  Optimal=%-12d  Best=%-12d  [%s]%n",
                    n, exp[t], (b == -1 ? "SKIPPED" : String.valueOf(b)), o, bt, ok ? "OK" : "MISMATCH");
        }
    }
}
