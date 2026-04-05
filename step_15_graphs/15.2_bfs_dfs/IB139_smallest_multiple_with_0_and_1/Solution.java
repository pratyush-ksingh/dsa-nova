/**
 * Problem: Smallest Multiple With 0 and 1
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given a positive integer N, find the smallest multiple of N that consists
 * only of digits 0 and 1. Return as a string (answer can be very large).
 *
 * Key insight: BFS on remainders mod N.
 * Numbers with only 0/1 digits: 1, 10, 11, 100, 101, 110, 111, ...
 * BFS builds them level by level; track remainder to avoid duplicates.
 *
 * Real-life use: Number theory, digital circuits, modular arithmetic puzzles.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(2^digits)  |  Space: O(2^digits)
    // Generate all 01-numbers in sorted order using a queue and check
    // divisibility. Simple but can be slow for large N.
    // ============================================================
    public static String bruteForce(int n) {
        if (n == 1) return "1";
        // BFS over 01-numbers in increasing order
        Queue<Long> queue = new LinkedList<>();
        queue.offer(1L);
        while (!queue.isEmpty()) {
            long num = queue.poll();
            if (num % n == 0) return String.valueOf(num);
            long n0 = num * 10;
            long n1 = num * 10 + 1;
            // Guard against overflow (works for small N ~< 300)
            if (n0 > 0) queue.offer(n0);
            if (n1 > 0) queue.offer(n1);
        }
        return "-1";
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(N)  |  Space: O(N)
    // BFS on remainder states (0 to N-1). Each BFS node = a remainder.
    // Transitions: rem -> (rem*10) % N  (append 0)
    //              rem -> (rem*10 + 1) % N  (append 1)
    // First time we reach remainder 0, we found the answer.
    // Reconstruct via parent tracking (no need to store huge numbers).
    // ============================================================
    public static String optimal(int n) {
        if (n == 1) return "1";

        int[] parent = new int[n];
        int[] digitUsed = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(parent, -1);

        Queue<Integer> queue = new LinkedList<>();
        int startRem = 1 % n;
        visited[startRem] = true;
        parent[startRem] = n; // sentinel for root (no parent)
        digitUsed[startRem] = 1;
        if (startRem == 0) return "1";
        queue.offer(startRem);

        int found = -1;
        outer:
        while (!queue.isEmpty()) {
            int rem = queue.poll();
            int[] nextDigits = {0, 1};
            for (int d : nextDigits) {
                int nextRem = (rem * 10 + d) % n;
                if (!visited[nextRem]) {
                    visited[nextRem] = true;
                    parent[nextRem] = rem;
                    digitUsed[nextRem] = d;
                    if (nextRem == 0) {
                        found = nextRem;
                        break outer;
                    }
                    queue.offer(nextRem);
                }
            }
        }

        // Backtrack to build the number
        StringBuilder sb = new StringBuilder();
        int cur = found;
        while (cur != n) { // n is the sentinel
            sb.append(digitUsed[cur]);
            cur = parent[cur];
        }
        sb.reverse();
        return sb.toString();
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(N)  |  Space: O(N)
    // Same BFS on remainders, but stores the number string directly
    // at each remainder. Cleaner to understand, same complexity.
    // For N up to ~1000 this is practical; for huge N use optimal.
    // ============================================================
    public static String best(int n) {
        if (n == 1) return "1";

        String[] numAtRem = new String[n];
        Queue<Integer> queue = new LinkedList<>();

        int r = 1 % n;
        numAtRem[r] = "1";
        if (r == 0) return "1";
        queue.offer(r);

        while (!queue.isEmpty()) {
            int rem = queue.poll();
            String cur = numAtRem[rem];
            // Append '0' first (gives smaller number)
            for (int d = 0; d <= 1; d++) {
                int nr = (rem * 10 + d) % n;
                if (numAtRem[nr] == null) {
                    numAtRem[nr] = cur + d;
                    if (nr == 0) return numAtRem[nr];
                    queue.offer(nr);
                }
            }
        }
        return "-1";
    }

    public static void main(String[] args) {
        System.out.println("=== Smallest Multiple With 0 and 1 ===\n");

        int[] tests = {2, 3, 5, 7, 12, 13};
        for (int n : tests) {
            String op = optimal(n);
            String be = best(n);
            // Verify
            long val = Long.parseLong(be.length() <= 18 ? be : be.substring(0, 18));
            System.out.printf("N=%3d | Optimal: %-20s | Best: %-20s%n", n, op, be);
        }

        // Verify correctness
        System.out.println("\n--- Verification ---");
        for (int n : new int[]{3, 7, 13}) {
            String res = best(n);
            // Check: every char is 0 or 1
            boolean onlyBinary = res.chars().allMatch(c -> c == '0' || c == '1');
            // Check divisibility via modular arithmetic
            int rem = 0;
            for (char c : res.toCharArray()) rem = (rem * 10 + (c - '0')) % n;
            System.out.printf("N=%2d -> %s, onlyBinary=%b, divisible=%b%n",
                    n, res, onlyBinary, rem == 0);
        }
    }
}
