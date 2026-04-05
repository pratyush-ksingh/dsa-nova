/**
 * Problem: Minimum Lights to Activate
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an array A of 0s and 1s (1 = light installed) and integer B (range),
 * find the minimum number of lights to turn on to illuminate all positions.
 * Each active light at index i covers [i-B, i+B]. Return -1 if impossible.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(2^k * n) where k = number of light positions  |  Space: O(k)
    // Enumerate all subsets of lights and check coverage
    // ============================================================
    static class BruteForce {
        public static int solve(int[] A, int B) {
            int n = A.length;
            // Collect indices with lights
            int k = 0;
            for (int x : A) if (x == 1) k++;
            int[] lights = new int[k];
            int idx = 0;
            for (int i = 0; i < n; i++) if (A[i] == 1) lights[idx++] = i;

            // Try all 2^k subsets
            for (int size = 0; size <= k; size++) {
                if (checkSubsets(A, B, lights, k, size, 0, 0, new boolean[n])) {
                    return size;
                }
            }
            return -1;
        }

        private static boolean checkSubsets(int[] A, int B, int[] lights, int k,
                                             int targetSize, int start, int chosen,
                                             boolean[] covered) {
            if (chosen == targetSize) {
                // Check all covered
                for (boolean c : covered) if (!c) return false;
                return true;
            }
            if (start >= k) return false;
            // Branch: include lights[start]
            boolean[] c1 = covered.clone();
            int center = lights[start];
            for (int p = Math.max(0, center - B); p <= Math.min(A.length - 1, center + B); p++) {
                c1[p] = true;
            }
            if (checkSubsets(A, B, lights, k, targetSize, start + 1, chosen + 1, c1)) return true;
            // Branch: skip lights[start]
            return checkSubsets(A, B, lights, k, targetSize, start + 1, chosen, covered);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Greedy)
    // Time: O(n)  |  Space: O(1)
    // At each uncovered position, find rightmost light in range; jump past coverage
    // ============================================================
    static class Optimal {
        /**
         * Greedy: maintain the leftmost uncovered position `pos`.
         * Look in window [pos-B, pos+B] for the rightmost available light.
         * Activate it, advance pos to (light_index + B + 1).
         * If no light found in window, return -1.
         */
        public static int solve(int[] A, int B) {
            int n = A.length;
            int count = 0;
            int pos = 0;

            while (pos < n) {
                int left  = Math.max(0, pos - B);
                int right = Math.min(n - 1, pos + B);

                int bestLight = -1;
                for (int i = right; i >= left; i--) {
                    if (A[i] == 1) {
                        bestLight = i;
                        break;
                    }
                }

                if (bestLight == -1) return -1;

                count++;
                pos = bestLight + B + 1;
            }
            return count;
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n)  |  Space: O(1)
    // Same greedy; scans window from right to left starting at pos+B
    // ============================================================
    static class Best {
        /**
         * Identical logic to Optimal with a slightly different loop structure.
         * Scans from min(pos+B, n-1) down to max(pos-B, 0) for the rightmost light.
         */
        public static int solve(int[] A, int B) {
            int n = A.length;
            int count = 0;
            int pos = 0;

            while (pos < n) {
                int found = -1;
                for (int i = Math.min(pos + B, n - 1); i >= Math.max(pos - B, 0); i--) {
                    if (A[i] == 1) {
                        found = i;
                        break;
                    }
                }
                if (found == -1) return -1;
                count++;
                pos = found + B + 1;
            }
            return count;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Minimum Lights to Activate ===");
        int[][][] cases = {
            {{0,0,1,0,0}, {2}},
            {{1,0,0,1,1,0,0,1}, {2}},
            {{0,0,0,1,0,0,0}, {1}},
            {{1,1,1,1,1}, {0}},
            {{1,0,1,0,1}, {1}},
        };
        int[] expected = {1, 3, -1, 5, 3};

        for (int t = 0; t < cases.length; t++) {
            int[] A = cases[t][0];
            int B = cases[t][1][0];
            int b  = BruteForce.solve(A, B);
            int o  = Optimal.solve(A, B);
            int be = Best.solve(A, B);
            String status = (b == o && o == be && be == expected[t]) ? "OK" : "FAIL";
            System.out.printf("A=%s B=%d | Brute: %2d | Optimal: %2d | Best: %2d | Expected: %2d | %s%n",
                    Arrays.toString(A), B, b, o, be, expected[t], status);
        }
    }
}
