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
// Collect all values, swap adjacent pairs in the array, rebuild list.
// ============================================================
class BruteForce {
    public static ListNode solve(ListNode head) {
        List<Integer> vals = new ArrayList<>();
        for (ListNode n = head; n != null; n = n.next) vals.add(n.val);
        for (int i = 0; i + 1 < vals.size(); i += 2) {
            int tmp = vals.get(i); vals.set(i, vals.get(i + 1)); vals.set(i + 1, tmp);
        }
        ListNode dummy = new ListNode(0), cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Iterative pointer manipulation
// Time: O(n)  |  Space: O(1)
// Use a dummy head and process pairs: for each pair (first, second),
// rewire pointers so second comes before first.
// ============================================================
class Optimal {
    public static ListNode solve(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        while (prev.next != null && prev.next.next != null) {
            ListNode first  = prev.next;
            ListNode second = prev.next.next;
            // Swap: prev -> second -> first -> rest
            first.next  = second.next;
            second.next = first;
            prev.next   = second;
            prev = first; // move prev to the end of the swapped pair
        }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 3: BEST - Recursive approach
// Time: O(n)  |  Space: O(n/2) recursion stack
// Recursively swap each pair; base case is 0 or 1 nodes.
// ============================================================
class Best {
    public static ListNode solve(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode first  = head;
        ListNode second = head.next;
        // Recurse for the rest
        first.next  = solve(second.next);
        second.next = first;
        return second;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Swap Nodes in Pairs ===");

        int[][] tests = {
            {1, 2, 3, 4},    // expected [2->1->4->3]
            {1},             // expected [1]
            {},              // expected []
            {1, 2, 3},       // expected [2->1->3]
        };

        for (int[] vals : tests) {
            String input = ListNode.toStr(ListNode.of(vals));
            System.out.printf("Input: %s => Brute: %s  Optimal: %s  Best: %s%n",
                input,
                ListNode.toStr(BruteForce.solve(ListNode.of(vals))),
                ListNode.toStr(Optimal.solve(ListNode.of(vals))),
                ListNode.toStr(Best.solve(ListNode.of(vals))));
        }
    }
}
