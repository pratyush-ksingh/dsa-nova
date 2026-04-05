/**
 * Problem: Sorted Permutation Rank
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a string A, find the rank of the string among all its permutations
 * sorted lexicographically. Return rank modulo (10^6 + 3).
 *
 * For each position i, the number of permutations starting with a character
 * smaller than A[i] (using remaining characters) comes before A.
 * rank = 1 + sum over i of: (smaller_count * suffix_fact / freq_product)
 *
 * @author DSA_Nova
 */
public class Solution {

    static final long MOD = 1_000_003L; // 10^6 + 3 (prime, enables Fermat inverse)

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n! * n)  |  Space: O(n!)
    // ============================================================
    static class BruteForce {
        /**
         * Generate all unique permutations (TreeSet for sorted order),
         * then find the 1-based index of A.
         * Only feasible for n <= 8.
         */
        public static long solve(String A) {
            char[] chars = A.toCharArray();
            java.util.TreeSet<String> perms = new java.util.TreeSet<>();
            generatePerms(chars, 0, perms);
            int rank = 0;
            for (String p : perms) {
                rank++;
                if (p.equals(A)) return rank % MOD;
            }
            return -1;
        }

        private static void generatePerms(char[] arr, int start,
                                          java.util.TreeSet<String> result) {
            if (start == arr.length) { result.add(new String(arr)); return; }
            for (int i = start; i < arr.length; i++) {
                char tmp = arr[start]; arr[start] = arr[i]; arr[i] = tmp;
                generatePerms(arr, start + 1, result);
                tmp = arr[start]; arr[start] = arr[i]; arr[i] = tmp;
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Position-by-position counting O(n^2)
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    static class Optimal {
        /**
         * For each position i:
         *   1. Count remaining chars smaller than A[i]: 'smaller'
         *   2. Contribution = smaller * (n-i-1)! / product(freq[c]!)
         * Modular inverse via Fermat's little theorem (MOD is prime).
         */
        public static long solve(String A) {
            int n = A.length();
            long[] fact = buildFactorial(n);
            int[] freq = new int[26];
            for (char c : A.toCharArray()) freq[c - 'a']++;

            long rank = 1;
            for (int i = 0; i < n; i++) {
                int ci = A.charAt(i) - 'a';

                long smaller = 0;
                for (int x = 0; x < ci; x++) smaller += freq[x];

                long suffixFact = fact[n - i - 1];
                long denom = 1;
                for (int x = 0; x < 26; x++) denom = denom * fact[freq[x]] % MOD;
                long invDenom = modPow(denom, MOD - 2, MOD);

                rank = (rank + (smaller % MOD) * suffixFact % MOD * invDenom % MOD) % MOD;
                freq[ci]--;
            }
            return rank;
        }

        static long[] buildFactorial(int n) {
            long[] fact = new long[n + 1];
            fact[0] = 1;
            for (int i = 1; i <= n; i++) fact[i] = fact[i - 1] * i % MOD;
            return fact;
        }

        static long modPow(long base, long exp, long mod) {
            long result = 1; base %= mod;
            while (exp > 0) {
                if ((exp & 1) == 1) result = result * base % mod;
                base = base * base % mod;
                exp >>= 1;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Fenwick Tree for counting, O(n log n)
    // Time: O(n log n)  |  Space: O(26)
    // ============================================================
    static class Best {
        /**
         * Use a BIT (Fenwick Tree) over the 26-char alphabet to count
         * remaining characters smaller than current in O(log 26) per step.
         * Modular inverse computed via Fermat's little theorem.
         */
        static int[] bit = new int[27];

        public static long solve(String A) {
            int n = A.length();
            long[] fact = Optimal.buildFactorial(n);
            int[] freq = new int[26];
            bit = new int[27];

            for (char c : A.toCharArray()) {
                freq[c - 'a']++;
                update(c - 'a', 1);
            }

            long rank = 1;
            for (int i = 0; i < n; i++) {
                int ci = A.charAt(i) - 'a';
                long smaller = ci > 0 ? query(ci - 1) : 0;

                long suffixFact = fact[n - i - 1];
                long denom = 1;
                for (int x = 0; x < 26; x++) denom = denom * fact[freq[x]] % MOD;
                long invDenom = Optimal.modPow(denom, MOD - 2, MOD);

                rank = (rank + (smaller % MOD) * suffixFact % MOD * invDenom % MOD) % MOD;
                freq[ci]--;
                update(ci, -1);
            }
            return rank;
        }

        private static void update(int pos, int delta) {
            for (pos++; pos < 27; pos += pos & -pos) bit[pos] += delta;
        }

        private static int query(int pos) {
            int s = 0;
            for (pos++; pos > 0; pos -= pos & -pos) s += bit[pos];
            return s;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Sorted Permutation Rank ===");
        String[] tests = {"ABC", "BAC", "ACB", "CBA", "ABCD"};
        for (String s : tests) {
            long brute = s.length() <= 8 ? BruteForce.solve(s) : -1;
            System.out.printf("'%s': Brute=%d, Optimal=%d, Best=%d%n",
                s, brute, Optimal.solve(s), Best.solve(s));
        }
    }
}
