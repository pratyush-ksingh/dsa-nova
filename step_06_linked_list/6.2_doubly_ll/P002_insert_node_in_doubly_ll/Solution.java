/**
 * Problem: Insert Node in Doubly Linked List
 * Difficulty: EASY | XP: 10
 *
 * Insert at beginning, end, before/after a given node in doubly linked list.
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
    // INSERT AT BEGINNING
    // Time: O(1)  |  Space: O(1)
    // ============================================================
    /**
     * Insert a new node at the beginning of the doubly linked list.
     * The new node becomes the new head.
     */
    public static ListNode insertAtBeginning(ListNode head, int val) {
        ListNode newNode = new ListNode(val);
        newNode.next = head;

        if (head != null) {
            head.prev = newNode;
        }

        return newNode; // new head
    }

    // ============================================================
    // INSERT AT END
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Insert a new node at the end. Traverse to tail, then attach.
     */
    public static ListNode insertAtEnd(ListNode head, int val) {
        ListNode newNode = new ListNode(val);

        if (head == null) {
            return newNode;
        }

        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }

        tail.next = newNode;
        newNode.prev = tail;
        return head;
    }

    // ============================================================
    // INSERT BEFORE A GIVEN NODE
    // Time: O(1)  |  Space: O(1)  (given direct node reference)
    // ============================================================
    /**
     * Insert a new node before the target node.
     * Returns updated head (changes if target was head).
     */
    public static ListNode insertBefore(ListNode head, ListNode target, int val) {
        if (target == null) return head;

        ListNode newNode = new ListNode(val);

        // Wire new node's pointers first (safe -- no references lost)
        newNode.prev = target.prev;
        newNode.next = target;

        // Wire the predecessor to new node
        if (target.prev != null) {
            target.prev.next = newNode;
        } else {
            head = newNode; // target was head
        }

        // Wire target backward to new node
        target.prev = newNode;

        return head;
    }

    // ============================================================
    // INSERT AFTER A GIVEN NODE
    // Time: O(1)  |  Space: O(1)  (given direct node reference)
    // ============================================================
    /**
     * Insert a new node after the target node.
     */
    public static ListNode insertAfter(ListNode head, ListNode target, int val) {
        if (target == null) return head;

        ListNode newNode = new ListNode(val);

        // Wire new node's pointers first
        newNode.next = target.next;
        newNode.prev = target;

        // Wire the successor backward to new node
        if (target.next != null) {
            target.next.prev = newNode;
        }

        // Wire target forward to new node
        target.next = newNode;

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
        System.out.println("=== Insert Node in Doubly LL ===\n");

        // Build: 10 <-> 20 <-> 30
        ListNode head = buildFromArray(new int[]{10, 20, 30});
        System.out.println("Initial list:");
        printForward(head);

        // Insert at beginning
        System.out.println("\nInsert 5 at beginning:");
        head = insertAtBeginning(head, 5);
        printForward(head); // 5 <-> 10 <-> 20 <-> 30

        // Insert at end
        System.out.println("\nInsert 40 at end:");
        head = insertAtEnd(head, 40);
        printForward(head); // 5 <-> 10 <-> 20 <-> 30 <-> 40

        // Insert before node(20)
        System.out.println("\nInsert 15 before node(20):");
        ListNode node20 = findNode(head, 20);
        head = insertBefore(head, node20, 15);
        printForward(head); // 5 <-> 10 <-> 15 <-> 20 <-> 30 <-> 40

        // Insert after node(20)
        System.out.println("\nInsert 25 after node(20):");
        node20 = findNode(head, 20);
        head = insertAfter(head, node20, 25);
        printForward(head); // 5 <-> 10 <-> 15 <-> 20 <-> 25 <-> 30 <-> 40

        // Edge: insert before head
        System.out.println("\nInsert 1 before head:");
        head = insertBefore(head, head, 1);
        printForward(head); // 1 <-> 5 <-> 10 <-> 15 <-> 20 <-> 25 <-> 30 <-> 40

        // Edge: empty list
        System.out.println("\nInsert into empty list:");
        ListNode empty = insertAtBeginning(null, 99);
        printForward(empty); // 99
    }
}
