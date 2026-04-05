/**
 * Problem: Middle of Linked List (LeetCode #876)
 * Difficulty: EASY | XP: 10
 *
 * Given the head of a singly linked list, return the middle node.
 * If two middle nodes, return the second one.
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
    // APPROACH 1: BRUTE FORCE - Count then traverse
    // Time: O(n)  |  Space: O(1)
    // Two passes: first count, then walk to middle.
    // ============================================================
    public static ListNode middleBrute(ListNode head) {
        // Pass 1: Count nodes
        int count = 0;
        ListNode current = head;
        while (current != null) {
            count++;
            current = current.next;
        }

        // Pass 2: Walk to middle (count / 2 steps from head)
        current = head;
        for (int i = 0; i < count / 2; i++) {
            current = current.next;
        }

        return current;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Slow and Fast Pointers (Tortoise & Hare)
    // Time: O(n)  |  Space: O(1)
    // One pass: slow moves 1 step, fast moves 2 steps.
    // When fast reaches end, slow is at middle.
    // ============================================================
    public static ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        // fast != null checks for even-length (fast goes past end)
        // fast.next != null checks for odd-length (fast lands on last node)
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    // ============================================================
    // APPROACH 3: BEST - Same as Optimal
    // The two-pointer approach IS optimal: O(n) time, O(1) space,
    // single pass. Cannot do better since every node must be visited.
    // ============================================================

    // ============================================================
    // HELPER: Build list from array
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

    public static void printFromNode(ListNode node) {
        StringBuilder sb = new StringBuilder();
        while (node != null) {
            sb.append(node.val);
            if (node.next != null) sb.append(" -> ");
            node = node.next;
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        System.out.println("=== Middle of Linked List ===\n");

        // Test 1: Odd length [1,2,3,4,5] -> middle is 3
        ListNode head1 = buildList(new int[]{1, 2, 3, 4, 5});
        System.out.println("List: 1 -> 2 -> 3 -> 4 -> 5");
        System.out.println("Brute force middle: " + middleBrute(head1).val);  // 3
        System.out.println("Two-pointer middle: " + middleNode(head1).val);   // 3
        System.out.print("From middle: ");
        printFromNode(middleNode(head1));

        // Test 2: Even length [1,2,3,4,5,6] -> second middle is 4
        System.out.println();
        ListNode head2 = buildList(new int[]{1, 2, 3, 4, 5, 6});
        System.out.println("List: 1 -> 2 -> 3 -> 4 -> 5 -> 6");
        System.out.println("Brute force middle: " + middleBrute(head2).val);  // 4
        System.out.println("Two-pointer middle: " + middleNode(head2).val);   // 4

        // Test 3: Single element [1] -> middle is 1
        System.out.println();
        ListNode head3 = buildList(new int[]{1});
        System.out.println("List: 1");
        System.out.println("Middle: " + middleNode(head3).val);  // 1

        // Test 4: Two elements [1,2] -> second middle is 2
        System.out.println();
        ListNode head4 = buildList(new int[]{1, 2});
        System.out.println("List: 1 -> 2");
        System.out.println("Middle: " + middleNode(head4).val);  // 2
    }
}
