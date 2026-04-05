/**
 * Problem: Expression Add Operators
 * Difficulty: HARD | XP: 50
 *
 * Given a string of digits and a target integer, insert +, -, * operators
 * (and no operator = concatenate) between digits to make the expression equal target.
 * Return all valid expressions.
 * Real-life use: Expression parsing, calculator logic, test case generation.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Generate all possible ways to split and insert operators.
    // Evaluate each string expression using a standard evaluator.
    // Time: O(4^N * N)  |  Space: O(4^N * N) — extremely slow for large N
    // ============================================================
    static class BruteForce {
        public static List<String> addOperators(String num, int target) {
            List<String> result = new ArrayList<>();
            generate(num, 0, "", result, target);
            return result;
        }

        private static void generate(String num, int idx, String expr,
                                     List<String> result, int target) {
            if (idx == num.length()) {
                if (evaluate(expr) == (long) target) result.add(expr);
                return;
            }
            for (int end = idx + 1; end <= num.length(); end++) {
                String token = num.substring(idx, end);
                // Avoid leading zeros in multi-digit numbers
                if (token.length() > 1 && token.charAt(0) == '0') break;
                if (expr.isEmpty()) {
                    generate(num, end, token, result, target);
                } else {
                    generate(num, end, expr + "+" + token, result, target);
                    generate(num, end, expr + "-" + token, result, target);
                    generate(num, end, expr + "*" + token, result, target);
                }
            }
        }

        private static long evaluate(String expr) {
            // Simple recursive descent: handles +, -, *
            Deque<Long> vals = new ArrayDeque<>();
            Deque<Character> ops = new ArrayDeque<>();
            int i = 0;
            while (i < expr.length()) {
                long num = 0;
                boolean neg = false;
                if (!vals.isEmpty() && (expr.charAt(i) == '+' || expr.charAt(i) == '-' || expr.charAt(i) == '*')) {
                    ops.push(expr.charAt(i++));
                }
                while (i < expr.length() && Character.isDigit(expr.charAt(i))) {
                    num = num * 10 + (expr.charAt(i++) - '0');
                }
                vals.push(num);
                // Handle * with higher precedence
                while (!ops.isEmpty() && ops.peek() == '*') {
                    ops.pop();
                    long b = vals.pop(), a = vals.pop();
                    vals.push(a * b);
                }
            }
            // Now only + and - remain
            long[] arr = new long[vals.size()];
            char[] op = new char[ops.size()];
            for (int j = arr.length - 1; j >= 0; j--) arr[j] = vals.pop();
            for (int j = op.length - 1; j >= 0; j--) op[j] = ops.pop();
            long res = arr[0];
            for (int j = 0; j < op.length; j++) {
                res = (op[j] == '+') ? res + arr[j + 1] : res - arr[j + 1];
            }
            return res;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Backtracking with running evaluation: track current value and
    // last multiplied term to correctly handle operator precedence for *.
    // Key: when we see *, we undo the last addition and redo with multiplication.
    // Time: O(4^N * N)  |  Space: O(N) — much better constants than brute force
    // ============================================================
    static class Optimal {
        public static List<String> addOperators(String num, int target) {
            List<String> result = new ArrayList<>();
            backtrack(num, target, 0, 0, 0, new StringBuilder(), result);
            return result;
        }

        private static void backtrack(String num, int target, int idx,
                                      long eval, long lastMul,
                                      StringBuilder path, List<String> result) {
            if (idx == num.length()) {
                if (eval == (long) target) result.add(path.toString());
                return;
            }
            int len = path.length();
            for (int end = idx + 1; end <= num.length(); end++) {
                // Avoid leading zeros
                if (end > idx + 1 && num.charAt(idx) == '0') break;
                String token = num.substring(idx, end);
                long cur = Long.parseLong(token);
                if (idx == 0) {
                    // First number: no operator
                    path.append(token);
                    backtrack(num, target, end, cur, cur, path, result);
                    path.setLength(len);
                } else {
                    // Add +
                    path.append('+').append(token);
                    backtrack(num, target, end, eval + cur, cur, path, result);
                    path.setLength(len);
                    // Add -
                    path.append('-').append(token);
                    backtrack(num, target, end, eval - cur, -cur, path, result);
                    path.setLength(len);
                    // Add *
                    path.append('*').append(token);
                    backtrack(num, target, end,
                              eval - lastMul + lastMul * cur, lastMul * cur, path, result);
                    path.setLength(len);
                }
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Same algorithm as Optimal but uses char[] buffer instead of StringBuilder
    // for a small constant-factor improvement in string building.
    // Time: O(4^N * N)  |  Space: O(N)
    // ============================================================
    static class Best {
        private static int[] result_indices;
        private static List<String> result;
        private static String num;
        private static int target;

        public static List<String> addOperators(String numStr, int tgt) {
            num = numStr;
            target = tgt;
            result = new ArrayList<>();
            char[] buf = new char[numStr.length() * 2]; // max size with operators
            dfs(0, 0, 0, 0, buf);
            return result;
        }

        private static void dfs(int idx, int pos, long eval, long lastMul, char[] buf) {
            if (idx == num.length()) {
                if (eval == (long) target) result.add(new String(buf, 0, pos));
                return;
            }
            long cur = 0;
            int numStart = pos;
            for (int end = idx; end < num.length(); end++) {
                // Prevent leading zeros
                if (end > idx && num.charAt(idx) == '0') break;
                cur = cur * 10 + (num.charAt(end) - '0');
                buf[pos++] = num.charAt(end);
                if (idx == 0) {
                    dfs(end + 1, pos, cur, cur, buf);
                } else {
                    buf[numStart - 1] = '+';
                    dfs(end + 1, pos, eval + cur, cur, buf);
                    buf[numStart - 1] = '-';
                    dfs(end + 1, pos, eval - cur, -cur, buf);
                    buf[numStart - 1] = '*';
                    dfs(end + 1, pos, eval - lastMul + lastMul * cur, lastMul * cur, buf);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Expression Add Operators ===");

        Object[][] tests = {{"123", 6}, {"232", 8}, {"105", 5}, {"00", 0}, {"3456237490", 9191}};
        for (Object[] t : tests) {
            String n = (String) t[0];
            int tgt = (Integer) t[1];
            System.out.printf("%nnum=\"%s\"  target=%d%n", n, tgt);
            System.out.println("  Optimal: " + Optimal.addOperators(n, tgt));
        }
    }
}
