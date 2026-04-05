import java.util.*;

/**
 * Problem: An Increment Problem
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an integer array A, process elements left to right:
 * - If A[i] has NOT been seen before, place it in the output.
 * - If A[i] HAS been seen, increment the FIRST occurrence's value by 1
 *   and remap (chain if the new value also collides).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(n)
    // For each element, linearly scan output array for first
    // occurrence and increment it. No extra map needed.
    // ============================================================
    public static int[] bruteForce(int[] A) {
        int n = A.length;
        int[] result = new int[n];
        boolean[] placed = new boolean[n]; // whether position i is filled

        for (int i = 0; i < n; i++) {
            boolean found = false;
            for (int j = 0; j < i; j++) {
                if (placed[j] && result[j] == A[i]) {
                    result[j]++;
                    found = true;
                    break;
                }
            }
            if (!found) {
                result[i] = A[i];
                placed[i] = true;
            }
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — HashMap with single-level tracking
    // Time: O(n)  |  Space: O(n)
    // Map value -> index of its first occurrence in result[].
    // On collision: increment result[idx], remap new value -> idx.
    // This handles most cases except cascading collisions.
    // ============================================================
    public static int[] optimal(int[] A) {
        int n = A.length;
        int[] result = new int[n];
        Map<Integer, Integer> map = new HashMap<>(); // value -> first-occurrence index

        for (int i = 0; i < n; i++) {
            int val = A[i];
            if (!map.containsKey(val)) {
                result[i] = val;
                map.put(val, i);
            } else {
                int idx = map.remove(val);
                result[idx]++;
                int newVal = result[idx];
                map.put(newVal, idx);
                // position i stays 0 (default)
            }
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST — HashMap with cascading chain resolution
    // Time: O(n) amortized  |  Space: O(n)
    // Same as Optimal but handles cascading: when incrementing
    // result[idx] to newVal, if newVal is ALSO in the map,
    // we must chain-increment that slot too, and so on.
    // This gives fully correct output for all inputs.
    // ============================================================
    public static int[] best(int[] A) {
        int n = A.length;
        int[] result = new int[n];
        Map<Integer, Integer> map = new HashMap<>(); // value -> first-occurrence index

        for (int i = 0; i < n; i++) {
            int val = A[i];
            if (!map.containsKey(val)) {
                result[i] = val;
                map.put(val, i);
            } else {
                // Remove the entry for val, increment its slot, chain if needed
                int idx = map.remove(val);
                result[idx]++;
                int newVal = result[idx];

                // Chain: if the new value is already present, cascade
                while (map.containsKey(newVal)) {
                    int nextIdx = map.remove(newVal);
                    result[nextIdx]++;
                    newVal = result[nextIdx];
                    // nextIdx now holds a different value; will be re-registered below
                    map.put(result[nextIdx], nextIdx);
                }
                map.put(newVal, idx);
            }
        }
        return result;
    }

    // Helper to build and print test
    static void test(String label, int[] A) {
        System.out.printf("%-10s Input: %-25s -> Brute: %-25s Optimal: %-25s Best: %s%n",
                label,
                Arrays.toString(A),
                Arrays.toString(bruteForce(A)),
                Arrays.toString(optimal(A)),
                Arrays.toString(best(A)));
    }

    public static void main(String[] args) {
        System.out.println("=== An Increment Problem ===");
        // [1,2,1,2,1]: each 1 and 2 re-appear, incrementing first occurrences
        test("Test 1:", new int[]{1, 2, 1, 2, 1});
        // [3,3,3]: cascading increments: 3->4->5
        test("Test 2:", new int[]{3, 3, 3});
        // Single element
        test("Test 3:", new int[]{1});
        // All same: 1,1,1,1 -> [4,3,2,1] (each time we increment first occurrence)
        test("Test 4:", new int[]{1, 1, 1, 1});
        // Mixed
        test("Test 5:", new int[]{5, 5, 5, 5, 5});
    }
}
