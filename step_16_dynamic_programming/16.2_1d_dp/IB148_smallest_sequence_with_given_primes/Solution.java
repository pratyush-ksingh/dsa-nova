/**
 * Problem: Smallest Sequence with Given Primes
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given three prime numbers A, B, C and an integer N, find the first N
 * numbers whose only prime factors are A, B, and C (sorted ascending).
 * This is the "Ugly Numbers" problem generalized to arbitrary primes.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(N * log(max_val))  |  Space: O(N)
    // For each candidate number starting from 2, check if it's A-B-C smooth.
    // Very slow when A, B, C are large primes.
    // ============================================================
    static class BruteForce {

        private static boolean isValid(long num, int A, int B, int C) {
            for (int p : new int[]{A, B, C}) {
                while (num % p == 0) num /= p;
            }
            return num == 1;
        }

        public static List<Integer> solve(int A, int B, int C, int N) {
            List<Integer> result = new ArrayList<>();
            long candidate = 1;
            while (result.size() < N) {
                candidate++;
                if (isValid(candidate, A, B, C)) {
                    result.add((int) candidate);
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Three-Pointer Merge
    // Time: O(N)  |  Space: O(N)
    // Maintain three pointers into the result array. At each step,
    // next candidates are result[pa]*A, result[pb]*B, result[pc]*C.
    // Pick minimum, advance pointer(s) that produced it.
    // ============================================================
    static class Optimal {

        public static List<Integer> solve(int A, int B, int C, int N) {
            int[] result = new int[N];
            int pa = 0, pb = 0, pc = 0;
            long nextA = A, nextB = B, nextC = C;

            for (int i = 0; i < N; i++) {
                long val = Math.min(nextA, Math.min(nextB, nextC));
                result[i] = (int) val;
                // Advance all pointers that produced this minimum (handle duplicates)
                if (val == nextA) { nextA = (long) result[pa++] * A; }
                if (val == nextB) { nextB = (long) result[pb++] * B; }
                if (val == nextC) { nextC = (long) result[pc++] * C; }
            }

            List<Integer> list = new ArrayList<>();
            for (int x : result) list.add(x);
            return list;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Min-Heap with Visited Set
    // Time: O(N log N)  |  Space: O(N)
    // Use a priority queue. Pop minimum, push multiples if not seen.
    // Cleanly handles all edge cases including equal primes.
    // ============================================================
    static class Best {

        public static List<Integer> solve(int A, int B, int C, int N) {
            PriorityQueue<Long> heap = new PriorityQueue<>();
            Set<Long> seen = new HashSet<>();

            for (int p : new int[]{A, B, C}) {
                if (seen.add((long) p)) heap.offer((long) p);
            }

            List<Integer> result = new ArrayList<>();
            while (result.size() < N) {
                long val = heap.poll();
                result.add((int) val);
                for (int p : new int[]{A, B, C}) {
                    long nxt = val * p;
                    if (seen.add(nxt)) heap.offer(nxt);
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Smallest Sequence with Given Primes ===");

        Object[][] tests = {
            {2, 3, 5, 5,  Arrays.asList(2, 3, 4, 5, 6)},
            {2, 3, 5, 10, Arrays.asList(2, 3, 4, 5, 6, 8, 9, 10, 12, 15)},
            {2, 3, 7, 5,  Arrays.asList(2, 3, 4, 6, 7)},
            {3, 5, 7, 4,  Arrays.asList(3, 5, 7, 9)},
        };

        for (Object[] t : tests) {
            int A = (int) t[0], B = (int) t[1], C = (int) t[2], N = (int) t[3];
            @SuppressWarnings("unchecked")
            List<Integer> expected = (List<Integer>) t[4];
            List<Integer> o = Optimal.solve(A, B, C, N);
            List<Integer> bt = Best.solve(A, B, C, N);
            String ok = (o.equals(expected) && bt.equals(expected)) ? "OK" : "FAIL";
            System.out.printf("  A=%d,B=%d,C=%d,N=%d | Expected=%s%n", A, B, C, N, expected);
            System.out.printf("    Optimal=%s, Best=%s [%s]%n", o, bt, ok);
        }
    }
}
