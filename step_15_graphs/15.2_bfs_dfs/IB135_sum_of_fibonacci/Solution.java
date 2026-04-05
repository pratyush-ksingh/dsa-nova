/**
 * Problem: Sum of Fibonacci (Zeckendorf's Representation)
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given a positive integer N, represent it as a sum of non-repeating
 * Fibonacci numbers (Zeckendorf's theorem guarantees a unique representation).
 *
 * Real-life use: Data compression, number theory, coding theory.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(log(N) * log(N))  |  Space: O(log(N))
    // Generate all Fibonacci numbers up to N, then greedily subtract
    // the largest Fibonacci <= remaining value.
    // ============================================================
    public static List<Integer> bruteForce(int n) {
        // Generate all Fibonacci numbers up to n
        List<Integer> fibs = new ArrayList<>();
        int a = 1, b = 2;
        fibs.add(a);
        while (b <= n) {
            fibs.add(b);
            int next = a + b;
            a = b;
            b = next;
        }

        List<Integer> result = new ArrayList<>();
        int remaining = n;
        // Greedily pick largest Fibonacci <= remaining
        for (int i = fibs.size() - 1; i >= 0 && remaining > 0; i--) {
            if (fibs.get(i) <= remaining) {
                result.add(fibs.get(i));
                remaining -= fibs.get(i);
            }
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(log(N))  |  Space: O(log(N))
    // Same greedy approach but generate Fibonacci numbers on-the-fly
    // using binary search style: find largest fib <= n efficiently.
    // Zeckendorf's theorem: every positive integer has a unique
    // representation as sum of non-consecutive Fibonacci numbers.
    // ============================================================
    public static List<Integer> optimal(int n) {
        // Find the largest Fibonacci number <= n
        int a = 1, b = 1;
        while (b <= n) {
            int c = a + b;
            a = b;
            b = c;
        }
        // Now 'a' is the largest Fibonacci <= n
        List<Integer> result = new ArrayList<>();
        int remaining = n;
        int prev = b, curr = a;
        while (remaining > 0 && curr >= 1) {
            if (curr <= remaining) {
                result.add(curr);
                remaining -= curr;
            }
            int prevPrev = prev - curr;
            prev = curr;
            curr = prevPrev;
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(log(N))  |  Space: O(log(N))
    // Pre-generate Fibonacci sequence up to n, then greedily subtract.
    // This is the canonical implementation of Zeckendorf's algorithm.
    // The result always consists of non-consecutive Fibonacci numbers.
    // ============================================================
    public static List<Integer> best(int n) {
        if (n <= 0) return new ArrayList<>();

        // Generate Fibonacci numbers up to n
        List<Integer> fibs = new ArrayList<>();
        int f1 = 1, f2 = 2;
        fibs.add(f1);
        while (f2 <= n) {
            fibs.add(f2);
            int f3 = f1 + f2;
            f1 = f2;
            f2 = f3;
        }

        List<Integer> zeckendorf = new ArrayList<>();
        int rem = n;
        // Zeckendorf's greedy: always pick largest fib <= remaining
        for (int i = fibs.size() - 1; i >= 0; i--) {
            if (fibs.get(i) <= rem) {
                zeckendorf.add(fibs.get(i));
                rem -= fibs.get(i);
                if (rem == 0) break;
            }
        }
        return zeckendorf;
    }

    public static void main(String[] args) {
        System.out.println("=== Sum of Fibonacci (Zeckendorf's Representation) ===\n");

        int[] tests = {1, 2, 3, 5, 10, 13, 100, 143};
        for (int n : tests) {
            List<Integer> bf = bruteForce(n);
            List<Integer> op = optimal(n);
            List<Integer> be = best(n);
            System.out.printf("N=%3d | Brute: %-30s | Optimal: %-30s | Best: %s%n",
                    n, bf, op, be);
        }

        // Verify correctness
        System.out.println("\n--- Verification ---");
        int[] verify = {10, 20, 30, 100};
        for (int n : verify) {
            List<Integer> res = best(n);
            int sum = res.stream().mapToInt(Integer::intValue).sum();
            System.out.printf("N=%3d -> %s, Sum=%d, Valid=%b%n", n, res, sum, sum == n);
        }
    }
}
