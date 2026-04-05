/**
 * Problem: Infix to Prefix
 * Difficulty: MEDIUM | XP: 25
 *
 * Convert an infix expression to prefix (Polish notation).
 * Supports +, -, *, /, ^ operators and single-char operands.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // Operator precedence
    static int precedence(char op) {
        switch (op) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            case '^':           return 3;
            default:            return 0;
        }
    }

    static boolean isOperand(char c) {
        return Character.isLetterOrDigit(c);
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Full Parenthesization + Recursive
    // Time: O(n^2) worst case  |  Space: O(n)
    //
    // Fully parenthesize the expression, then recursively extract
    // prefix by finding the main operator of each sub-expression.
    // (Simplified: we implement the core logic using a tree-like
    //  recursive descent parser.)
    // ============================================================
    public static String bruteForce(String infix) {
        // Use recursive descent parsing
        // This is essentially building an implicit expression tree
        int[] pos = {0};
        String result = parseExpression(infix, pos, 0);
        return result;
    }

    private static String parseExpression(String s, int[] pos, int minPrec) {
        String left = parsePrimary(s, pos);

        while (pos[0] < s.length()) {
            char op = s.charAt(pos[0]);
            if (!isOperator(op) || precedence(op) < minPrec) break;

            pos[0]++; // consume operator
            int nextMinPrec = (op == '^') ? precedence(op) : precedence(op) + 1;
            String right = parseExpression(s, pos, nextMinPrec);
            left = op + left + right; // prefix: operator before operands
        }
        return left;
    }

    private static String parsePrimary(String s, int[] pos) {
        if (pos[0] < s.length() && s.charAt(pos[0]) == '(') {
            pos[0]++; // skip '('
            String result = parseExpression(s, pos, 0);
            pos[0]++; // skip ')'
            return result;
        }
        // Single operand
        return String.valueOf(s.charAt(pos[0]++));
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Reverse + Infix-to-Postfix + Reverse
    // Time: O(n)  |  Space: O(n)
    //
    // Classic trick:
    // 1. Reverse the infix string
    // 2. Swap ( <-> )
    // 3. Apply Shunting Yard (infix-to-postfix) with modified
    //    associativity: for left-assoc ops, use < (not <=)
    //    when comparing same precedence
    // 4. Reverse the result to get prefix
    // ============================================================
    public static String optimal(String infix) {
        // Step 1 & 2: Reverse and swap parentheses
        char[] chars = new StringBuilder(infix).reverse().toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(') chars[i] = ')';
            else if (chars[i] == ')') chars[i] = '(';
        }

        // Step 3: Shunting Yard (modified)
        StringBuilder output = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : chars) {
            if (isOperand(c)) {
                output.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop());
                }
                if (!stack.isEmpty()) stack.pop(); // remove '('
            } else {
                // Operator: for left-assoc, pop only strictly higher precedence
                // For right-assoc (^), pop higher-or-equal precedence
                while (!stack.isEmpty() && stack.peek() != '(' &&
                       shouldPop(stack.peek(), c)) {
                    output.append(stack.pop());
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop());
        }

        // Step 4: Reverse to get prefix
        return output.reverse().toString();
    }

    // In the reversed Shunting Yard:
    // For left-assoc ops: pop only if stack top has STRICTLY higher precedence
    // For right-assoc (^): pop if stack top has higher OR equal precedence
    private static boolean shouldPop(char stackTop, char incoming) {
        if (incoming == '^') {
            // Right-associative: pop if stack has strictly higher precedence
            return precedence(stackTop) > precedence(incoming);
        }
        // Left-associative: pop if stack has strictly higher precedence
        // (NOT equal -- this is the key difference from standard postfix)
        return precedence(stackTop) > precedence(incoming);
    }

    // ============================================================
    // APPROACH 3: BEST -- Direct Right-to-Left Scan
    // Time: O(n)  |  Space: O(n)
    //
    // Scan infix right-to-left, build prefix directly without
    // explicit string reversals. Treat ')' as open bracket and
    // '(' as close bracket.
    // ============================================================
    public static String best(String infix) {
        Deque<Character> stack = new ArrayDeque<>();
        StringBuilder result = new StringBuilder();

        for (int i = infix.length() - 1; i >= 0; i--) {
            char c = infix.charAt(i);

            if (isOperand(c)) {
                result.append(c);
            } else if (c == ')') {
                stack.push(c);
            } else if (c == '(') {
                while (!stack.isEmpty() && stack.peek() != ')') {
                    result.append(stack.pop());
                }
                if (!stack.isEmpty()) stack.pop(); // remove ')'
            } else {
                // Operator
                // For left-assoc: pop strictly higher precedence only
                // For right-assoc ^: pop strictly higher precedence only
                // (In right-to-left scan, same-precedence left-assoc should NOT pop)
                while (!stack.isEmpty() && stack.peek() != ')' &&
                       shouldPopRTL(stack.peek(), c)) {
                    result.append(stack.pop());
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }

        // We built the prefix in reverse order
        return result.reverse().toString();
    }

    private static boolean shouldPopRTL(char stackTop, char incoming) {
        if (incoming == '^') {
            // Right-associative: do NOT pop same precedence
            return precedence(stackTop) > precedence(incoming);
        }
        // Left-associative: pop same or higher precedence
        return precedence(stackTop) >= precedence(incoming);
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Infix to Prefix ===\n");

        String[][] tests = {
            {"A+B",       "+AB"},
            {"A+B*C",     "+A*BC"},
            {"(A+B)*C",   "*+ABC"},
            {"A+B*C-D",   "-+A*BCD"},
            {"A^B^C",     "^A^BC"},
            {"(A+B)*(C-D)", "*+AB-CD"},
            {"A",         "A"},
        };

        for (String[] test : tests) {
            String infix = test[0], expected = test[1];
            String b = bruteForce(infix);
            String o = optimal(infix);
            String be = best(infix);
            System.out.printf("Infix: %-20s Expected: %-10s%n", infix, expected);
            System.out.printf("  Brute:   %-10s %s%n", b, b.equals(expected) ? "PASS" : "FAIL");
            System.out.printf("  Optimal: %-10s %s%n", o, o.equals(expected) ? "PASS" : "FAIL");
            System.out.printf("  Best:    %-10s %s%n", be, be.equals(expected) ? "PASS" : "FAIL");
            System.out.println();
        }
    }
}
