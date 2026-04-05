/**
 * Problem: Inserting a Node in Linked List
 * Difficulty: EASY | XP: 10
 *
 * Insert a node at beginning, end, and at a given position in a singly linked list.
 *
 * @author DSA_Nova
 */
public class Solution {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    // ============================================================
    // INSERT AT BEGINNING
    // Time: O(1)  |  Space: O(1)
    // Create node, point it to old head, return as new head.
    // ============================================================
    public static ListNode insertAtBeginning(ListNode head, int val) {
        ListNode newNode = new ListNode(val);
        newNode.next = head;
        return newNode;  // new head
    }

    // ============================================================
    // INSERT AT END
    // Time: O(n)  |  Space: O(1)
    // Traverse to last node, attach new node.
    // ============================================================
    public static ListNode insertAtEnd(ListNode head, int val) {
        ListNode newNode = new ListNode(val);

        if (head == null) return newNode;

        ListNode current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;

        return head;
    }

    // ============================================================
    // INSERT AT POSITION (1-indexed)
    // Time: O(k)  |  Space: O(1)
    // Walk to position k-1, insert after it.
    // ============================================================
    public static ListNode insertAtPosition(ListNode head, int val, int position) {
        // Position 1 means insert at head
        if (position <= 1) {
            return insertAtBeginning(head, val);
        }

        ListNode current = head;
        // Walk to the node at position (position - 1)
        for (int i = 1; i < position - 1 && current != null; i++) {
            current = current.next;
        }

        // If position is beyond list length, insert at end
        if (current == null) return head;

        // Insert after 'current'
        ListNode newNode = new ListNode(val);
        newNode.next = current.next;  // IMPORTANT: set newNode.next FIRST
        current.next = newNode;

        return head;
    }

    // ============================================================
    // UNIFIED INSERT WITH SENTINEL (Approach 2)
    // Time: O(k)  |  Space: O(1)
    // Dummy head eliminates special case for position 1.
    // ============================================================
    public static ListNode insertWithSentinel(ListNode head, int val, int position) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode current = dummy;
        // Walk (position - 1) steps from dummy
        for (int i = 0; i < position - 1 && current != null; i++) {
            current = current.next;
        }

        if (current == null) return dummy.next;

        ListNode newNode = new ListNode(val);
        newNode.next = current.next;
        current.next = newNode;

        return dummy.next;
    }

    // ============================================================
    // HELPER: Build list from array and print
    // ============================================================
    public static ListNode buildList(int[] arr) {
        if (arr.length == 0) return null;
        ListNode head = new ListNode(arr[0]);
        ListNode current = head;
        for (int i = 1; i < arr.length; i++) {
            current.next = new ListNode(arr[i]);
            current = current.next;
        }
        return head;
    }

    public static void printList(ListNode head) {
        ListNode current = head;
        StringBuilder sb = new StringBuilder();
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) sb.append(" -> ");
            current = current.next;
        }
        System.out.println(sb.length() == 0 ? "(empty)" : sb.toString());
    }

    public static void main(String[] args) {
        System.out.println("=== Inserting a Node in Linked List ===\n");

        // Build initial list: 1 -> 2 -> 3 -> 4 -> 5
        ListNode head = buildList(new int[]{1, 2, 3, 4, 5});
        System.out.print("Original:        ");
        printList(head);

        // Insert at beginning
        head = insertAtBeginning(head, 0);
        System.out.print("Insert 0 at head:  ");
        printList(head);  // 0 -> 1 -> 2 -> 3 -> 4 -> 5

        // Insert at end
        head = insertAtEnd(head, 6);
        System.out.print("Insert 6 at end:   ");
        printList(head);  // 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6

        // Insert at position 4 (1-indexed)
        head = insertAtPosition(head, 99, 4);
        System.out.print("Insert 99 at pos 4:");
        printList(head);  // 0 -> 1 -> 2 -> 99 -> 3 -> 4 -> 5 -> 6

        // Sentinel approach: insert at position 1 (head)
        System.out.println("\n--- Sentinel Approach ---");
        ListNode head2 = buildList(new int[]{10, 20, 30});
        System.out.print("Original:            ");
        printList(head2);
        head2 = insertWithSentinel(head2, 5, 1);
        System.out.print("Insert 5 at pos 1:   ");
        printList(head2);  // 5 -> 10 -> 20 -> 30
        head2 = insertWithSentinel(head2, 25, 4);
        System.out.print("Insert 25 at pos 4:  ");
        printList(head2);  // 5 -> 10 -> 20 -> 25 -> 30

        // Edge case: insert into empty list
        System.out.println("\n--- Edge Cases ---");
        ListNode empty = null;
        empty = insertAtBeginning(empty, 42);
        System.out.print("Insert into empty (head): ");
        printList(empty);  // 42

        ListNode empty2 = null;
        empty2 = insertAtEnd(empty2, 99);
        System.out.print("Insert into empty (end):  ");
        printList(empty2);  // 99
    }
}
