import java.util.*;

/**
 * Problem: Justified Text
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given an array of words and a line width L, format the text so that
 * each line has exactly L characters and is fully (left) justified.
 *
 * Rules:
 * - Pack as many words as possible per line (greedy).
 * - Extra spaces are distributed evenly between words; if uneven,
 *   leftmost gaps get more spaces.
 * - The LAST line is left-justified (words separated by single space,
 *   trailing spaces to fill L).
 *
 * @author DSA_Nova
 */
public class Solution {

    // Helper: build one justified line from words[start..end-1]
    static String buildLine(String[] words, int start, int end, int L, boolean isLast) {
        int totalChars = 0;
        for (int i = start; i < end; i++) totalChars += words[i].length();
        int numGaps = end - start - 1;
        StringBuilder sb = new StringBuilder();

        if (isLast || numGaps == 0) {
            // Left-justify: single space between words, trailing spaces
            for (int i = start; i < end; i++) {
                sb.append(words[i]);
                if (i < end - 1) sb.append(' ');
            }
            while (sb.length() < L) sb.append(' ');
        } else {
            int totalSpaces = L - totalChars;
            int spacePerGap = totalSpaces / numGaps;
            int extra = totalSpaces % numGaps;
            for (int i = start; i < end; i++) {
                sb.append(words[i]);
                if (i < end - 1) {
                    int spaces = spacePerGap + (i - start < extra ? 1 : 0);
                    for (int s = 0; s < spaces; s++) sb.append(' ');
                }
            }
        }
        return sb.toString();
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n * L)  |  Space: O(n * L)
    // Greedy line packing: try adding words one by one, commit when
    // the next word doesn't fit. Build each line naively in a loop.
    // ============================================================
    public static List<String> bruteForce(String[] words, int L) {
        List<String> result = new ArrayList<>();
        int i = 0, n = words.length;

        while (i < n) {
            int lineLen = words[i].length();
            int j = i + 1;
            while (j < n && lineLen + 1 + words[j].length() <= L) {
                lineLen += 1 + words[j].length();
                j++;
            }
            boolean isLast = (j == n);
            result.add(buildLine(words, i, j, L, isLast));
            i = j;
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Greedy with two-pointer line scan
    // Time: O(n * L)  |  Space: O(n * L)
    // Same algorithm but tracks total character count explicitly
    // to avoid repeated string length calls inside the loop.
    // ============================================================
    public static List<String> optimal(String[] words, int L) {
        List<String> result = new ArrayList<>();
        int n = words.length;
        int[] wordLen = new int[n];
        for (int i = 0; i < n; i++) wordLen[i] = words[i].length();

        int i = 0;
        while (i < n) {
            int lineChars = wordLen[i];
            int j = i + 1;
            while (j < n && lineChars + 1 + wordLen[j] <= L) {
                lineChars += 1 + wordLen[j];
                j++;
            }
            // words[i..j-1] go on this line
            int numWords = j - i;
            int numGaps = numWords - 1;
            boolean isLast = (j == n);
            StringBuilder sb = new StringBuilder();

            if (isLast || numGaps == 0) {
                for (int k = i; k < j; k++) {
                    sb.append(words[k]);
                    if (k < j - 1) sb.append(' ');
                }
                while (sb.length() < L) sb.append(' ');
            } else {
                int totalSpaces = L - lineChars + numGaps; // remove the 1-space counts
                // lineChars already includes 1 space per gap, so actual char count = sum of word lengths
                int totalWordChars = 0;
                for (int k = i; k < j; k++) totalWordChars += wordLen[k];
                totalSpaces = L - totalWordChars;
                int spacePerGap = totalSpaces / numGaps;
                int extra = totalSpaces % numGaps;
                for (int k = i; k < j; k++) {
                    sb.append(words[k]);
                    if (k < j - 1) {
                        int spaces = spacePerGap + (k - i < extra ? 1 : 0);
                        for (int s = 0; s < spaces; s++) sb.append(' ');
                    }
                }
            }
            result.add(sb.toString());
            i = j;
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST — Clean modular implementation
    // Time: O(n * L)  |  Space: O(n * L)
    // Uses the helper method buildLine for clean separation of
    // concerns. Identical algorithm, best readability.
    // ============================================================
    public static List<String> best(String[] words, int L) {
        List<String> result = new ArrayList<>();
        int n = words.length;
        int i = 0;

        while (i < n) {
            int lineLen = words[i].length();
            int j = i + 1;
            while (j < n && lineLen + 1 + words[j].length() <= L) {
                lineLen += 1 + words[j].length();
                j++;
            }
            result.add(buildLine(words, i, j, L, j == n));
            i = j;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Justified Text ===");

        String[] words1 = {"This", "is", "an", "example", "of", "text", "justification."};
        int L1 = 16;
        // Expected:
        // "This    is    an"
        // "example  of text"
        // "justification.  "

        String[] words2 = {"What", "must", "be", "acknowledgment", "shall", "be"};
        int L2 = 16;

        System.out.println("--- Test 1 ---");
        System.out.println("Brute:");
        bruteForce(words1, L1).forEach(s -> System.out.println("|" + s + "|"));
        System.out.println("Best:");
        best(words1, L1).forEach(s -> System.out.println("|" + s + "|"));

        System.out.println("\n--- Test 2 ---");
        System.out.println("Best:");
        best(words2, L2).forEach(s -> System.out.println("|" + s + "|"));
    }
}
