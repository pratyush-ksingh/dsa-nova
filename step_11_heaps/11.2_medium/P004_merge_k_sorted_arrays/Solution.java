/**
 * Problem: Merge K Sorted Arrays
 * Difficulty: MEDIUM | XP: 25
 *
 * Given k sorted arrays of size n each, merge them into a single sorted array.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Concatenate and Sort)
// Time: O(nk * log(nk)) | Space: O(nk)
// ============================================================
class BruteForce {
    public static int[] solve(int[][] arrays) {
        List<Integer> merged = new ArrayList<>();
        for (int[] arr : arrays) {
            for (int val : arr) merged.add(val);
        }
        Collections.sort(merged);
        return merged.stream().mapToInt(Integer::intValue).toArray();
    }
}

// ============================================================
// Approach 2: Optimal (Min-Heap with index tracking)
// Time: O(nk * log k) | Space: O(k)
// ============================================================
class Optimal {
    public static int[] solve(int[][] arrays) {
        // PriorityQueue entry: [value, arrayIndex, elementIndex]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        int total = 0;

        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].length > 0) {
                minHeap.offer(new int[]{arrays[i][0], i, 0});
                total += arrays[i].length;
            }
        }

        int[] result = new int[total];
        int idx = 0;

        while (!minHeap.isEmpty()) {
            int[] top = minHeap.poll();
            int val = top[0], arrIdx = top[1], elemIdx = top[2];
            result[idx++] = val;
            int nextElem = elemIdx + 1;
            if (nextElem < arrays[arrIdx].length) {
                minHeap.offer(new int[]{arrays[arrIdx][nextElem], arrIdx, nextElem});
            }
        }

        return result;
    }
}

// ============================================================
// Approach 3: Best (Same Min-Heap — idiomatic Java)
// Time: O(nk * log k) | Space: O(k)
// ============================================================
class Best {
    public static int[] solve(int[][] arrays) {
        // Same heap algorithm; written as a clean production method.
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        int total = 0;

        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].length > 0) {
                pq.offer(new int[]{arrays[i][0], i, 0});
                total += arrays[i].length;
            }
        }

        int[] result = new int[total];
        int pos = 0;

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            result[pos++] = cur[0];
            int ni = cur[2] + 1;
            if (ni < arrays[cur[1]].length) {
                pq.offer(new int[]{arrays[cur[1]][ni], cur[1], ni});
            }
        }

        return result;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Merge K Sorted Arrays ===\n");

        int[][][] inputs = {
            {{1, 3, 5, 7}, {2, 4, 6, 8}, {0, 9, 10, 11}},
            {{1, 3}, {2, 4}, {5, 6}},
            {{10}, {5}, {1}},
            {{1}},
        };
        int[][] expecteds = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
            {1, 2, 3, 4, 5, 6},
            {1, 5, 10},
            {1},
        };

        for (int t = 0; t < inputs.length; t++) {
            int[][] arrays = inputs[t];
            int[] expected = expecteds[t];

            int[] b = BruteForce.solve(arrays);
            int[] o = Optimal.solve(arrays);
            int[] h = Best.solve(arrays);

            boolean pass = Arrays.equals(b, expected)
                        && Arrays.equals(o, expected)
                        && Arrays.equals(h, expected);

            System.out.println("Input:    " + arraysToString(arrays));
            System.out.println("Brute:    " + Arrays.toString(b));
            System.out.println("Optimal:  " + Arrays.toString(o));
            System.out.println("Best:     " + Arrays.toString(h));
            System.out.println("Expected: " + Arrays.toString(expected));
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }

    private static String arraysToString(int[][] arrays) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arrays.length; i++) {
            sb.append(Arrays.toString(arrays[i]));
            if (i < arrays.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
