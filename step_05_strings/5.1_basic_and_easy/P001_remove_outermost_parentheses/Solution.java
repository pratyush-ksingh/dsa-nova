/**
 * Problem: Remove Outermost Parentheses (LeetCode #1021)
 * Difficulty: EASY | XP: 10
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Stack-Based Primitive Extraction
    // Time: O(n)  |  Space: O(n)
    //
    // Use a stack to locate each primitive, then slice off the
    // first and last character of each primitive substring.
    // ============================================================
    static class BruteForce {
        public String removeOuterParentheses(String s) {
            java.util.Stack<Integer> stack = new java.util.Stack<>();
            StringBuilder result = new StringBuilder();
            int start = 0;

            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(') {
                    stack.push(i);
                } else {
                    stack.pop();
                    if (stack.isEmpty()) {
                        // Found a complete primitive from start to i (inclusive)
                        // Strip the outermost: take substring(start+1, i)
                        result.append(s, start + 1, i);
                        start = i + 1;
                    }
                }
            }
            return result.toString();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Depth Counter with Conditional Append
    // Time: O(n)  |  Space: O(1) extra
    //
    // Track depth with an integer. The outermost '(' raises depth
    // from 0->1 and the outermost ')' drops it from 1->0. Skip
    // those characters; append everything else.
    // ============================================================
    static class Optimal {
        public String removeOuterParentheses(String s) {
            StringBuilder result = new StringBuilder();
            int depth = 0;

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '(') {
                    depth++;
                    if (depth > 1) {
                        result.append(c);
                    }
                } else {
                    if (depth > 1) {
                        result.append(c);
                    }
                    depth--;
                }
            }
            return result.toString();
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same as Optimal (provably optimal)
    // Time: O(n)  |  Space: O(1) extra
    //
    // O(n) time is the lower bound (must read every character).
    // O(1) extra space is minimal. Identical logic to Approach 2,
    // presented as a compact single-pass variant.
    // ============================================================
    static class Best {
        public String removeOuterParentheses(String s) {
            StringBuilder sb = new StringBuilder();
            int depth = 0;

            for (char c : s.toCharArray()) {
                if (c == '(' && ++depth > 1) {
                    sb.append(c);
                } else if (c == ')' && depth-- > 1) {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Remove Outermost Parentheses ===\n");

        String[] tests = {"(()())(())", "(()())(())(()(()))", "()()"};
        String[] expected = {"()()()", "()()()()(())", ""};

        for (int t = 0; t < tests.length; t++) {
            String s = tests[t];
            System.out.println("Input:    \"" + s + "\"");
            System.out.println("Expected: \"" + expected[t] + "\"");
            System.out.println("Brute:    \"" + new BruteForce().removeOuterParentheses(s) + "\"");
            System.out.println("Optimal:  \"" + new Optimal().removeOuterParentheses(s) + "\"");
            System.out.println("Best:     \"" + new Best().removeOuterParentheses(s) + "\"");
            System.out.println();
        }
    }
}
