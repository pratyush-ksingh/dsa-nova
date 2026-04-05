/**
 * Problem: XOR of Numbers in a Range [L, R]
 * Difficulty: MEDIUM | XP: 25
 *
 * Given two integers L and R, find the XOR of all numbers from L to R inclusive.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — XOR all numbers in range
    // Time: O(n)  where n = R - L + 1  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Simply XOR every integer from L to R.
         */
        public int xorRange(int L, int R) {
            int result = 0;
            for (int i = L; i <= R; i++) result ^= i;
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Prefix XOR pattern (O(1))
    // Time: O(1)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * XOR(L..R) = xorUpto(R) XOR xorUpto(L-1).
         *
         * xorUpto(n) = XOR of 0..n follows a 4-cycle:
         *   n%4 == 0  ->  n
         *   n%4 == 1  ->  1
         *   n%4 == 2  ->  n+1
         *   n%4 == 3  ->  0
         *
         * Derivation: XOR of any 4 consecutive integers 4k, 4k+1, 4k+2, 4k+3
         * equals 0 (the upper bits all cancel; lower 2 bits: 00^01^10^11 = 00).
         * So XOR(0..4k-1) = 0, and we only need the remainder.
         */
        private int xorUpto(int n) {
            switch (n % 4) {
                case 0: return n;
                case 1: return 1;
                case 2: return n + 1;
                default: return 0; // case 3
            }
        }

        public int xorRange(int L, int R) {
            return xorUpto(R) ^ xorUpto(L - 1);
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Same O(1) approach (lookup array style)
    // Time: O(1)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Same as Approach 2 but expresses the pattern as an array lookup
         * for compactness. Identical runtime — presented for interview clarity.
         */
        private int prefixXor(int n) {
            int[] table = {n, 1, n + 1, 0};
            return table[n % 4];
        }

        public int xorRange(int L, int R) {
            return prefixXor(R) ^ prefixXor(L - 1);
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== XOR of Numbers in a Range ===");
        int[][] tests = {{1, 5}, {3, 8}, {0, 0}, {2, 4}, {5, 5}};
        BruteForce bf = new BruteForce();
        Optimal op = new Optimal();
        Best be = new Best();

        for (int[] t : tests) {
            int L = t[0], R = t[1];
            System.out.printf("[%d,%d]  Brute=%d  Optimal=%d  Best=%d%n",
                L, R, bf.xorRange(L, R), op.xorRange(L, R), be.xorRange(L, R));
        }
    }
}
