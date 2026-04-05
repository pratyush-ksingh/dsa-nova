/**
 * Problem: Distinct Numbers in Window
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given array A of N integers and window size K, return an array of size N-K+1
 * where each element is the count of distinct integers in the corresponding window.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Recount distinct for each window
    // Time: O(N * K)  |  Space: O(K)
    // ============================================================
    public static int[] bruteForce(int[] A, int k) {
        if (k > A.length) return new int[0];
        int n = A.length;
        int[] result = new int[n - k + 1];
        for (int i = 0; i <= n - k; i++) {
            Set<Integer> set = new HashSet<>();
            for (int j = i; j < i + k; j++) set.add(A[j]);
            result[i] = set.size();
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Sliding window with HashMap frequency count
    // Time: O(N)  |  Space: O(K)
    // Maintain frequency map. When count drops to 0, remove from map (= fewer distinct).
    // ============================================================
    public static int[] optimal(int[] A, int k) {
        if (k > A.length) return new int[0];
        int n = A.length;
        int[] result = new int[n - k + 1];
        Map<Integer, Integer> freq = new HashMap<>();

        // Initialize first window
        for (int i = 0; i < k; i++)
            freq.merge(A[i], 1, Integer::sum);

        result[0] = freq.size();

        for (int i = k; i < n; i++) {
            // Add new element
            freq.merge(A[i], 1, Integer::sum);
            // Remove outgoing element
            int out = A[i - k];
            freq.put(out, freq.get(out) - 1);
            if (freq.get(out) == 0) freq.remove(out);
            result[i - k + 1] = freq.size();
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST - Sliding window, same O(N) but with array freq
    // Time: O(N)  |  Space: O(max_val) or O(K) with HashMap
    // Same as optimal; use array if values are bounded, else HashMap.
    // For generality, identical to optimal but highlights key insight:
    // distinct count only changes at boundaries of the window.
    // ============================================================
    public static int[] best(int[] A, int k) {
        return optimal(A, k); // identical time/space; refer to optimal's logic
    }

    public static void main(String[] args) {
        System.out.println("=== Distinct Numbers in Window ===");

        int[] A1 = {1, 2, 1, 3, 4, 3};
        int k1 = 3;
        System.out.println("A1 k=3 brute: " + Arrays.toString(bruteForce(A1, k1)));   // [2,3,3,3]
        System.out.println("A1 k=3 opt:   " + Arrays.toString(optimal(A1, k1)));
        System.out.println("A1 k=3 best:  " + Arrays.toString(best(A1, k1)));

        int[] A2 = {1, 1, 1, 1};
        System.out.println("A2 k=2 brute: " + Arrays.toString(bruteForce(A2, 2)));     // [1,1,1]
        System.out.println("A2 k=2 opt:   " + Arrays.toString(optimal(A2, 2)));
    }
}
