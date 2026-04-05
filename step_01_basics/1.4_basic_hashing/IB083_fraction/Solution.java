import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(denominator)  |  Space: O(denominator)
// Long division simulation — detect cycle via seen remainders
// (same as optimal but written more verbosely for clarity)
// ============================================================
class BruteForce {
    public static String solve(int numerator, int denominator) {
        if (denominator == 0) return "undefined";
        StringBuilder result = new StringBuilder();

        // Handle sign
        long num = numerator, den = denominator;
        if ((num < 0) ^ (den < 0)) result.append('-');
        num = Math.abs(num);
        den = Math.abs(den);

        result.append(num / den);
        long remainder = num % den;
        if (remainder == 0) return result.toString();

        result.append('.');
        Map<Long, Integer> seen = new HashMap<>();
        StringBuilder fracPart = new StringBuilder();

        while (remainder != 0) {
            if (seen.containsKey(remainder)) {
                int pos = seen.get(remainder);
                fracPart.insert(pos, '(');
                fracPart.append(')');
                break;
            }
            seen.put(remainder, fracPart.length());
            remainder *= 10;
            fracPart.append(remainder / den);
            remainder %= den;
        }
        result.append(fracPart);
        return result.toString();
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(denominator)  |  Space: O(denominator)
// Long division with HashMap tracking (remainder -> position)
// to detect the repeating cycle start
// ============================================================
class Optimal {
    public static String solve(int numerator, int denominator) {
        StringBuilder sb = new StringBuilder();
        long num = numerator, den = denominator;

        // Handle negative
        if ((num < 0) ^ (den < 0)) sb.append('-');
        num = Math.abs(num);
        den = Math.abs(den);

        sb.append(num / den);
        long rem = num % den;
        if (rem == 0) return sb.toString();

        sb.append('.');
        Map<Long, Integer> map = new HashMap<>();

        while (rem != 0) {
            if (map.containsKey(rem)) {
                sb.insert(map.get(rem), '(');
                sb.append(')');
                return sb.toString();
            }
            map.put(rem, sb.length());
            rem *= 10;
            sb.append(rem / den);
            rem %= den;
        }
        return sb.toString();
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(denominator)  |  Space: O(denominator)
// Same algorithm but avoids repeated string insertions by
// building integer part, fractional part separately, then
// inserting the parenthesis at the detected position.
// ============================================================
class Best {
    public static String solve(int numerator, int denominator) {
        long num = numerator, den = denominator;
        boolean negative = (num < 0) ^ (den < 0);
        num = Math.abs(num); den = Math.abs(den);

        String intPart = String.valueOf(num / den);
        long rem = num % den;
        if (rem == 0) return (negative ? "-" : "") + intPart;

        StringBuilder frac = new StringBuilder();
        Map<Long, Integer> pos = new HashMap<>();

        int cycleStart = -1;
        while (rem != 0) {
            if (pos.containsKey(rem)) { cycleStart = pos.get(rem); break; }
            pos.put(rem, frac.length());
            rem *= 10;
            frac.append(rem / den);
            rem %= den;
        }

        String fracStr;
        if (cycleStart >= 0) {
            fracStr = frac.substring(0, cycleStart) + "(" + frac.substring(cycleStart) + ")";
        } else {
            fracStr = frac.toString();
        }
        return (negative ? "-" : "") + intPart + "." + fracStr;
    }
}

public class Solution {
    public static void main(String[] args) {
        int[][] cases = {{1, 2}, {2, 1}, {2, 3}, {1, 6}, {-1, 6}, {1, 7}, {-50, 8}, {7, 12}};
        String[] expected = {"0.5", "2", "0.(6)", "0.1(6)", "-0.1(6)", "0.(142857)", "-6.25", "0.58(3)"};
        System.out.println("=== Fraction to Recurring Decimal ===");
        for (int i = 0; i < cases.length; i++) {
            String b = BruteForce.solve(cases[i][0], cases[i][1]);
            String o = Optimal.solve(cases[i][0], cases[i][1]);
            String bst = Best.solve(cases[i][0], cases[i][1]);
            System.out.printf("%d/%d => BF=%s  OPT=%s  BEST=%s  EXP=%s%n",
                cases[i][0], cases[i][1], b, o, bst, expected[i]);
        }
    }
}
