/**
 * Problem: Length of Last Word
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit | LeetCode #58
 *
 * Key Insight: Scan from right -- skip trailing spaces, then count non-spaces.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Split and Take Last
    // Time: O(n)  |  Space: O(n)
    //
    // Split by spaces, return length of last non-empty token.
    // ============================================================
    public static int bruteForce(String s) {
        String trimmed = s.trim();
        if (trimmed.isEmpty()) return 0;
        String[] words = trimmed.split("\\s+");
        return words[words.length - 1].length();
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Right-to-Left Scan
    // Time: O(k)  |  Space: O(1)
    //
    // Phase 1: skip trailing spaces.
    // Phase 2: count non-space characters.
    // ============================================================
    public static int optimal(String s) {
        int i = s.length() - 1;

        // Phase 1: skip trailing spaces
        while (i >= 0 && s.charAt(i) == ' ') {
            i--;
        }

        // Phase 2: count last word characters
        int length = 0;
        while (i >= 0 && s.charAt(i) != ' ') {
            length++;
            i--;
        }

        return length;
    }

    // ============================================================
    // APPROACH 3: BEST -- Same as Optimal (single loop variant)
    // Time: O(k)  |  Space: O(1)
    //
    // Combines both phases into a single loop with a flag.
    // ============================================================
    public static int best(String s) {
        int length = 0;
        boolean foundWord = false;

        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != ' ') {
                foundWord = true;
                length++;
            } else if (foundWord) {
                // Hit a space after finding the word -- done
                break;
            }
        }

        return length;
    }

    public static void main(String[] args) {
        System.out.println("=== Length of Last Word ===");

        String[] tests = {
            "Hello World",
            "   fly me   to   the moon  ",
            "luffy is still joyboy",
            "a",
            "   "
        };

        for (String t : tests) {
            System.out.printf("Input: \"%s\"%n", t);
            System.out.printf("  Brute:   %d%n", bruteForce(t));
            System.out.printf("  Optimal: %d%n", optimal(t));
            System.out.printf("  Best:    %d%n", best(t));
            System.out.println();
        }
    }
}
