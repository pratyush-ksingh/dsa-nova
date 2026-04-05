/**
 * Problem: Reverse a Linked List
 * Difficulty: EASY | XP: 10
 * LeetCode #206
 *
 * Reverse a singly linked list iteratively and recursively.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // NODE DEFINITION
    // ============================================================
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- USING ARRAY
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Store values in array, overwrite node values in reverse order.
     * Note: Changes values, not pointers. Works when pointer identity
     * doesn't matter.
     */
    public static ListNode reverseBruteForce(ListNode head) {
        if (head == null) return null;

        // Count nodes
        int count = 0;
        ListNode temp = head;
        while (temp != null) {
            count++;
            temp = temp.next;
        }

        // Store values
        int[] vals = new int[count];
        temp = head;
        for (int i = 0; i < count; i++) {
            vals[i] = temp.val;
            temp = temp.next;
        }

        // Overwrite in reverse
        temp = head;
        for (int i = count - 1; i >= 0; i--) {
            temp.val = vals[i];
            temp = temp.next;
        }

        return head;
    }

    // ============================================================
    // APPROACH 2: ITERATIVE (OPTIMAL)
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Three-pointer technique: prev, curr, next.
     * Reverse each next pointer one by one in a single pass.
     */
    public static ListNode reverseIterative(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode next = curr.next; // save next
            curr.next = prev;          // reverse arrow
            prev = curr;               // advance prev
            curr = next;               // advance curr
        }

        return prev; // prev is the new head
    }

    // ============================================================
    // APPROACH 3: RECURSIVE
    // Time: O(n)  |  Space: O(n) call stack
    // ============================================================
    /**
     * Recursively reverse the rest of the list, then fix the
     * backward link from head.next back to head.
     */
    public static ListNode reverseRecursive(ListNode head) {
        // Base case: empty list or single node
        if (head == null || head.next == null) {
            return head;
        }

        // Recursively reverse everything after head
        ListNode newHead = reverseRecursive(head.next);

        // head.next is now the tail of the reversed sublist
        // Make it point back to head
        head.next.next = head;
        head.next = null; // head is now the tail

        return newHead; // propagate new head up
    }

    // ============================================================
    // UTILITY: Build list from array
    // ============================================================
    public static ListNode buildFromArray(int[] arr) {
        if (arr == null || arr.length == 0) return null;
        ListNode head = new ListNode(arr[0]);
        ListNode current = head;
        for (int i = 1; i < arr.length; i++) {
            current.next = new ListNode(arr[i]);
            current = current.next;
        }
        return head;
    }

    // ============================================================
    // UTILITY: Print list
    // ============================================================
    public static void printList(ListNode head) {
        StringBuilder sb = new StringBuilder();
        ListNode current = head;
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) sb.append(" -> ");
            current = current.next;
        }
        System.out.println(sb.length() > 0 ? sb.toString() : "(empty)");
    }

    public static void main(String[] args) {
        System.out.println("=== Reverse a Linked List ===\n");

        // Approach 1: Brute force (value swap)
        System.out.println("Approach 1 - Brute Force (array):");
        ListNode head1 = buildFromArray(new int[]{1, 2, 3, 4, 5});
        System.out.print("Before: "); printList(head1);
        head1 = reverseBruteForce(head1);
        System.out.print("After:  "); printList(head1);

        // Approach 2: Iterative
        System.out.println("\nApproach 2 - Iterative:");
        ListNode head2 = buildFromArray(new int[]{1, 2, 3, 4, 5});
        System.out.print("Before: "); printList(head2);
        head2 = reverseIterative(head2);
        System.out.print("After:  "); printList(head2);

        // Approach 3: Recursive
        System.out.println("\nApproach 3 - Recursive:");
        ListNode head3 = buildFromArray(new int[]{1, 2, 3, 4, 5});
        System.out.print("Before: "); printList(head3);
        head3 = reverseRecursive(head3);
        System.out.print("After:  "); printList(head3);

        // Edge cases
        System.out.println("\n--- Edge Cases ---");

        System.out.print("Empty list: ");
        printList(reverseIterative(null));

        System.out.print("Single node: ");
        printList(reverseIterative(buildFromArray(new int[]{42})));

        System.out.print("Two nodes: ");
        printList(reverseIterative(buildFromArray(new int[]{1, 2})));
    }
}
