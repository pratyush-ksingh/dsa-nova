/**
 * Problem: Generate Parentheses (LeetCode #22)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given n pairs of parentheses, generate all combinations of well-formed parentheses.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Generate All & Filter)
// Time: O(2^(2n) * n) | Space: O(2^(2n) * n)
// ============================================================
class BruteForce {
    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        generateAll(new char[2 * n], 0, result);
        return result;
    }

    private static void generateAll(char[] current, int pos, List<String> result) {
        if (pos == current.length) {
            if (isValid(current)) {
                result.add(new String(current));
            }
            return;
        }
        current[pos] = '(';
        generateAll(current, pos + 1, result);
        current[pos] = ')';
        generateAll(current, pos + 1, result);
    }

    private static boolean isValid(char[] current) {
        int balance = 0;
        for (char c : current) {
            if (c == '(') balance++;
            else balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }
}

// ============================================================
// Approach 2: Optimal (Backtracking with Open/Close Counts)
// Time: O(4^n / sqrt(n)) | Space: O(n) recursion depth
// ============================================================
class Optimal {
    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, new StringBuilder(), 0, 0, n);
        return result;
    }

    private static void backtrack(List<String> result, StringBuilder sb,
                                  int open, int close, int n) {
        if (sb.length() == 2 * n) {
            result.add(sb.toString());
            return;
        }
        if (open < n) {
            sb.append('(');
            backtrack(result, sb, open + 1, close, n);
            sb.deleteCharAt(sb.length() - 1);
        }
        if (close < open) {
            sb.append(')');
            backtrack(result, sb, open, close + 1, n);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}

// ============================================================
// Approach 3: Best (Same Backtracking -- String Concat Variant)
// Time: O(4^n / sqrt(n)) | Space: O(n) recursion depth
// ============================================================
class Best {
    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, "", 0, 0, n);
        return result;
    }

    private static void backtrack(List<String> result, String current,
                                  int open, int close, int n) {
        if (current.length() == 2 * n) {
            result.add(current);
            return;
        }
        if (open < n) {
            backtrack(result, current + "(", open + 1, close, n);
        }
        if (close < open) {
            backtrack(result, current + ")", open, close + 1, n);
        }
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Generate Parentheses ===\n");

        int[] inputs = {1, 2, 3};
        for (int n : inputs) {
            List<String> b = BruteForce.generateParenthesis(n);
            List<String> o = Optimal.generateParenthesis(n);
            List<String> r = Best.generateParenthesis(n);

            System.out.println("n = " + n);
            System.out.println("  Brute:   " + b);
            System.out.println("  Optimal: " + o);
            System.out.println("  Best:    " + r);
            System.out.println();
        }
    }
}
