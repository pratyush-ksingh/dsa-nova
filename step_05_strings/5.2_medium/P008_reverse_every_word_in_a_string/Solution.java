/**
 * Problem: Reverse Words in a String (LeetCode 151)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a string s, reverse the order of the words.
 * Words are separated by single spaces in the output; no leading/trailing spaces.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n)  |  Space: O(n)
    // Split on whitespace regex, filter empties, reverse array, join.
    // ============================================================

    /**
     * Uses String.trim() + split on "\\s+" to get a clean word array,
     * then manually reverses it and joins with a single space.
     * Java's split("\\s+") handles multiple consecutive spaces.
     */
    public static String bruteForce(String s) {
        String[] words = s.trim().split("\\s+");
        int lo = 0, hi = words.length - 1;
        while (lo < hi) {
            String tmp = words[lo];
            words[lo] = words[hi];
            words[hi] = tmp;
            lo++;
            hi--;
        }
        return String.join(" ", words);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (in-place on char array — O(1) extra)
    // Time: O(n)  |  Space: O(n) for char array; O(1) extra work
    // 1. Strip/collapse spaces into a clean char array.
    // 2. Reverse the entire array.
    // 3. Reverse each individual word.
    // ============================================================

    /**
     * Classic two-step reversal on a char array:
     *  (a) Reverse whole string  → word order reversed, each word backwards.
     *  (b) Reverse each word     → each word restored to correct order.
     * Space compression (multiple spaces → single space) is done first
     * to keep the output clean.
     */
    public static String optimal(String s) {
        // Step 1: build cleaned char array (single spaces, no leading/trailing)
        char[] arr = buildClean(s);
        int n = arr.length;

        // Step 2: reverse the whole array
        reverse(arr, 0, n - 1);

        // Step 3: reverse each word
        int start = 0;
        for (int i = 0; i <= n; i++) {
            if (i == n || arr[i] == ' ') {
                reverse(arr, start, i - 1);
                start = i + 1;
            }
        }
        return new String(arr);
    }

    private static char[] buildClean(String s) {
        // Equivalent to String.join(" ", s.trim().split("\\s+"))
        String[] words = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(words[i]);
        }
        return sb.toString().toCharArray();
    }

    private static void reverse(char[] arr, int lo, int hi) {
        while (lo < hi) {
            char tmp = arr[lo];
            arr[lo] = arr[hi];
            arr[hi] = tmp;
            lo++;
            hi--;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  (two pointers scanning from the end)
    // Time: O(n)  |  Space: O(n)
    // Walk backwards collecting words; append to StringBuilder.
    // One pass, no double-reversal, easiest to explain under pressure.
    // ============================================================

    /**
     * Two-pointer scan from the right end:
     * 'right' marks the end of the current word being extracted.
     * 'left' walks left until it hits a space (or the start).
     * Words are naturally collected in reversed order.
     */
    public static String best(String s) {
        StringBuilder sb = new StringBuilder();
        int right = s.length() - 1;

        while (right >= 0) {
            // Skip spaces
            while (right >= 0 && s.charAt(right) == ' ') right--;
            if (right < 0) break;

            // Find start of this word
            int left = right;
            while (left >= 0 && s.charAt(left) != ' ') left--;

            // Append word: s[left+1 .. right]
            if (sb.length() > 0) sb.append(' ');
            sb.append(s, left + 1, right + 1);
            right = left - 1;
        }
        return sb.toString();
    }

    // ============================================================
    // MAIN — smoke tests
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Reverse Words in a String ===\n");

        String[][] tests = {
            {"the sky is blue",          "blue is sky the"},
            {"  hello world  ",          "world hello"},
            {"a good   example",         "example good a"},
            {"  Bob    Loves  Alice   ", "Alice Loves Bob"},
        };

        for (String[] tc : tests) {
            String input    = tc[0];
            String expected = tc[1];
            String b   = bruteForce(input);
            String o   = optimal(input);
            String bst = best(input);
            String status = (b.equals(expected) && o.equals(expected) && bst.equals(expected))
                            ? "PASS" : "FAIL";
            System.out.printf("[%s] Input:   \"%s\"%n", status, input);
            System.out.printf("       Brute:   \"%s\"%n", b);
            System.out.printf("       Optimal: \"%s\"%n", o);
            System.out.printf("       Best:    \"%s\"%n", bst);
            System.out.printf("       Expect:  \"%s\"%n%n", expected);
        }
    }
}
