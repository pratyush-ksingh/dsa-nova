/**
 * Problem: Longest Common Prefix (LeetCode #14)
 * Difficulty: EASY | XP: 10
 *
 * Find the longest common prefix string amongst an array of strings.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Horizontal Scanning)
// Time: O(S) where S = sum of all chars | Space: O(m)
// ============================================================
class BruteForce {
    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            // Trim prefix until strs[i] starts with it
            while (!strs[i].startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        }
        return prefix;
    }
}

// ============================================================
// Approach 2: Optimal (Vertical Scanning)
// Time: O(n * m) | Space: O(1) extra
// ============================================================
class Optimal {
    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";

        // Find minimum length
        int minLen = strs[0].length();
        for (String s : strs) {
            minLen = Math.min(minLen, s.length());
        }

        // Compare column by column
        for (int col = 0; col < minLen; col++) {
            char c = strs[0].charAt(col);
            for (int i = 1; i < strs.length; i++) {
                if (strs[i].charAt(col) != c) {
                    return strs[0].substring(0, col);
                }
            }
        }
        return strs[0].substring(0, minLen);
    }
}

// ============================================================
// Approach 3: Sort-Based
// Time: O(n * m * log n) | Space: O(m)
// ============================================================
class SortBased {
    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        Arrays.sort(strs);

        // After sorting, compare only the first and last strings
        String first = strs[0];
        String last = strs[strs.length - 1];
        int i = 0;
        while (i < first.length() && i < last.length() && first.charAt(i) == last.charAt(i)) {
            i++;
        }
        return first.substring(0, i);
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Longest Common Prefix ===\n");

        String[][] testCases = {
            {"flower", "flow", "flight"},
            {"dog", "racecar", "car"},
            {"interspecies", "interstellar", "interstate"},
            {"a"},
            {"", "abc"},
            {"abc", "abc", "abc"},
            {"apple", "ape", "art"}
        };
        String[] expected = {"fl", "", "inters", "a", "", "abc", "a"};

        for (int t = 0; t < testCases.length; t++) {
            String[] strs = testCases[t];
            String bruteResult = BruteForce.longestCommonPrefix(strs.clone());
            String optimalResult = Optimal.longestCommonPrefix(strs.clone());
            String sortResult = SortBased.longestCommonPrefix(strs.clone());

            System.out.println("Input:     " + Arrays.toString(strs));
            System.out.println("  Brute:     \"" + bruteResult + "\"");
            System.out.println("  Optimal:   \"" + optimalResult + "\"");
            System.out.println("  SortBased: \"" + sortResult + "\"");
            System.out.println("  Expected:  \"" + expected[t] + "\"");
            System.out.println("  Pass:      " + (bruteResult.equals(expected[t])
                    && optimalResult.equals(expected[t]) && sortResult.equals(expected[t])));
            System.out.println();
        }
    }
}
