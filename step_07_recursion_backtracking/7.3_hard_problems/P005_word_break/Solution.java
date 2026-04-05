import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(2^n)  |  Space: O(n)
// Pure recursion: at each index try every word in the dictionary
// and recurse. No memoization — exponential in worst case.
// ============================================================
class BruteForce {
    public static boolean solve(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        return check(s, 0, dict);
    }

    private static boolean check(String s, int start, Set<String> dict) {
        if (start == s.length()) return true;
        for (int end = start + 1; end <= s.length(); end++) {
            if (dict.contains(s.substring(start, end)) && check(s, end, dict)) {
                return true;
            }
        }
        return false;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Top-Down DP / Memoization)
// Time: O(n^2 * n)  |  Space: O(n)
// Recursion + memoization: cache results for each start index.
// ============================================================
class Optimal {
    public static boolean solve(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        Boolean[] memo = new Boolean[s.length() + 1];
        return dp(s, 0, dict, memo);
    }

    private static boolean dp(String s, int start, Set<String> dict, Boolean[] memo) {
        if (start == s.length()) return true;
        if (memo[start] != null) return memo[start];
        for (int end = start + 1; end <= s.length(); end++) {
            if (dict.contains(s.substring(start, end)) && dp(s, end, dict, memo)) {
                memo[start] = true;
                return true;
            }
        }
        memo[start] = false;
        return false;
    }
}

// ============================================================
// APPROACH 3: BEST (Bottom-Up DP)
// Time: O(n^2)  |  Space: O(n)
// dp[i] = true if s[0..i) can be segmented.
// For each i, check all j < i: if dp[j] && s[j..i) in dict, dp[i]=true.
// ============================================================
class Best {
    public static boolean solve(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;  // empty string can be segmented

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Word Break ===");

        Object[][] tests = {
            {"leetcode",   Arrays.asList("leet","code"),       true},
            {"applepenapple", Arrays.asList("apple","pen"),    true},
            {"catsandog",  Arrays.asList("cats","dog","sand","and","cat"), false},
            {"a",          Arrays.asList("a"),                 true},
            {"abcd",       Arrays.asList("a","abc","b","cd"),  true},
        };

        for (Object[] t : tests) {
            String s = (String) t[0];
            @SuppressWarnings("unchecked")
            List<String> dict = (List<String>) t[1];
            boolean expected = (boolean) t[2];

            boolean b    = BruteForce.solve(s, dict);
            boolean o    = Optimal.solve(s, dict);
            boolean best = Best.solve(s, dict);
            String status = (b == expected && o == expected && best == expected) ? "PASS" : "FAIL";
            System.out.printf("s=%-15s | Brute: %-5s | Optimal: %-5s | Best: %-5s | Expected: %-5s | %s%n",
                    "\"" + s + "\"", b, o, best, expected, status);
        }
    }
}
