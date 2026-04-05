/**
 * Problem: Order of People Heights
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given heights[] and infronts[], reconstruct the queue such that
 * person i has exactly infronts[i] people of height >= heights[i] in front.
 * Return the array of heights in the reconstructed order.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Try all permutations)
// Time: O(n! * n) | Space: O(n)
// ============================================================
class BruteForce {
    static int[] heights;
    static int[] infronts;
    static int[] result;
    static boolean found;

    public static int[] solve(int[] h, int[] inf) {
        heights = h;
        infronts = inf;
        int n = h.length;
        result = new int[n];
        found = false;

        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;
        permute(perm, 0);
        return result;
    }

    static void permute(int[] perm, int start) {
        if (found) return;
        if (start == perm.length) {
            // Build queue and verify
            int[] queue = new int[perm.length];
            for (int i = 0; i < perm.length; i++) queue[i] = heights[perm[i]];
            for (int pos = 0; pos < perm.length; pos++) {
                int h = heights[perm[pos]];
                int inf = infronts[perm[pos]];
                int count = 0;
                for (int j = 0; j < pos; j++) if (queue[j] >= h) count++;
                if (count != inf) return;
            }
            System.arraycopy(queue, 0, result, 0, queue.length);
            found = true;
            return;
        }
        for (int i = start; i < perm.length; i++) {
            int tmp = perm[start]; perm[start] = perm[i]; perm[i] = tmp;
            permute(perm, start + 1);
            tmp = perm[start]; perm[start] = perm[i]; perm[i] = tmp;
        }
    }
}

// ============================================================
// Approach 2: Optimal (Sort by height desc, insert at index=infronts)
// Time: O(n²) | Space: O(n)
// ============================================================
class Optimal {
    public static int[] solve(int[] heights, int[] infronts) {
        int n = heights.length;
        // Pair (height, infront) and sort by height desc, ties by infront asc
        int[][] people = new int[n][2];
        for (int i = 0; i < n; i++) {
            people[i][0] = heights[i];
            people[i][1] = infronts[i];
        }
        Arrays.sort(people, (a, b) -> a[0] != b[0] ? b[0] - a[0] : a[1] - b[1]);

        LinkedList<Integer> result = new LinkedList<>();
        for (int[] p : people) {
            result.add(p[1], p[0]); // insert height at index=infront
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}

// ============================================================
// Approach 3: Best (Same greedy — clean production form)
// Time: O(n²) | Space: O(n)
// Note: O(n log n) with BIT/Segment Tree for finding k-th free slot
// ============================================================
class Best {
    public static int[] solve(int[] heights, int[] infronts) {
        int n = heights.length;
        int[][] people = new int[n][2];
        for (int i = 0; i < n; i++) {
            people[i][0] = heights[i];
            people[i][1] = infronts[i];
        }
        // Sort: tallest first, ties broken by fewer-infronts first
        Arrays.sort(people, (a, b) -> a[0] != b[0] ? b[0] - a[0] : a[1] - b[1]);

        // ArrayList for simpler indexed insertion
        ArrayList<Integer> result = new ArrayList<>();
        for (int[] p : people) {
            result.add(p[1], p[0]);
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Order of People Heights ===\n");

        int[][] heightsArr  = {{5, 3, 2, 6, 1, 4}, {6, 5, 4, 3, 2, 1}, {1, 2}};
        int[][] infrontsArr = {{0, 1, 2, 0, 3, 2}, {0, 0, 0, 0, 0, 0}, {1, 0}};
        int[][] expecteds   = {{5, 3, 2, 1, 6, 4}, {1, 2, 3, 4, 5, 6}, {2, 1}};

        for (int t = 0; t < heightsArr.length; t++) {
            int[] o = Optimal.solve(heightsArr[t], infrontsArr[t]);
            int[] h = Best.solve(heightsArr[t], infrontsArr[t]);
            boolean pass = Arrays.equals(o, expecteds[t]) && Arrays.equals(h, expecteds[t]);

            System.out.println("Heights:  " + Arrays.toString(heightsArr[t]));
            System.out.println("Infronts: " + Arrays.toString(infrontsArr[t]));
            System.out.println("Optimal:  " + Arrays.toString(o));
            System.out.println("Best:     " + Arrays.toString(h));
            System.out.println("Expected: " + Arrays.toString(expecteds[t]));
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
