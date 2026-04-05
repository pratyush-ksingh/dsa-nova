import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n)  |  Space: O(n)
// Convert LL to number string, add 1 using BigInteger,
// convert back to linked list.
// ============================================================
class ListNode1 {
    int val;
    ListNode1 next;
    ListNode1(int val) { this.val = val; }
}

class BruteForce1 {
    public static ListNode1 solve(ListNode1 head) {
        // Build number string from LL
        StringBuilder sb = new StringBuilder();
        ListNode1 cur = head;
        while (cur != null) { sb.append(cur.val); cur = cur.next; }

        // Add 1
        java.math.BigInteger num = new java.math.BigInteger(sb.toString()).add(java.math.BigInteger.ONE);
        String result = num.toString();

        // Rebuild LL
        ListNode1 dummy = new ListNode1(0);
        cur = dummy;
        for (char c : result.toCharArray()) {
            cur.next = new ListNode1(c - '0');
            cur = cur.next;
        }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(1)
// Reverse the list, add 1 with carry, reverse again.
// ============================================================
class Optimal1 {
    private static ListNode1 reverse(ListNode1 head) {
        ListNode1 prev = null, cur = head;
        while (cur != null) {
            ListNode1 next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    public static ListNode1 solve(ListNode1 head) {
        head = reverse(head);
        ListNode1 cur = head;
        int carry = 1;
        ListNode1 prev = null;

        while (cur != null && carry > 0) {
            int sum = cur.val + carry;
            cur.val = sum % 10;
            carry = sum / 10;
            prev = cur;
            cur = cur.next;
        }
        if (carry > 0) {
            prev.next = new ListNode1(carry);
        }
        return reverse(head);
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(n) recursion stack
// Recursive approach: add carry from the end, propagate upward.
// Clean and avoids explicit reversal.
// ============================================================
class Best1 {
    private static int addCarry(ListNode1 node) {
        if (node == null) return 1; // initial carry of 1
        int carry = addCarry(node.next);
        int sum = node.val + carry;
        node.val = sum % 10;
        return sum / 10;
    }

    public static ListNode1 solve(ListNode1 head) {
        int carry = addCarry(head);
        if (carry > 0) {
            ListNode1 newHead = new ListNode1(carry);
            newHead.next = head;
            return newHead;
        }
        return head;
    }
}

// ---- Helpers ----
class LL1 {
    static ListNode1 build(int... vals) {
        ListNode1 dummy = new ListNode1(0), cur = dummy;
        for (int v : vals) { cur.next = new ListNode1(v); cur = cur.next; }
        return dummy.next;
    }
    static String print(ListNode1 head) {
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
        System.out.println("=== Add One to LL Number ===");

        int[][] inputs = {{1,2,3}, {9,9,9}, {0}, {1,0,0,0}};
        String[] expected = {"[1, 2, 4]", "[1, 0, 0, 0]", "[1]", "[1, 0, 0, 1]"};

        for (int i = 0; i < inputs.length; i++) {
            String b    = LL1.print(BruteForce1.solve(LL1.build(inputs[i])));
            String o    = LL1.print(Optimal1.solve(LL1.build(inputs[i])));
            String best = LL1.print(Best1.solve(LL1.build(inputs[i])));
            String status = (b.equals(expected[i]) && o.equals(expected[i]) && best.equals(expected[i])) ? "PASS" : "FAIL";
            System.out.printf("Input: %-15s | Brute: %-15s | Optimal: %-15s | Best: %-15s | Expected: %-15s | %s%n",
                    LL1.print(LL1.build(inputs[i])), b, o, best, expected[i], status);
        }
    }
}
