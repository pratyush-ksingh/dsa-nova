import java.util.*;
import java.math.BigInteger;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n * m)  |  Space: O(n + m)
// Use BigInteger to parse and multiply — shows correctness baseline.
// ============================================================
class BruteForce {
    public static String solve(String num1, String num2) {
        return new BigInteger(num1).multiply(new BigInteger(num2)).toString();
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n * m)  |  Space: O(n + m)
// Grade-school multiplication with a position array.
// Product of n-digit and m-digit number fits in n+m digits.
// ============================================================
class Optimal {
    public static String solve(String num1, String num2) {
        int n = num1.length(), m = num2.length();
        int[] result = new int[n + m];

        for (int i = n - 1; i >= 0; i--) {
            for (int j = m - 1; j >= 0; j--) {
                int mul  = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int p1   = i + j, p2 = i + j + 1;
                int sum  = mul + result[p2];
                result[p2] = sum % 10;
                result[p1] += sum / 10;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int digit : result) {
            if (!(sb.length() == 0 && digit == 0)) sb.append(digit);
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n * m)  |  Space: O(n + m)
// Same digit-array approach with an early exit for zero inputs.
// Grade-school is the standard O(n*m) interview solution;
// Karatsuba/FFT are out of scope for interview settings.
// ============================================================
class Best {
    public static String solve(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) return "0";
        int n = num1.length(), m = num2.length();
        int[] pos = new int[n + m];

        for (int i = n - 1; i >= 0; i--) {
            int a = num1.charAt(i) - '0';
            for (int j = m - 1; j >= 0; j--) {
                int mul = a * (num2.charAt(j) - '0') + pos[i + j + 1];
                pos[i + j + 1] = mul % 10;
                pos[i + j]    += mul / 10;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int p : pos) {
            if (!(sb.length() == 0 && p == 0)) sb.append(p);
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }
}

public class Solution {
    public static void main(String[] args) {
        String[][] tests = {
            {"2",         "3",         "6"},
            {"123",       "456",       "56088"},
            {"0",         "12345",     "0"},
            {"999",       "999",       "998001"},
            {"9",         "9",         "81"},
            {"99",        "99",        "9801"},
            {"123456789", "987654321", "121932631112635269"}
        };

        System.out.println("=== Multiply Strings ===");
        for (String[] t : tests) {
            String b    = BruteForce.solve(t[0], t[1]);
            String o    = Optimal.solve(t[0], t[1]);
            String best = Best.solve(t[0], t[1]);
            String status = (b.equals(t[2]) && o.equals(t[2]) && best.equals(t[2])) ? "PASS" : "FAIL";
            System.out.printf("%-12s * %-12s = %-25s (expected %-25s) | %s%n",
                    t[0], t[1], best, t[2], status);
        }
    }
}
