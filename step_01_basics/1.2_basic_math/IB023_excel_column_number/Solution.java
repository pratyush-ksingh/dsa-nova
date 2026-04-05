/**
 * Problem: Excel Column Number
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Convert an Excel column title (e.g. "A", "AB", "ZY") to its corresponding
 * column number. Treats the title as a base-26 number where A=1..Z=26.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Right-to-Left Positional Value
    // Time: O(n)  |  Space: O(1)
    // Multiply each digit's value by 26^position, summing from right.
    // ============================================================
    static class BruteForce {
        public static int titleToNumber(String s) {
            int result = 0;
            int n = s.length();
            for (int i = 0; i < n; i++) {
                int value = s.charAt(i) - 'A' + 1;   // A=1, B=2, ..., Z=26
                int power = n - 1 - i;                // rightmost position = 0
                result += value * (int) Math.pow(26, power);
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Left-to-Right Horner's Method
    // Time: O(n)  |  Space: O(1)
    // result = result * 26 + digit_value  (no power computation needed)
    // ============================================================
    static class Optimal {
        public static int titleToNumber(String s) {
            int result = 0;
            for (char ch : s.toCharArray()) {
                result = result * 26 + (ch - 'A' + 1);
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same as Optimal (already O(n), O(1))
    // Time: O(n)  |  Space: O(1)
    // Identical to Optimal; Horner's method is the canonical solution.
    // ============================================================
    static class Best {
        public static int titleToNumber(String s) {
            int result = 0;
            for (int i = 0; i < s.length(); i++) {
                result = result * 26 + (s.charAt(i) - 'A' + 1);
            }
            return result;
        }
    }

    public static void main(String[] args) {
        String[][] testCases = {{"A","1"},{"Z","26"},{"AA","27"},{"AB","28"},{"AZ","52"},{"ZY","701"}};
        System.out.println("=== Excel Column Number ===");
        for (String[] tc : testCases) {
            String title = tc[0];
            int expected = Integer.parseInt(tc[1]);
            int b   = BruteForce.titleToNumber(title);
            int o   = Optimal.titleToNumber(title);
            int bst = Best.titleToNumber(title);
            String status = (b == o && o == bst && bst == expected) ? "OK" : "FAIL";
            System.out.printf("  '%s' -> Brute=%d  Optimal=%d  Best=%d  Expected=%d  [%s]%n",
                              title, b, o, bst, expected, status);
        }
    }
}
