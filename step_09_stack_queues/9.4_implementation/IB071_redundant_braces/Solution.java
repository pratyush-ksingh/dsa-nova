/**
 * Problem: Redundant Braces
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given a valid expression with operators +, -, *, / and parentheses,
 * determine if the expression contains redundant braces.
 *
 * @author DSA_Nova
 */
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Repeated Inner-Pair Check
    // Time: O(n^2)  |  Space: O(n)
    // Find innermost parens, check if they contain an operator.
    // ============================================================
    static class BruteForce {
        public static boolean hasRedundantBraces(String s) {
            Set<Character> operators = Set.of('+', '-', '*', '/');
            StringBuilder sb = new StringBuilder(s);

            while (true) {
                // Find the innermost closing paren
                int closeIdx = -1;
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == ')') {
                        closeIdx = i;
                        break;
                    }
                }
                if (closeIdx == -1) break;  // No more parentheses

                // Find its matching opening paren (scan backwards)
                int openIdx = -1;
                for (int i = closeIdx - 1; i >= 0; i--) {
                    if (sb.charAt(i) == '(') {
                        openIdx = i;
                        break;
                    }
                }

                // Check content between openIdx and closeIdx
                boolean hasOp = false;
                for (int i = openIdx + 1; i < closeIdx; i++) {
                    if (operators.contains(sb.charAt(i))) {
                        hasOp = true;
                        break;
                    }
                }

                if (!hasOp) return true;  // Redundant!

                // Remove this pair of parentheses and continue
                sb.deleteCharAt(closeIdx);
                sb.deleteCharAt(openIdx);
            }

            return false;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Stack Scan
    // Time: O(n)  |  Space: O(n)
    // Push everything except ')'. On ')', pop until '(' and check
    // if any operator was found between them.
    // ============================================================
    static class Optimal {
        public static boolean hasRedundantBraces(String s) {
            Stack<Character> stack = new Stack<>();

            for (char c : s.toCharArray()) {
                if (c == ')') {
                    boolean hasOperator = false;

                    // Pop until we find the matching '('
                    while (stack.peek() != '(') {
                        char popped = stack.pop();
                        if (popped == '+' || popped == '-' ||
                            popped == '*' || popped == '/') {
                            hasOperator = true;
                        }
                    }
                    stack.pop();  // Remove the '('

                    if (!hasOperator) return true;  // Redundant braces!
                } else {
                    stack.push(c);
                }
            }

            return false;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Stack with Operator Set (Clean Production Code)
    // Time: O(n)  |  Space: O(n)
    // Same logic with a clean operator set for extensibility.
    // ============================================================
    static class Best {
        private static final Set<Character> OPERATORS = Set.of('+', '-', '*', '/');

        public static boolean hasRedundantBraces(String s) {
            Stack<Character> stack = new Stack<>();

            for (char c : s.toCharArray()) {
                if (c == ')') {
                    boolean hasOperator = false;
                    int count = 0;  // Track elements between ( and )

                    while (stack.peek() != '(') {
                        char popped = stack.pop();
                        if (OPERATORS.contains(popped)) {
                            hasOperator = true;
                        }
                        count++;
                    }
                    stack.pop();  // Remove '('

                    // Redundant if: no operator found, or nothing between parens
                    if (!hasOperator || count == 0) return true;
                } else {
                    stack.push(c);
                }
            }

            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Redundant Braces ===\n");

        String[] testCases = {
            "((a+b))",   // true  -- outer braces redundant
            "(a+b)",     // false -- braces contain operator
            "(a)",       // true  -- no operator inside
            "a+(b*c)",   // false -- braces contain *
            "((a))",     // true  -- both layers redundant
            "a+b",       // false -- no braces at all
            "(a+b)*(c+d)" // false -- both pairs have operators
        };

        for (String s : testCases) {
            System.out.printf("Input: %-18s  Brute: %-5s  Optimal: %-5s  Best: %-5s%n",
                s,
                BruteForce.hasRedundantBraces(s),
                Optimal.hasRedundantBraces(s),
                Best.hasRedundantBraces(s));
        }
    }
}
