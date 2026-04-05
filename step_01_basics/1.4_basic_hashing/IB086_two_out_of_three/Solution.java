/**
 * Problem: Two Out of Three
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit (LeetCode 2032)
 *
 * Given three integer arrays, return a sorted list of all values present
 * in at least two of the three arrays. Each element counted once per array.
 *
 * Example:
 *   [1,1,3,2], [2,3], [3]  ->  [2, 3]
 *   [3,1], [2,3], [1,2]    ->  [1, 2, 3]
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Linear Scan for Each Candidate
    // Time: O((n+m+k) * (n+m+k))  |  Space: O(n+m+k)
    // Collect all unique values; for each, count how many arrays
    // contain it using contains(). Return those with count >= 2.
    // ============================================================
    static class BruteForce {
        private static boolean contains(int[] arr, int val) {
            for (int x : arr) if (x == val) return true;
            return false;
        }

        public static List<Integer> twoOutOfThree(int[] n1, int[] n2, int[] n3) {
            Set<Integer> allVals = new HashSet<>();
            for (int x : n1) allVals.add(x);
            for (int x : n2) allVals.add(x);
            for (int x : n3) allVals.add(x);

            List<Integer> result = new ArrayList<>();
            for (int val : allVals) {
                int cnt = (contains(n1, val) ? 1 : 0)
                        + (contains(n2, val) ? 1 : 0)
                        + (contains(n3, val) ? 1 : 0);
                if (cnt >= 2) result.add(val);
            }
            Collections.sort(result);
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Pairwise Set Intersections Union
    // Time: O(n + m + k)  |  Space: O(n + m + k)
    // s1 & s2 gives elements in both 1 and 2; union with
    // s1 & s3 and s2 & s3 to get all elements in >= 2 arrays.
    // ============================================================
    static class Optimal {
        public static List<Integer> twoOutOfThree(int[] n1, int[] n2, int[] n3) {
            Set<Integer> s1 = new HashSet<>(), s2 = new HashSet<>(), s3 = new HashSet<>();
            for (int x : n1) s1.add(x);
            for (int x : n2) s2.add(x);
            for (int x : n3) s3.add(x);

            Set<Integer> result = new HashSet<>(s1);
            result.retainAll(s2);                  // s1 & s2

            Set<Integer> tmp = new HashSet<>(s1);
            tmp.retainAll(s3);
            result.addAll(tmp);                    // union s1 & s3

            tmp = new HashSet<>(s2);
            tmp.retainAll(s3);
            result.addAll(tmp);                    // union s2 & s3

            List<Integer> out = new ArrayList<>(result);
            Collections.sort(out);
            return out;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Occurrence Counter (Single HashMap)
    // Time: O(n + m + k)  |  Space: O(n + m + k)
    // For each array, add its DISTINCT values to a count map.
    // Values with count >= 2 appear in at least 2 arrays.
    // Generalises cleanly to any number of input arrays.
    // ============================================================
    static class Best {
        public static List<Integer> twoOutOfThree(int[] n1, int[] n2, int[] n3) {
            Map<Integer, Integer> count = new HashMap<>();
            int[][] arrays = {n1, n2, n3};
            for (int[] arr : arrays) {
                Set<Integer> seen = new HashSet<>();
                for (int x : arr) {
                    if (seen.add(x)) {                       // first occurrence in this array
                        count.merge(x, 1, Integer::sum);
                    }
                }
            }
            List<Integer> result = new ArrayList<>();
            for (Map.Entry<Integer, Integer> e : count.entrySet()) {
                if (e.getValue() >= 2) result.add(e.getKey());
            }
            Collections.sort(result);
            return result;
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 1, 3, 2}, b = {2, 3}, c = {3};
        System.out.println("=== Two Out of Three ===");
        System.out.println("Brute:   " + BruteForce.twoOutOfThree(a, b, c));
        System.out.println("Optimal: " + Optimal.twoOutOfThree(a, b, c));
        System.out.println("Best:    " + Best.twoOutOfThree(a, b, c));

        int[] a2 = {3, 1}, b2 = {2, 3}, c2 = {1, 2};
        System.out.println("Brute:   " + BruteForce.twoOutOfThree(a2, b2, c2));
        System.out.println("Optimal: " + Optimal.twoOutOfThree(a2, b2, c2));
        System.out.println("Best:    " + Best.twoOutOfThree(a2, b2, c2));
    }
}
