/**
 * Problem: Check Palindrome String (Recursion)
 * Difficulty: EASY | XP: 10
 *
 * Determine whether a string is a palindrome using recursion.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Reverse and Compare
    // Time: O(N)  |  Space: O(N)
    // Reverse the string and check equality.
    // ============================================================
    static class BruteForce {
        public static boolean isPalindrome(String s) {
            String reversed = new StringBuilder(s).reverse().toString();
            return s.equals(reversed);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Two-Pointer
    // Time: O(N)  |  Space: O(N) recursion stack
    // Compare endpoints, recurse on inner substring.
    // ============================================================
    static class Optimal {
        public static boolean isPalindrome(String s) {
            return check(s, 0, s.length() - 1);
        }

        private static boolean check(String s, int left, int right) {
            // Base case: pointers met or crossed
            if (left >= right) return true;
            // Mismatch found
            if (s.charAt(left) != s.charAt(right)) return false;
            // Recurse on inner substring
            return check(s, left + 1, right - 1);
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative Two-Pointer
    // Time: O(N)  |  Space: O(1)
    // Same logic as recursive, but with a while loop.
    // ============================================================
    static class Best {
        public static boolean isPalindrome(String s) {
            int left = 0, right = s.length() - 1;
            while (left < right) {
                if (s.charAt(left) != s.charAt(right)) return false;
                left++;
                right--;
            }
            return true;
        }
    }

    // ============================================================
    // TESTS
    // ============================================================
    public static void main(String[] args) {
        String[] testCases = {"", "a", "ab", "aa", "madam", "racecar", "hello", "abba", "abcba"};
        boolean[] expected = {true, true, false, true, true, true, false, true, true};

        System.out.println("=== Check Palindrome String ===\n");
        for (int i = 0; i < testCases.length; i++) {
            String s = testCases[i];
            boolean b = BruteForce.isPalindrome(s);
            boolean o = Optimal.isPalindrome(s);
            boolean bt = Best.isPalindrome(s);

            String status = (b == expected[i] && o == expected[i] && bt == expected[i]) ? "PASS" : "FAIL";
            System.out.printf("[%s] s=%-12s | Brute=%-5b | Recursive=%-5b | Iterative=%-5b | Expected=%-5b%n",
                    status, "\"" + s + "\"", b, o, bt, expected[i]);
        }
    }
}
