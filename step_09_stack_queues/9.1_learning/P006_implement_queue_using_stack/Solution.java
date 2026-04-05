/**
 * Problem: Implement Queue using Stack (LeetCode #232)
 * Difficulty: EASY | XP: 10
 *
 * Implement a FIFO queue using only standard stack operations.
 *
 * @author DSA_Nova
 */
import java.util.Stack;

// ============================================================
// Approach 1: Brute Force (Single Stack, Costly Pop)
// push O(1), pop O(n), peek O(n)
// ============================================================
class BruteForceQueue {
    private Stack<Integer> main = new Stack<>();
    private Stack<Integer> temp = new Stack<>();

    /** Push: just push onto main. O(1). */
    public void push(int x) {
        main.push(x);
    }

    /**
     * Pop: transfer all to temp (reverses order), pop top (oldest element),
     * transfer everything back. O(n).
     */
    public int pop() {
        while (!main.isEmpty()) {
            temp.push(main.pop());
        }
        int front = temp.pop();
        while (!temp.isEmpty()) {
            main.push(temp.pop());
        }
        return front;
    }

    /** Peek: same as pop but push the element back. O(n). */
    public int peek() {
        while (!main.isEmpty()) {
            temp.push(main.pop());
        }
        int front = temp.peek();
        while (!temp.isEmpty()) {
            main.push(temp.pop());
        }
        return front;
    }

    /** Empty check. O(1). */
    public boolean empty() {
        return main.isEmpty();
    }
}

// ============================================================
// Approach 2: Optimal (Two Stacks, Lazy Transfer)
// push O(1), pop amortized O(1), peek amortized O(1)
// ============================================================
class OptimalQueue {
    private Stack<Integer> inbox  = new Stack<>();
    private Stack<Integer> outbox = new Stack<>();

    /** Push: always goes to inbox. O(1). */
    public void push(int x) {
        inbox.push(x);
    }

    /**
     * Pop: if outbox is empty, transfer all from inbox.
     * Pop from outbox. Amortized O(1).
     */
    public int pop() {
        if (outbox.isEmpty()) {
            transfer();
        }
        return outbox.pop();
    }

    /** Peek: if outbox is empty, transfer all from inbox. Amortized O(1). */
    public int peek() {
        if (outbox.isEmpty()) {
            transfer();
        }
        return outbox.peek();
    }

    /** Empty: both stacks must be empty. O(1). */
    public boolean empty() {
        return inbox.isEmpty() && outbox.isEmpty();
    }

    /** Move all from inbox to outbox (reverses order). */
    private void transfer() {
        while (!inbox.isEmpty()) {
            outbox.push(inbox.pop());
        }
    }
}

// ============================================================
// Approach 3: Best (Two Stacks + Front Cache for O(1) Peek)
// push O(1), pop amortized O(1), peek O(1) worst-case
// ============================================================
class BestQueue {
    private Stack<Integer> inbox  = new Stack<>();
    private Stack<Integer> outbox = new Stack<>();
    private int front; // cached front element for O(1) peek

    /** Push: track front when inbox is empty. O(1). */
    public void push(int x) {
        if (inbox.isEmpty()) {
            front = x; // this is the oldest element if outbox is empty later
        }
        inbox.push(x);
    }

    /** Pop: lazy transfer if outbox empty. Amortized O(1). */
    public int pop() {
        if (outbox.isEmpty()) {
            transfer();
        }
        return outbox.pop();
    }

    /** Peek: use outbox top if available, else cached front. O(1) worst-case. */
    public int peek() {
        if (!outbox.isEmpty()) {
            return outbox.peek();
        }
        return front;
    }

    /** Empty: both stacks must be empty. O(1). */
    public boolean empty() {
        return inbox.isEmpty() && outbox.isEmpty();
    }

    private void transfer() {
        while (!inbox.isEmpty()) {
            outbox.push(inbox.pop());
        }
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Implement Queue using Stack ===\n");

        System.out.println("--- Approach 1: Single Stack, Costly Pop ---");
        testBrute();

        System.out.println("--- Approach 2: Two Stacks, Lazy Transfer ---");
        testOptimal();

        System.out.println("--- Approach 3: Two Stacks + Front Cache ---");
        testBest();
    }

    private static void testBrute() {
        BruteForceQueue q = new BruteForceQueue();
        q.push(1); q.push(2); q.push(3);
        System.out.println("After push(1,2,3):");
        System.out.println("  peek()  = " + q.peek() + " (expected 1) " + (q.peek() == 1 ? "PASS" : "FAIL"));
        System.out.println("  pop()   = " + q.pop() + " (expected 1)");
        System.out.println("  pop()   = " + q.pop() + " (expected 2)");
        q.push(4);
        System.out.println("  push(4), peek() = " + q.peek() + " (expected 3) " + (q.peek() == 3 ? "PASS" : "FAIL"));
        System.out.println("  pop()   = " + q.pop() + " (expected 3)");
        System.out.println("  pop()   = " + q.pop() + " (expected 4)");
        System.out.println("  empty() = " + q.empty() + " (expected true) " + (q.empty() ? "PASS" : "FAIL"));
        System.out.println();
    }

    private static void testOptimal() {
        OptimalQueue q = new OptimalQueue();
        q.push(1); q.push(2); q.push(3);
        System.out.println("After push(1,2,3):");
        System.out.println("  peek()  = " + q.peek() + " (expected 1) " + (q.peek() == 1 ? "PASS" : "FAIL"));
        System.out.println("  pop()   = " + q.pop() + " (expected 1)");
        System.out.println("  pop()   = " + q.pop() + " (expected 2)");
        q.push(4);
        System.out.println("  push(4), peek() = " + q.peek() + " (expected 3) " + (q.peek() == 3 ? "PASS" : "FAIL"));
        System.out.println("  pop()   = " + q.pop() + " (expected 3)");
        System.out.println("  pop()   = " + q.pop() + " (expected 4)");
        System.out.println("  empty() = " + q.empty() + " (expected true) " + (q.empty() ? "PASS" : "FAIL"));
        System.out.println();
    }

    private static void testBest() {
        BestQueue q = new BestQueue();
        q.push(1); q.push(2); q.push(3);
        System.out.println("After push(1,2,3):");
        System.out.println("  peek()  = " + q.peek() + " (expected 1) " + (q.peek() == 1 ? "PASS" : "FAIL"));
        System.out.println("  pop()   = " + q.pop() + " (expected 1)");
        System.out.println("  pop()   = " + q.pop() + " (expected 2)");
        q.push(4);
        System.out.println("  push(4), peek() = " + q.peek() + " (expected 3) " + (q.peek() == 3 ? "PASS" : "FAIL"));
        System.out.println("  pop()   = " + q.pop() + " (expected 3)");
        System.out.println("  pop()   = " + q.pop() + " (expected 4)");
        System.out.println("  empty() = " + q.empty() + " (expected true) " + (q.empty() ? "PASS" : "FAIL"));
        System.out.println();
    }
}
