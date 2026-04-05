import java.util.*;

// ============================================================
// ListNode definition (shared by all approaches)
// ============================================================
class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }

    static ListNode of(int... vals) {
        ListNode dummy = new ListNode(0), cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    static String toStr(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        for (ListNode n = head; n != null; n = n.next) {
            sb.append(n.val); if (n.next != null) sb.append("->");
        }
        return sb.append("]").toString();
    }
}

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(n)
// Collect all values, sort them, rebuild the list
// ============================================================
class BruteForce {
    public static ListNode solve(ListNode head) {
        List<Integer> vals = new ArrayList<>();
        for (ListNode n = head; n != null; n = n.next) vals.add(n.val);
        Collections.sort(vals);
        ListNode dummy = new ListNode(0), cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Classic Insertion Sort on linked list
// Time: O(n^2)  |  Space: O(1)
// Maintain a sorted prefix. For each unsorted node, find the correct
// position in the sorted part and insert it there.
// ============================================================
class Optimal {
    public static ListNode solve(ListNode head) {
        ListNode dummy = new ListNode(Integer.MIN_VALUE);
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            // Find insertion point in sorted part
            ListNode prev = dummy;
            while (prev.next != null && prev.next.val < cur.val) prev = prev.next;
            cur.next = prev.next;
            prev.next = cur;
            cur = next;
        }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 3: BEST - Insertion Sort with "last sorted" optimization
// Time: O(n^2) worst, better on nearly-sorted input  |  Space: O(1)
// Keep a pointer to the last sorted node. If current val >= last sorted val,
// no need to scan from dummy — just extend the sorted tail.
// ============================================================
class Best {
    public static ListNode solve(ListNode head) {
        ListNode dummy = new ListNode(Integer.MIN_VALUE);
        ListNode lastSorted = dummy, cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            if (cur.val >= lastSorted.val) {
                // Append directly to the sorted tail
                lastSorted.next = cur;
                lastSorted = lastSorted.next;
            } else {
                // Find position from head
                ListNode prev = dummy;
                while (prev.next != null && prev.next.val < cur.val) prev = prev.next;
                cur.next = prev.next;
                prev.next = cur;
            }
            lastSorted.next = next;
            cur = next;
        }
        return dummy.next;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Insertion Sort List ===");

        int[][] tests = {
            {4, 2, 1, 3},        // expected [1->2->3->4]
            {-1, 5, 3, 4, 0},    // expected [-1->0->3->4->5]
            {1},                  // single element
            {3, 1, 2},           // expected [1->2->3]
        };

        for (int[] vals : tests) {
            ListNode h1 = ListNode.of(vals);
            ListNode h2 = ListNode.of(vals);
            ListNode h3 = ListNode.of(vals);
            System.out.printf("Input: %s => Brute: %s  Optimal: %s  Best: %s%n",
                ListNode.toStr(ListNode.of(vals)),
                ListNode.toStr(BruteForce.solve(h1)),
                ListNode.toStr(Optimal.solve(h2)),
                ListNode.toStr(Best.solve(h3)));
        }
    }
}
