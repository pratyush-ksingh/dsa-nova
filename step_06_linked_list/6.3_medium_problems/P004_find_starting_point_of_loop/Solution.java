/**
 * Problem: Find Starting Point of Loop (LeetCode #142)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- HashSet
    // Time: O(n)  |  Space: O(n)
    //
    // Traverse the list, store visited nodes in a set.
    // The first repeated node is the cycle start.
    // ============================================================
    static class BruteForce {
        public ListNode detectCycle(ListNode head) {
            Set<ListNode> visited = new HashSet<>();
            ListNode curr = head;
            while (curr != null) {
                if (visited.contains(curr)) {
                    return curr;
                }
                visited.add(curr);
                curr = curr.next;
            }
            return null;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Floyd's Cycle Detection
    // Time: O(n)  |  Space: O(1)
    //
    // Phase 1: slow/fast pointers meet inside the cycle.
    // Phase 2: reset one to head, walk both at speed 1.
    // They meet at the cycle start.
    // ============================================================
    static class Optimal {
        public ListNode detectCycle(ListNode head) {
            ListNode slow = head, fast = head;

            // Phase 1: Detect cycle
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) {
                    // Phase 2: Find cycle start
                    slow = head;
                    while (slow != fast) {
                        slow = slow.next;
                        fast = fast.next;
                    }
                    return slow;
                }
            }
            return null;  // No cycle
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Floyd's (Cleanest Implementation)
    // Time: O(n)  |  Space: O(1)
    //
    // Same Floyd's, single method, minimal lines.
    // ============================================================
    static class Best {
        public ListNode detectCycle(ListNode head) {
            for (ListNode slow = head, fast = head;
                 fast != null && fast.next != null; ) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) {
                    for (slow = head; slow != fast;
                         slow = slow.next, fast = fast.next);
                    return slow;
                }
            }
            return null;
        }
    }

    // ---- Helper for testing ----
    static ListNode[] buildWithCycle(int[] vals, int pos) {
        if (vals.length == 0) return new ListNode[]{null, null};
        ListNode[] nodes = new ListNode[vals.length];
        for (int i = 0; i < vals.length; i++) nodes[i] = new ListNode(vals[i]);
        for (int i = 0; i < vals.length - 1; i++) nodes[i].next = nodes[i + 1];
        if (pos >= 0) nodes[vals.length - 1].next = nodes[pos];
        return new ListNode[]{nodes[0], pos >= 0 ? nodes[pos] : null};
    }

    public static void main(String[] args) {
        System.out.println("=== Find Starting Point of Loop ===\n");

        // Test 1: [3,2,0,-4], cycle at index 1
        ListNode[] t1 = buildWithCycle(new int[]{3, 2, 0, -4}, 1);
        System.out.println("Test 1: [3,2,0,-4] cycle at index 1");
        System.out.println("  Expected: " + (t1[1] != null ? t1[1].val : "null"));
        ListNode r1 = new BruteForce().detectCycle(t1[0]);
        System.out.println("  Brute:    " + (r1 != null ? r1.val : "null"));
        // Need fresh list for each approach since brute doesn't modify, but for safety:
        t1 = buildWithCycle(new int[]{3, 2, 0, -4}, 1);
        ListNode r2 = new Optimal().detectCycle(t1[0]);
        System.out.println("  Optimal:  " + (r2 != null ? r2.val : "null"));
        t1 = buildWithCycle(new int[]{3, 2, 0, -4}, 1);
        ListNode r3 = new Best().detectCycle(t1[0]);
        System.out.println("  Best:     " + (r3 != null ? r3.val : "null"));
        System.out.println();

        // Test 2: [1,2], cycle at index 0
        ListNode[] t2 = buildWithCycle(new int[]{1, 2}, 0);
        System.out.println("Test 2: [1,2] cycle at index 0");
        System.out.println("  Expected: " + (t2[1] != null ? t2[1].val : "null"));
        ListNode r4 = new BruteForce().detectCycle(t2[0]);
        System.out.println("  Brute:    " + (r4 != null ? r4.val : "null"));
        t2 = buildWithCycle(new int[]{1, 2}, 0);
        ListNode r5 = new Optimal().detectCycle(t2[0]);
        System.out.println("  Optimal:  " + (r5 != null ? r5.val : "null"));
        t2 = buildWithCycle(new int[]{1, 2}, 0);
        ListNode r6 = new Best().detectCycle(t2[0]);
        System.out.println("  Best:     " + (r6 != null ? r6.val : "null"));
        System.out.println();

        // Test 3: [1], no cycle
        ListNode[] t3 = buildWithCycle(new int[]{1}, -1);
        System.out.println("Test 3: [1] no cycle");
        System.out.println("  Expected: null");
        System.out.println("  Brute:    " + (new BruteForce().detectCycle(t3[0]) == null ? "null" : "NOT null"));
        System.out.println("  Optimal:  " + (new Optimal().detectCycle(t3[0]) == null ? "null" : "NOT null"));
        System.out.println("  Best:     " + (new Best().detectCycle(t3[0]) == null ? "null" : "NOT null"));
    }
}
