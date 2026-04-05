/**
 * Problem: Intro to Linked List
 * Difficulty: EASY | XP: 10
 *
 * Create a linked list from an array, traverse and print.
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
    // APPROACH 1: ITERATIVE BUILD FROM ARRAY
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Build a linked list from an array iteratively.
     * Walk through the array, creating a node for each element
     * and linking it to the growing chain.
     */
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
    // APPROACH 2: RECURSIVE BUILD FROM ARRAY
    // Time: O(n)  |  Space: O(n) + O(n) call stack
    // ============================================================
    /**
     * Build a linked list recursively.
     * Create current node, set its next to the result of building the rest.
     */
    public static ListNode buildRecursive(int[] arr, int index) {
        if (index >= arr.length) return null;

        ListNode node = new ListNode(arr[index]);
        node.next = buildRecursive(arr, index + 1);
        return node;
    }

    // ============================================================
    // APPROACH 3: HEAD INSERTION (builds reversed list)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Build by inserting each element at the head.
     * Traverse array right-to-left to preserve original order.
     */
    public static ListNode buildByHeadInsertion(int[] arr) {
        if (arr == null || arr.length == 0) return null;

        ListNode head = null;
        for (int i = arr.length - 1; i >= 0; i--) {
            ListNode node = new ListNode(arr[i]);
            node.next = head;
            head = node;
        }
        return head;
    }

    // ============================================================
    // TRAVERSAL: Print linked list
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    public static void printList(ListNode head) {
        ListNode current = head;
        StringBuilder sb = new StringBuilder();
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) sb.append(" -> ");
            current = current.next;
        }
        System.out.println(sb.toString());
    }

    // ============================================================
    // UTILITY: Get length of linked list
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    public static int getLength(ListNode head) {
        int count = 0;
        ListNode current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    // ============================================================
    // UTILITY: Search for a value in linked list
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    public static boolean search(ListNode head, int target) {
        ListNode current = head;
        while (current != null) {
            if (current.val == target) return true;
            current = current.next;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("=== Intro to Linked List ===\n");

        int[] arr = {1, 2, 3, 4, 5};

        // Approach 1: Iterative build
        System.out.println("Approach 1 - Iterative Build:");
        ListNode head1 = buildFromArray(arr);
        printList(head1);
        System.out.println("Length: " + getLength(head1));

        // Approach 2: Recursive build
        System.out.println("\nApproach 2 - Recursive Build:");
        ListNode head2 = buildRecursive(arr, 0);
        printList(head2);

        // Approach 3: Head insertion
        System.out.println("\nApproach 3 - Head Insertion:");
        ListNode head3 = buildByHeadInsertion(arr);
        printList(head3);

        // Search
        System.out.println("\nSearch for 3: " + search(head1, 3));
        System.out.println("Search for 9: " + search(head1, 9));

        // Edge case: empty array
        System.out.println("\nEdge case - Empty array:");
        ListNode empty = buildFromArray(new int[]{});
        printList(empty); // prints nothing

        // Edge case: single element
        System.out.println("Edge case - Single element:");
        ListNode single = buildFromArray(new int[]{42});
        printList(single);
        System.out.println("Length: " + getLength(single));
    }
}
