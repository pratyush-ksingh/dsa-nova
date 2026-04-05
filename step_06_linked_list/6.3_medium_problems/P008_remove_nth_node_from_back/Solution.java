/**
 * Problem: Remove Nth Node From End of List (LeetCode 19)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given the head of a linked list, remove the n-th node from the end
 * and return the modified head. n is always valid (1 <= n <= length).
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
    // Pass 1: measure total length L.
    // Pass 2: walk to the (L - n)-th node and unlink next.
    // ============================================================

    /**
     * Count the list length, then walk to the predecessor of the
     * node to remove (position L - n in 0-indexed terms from dummy).
     * A dummy head before 'head' ensures we can remove the first node
     * without special-casing (happens when n == L).
     */
    public static ListNode bruteForce(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);

        // Pass 1
        int length = 0;
        ListNode cur = head;
        while (cur != null) { length++; cur = cur.next; }

        // Pass 2: walk to (length - n)-th node (0-indexed from dummy)
        cur = dummy;
        for (int i = 0; i < length - n; i++) cur = cur.next;
        cur.next = cur.next.next;

        return dummy.next;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (two pointers, single pass)
    // Time: O(n)  |  Space: O(1)
    // Move 'fast' n+1 steps ahead of 'slow' (both start at dummy).
    // Advance both until fast is null; slow is then the predecessor.
    // ============================================================

    /**
     * Two-pointer with n+1 initial gap from dummy:
     *   fast is n+1 steps ahead of slow initially.
     *   When fast becomes null, slow.next is the target node.
     * The +1 ensures slow stops at the predecessor, not the target.
     */
    public static ListNode optimal(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode fast  = dummy;
        ListNode slow  = dummy;

        for (int i = 0; i < n + 1; i++) fast = fast.next;

        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;
        return dummy.next;
    }

    // ============================================================
    // APPROACH 3: BEST  (two pointers, explicit n-step gap)
    // Time: O(n)  |  Space: O(1)
    // Same idea but create a gap of exactly n (not n+1) so that
    // when right reaches the last node, left is at the predecessor.
    // ============================================================

    /**
     * Most common interview variant:
     * - Create n-step gap between left and right (both from dummy).
     * - Advance both until right.next is null.
     * - left.next is the node to delete.
     *
     * Why dummy works for head deletion: when n == length, right ends
     * at the last original node and left stays at dummy.
     * left.next = left.next.next effectively removes head.
     */
    public static ListNode best(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode left  = dummy;
        ListNode right = dummy;

        // Advance right n steps so gap = n
        for (int i = 0; i < n; i++) right = right.next;

        while (right.next != null) {
            left  = left.next;
            right = right.next;
        }
        left.next = left.next.next;
        return dummy.next;
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
        System.out.println("=== Remove Nth Node From End of List ===\n");

        Object[][] tests = {
            {new int[]{1,2,3,4,5}, 2, new int[]{1,2,3,5}},
            {new int[]{1},         1, new int[]{}},
            {new int[]{1,2},       1, new int[]{1}},
            {new int[]{1,2},       2, new int[]{2}},
            {new int[]{1,2,3},     3, new int[]{2,3}},
        };

        for (Object[] tc : tests) {
            int[] input    = (int[]) tc[0];
            int   n        = (int)   tc[1];
            int[] expected = (int[]) tc[2];

            String b   = listToString(bruteForce(buildList(input), n));
            String o   = listToString(optimal(buildList(input), n));
            String bst = listToString(best(buildList(input), n));

            StringBuilder expSb = new StringBuilder("[");
            for (int i = 0; i < expected.length; i++) {
                if (i > 0) expSb.append(", ");
                expSb.append(expected[i]);
            }
            String expStr = expSb.append("]").toString();

            String status = (b.equals(expStr) && o.equals(expStr) && bst.equals(expStr))
                            ? "PASS" : "FAIL";
            System.out.printf("[%s] Input: %s, n=%d%n", status,
                              java.util.Arrays.toString(input), n);
            System.out.printf("       Brute:   %s%n", b);
            System.out.printf("       Optimal: %s%n", o);
            System.out.printf("       Best:    %s%n", bst);
            System.out.printf("       Expect:  %s%n%n", expStr);
        }
    }
}
