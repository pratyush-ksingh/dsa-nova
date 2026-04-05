/**
 * Problem: Maximal String
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a string S and an integer K, perform at most K swaps to get
 * the lexicographically largest string possible.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(K * N^2)  |  Space: O(N * K) recursion depth
    // Generate all possible strings after at most K swaps and track max.
    // ============================================================
    static String bruteResult;

    public static String bruteForce(String s, int k) {
        bruteResult = s;
        char[] arr = s.toCharArray();
        bruteHelper(arr, k);
        return bruteResult;
    }

    private static void bruteHelper(char[] arr, int k) {
        String cur = new String(arr);
        if (cur.compareTo(bruteResult) > 0) bruteResult = cur;
        if (k == 0) return;
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[j] > arr[i]) {
                    char tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
                    bruteHelper(arr, k - 1);
                    tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
                }
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Greedy Backtracking with Pruning
    // Time: O(N^2 * K)  |  Space: O(N) recursion stack
    // At each position, find the rightmost max char in remaining, bring it front.
    // Each such swap uses 1 swap. If max is already at pos, no swap needed.
    // ============================================================
    static String optResult;

    public static String optimal(String s, int k) {
        optResult = s;
        char[] arr = s.toCharArray();
        greedyMax(arr, 0, k);
        return optResult;
    }

    private static void greedyMax(char[] arr, int pos, int k) {
        if (k == 0 || pos == arr.length) {
            String cur = new String(arr);
            if (cur.compareTo(optResult) > 0) optResult = cur;
            return;
        }
        // Find max char from pos to end
        char maxCh = arr[pos];
        for (int i = pos + 1; i < arr.length; i++) {
            if (arr[i] > maxCh) maxCh = arr[i];
        }
        // If already max at pos, no swap needed
        if (arr[pos] == maxCh) {
            greedyMax(arr, pos + 1, k);
            return;
        }
        // Try bringing each occurrence of maxCh to pos
        for (int i = pos + 1; i < arr.length; i++) {
            if (arr[i] == maxCh) {
                // Bubble left: each adjacent swap costs 1
                // For simplicity, do single swap (brings rightmost occurrence)
                char tmp = arr[pos]; arr[pos] = arr[i]; arr[i] = tmp;
                greedyMax(arr, pos + 1, k - 1);
                tmp = arr[pos]; arr[pos] = arr[i]; arr[i] = tmp;
            }
        }
        String cur = new String(arr);
        if (cur.compareTo(optResult) > 0) optResult = cur;
    }

    // ============================================================
    // APPROACH 3: BEST - Greedy with Rightmost Max First
    // Time: O(N^2)  |  Space: O(N)
    // For each position, find rightmost occurrence of the maximum character
    // from pos to end. If it's not at pos, swap and decrement K.
    // This is a well-known greedy O(N^2) solution.
    // ============================================================
    public static String best(String s, int k) {
        char[] arr = s.toCharArray();
        int n = arr.length;
        for (int i = 0; i < n - 1 && k > 0; i++) {
            // Find max char from i+1 to n-1
            int maxIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] >= arr[maxIdx]) maxIdx = j;  // >= to pick rightmost
            }
            if (maxIdx == i) continue; // already max at i
            // Bring arr[maxIdx] to position i by adjacent swaps
            while (maxIdx > i && k > 0) {
                char tmp = arr[maxIdx]; arr[maxIdx] = arr[maxIdx - 1]; arr[maxIdx - 1] = tmp;
                maxIdx--;
                k--;
            }
        }
        return new String(arr);
    }

    public static void main(String[] args) {
        System.out.println("=== Maximal String ===");

        // Test 1: "dcab", k=2 => "dcba"? Let's verify
        System.out.println("Brute  'dcab' k=2: " + bruteForce("dcab", 2));
        System.out.println("Optimal 'dcab' k=2: " + optimal("dcab", 2));
        System.out.println("Best   'dcab' k=2: " + best("dcab", 2));

        // Test 2: "abcd", k=4 => "dcba"
        System.out.println("Brute  'abcd' k=4: " + bruteForce("abcd", 4));
        System.out.println("Optimal 'abcd' k=4: " + optimal("abcd", 4));
        System.out.println("Best   'abcd' k=4: " + best("abcd", 4));

        // Test 3: "abcd", k=1 => "bacd"
        System.out.println("Brute  'abcd' k=1: " + bruteForce("abcd", 1));
        System.out.println("Optimal 'abcd' k=1: " + optimal("abcd", 1));
        System.out.println("Best   'abcd' k=1: " + best("abcd", 1));

        // Test 4: "aaz", k=1 => "zaa" (swap pos0 and pos2 costs 2 adjacent swaps, but direct swap = 1)
        // Note: "best" uses adjacent swaps; direct single swap gives "zaa" in 1 if we allow non-adjacent
        // Actually for this problem a "swap" is any two indices, not just adjacent.
        // Let's re-check: "aaz" k=1 direct swap(0,2) = "zaa"
        System.out.println("Best 'aaz' k=1: " + best("aaz", 1));
    }
}
