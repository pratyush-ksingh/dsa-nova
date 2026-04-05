/**
 * Problem: Roman to Integer (LeetCode #13)
 * Difficulty: EASY | XP: 10
 *
 * Convert a Roman numeral string to an integer.
 *
 * @author DSA_Nova
 */
import java.util.HashMap;
import java.util.Map;

// ============================================================
// Approach 1: Brute Force (Handle All 13 Cases Explicitly)
// Time: O(n) | Space: O(1)
// ============================================================
class BruteForce {
    public static int romanToInt(String s) {
        int result = 0;
        int i = 0;
        while (i < s.length()) {
            // Check two-character subtractive patterns first
            if (i + 1 < s.length()) {
                String two = s.substring(i, i + 2);
                if (two.equals("CM")) { result += 900; i += 2; continue; }
                if (two.equals("CD")) { result += 400; i += 2; continue; }
                if (two.equals("XC")) { result += 90;  i += 2; continue; }
                if (two.equals("XL")) { result += 40;  i += 2; continue; }
                if (two.equals("IX")) { result += 9;   i += 2; continue; }
                if (two.equals("IV")) { result += 4;   i += 2; continue; }
            }
            // Single character
            char c = s.charAt(i);
            if (c == 'M') result += 1000;
            else if (c == 'D') result += 500;
            else if (c == 'C') result += 100;
            else if (c == 'L') result += 50;
            else if (c == 'X') result += 10;
            else if (c == 'V') result += 5;
            else if (c == 'I') result += 1;
            i++;
        }
        return result;
    }
}

// ============================================================
// Approach 2: Optimal (Subtraction Rule with Lookahead)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    private static final Map<Character, Integer> MAP = new HashMap<>();
    static {
        MAP.put('I', 1);    MAP.put('V', 5);
        MAP.put('X', 10);   MAP.put('L', 50);
        MAP.put('C', 100);  MAP.put('D', 500);
        MAP.put('M', 1000);
    }

    public static int romanToInt(String s) {
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            int curr = MAP.get(s.charAt(i));
            int next = (i + 1 < s.length()) ? MAP.get(s.charAt(i + 1)) : 0;
            if (curr < next) {
                result -= curr;  // subtractive case
            } else {
                result += curr;
            }
        }
        return result;
    }
}

// ============================================================
// Approach 3: Right-to-Left Scan
// Time: O(n) | Space: O(1)
// ============================================================
class RightToLeft {
    private static int valueOf(char c) {
        switch (c) {
            case 'I': return 1;    case 'V': return 5;
            case 'X': return 10;   case 'L': return 50;
            case 'C': return 100;  case 'D': return 500;
            case 'M': return 1000; default:  return 0;
        }
    }

    public static int romanToInt(String s) {
        int result = valueOf(s.charAt(s.length() - 1));
        for (int i = s.length() - 2; i >= 0; i--) {
            int curr = valueOf(s.charAt(i));
            int next = valueOf(s.charAt(i + 1));
            if (curr < next) {
                result -= curr;
            } else {
                result += curr;
            }
        }
        return result;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Roman to Integer ===\n");

        String[] testCases = {"III", "LVIII", "MCMXCIV", "IV", "IX", "V", "MMMCMXCIX", "I", "XIV"};
        int[] expected =     {3,     58,      1994,       4,    9,    5,   3999,        1,   14};

        for (int t = 0; t < testCases.length; t++) {
            String s = testCases[t];
            int bruteResult = BruteForce.romanToInt(s);
            int optimalResult = Optimal.romanToInt(s);
            int rtlResult = RightToLeft.romanToInt(s);

            System.out.println("Input:      \"" + s + "\"");
            System.out.println("  Brute:      " + bruteResult);
            System.out.println("  Optimal:    " + optimalResult);
            System.out.println("  RightToLeft:" + rtlResult);
            System.out.println("  Expected:   " + expected[t]);
            System.out.println("  Pass:       " + (bruteResult == expected[t]
                    && optimalResult == expected[t] && rtlResult == expected[t]));
            System.out.println();
        }
    }
}
