/**
 * Problem: Postfix to Infix Conversion
 * Difficulty: MEDIUM | XP: 25
 *
 * Convert a postfix expression to its equivalent infix expression.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Build Expression Tree, Inorder Traverse)
// Time: O(n) | Space: O(n)
// ============================================================
class BruteForce {
    static class Node {
        String val;
        Node left, right;
        Node(String val) { this.val = val; }
        Node(String val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static String postfixToInfix(String postfix) {
        Set<Character> operators = new HashSet<>(Arrays.asList('+', '-', '*', '/', '^'));
        Deque<Node> stack = new ArrayDeque<>();

        for (char ch : postfix.toCharArray()) {
            if (!operators.contains(ch)) {
                stack.push(new Node(String.valueOf(ch)));
            } else {
                Node right = stack.pop();
                Node left = stack.pop();
                stack.push(new Node(String.valueOf(ch), left, right));
            }
        }

        return inorder(stack.peek());
    }

    private static String inorder(Node node) {
        if (node == null) return "";
        if (node.left == null && node.right == null) return node.val;
        return "(" + inorder(node.left) + node.val + inorder(node.right) + ")";
    }
}

// ============================================================
// Approach 2: Optimal (Stack-Based Direct Conversion)
// Time: O(n) | Space: O(n)
// ============================================================
class Optimal {
    public static String postfixToInfix(String postfix) {
        Set<Character> operators = new HashSet<>(Arrays.asList('+', '-', '*', '/', '^'));
        Deque<String> stack = new ArrayDeque<>();

        for (char ch : postfix.toCharArray()) {
            if (!operators.contains(ch)) {
                stack.push(String.valueOf(ch));
            } else {
                String op2 = stack.pop();
                String op1 = stack.pop();
                String expr = "(" + op1 + ch + op2 + ")";
                stack.push(expr);
            }
        }

        return stack.isEmpty() ? "" : stack.peek();
    }
}

// ============================================================
// Approach 3: Best (Stack-Based with Minimal Parentheses)
// Time: O(n) | Space: O(n)
// ============================================================
class Best {
    private static int precedence(char op) {
        switch (op) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            case '^': return 3;
            default: return 0;
        }
    }

    public static String postfixToInfix(String postfix) {
        Set<Character> operators = new HashSet<>(Arrays.asList('+', '-', '*', '/', '^'));
        // Stack stores: [expression, rootOperatorChar or '\0']
        Deque<String[]> stack = new ArrayDeque<>();

        for (char ch : postfix.toCharArray()) {
            if (!operators.contains(ch)) {
                stack.push(new String[]{String.valueOf(ch), "\0"});
            } else {
                String[] right = stack.pop();
                String[] left = stack.pop();

                String leftExpr = left[0];
                char leftOp = left[1].charAt(0);
                String rightExpr = right[0];
                char rightOp = right[1].charAt(0);

                // Add parens to left if its root has lower precedence
                if (leftOp != '\0' && precedence(leftOp) < precedence(ch)) {
                    leftExpr = "(" + leftExpr + ")";
                }

                // Add parens to right if its root has strictly lower precedence
                if (rightOp != '\0' && precedence(rightOp) < precedence(ch)) {
                    rightExpr = "(" + rightExpr + ")";
                }

                String expr = leftExpr + ch + rightExpr;
                stack.push(new String[]{expr, String.valueOf(ch)});
            }
        }

        return stack.isEmpty() ? "" : stack.peek()[0];
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Postfix to Infix ===\n");

        String[] tests = {"ab+", "ab+c*", "abc*+", "ab+cd+*"};
        String[] expected = {"(a+b)", "((a+b)*c)", "(a+(b*c))", "((a+b)*(c+d))"};

        for (int i = 0; i < tests.length; i++) {
            String b = BruteForce.postfixToInfix(tests[i]);
            String o = Optimal.postfixToInfix(tests[i]);
            String r = Best.postfixToInfix(tests[i]);
            boolean pass = o.equals(expected[i]);

            System.out.println("Postfix:  " + tests[i]);
            System.out.println("  Brute:    " + b);
            System.out.println("  Optimal:  " + o);
            System.out.println("  Best:     " + r);
            System.out.println("  Expected: " + expected[i] + "  [" + (pass ? "PASS" : "FAIL") + "]");
            System.out.println();
        }
    }
}
