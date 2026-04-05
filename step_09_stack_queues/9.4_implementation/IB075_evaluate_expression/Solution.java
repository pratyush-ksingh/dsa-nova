/**
 * Problem: Evaluate Expression (Reverse Polish Notation)
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Evaluate the value of a postfix (RPN) expression.
 * Valid operators: +, -, *, /  (integer division truncates toward zero).
 * Real-life use: Compilers, calculators, stack-based VM evaluation.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Convert RPN to infix, then use recursive descent parser.
    // (Educational — same O() as optimal but more code.)
    // Time: O(N)  |  Space: O(N)
    // ============================================================
    static class BruteForce {
        public static int evalRPN(String[] tokens) {
            // Recursive eval using index pointer
            int[] idx = {tokens.length - 1};
            return solve(tokens, idx);
        }

        private static int solve(String[] tokens, int[] idx) {
            String t = tokens[idx[0]--];
            if (!isOp(t)) return Integer.parseInt(t);
            int right = solve(tokens, idx);
            int left  = solve(tokens, idx);
            return apply(left, right, t);
        }

        private static boolean isOp(String t) {
            return t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/");
        }

        private static int apply(int a, int b, String op) {
            switch (op) {
                case "+": return a + b;
                case "-": return a - b;
                case "*": return a * b;
                default:  return (int)(a / (double)b); // truncate toward zero
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Classic iterative stack approach.
    // Push numbers; on operator, pop two, compute, push result.
    // Time: O(N)  |  Space: O(N)
    // ============================================================
    static class Optimal {
        public static int evalRPN(String[] tokens) {
            Deque<Integer> stack = new ArrayDeque<>();
            for (String t : tokens) {
                if (isOp(t)) {
                    int b = stack.pop(), a = stack.pop();
                    stack.push(apply(a, b, t));
                } else {
                    stack.push(Integer.parseInt(t));
                }
            }
            return stack.pop();
        }

        private static boolean isOp(String t) {
            return "+".equals(t) || "-".equals(t) || "*".equals(t) || "/".equals(t);
        }

        private static int apply(int a, int b, String op) {
            switch (op) {
                case "+": return a + b;
                case "-": return a - b;
                case "*": return a * b;
                default:  return (int)(a / (double)b);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Same iterative stack but uses int[] array as manual stack for
    // slightly better performance (avoids Integer boxing/unboxing).
    // Time: O(N)  |  Space: O(N)
    // ============================================================
    static class Best {
        public static int evalRPN(String[] tokens) {
            int[] stack = new int[tokens.length];
            int top = -1;
            Set<String> ops = Set.of("+", "-", "*", "/");
            for (String t : tokens) {
                if (ops.contains(t)) {
                    int b = stack[top--], a = stack[top--];
                    int res;
                    switch (t) {
                        case "+": res = a + b; break;
                        case "-": res = a - b; break;
                        case "*": res = a * b; break;
                        default:  res = (int)(a / (double)b); break;
                    }
                    stack[++top] = res;
                } else {
                    stack[++top] = Integer.parseInt(t);
                }
            }
            return stack[top];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Evaluate Expression (RPN) ===");

        String[][] tests = {
            {"2", "1", "+", "3", "*"},         // (2+1)*3 = 9
            {"4", "13", "5", "/", "+"},         // 4+(13/5) = 6
            {"10", "6", "9", "3", "+", "-11",
             "*", "/", "*", "17", "+", "5", "+"}, // = 22
            {"3", "11", "+", "5", "-"},         // (3+11)-5 = 9
        };

        for (String[] t : tests) {
            System.out.printf("%ntokens=%s%n", Arrays.toString(t));
            System.out.println("  Brute  : " + BruteForce.evalRPN(t));
            System.out.println("  Optimal: " + Optimal.evalRPN(t));
            System.out.println("  Best   : " + Best.evalRPN(t));
        }
    }
}
