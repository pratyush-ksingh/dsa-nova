/**
 * Problem: Search in Linked List
 * Difficulty: EASY | XP: 10
 *
 * Given a singly linked list and a target value, determine if the
 * target exists in the list. Return true/false or the index.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ----- Linked List Node Definition -----
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; this.next = null; }
    }

    // Helper: build list from array
    static ListNode buildList(int[] arr) {
        if (arr.length == 0) return null;
        ListNode head = new ListNode(arr[0]);
        ListNode curr = head;
        for (int i = 1; i < arr.length; i++) {
            curr.next = new ListNode(arr[i]);
            curr = curr.next;
        }
        return head;
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Iterative Boolean Search
    // Time: O(n)  |  Space: O(1)
    //
    // Walk from head to tail comparing each node's value to target.
    // Return true the instant a match is found; false after exhausting
    // the entire list.
    // ============================================================
    public static boolean bruteForce(ListNode head, int target) {
        ListNode current = head;
        while (current != null) {
            if (current.val == target) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Search
    // Time: O(n)  |  Space: O(n) due to call stack
    //
    // Recursion mirrors the linked-list structure beautifully.
    // Base case 1: null node -> not found.
    // Base case 2: node.val == target -> found.
    // Recursive case: search the rest of the list.
    // ============================================================
    public static boolean optimal(ListNode node, int target) {
        // Base case: end of list
        if (node == null) return false;
        // Base case: found
        if (node.val == target) return true;
        // Recurse on the remaining list
        return optimal(node.next, target);
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative Search with Index Return
    // Time: O(n)  |  Space: O(1)
    //
    // Same traversal as brute force, but maintains an index counter.
    // Returns the 0-based index of the first occurrence, or -1 if
    // the target is absent. This solves both the boolean and index
    // variants in a single clean pass.
    // ============================================================
    public static int best(ListNode head, int target) {
        ListNode current = head;
        int idx = 0;
        while (current != null) {
            if (current.val == target) {
                return idx;
            }
            current = current.next;
            idx++;
        }
        return -1;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Search in Linked List ===\n");

        ListNode list1 = buildList(new int[]{1, 3, 5, 7});
        ListNode empty = buildList(new int[]{});
        ListNode single = buildList(new int[]{9});

        // Approach 1: Brute Force
        System.out.println("--- Brute Force (boolean) ---");
        System.out.println("[1,3,5,7] target=5 -> " + bruteForce(list1, 5));  // true
        System.out.println("[1,3,5,7] target=4 -> " + bruteForce(list1, 4));  // false
        System.out.println("[]        target=1 -> " + bruteForce(empty, 1));  // false
        System.out.println("[9]       target=9 -> " + bruteForce(single, 9)); // true

        // Approach 2: Optimal (Recursive)
        System.out.println("\n--- Optimal (recursive boolean) ---");
        System.out.println("[1,3,5,7] target=5 -> " + optimal(list1, 5));  // true
        System.out.println("[1,3,5,7] target=4 -> " + optimal(list1, 4));  // false
        System.out.println("[]        target=1 -> " + optimal(empty, 1));  // false
        System.out.println("[9]       target=9 -> " + optimal(single, 9)); // true

        // Approach 3: Best (index return)
        System.out.println("\n--- Best (index return) ---");
        System.out.println("[1,3,5,7] target=5 -> index " + best(list1, 5));  // 2
        System.out.println("[1,3,5,7] target=4 -> index " + best(list1, 4));  // -1
        System.out.println("[]        target=1 -> index " + best(empty, 1));  // -1
        System.out.println("[9]       target=9 -> index " + best(single, 9)); // 0
    }
}
