import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Iterate all numbers, check each
// Time: O((B-A) * digits)  |  Space: O(result size)
// ============================================================
// For every number in [A,B], check if it is a stepping number.
// A number is stepping if every pair of adjacent digits differs
// by exactly 1. Simple but slow for large ranges.
// ============================================================

class BruteForce {
    private boolean isStepping(int n) {
        if (n < 10) return true;
        while (n >= 10) {
            int d1 = n % 10;
            n /= 10;
            int d2 = n % 10;
            if (Math.abs(d1 - d2) != 1) return false;
        }
        return true;
    }

    public List<Integer> steppingNumbers(int A, int B) {
        List<Integer> res = new ArrayList<>();
        for (int n = A; n <= B; n++) {
            if (isStepping(n)) res.add(n);
        }
        return res;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - BFS from each single digit
// Time: O(result size * 10)  |  Space: O(result size)
// ============================================================
// Start BFS from digits 1-9. From a number n, extend by
// appending last_digit-1 and last_digit+1. Stop when number
// exceeds B. Add to result if in [A,B]. Handle 0 separately.
// ============================================================

class Optimal {
    public List<Integer> steppingNumbers(int A, int B) {
        List<Integer> res = new ArrayList<>();
        if (A == 0) res.add(0);

        Queue<Long> q = new LinkedList<>();
        for (int d = 1; d <= 9; d++) q.add((long) d);

        while (!q.isEmpty()) {
            long n = q.poll();
            if (n > B) continue;
            if (n >= A) res.add((int) n);

            int last = (int)(n % 10);
            if (last > 0) q.add(n * 10 + (last - 1));
            if (last < 9) q.add(n * 10 + (last + 1));
        }

        Collections.sort(res);
        return res;
    }
}

// ============================================================
// APPROACH 3: BEST - DFS (iterative, same complexity as BFS)
// Time: O(result size * 10)  |  Space: O(depth * 10)
// ============================================================
// DFS explores the digit tree depth-first. Memory usage is
// O(log10(B)) stack depth vs BFS's potentially wider queue.
// Both are optimal; DFS has smaller practical memory footprint.
// Real-life use: generating phone number patterns or PIN codes
// for smooth keyboard-swipe entry on numeric keypads.
// ============================================================

class Best {
    public List<Integer> steppingNumbers(int A, int B) {
        List<Integer> res = new ArrayList<>();
        if (A == 0) res.add(0);

        Deque<Long> stack = new ArrayDeque<>();
        for (int d = 1; d <= 9; d++) stack.push((long) d);

        while (!stack.isEmpty()) {
            long n = stack.pop();
            if (n > B) continue;
            if (n >= A) res.add((int) n);

            int last = (int)(n % 10);
            // Push in reverse so smaller numbers come out first (optional sorting)
            if (last < 9) stack.push(n * 10 + (last + 1));
            if (last > 0) stack.push(n * 10 + (last - 1));
        }

        Collections.sort(res);
        return res;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================

public class Solution {
    public static void main(String[] args) {
        // Test 1: A=0, B=21 => [0,1,2,3,4,5,6,7,8,9,10,12,21]
        System.out.println("=== Stepping Numbers ===");
        System.out.println("Test1 Brute   (expect [0..9,10,12,21]): " +
            new BruteForce().steppingNumbers(0, 21));
        System.out.println("Test1 Optimal (expect [0..9,10,12,21]): " +
            new Optimal().steppingNumbers(0, 21));
        System.out.println("Test1 Best    (expect [0..9,10,12,21]): " +
            new Best().steppingNumbers(0, 21));

        // Test 2: A=10, B=15 => [10,12]
        System.out.println("Test2 Best    (expect [10,12]): " +
            new Best().steppingNumbers(10, 15));

        // Test 3: A=100, B=130 => [101,121,123]
        System.out.println("Test3 Best    (expect [101,121,123]): " +
            new Best().steppingNumbers(100, 130));

        // Test 4: A=0, B=0 => [0]
        System.out.println("Test4 Best    (expect [0]): " +
            new Best().steppingNumbers(0, 0));
    }
}
