/**
 * Problem: Letter Combinations of Phone Number
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a string of digits 2-9, return all possible letter combinations
 * from the phone keypad (T9 mapping). Empty input returns empty list.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static final String[] PHONE = {
        "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
    };

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Iterative BFS, expand layer by layer
    // Time: O(4^n * n)  |  Space: O(4^n * n)
    // For each digit, extend every current partial string with each mapped letter
    // ============================================================
    public static List<String> bruteForce(String digits) {
        if (digits == null || digits.isEmpty()) return new ArrayList<>();
        List<String> result = new ArrayList<>();
        result.add("");
        for (char d : digits.toCharArray()) {
            String letters = PHONE[d - '0'];
            List<String> next = new ArrayList<>();
            for (String prefix : result) {
                for (char c : letters.toCharArray()) {
                    next.add(prefix + c);
                }
            }
            result = next;
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Backtracking DFS with StringBuilder
    // Time: O(4^n * n)  |  Space: O(n) recursion stack (excluding output)
    // Avoids creating intermediate strings; uses StringBuilder in-place
    // ============================================================
    public static List<String> optimal(String digits) {
        List<String> res = new ArrayList<>();
        if (digits == null || digits.isEmpty()) return res;
        dfs(digits, 0, new StringBuilder(), res);
        return res;
    }

    private static void dfs(String digits, int idx, StringBuilder sb, List<String> res) {
        if (idx == digits.length()) {
            res.add(sb.toString());
            return;
        }
        for (char c : PHONE[digits.charAt(idx) - '0'].toCharArray()) {
            sb.append(c);
            dfs(digits, idx + 1, sb, res);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Queue-based BFS (iterative, no recursion overhead)
    // Time: O(4^n * n)  |  Space: O(4^n * n)
    // Processes each digit level, expanding the queue entries
    // ============================================================
    public static List<String> best(String digits) {
        if (digits == null || digits.isEmpty()) return new ArrayList<>();
        Deque<String> queue = new ArrayDeque<>();
        queue.offer("");
        for (char d : digits.toCharArray()) {
            String letters = PHONE[d - '0'];
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String curr = queue.poll();
                for (char c : letters.toCharArray()) {
                    queue.offer(curr + c);
                }
            }
        }
        return new ArrayList<>(queue);
    }

    public static void main(String[] args) {
        System.out.println("=== Letter Combinations of Phone Number ===");

        String digits = "23";
        System.out.println("digits=\"" + digits + "\"");
        System.out.println("Brute:   " + bruteForce(digits));
        System.out.println("Optimal: " + optimal(digits));
        System.out.println("Best:    " + best(digits));

        digits = "";
        System.out.println("\ndigits=\"\" (empty)");
        System.out.println("Brute:   " + bruteForce(digits));
        System.out.println("Optimal: " + optimal(digits));
        System.out.println("Best:    " + best(digits));

        digits = "79";
        System.out.println("\ndigits=\"79\" (pqrs + wxyz)");
        System.out.println("Brute:   " + bruteForce(digits));
        System.out.println("Optimal: " + optimal(digits));
        System.out.println("Best:    " + best(digits));
    }
}
