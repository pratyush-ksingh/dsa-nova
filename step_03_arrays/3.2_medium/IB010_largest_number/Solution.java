import java.util.*;

/**
 * Problem: Largest Number
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a list of non-negative integers, arrange them so that they form
 * the largest possible number and return it as a string.
 *
 * Key insight: to compare which ordering (ab vs ba) is larger, compare
 * the string concatenations: if "ab" > "ba", then a should come before b.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n! * n)  |  Space: O(n)
    // Generate all permutations and pick the one that forms the
    // largest number (correct but impractical for large input).
    // ============================================================
    static String maxPerm = "";

    private static void permute(String[] nums, int start) {
        if (start == nums.length) {
            String candidate = String.join("", nums);
            // Compare as numbers (string compare works since same length after join)
            if (maxPerm.isEmpty() || compareLarger(candidate, maxPerm) > 0) {
                maxPerm = candidate;
            }
            return;
        }
        for (int i = start; i < nums.length; i++) {
            String tmp = nums[start]; nums[start] = nums[i]; nums[i] = tmp;
            permute(nums, start + 1);
            tmp = nums[start]; nums[start] = nums[i]; nums[i] = tmp;
        }
    }

    // Returns positive if a > b as large numbers (string comparison)
    private static int compareLarger(String a, String b) {
        // Pad shorter string with its first char (for equal-length comparison)
        // Actually direct string compare works when same length
        if (a.length() == b.length()) return a.compareTo(b);
        // Lexicographic compare after normalising — just compare concatenations
        String ab = a + b, ba = b + a;
        return ab.compareTo(ba);
    }

    public static String bruteForce(int[] A) {
        if (A == null || A.length == 0) return "";
        String[] nums = new String[A.length];
        for (int i = 0; i < A.length; i++) nums[i] = String.valueOf(A[i]);
        maxPerm = "";
        permute(nums, 0);
        // Remove leading zeros
        if (maxPerm.charAt(0) == '0') return "0";
        return maxPerm;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Custom Comparator Sort
    // Time: O(n log n * L)  |  Space: O(n * L)  where L = avg digit length
    // Sort using comparator: a > b if (a+b) > (b+a) lexicographically.
    // Then concatenate.
    // ============================================================
    public static String optimal(int[] A) {
        if (A == null || A.length == 0) return "";
        String[] nums = new String[A.length];
        for (int i = 0; i < A.length; i++) nums[i] = String.valueOf(A[i]);

        Arrays.sort(nums, (a, b) -> (b + a).compareTo(a + b));

        if (nums[0].equals("0")) return "0";
        return String.join("", nums);
    }

    // ============================================================
    // APPROACH 3: BEST — Same as Optimal but using StringBuilder
    // Time: O(n log n * L)  |  Space: O(n * L)
    // Identical algorithm; uses StringBuilder for final assembly —
    // more memory-efficient for very large inputs.
    // ============================================================
    public static String best(int[] A) {
        if (A == null || A.length == 0) return "";
        String[] nums = new String[A.length];
        for (int i = 0; i < A.length; i++) nums[i] = String.valueOf(A[i]);

        // Comparator: prefer ordering that yields larger concatenation
        Arrays.sort(nums, (a, b) -> (b + a).compareTo(a + b));

        if (nums[0].equals("0")) return "0";

        StringBuilder sb = new StringBuilder();
        for (String s : nums) sb.append(s);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Largest Number ===");
        int[][] tests = {
            {3, 30, 34, 5, 9},      // "9534330"
            {10, 2},                // "210"
            {0, 0},                 // "0"
            {1},                    // "1"
            {121, 12},              // "12121"
            {9, 99, 999},           // "999999"
        };
        String[] expected = {"9534330", "210", "0", "1", "12121", "999999"};
        for (int t = 0; t < tests.length; t++) {
            String bf = bruteForce(tests[t]);
            String op = optimal(tests[t]);
            String be = best(tests[t]);
            System.out.printf("A=%-20s Brute=%-12s Optimal=%-12s Best=%-12s Expected=%s%n",
                    Arrays.toString(tests[t]), bf, op, be, expected[t]);
        }
    }
}
