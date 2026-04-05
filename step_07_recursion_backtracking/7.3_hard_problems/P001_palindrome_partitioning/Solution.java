/**
 * Problem: Palindrome Partitioning
 * Difficulty: HARD | XP: 50
 *
 * Given a string s, partition it such that every substring is a palindrome.
 * Return all possible palindrome partitioning of s.
 *
 * Example: "aab" -> [["a","a","b"], ["aa","b"]]
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Backtracking, isPalindrome on-the-fly
    // Time: O(n * 2^n)  |  Space: O(n) recursion depth
    // At each position try every possible next substring; check palindrome each time
    // ============================================================
    public static List<List<String>> bruteForce(String s) {
        List<List<String>> res = new ArrayList<>();
        backtrackBrute(s, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrackBrute(String s, int start,
                                        List<String> current, List<List<String>> res) {
        if (start == s.length()) {
            res.add(new ArrayList<>(current));
            return;
        }
        for (int end = start + 1; end <= s.length(); end++) {
            String sub = s.substring(start, end);
            if (isPalin(sub)) {
                current.add(sub);
                backtrackBrute(s, end, current, res);
                current.remove(current.size() - 1);
            }
        }
    }

    private static boolean isPalin(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l++) != s.charAt(r--)) return false;
        }
        return true;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Backtracking + DP palindrome precomputation
    // Time: O(n * 2^n)  |  Space: O(n^2) for DP table
    // Precompute dp[i][j] = true if s[i..j] is palindrome; O(1) lookup during backtrack
    // ============================================================
    public static List<List<String>> optimal(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        // dp[i][j]: palindrome if i==j, or s[i]==s[j] and dp[i+1][j-1]
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                dp[i][j] = s.charAt(i) == s.charAt(j) &&
                            (j - i <= 2 || dp[i + 1][j - 1]);
            }
        }
        List<List<String>> res = new ArrayList<>();
        backtrackOptimal(s, 0, dp, new ArrayList<>(), res);
        return res;
    }

    private static void backtrackOptimal(String s, int start, boolean[][] dp,
                                          List<String> current, List<List<String>> res) {
        if (start == s.length()) {
            res.add(new ArrayList<>(current));
            return;
        }
        for (int end = start; end < s.length(); end++) {
            if (dp[start][end]) {
                current.add(s.substring(start, end + 1));
                backtrackOptimal(s, end + 1, dp, current, res);
                current.remove(current.size() - 1);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST - DP precompute + iterative memoization of partition lists
    // Time: O(n * 2^n)  |  Space: O(n^2)
    // Same complexity but explicitly structured: dp[i] = all partitions of s[0..i-1]
    // ============================================================
    public static List<List<String>> best(String s) {
        int n = s.length();
        // palin[i][j] = true if s[i..j] is palindrome
        boolean[][] palin = new boolean[n][n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                palin[i][j] = s.charAt(i) == s.charAt(j) &&
                               (j - i <= 2 || palin[i + 1][j - 1]);
            }
        }
        // dp[i] = list of all palindrome partitions of s[0..i-1]
        @SuppressWarnings("unchecked")
        List<List<String>>[] dp = new List[n + 1];
        dp[0] = new ArrayList<>();
        dp[0].add(new ArrayList<>());
        for (int end = 1; end <= n; end++) {
            dp[end] = new ArrayList<>();
            for (int start = 0; start < end; start++) {
                if (palin[start][end - 1] && dp[start] != null) {
                    String part = s.substring(start, end);
                    for (List<String> prev : dp[start]) {
                        List<String> next = new ArrayList<>(prev);
                        next.add(part);
                        dp[end].add(next);
                    }
                }
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println("=== Palindrome Partitioning ===");

        String s = "aab";
        System.out.println("s=\"" + s + "\"");
        System.out.println("Brute:   " + bruteForce(s));
        System.out.println("Optimal: " + optimal(s));
        System.out.println("Best:    " + best(s));

        s = "a";
        System.out.println("\ns=\"" + s + "\"");
        System.out.println("Brute:   " + bruteForce(s));
        System.out.println("Optimal: " + optimal(s));
        System.out.println("Best:    " + best(s));

        s = "racecar";
        System.out.println("\ns=\"" + s + "\"");
        System.out.println("Brute:   " + bruteForce(s));
        System.out.println("Optimal: " + optimal(s));
        System.out.println("Best:    " + best(s));
    }
}
