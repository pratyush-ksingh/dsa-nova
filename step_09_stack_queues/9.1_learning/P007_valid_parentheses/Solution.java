/**
 * Problem: Valid Parentheses
 * Difficulty: EASY | XP: 10
 *
 * Given a string containing only '(', ')', '{', '}', '[', ']',
 * determine if the input string is valid.
 * LeetCode #20 - Valid Parentheses
 *
 * @author DSA_Nova
 */
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Repeated Removal
    // Time: O(n^2)  |  Space: O(n)
    // Repeatedly remove "()", "[]", "{}" until no more can be removed.
    // ============================================================
    static class BruteForce {
        public static boolean isValid(String s) {
            String prev = "";
            while (!s.equals(prev)) {
                prev = s;
                s = s.replace("()", "");
                s = s.replace("[]", "");
                s = s.replace("{}", "");
            }
            return s.isEmpty();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Stack-Based Matching
    // Time: O(n)  |  Space: O(n)
    // Push openers, pop on closers, verify match.
    // ============================================================
    static class Optimal {
        public static boolean isValid(String s) {
            Stack<Character> stack = new Stack<>();

            for (char c : s.toCharArray()) {
                if (c == '(' || c == '[' || c == '{') {
                    stack.push(c);
                } else {
                    // It's a closer
                    if (stack.isEmpty()) return false;

                    char top = stack.pop();
                    if (c == ')' && top != '(') return false;
                    if (c == ']' && top != '[') return false;
                    if (c == '}' && top != '{') return false;
                }
            }

            return stack.isEmpty();  // All openers must be matched
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Stack with Map Lookup (Production-Ready)
    // Time: O(n)  |  Space: O(n)
    // Map-based bracket matching for cleaner, extensible code.
    // ============================================================
    static class Best {
        public static boolean isValid(String s) {
            // Early exit: odd-length strings can never be valid
            if (s.length() % 2 != 0) return false;

            // Map each closer to its expected opener
            Map<Character, Character> map = new HashMap<>();
            map.put(')', '(');
            map.put(']', '[');
            map.put('}', '{');

            Stack<Character> stack = new Stack<>();

            for (char c : s.toCharArray()) {
                if (map.containsKey(c)) {
                    // It's a closer -- check for matching opener
                    if (stack.isEmpty() || stack.pop() != map.get(c)) {
                        return false;
                    }
                } else {
                    // It's an opener -- push to stack
                    stack.push(c);
                }
            }

            return stack.isEmpty();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Valid Parentheses ===\n");

        String[] testCases = {"()", "()[]{}", "(]", "([)]", "{[]}", "", "(", "((([[]])))"};

        for (String s : testCases) {
            String display = s.isEmpty() ? "(empty)" : s;
            System.out.printf("Input: %-15s  Brute: %-5s  Optimal: %-5s  Best: %-5s%n",
                display,
                BruteForce.isValid(s),
                Optimal.isValid(s),
                Best.isValid(s));
        }
    }
}
