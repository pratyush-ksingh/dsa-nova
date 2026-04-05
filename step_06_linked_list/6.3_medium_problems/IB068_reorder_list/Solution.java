/**
 * Problem: Reorder List
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a linked list L0 -> L1 -> ... -> Ln-1 -> Ln,
 * reorder it to: L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 -> ...
 * Modify in-place. Do not change node values.
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
    // APPROACH 1: BRUTE FORCE  (ArrayList + rebuild)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Store all nodes in an ArrayList, then use two pointers from both ends
     * to interleave and relink.
     * Real-life: Interleaving two data streams into a single combined feed.
     */
    public static void bruteForce(ListNode head) {
        if (head == null || head.next == null) return;
        List<ListNode> nodes = new ArrayList<>();
        for (ListNode cur = head; cur != null; cur = cur.next) nodes.add(cur);

        int lo = 0, hi = nodes.size() - 1;
        while (lo < hi) {
            nodes.get(lo).next = nodes.get(hi);
            lo++;
            if (lo == hi) break;
            nodes.get(hi).next = nodes.get(lo);
            hi--;
        }
        nodes.get(lo).next = null; // terminate the list
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Find middle + reverse second half + merge)
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Three steps:
     * 1. Find the middle of the list (slow/fast pointers)
     * 2. Reverse the second half
     * 3. Merge first half and reversed second half
     * Real-life: In-place list restructuring in memory-constrained systems.
     */
    public static void optimal(ListNode head) {
        if (head == null || head.next == null) return;

        // Step 1: Find middle
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Step 2: Reverse second half
        ListNode secondHalf = reverse(slow.next);
        slow.next = null; // cut off first half

        // Step 3: Merge
        ListNode first = head, second = secondHalf;
        while (second != null) {
            ListNode f1 = first.next;
            ListNode s1 = second.next;
            first.next = second;
            second.next = f1;
            first = f1;
            second = s1;
        }
    }

    private static ListNode reverse(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode nxt = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nxt;
        }
        return prev;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Same as optimal — O(n)/O(1) is the best achievable for in-place reordering.
     * This version uses a slightly cleaner merge loop.
     * Real-life: Media playlist shuffle that alternates first and last items.
     */
    public static void best(ListNode head) {
        optimal(head); // same algorithm, already optimal
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

    private static ListNode clone(ListNode head) {
        ListNode dummy = new ListNode(-1), cur = dummy;
        while (head != null) { cur.next = new ListNode(head.val); cur = cur.next; head = head.next; }
        return dummy.next;
    }

    public static void main(String[] args) {
        System.out.println("=== Reorder List ===");

        int[][] inputs = {{1,2,3,4}, {1,2,3,4,5}, {1}, {1,2}};
        String[] expected = {
            "[1 -> 4 -> 2 -> 3]",
            "[1 -> 5 -> 2 -> 4 -> 3]",
            "[1]",
            "[1 -> 2]"
        };

        for (int t = 0; t < inputs.length; t++) {
            System.out.println("\nInput: " + Arrays.toString(inputs[t]) + "  =>  expected: " + expected[t]);

            ListNode h1 = build(inputs[t]);
            bruteForce(h1);
            System.out.println("Brute:   " + toStr(h1));

            ListNode h2 = build(inputs[t]);
            optimal(h2);
            System.out.println("Optimal: " + toStr(h2));

            ListNode h3 = build(inputs[t]);
            best(h3);
            System.out.println("Best:    " + toStr(h3));
        }
    }
}
