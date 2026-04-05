import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(dividend/divisor)  |  Space: O(1)
// Repeatedly subtract divisor from dividend, count iterations.
// Very slow for large dividends.
// ============================================================
class BruteForce {
    public static int solve(int dividend, int divisor) {
        if (divisor == 0) throw new ArithmeticException("Division by zero");
        // Handle overflow: Integer.MIN_VALUE / -1 = Integer.MAX_VALUE (clamp)
        if (dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;

        boolean negative = (dividend < 0) ^ (divisor < 0);
        long a = Math.abs((long) dividend);
        long b = Math.abs((long) divisor);
        long count = 0;

        while (a >= b) {
            a -= b;
            count++;
        }
        long result = negative ? -count : count;
        return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, result));
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(log^2 n)  |  Space: O(1)
// Bit doubling: for each power of 2, find largest multiple of
// divisor <= remaining dividend using left shifts.
// ============================================================
class Optimal {
    public static int solve(int dividend, int divisor) {
        if (divisor == 0) throw new ArithmeticException("Division by zero");
        if (dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;

        boolean negative = (dividend < 0) ^ (divisor < 0);
        long a = Math.abs((long) dividend);
        long b = Math.abs((long) divisor);
        long result = 0;

        while (a >= b) {
            long temp = b, multiple = 1;
            while (a >= (temp << 1)) {
                temp <<= 1;
                multiple <<= 1;
            }
            a -= temp;
            result += multiple;
        }

        result = negative ? -result : result;
        return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, result));
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(log n)  |  Space: O(1)
// Iterate from highest bit (31) down to 0; build quotient bit by bit.
// Single pass through 32 bits — cleaner and bounded loop count.
// ============================================================
class Best {
    public static int solve(int dividend, int divisor) {
        if (divisor == 0) throw new ArithmeticException("Division by zero");
        if (dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;

        boolean negative = (dividend < 0) ^ (divisor < 0);
        long a = Math.abs((long) dividend);
        long b = Math.abs((long) divisor);
        long result = 0;

        for (int i = 31; i >= 0; i--) {
            if ((a >> i) >= b) {
                result += (1L << i);
                a -= b << i;
            }
        }

        result = negative ? -result : result;
        return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, result));
    }
}

public class Solution {
    public static void main(String[] args) {
        int[][] tests = {
            {10,  3,   3},
            {7,   -3, -2},
            {0,   1,   0},
            {1,   1,   1},
            {-1,  1,  -1},
            {Integer.MIN_VALUE, -1, Integer.MAX_VALUE},
            {Integer.MIN_VALUE,  1, Integer.MIN_VALUE},
            {100, 10,  10},
        };

        System.out.println("=== Divide Two Integers (No *, /, %) ===");
        for (int[] t : tests) {
            int b    = BruteForce.solve(t[0], t[1]);
            int o    = Optimal.solve(t[0], t[1]);
            int best = Best.solve(t[0], t[1]);
            String status = (b == t[2] && o == t[2] && best == t[2]) ? "PASS" : "FAIL";
            System.out.printf("%-15d / %-10d | Brute: %-12d | Optimal: %-12d | Best: %-12d | Expected: %-12d | %s%n",
                    t[0], t[1], b, o, best, t[2], status);
        }
    }
}
