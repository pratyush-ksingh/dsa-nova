/**
 * Problem: Deleting a Node in Singly Linked List
 * Difficulty: EASY | XP: 10
 *
 * Delete node at beginning, end, and given position in singly linked list.
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
    // DELETE FROM BEGINNING
    // Time: O(1)  |  Space: O(1)
    // ============================================================
    /**
     * Remove the head node and return the new head.
     */
    public static ListNode deleteFromBeginning(ListNode head) {
        if (head == null) return null;
        ListNode newHead = head.next;
        head.next = null; // help GC
        return newHead;
    }

    // ============================================================
    // DELETE FROM END
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Remove the last node. Traverse to second-to-last, set its next to null.
     */
    public static ListNode deleteFromEnd(ListNode head) {
        if (head == null) return null;

        // Single node -- list becomes empty
        if (head.next == null) return null;

        // Traverse to second-to-last node
        ListNode current = head;
        while (current.next.next != null) {
            current = current.next;
        }

        current.next = null; // detach last node
        return head;
    }

    // ============================================================
    // DELETE AT GIVEN POSITION (1-indexed)
    // Time: O(k)  |  Space: O(1)
    // ============================================================
    /**
     * Delete the node at position k (1-indexed).
     * Position 1 = head, position n = tail.
     */
    public static ListNode deleteAtPosition(ListNode head, int position) {
        if (head == null || position < 1) return head;

        // Position 1 = delete head
        if (position == 1) {
            ListNode newHead = head.next;
            head.next = null;
            return newHead;
        }

        // Traverse to the node just before position k
        ListNode prev = head;
        for (int i = 1; i < position - 1 && prev.next != null; i++) {
            prev = prev.next;
        }

        // If position is out of bounds, prev.next is null
        if (prev.next == null) {
            System.out.println("Position " + position + " out of bounds.");
            return head;
        }

        // Skip the target node
        ListNode target = prev.next;
        prev.next = target.next;
        target.next = null; // help GC

        return head;
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
        System.out.println("=== Deleting a Node in LL ===\n");

        // Delete from beginning
        System.out.println("Delete from Beginning:");
        ListNode head1 = buildFromArray(new int[]{10, 20, 30, 40});
        System.out.print("Before: "); printList(head1);
        head1 = deleteFromBeginning(head1);
        System.out.print("After:  "); printList(head1); // 20 -> 30 -> 40

        // Delete from end
        System.out.println("\nDelete from End:");
        ListNode head2 = buildFromArray(new int[]{10, 20, 30, 40});
        System.out.print("Before: "); printList(head2);
        head2 = deleteFromEnd(head2);
        System.out.print("After:  "); printList(head2); // 10 -> 20 -> 30

        // Delete at position 3
        System.out.println("\nDelete at Position 3:");
        ListNode head3 = buildFromArray(new int[]{10, 20, 30, 40, 50});
        System.out.print("Before: "); printList(head3);
        head3 = deleteAtPosition(head3, 3);
        System.out.print("After:  "); printList(head3); // 10 -> 20 -> 40 -> 50

        // Edge: delete head via position
        System.out.println("\nDelete at Position 1 (head):");
        ListNode head4 = buildFromArray(new int[]{10, 20, 30});
        head4 = deleteAtPosition(head4, 1);
        System.out.print("After:  "); printList(head4); // 20 -> 30

        // Edge: single node
        System.out.println("\nDelete from single-node list:");
        ListNode head5 = buildFromArray(new int[]{99});
        head5 = deleteFromBeginning(head5);
        System.out.print("After:  "); printList(head5); // (empty)

        // Edge: empty list
        System.out.println("\nDelete from empty list:");
        ListNode head6 = deleteFromBeginning(null);
        System.out.print("After:  "); printList(head6); // (empty)

        // Edge: position out of bounds
        System.out.println("\nDelete at Position 10 (out of bounds):");
        ListNode head7 = buildFromArray(new int[]{10, 20, 30});
        head7 = deleteAtPosition(head7, 10);
        System.out.print("After:  "); printList(head7); // 10 -> 20 -> 30
    }
}
