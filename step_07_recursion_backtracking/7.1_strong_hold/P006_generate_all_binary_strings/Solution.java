/**
 * Problem: Generate All Binary Strings
 * Difficulty: MEDIUM | XP: 25
 *
 * Generate all binary strings of length N such that no two consecutive 1s appear.
 * Real-life use: Constraint satisfaction, combinatorial enumeration, testing patterns.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Generate all 2^N binary strings, then filter out those with consecutive 1s.
    // Time: O(N * 2^N)  |  Space: O(N * 2^N)
    // ============================================================
    static class BruteForce {
        public static List<String> generate(int n) {
            List<String> all = new ArrayList<>();
            generateAll("", n, all);
            List<String> result = new ArrayList<>();
            for (String s : all) {
                if (isValid(s)) result.add(s);
            }
            return result;
        }

        private static void generateAll(String current, int n, List<String> all) {
            if (current.length() == n) {
                all.add(current);
                return;
            }
            generateAll(current + "0", n, all);
            generateAll(current + "1", n, all);
        }

        private static boolean isValid(String s) {
            for (int i = 0; i < s.length() - 1; i++) {
                if (s.charAt(i) == '1' && s.charAt(i + 1) == '1') return false;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Backtracking: prune immediately when last char was '1' — skip placing another '1'.
    // Time: O(N * F(N)) where F(N) ~ Fibonacci(N+2) valid strings  |  Space: O(N)
    // ============================================================
    static class Optimal {
        public static List<String> generate(int n) {
            List<String> result = new ArrayList<>();
            backtrack(new char[n], 0, result);
            return result;
        }

        private static void backtrack(char[] arr, int pos, List<String> result) {
            if (pos == arr.length) {
                result.add(new String(arr));
                return;
            }
            // Always place '0'
            arr[pos] = '0';
            backtrack(arr, pos + 1, result);

            // Place '1' only if previous was NOT '1'
            if (pos == 0 || arr[pos - 1] != '1') {
                arr[pos] = '1';
                backtrack(arr, pos + 1, result);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Iterative DP-style construction: build strings level by level.
    // Each string ending in '0' can extend with '0' or '1'.
    // Each string ending in '1' can only extend with '0'.
    // Time: O(N * F(N))  |  Space: O(F(N)) — no recursion stack
    // ============================================================
    static class Best {
        public static List<String> generate(int n) {
            List<String> current = new ArrayList<>();
            current.add("0");
            current.add("1");
            for (int i = 1; i < n; i++) {
                List<String> next = new ArrayList<>();
                for (String s : current) {
                    next.add(s + "0");
                    if (s.charAt(s.length() - 1) != '1') {
                        next.add(s + "1");
                    }
                }
                current = next;
            }
            return current;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Generate All Binary Strings (No Consecutive 1s) ===");

        int n = 4;
        System.out.println("\nN = " + n);

        List<String> brute = BruteForce.generate(n);
        System.out.println("Brute  (" + brute.size() + "): " + brute);

        List<String> optimal = Optimal.generate(n);
        System.out.println("Optimal(" + optimal.size() + "): " + optimal);

        List<String> best = Best.generate(n);
        System.out.println("Best   (" + best.size() + "): " + best);

        // N=3: expected 5 strings (000,001,010,100,101)
        System.out.println("\nN = 3");
        System.out.println("Optimal: " + Optimal.generate(3));

        // Verify counts match Fibonacci pattern: F(N+2)
        for (int i = 1; i <= 6; i++) {
            System.out.println("N=" + i + " count=" + Optimal.generate(i).size());
        }
    }
}
