/**
 * Problem: Find Permutation
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a string S of 'D' and 'I' of length n-1, find the lexicographically
 * smallest permutation of [1..n] satisfying the direction constraints.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.Deque;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n! * n)  |  Space: O(n)
    // Generate permutations in lexicographic order; return first valid one.
    // ============================================================
    static class BruteForce {
        private static int[] result;

        public static int[] findPerm(String S) {
            int n = S.length() + 1;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = i + 1;
            result = null;
            permute(arr, 0, S);
            return result;
        }

        private static void permute(int[] arr, int start, String S) {
            if (result != null) return;  // already found
            if (start == arr.length) {
                if (isValid(arr, S)) result = arr.clone();
                return;
            }
            for (int i = start; i < arr.length; i++) {
                swap(arr, start, i);
                permute(arr, start + 1, S);
                swap(arr, start, i);
            }
        }

        private static boolean isValid(int[] arr, String S) {
            for (int i = 0; i < S.length(); i++) {
                if (S.charAt(i) == 'I' && arr[i] >= arr[i + 1]) return false;
                if (S.charAt(i) == 'D' && arr[i] <= arr[i + 1]) return false;
            }
            return true;
        }

        private static void swap(int[] arr, int i, int j) {
            int t = arr[i]; arr[i] = arr[j]; arr[j] = t;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Stack-based)
    // Time: O(n)  |  Space: O(n)
    // Push 1..n; pop entire stack on 'I' or at end.
    // ============================================================
    static class Optimal {
        /**
         * Push numbers 1..n onto a stack one at a time.
         * When we encounter 'I' (or reach the last number), drain the stack.
         * The stack ensures consecutive D-segments are output in reverse order
         * (descending), while I transitions force the accumulated run to flush.
         */
        public static int[] findPerm(String S) {
            int n = S.length() + 1;
            int[] result = new int[n];
            int idx = 0;
            Deque<Integer> stack = new ArrayDeque<>();

            for (int i = 1; i <= n; i++) {
                stack.push(i);
                if (i == n || S.charAt(i - 1) == 'I') {
                    while (!stack.isEmpty()) {
                        result[idx++] = stack.pop();
                    }
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Reverse D-segments)
    // Time: O(n)  |  Space: O(n)
    // Start with [1..n], reverse each maximal D-run.
    // ============================================================
    static class Best {
        /**
         * Start with the identity permutation [1..n].
         * For each maximal run of 'D's at indices [i..j] in S,
         * reverse result[i..j+1].
         * This is the most intuitive "construct then patch" approach.
         */
        public static int[] findPerm(String S) {
            int n = S.length() + 1;
            int[] result = new int[n];
            for (int i = 0; i < n; i++) result[i] = i + 1;

            int i = 0;
            while (i < S.length()) {
                if (S.charAt(i) == 'D') {
                    int j = i;
                    while (j < S.length() && S.charAt(j) == 'D') j++;
                    // Reverse result[i..j]
                    reverse(result, i, j);
                    i = j;
                } else {
                    i++;
                }
            }
            return result;
        }

        private static void reverse(int[] arr, int l, int r) {
            while (l < r) {
                int t = arr[l]; arr[l] = arr[r]; arr[r] = t;
                l++; r--;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Find Permutation ===");
        String[] inputs   = {"I", "D", "DI", "ID", "DDIID", "IDID"};
        int[][] expected = {
            {1, 2},
            {2, 1},
            {2, 1, 3},
            {1, 3, 2},
            {3, 2, 1, 4, 6, 5},
            {1, 3, 2, 5, 4}
        };

        for (int t = 0; t < inputs.length; t++) {
            int[] b  = BruteForce.findPerm(inputs[t]);
            int[] o  = Optimal.findPerm(inputs[t]);
            int[] be = Best.findPerm(inputs[t]);
            boolean ok = Arrays.equals(b, o) && Arrays.equals(o, be) && Arrays.equals(be, expected[t]);
            System.out.printf("S=\"%s\" | Brute: %-20s | Optimal: %-20s | Best: %-20s | %s%n",
                    inputs[t], Arrays.toString(b), Arrays.toString(o), Arrays.toString(be),
                    ok ? "OK" : "FAIL");
        }
    }
}
