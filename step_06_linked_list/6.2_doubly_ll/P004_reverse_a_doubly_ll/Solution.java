/**
 * Problem: Reverse a Doubly Linked List
 * Difficulty: EASY | XP: 10
 *
 * Given the head of a doubly linked list, reverse it by swapping
 * prev and next pointers at every node. Return the new head.
 *
 * @author DSA_Nova
 */
import java.util.Stack;
import java.util.ArrayList;

public class Solution {

    static class DLLNode {
        int val;
        DLLNode prev;
        DLLNode next;
        DLLNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Stack-Based Value Reversal
    // Time: O(n)  |  Space: O(n)
    // Push all values to a stack, then pop and overwrite.
    // ============================================================
    static class BruteForce {
        public static DLLNode reverse(DLLNode head) {
            if (head == null) return null;

            // Pass 1: Collect all values onto a stack
            Stack<Integer> stack = new Stack<>();
            DLLNode current = head;
            while (current != null) {
                stack.push(current.val);
                current = current.next;
            }

            // Pass 2: Overwrite values from the stack
            current = head;
            while (current != null) {
                current.val = stack.pop();
                current = current.next;
            }

            return head;  // Same head node, reversed values
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Iterative Pointer Swap
    // Time: O(n)  |  Space: O(1)
    // Walk through each node, swap prev and next pointers.
    // ============================================================
    static class Optimal {
        public static DLLNode reverse(DLLNode head) {
            if (head == null) return null;

            DLLNode current = head;
            DLLNode newHead = null;

            while (current != null) {
                // Swap prev and next
                DLLNode temp = current.prev;
                current.prev = current.next;
                current.next = temp;

                // This node might be the new head (if it was the old tail)
                newHead = current;

                // Move forward: after swap, original next is now current.prev
                current = current.prev;
            }

            return newHead;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Clean Iterative with Explicit Last Tracking
    // Time: O(n)  |  Space: O(1)
    // Same algorithm, cleanest form with explicit last-node tracking.
    // ============================================================
    static class Best {
        public static DLLNode reverse(DLLNode head) {
            if (head == null) return null;

            DLLNode current = head;
            DLLNode last = null;

            while (current != null) {
                last = current;

                // Swap prev and next pointers
                DLLNode temp = current.prev;
                current.prev = current.next;
                current.next = temp;

                // Advance to the next unprocessed node (original next, now prev)
                current = current.prev;
            }

            return last;  // The last node we visited is the new head
        }
    }

    // Helper: build a DLL from an array
    private static DLLNode buildDLL(int[] values) {
        if (values.length == 0) return null;
        DLLNode head = new DLLNode(values[0]);
        DLLNode current = head;
        for (int i = 1; i < values.length; i++) {
            DLLNode node = new DLLNode(values[i]);
            current.next = node;
            node.prev = current;
            current = node;
        }
        return head;
    }

    // Helper: convert DLL to string for display
    private static String dllToString(DLLNode head) {
        if (head == null) return "null";
        StringBuilder sb = new StringBuilder();
        DLLNode current = head;
        while (current != null) {
            if (sb.length() > 0) sb.append(" <-> ");
            sb.append(current.val);
            current = current.next;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Reverse a Doubly Linked List ===\n");

        // Test 1: Brute Force
        DLLNode h1 = buildDLL(new int[]{1, 2, 3, 4});
        System.out.println("Input:       " + dllToString(h1));
        h1 = BruteForce.reverse(h1);
        System.out.println("Brute Force: " + dllToString(h1));

        // Test 2: Optimal
        DLLNode h2 = buildDLL(new int[]{1, 2, 3, 4});
        h2 = Optimal.reverse(h2);
        System.out.println("Optimal:     " + dllToString(h2));

        // Test 3: Best
        DLLNode h3 = buildDLL(new int[]{1, 2, 3, 4});
        h3 = Best.reverse(h3);
        System.out.println("Best:        " + dllToString(h3));

        // Test 4: Single node
        DLLNode single = buildDLL(new int[]{5});
        single = Best.reverse(single);
        System.out.println("\nSingle node: " + dllToString(single));

        // Test 5: Two nodes
        DLLNode two = buildDLL(new int[]{1, 2});
        two = Best.reverse(two);
        System.out.println("Two nodes:   " + dllToString(two));

        // Test 6: Empty list
        DLLNode empty = Best.reverse(null);
        System.out.println("Empty list:  " + dllToString(empty));
    }
}
