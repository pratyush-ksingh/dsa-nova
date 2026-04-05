/**
 * Problem: Postfix to Prefix Conversion
 * Difficulty: MEDIUM | XP: 25
 *
 * Convert a postfix expression to its equivalent prefix expression.
 * E.g., "AB+CD-*" -> "*+AB-CD"
 * Operands are single characters (A-Z, 0-9). Operators: +, -, *, /, ^
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Build Expression Tree, Preorder Traverse
    // Time: O(n)  |  Space: O(n)
    //
    // Build an explicit expression tree from postfix notation.
    // Preorder traversal (root-left-right) gives the prefix form.
    // ============================================================
    static class TreeNode {
        char val;
        TreeNode left, right;
        TreeNode(char val) { this.val = val; }
        TreeNode(char val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static String bruteForce(String postfix) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        for (char ch : postfix.toCharArray()) {
            if (!isOperator(ch)) {
                stack.push(new TreeNode(ch));
            } else {
                TreeNode right = stack.pop();
                TreeNode left = stack.pop();
                stack.push(new TreeNode(ch, left, right));
            }
        }
        if (stack.isEmpty()) return "";
        return preorder(stack.peek());
    }

    private static String preorder(TreeNode node) {
        if (node == null) return "";
        if (node.left == null && node.right == null) return String.valueOf(node.val);
        return node.val + preorder(node.left) + preorder(node.right);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Stack-Based Direct Conversion
    // Time: O(n)  |  Space: O(n)
    //
    // Scan left to right. Push operands as strings.
    // On operator: pop op2, pop op1, push (operator + op1 + op2).
    // Final stack top is the prefix expression.
    // ============================================================
    public static String optimal(String postfix) {
        Deque<String> stack = new ArrayDeque<>();
        for (char ch : postfix.toCharArray()) {
            if (!isOperator(ch)) {
                stack.push(String.valueOf(ch));
            } else {
                String op2 = stack.pop();
                String op1 = stack.pop();
                stack.push(ch + op1 + op2);   // operator before operands
            }
        }
        return stack.isEmpty() ? "" : stack.peek();
    }

    // ============================================================
    // APPROACH 3: BEST -- Stack-Based with StringBuilder
    // Time: O(n)  |  Space: O(n)
    //
    // Same stack logic but uses StringBuilder for O(n) total string
    // concatenations (avoids creating many intermediate strings).
    // In practice identical to Approach 2 for single-char operands.
    // ============================================================
    public static String best(String postfix) {
        Deque<String> stack = new ArrayDeque<>();
        for (char ch : postfix.toCharArray()) {
            if (!isOperator(ch)) {
                stack.push(String.valueOf(ch));
            } else {
                String op2 = stack.pop();
                String op1 = stack.pop();
                // Build with StringBuilder for cleaner string handling
                String expr = new StringBuilder()
                    .append(ch).append(op1).append(op2).toString();
                stack.push(expr);
            }
        }
        return stack.isEmpty() ? "" : stack.peek();
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Postfix to Prefix ===\n");

        String[][] tests = {
            {"AB+",     "+AB"},
            {"AB+C*",   "*+ABC"},
            {"AB+CD-*", "*+AB-CD"},
            {"ABC*+",   "+A*BC"},
            {"A",       "A"},
        };

        for (String[] test : tests) {
            String postfix = test[0], expected = test[1];
            String b = bruteForce(postfix);
            String o = optimal(postfix);
            String r = best(postfix);
            String status = (b.equals(expected) && o.equals(expected) && r.equals(expected))
                ? "PASS" : "FAIL";
            System.out.printf("Postfix: %-12s Expected: %-12s Brute: %-12s Optimal: %-12s Best: %-12s [%s]%n",
                postfix, expected, b, o, r, status);
        }
    }
}
