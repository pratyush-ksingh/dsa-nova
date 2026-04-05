import java.util.*;

/**
 * Problem: Union of Two Sorted Arrays
 * Difficulty: MEDIUM | XP: 25
 *
 * Given two sorted arrays A and B, return their union (sorted, no duplicates).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O((m+n) log(m+n))  |  Space: O(m+n)
    // Merge both arrays into a set, then sort the result.
    // Simple but does not exploit the sorted property.
    // ============================================================
    public static List<Integer> bruteForce(int[] A, int[] B) {
        Set<Integer> set = new LinkedHashSet<>();
        for (int x : A) set.add(x);
        for (int x : B) set.add(x);
        List<Integer> result = new ArrayList<>(set);
        Collections.sort(result);
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Two Pointer Merge
    // Time: O(m + n)  |  Space: O(m + n)
    // Simultaneously advance through both sorted arrays,
    // always picking the smaller element and skipping duplicates.
    // ============================================================
    public static List<Integer> optimal(int[] A, int[] B) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        int m = A.length, n = B.length;

        while (i < m && j < n) {
            // Skip duplicates within A
            while (i > 0 && i < m && A[i] == A[i - 1]) i++;
            // Skip duplicates within B
            while (j > 0 && j < n && B[j] == B[j - 1]) j++;

            if (i >= m || j >= n) break;

            if (A[i] < B[j]) {
                if (result.isEmpty() || result.get(result.size() - 1) != A[i])
                    result.add(A[i]);
                i++;
            } else if (B[j] < A[i]) {
                if (result.isEmpty() || result.get(result.size() - 1) != B[j])
                    result.add(B[j]);
                j++;
            } else {
                if (result.isEmpty() || result.get(result.size() - 1) != A[i])
                    result.add(A[i]);
                i++;
                j++;
            }
        }

        while (i < m) {
            if (result.isEmpty() || result.get(result.size() - 1) != A[i])
                result.add(A[i]);
            i++;
        }
        while (j < n) {
            if (result.isEmpty() || result.get(result.size() - 1) != B[j])
                result.add(B[j]);
            j++;
        }

        return result;
    }

    // ============================================================
    // APPROACH 3: BEST — Clean Two Pointer (cleaner duplicate handling)
    // Time: O(m + n)  |  Space: O(m + n)
    // Cleaner version: use last-added tracking to skip all duplicates
    // regardless of source. Handles arrays with duplicates cleanly.
    // ============================================================
    public static List<Integer> best(int[] A, int[] B) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        int m = A.length, n = B.length;

        while (i < m && j < n) {
            int a = A[i], b = B[j];
            if (a <= b) {
                if (result.isEmpty() || result.get(result.size() - 1) != a)
                    result.add(a);
                i++;
                if (a == b) j++; // consume from B too
            } else {
                if (result.isEmpty() || result.get(result.size() - 1) != b)
                    result.add(b);
                j++;
            }
        }

        while (i < m) {
            if (result.isEmpty() || result.get(result.size() - 1) != A[i])
                result.add(A[i]);
            i++;
        }
        while (j < n) {
            if (result.isEmpty() || result.get(result.size() - 1) != B[j])
                result.add(B[j]);
            j++;
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Union of Two Sorted Arrays ===");

        int[][] As = {
            {1, 2, 3, 4, 5},
            {1, 1, 2, 3},
            {1},
            {1, 2, 4, 5, 6},
        };
        int[][] Bs = {
            {1, 2, 3},
            {2, 3, 4, 4},
            {2},
            {2, 3, 4, 7},
        };

        for (int t = 0; t < As.length; t++) {
            System.out.printf("A=%s B=%s%n", Arrays.toString(As[t]), Arrays.toString(Bs[t]));
            System.out.println("  Brute:   " + bruteForce(As[t], Bs[t]));
            System.out.println("  Optimal: " + optimal(As[t], Bs[t]));
            System.out.println("  Best:    " + best(As[t], Bs[t]));
        }
    }
}
