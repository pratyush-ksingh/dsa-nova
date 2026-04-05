/**
 * Problem: Fibonacci Number (LeetCode #509)
 * Difficulty: EASY | XP: 10
 *
 * Compute the n-th Fibonacci number.
 * F(0)=0, F(1)=1, F(n)=F(n-1)+F(n-2).
 *
 * @author DSA_Nova
 */
import java.util.HashMap;
import java.util.Map;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Naive Recursion
    // Time: O(2^n)  |  Space: O(n) stack
    // Directly implement the recurrence. Exponentially slow.
    // ============================================================
    static class BruteForce {
        public static int fib(int n) {
            if (n <= 1) return n;
            return fib(n - 1) + fib(n - 2);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Memoized Recursion (Top-Down DP)
    // Time: O(n)  |  Space: O(n) memo + stack
    // Cache each F(k) on first computation. Eliminates redundancy.
    // ============================================================
    static class Optimal {
        private static Map<Integer, Integer> memo = new HashMap<>();

        public static int fib(int n) {
            if (n <= 1) return n;
            if (memo.containsKey(n)) return memo.get(n);
            int result = fib(n - 1) + fib(n - 2);
            memo.put(n, result);
            return result;
        }

        public static void resetMemo() {
            memo.clear();
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative Bottom-Up (O(1) Space)
    // Time: O(n)  |  Space: O(1)
    // Only keep previous two values. No recursion, no memo.
    // ============================================================
    static class Best {
        public static int fib(int n) {
            if (n <= 1) return n;
            int prev2 = 0; // F(i-2)
            int prev1 = 1; // F(i-1)
            for (int i = 2; i <= n; i++) {
                int curr = prev1 + prev2;
                prev2 = prev1;
                prev1 = curr;
            }
            return prev1;
        }
    }

    // ============================================================
    // TESTS
    // ============================================================
    public static void main(String[] args) {
        int[] testCases = {0, 1, 2, 3, 5, 10, 20, 30};
        int[] expected =  {0, 1, 1, 2, 5, 55, 6765, 832040};

        System.out.println("=== Fibonacci Number ===\n");
        for (int i = 0; i < testCases.length; i++) {
            int n = testCases[i];
            Optimal.resetMemo();

            int b = BruteForce.fib(n);
            int o = Optimal.fib(n);
            int bt = Best.fib(n);

            String status = (b == expected[i] && o == expected[i] && bt == expected[i]) ? "PASS" : "FAIL";
            System.out.printf("[%s] n=%-4d | Naive=%-10d | Memo=%-10d | Iterative=%-10d | Expected=%-10d%n",
                    status, n, b, o, bt, expected[i]);
        }
    }
}
