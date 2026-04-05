/**
 * Problem: Largest Odd Number in String (LeetCode #1903)
 * Difficulty: EASY | XP: 10
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check All Substrings Starting at 0
    // Time: O(n)  |  Space: O(1) extra
    //
    // Since the largest value substring must start at index 0,
    // we only need to check substrings num[0..j] for each j.
    // Scan from right: first odd last-digit gives the answer.
    //
    // (A true brute force checking ALL substrings would be O(n^2)
    //  but even the "brute" observation that we need prefix-substrings
    //  reduces this. Shown here with explicit substring creation.)
    // ============================================================
    static class BruteForce {
        public String largestOddNumber(String num) {
            // Check all prefix substrings from longest to shortest
            for (int j = num.length() - 1; j >= 0; j--) {
                int digit = num.charAt(j) - '0';
                if (digit % 2 == 1) {
                    return num.substring(0, j + 1);
                }
            }
            return "";
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Right-Scan for Last Odd Digit
    // Time: O(n)  |  Space: O(1) extra
    //
    // A number is odd iff its last digit is odd. The largest
    // substring starting at index 0 ending at an odd digit is
    // the answer. Scan from the right to find the first odd digit.
    // ============================================================
    static class Optimal {
        public String largestOddNumber(String num) {
            for (int i = num.length() - 1; i >= 0; i--) {
                char c = num.charAt(i);
                // Odd digits: '1','3','5','7','9' -- all have odd ASCII values
                if ((c - '0') % 2 != 0) {
                    return num.substring(0, i + 1);
                }
            }
            return "";
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same as Optimal (provably optimal)
    // Time: O(n)  |  Space: O(1) extra
    //
    // Compact variant using bitwise check for oddness.
    // A digit character is odd iff (c & 1) == 1 (works because
    // '0' is 48 (even), '1' is 49 (odd), etc.)
    // ============================================================
    static class Best {
        public String largestOddNumber(String num) {
            for (int i = num.length() - 1; i >= 0; i--) {
                if ((num.charAt(i) & 1) == 1) {
                    return num.substring(0, i + 1);
                }
            }
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Largest Odd Number in String ===\n");

        String[] tests = {"52", "4206", "35427", "2468", "10"};
        String[] expected = {"5", "", "35427", "", "1"};

        for (int t = 0; t < tests.length; t++) {
            String num = tests[t];
            System.out.println("Input:    \"" + num + "\"");
            System.out.println("Expected: \"" + expected[t] + "\"");
            System.out.println("Brute:    \"" + new BruteForce().largestOddNumber(num) + "\"");
            System.out.println("Optimal:  \"" + new Optimal().largestOddNumber(num) + "\"");
            System.out.println("Best:     \"" + new Best().largestOddNumber(num) + "\"");
            System.out.println();
        }
    }
}
