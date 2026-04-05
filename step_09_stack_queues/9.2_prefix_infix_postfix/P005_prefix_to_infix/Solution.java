/**
 * Problem: Prefix to Infix Conversion
 * Difficulty: MEDIUM | XP: 25
 *
 * Convert a prefix expression to its equivalent fully parenthesized infix expression.
 * E.g., "*+AB-CD" -> "((A+B)*(C-D))"
 * Operands are single characters. Operators: +, -, *, /, ^
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Build Expression Tree, Inorder Traverse
    // Time: O(n)  |  Space: O(n)
    //
    // Prefix is a preorder traversal of the expression tree.
    // Reconstruct the tree by recursive descent (each operator
    // consumes the next two sub-trees). Then inorder traverse
    // with parentheses to get infix.
    // ============================================================
    static class TreeNode {
        char val;
        TreeNode left, right;
        TreeNode(char val) { this.val = val; }
        TreeNode(char val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    static int[] pos = {0};

    public static String bruteForce(String prefix) {
        pos[0] = 0;
        TreeNode root = buildTree(prefix);
        return inorder(root);
    }

    private static TreeNode buildTree(String prefix) {
        if (pos[0] >= prefix.length()) return null;
        char ch = prefix.charAt(pos[0]++);
        if (!isOperator(ch)) return new TreeNode(ch);
        TreeNode left = buildTree(prefix);
        TreeNode right = buildTree(prefix);
        return new TreeNode(ch, left, right);
    }

    private static String inorder(TreeNode node) {
        if (node == null) return "";
        if (node.left == null && node.right == null) return String.valueOf(node.val);
        return "(" + inorder(node.left) + node.val + inorder(node.right) + ")";
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Stack-Based Right-to-Left Scan
    // Time: O(n)  |  Space: O(n)
    //
    // Scan the prefix string from right to left:
    // - Operand: push as string.
    // - Operator: pop op1 and op2, form "(op1 op op2)", push.
    // The final stack top is the fully parenthesized infix.
    // ============================================================
    public static String optimal(String prefix) {
        Deque<String> stack = new ArrayDeque<>();
        for (int i = prefix.length() - 1; i >= 0; i--) {
            char ch = prefix.charAt(i);
            if (!isOperator(ch)) {
                stack.push(String.valueOf(ch));
            } else {
                String op1 = stack.pop();
                String op2 = stack.pop();
                stack.push("(" + op1 + ch + op2 + ")");
            }
        }
        return stack.isEmpty() ? "" : stack.peek();
    }

    // ============================================================
    // APPROACH 3: BEST -- Same Stack with StringBuilder
    // Time: O(n)  |  Space: O(n)
    //
    // Identical to Approach 2 but uses StringBuilder to avoid
    // string concatenation overhead. For n characters, total
    // string building is O(n) with StringBuilder.
    // ============================================================
    public static String best(String prefix) {
        Deque<String> stack = new ArrayDeque<>();
        for (int i = prefix.length() - 1; i >= 0; i--) {
            char ch = prefix.charAt(i);
            if (!isOperator(ch)) {
                stack.push(String.valueOf(ch));
            } else {
                String op1 = stack.pop();
                String op2 = stack.pop();
                String expr = new StringBuilder()
                    .append('(').append(op1).append(ch).append(op2).append(')')
                    .toString();
                stack.push(expr);
            }
        }
        return stack.isEmpty() ? "" : stack.peek();
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Prefix to Infix ===\n");

        String[][] tests = {
            {"+AB",      "(A+B)"},
            {"*+AB-CD",  "((A+B)*(C-D))"},
            {"+A*BC",    "(A+(B*C))"},
            {"*+ABC",    "((A+B)*C)"},
            {"-+A*BCD",  "((A+(B*C))-D)"},
            {"A",        "A"},
        };

        for (String[] test : tests) {
            String prefix = test[0], expected = test[1];
            String b = bruteForce(prefix);
            String o = optimal(prefix);
            String r = best(prefix);
            String status = (b.equals(expected) && o.equals(expected) && r.equals(expected))
                ? "PASS" : "FAIL";
            System.out.printf("Prefix: %-12s Expected: %-24s Brute: %-24s Optimal: %-24s [%s]%n",
                prefix, expected, b, o, status);
        }
    }
}
