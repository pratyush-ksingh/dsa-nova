/**
 * Problem: Find Length of Linked List
 * Difficulty: EASY | XP: 10
 *
 * Given the head of a singly linked list, find the total number of nodes.
 *
 * @author DSA_Nova
 */
public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Iterative Traversal
    // Time: O(n)  |  Space: O(1)
    // Walk through the list, counting each node until null.
    // ============================================================
    static class BruteForce {
        public static int length(ListNode head) {
            int count = 0;
            ListNode current = head;

            while (current != null) {
                count++;
                current = current.next;
            }
            return count;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Length
    // Time: O(n)  |  Space: O(n) due to recursion stack
    // length(node) = 0 if node is null, else 1 + length(node.next)
    // ============================================================
    static class Optimal {
        public static int length(ListNode node) {
            if (node == null) return 0;
            return 1 + length(node.next);
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Tail-Recursive Style (Iterative in Java)
    // Time: O(n)  |  Space: O(1)
    // Clean iterative version -- production-ready.
    // Java has no TCO, so we use iterative for O(1) space.
    // ============================================================
    static class Best {
        public static int length(ListNode head) {
            int count = 0;
            for (ListNode curr = head; curr != null; curr = curr.next) {
                count++;
            }
            return count;
        }
    }

    // Helper: build a linked list from an array
    private static ListNode buildList(int[] values) {
        if (values.length == 0) return null;
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
        }
        return head;
    }

    public static void main(String[] args) {
        System.out.println("=== Find Length of Linked List ===\n");

        int[][] testCases = {
            {1, 2, 3, 4, 5},  // 5 nodes
            {10, 20},          // 2 nodes
            {7},               // 1 node
            {}                 // empty list
        };

        for (int[] tc : testCases) {
            ListNode head = buildList(tc);
            String label = tc.length == 0 ? "null" : java.util.Arrays.toString(tc);
            System.out.printf("Input: %s%n", label);
            System.out.printf("  Brute Force: %d%n", BruteForce.length(head));
            System.out.printf("  Optimal:     %d%n", Optimal.length(head));
            System.out.printf("  Best:        %d%n", Best.length(head));
            System.out.println();
        }
    }
}
