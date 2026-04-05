import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n * k)  |  Space: O(n)
// Remove k digits one at a time. Each time, find and remove
// the first digit that is greater than the next digit.
// If no such digit exists, remove the last one.
// ============================================================
class BruteForce {
    public static String solve(String num, int k) {
        StringBuilder sb = new StringBuilder(num);
        for (int i = 0; i < k; i++) {
            int idx = 0;
            while (idx < sb.length() - 1 && sb.charAt(idx) <= sb.charAt(idx + 1)) {
                idx++;
            }
            sb.deleteCharAt(idx);
        }
        // Remove leading zeros
        int start = 0;
        while (start < sb.length() - 1 && sb.charAt(start) == '0') start++;
        return sb.substring(start);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(n)
// Monotonic stack: maintain an increasing stack of digits.
// For each digit, pop larger digits from the stack (up to k times).
// ============================================================
class Optimal {
    public static String solve(String num, int k) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : num.toCharArray()) {
            while (k > 0 && !stack.isEmpty() && stack.peek() > c) {
                stack.pop();
                k--;
            }
            stack.push(c);
        }
        // If k still > 0, remove from the top (largest end of increasing sequence)
        while (k-- > 0) stack.pop();

        // Build result from stack (stack is in reverse order)
        StringBuilder sb = new StringBuilder();
        for (char c : stack) sb.append(c);
        sb.reverse();

        // Remove leading zeros
        int start = 0;
        while (start < sb.length() - 1 && sb.charAt(start) == '0') start++;
        return sb.substring(start);
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(n)
// Same monotonic stack but using a StringBuilder as the stack
// (append/deleteCharAt at end) for simpler index management.
// ============================================================
class Best {
    public static String solve(String num, int k) {
        StringBuilder stack = new StringBuilder();
        for (char c : num.toCharArray()) {
            while (k > 0 && stack.length() > 0 && stack.charAt(stack.length() - 1) > c) {
                stack.deleteCharAt(stack.length() - 1);
                k--;
            }
            stack.append(c);
        }
        // Remove remaining digits from the end
        stack.setLength(stack.length() - k);

        // Strip leading zeros
        int start = 0;
        while (start < stack.length() - 1 && stack.charAt(start) == '0') start++;
        return stack.substring(start);
    }
}

public class Solution {
    public static void main(String[] args) {
        String[][] tests = {
            {"1432219", "3", "1219"},
            {"10200",   "1", "200"},
            {"10",      "2", "0"},
            {"9",       "1", "0"},
            {"112",     "1", "11"},
            {"1234567890", "9", "0"},
        };

        System.out.println("=== Remove K Digits ===");
        for (String[] t : tests) {
            String b    = BruteForce.solve(t[0], Integer.parseInt(t[1]));
            String o    = Optimal.solve(t[0], Integer.parseInt(t[1]));
            String best = Best.solve(t[0], Integer.parseInt(t[1]));
            String status = (b.equals(t[2]) && o.equals(t[2]) && best.equals(t[2])) ? "PASS" : "FAIL";
            System.out.printf("num=%-15s k=%-3s | Brute: %-8s | Optimal: %-8s | Best: %-8s | Expected: %-8s | %s%n",
                    t[0], t[1], b, o, best, t[2], status);
        }
    }
}
