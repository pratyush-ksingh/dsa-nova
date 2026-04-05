/**
 * Problem: Valid Parenthesis String (LeetCode 678)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a string containing '(', ')' and '*', where '*' can be '(', ')'
 * or empty, determine if the string is valid.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Recursion -- Try All Options)
// Time: O(3^m) where m = number of *s | Space: O(n) stack
// ============================================================
class BruteForce {
    public static boolean solve(String s) {
        return helper(s, 0, 0);
    }

    private static boolean helper(String s, int idx, int openCount) {
        if (openCount < 0) return false;
        if (idx == s.length()) return openCount == 0;

        char ch = s.charAt(idx);
        if (ch == '(') {
            return helper(s, idx + 1, openCount + 1);
        } else if (ch == ')') {
            return helper(s, idx + 1, openCount - 1);
        } else { // '*'
            return helper(s, idx + 1, openCount + 1)  // as '('
                || helper(s, idx + 1, openCount - 1)   // as ')'
                || helper(s, idx + 1, openCount);       // as empty
        }
    }
}

// ============================================================
// Approach 2: Optimal (Track Min/Max Open Count)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static boolean solve(String s) {
        int lo = 0, hi = 0;

        for (char ch : s.toCharArray()) {
            if (ch == '(') {
                lo++;
                hi++;
            } else if (ch == ')') {
                lo--;
                hi--;
            } else { // '*'
                lo--;
                hi++;
            }

            if (hi < 0) return false;
            lo = Math.max(lo, 0);
        }

        return lo == 0;
    }
}

// ============================================================
// Approach 3: Best (Same Two-Counter -- Cleanest Form)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static boolean solve(String s) {
        int minOpen = 0, maxOpen = 0;

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                minOpen++;
                maxOpen++;
            } else if (ch == ')') {
                minOpen--;
                maxOpen--;
            } else {
                minOpen--;
                maxOpen++;
            }

            if (maxOpen < 0) return false;
            minOpen = Math.max(minOpen, 0);
        }

        return minOpen == 0;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Valid Parenthesis String ===\n");

        Object[][] tests = {
            {"()", true},
            {"(*)", true},
            {"(*))", true},
            {"(((*)", false},
            {"", true},
            {"*", true},
            {"(((******))", true},
            {")(", false},
        };

        for (Object[] t : tests) {
            String s = (String) t[0];
            boolean expected = (boolean) t[1];

            boolean b = BruteForce.solve(s);
            boolean o = Optimal.solve(s);
            boolean h = Best.solve(s);
            boolean pass = b == expected && o == expected && h == expected;

            System.out.println("Input:    \"" + s + "\"");
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + h);
            System.out.println("Expected: " + expected);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
