/**
 * Problem: Odd Even Linked List (LeetCode 328)
 * Difficulty: MEDIUM | XP: 25
 *
 * Group all nodes at odd indices (1-based) together followed by nodes
 * at even indices. Must be O(n) time, O(1) extra space.
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
    // APPROACH 1: BRUTE FORCE
    // Time: O(n)  |  Space: O(n)
    // Collect odd-indexed and even-indexed values into two int arrays,
    // then rebuild the linked list by concatenating them.
    // ============================================================

    /**
     * Single pass to collect values at 1-based odd/even positions
     * into two separate ArrayLists. Rebuild a new list as:
     *   odd values ... even values ...
     */
    public static ListNode bruteForce(ListNode head) {
        if (head == null) return null;

        java.util.List<Integer> odd  = new java.util.ArrayList<>();
        java.util.List<Integer> even = new java.util.ArrayList<>();

        ListNode cur = head;
        int idx = 1;
        while (cur != null) {
            if (idx % 2 == 1) odd.add(cur.val);
            else              even.add(cur.val);
            cur = cur.next;
            idx++;
        }

        ListNode dummy = new ListNode(0);
        cur = dummy;
        for (int v : odd)  { cur.next = new ListNode(v); cur = cur.next; }
        for (int v : even) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (two-chain pointer re-linking)
    // Time: O(n)  |  Space: O(1)
    // Maintain odd and even chains, rewire .next pointers in-place.
    // Attach even chain to end of odd chain.
    // ============================================================

    /**
     * Two tails 'odd' and 'even' walk through the list together.
     * In each iteration:
     *   odd.next  = even.next   (odd tail skips the even node)
     *   even.next = odd.next    (even tail skips the next odd node)
     * After the loop, attach even_head after odd tail.
     */
    public static ListNode optimal(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode odd      = head;
        ListNode even     = head.next;
        ListNode evenHead = even;

        while (even != null && even.next != null) {
            odd.next  = even.next;
            odd       = odd.next;
            even.next = odd.next;
            even      = even.next;
        }
        odd.next = evenHead;
        return head;
    }

    // ============================================================
    // APPROACH 3: BEST  (same algorithm, explicit named pointers)
    // Time: O(n)  |  Space: O(1)
    // Identical to Approach 2 but written with more descriptive
    // variable names to make whiteboard explanation easier.
    // ============================================================

    /**
     * Same two-chain re-linking as optimal(), but each pointer
     * update is given its own named variable step for clarity.
     */
    public static ListNode best(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode oddTail  = head;
        ListNode evenHead = head.next;
        ListNode evenTail = evenHead;

        while (evenTail != null && evenTail.next != null) {
            ListNode nextOdd  = evenTail.next;
            oddTail.next  = nextOdd;
            oddTail       = nextOdd;

            ListNode nextEven = oddTail.next;
            evenTail.next = nextEven;
            if (nextEven == null) break;
            evenTail = nextEven;
        }
        oddTail.next = evenHead;
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
        System.out.println("=== Odd Even Linked List ===\n");

        int[][][] tests = {
            {{1, 2, 3, 4, 5},       {1, 3, 5, 2, 4}},
            {{2, 1, 3, 5, 6, 4, 7}, {2, 3, 6, 7, 1, 5, 4}},
            {{1},                    {1}},
            {{1, 2},                 {1, 2}},
            {{1, 2, 3},              {1, 3, 2}},
        };

        for (int[][] tc : tests) {
            int[] input    = tc[0];
            int[] expected = tc[1];

            String b   = listToString(bruteForce(buildList(input)));
            String o   = listToString(optimal(buildList(input)));
            String bst = listToString(best(buildList(input)));
            String exp = java.util.Arrays.toString(expected).replace(", ", ", ");

            // normalise expected to same format
            StringBuilder expSb = new StringBuilder("[");
            for (int i = 0; i < expected.length; i++) {
                if (i > 0) expSb.append(", ");
                expSb.append(expected[i]);
            }
            expSb.append("]");
            String expStr = expSb.toString();

            String status = (b.equals(expStr) && o.equals(expStr) && bst.equals(expStr))
                            ? "PASS" : "FAIL";
            System.out.printf("[%s] Input:   %s%n", status,
                              java.util.Arrays.toString(input));
            System.out.printf("       Brute:   %s%n", b);
            System.out.printf("       Optimal: %s%n", o);
            System.out.printf("       Best:    %s%n", bst);
            System.out.printf("       Expect:  %s%n%n", expStr);
        }
    }
}
