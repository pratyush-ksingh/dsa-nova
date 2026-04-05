import java.util.*;

// ============================================================
// ListNode definition
// ============================================================
class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }

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
// Time: O(n)  |  Space: O(n)
// Extract values at even positions (1-indexed: 2,4,6,...) into an array,
// reverse that array, then write values back.
// ============================================================
class BruteForce {
    public static ListNode solve(ListNode head) {
        // Collect even-position (1-indexed) values
        List<Integer> evenVals = new ArrayList<>();
        int pos = 1;
        for (ListNode n = head; n != null; n = n.next) {
            if (pos % 2 == 0) evenVals.add(n.val);
            pos++;
        }
        Collections.reverse(evenVals);
        // Write back
        int idx = 0; pos = 1;
        for (ListNode n = head; n != null; n = n.next) {
            if (pos % 2 == 0) { n.val = evenVals.get(idx++); }
            pos++;
        }
        return head;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Extract even-position nodes, reverse, re-weave
// Time: O(n)  |  Space: O(n) for the sub-list
// Collect even-indexed nodes, reverse that sub-list in-place,
// then interleave back into the original list.
// ============================================================
class Optimal {
    private static ListNode reverse(ListNode head) {
        ListNode prev = null, cur = head;
        while (cur != null) { ListNode next = cur.next; cur.next = prev; prev = cur; cur = next; }
        return prev;
    }

    public static ListNode solve(ListNode head) {
        // Separate into odd and even positioned nodes (1-indexed)
        ListNode oddDummy  = new ListNode(0), oddCur  = oddDummy;
        ListNode evenDummy = new ListNode(0), evenCur = evenDummy;
        ListNode cur = head;
        int pos = 1;
        while (cur != null) {
            ListNode next = cur.next; cur.next = null;
            if (pos % 2 == 1) { oddCur.next = cur; oddCur = oddCur.next; }
            else              { evenCur.next = cur; evenCur = evenCur.next; }
            cur = next; pos++;
        }
        // Reverse even list
        ListNode evenHead = reverse(evenDummy.next);
        // Interleave odd and (reversed) even
        ListNode dummy = new ListNode(0), res = dummy;
        ListNode o = oddDummy.next, e = evenHead;
        while (o != null || e != null) {
            if (o != null) { res.next = o; res = res.next; o = o.next; }
            if (e != null) { res.next = e; res = res.next; e = e.next; }
        }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 3: BEST - In-place value swap (avoid re-weaving)
// Time: O(n)  |  Space: O(n/2) for even values array
// Collect only even-position values, reverse, write back — clean O(n) pass
// ============================================================
class Best {
    public static ListNode solve(ListNode head) {
        // Count even-position nodes first
        List<Integer> evenVals = new ArrayList<>();
        int pos = 1;
        for (ListNode n = head; n != null; n = n.next) {
            if (pos % 2 == 0) evenVals.add(n.val);
            pos++;
        }
        if (evenVals.isEmpty()) return head;
        Collections.reverse(evenVals);
        int idx = 0; pos = 1;
        for (ListNode n = head; n != null; n = n.next) {
            if (pos % 2 == 0) n.val = evenVals.get(idx++);
            pos++;
        }
        return head;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Even Reverse ===");
        // 1->2->3->4->5  even positions (1-indexed): 2,4  reversed: 4,2  => 1->4->3->2->5
        int[][] tests = {
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4},
            {1},
            {1, 2},
        };
        for (int[] vals : tests) {
            System.out.printf("Input: %s => Brute: %s  Optimal: %s  Best: %s%n",
                ListNode.toStr(ListNode.of(vals)),
                ListNode.toStr(BruteForce.solve(ListNode.of(vals))),
                ListNode.toStr(Optimal.solve(ListNode.of(vals))),
                ListNode.toStr(Best.solve(ListNode.of(vals))));
        }
    }
}
