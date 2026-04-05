/**
 * Problem: Excel Column Title
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given a positive integer n, return the corresponding Excel column title.
 * 1 -> "A", 26 -> "Z", 27 -> "AA", 28 -> "AB", 701 -> "ZY"
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(log26 n)  |  Space: O(log26 n)
    // Iterative with StringBuilder prepend
    // ============================================================
    static class BruteForce {
        /**
         * Build the result by prepending characters.
         * Subtract 1 each iteration to convert 1-indexed to 0-indexed,
         * then take mod 26 to get the current character.
         */
        public static String convertToTitle(int n) {
            StringBuilder sb = new StringBuilder();
            while (n > 0) {
                n--;                                     // shift to 0-indexed (A=0..Z=25)
                sb.insert(0, (char) ('A' + n % 26));    // prepend character
                n /= 26;
            }
            return sb.toString();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(log26 n)  |  Space: O(log26 n)
    // Append then reverse — avoids O(k) insert at index 0 each step
    // ============================================================
    static class Optimal {
        /**
         * Append characters to the end of a StringBuilder, then reverse.
         * Appending is O(1) amortized; insert(0, ...) is O(k) per call.
         */
        public static String convertToTitle(int n) {
            StringBuilder sb = new StringBuilder();
            while (n > 0) {
                n--;
                sb.append((char) ('A' + n % 26));
                n /= 26;
            }
            return sb.reverse().toString();
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(log26 n)  |  Space: O(log26 n) stack space
    // Recursive — clean and interview-friendly
    // ============================================================
    static class Best {
        /**
         * Recursive approach: base case n==0 returns empty string.
         * The recursion computes the prefix first, then appends the current char.
         */
        public static String convertToTitle(int n) {
            if (n == 0) return "";
            n--;                                         // 0-indexed
            return convertToTitle(n / 26) + (char) ('A' + n % 26);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Excel Column Title ===");
        int[][] tests = {{1, 0}, {26, 0}, {27, 0}, {28, 0}, {701, 0}, {702, 0}, {703, 0}};
        String[] expected = {"A", "Z", "AA", "AB", "ZY", "ZZ", "AAA"};

        for (int i = 0; i < expected.length; i++) {
            int n = tests[i][0];
            String b  = BruteForce.convertToTitle(n);
            String o  = Optimal.convertToTitle(n);
            String be = Best.convertToTitle(n);
            String status = (b.equals(o) && o.equals(be) && be.equals(expected[i])) ? "OK" : "FAIL";
            System.out.printf("n=%-4d | Brute: %-5s | Optimal: %-5s | Best: %-5s | Expected: %-5s | %s%n",
                    n, b, o, be, expected[i], status);
        }
    }
}
