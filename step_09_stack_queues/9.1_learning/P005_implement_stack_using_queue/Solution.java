/**
 * Problem: Implement Stack using Queue (LeetCode #225)
 * Difficulty: EASY | XP: 10
 *
 * Implement a LIFO stack using only standard queue operations.
 *
 * @author DSA_Nova
 */
import java.util.LinkedList;
import java.util.Queue;

// ============================================================
// Approach 1: Brute Force (Two Queues, Costly Pop)
// push O(1), pop O(n), top O(n)
// ============================================================
class BruteForceStack {
    private Queue<Integer> q1 = new LinkedList<>();
    private Queue<Integer> q2 = new LinkedList<>();

    /** Push: just enqueue into q1. O(1). */
    public void push(int x) {
        q1.offer(x);
    }

    /** Pop: move all but last from q1 to q2, dequeue last, swap. O(n). */
    public int pop() {
        // Move n-1 elements to q2
        while (q1.size() > 1) {
            q2.offer(q1.poll());
        }
        int top = q1.poll(); // the "stack top" is the last enqueued

        // Swap q1 and q2
        Queue<Integer> temp = q1;
        q1 = q2;
        q2 = temp;

        return top;
    }

    /** Top: same as pop but put the element back. O(n). */
    public int top() {
        while (q1.size() > 1) {
            q2.offer(q1.poll());
        }
        int top = q1.poll();
        q2.offer(top); // put it back

        Queue<Integer> temp = q1;
        q1 = q2;
        q2 = temp;

        return top;
    }

    /** Empty check. O(1). */
    public boolean empty() {
        return q1.isEmpty();
    }
}

// ============================================================
// Approach 2: Optimal (Two Queues, Costly Push)
// push O(n), pop O(1), top O(1)
// ============================================================
class OptimalStack {
    private Queue<Integer> q1 = new LinkedList<>();
    private Queue<Integer> q2 = new LinkedList<>();

    /** Push: enqueue into q2, move all of q1 into q2, swap. O(n). */
    public void push(int x) {
        q2.offer(x);

        // Move all from q1 to q2 (so x is at front)
        while (!q1.isEmpty()) {
            q2.offer(q1.poll());
        }

        // Swap q1 and q2
        Queue<Integer> temp = q1;
        q1 = q2;
        q2 = temp;
    }

    /** Pop: front of q1 is the stack top. O(1). */
    public int pop() {
        return q1.poll();
    }

    /** Top: peek at front. O(1). */
    public int top() {
        return q1.peek();
    }

    /** Empty check. O(1). */
    public boolean empty() {
        return q1.isEmpty();
    }
}

// ============================================================
// Approach 3: Best (Single Queue, Costly Push with Rotation)
// push O(n), pop O(1), top O(1)
// ============================================================
class BestStack {
    private Queue<Integer> q = new LinkedList<>();

    /**
     * Push: enqueue x, then rotate previous (size-1) elements behind it.
     * After rotation, x is at the front of the queue. O(n).
     */
    public void push(int x) {
        q.offer(x);
        int rotations = q.size() - 1;
        for (int i = 0; i < rotations; i++) {
            q.offer(q.poll()); // dequeue front, enqueue to back
        }
    }

    /** Pop: dequeue from front (most recently pushed). O(1). */
    public int pop() {
        return q.poll();
    }

    /** Top: peek at front. O(1). */
    public int top() {
        return q.peek();
    }

    /** Empty check. O(1). */
    public boolean empty() {
        return q.isEmpty();
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Implement Stack using Queue ===\n");

        // Test all three approaches with the same sequence
        System.out.println("--- Approach 1: Two Queues, Costly Pop ---");
        BruteForceStack s1 = new BruteForceStack();
        testStack(s1);

        System.out.println("--- Approach 2: Two Queues, Costly Push ---");
        OptimalStack s2 = new OptimalStack();
        testStack(s2);

        System.out.println("--- Approach 3: Single Queue, Costly Push ---");
        BestStack s3 = new BestStack();
        testStack(s3);
    }

    // Generic test using the same operations
    private static void testStack(Object stack) {
        // We use reflection-like manual calls since Java doesn't have duck typing
        if (stack instanceof BruteForceStack) {
            BruteForceStack s = (BruteForceStack) stack;
            s.push(1); s.push(2); s.push(3);
            System.out.println("After push(1,2,3):");
            System.out.println("  top()   = " + s.top() + " (expected 3) " + (s.top() == 3 ? "PASS" : "FAIL"));
            System.out.println("  pop()   = " + s.pop() + " (expected 3)");
            System.out.println("  pop()   = " + s.pop() + " (expected 2)");
            System.out.println("  top()   = " + s.top() + " (expected 1) " + (s.top() == 1 ? "PASS" : "FAIL"));
            System.out.println("  empty() = " + s.empty() + " (expected false) " + (!s.empty() ? "PASS" : "FAIL"));
            System.out.println("  pop()   = " + s.pop() + " (expected 1)");
            System.out.println("  empty() = " + s.empty() + " (expected true) " + (s.empty() ? "PASS" : "FAIL"));
        } else if (stack instanceof OptimalStack) {
            OptimalStack s = (OptimalStack) stack;
            s.push(1); s.push(2); s.push(3);
            System.out.println("After push(1,2,3):");
            System.out.println("  top()   = " + s.top() + " (expected 3) " + (s.top() == 3 ? "PASS" : "FAIL"));
            System.out.println("  pop()   = " + s.pop() + " (expected 3)");
            System.out.println("  pop()   = " + s.pop() + " (expected 2)");
            System.out.println("  top()   = " + s.top() + " (expected 1) " + (s.top() == 1 ? "PASS" : "FAIL"));
            System.out.println("  empty() = " + s.empty() + " (expected false) " + (!s.empty() ? "PASS" : "FAIL"));
            System.out.println("  pop()   = " + s.pop() + " (expected 1)");
            System.out.println("  empty() = " + s.empty() + " (expected true) " + (s.empty() ? "PASS" : "FAIL"));
        } else if (stack instanceof BestStack) {
            BestStack s = (BestStack) stack;
            s.push(1); s.push(2); s.push(3);
            System.out.println("After push(1,2,3):");
            System.out.println("  top()   = " + s.top() + " (expected 3) " + (s.top() == 3 ? "PASS" : "FAIL"));
            System.out.println("  pop()   = " + s.pop() + " (expected 3)");
            System.out.println("  pop()   = " + s.pop() + " (expected 2)");
            System.out.println("  top()   = " + s.top() + " (expected 1) " + (s.top() == 1 ? "PASS" : "FAIL"));
            System.out.println("  empty() = " + s.empty() + " (expected false) " + (!s.empty() ? "PASS" : "FAIL"));
            System.out.println("  pop()   = " + s.pop() + " (expected 1)");
            System.out.println("  empty() = " + s.empty() + " (expected true) " + (s.empty() ? "PASS" : "FAIL"));
        }
        System.out.println();
    }
}
