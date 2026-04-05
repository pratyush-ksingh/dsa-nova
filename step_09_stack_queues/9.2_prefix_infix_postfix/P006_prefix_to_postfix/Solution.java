/**
 * Problem: Prefix to Postfix
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Convert prefix->infix->postfix
    // Time: O(n^2) due to string concat  |  Space: O(n)
    // ============================================================
    public static String bruteForce(String prefix) {
        // Step 1: prefix to infix
        Stack<String> infixStack = new Stack<>();
        for (int i = prefix.length() - 1; i >= 0; i--) {
            char ch = prefix.charAt(i);
            if (Character.isLetterOrDigit(ch)) {
                infixStack.push(String.valueOf(ch));
            } else {
                String op1 = infixStack.pop();
                String op2 = infixStack.pop();
                infixStack.push("(" + op1 + ch + op2 + ")");
            }
        }
        String infix = infixStack.peek();

        // Step 2: infix to postfix (Shunting-yard)
        StringBuilder postfix = new StringBuilder();
        Stack<Character> ops = new Stack<>();
        for (char ch : infix.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                postfix.append(ch);
            } else if (ch == '(') {
                ops.push(ch);
            } else if (ch == ')') {
                while (ops.peek() != '(') postfix.append(ops.pop());
                ops.pop();
            } else {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
                    postfix.append(ops.pop());
                }
                ops.push(ch);
            }
        }
        while (!ops.isEmpty()) postfix.append(ops.pop());
        return postfix.toString();
    }

    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        if (op == '^') return 3;
        return 0;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Stack-based direct conversion (right to left)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static String optimal(String prefix) {
        Stack<String> stack = new Stack<>();
        for (int i = prefix.length() - 1; i >= 0; i--) {
            char ch = prefix.charAt(i);
            if (Character.isLetterOrDigit(ch)) {
                stack.push(String.valueOf(ch));
            } else {
                String op1 = stack.pop();
                String op2 = stack.pop();
                stack.push(op1 + op2 + ch);
            }
        }
        return stack.peek();
    }

    // ============================================================
    // APPROACH 3: BEST - Recursive descent parsing
    // Time: O(n)  |  Space: O(n) recursion depth
    // ============================================================
    static int pos;

    public static String best(String prefix) {
        pos = 0;
        return parsePrefix(prefix);
    }

    private static String parsePrefix(String s) {
        char ch = s.charAt(pos++);
        if (Character.isLetterOrDigit(ch)) {
            return String.valueOf(ch);
        }
        // ch is an operator: parse two operands recursively
        String left = parsePrefix(s);
        String right = parsePrefix(s);
        return left + right + ch;
    }

    public static void main(String[] args) {
        System.out.println("=== Prefix to Postfix ===");
        String prefix = "*+AB-CD";  // Expected: AB+CD-*
        System.out.println("Brute Force: " + bruteForce(prefix));
        System.out.println("Optimal:     " + optimal(prefix));
        System.out.println("Best:        " + best(prefix));
    }
}
