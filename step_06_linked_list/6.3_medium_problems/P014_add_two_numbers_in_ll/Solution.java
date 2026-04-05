import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n + m)  |  Space: O(n + m)
// Collect digits from both lists, build number strings,
// add as BigIntegers, rebuild LL from result.
// Lists store digits in reverse order (LSB first).
// ============================================================
class ListNode2 {
    int val;
    ListNode2 next;
    ListNode2(int val) { this.val = val; }
}

class BruteForce2 {
    public static ListNode2 solve(ListNode2 l1, ListNode2 l2) {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        ListNode2 cur = l1;
        while (cur != null) { sb1.insert(0, cur.val); cur = cur.next; }
        cur = l2;
        while (cur != null) { sb2.insert(0, cur.val); cur = cur.next; }

        java.math.BigInteger sum = new java.math.BigInteger(sb1.toString())
                .add(new java.math.BigInteger(sb2.toString()));
        String s = sum.toString();

        ListNode2 dummy = new ListNode2(0);
        cur = dummy;
        // Store in reverse (LSB first)
        for (int i = s.length() - 1; i >= 0; i--) {
            cur.next = new ListNode2(s.charAt(i) - '0');
            cur = cur.next;
        }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(max(n, m))  |  Space: O(max(n, m))
// Simultaneous traversal with carry. Both lists are LSB first.
// ============================================================
class Optimal2 {
    public static ListNode2 solve(ListNode2 l1, ListNode2 l2) {
        ListNode2 dummy = new ListNode2(0);
        ListNode2 cur = dummy;
        int carry = 0;

        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;
            if (l1 != null) { sum += l1.val; l1 = l1.next; }
            if (l2 != null) { sum += l2.val; l2 = l2.next; }
            carry = sum / 10;
            cur.next = new ListNode2(sum % 10);
            cur = cur.next;
        }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(max(n, m))  |  Space: O(max(n, m))
// Same as Optimal but with recursive approach for elegance.
// ============================================================
class Best2 {
    public static ListNode2 solve(ListNode2 l1, ListNode2 l2) {
        return addHelper(l1, l2, 0);
    }

    private static ListNode2 addHelper(ListNode2 l1, ListNode2 l2, int carry) {
        if (l1 == null && l2 == null && carry == 0) return null;
        int sum = carry;
        if (l1 != null) { sum += l1.val; l1 = l1.next; }
        if (l2 != null) { sum += l2.val; l2 = l2.next; }
        ListNode2 node = new ListNode2(sum % 10);
        node.next = addHelper(l1, l2, sum / 10);
        return node;
    }
}

// ---- Helpers ----
class LL2 {
    // Build LL from digits array (index 0 = least significant digit)
    static ListNode2 build(int... vals) {
        ListNode2 dummy = new ListNode2(0), cur = dummy;
        for (int v : vals) { cur.next = new ListNode2(v); cur = cur.next; }
        return dummy.next;
    }
    static String print(ListNode2 head) {
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
        System.out.println("=== Add Two Numbers in LL ===");
        // Numbers stored reverse (LSB first)
        // [2,4,3] = 342,  [5,6,4] = 465  => 342+465=807 => [7,0,8]
        System.out.println("342 + 465:");
        System.out.println("Brute:   " + LL2.print(BruteForce2.solve(LL2.build(2,4,3), LL2.build(5,6,4))));
        System.out.println("Optimal: " + LL2.print(Optimal2.solve(LL2.build(2,4,3), LL2.build(5,6,4))));
        System.out.println("Best:    " + LL2.print(Best2.solve(LL2.build(2,4,3), LL2.build(5,6,4))));
        System.out.println("Expected: [7, 0, 8]");

        // 9999999 + 9999 = 10009998 => LSB first: [8,9,9,9,0,0,0,1]
        System.out.println("\n9999999 + 9999:");
        System.out.println("Best:    " + LL2.print(Best2.solve(
                LL2.build(9,9,9,9,9,9,9), LL2.build(9,9,9,9))));
        System.out.println("Expected: [8, 9, 9, 9, 0, 0, 0, 1]");

        // Carry at end: [5] + [5] = 10 => [0,1]
        System.out.println("\n5 + 5:");
        System.out.println("Best:    " + LL2.print(Best2.solve(LL2.build(5), LL2.build(5))));
        System.out.println("Expected: [0, 1]");
    }
}
