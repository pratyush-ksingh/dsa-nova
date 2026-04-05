/**
 * Problem: Count and Say
 * LeetCode 38 | Difficulty: MEDIUM | XP: 25
 *
 * The count-and-say sequence:
 *   countAndSay(1) = "1"
 *   countAndSay(n) = run-length encoding of countAndSay(n-1).
 *
 * Example: "1211" -> "one 1, one 2, two 1s" -> "111221"
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  -  Iterative character-by-character
    // Time: O(n * L)  where L = max length of any term  |  Space: O(L)
    // ============================================================
    static class BruteForce {
        /**
         * Start with "1".  For each of n-1 subsequent steps, scan the
         * current string, count consecutive identical characters, and
         * build the next string by appending count + character.
         */
        public String countAndSay(int n) {
            String result = "1";

            for (int step = 1; step < n; step++) {
                StringBuilder sb = new StringBuilder();
                int i = 0;
                while (i < result.length()) {
                    char ch = result.charAt(i);
                    int count = 0;
                    while (i < result.length() && result.charAt(i) == ch) {
                        count++;
                        i++;
                    }
                    sb.append(count).append(ch);
                }
                result = sb.toString();
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  -  Iterative with two-pointer run scan
    // Time: O(n * L)  |  Space: O(L)
    // ============================================================
    static class Optimal {
        /**
         * Same iteration as BruteForce but uses explicit two-pointer
         * (start, end) to delineate each run, making the run detection
         * cleaner and avoiding an inner counter variable.
         */
        public String countAndSay(int n) {
            String result = "1";

            for (int step = 1; step < n; step++) {
                StringBuilder sb = new StringBuilder();
                int start = 0;
                while (start < result.length()) {
                    int end = start;
                    while (end < result.length() && result.charAt(end) == result.charAt(start)) {
                        end++;
                    }
                    sb.append(end - start).append(result.charAt(start));
                    start = end;
                }
                result = sb.toString();
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  -  Recursive with memoization
    // Time: O(n * L)  |  Space: O(n * L) for memo + call stack
    // ============================================================
    static class Best {
        /**
         * Recursive top-down: countAndSay(n) depends on countAndSay(n-1).
         * Memoize results in an array to avoid recomputation when called
         * multiple times with different values of n.
         */
        private String[] memo;

        public String countAndSay(int n) {
            memo = new String[n + 1];
            return solve(n);
        }

        private String solve(int n) {
            if (n == 1) return "1";
            if (memo[n] != null) return memo[n];

            String prev = solve(n - 1);
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < prev.length()) {
                char ch = prev.charAt(i);
                int count = 0;
                while (i < prev.length() && prev.charAt(i) == ch) {
                    count++;
                    i++;
                }
                sb.append(count).append(ch);
            }
            memo[n] = sb.toString();
            return memo[n];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Count And Say ===");
        // Expected: 1, 11, 21, 1211, 111221, 312211, 13112221
        for (int i = 1; i <= 7; i++) {
            String b  = new BruteForce().countAndSay(i);
            String o  = new Optimal().countAndSay(i);
            String be = new Best().countAndSay(i);
            System.out.printf("n=%d  Brute=%-10s  Optimal=%-10s  Best=%s%n", i, b, o, be);
        }
    }
}
