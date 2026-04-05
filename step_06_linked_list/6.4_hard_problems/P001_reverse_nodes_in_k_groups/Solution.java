import java.util.*;

/**
 * Problem: Reverse Nodes in K Groups
 * Difficulty: HARD | XP: 50
 *
 * Given a linked list, reverse every k nodes as a group.
 * If the remaining nodes are fewer than k, leave them as is.
 *
 * @author DSA_Nova
 */
public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // Helper: build list from array
    static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    // Helper: convert list to string
    static String listToStr(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(", ");
            head = head.next;
        }
        return sb.append("]").toString();
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Collect values, reverse in chunks
    // Time: O(n)  |  Space: O(n)
    // Extract all values into a list, reverse every k-chunk in place,
    // then rebuild the linked list.
    // ============================================================
    public static ListNode bruteForce(ListNode head, int k) {
        List<Integer> vals = new ArrayList<>();
        ListNode cur = head;
        while (cur != null) { vals.add(cur.val); cur = cur.next; }

        for (int i = 0; i + k <= vals.size(); i += k) {
            int l = i, r = i + k - 1;
            while (l < r) {
                int tmp = vals.get(l); vals.set(l, vals.get(r)); vals.set(r, tmp);
                l++; r--;
            }
        }

        ListNode dummy = new ListNode(0);
        cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Iterative In-Place Reversal
    // Time: O(n)  |  Space: O(1)
    // Use a dummy node. For each group of k:
    //   1. Check if k nodes remain.
    //   2. Reverse k nodes in place using standard reversal.
    //   3. Reconnect the reversed group to the previous tail.
    // ============================================================
    public static ListNode optimal(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prevGroupTail = dummy;

        while (true) {
            // Check if at least k nodes remain
            ListNode check = prevGroupTail;
            for (int i = 0; i < k; i++) {
                check = check.next;
                if (check == null) return dummy.next;
            }

            // Reverse k nodes starting from prevGroupTail.next
            ListNode groupHead = prevGroupTail.next;
            ListNode prev = null, curr = groupHead;
            for (int i = 0; i < k; i++) {
                ListNode nxt = curr.next;
                curr.next = prev;
                prev = curr;
                curr = nxt;
            }
            // prev = new head of reversed group
            // curr = first node of next group
            // groupHead = new tail of reversed group
            prevGroupTail.next = prev;
            groupHead.next = curr;
            prevGroupTail = groupHead;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Recursive In-Place Reversal
    // Time: O(n)  |  Space: O(n/k) stack frames
    // Recursively reverse each group of k and connect to the
    // result of recursing on the rest.
    // Cleaner code at the cost of O(n/k) stack space.
    // ============================================================
    public static ListNode best(ListNode head, int k) {
        // Count k nodes
        ListNode cur = head;
        int count = 0;
        while (cur != null && count < k) { cur = cur.next; count++; }
        if (count < k) return head; // fewer than k nodes, no reversal

        // Reverse k nodes
        ListNode prev = null, node = head;
        for (int i = 0; i < k; i++) {
            ListNode nxt = node.next;
            node.next = prev;
            prev = node;
            node = nxt;
        }
        // head is now tail of reversed group; recurse for rest
        head.next = best(node, k);
        return prev; // prev is new head of reversed group
    }

    public static void main(String[] args) {
        System.out.println("=== Reverse Nodes in K Groups ===");

        int[][] arrays = {{1, 2, 3, 4, 5}, {1, 2, 3, 4, 5}, {1, 2, 3, 4, 5}, {1}};
        int[] ks = {2, 3, 1, 1};
        // Expected: [2,1,4,3,5], [3,2,1,4,5], [1,2,3,4,5], [1]

        for (int t = 0; t < arrays.length; t++) {
            ListNode h1 = build(arrays[t]);
            ListNode h2 = build(arrays[t]);
            ListNode h3 = build(arrays[t]);
            System.out.printf("Input=%s k=%d%n", Arrays.toString(arrays[t]), ks[t]);
            System.out.println("  Brute:   " + listToStr(bruteForce(h1, ks[t])));
            System.out.println("  Optimal: " + listToStr(optimal(h2, ks[t])));
            System.out.println("  Best:    " + listToStr(best(h3, ks[t])));
        }
    }
}
