/**
 * Problem: Isomorphic Strings (LeetCode #205)
 * Difficulty: EASY | XP: 10
 *
 * Check if two strings are isomorphic (one-to-one character mapping).
 *
 * @author DSA_Nova
 */
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Transform to Pattern)
// Time: O(n) | Space: O(n)
// ============================================================
class BruteForce {
    public static boolean isIsomorphic(String s, String t) {
        return toPattern(s).equals(toPattern(t));
    }

    private static String toPattern(String str) {
        Map<Character, Integer> firstOccurrence = new HashMap<>();
        StringBuilder pattern = new StringBuilder();
        int counter = 0;
        for (char c : str.toCharArray()) {
            if (!firstOccurrence.containsKey(c)) {
                firstOccurrence.put(c, counter++);
            }
            pattern.append(firstOccurrence.get(c)).append(',');
        }
        return pattern.toString();
    }
}

// ============================================================
// Approach 2: Optimal (Dual HashMap)
// Time: O(n) | Space: O(k) where k = charset size
// ============================================================
class Optimal {
    public static boolean isIsomorphic(String s, String t) {
        Map<Character, Character> mapST = new HashMap<>();
        Map<Character, Character> mapTS = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i);
            char tc = t.charAt(i);

            // Check s -> t mapping
            if (mapST.containsKey(sc)) {
                if (mapST.get(sc) != tc) return false;
            } else {
                mapST.put(sc, tc);
            }

            // Check t -> s mapping
            if (mapTS.containsKey(tc)) {
                if (mapTS.get(tc) != sc) return false;
            } else {
                mapTS.put(tc, sc);
            }
        }
        return true;
    }
}

// ============================================================
// Approach 3: Elegant (Last-Seen Index Comparison)
// Time: O(n) | Space: O(1) -- fixed 256-size arrays
// ============================================================
class LastSeen {
    public static boolean isIsomorphic(String s, String t) {
        int[] lastS = new int[256];
        int[] lastT = new int[256];
        Arrays.fill(lastS, -1);
        Arrays.fill(lastT, -1);

        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i);
            char tc = t.charAt(i);
            if (lastS[sc] != lastT[tc]) {
                return false;
            }
            lastS[sc] = i;
            lastT[tc] = i;
        }
        return true;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Isomorphic Strings ===\n");

        String[][] testCases = {
            {"egg", "add"},
            {"foo", "bar"},
            {"paper", "title"},
            {"badc", "baba"},
            {"ab", "aa"},
            {"a", "b"},
            {"abc", "abc"},
            {"aaa", "bbb"}
        };
        boolean[] expected = {true, false, true, false, false, true, true, true};

        for (int i = 0; i < testCases.length; i++) {
            String s = testCases[i][0];
            String t = testCases[i][1];

            boolean bruteResult = BruteForce.isIsomorphic(s, t);
            boolean optimalResult = Optimal.isIsomorphic(s, t);
            boolean lastSeenResult = LastSeen.isIsomorphic(s, t);

            System.out.println("s=\"" + s + "\", t=\"" + t + "\"");
            System.out.println("  Brute:    " + bruteResult);
            System.out.println("  Optimal:  " + optimalResult);
            System.out.println("  LastSeen: " + lastSeenResult);
            System.out.println("  Expected: " + expected[i]);
            System.out.println("  Pass:     " + (bruteResult == expected[i]
                    && optimalResult == expected[i] && lastSeenResult == expected[i]));
            System.out.println();
        }
    }
}
