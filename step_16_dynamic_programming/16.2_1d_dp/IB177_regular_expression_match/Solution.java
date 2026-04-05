/**
 * Problem: Regular Expression Match
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Implement regex matching with '.' (matches any single char) and
 * '*' (matches zero or more of the preceding char). Full string match.
 *
 * DP: dp[i][j] = does s[0..i-1] match p[0..j-1]?
 *
 * Transitions:
 *   - p[j-1] is '.': dp[i][j] = dp[i-1][j-1]  (dot matches s[i-1])
 *   - p[j-1] is '*':
 *       zero occurrences: dp[i][j] |= dp[i][j-2]
 *       one+ occurrences: dp[i][j] |= dp[i-1][j] if p[j-2]=='.' or p[j-2]==s[i-1]
 *   - else: dp[i][j] = dp[i-1][j-1] && s[i-1] == p[j-1]
 *
 * Real-life use: Regex engines, input validation, text search.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(2^(S+P))  |  Space: O(S + P)
    // Recursive without memoization. Exponential due to '*' backtracking.
    // ============================================================
    public static boolean bruteForce(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();
        boolean firstMatch = !s.isEmpty() &&
                             (p.charAt(0) == s.charAt(0) || p.charAt(0) == '.');
        if (p.length() >= 2 && p.charAt(1) == '*') {
            // '*' = zero occurrences OR one+ occurrences
            return bruteForce(s, p.substring(2)) ||
                   (firstMatch && bruteForce(s.substring(1), p));
        } else {
            return firstMatch && bruteForce(s.substring(1), p.substring(1));
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(S * P)  |  Space: O(S * P)
    // Memoized recursion (top-down DP). Cache (i, j) states.
    // ============================================================
    public static boolean optimal(String s, String p) {
        int m = s.length(), n = p.length();
        int[][] memo = new int[m + 1][n + 1]; // 0=unset, 1=true, -1=false
        return dpMemo(s, p, 0, 0, memo);
    }

    private static boolean dpMemo(String s, String p, int i, int j, int[][] memo) {
        int m = s.length(), n = p.length();
        if (j == n) return i == m;
        if (memo[i][j] != 0) return memo[i][j] == 1;

        boolean firstMatch = (i < m) && (p.charAt(j) == s.charAt(i) || p.charAt(j) == '.');
        boolean res;

        if (j + 1 < n && p.charAt(j + 1) == '*') {
            res = dpMemo(s, p, i, j + 2, memo) ||
                  (firstMatch && dpMemo(s, p, i + 1, j, memo));
        } else {
            res = firstMatch && dpMemo(s, p, i + 1, j + 1, memo);
        }
        memo[i][j] = res ? 1 : -1;
        return res;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(S * P)  |  Space: O(S * P)
    // Bottom-up DP table. dp[i][j] = s[0..i-1] matches p[0..j-1].
    // Space can be reduced to O(P) with rolling array.
    // ============================================================
    public static boolean best(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;

        // Handle patterns like a*, a*b*, a*b*c* matching empty string
        for (int j = 2; j <= n; j++) {
            if (p.charAt(j - 1) == '*') dp[0][j] = dp[0][j - 2];
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char sc = s.charAt(i - 1);
                char pc = p.charAt(j - 1);

                if (pc == '*') {
                    // '*' can't be first char, so j >= 2
                    char prev = p.charAt(j - 2);
                    // Zero occurrences of prev
                    dp[i][j] = dp[i][j - 2];
                    // One or more occurrences: prev matches sc
                    if (prev == '.' || prev == sc) {
                        dp[i][j] |= dp[i - 1][j];
                    }
                } else if (pc == '.' || pc == sc) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
                // else: dp[i][j] remains false
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        System.out.println("=== Regular Expression Match ===\n");

        String[][] tests = {
            {"aa", "a"},        // false
            {"aa", "a*"},       // true
            {"ab", ".*"},       // true
            {"aab", "c*a*b"},   // true
            {"mississippi", "mis*is*p*."},  // false
            {"", ""},           // true
            {"", "a*"},         // true
            {"abc", "abc"},     // true
            {"abc", "a.c"},     // true
            {"abc", "a.*c"},    // true
        };

        String[] expected = {"false", "true", "true", "true", "false", "true", "true", "true", "true", "true"};

        for (int i = 0; i < tests.length; i++) {
            String s = tests[i][0], p = tests[i][1];
            boolean bf = bruteForce(s, p);
            boolean op = optimal(s, p);
            boolean be = best(s, p);
            boolean allMatch = (bf == op && op == be);
            System.out.printf("s=%-15s p=%-12s | Brute=%b Optimal=%b Best=%b | Expected=%s [%s]%n",
                    "\"" + s + "\"", "\"" + p + "\"", bf, op, be,
                    expected[i], allMatch ? "OK" : "MISMATCH");
        }
    }
}
