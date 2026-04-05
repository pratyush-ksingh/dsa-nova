/**
 * Problem: FizzBuzz
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Print numbers from 1 to n with substitutions:
 *   divisible by 3 and 5 -> "FizzBuzz"
 *   divisible by 3       -> "Fizz"
 *   divisible by 5       -> "Buzz"
 *   otherwise            -> the number as a string
 *
 * @author DSA_Nova
 */
import java.util.ArrayList;
import java.util.List;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- If-Else Chain
    // Time: O(n)  |  Space: O(n)
    // Classic explicit conditional chain; check 15 first to
    // avoid checking both 3 and 5 separately for FizzBuzz.
    // ============================================================
    static class BruteForce {
        public static List<String> fizzBuzz(int n) {
            List<String> result = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                if (i % 15 == 0)      result.add("FizzBuzz");
                else if (i % 3 == 0)  result.add("Fizz");
                else if (i % 5 == 0)  result.add("Buzz");
                else                  result.add(String.valueOf(i));
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- String Concatenation
    // Time: O(n)  |  Space: O(n)
    // Build label by appending "Fizz" and/or "Buzz".
    // Eliminates the separate divisible-by-15 check.
    // ============================================================
    static class Optimal {
        public static List<String> fizzBuzz(int n) {
            List<String> result = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                StringBuilder label = new StringBuilder();
                if (i % 3 == 0) label.append("Fizz");
                if (i % 5 == 0) label.append("Buzz");
                result.add(label.length() > 0 ? label.toString() : String.valueOf(i));
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Rule-Table / Mapping Approach
    // Time: O(n)  |  Space: O(n)
    // Store (divisor, word) pairs; iterate rules to build label.
    // Adding a new rule (e.g., 7 -> "Jazz") requires no code
    // restructuring -- just add one entry to the table.
    // ============================================================
    static class Best {
        public static List<String> fizzBuzz(int n) {
            int[]    divisors = {3,      5};
            String[] words    = {"Fizz", "Buzz"};

            List<String> result = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                StringBuilder label = new StringBuilder();
                for (int r = 0; r < divisors.length; r++) {
                    if (i % divisors[r] == 0) label.append(words[r]);
                }
                result.add(label.length() > 0 ? label.toString() : String.valueOf(i));
            }
            return result;
        }
    }

    public static void main(String[] args) {
        int n = 15;
        System.out.println("=== FizzBuzz ===");
        System.out.println("Brute:   " + BruteForce.fizzBuzz(n));
        System.out.println("Optimal: " + Optimal.fizzBuzz(n));
        System.out.println("Best:    " + Best.fizzBuzz(n));
    }
}
