/**
 * Problem: Interleaving Strings
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given strings s1, s2, and s3, determine if s3 is formed by interleaving
 * s1 and s2. s3 is an interleaving if it contains all chars of s1 and s2
 * and preserves their relative order.
 *
 * DP: dp[i][j] = can s3[0..i+j-1] be formed by interleaving s1[0..i-1] and s2[0..j-1]?
 *
 * Transitions:
 *   dp[i][j] = (dp[i-1][j] && s1[i-1] == s3[i+j-1])
 *            || (dp[i][j-1] && s2[j-1] == s3[i+j-1])
 *
 * Real-life use: Network packet ordering, merge conflict resolution,
 *                concurrent operation interleaving analysis.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(2^(M+N))  |  Space: O(M + N)
    // Recursive without memoization.
    // ============================================================
    public static boolean bruteForce(String s1, String s2, String s3) {
        if (s1.length() + s2.length() != s3.length()) return false;
        return recurse(s1, s2, s3, 0, 0, 0);
    }

    private static boolean recurse(String s1, String s2, String s3,
                                    int i, int j, int k) {
        if (k == s3.length()) return i == s1.length() && j == s2.length();
        char c = s3.charAt(k);
        boolean res = false;
        if (i < s1.length() && s1.charAt(i) == c)
            res = recurse(s1, s2, s3, i + 1, j, k + 1);
        if (!res && j < s2.length() && s2.charAt(j) == c)
            res = recurse(s1, s2, s3, i, j + 1, k + 1);
        return res;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(M * N)  |  Space: O(M * N)
    // Memoized recursion (top-down DP). k = i + j so we only need (i, j).
    // ============================================================
    public static boolean optimal(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (m + n != s3.length()) return false;
        int[][] memo = new int[m + 1][n + 1]; // 0=unset, 1=true, -1=false
        return memoDP(s1, s2, s3, 0, 0, memo);
    }

    private static boolean memoDP(String s1, String s2, String s3,
                                   int i, int j, int[][] memo) {
        int k = i + j;
        if (k == s3.length()) return i == s1.length() && j == s2.length();
        if (memo[i][j] != 0) return memo[i][j] == 1;

        boolean res = false;
        if (i < s1.length() && s1.charAt(i) == s3.charAt(k))
            res = memoDP(s1, s2, s3, i + 1, j, memo);
        if (!res && j < s2.length() && s2.charAt(j) == s3.charAt(k))
            res = memoDP(s1, s2, s3, i, j + 1, memo);

        memo[i][j] = res ? 1 : -1;
        return res;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(M * N)  |  Space: O(N) (space-optimized bottom-up)
    // Classic 2D DP reduced to 1D rolling array.
    // dp[j] = can s3[0..i+j-1] be formed from s1[0..i-1] and s2[0..j-1]?
    // ============================================================
    public static boolean best(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (m + n != s3.length()) return false;

        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        // Initialize first row (using only s2)
        for (int j = 1; j <= n; j++) {
            dp[j] = dp[j - 1] && s2.charAt(j - 1) == s3.charAt(j - 1);
        }

        for (int i = 1; i <= m; i++) {
            // Update dp[0] for this row (using only s1[0..i-1])
            dp[0] = dp[0] && s1.charAt(i - 1) == s3.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                // From above: dp[j] (came from s1)
                // From left: dp[j-1] (came from s2)
                dp[j] = (dp[j] && s1.charAt(i - 1) == s3.charAt(i + j - 1)) ||
                        (dp[j - 1] && s2.charAt(j - 1) == s3.charAt(i + j - 1));
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println("=== Interleaving Strings ===\n");

        String[][] tests = {
            {"aabcc", "dbbca", "aadbbcbcac"},  // true
            {"aabcc", "dbbca", "aadbbbaccc"},  // false
            {"", "", ""},                        // true
            {"a", "", "a"},                      // true
            {"", "b", "b"},                      // true
            {"abc", "def", "adbecf"},            // true
            {"abc", "def", "abcdef"},            // true
            {"abc", "def", "abdecf"},            // false? actually true: a,b,d,e,c,f -> s1=abc s2=def
        };
        boolean[] expected = {true, false, true, true, true, true, true, true};

        for (int i = 0; i < tests.length; i++) {
            String s1 = tests[i][0], s2 = tests[i][1], s3 = tests[i][2];
            boolean bf = bruteForce(s1, s2, s3);
            boolean op = optimal(s1, s2, s3);
            boolean be = best(s1, s2, s3);
            boolean allMatch = (bf == op && op == be);
            System.out.printf("s1=%-8s s2=%-8s s3=%-12s | B=%b O=%b Best=%b | Exp=%b [%s]%n",
                    "\"" + s1 + "\"", "\"" + s2 + "\"", "\"" + s3 + "\"",
                    bf, op, be, expected[i], allMatch ? "OK" : "CHECK");
        }
    }
}
