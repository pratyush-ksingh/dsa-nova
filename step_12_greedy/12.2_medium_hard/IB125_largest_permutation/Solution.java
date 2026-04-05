/**
 * Problem: Largest Permutation
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a permutation A of first N natural numbers and integer B (max swaps),
 * find the lexicographically largest permutation achievable in at most B swaps.
 *
 * Greedy: from left to right, place the largest available number at each position.
 * Use a position map for O(1) swaps.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Try all swap sequences (exponential)
    // Time: O(N! * B)  |  Space: O(N)
    // NOTE: Only feasible for tiny inputs; illustrative only
    // For larger inputs this would TLE — use greedy
    // ============================================================
    public static int[] bruteForce(int[] A, int B) {
        // For brute-force clarity: greedy pass with simple scan (O(N*B))
        int n = A.length;
        int[] arr = A.clone();
        for (int i = 0; i < n && B > 0; i++) {
            // Find max value in arr[i..n-1]
            int maxVal = arr[i], maxIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] > maxVal) { maxVal = arr[j]; maxIdx = j; }
            }
            if (maxIdx != i) {
                // Swap arr[i] and arr[maxIdx]
                int tmp = arr[i]; arr[i] = arr[maxIdx]; arr[maxIdx] = tmp;
                B--;
            }
        }
        return arr;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Greedy + position map for O(1) swaps
    // Time: O(N)  |  Space: O(N)
    // pos[v] = current index of value v.
    // For each position i, if arr[i] != n-i (largest available),
    // swap arr[i] with arr[pos[n-i]] and update position map.
    // ============================================================
    public static int[] optimal(int[] A, int B) {
        int n = A.length;
        int[] arr = A.clone();
        int[] pos = new int[n + 1]; // pos[val] = index of val in arr
        for (int i = 0; i < n; i++) pos[arr[i]] = i;

        for (int i = 0; i < n && B > 0; i++) {
            int want = n - i; // largest value not yet placed (1-indexed)
            if (arr[i] == want) continue;
            // Swap arr[i] with the position of 'want'
            int j = pos[want];
            pos[arr[i]] = j;   // arr[i]'s value now lives at index j
            pos[want] = i;     // want now lives at index i
            int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            B--;
        }
        return arr;
    }

    // ============================================================
    // APPROACH 3: BEST - Same O(N) greedy; early exit if B >= N
    // Time: O(N)  |  Space: O(N)
    // If B >= N, trivially return sorted descending (N-1 swaps at most needed)
    // ============================================================
    public static int[] best(int[] A, int B) {
        int n = A.length;
        if (B >= n) {
            // Can always sort descending in at most n-1 swaps
            int[] res = new int[n];
            for (int i = 0; i < n; i++) res[i] = n - i;
            return res;
        }
        return optimal(A, B);
    }

    public static void main(String[] args) {
        System.out.println("=== Largest Permutation ===");

        int[] A = {1, 2, 3, 4};
        System.out.println("A=" + Arrays.toString(A) + ", B=1");
        System.out.println("Brute:   " + Arrays.toString(bruteForce(A.clone(), 1)));   // [4,2,3,1]
        System.out.println("Optimal: " + Arrays.toString(optimal(A.clone(), 1)));
        System.out.println("Best:    " + Arrays.toString(best(A.clone(), 1)));

        A = new int[]{3, 1, 2};
        System.out.println("\nA=" + Arrays.toString(A) + ", B=2");
        System.out.println("Brute:   " + Arrays.toString(bruteForce(A.clone(), 2)));   // [3,2,1]
        System.out.println("Optimal: " + Arrays.toString(optimal(A.clone(), 2)));
        System.out.println("Best:    " + Arrays.toString(best(A.clone(), 2)));

        A = new int[]{2, 1, 3};
        System.out.println("\nA=" + Arrays.toString(A) + ", B=1");
        System.out.println("Brute:   " + Arrays.toString(bruteForce(A.clone(), 1)));   // [3,1,2]
        System.out.println("Optimal: " + Arrays.toString(optimal(A.clone(), 1)));
        System.out.println("Best:    " + Arrays.toString(best(A.clone(), 1)));
    }
}
