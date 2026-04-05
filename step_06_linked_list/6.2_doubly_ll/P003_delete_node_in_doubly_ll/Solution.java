/**
 * Problem: Delete Node in Doubly Linked List
 * Difficulty: EASY | XP: 10
 *
 * Delete node at beginning, end, and given position in doubly linked list.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // NODE DEFINITION
    // ============================================================
    static class ListNode {
        int val;
        ListNode prev;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.prev = null;
            this.next = null;
        }
    }

    // ============================================================
    // DELETE FROM BEGINNING
    // Time: O(1)  |  Space: O(1)
    // ============================================================
    /**
     * Remove the head node. Return the new head.
     */
    public static ListNode deleteFromBeginning(ListNode head) {
        if (head == null) return null;

        ListNode newHead = head.next;
        if (newHead != null) {
            newHead.prev = null;
        }
        head.next = null; // help GC
        return newHead;
    }

    // ============================================================
    // DELETE FROM END
    // Time: O(n)  |  Space: O(1)  (O(1) with tail pointer)
    // ============================================================
    /**
     * Remove the last node. Traverse to tail, unlink via tail.prev.
     */
    public static ListNode deleteFromEnd(ListNode head) {
        if (head == null) return null;

        // Single node
        if (head.next == null) return null;

        // Traverse to tail
        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }

        // Unlink tail
        tail.prev.next = null;
        tail.prev = null; // help GC
        return head;
    }

    // ============================================================
    // DELETE AT GIVEN POSITION (1-indexed)
    // Time: O(k)  |  Space: O(1)
    // ============================================================
    /**
     * Delete the node at position k (1-indexed).
     */
    public static ListNode deleteAtPosition(ListNode head, int position) {
        if (head == null || position < 1) return head;

        // Position 1 = delete head
        if (position == 1) {
            return deleteFromBeginning(head);
        }

        // Traverse to the k-th node
        ListNode target = head;
        for (int i = 1; i < position && target != null; i++) {
            target = target.next;
        }

        // Out of bounds
        if (target == null) {
            System.out.println("Position " + position + " out of bounds.");
            return head;
        }

        // Rewire predecessor
        if (target.prev != null) {
            target.prev.next = target.next;
        }

        // Rewire successor
        if (target.next != null) {
            target.next.prev = target.prev;
        }

        // Disconnect target
        target.prev = null;
        target.next = null;

        return head;
    }

    // ============================================================
    // BONUS: DELETE A GIVEN NODE REFERENCE (O(1))
    // Time: O(1)  |  Space: O(1)
    // ============================================================
    /**
     * Delete a node given its reference directly. O(1) operation.
     * Returns the (possibly new) head.
     */
    public static ListNode deleteNode(ListNode head, ListNode target) {
        if (target == null) return head;

        // Rewire predecessor
        if (target.prev != null) {
            target.prev.next = target.next;
        } else {
            // target is the head
            head = target.next;
        }

        // Rewire successor
        if (target.next != null) {
            target.next.prev = target.prev;
        }

        target.prev = null;
        target.next = null;
        return head;
    }

    // ============================================================
    // UTILITY: Build DLL from array
    // ============================================================
    public static ListNode buildFromArray(int[] arr) {
        if (arr == null || arr.length == 0) return null;
        ListNode head = new ListNode(arr[0]);
        ListNode current = head;
        for (int i = 1; i < arr.length; i++) {
            ListNode node = new ListNode(arr[i]);
            current.next = node;
            node.prev = current;
            current = node;
        }
        return head;
    }

    // ============================================================
    // UTILITY: Print DLL forward
    // ============================================================
    public static void printForward(ListNode head) {
        StringBuilder sb = new StringBuilder();
        ListNode current = head;
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) sb.append(" <-> ");
            current = current.next;
        }
        System.out.println(sb.length() > 0 ? sb.toString() : "(empty)");
    }

    // ============================================================
    // UTILITY: Find node by value
    // ============================================================
    public static ListNode findNode(ListNode head, int val) {
        ListNode current = head;
        while (current != null) {
            if (current.val == val) return current;
            current = current.next;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("=== Delete Node in Doubly LL ===\n");

        // Delete from beginning
        System.out.println("Delete from Beginning:");
        ListNode head1 = buildFromArray(new int[]{10, 20, 30, 40});
        System.out.print("Before: "); printForward(head1);
        head1 = deleteFromBeginning(head1);
        System.out.print("After:  "); printForward(head1); // 20 <-> 30 <-> 40

        // Delete from end
        System.out.println("\nDelete from End:");
        ListNode head2 = buildFromArray(new int[]{10, 20, 30, 40});
        System.out.print("Before: "); printForward(head2);
        head2 = deleteFromEnd(head2);
        System.out.print("After:  "); printForward(head2); // 10 <-> 20 <-> 30

        // Delete at position 3
        System.out.println("\nDelete at Position 3:");
        ListNode head3 = buildFromArray(new int[]{10, 20, 30, 40, 50});
        System.out.print("Before: "); printForward(head3);
        head3 = deleteAtPosition(head3, 3);
        System.out.print("After:  "); printForward(head3); // 10 <-> 20 <-> 40 <-> 50

        // Delete a given node reference
        System.out.println("\nDelete node(30) by reference:");
        ListNode head4 = buildFromArray(new int[]{10, 20, 30, 40, 50});
        System.out.print("Before: "); printForward(head4);
        ListNode node30 = findNode(head4, 30);
        head4 = deleteNode(head4, node30);
        System.out.print("After:  "); printForward(head4); // 10 <-> 20 <-> 40 <-> 50

        // Edge: single node
        System.out.println("\nDelete from single-node list:");
        ListNode head5 = buildFromArray(new int[]{99});
        head5 = deleteFromBeginning(head5);
        System.out.print("After:  "); printForward(head5); // (empty)

        // Edge: empty list
        System.out.println("\nDelete from empty list:");
        printForward(deleteFromBeginning(null)); // (empty)

        // Edge: delete head by reference
        System.out.println("\nDelete head by reference:");
        ListNode head6 = buildFromArray(new int[]{10, 20, 30});
        head6 = deleteNode(head6, head6);
        System.out.print("After:  "); printForward(head6); // 20 <-> 30
    }
}
