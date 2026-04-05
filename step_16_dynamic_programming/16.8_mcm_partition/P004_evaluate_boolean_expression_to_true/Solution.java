/**
 * Problem: Evaluate Boolean Expression to True
 * Difficulty: HARD | XP: 50
 *
 * Given a boolean expression string of T/F operands and &/|/^ operators
 * (alternating, always valid), count the number of ways to parenthesize
 * it so the whole expression evaluates to True.
 *
 * This is the classic MCM/Interval-DP variant — exactly like Matrix Chain
 * Multiplication but for counting true/false evaluation trees.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Pure Recursion (no memoization)
    // Time: O(4^n * n)  |  Space: O(n) recursion stack
    // ============================================================
    static class BruteForce {
        private String expr;

        /**
         * Split the expression at every operator position k.
         * Recursively compute (trueCount, falseCount) for left and right halves.
         * Combine based on the operator:
         *   AND: true  = lt*rt;             false = lt*rf + lf*rt + lf*rf
         *   OR:  true  = lt*rt+lt*rf+lf*rt; false = lf*rf
         *   XOR: true  = lt*rf+lf*rt;       false = lt*rt+lf*rf
         * Return [trueCount, falseCount] for interval [lo, hi].
         */
        public int countWays(String expression) {
            this.expr = expression;
            int n = expression.length();
            if (n == 0) return 0;
            return recurse(0, n - 1)[0];
        }

        private long[] recurse(int lo, int hi) {
            // Base case: single operand
            if (lo == hi) {
                return expr.charAt(lo) == 'T' ? new long[]{1, 0} : new long[]{0, 1};
            }
            long totalTrue = 0, totalFalse = 0;
            for (int k = lo + 1; k < hi; k += 2) { // k is operator index
                char op = expr.charAt(k);
                long[] left  = recurse(lo, k - 1);
                long[] right = recurse(k + 1, hi);
                long lt = left[0], lf = left[1];
                long rt = right[0], rf = right[1];
                if (op == '&') {
                    totalTrue  += lt * rt;
                    totalFalse += lt * rf + lf * rt + lf * rf;
                } else if (op == '|') {
                    totalTrue  += lt * rt + lt * rf + lf * rt;
                    totalFalse += lf * rf;
                } else { // '^'
                    totalTrue  += lt * rf + lf * rt;
                    totalFalse += lt * rt + lf * rf;
                }
            }
            return new long[]{totalTrue, totalFalse};
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Memoized Recursion (Top-Down DP)
    // Time: O(n^3)  |  Space: O(n^2)
    // ============================================================
    static class Optimal {
        private String expr;
        private long[][][] memo; // memo[lo][hi][0/1] = true/false count

        /**
         * Same recursion as BruteForce, but cache results in memo[lo][hi].
         * O(n^2) unique subproblems, each takes O(n) to solve -> O(n^3) total.
         * n here = (string_length + 1) / 2 = number of operands.
         */
        public int countWays(String expression) {
            this.expr = expression;
            int n = expression.length();
            if (n == 0) return 0;
            memo = new long[n][n][2];
            // Fill with -1 to mark unvisited
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++) {
                    memo[i][j][0] = -1;
                    memo[i][j][1] = -1;
                }
            return (int) recurse(0, n - 1)[0];
        }

        private long[] recurse(int lo, int hi) {
            if (memo[lo][hi][0] != -1) {
                return new long[]{memo[lo][hi][0], memo[lo][hi][1]};
            }
            if (lo == hi) {
                long[] res = expr.charAt(lo) == 'T' ? new long[]{1, 0} : new long[]{0, 1};
                memo[lo][hi][0] = res[0];
                memo[lo][hi][1] = res[1];
                return res;
            }
            long totalTrue = 0, totalFalse = 0;
            for (int k = lo + 1; k < hi; k += 2) {
                char op = expr.charAt(k);
                long[] left  = recurse(lo, k - 1);
                long[] right = recurse(k + 1, hi);
                long lt = left[0], lf = left[1];
                long rt = right[0], rf = right[1];
                if (op == '&') {
                    totalTrue  += lt * rt;
                    totalFalse += lt * rf + lf * rt + lf * rf;
                } else if (op == '|') {
                    totalTrue  += lt * rt + lt * rf + lf * rt;
                    totalFalse += lf * rf;
                } else {
                    totalTrue  += lt * rf + lf * rt;
                    totalFalse += lt * rt + lf * rf;
                }
            }
            memo[lo][hi][0] = totalTrue;
            memo[lo][hi][1] = totalFalse;
            return new long[]{totalTrue, totalFalse};
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Bottom-Up Tabulation DP
    // Time: O(n^3)  |  Space: O(n^2)
    // ============================================================
    static class Best {
        /**
         * Bottom-up tabulation fills intervals in order of increasing length.
         *
         * dpTrue[i][j]  = ways expression[i..j] evaluates to True
         * dpFalse[i][j] = ways expression[i..j] evaluates to False
         *
         * i and j always refer to operand indices (even positions in string).
         *
         * Gap 0: single operands (base case).
         * Gap 2, 4, ...: combine left/right sub-intervals via operator at k.
         *
         * No recursion stack overhead; all transitions are O(1) per (i,j,k) triple.
         */
        public int countWays(String expression) {
            int n = expression.length();
            if (n == 0) return 0;

            long[][] dpTrue  = new long[n][n];
            long[][] dpFalse = new long[n][n];

            // Base: single operands
            for (int i = 0; i < n; i += 2) {
                if (expression.charAt(i) == 'T') {
                    dpTrue[i][i]  = 1;
                    dpFalse[i][i] = 0;
                } else {
                    dpTrue[i][i]  = 0;
                    dpFalse[i][i] = 1;
                }
            }

            // Fill for increasing gap
            for (int gap = 2; gap < n; gap += 2) {
                for (int i = 0; i + gap < n; i += 2) {
                    int j = i + gap;
                    for (int k = i + 1; k < j; k += 2) { // operator index
                        char op = expression.charAt(k);
                        long lt = dpTrue[i][k - 1],  lf = dpFalse[i][k - 1];
                        long rt = dpTrue[k + 1][j],  rf = dpFalse[k + 1][j];
                        if (op == '&') {
                            dpTrue[i][j]  += lt * rt;
                            dpFalse[i][j] += lt * rf + lf * rt + lf * rf;
                        } else if (op == '|') {
                            dpTrue[i][j]  += lt * rt + lt * rf + lf * rt;
                            dpFalse[i][j] += lf * rf;
                        } else { // '^'
                            dpTrue[i][j]  += lt * rf + lf * rt;
                            dpFalse[i][j] += lt * rt + lf * rf;
                        }
                    }
                }
            }

            return (int) dpTrue[0][n - 1];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Evaluate Boolean Expression to True ===\n");

        String[] exprs    = {"T|F", "T&F", "T^T", "T|F&T^F", "T", "F"};
        int[]    expected = {1,      0,     0,      5,          1,   0};

        BruteForce bf = new BruteForce();
        Optimal    op = new Optimal();
        Best       be = new Best();

        for (int i = 0; i < exprs.length; i++) {
            String expr = exprs[i];
            int bfRes = bf.countWays(expr);
            int opRes = op.countWays(expr);
            int beRes = be.countWays(expr);
            String status = (bfRes == opRes && opRes == beRes && beRes == expected[i]) ? "OK" : "MISMATCH";
            System.out.printf("expr=%-15s => Brute=%d, Optimal=%d, Best=%d | Expected=%d [%s]%n",
                "\"" + expr + "\"", bfRes, opRes, beRes, expected[i], status);
        }
    }
}
