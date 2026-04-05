/**
 * Problem: Print All Divisors
 * Difficulty: EASY | XP: 10
 *
 * Given N, print all divisors of N in sorted ascending order.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check Every Number 1 to N
    // Time: O(N)  |  Space: O(d)
    // Iterate 1..N, collect those that divide N evenly.
    // ============================================================
    static class BruteForce {
        public static List<Integer> getDivisors(int n) {
            List<Integer> result = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                if (n % i == 0) {
                    result.add(i);
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Iterate to sqrt(N), Sort
    // Time: O(sqrt(N) + d*log(d))  |  Space: O(d)
    // Use divisor pairing: if i divides N, so does N/i.
    // ============================================================
    static class Optimal {
        public static List<Integer> getDivisors(int n) {
            List<Integer> result = new ArrayList<>();
            for (int i = 1; i * i <= n; i++) {
                if (n % i == 0) {
                    result.add(i);
                    if (i != n / i) {
                        result.add(n / i);
                    }
                }
            }
            Collections.sort(result);
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- sqrt(N) with Two-List Merge (No Sort)
    // Time: O(sqrt(N))  |  Space: O(d)
    // Maintain low and high lists to avoid sorting.
    // ============================================================
    static class Best {
        public static List<Integer> getDivisors(int n) {
            List<Integer> low = new ArrayList<>();
            List<Integer> high = new ArrayList<>();

            for (int i = 1; i * i <= n; i++) {
                if (n % i == 0) {
                    low.add(i);
                    if (i != n / i) {
                        high.add(n / i);
                    }
                }
            }

            // high is in descending order, reverse it
            Collections.reverse(high);
            low.addAll(high);
            return low;
        }
    }

    // ============================================================
    // TESTS
    // ============================================================
    public static void main(String[] args) {
        int[] testCases = {1, 7, 12, 36, 100};

        System.out.println("=== Print All Divisors ===\n");
        for (int n : testCases) {
            List<Integer> b = BruteForce.getDivisors(n);
            List<Integer> o = Optimal.getDivisors(n);
            List<Integer> bt = Best.getDivisors(n);

            boolean match = b.equals(o) && o.equals(bt);
            String status = match ? "PASS" : "FAIL";

            System.out.printf("[%s] N=%-6d | Brute: %-30s%n", status, n, b);
            System.out.printf("              | Optimal: %-30s%n", o);
            System.out.printf("              | Best:    %-30s%n%n", bt);
        }
    }
}
