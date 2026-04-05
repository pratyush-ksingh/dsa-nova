/**
 * Problem: Generate All Subsequences
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array of n elements, generate all 2^n subsequences.
 * A subsequence is any subset of elements in their original order.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Iterative Bit Masking
    // Time: O(n * 2^n)  |  Space: O(n * 2^n)
    //
    // Each integer from 0 to 2^n - 1 encodes a subsequence.
    // Bit j set => include arr[j].
    // ============================================================
    public static List<List<Integer>> bruteForce(int[] arr) {
        int n = arr.length;
        int total = 1 << n; // 2^n
        List<List<Integer>> result = new ArrayList<>();

        for (int mask = 0; mask < total; mask++) {
            List<Integer> subseq = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((mask & (1 << j)) != 0) {
                    subseq.add(arr[j]);
                }
            }
            result.add(subseq);
        }

        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Include/Exclude (Backtracking)
    // Time: O(n * 2^n)  |  Space: O(n) recursion stack
    //
    // At each index, include the element or exclude it. When we
    // reach the end, add a copy of current to results.
    // Backtrack by removing the last element after the include call.
    // ============================================================
    public static List<List<Integer>> optimal(int[] arr) {
        List<List<Integer>> result = new ArrayList<>();
        generateBacktrack(arr, 0, new ArrayList<>(), result);
        return result;
    }

    private static void generateBacktrack(int[] arr, int i,
                                           List<Integer> current,
                                           List<List<Integer>> result) {
        // Base case: processed all elements
        if (i == arr.length) {
            result.add(new ArrayList<>(current)); // COPY current
            return;
        }

        // Include arr[i]
        current.add(arr[i]);
        generateBacktrack(arr, i + 1, current, result);
        current.remove(current.size() - 1); // backtrack

        // Exclude arr[i]
        generateBacktrack(arr, i + 1, current, result);
    }

    // ============================================================
    // APPROACH 3: BEST -- Cascading (Iterative Build-Up)
    // Time: O(n * 2^n)  |  Space: O(n * 2^n)
    //
    // Start with [[]]. For each element, duplicate all existing
    // subsequences with the new element appended.
    // No recursion, very clean and intuitive.
    // ============================================================
    public static List<List<Integer>> best(int[] arr) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>()); // start with empty subsequence

        for (int x : arr) {
            int size = result.size();
            for (int j = 0; j < size; j++) {
                // Create new subsequence = existing + x
                List<Integer> newSubseq = new ArrayList<>(result.get(j));
                newSubseq.add(x);
                result.add(newSubseq);
            }
        }

        return result;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Generate All Subsequences ===\n");

        int[][] tests = {
            {1, 2, 3},
            {1, 2},
            {5},
            {},
        };

        for (int[] arr : tests) {
            System.out.println("Input: " + Arrays.toString(arr));
            System.out.println("Expected count: " + (1 << arr.length));

            List<List<Integer>> b = bruteForce(arr);
            List<List<Integer>> o = optimal(arr);
            List<List<Integer>> be = best(arr);

            System.out.println("Brute count:   " + b.size() +
                " " + (b.size() == (1 << arr.length) ? "PASS" : "FAIL"));
            System.out.println("Optimal count: " + o.size() +
                " " + (o.size() == (1 << arr.length) ? "PASS" : "FAIL"));
            System.out.println("Best count:    " + be.size() +
                " " + (be.size() == (1 << arr.length) ? "PASS" : "FAIL"));

            // Print all subsequences for small inputs
            if (arr.length <= 3) {
                System.out.println("Brute:   " + b);
                System.out.println("Optimal: " + o);
                System.out.println("Best:    " + be);
            }
            System.out.println();
        }
    }
}
