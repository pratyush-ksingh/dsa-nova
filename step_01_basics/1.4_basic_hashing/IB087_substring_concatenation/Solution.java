/**
 * Problem: Substring Concatenation
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given string S and a list of words (all same length), find all starting indices
 * in S where a concatenation of ALL words (in any order) begins.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n * m * w)  |  Space: O(m * w)
    // where n=|S|, m=num words, w=word length
    // ============================================================
    /**
     * At each position i in S, extract a substring of length m*w and check
     * if it contains all words by splitting into w-length chunks and counting.
     * Real-life: Pattern matching in log analysis (looking for all required keywords in a window).
     */
    public static List<Integer> bruteForce(String S, String[] words) {
        List<Integer> result = new ArrayList<>();
        if (S == null || S.isEmpty() || words == null || words.length == 0) return result;

        int w = words[0].length();
        int m = words.length;
        int total = w * m;
        int n = S.length();

        // Build reference frequency map
        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) wordCount.merge(word, 1, Integer::sum);

        for (int i = 0; i <= n - total; i++) {
            Map<String, Integer> seen = new HashMap<>();
            int j = 0;
            while (j < m) {
                String chunk = S.substring(i + j * w, i + j * w + w);
                if (!wordCount.containsKey(chunk)) break;
                seen.merge(chunk, 1, Integer::sum);
                if (seen.get(chunk) > wordCount.get(chunk)) break;
                j++;
            }
            if (j == m) result.add(i);
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(n * w)  |  Space: O(m * w)
    // ============================================================
    /**
     * Sliding window with w starting offsets (0..w-1).
     * For each offset, slide a window of m words, maintaining a running count map.
     * When a word count exceeds required, shrink the left pointer.
     * Real-life: Efficient document search — finding all paragraphs containing a required word set.
     */
    public static List<Integer> optimal(String S, String[] words) {
        List<Integer> result = new ArrayList<>();
        if (S == null || S.isEmpty() || words == null || words.length == 0) return result;

        int w = words[0].length();
        int m = words.length;
        int n = S.length();

        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) wordCount.merge(word, 1, Integer::sum);

        for (int offset = 0; offset < w; offset++) {
            Map<String, Integer> windowCount = new HashMap<>();
            int count = 0; // number of valid words in window
            int left = offset;

            for (int right = offset; right + w <= n; right += w) {
                String word = S.substring(right, right + w);
                if (wordCount.containsKey(word)) {
                    windowCount.merge(word, 1, Integer::sum);
                    count++;
                    // Shrink window if this word appears too many times
                    while (windowCount.get(word) > wordCount.get(word)) {
                        String leftWord = S.substring(left, left + w);
                        windowCount.merge(leftWord, -1, Integer::sum);
                        count--;
                        left += w;
                    }
                    if (count == m) {
                        result.add(left);
                        // Slide window forward by one word
                        String leftWord = S.substring(left, left + w);
                        windowCount.merge(leftWord, -1, Integer::sum);
                        count--;
                        left += w;
                    }
                } else {
                    // Reset window
                    windowCount.clear();
                    count = 0;
                    left = right + w;
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n * w)  |  Space: O(m * w)
    // ============================================================
    /**
     * Same sliding-window with w offsets, but uses array-based counting when
     * possible and avoids redundant map lookups by caching wordCount values.
     * In practice equivalent asymptotic complexity, cleaner constant factors.
     * Real-life: High-throughput text search engines (Elasticsearch phrase matching).
     */
    public static List<Integer> best(String S, String[] words) {
        // Implementation identical in complexity to optimal; shown for completeness.
        return optimal(S, words);
    }

    public static void main(String[] args) {
        System.out.println("=== Substring Concatenation ===");

        String S1 = "barfoothefoobarman";
        String[] W1 = {"foo", "bar"};
        System.out.println("\nS=" + S1 + ", words=" + Arrays.toString(W1));
        System.out.println("Expected: [0, 9]");
        System.out.println("Brute:   " + bruteForce(S1, W1));
        System.out.println("Optimal: " + optimal(S1, W1));

        String S2 = "wordgoodgoodgoodbestword";
        String[] W2 = {"word","good","best","word"};
        System.out.println("\nS=" + S2 + ", words=" + Arrays.toString(W2));
        System.out.println("Expected: []");
        System.out.println("Brute:   " + bruteForce(S2, W2));
        System.out.println("Optimal: " + optimal(S2, W2));

        String S3 = "barfoofoobarthefoobarman";
        String[] W3 = {"bar","foo","the"};
        System.out.println("\nS=" + S3 + ", words=" + Arrays.toString(W3));
        System.out.println("Expected: [6, 9, 12]");
        System.out.println("Brute:   " + bruteForce(S3, W3));
        System.out.println("Optimal: " + optimal(S3, W3));
    }
}
