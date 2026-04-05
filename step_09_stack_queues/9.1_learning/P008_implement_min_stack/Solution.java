/**
 * Problem: Implement Min Stack (LeetCode 155)
 * Difficulty: MEDIUM | XP: 25
 *
 * Design a stack supporting push, pop, top, and getMin — all O(1).
 *
 * @author DSA_Nova
 */
import java.util.Stack;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Stack of (value, currentMin) pairs
    // Time: O(1) all ops  |  Space: O(n) — stores min at every level
    // ============================================================
    static class BruteForce {
        private Stack<long[]> stack; // [value, minSoFar]

        public BruteForce() {
            stack = new Stack<>();
        }

        public void push(int val) {
            long currentMin = stack.isEmpty() ? val : Math.min(val, stack.peek()[1]);
            stack.push(new long[]{val, currentMin});
        }

        public void pop() {
            stack.pop();
        }

        public int top() {
            return (int) stack.peek()[0];
        }

        public int getMin() {
            return (int) stack.peek()[1];
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Encoded value trick (single stack)
    // Time: O(1) all ops  |  Space: O(1) extra beyond the stack
    // ============================================================
    static class Optimal {
        /**
         * Store encoded values to embed the old minimum in the stack.
         * Uses long to avoid 32-bit overflow from (2*val - min).
         *
         * Push: if val < min, store (2L*val - min) and update min = val.
         * Pop:  if top < min, old_min = 2*min - top; restore it.
         * Top:  if top < min, real top = min; else real top = top.
         */
        private Stack<Long> stack;
        private long min;

        public Optimal() {
            stack = new Stack<>();
            min = Long.MAX_VALUE;
        }

        public void push(int val) {
            if (stack.isEmpty()) {
                stack.push((long) val);
                min = val;
            } else if (val < min) {
                stack.push(2L * val - min);
                min = val;
            } else {
                stack.push((long) val);
            }
        }

        public void pop() {
            long top = stack.pop();
            if (top < min) {
                min = 2 * min - top; // recover old min
            }
        }

        public int top() {
            long top = stack.peek();
            return (int)(top < min ? min : top);
        }

        public int getMin() {
            return (int) min;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Two-stack (auxiliary min-stack)
    // Time: O(1) all ops  |  Space: O(n) worst case, but clear & safe
    // ============================================================
    static class Best {
        /**
         * Two stacks:
         * - stack     : normal value stack.
         * - minStack  : tracks running minimums.
         *   Push to minStack when val <= current min.
         *   Pop from minStack when popped val == current min.
         *
         * Clean, overflow-safe, and the preferred interview answer.
         */
        private Stack<Integer> stack;
        private Stack<Integer> minStack;

        public Best() {
            stack = new Stack<>();
            minStack = new Stack<>();
        }

        public void push(int val) {
            stack.push(val);
            if (minStack.isEmpty() || val <= minStack.peek())
                minStack.push(val);
        }

        public void pop() {
            int val = stack.pop();
            if (val == minStack.peek())
                minStack.pop();
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Implement Min Stack ===");
        String[] names = {"Brute", "Optimal", "Best"};
        Object[] instances = {new BruteForce(), new Optimal(), new Best()};

        for (int i = 0; i < 3; i++) {
            String name = names[i];
            if (i == 0) {
                BruteForce ms = (BruteForce) instances[i];
                ms.push(-2); ms.push(0); ms.push(-3);
                System.out.printf("%s  getMin=%d  top=%d", name, ms.getMin(), ms.top());
                ms.pop();
                System.out.printf("  after pop: top=%d  getMin=%d%n", ms.top(), ms.getMin());
            } else if (i == 1) {
                Optimal ms = (Optimal) instances[i];
                ms.push(-2); ms.push(0); ms.push(-3);
                System.out.printf("%s  getMin=%d  top=%d", name, ms.getMin(), ms.top());
                ms.pop();
                System.out.printf("  after pop: top=%d  getMin=%d%n", ms.top(), ms.getMin());
            } else {
                Best ms = (Best) instances[i];
                ms.push(-2); ms.push(0); ms.push(-3);
                System.out.printf("%s  getMin=%d  top=%d", name, ms.getMin(), ms.top());
                ms.pop();
                System.out.printf("  after pop: top=%d  getMin=%d%n", ms.top(), ms.getMin());
            }
        }
    }
}
