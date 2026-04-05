import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n)  |  Space: O(n)
// Collect all values < x into one list, >= x into another,
// then rebuild a new linked list by combining both.
// ============================================================
class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
}

class BruteForce {
    public static ListNode solve(ListNode head, int x) {
        List<Integer> less    = new ArrayList<>();
        List<Integer> greaterEq = new ArrayList<>();

        ListNode cur = head;
        while (cur != null) {
            if (cur.val < x) less.add(cur.val);
            else greaterEq.add(cur.val);
            cur = cur.next;
        }

        // Rebuild from two lists
        ListNode dummy = new ListNode(0);
        cur = dummy;
        for (int v : less)      { cur.next = new ListNode(v); cur = cur.next; }
        for (int v : greaterEq) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(1)
// Two-pointer (two dummy-headed sublists): one for nodes < x,
// one for nodes >= x. Merge them in a single pass. No extra nodes.
// ============================================================
class Optimal {
    public static ListNode solve(ListNode head, int x) {
        ListNode lessHead    = new ListNode(0);
        ListNode greaterHead = new ListNode(0);
        ListNode less    = lessHead;
        ListNode greater = greaterHead;

        ListNode cur = head;
        while (cur != null) {
            if (cur.val < x) {
                less.next = cur;
                less = less.next;
            } else {
                greater.next = cur;
                greater = greater.next;
            }
            cur = cur.next;
        }
        greater.next = null;          // terminate greater list
        less.next = greaterHead.next; // merge
        return lessHead.next;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(1)
// Same two-dummy approach, slightly cleaner iteration using
// node re-linking (no new node allocation). Identical complexity
// to Optimal — both are the canonical in-place solution.
// ============================================================
class Best {
    public static ListNode solve(ListNode head, int x) {
        ListNode dLess = new ListNode(0), dGreater = new ListNode(0);
        ListNode pLess = dLess, pGreater = dGreater;

        while (head != null) {
            ListNode next = head.next;
            head.next = null; // detach
            if (head.val < x) { pLess.next = head; pLess = pLess.next; }
            else               { pGreater.next = head; pGreater = pGreater.next; }
            head = next;
        }
        pLess.next = dGreater.next;
        return dLess.next;
    }
}

// ---- Helpers ----
class LL {
    static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0), cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }
    static String print(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(", ");
            head = head.next;
        }
        return sb.append("]").toString();
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Partition List ===");

        // Test 1: [1,4,3,2,5,2], x=3  => [1,2,2,4,3,5]
        int x = 3;
        System.out.println("Input: [1,4,3,2,5,2], x=" + x);
        System.out.println("Brute:   " + LL.print(BruteForce.solve(LL.build(1,4,3,2,5,2), x)));
        System.out.println("Optimal: " + LL.print(Optimal.solve(LL.build(1,4,3,2,5,2), x)));
        System.out.println("Best:    " + LL.print(Best.solve(LL.build(1,4,3,2,5,2), x)));
        System.out.println("Expected: [1, 2, 2, 4, 3, 5]");

        // Test 2: [2,1], x=2  => [1,2]
        x = 2;
        System.out.println("\nInput: [2,1], x=" + x);
        System.out.println("Brute:   " + LL.print(BruteForce.solve(LL.build(2,1), x)));
        System.out.println("Optimal: " + LL.print(Optimal.solve(LL.build(2,1), x)));
        System.out.println("Best:    " + LL.print(Best.solve(LL.build(2,1), x)));
        System.out.println("Expected: [1, 2]");

        // Test 3: all less than x
        x = 10;
        System.out.println("\nInput: [1,2,3], x=" + x);
        System.out.println("Best:    " + LL.print(Best.solve(LL.build(1,2,3), x)));
        System.out.println("Expected: [1, 2, 3]");
    }
}
