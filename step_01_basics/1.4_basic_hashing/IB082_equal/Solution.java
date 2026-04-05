/**
 * Problem: Equal
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Find all unique quadruplets (A, B, C, D) such that:
 *   A[A] + A[B] = A[C] + A[D]
 *   A != C, A != D, B != C, B != D  (indices all different, A < B, C < D, (A,B) < (C,D))
 * Return in sorted order, lexicographically smallest first.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^4)  |  Space: O(1) ignoring output
    // ============================================================
    /**
     * Try all combinations of 4 distinct indices, check the sum condition.
     * Real-life: Exhaustive search for balance in financial transaction pairing.
     */
    public static List<List<Integer>> bruteForce(int[] A) {
        int n = A.length;
        Set<List<Integer>> resultSet = new TreeSet<>(Comparator
            .comparingInt((List<Integer> l) -> l.get(0))
            .thenComparingInt(l -> l.get(1))
            .thenComparingInt(l -> l.get(2))
            .thenComparingInt(l -> l.get(3)));

        for (int a = 0; a < n - 1; a++) {
            for (int b = a + 1; b < n; b++) {
                for (int c = 0; c < n - 1; c++) {
                    for (int d = c + 1; d < n; d++) {
                        if (a == c || a == d || b == c || b == d) continue;
                        if (A[a] + A[b] == A[c] + A[d]) {
                            List<Integer> quad = new ArrayList<>(Arrays.asList(a, b, c, d));
                            resultSet.add(quad);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(resultSet);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(n^2)  |  Space: O(n^2)
    // ============================================================
    /**
     * Hash pairs: for each pair (i,j) with i<j, map sum -> list of (i,j).
     * For each sum bucket with multiple pairs, generate all combinations ensuring
     * indices are distinct. Sort and deduplicate results.
     * Real-life: Matching financial trades with equal net value using hash maps.
     */
    public static List<List<Integer>> optimal(int[] A) {
        int n = A.length;
        // sum -> list of [i, j] pairs with i < j
        Map<Integer, List<int[]>> sumMap = new HashMap<>();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int s = A[i] + A[j];
                sumMap.computeIfAbsent(s, k -> new ArrayList<>()).add(new int[]{i, j});
            }
        }

        Set<List<Integer>> seen = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();

        for (List<int[]> pairs : sumMap.values()) {
            int sz = pairs.size();
            for (int p = 0; p < sz - 1; p++) {
                for (int q = p + 1; q < sz; q++) {
                    int a = pairs.get(p)[0], b = pairs.get(p)[1];
                    int c = pairs.get(q)[0], d = pairs.get(q)[1];
                    // All four indices must be distinct
                    if (a == c || a == d || b == c || b == d) continue;
                    // Canonical form: (a,b) < (c,d)
                    int[] first  = {a, b};
                    int[] second = {c, d};
                    if (first[0] > second[0] || (first[0] == second[0] && first[1] > second[1])) {
                        int[] tmp = first; first = second; second = tmp;
                    }
                    List<Integer> quad = Arrays.asList(first[0], first[1], second[0], second[1]);
                    if (seen.add(quad)) result.add(quad);
                }
            }
        }
        result.sort(Comparator
            .comparingInt((List<Integer> l) -> l.get(0))
            .thenComparingInt(l -> l.get(1))
            .thenComparingInt(l -> l.get(2))
            .thenComparingInt(l -> l.get(3)));
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n^2)  |  Space: O(n^2)
    // ============================================================
    /**
     * Same O(n^2) approach but uses a single map<sum, firstPair> to avoid
     * storing all pairs — emits a result the moment a second pair with the same
     * sum is found. Collects into a sorted set to deduplicate.
     * Real-life: Stream-based duplicate-sum detection in real-time trading systems.
     */
    public static List<List<Integer>> best(int[] A) {
        int n = A.length;
        // sum -> first (i, j) seen
        Map<Integer, int[]> firstPair = new HashMap<>();
        Set<List<Integer>> seen = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int s = A[i] + A[j];
                if (firstPair.containsKey(s)) {
                    int[] prev = firstPair.get(s);
                    int a = prev[0], b = prev[1];
                    int c = i, d = j;
                    if (a != c && a != d && b != c && b != d) {
                        // Canonical: (a,b) first
                        List<Integer> quad = Arrays.asList(a, b, c, d);
                        if (seen.add(quad)) result.add(quad);
                    }
                } else {
                    firstPair.put(s, new int[]{i, j});
                }
            }
        }
        result.sort(Comparator
            .comparingInt((List<Integer> l) -> l.get(0))
            .thenComparingInt(l -> l.get(1))
            .thenComparingInt(l -> l.get(2))
            .thenComparingInt(l -> l.get(3)));
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Equal ===");

        int[] A1 = {3, 4, 7, 1, 2, 9, 8};
        System.out.println("\nInput: " + Arrays.toString(A1));
        System.out.println("Expected: [[0,2,3,5], [0,3,4,6], [0,2,4,5] ...]");
        System.out.println("Brute:   " + bruteForce(A1));
        System.out.println("Optimal: " + optimal(A1));
        System.out.println("Best:    " + best(A1));

        int[] A2 = {1, 1, 1, 1};
        System.out.println("\nInput: " + Arrays.toString(A2));
        System.out.println("Brute:   " + bruteForce(A2));
        System.out.println("Optimal: " + optimal(A2));
        System.out.println("Best:    " + best(A2));
    }
}
