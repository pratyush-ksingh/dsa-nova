import java.util.*;
import java.math.BigInteger;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(log n)  |  Space: O(n)
// Parse BigInteger; repeatedly divide by 2 until 1 or odd remainder.
// ============================================================
class BruteForce {
    public static int solve(String A) {
        BigInteger n;
        try { n = new BigInteger(A); } catch (NumberFormatException e) { return 0; }
        if (n.compareTo(BigInteger.ZERO) <= 0) return 0;
        BigInteger two = BigInteger.valueOf(2);
        while (n.compareTo(BigInteger.ONE) > 0) {
            if (!n.mod(two).equals(BigInteger.ZERO)) return 0;
            n = n.divide(two);
        }
        return 1;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(n)
// A power of 2 has exactly one bit set: BigInteger.bitCount() == 1.
// ============================================================
class Optimal {
    public static int solve(String A) {
        BigInteger n;
        try { n = new BigInteger(A); } catch (NumberFormatException e) { return 0; }
        if (n.compareTo(BigInteger.ZERO) <= 0) return 0;
        return (n.bitCount() == 1) ? 1 : 0;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(n)
// n & (n-1) == 0 iff n is a power of 2.  Fastest BigInteger check.
// ============================================================
class Best {
    public static int solve(String A) {
        BigInteger n;
        try { n = new BigInteger(A); } catch (NumberFormatException e) { return 0; }
        if (n.compareTo(BigInteger.ONE) < 0) return 0;
        return n.and(n.subtract(BigInteger.ONE)).equals(BigInteger.ZERO) ? 1 : 0;
    }
}

public class Solution {
    public static void main(String[] args) {
        String[][] tests = {
            {"1",          "1"},   // 2^0
            {"2",          "1"},   // 2^1
            {"4",          "1"},
            {"1000",       "0"},
            {"1024",       "1"},   // 2^10
            {"3",          "0"},
            {"0",          "0"},
            {"2147483648", "1"},   // 2^31
            {"9999999999999999999999999999998", "0"}
        };

        System.out.println("=== Power of 2 ===");
        for (String[] t : tests) {
            int b    = BruteForce.solve(t[0]);
            int o    = Optimal.solve(t[0]);
            int best = Best.solve(t[0]);
            int exp  = Integer.parseInt(t[1]);
            String status = (b == exp && o == exp && best == exp) ? "PASS" : "FAIL";
            System.out.printf("Input: %-35s | Brute: %d | Optimal: %d | Best: %d | Expected: %d | %s%n",
                    t[0], b, o, best, exp, status);
        }
    }
}
