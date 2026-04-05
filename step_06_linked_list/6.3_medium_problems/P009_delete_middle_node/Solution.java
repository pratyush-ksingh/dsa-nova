/**
 * Problem: Delete the Middle Node of a Linked List (LeetCode 2095)
 * Difficulty: MEDIUM | XP: 25
 *
 * Delete the middle node (index floor(n/2), 0-based) of a linked list.
 * Single-node list: returns null (the only node is deleted).
 *
 * @author DSA_Nova
 */
public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE  (two-pass)
    // Time: O(2n) = O(n)  |  Space: O(1)
    // Pass 1: count length, compute mid = length / 2.
    // Pass 2: walk to predecessor of mid and unlink.
    // ============================================================

    /**
     * Two-pass approach: first count nodes, then walk to the node
     * just before the middle (index mid - 1 from dummy = index mid - 1
     * from 0). A dummy head before 'head' handles the edge case where
     * n == 1 (middle IS head; dummy.next becomes null cleanly).
     */
    public static ListNode bruteForce(ListNode head) {
        if (head == null || head.next == null) return null;

        // Pass 1: count
        int length = 0;
        ListNode cur = head;
        while (cur != null) { length++; cur = cur.next; }

        int mid = length / 2;   // 0-based index to delete

        // Pass 2: walk to node at index (mid - 1) from head
        // Use dummy so index 0 from dummy == head
        ListNode dummy = new ListNode(0, head);
        cur = dummy;
        for (int i = 0; i < mid; i++) cur = cur.next;
        cur.next = cur.next.next;

        return dummy.next;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (slow/fast + prev pointer)
    // Time: O(n)  |  Space: O(1)
    // slow advances 1 step, fast advances 2 steps.
    // When fast exits, slow is at the middle. prev tracks slow-1.
    // ============================================================

    /**
     * Classic slow/fast pointer to locate the middle in one pass.
     * A 'prev' pointer trails one step behind 'slow'.
     * When the loop ends, slow == middle, so we do:
     *   prev.next = slow.next
     * A dummy head lets prev start before head, handling
     * the n == 1 case where slow never moves (fast exits immediately).
     */
    public static ListNode optimal(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode dummy = new ListNode(0, head);
        ListNode prev  = dummy;
        ListNode slow  = head;
        ListNode fast  = head;

        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        prev.next = slow.next;
        return dummy.next;
    }

    // ============================================================
    // APPROACH 3: BEST  (fast starts one node ahead — no prev needed)
    // Time: O(n)  |  Space: O(1)
    // Start fast at head.next so that when fast can no longer
    // advance two steps, slow is already AT the predecessor of middle.
    // Eliminates the prev variable entirely.
    // ============================================================

    /**
     * Modified slow/fast: fast starts at head.next (one ahead).
     * Loop condition: fast.next != null && fast.next.next != null.
     * When loop exits, slow.next is the middle node.
     * slow.next = slow.next.next unlinks it.
     *
     * This is the cleanest pattern for interviews because it avoids
     * any auxiliary variable and has an obvious loop invariant.
     */
    public static ListNode best(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode slow = head;
        ListNode fast = head.next;   // one ahead

        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        slow.next = slow.next.next;
        return head;
    }

    // ============================================================
    // Helpers
    // ============================================================
    static ListNode buildList(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    static String listToString(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(", ");
            head = head.next;
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Delete Middle Node ===\n");

        // {input[], expected[]}
        Object[][] tests = {
            {new int[]{1,3,4,7,1,2,6}, new int[]{1,3,4,1,2,6}},  // n=7, mid=3 (val 7)
            {new int[]{1,2,3,4},       new int[]{1,2,4}},          // n=4, mid=2 (val 3)
            {new int[]{2,1},           new int[]{2}},               // n=2, mid=1 (val 1)
            {new int[]{1},             new int[]{}},                // n=1, only node
            {new int[]{1,2,3},         new int[]{1,3}},             // n=3, mid=1 (val 2)
        };

        for (Object[] tc : tests) {
            int[] input    = (int[]) tc[0];
            int[] expected = (int[]) tc[1];

            String b   = listToString(bruteForce(buildList(input)));
            String o   = listToString(optimal(buildList(input)));
            String bst = listToString(best(buildList(input)));

            StringBuilder expSb = new StringBuilder("[");
            for (int i = 0; i < expected.length; i++) {
                if (i > 0) expSb.append(", ");
                expSb.append(expected[i]);
            }
            String expStr = expSb.append("]").toString();

            String status = (b.equals(expStr) && o.equals(expStr) && bst.equals(expStr))
                            ? "PASS" : "FAIL";
            System.out.printf("[%s] Input: %s%n", status,
                              java.util.Arrays.toString(input));
            System.out.printf("       Brute:   %s%n", b);
            System.out.printf("       Optimal: %s%n", o);
            System.out.printf("       Best:    %s%n", bst);
            System.out.printf("       Expect:  %s%n%n", expStr);
        }
    }
}
