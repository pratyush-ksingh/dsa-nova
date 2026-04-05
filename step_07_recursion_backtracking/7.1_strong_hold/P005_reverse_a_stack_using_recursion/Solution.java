/**
 * Problem: Reverse a Stack using Recursion
 * Difficulty: MEDIUM | XP: 25
 *
 * Reverse a stack using only recursion — no loops, no extra data structures
 * (except the implicit call stack).
 *
 * @author DSA_Nova
 */
import java.util.Stack;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Auxiliary Stack
    // Time: O(n)  |  Space: O(n) extra
    // ============================================================
    static class BruteForce {
        /**
         * Transfer all elements to an auxiliary stack (reverses order),
         * then transfer back to the original stack.
         */
        public void reverseStack(Stack<Integer> stack) {
            Stack<Integer> aux = new Stack<>();
            while (!stack.isEmpty()) aux.push(stack.pop());
            while (!aux.isEmpty())  stack.push(aux.pop());
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Recursive Reverse + insertAtBottom
    // Time: O(n^2)  |  Space: O(n) call stack only
    // ============================================================
    static class Optimal {
        /**
         * Recursively insert an item at the bottom of the stack.
         */
        private void insertAtBottom(Stack<Integer> stack, int item) {
            if (stack.isEmpty()) {
                stack.push(item);
                return;
            }
            int top = stack.pop();
            insertAtBottom(stack, item);
            stack.push(top);
        }

        /**
         * Pop the top, reverse the rest recursively,
         * then insert the popped element at the bottom.
         */
        public void reverseStack(Stack<Integer> stack) {
            if (stack.size() <= 1) return;
            int top = stack.pop();
            reverseStack(stack);
            insertAtBottom(stack, top);
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Same approach, canonical clean version
    // Time: O(n^2)  |  Space: O(n) call stack only
    // ============================================================
    static class Best {
        /**
         * Canonical interview solution: only recursion, no auxiliary storage.
         * insertAtBottom is the key helper that threads each popped element
         * to the bottom after the rest of the stack is reversed.
         */
        private void insertAtBottom(Stack<Integer> stack, int item) {
            if (stack.isEmpty()) { stack.push(item); return; }
            int held = stack.pop();
            insertAtBottom(stack, item);
            stack.push(held);
        }

        public void reverseStack(Stack<Integer> stack) {
            if (stack.isEmpty()) return;
            int top = stack.pop();
            reverseStack(stack);
            insertAtBottom(stack, top);
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Reverse a Stack using Recursion ===");

        Stack<Integer> s1 = new Stack<>();
        for (int v : new int[]{1, 2, 3, 4, 5}) s1.push(v);
        new BruteForce().reverseStack(s1);
        System.out.println("Brute:   " + s1);   // [5, 4, 3, 2, 1]

        Stack<Integer> s2 = new Stack<>();
        for (int v : new int[]{1, 2, 3, 4, 5}) s2.push(v);
        new Optimal().reverseStack(s2);
        System.out.println("Optimal: " + s2);   // [5, 4, 3, 2, 1]

        Stack<Integer> s3 = new Stack<>();
        for (int v : new int[]{1, 2, 3, 4, 5}) s3.push(v);
        new Best().reverseStack(s3);
        System.out.println("Best:    " + s3);   // [5, 4, 3, 2, 1]
    }
}
