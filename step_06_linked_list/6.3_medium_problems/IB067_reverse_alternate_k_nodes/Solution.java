/**
 * Problem: Reverse Alternate K Nodes
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a linked list and integer K:
 *   - Reverse the first K nodes
 *   - Skip the next K nodes (leave them as-is)
 *   - Repeat until end of list
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE  (Collect values, rebuild)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Collect all values, apply the pattern to indices, rebuild the list.
     * Easy to reason about but uses O(n) extra space.
     * Real-life: Alternating audio channel reversal in signal processing pipelines.
     */
    public static ListNode bruteForce(ListNode head, int k) {
        List<Integer> vals = new ArrayList<>();
        for (ListNode cur = head; cur != null; cur = cur.next) vals.add(cur.val);
        int n = vals.size();
        List<Integer> result = new ArrayList<>(Collections.nCopies(n, 0));

        int i = 0;
        while (i < n) {
            // Reverse block of k
            int end = Math.min(i + k, n);
            for (int j = i; j < end; j++) result.set(j, vals.get(i + end - 1 - j));
            i = end;
            // Skip block of k
            for (int j = i; j < Math.min(i + k, n); j++) result.set(j, vals.get(j));
            i = Math.min(i + k, n);
        }

        ListNode dummy = new ListNode(-1);
        ListNode cur = dummy;
        for (int v : result) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (In-place pointer manipulation)
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * In-place: reverse k nodes, then skip k nodes, repeat.
     * Use standard reverse-k-nodes technique with careful pointer linking.
     * Real-life: In-place packet reordering in network routers.
     */
    public static ListNode optimal(ListNode head, int k) {
        if (head == null || k <= 1) return head;
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode prevGroupTail = dummy;
        ListNode curr = head;

        while (curr != null) {
            // --- Phase 1: Reverse k nodes ---
            // Check if at least k nodes exist
            ListNode check = curr;
            for (int i = 0; i < k; i++) {
                if (check == null) return dummy.next;
                check = check.next;
            }
            // Reverse k nodes starting from curr
            ListNode prev = null;
            ListNode node = curr;
            ListNode groupHead = curr; // will become the tail after reversal
            for (int i = 0; i < k && node != null; i++) {
                ListNode nxt = node.next;
                node.next = prev;
                prev = node;
                node = nxt;
            }
            // prev is new head of reversed group
            prevGroupTail.next = prev;
            groupHead.next = node; // connect reversed tail to rest
            prevGroupTail = groupHead;
            curr = node;

            // --- Phase 2: Skip k nodes ---
            for (int i = 0; i < k && curr != null; i++) {
                prevGroupTail = curr;
                curr = curr.next;
            }
        }
        return dummy.next;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Same as optimal — O(n)/O(1) is already the best achievable.
     * Recursive version for clarity and elegance.
     * Real-life: Recursive data stream transformations in functional pipelines.
     */
    public static ListNode best(ListNode head, int k) {
        if (head == null || k <= 1) return head;
        return reverseAlternate(head, k, true);
    }

    private static ListNode reverseAlternate(ListNode head, int k, boolean shouldReverse) {
        if (head == null) return null;
        if (!shouldReverse) {
            // Skip k nodes
            ListNode curr = head;
            for (int i = 1; i < k && curr.next != null; i++) curr = curr.next;
            curr.next = reverseAlternate(curr.next, k, true);
            return head;
        }
        // Reverse k nodes
        // Check if k nodes exist
        ListNode check = head;
        for (int i = 0; i < k; i++) {
            if (check == null) return head; // fewer than k, leave as-is
            check = check.next;
        }
        ListNode prev = null, curr = head;
        ListNode groupTail = head;
        for (int i = 0; i < k && curr != null; i++) {
            ListNode nxt = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nxt;
        }
        groupTail.next = reverseAlternate(curr, k, false);
        return prev;
    }

    // --- Helper utilities ---
    private static ListNode build(int... vals) {
        ListNode dummy = new ListNode(-1), cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private static String toStr(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(" -> ");
            head = head.next;
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Reverse Alternate K Nodes ===");

        int[][] inputs = {{1,2,3,4,5,6,7,8}, {1,2,3,4,5}, {1,2,3,4,5,6}};
        int[] ks = {2, 3, 2};
        String[] expected = {"[2->1->3->4->6->5->7->8]", "[3->2->1->4->5]", "[2->1->3->4->6->5]"};

        for (int t = 0; t < inputs.length; t++) {
            System.out.println("\nInput: " + Arrays.toString(inputs[t]) + "  K=" + ks[t]);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Brute:   " + toStr(bruteForce(build(inputs[t]), ks[t])));
            System.out.println("Optimal: " + toStr(optimal(build(inputs[t]), ks[t])));
            System.out.println("Best:    " + toStr(best(build(inputs[t]), ks[t])));
        }
    }
}
