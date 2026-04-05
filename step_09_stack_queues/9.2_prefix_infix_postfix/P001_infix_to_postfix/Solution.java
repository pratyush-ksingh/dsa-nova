/**
 * Problem: Infix to Postfix
 * Difficulty: MEDIUM | XP: 25
 *
 * Convert an infix expression to postfix (Reverse Polish Notation)
 * using Dijkstra's Shunting Yard algorithm.
 *
 * @author DSA_Nova
 */
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Fully Parenthesize Then Convert
    // Time: O(n^2)  |  Space: O(n)
    // Conceptual approach: add full parentheses, then rearrange.
    // In practice, this is hard to implement. We show a simplified
    // version that handles basic +, -, *, / without parentheses.
    // ============================================================
    static class BruteForce {
        public static String infixToPostfix(String s) {
            // Simplified: repeatedly find the highest-precedence operator,
            // extract its operands, and combine as "operand1 operand2 op".
            // This is impractical for general use. See Optimal for real solution.

            // Fallback to the Shunting Yard (this approach exists for illustration)
            return shuntingYard(s, false);
        }

        private static String shuntingYard(String s, boolean handlePower) {
            StringBuilder output = new StringBuilder();
            Stack<Character> stack = new Stack<>();

            for (char c : s.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    output.append(c);
                } else if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        output.append(stack.pop());
                    }
                    if (!stack.isEmpty()) stack.pop(); // Remove '('
                } else {
                    // Operator: pop higher/equal precedence (left-associative)
                    while (!stack.isEmpty() && stack.peek() != '('
                           && precedence(stack.peek()) >= precedence(c)) {
                        output.append(stack.pop());
                    }
                    stack.push(c);
                }
            }

            while (!stack.isEmpty()) {
                output.append(stack.pop());
            }

            return output.toString();
        }

        private static int precedence(char op) {
            if (op == '+' || op == '-') return 1;
            if (op == '*' || op == '/') return 2;
            return 0;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Shunting Yard Algorithm
    // Time: O(n)  |  Space: O(n)
    // Operands to output. Operators on stack with precedence-based popping.
    // Parentheses act as barriers.
    // ============================================================
    static class Optimal {
        public static String infixToPostfix(String s) {
            StringBuilder output = new StringBuilder();
            Stack<Character> stack = new Stack<>();

            for (char c : s.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    // Operands go directly to output
                    output.append(c);

                } else if (c == '(') {
                    // Left paren is a barrier -- push it
                    stack.push(c);

                } else if (c == ')') {
                    // Pop everything until matching '('
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        output.append(stack.pop());
                    }
                    if (!stack.isEmpty()) stack.pop();  // Discard '('

                } else {
                    // Operator: pop stack while top has >= precedence (left-assoc)
                    while (!stack.isEmpty() && stack.peek() != '('
                           && precedence(stack.peek()) >= precedence(c)) {
                        output.append(stack.pop());
                    }
                    stack.push(c);
                }
            }

            // Pop all remaining operators
            while (!stack.isEmpty()) {
                output.append(stack.pop());
            }

            return output.toString();
        }

        private static int precedence(char op) {
            if (op == '+' || op == '-') return 1;
            if (op == '*' || op == '/') return 2;
            return 0;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Shunting Yard with Full Operator Support
    // Time: O(n)  |  Space: O(n)
    // Handles right-associative operators (^), configurable precedence.
    // ============================================================
    static class Best {
        private static final Map<Character, Integer> PRECEDENCE = new HashMap<>();
        private static final Map<Character, Character> ASSOCIATIVITY = new HashMap<>();

        static {
            PRECEDENCE.put('+', 1);
            PRECEDENCE.put('-', 1);
            PRECEDENCE.put('*', 2);
            PRECEDENCE.put('/', 2);
            PRECEDENCE.put('^', 3);

            ASSOCIATIVITY.put('+', 'L');
            ASSOCIATIVITY.put('-', 'L');
            ASSOCIATIVITY.put('*', 'L');
            ASSOCIATIVITY.put('/', 'L');
            ASSOCIATIVITY.put('^', 'R');
        }

        public static String infixToPostfix(String s) {
            StringBuilder output = new StringBuilder();
            Stack<Character> stack = new Stack<>();

            for (char c : s.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    output.append(c);

                } else if (c == '(') {
                    stack.push(c);

                } else if (c == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        output.append(stack.pop());
                    }
                    if (!stack.isEmpty()) stack.pop();

                } else if (PRECEDENCE.containsKey(c)) {
                    // Operator with associativity-aware popping
                    while (!stack.isEmpty() && stack.peek() != '('
                           && PRECEDENCE.containsKey(stack.peek())
                           && shouldPop(stack.peek(), c)) {
                        output.append(stack.pop());
                    }
                    stack.push(c);
                }
            }

            while (!stack.isEmpty()) {
                output.append(stack.pop());
            }

            return output.toString();
        }

        /**
         * Determine if the stack top operator should be popped before pushing current.
         * Left-associative: pop when stackTop >= current
         * Right-associative: pop when stackTop > current (NOT equal)
         */
        private static boolean shouldPop(char stackTop, char current) {
            int topPrec = PRECEDENCE.getOrDefault(stackTop, 0);
            int curPrec = PRECEDENCE.getOrDefault(current, 0);

            if (ASSOCIATIVITY.getOrDefault(current, 'L') == 'L') {
                return topPrec >= curPrec;
            } else {
                return topPrec > curPrec;  // Right-associative: strict greater
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Infix to Postfix ===\n");

        String[][] testCases = {
            {"a+b",         "ab+"},
            {"a+b*c",       "abc*+"},
            {"(a+b)*c",     "ab+c*"},
            {"a+b+c",       "ab+c+"},
            {"a*(b+c*d)+e", "abcd*+*e+"},
            {"((a+b))",     "ab+"},
            {"a^b^c",       "abc^^"},    // Right-associative
            {"a+b*c-d/e",   "abc*+de/-"},
        };

        for (String[] tc : testCases) {
            String input = tc[0];
            String expected = tc[1];
            String result = Best.infixToPostfix(input);
            String status = result.equals(expected) ? "PASS" : "FAIL";

            System.out.printf("[%s] Input: %-18s  Expected: %-10s  Got: %-10s%n",
                status, input, expected, result);
        }

        // Also show all three approaches for a standard case
        System.out.println("\n--- Comparison (a+b*c) ---");
        System.out.println("  Brute:   " + BruteForce.infixToPostfix("a+b*c"));
        System.out.println("  Optimal: " + Optimal.infixToPostfix("a+b*c"));
        System.out.println("  Best:    " + Best.infixToPostfix("a+b*c"));
    }
}
