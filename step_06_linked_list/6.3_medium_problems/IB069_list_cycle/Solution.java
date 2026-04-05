/**
 * Problem: List Cycle
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a linked list, return the node where the cycle begins.
 * If there is no cycle, return null.
 *
 * Real-life use case: Detecting circular references in dependency graphs,
 * infinite loops in OS process scheduling queues.
 *
 * @author DSA_Nova
 */

class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
}

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n)  |  Space: O(n)
// Use a HashSet to record visited nodes. First repeated node is cycle start.
// ============================================================
class BruteForce {
    public ListNode detectCycleStart(ListNode head) {
        java.util.HashSet<ListNode> visited = new java.util.HashSet<>();
        ListNode curr = head;
        while (curr != null) {
            if (visited.contains(curr)) return curr;
            visited.add(curr);
            curr = curr.next;
        }
        return null;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(1)
// Floyd's cycle detection: fast+slow pointers meet inside cycle.
// Then reset one pointer to head; advance both by 1 until they meet at cycle start.
// Math proof: if meeting point is k steps into cycle, head is also k steps from start.
// ============================================================
class Optimal {
    public ListNode detectCycleStart(ListNode head) {
        ListNode slow = head, fast = head;
        // Phase 1: detect cycle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) break;
        }
        // No cycle
        if (fast == null || fast.next == null) return null;
        // Phase 2: find cycle start
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(1)
// Same as Optimal (Floyd's is already theoretically optimal for O(1) space).
// This version adds a length calculation as bonus insight:
// after finding the meeting node, walk the cycle to find its length L,
// then use two pointers L apart from head to find start.
// ============================================================
class Best {
    public ListNode detectCycleStart(ListNode head) {
        ListNode slow = head, fast = head;
        // Phase 1: detect cycle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) break;
        }
        if (fast == null || fast.next == null) return null;

        // Find cycle length
        int cycleLen = 1;
        ListNode temp = slow.next;
        while (temp != slow) {
            cycleLen++;
            temp = temp.next;
        }

        // Phase 2: two pointers, advance one by cycleLen first
        ListNode p1 = head, p2 = head;
        for (int i = 0; i < cycleLen; i++) p2 = p2.next;
        while (p1 != p2) {
            p1 = p1.next;
            p2 = p2.next;
        }
        return p1;
    }
}

public class Solution {

    // Helper: build a linked list with a cycle at position pos (0-indexed), -1 = no cycle
    static ListNode buildList(int[] vals, int pos) {
        if (vals.length == 0) return null;
        ListNode[] nodes = new ListNode[vals.length];
        for (int i = 0; i < vals.length; i++) nodes[i] = new ListNode(vals[i]);
        for (int i = 0; i < vals.length - 1; i++) nodes[i].next = nodes[i + 1];
        if (pos >= 0) nodes[vals.length - 1].next = nodes[pos];
        return nodes[0];
    }

    public static void main(String[] args) {
        System.out.println("=== List Cycle ===");

        // Test 1: cycle at index 1  => list: 3->2->0->-4->back to 2
        ListNode head1 = buildList(new int[]{3, 2, 0, -4}, 1);

        BruteForce bf = new BruteForce();
        Optimal opt = new Optimal();
        Best best = new Best();

        ListNode r1 = bf.detectCycleStart(head1);
        System.out.println("Brute   (cycle at val=2): " + (r1 != null ? r1.val : "null"));  // 2

        // Rebuild (HashSet doesn't modify list)
        ListNode head2 = buildList(new int[]{3, 2, 0, -4}, 1);
        ListNode r2 = opt.detectCycleStart(head2);
        System.out.println("Optimal (cycle at val=2): " + (r2 != null ? r2.val : "null"));  // 2

        ListNode head3 = buildList(new int[]{3, 2, 0, -4}, 1);
        ListNode r3 = best.detectCycleStart(head3);
        System.out.println("Best    (cycle at val=2): " + (r3 != null ? r3.val : "null"));  // 2

        // Test 2: no cycle
        ListNode head4 = buildList(new int[]{1, 2, 3}, -1);
        System.out.println("\nNo cycle Brute:   " + bf.detectCycleStart(head4));   // null
        System.out.println("No cycle Optimal: " + opt.detectCycleStart(head4));    // null
        System.out.println("No cycle Best:    " + best.detectCycleStart(head4));   // null

        // Test 3: cycle at head (pos=0)
        ListNode head5 = buildList(new int[]{1, 2}, 0);
        ListNode r5 = opt.detectCycleStart(head5);
        System.out.println("\nCycle at head Optimal (val=1): " + (r5 != null ? r5.val : "null")); // 1
    }
}
