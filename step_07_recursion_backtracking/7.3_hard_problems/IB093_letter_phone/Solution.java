/**
 * Problem: Letter Phone
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a digit string (2-9), return all possible letter combinations
 * as a phone keypad would map (T9 style).
 * Real-life use: Predictive text, spell-checking, password generation.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static final String[] KEYS = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Iterative BFS-style: for each digit, expand every existing combination.
    // Time: O(4^N * N)  |  Space: O(4^N * N)
    // ============================================================
    static class BruteForce {
        public static List<String> letterCombinations(String digits) {
            if (digits == null || digits.isEmpty()) return new ArrayList<>();
            List<String> result = new ArrayList<>();
            result.add("");
            for (char d : digits.toCharArray()) {
                String letters = KEYS[d - '0'];
                List<String> next = new ArrayList<>();
                for (String combo : result) {
                    for (char c : letters.toCharArray()) {
                        next.add(combo + c);
                    }
                }
                result = next;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Recursive DFS backtracking with StringBuilder for O(N) string building.
    // Time: O(4^N * N)  |  Space: O(N) recursion stack + output
    // ============================================================
    static class Optimal {
        public static List<String> letterCombinations(String digits) {
            List<String> result = new ArrayList<>();
            if (digits == null || digits.isEmpty()) return result;
            backtrack(digits, 0, new StringBuilder(), result);
            return result;
        }

        private static void backtrack(String digits, int idx, StringBuilder sb, List<String> result) {
            if (idx == digits.length()) {
                result.add(sb.toString());
                return;
            }
            String letters = KEYS[digits.charAt(idx) - '0'];
            for (char c : letters.toCharArray()) {
                sb.append(c);
                backtrack(digits, idx + 1, sb, result);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Same DFS but uses char array buffer — avoids StringBuilder overhead.
    // Also handles '0' and '1' gracefully (map to empty string).
    // Time: O(4^N * N)  |  Space: O(N)
    // ============================================================
    static class Best {
        public static List<String> letterCombinations(String digits) {
            List<String> result = new ArrayList<>();
            if (digits == null || digits.isEmpty()) return result;
            char[] buf = new char[digits.length()];
            backtrack(digits, 0, buf, result);
            return result;
        }

        private static void backtrack(String digits, int idx, char[] buf, List<String> result) {
            if (idx == digits.length()) {
                result.add(new String(buf));
                return;
            }
            String letters = KEYS[digits.charAt(idx) - '0'];
            if (letters.isEmpty()) {
                backtrack(digits, idx + 1, buf, result);
                return;
            }
            for (char c : letters.toCharArray()) {
                buf[idx] = c;
                backtrack(digits, idx + 1, buf, result);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Letter Phone ===");

        String[] inputs = {"23", "2", "9", ""};
        for (String s : inputs) {
            System.out.printf("%ndigits=\"%s\"%n", s);
            System.out.println("  Brute  : " + BruteForce.letterCombinations(s));
            System.out.println("  Optimal: " + Optimal.letterCombinations(s));
            System.out.println("  Best   : " + Best.letterCombinations(s));
        }
    }
}
