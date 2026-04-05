import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(1) — at most 3^4 * 4 iterations  |  Space: O(1)
// Try all ways to place 3 dots at positions i,j,k.
// Check each resulting 4 segments for validity.
// ============================================================
class BruteForce {
    private static boolean valid(String s) {
        if (s.isEmpty() || s.length() > 3) return false;
        if (s.length() > 1 && s.charAt(0) == '0') return false;
        int val = Integer.parseInt(s);
        return val >= 0 && val <= 255;
    }

    public static List<String> solve(String s) {
        List<String> result = new ArrayList<>();
        int n = s.length();
        for (int i = 1; i <= 3 && i < n; i++) {
            for (int j = i + 1; j <= i + 3 && j < n; j++) {
                for (int k = j + 1; k <= j + 3 && k < n; k++) {
                    String s1 = s.substring(0, i);
                    String s2 = s.substring(i, j);
                    String s3 = s.substring(j, k);
                    String s4 = s.substring(k);
                    if (valid(s1) && valid(s2) && valid(s3) && valid(s4)) {
                        result.add(s1 + "." + s2 + "." + s3 + "." + s4);
                    }
                }
            }
        }
        return result;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Backtracking
// Time: O(1) bounded  |  Space: O(1)
// Use backtracking: build each octet (1-3 digits), recurse for next octet.
// When 4 octets are placed and we've consumed all digits, record result.
// ============================================================
class Optimal {
    private static boolean isValid(String s) {
        if (s.isEmpty() || s.length() > 3) return false;
        if (s.length() > 1 && s.charAt(0) == '0') return false;
        return Integer.parseInt(s) <= 255;
    }

    private static void backtrack(String s, int start, List<String> parts, List<String> result) {
        if (parts.size() == 4) {
            if (start == s.length()) result.add(String.join(".", parts));
            return;
        }
        for (int len = 1; len <= 3; len++) {
            if (start + len > s.length()) break;
            String seg = s.substring(start, start + len);
            if (isValid(seg)) {
                parts.add(seg);
                backtrack(s, start + len, parts, result);
                parts.remove(parts.size() - 1);
            }
        }
    }

    public static List<String> solve(String s) {
        List<String> result = new ArrayList<>();
        backtrack(s, 0, new ArrayList<>(), result);
        return result;
    }
}

// ============================================================
// APPROACH 3: BEST - Backtracking with pruning (remaining length check)
// Time: O(1) bounded  |  Space: O(1)
// Same backtracking with early exit when remaining characters cannot
// form valid octets (each octet 1-3 chars, 4 octets total).
// ============================================================
class Best {
    private static void bt(String s, int pos, int part, List<String> cur, List<String> res) {
        int remaining = s.length() - pos;
        int partsLeft = 4 - part;
        if (remaining < partsLeft || remaining > 3 * partsLeft) return;
        if (part == 4 && pos == s.length()) { res.add(String.join(".", cur)); return; }
        if (part == 4) return;
        for (int len = 1; len <= 3 && pos + len <= s.length(); len++) {
            String seg = s.substring(pos, pos + len);
            if (len > 1 && seg.charAt(0) == '0') break;
            if (Integer.parseInt(seg) > 255) break;
            cur.add(seg);
            bt(s, pos + len, part + 1, cur, res);
            cur.remove(cur.size() - 1);
        }
    }

    public static List<String> solve(String s) {
        List<String> res = new ArrayList<>();
        bt(s, 0, 0, new ArrayList<>(), res);
        return res;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Valid IP Addresses ===");

        String[] tests = {"25525511135", "0000", "1111", "010010"};
        for (String s : tests) {
            System.out.println("Input: \"" + s + "\"");
            System.out.println("  Brute:    " + BruteForce.solve(s));
            System.out.println("  Optimal:  " + Optimal.solve(s));
            System.out.println("  Best:     " + Best.solve(s));
        }
    }
}
