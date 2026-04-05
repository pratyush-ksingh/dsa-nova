/**
 * Problem: Max Nesting Depth of Parentheses (LeetCode #1614)
 * Difficulty: EASY | XP: 10
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Stack
    // Time: O(n)  |  Space: O(n)
    //
    // Push '(' onto a stack, pop on ')'. Track maximum stack size.
    // ============================================================
    static class BruteForce {
        public int maxDepth(String s) {
            java.util.Stack<Character> stack = new java.util.Stack<>();
            int maxDepth = 0;

            for (char c : s.toCharArray()) {
                if (c == '(') {
                    stack.push(c);
                    maxDepth = Math.max(maxDepth, stack.size());
                } else if (c == ')') {
                    stack.pop();
                }
                // Ignore all other characters
            }
            return maxDepth;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Counter-Based
    // Time: O(n)  |  Space: O(1)
    //
    // Replace the stack with a single integer depth counter.
    // Increment on '(', decrement on ')'. Track running max.
    // ============================================================
    static class Optimal {
        public int maxDepth(String s) {
            int depth = 0;
            int maxDepth = 0;

            for (char c : s.toCharArray()) {
                if (c == '(') {
                    depth++;
                    maxDepth = Math.max(maxDepth, depth);
                } else if (c == ')') {
                    depth--;
                }
            }
            return maxDepth;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same as Optimal (provably optimal)
    // Time: O(n)  |  Space: O(1)
    //
    // Cannot improve beyond O(n) time (must read every char)
    // or O(1) space (two integers). Compact variant.
    // ============================================================
    static class Best {
        public int maxDepth(String s) {
            int depth = 0, max = 0;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '(') max = Math.max(max, ++depth);
                else if (c == ')') depth--;
            }
            return max;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Max Nesting Depth of Parentheses ===\n");

        String[] tests = {
            "(1+(2*3)+((8)/4))+1",
            "(1)+((2))+(((3)))",
            "1+(2*3)/(2-1)",
            "1"
        };
        int[] expected = {3, 3, 1, 0};

        for (int t = 0; t < tests.length; t++) {
            String s = tests[t];
            System.out.println("Input:    \"" + s + "\"");
            System.out.println("Expected: " + expected[t]);
            System.out.println("Brute:    " + new BruteForce().maxDepth(s));
            System.out.println("Optimal:  " + new Optimal().maxDepth(s));
            System.out.println("Best:     " + new Best().maxDepth(s));
            System.out.println();
        }
    }
}
