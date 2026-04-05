import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Check every substring
// Time: O(n^3)  |  Space: O(n)
// ============================================================
// For every pair (i,j), check if substring s[i..j] is a valid
// parentheses string using a counter. Track maximum length.
// ============================================================

class BruteForce {
    private boolean isValid(String s, int lo, int hi) {
        int cnt = 0;
        for (int i = lo; i <= hi; i++) {
            if (s.charAt(i) == '(') cnt++;
            else cnt--;
            if (cnt < 0) return false;
        }
        return cnt == 0;
    }

    public int longestValidParentheses(String s) {
        int n = s.length(), maxLen = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j += 2) { // valid must be even length
                if (isValid(s, i, j)) maxLen = Math.max(maxLen, j - i + 1);
            }
        }
        return maxLen;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Stack-based
// Time: O(n)  |  Space: O(n)
// ============================================================
// Push index of '(' onto stack. When ')' is seen:
//   - If stack is empty (or top is also ')'), push current index
//     as the new "base" marker.
//   - Else pop the matching '(' and compute length:
//     current index - stack top (the unmatched index before this group).
// Initialize stack with sentinel -1.
// ============================================================

class Optimal {
    public int longestValidParentheses(String s) {
        int maxLen = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1); // sentinel

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i); // new base
                } else {
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }
        return maxLen;
    }
}

// ============================================================
// APPROACH 3: BEST - Two-pass left-right counters (O(1) space)
// Time: O(n)  |  Space: O(1)
// ============================================================
// Left-to-right pass: count open/close. When they are equal,
// record length. When close > open, reset both to 0.
// Right-to-left pass: reverse roles (when open > close, reset).
// This handles cases the L-R pass misses (e.g. "(()" - won't
// reset R-L pass catches it by going right-to-left).
// Real-life use: validating code blocks in real-time IDEs,
// parsing nested XML/HTML tags in streaming parsers.
// ============================================================

class Best {
    public int longestValidParentheses(String s) {
        int n = s.length(), maxLen = 0;
        int open = 0, close = 0;

        // Left to right
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '(') open++;
            else close++;
            if (open == close) maxLen = Math.max(maxLen, 2 * close);
            else if (close > open) { open = 0; close = 0; }
        }

        open = 0; close = 0;
        // Right to left
        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') open++;
            else close++;
            if (open == close) maxLen = Math.max(maxLen, 2 * open);
            else if (open > close) { open = 0; close = 0; }
        }

        return maxLen;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Longest Valid Parentheses ===");

        String[] tests = {"(()", ")()())", "", "()", "(()(", ")((", "(()(((()"};
        int[] expects   = {2,     4,      0,  2,  2,    0,   2};

        for (int t = 0; t < tests.length; t++) {
            String s = tests[t];
            int b  = new BruteForce().longestValidParentheses(s);
            int op = new Optimal().longestValidParentheses(s);
            int bst = new Best().longestValidParentheses(s);
            boolean ok = b == op && op == bst && bst == expects[t];
            System.out.printf("s=%-12s expect=%d  b=%d op=%d bst=%d  %s%n",
                "\""+s+"\"", expects[t], b, op, bst, ok?"OK":"FAIL");
        }
    }
}
