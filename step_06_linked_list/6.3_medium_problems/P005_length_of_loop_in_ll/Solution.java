/**
 * Problem: Length of Loop in Linked List
 * Difficulty: MEDIUM | XP: 25
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
    // APPROACH 1: BRUTE FORCE -- HashMap of visited nodes
    // Time: O(n)  |  Space: O(n)
    //
    // Traverse the list. Store each node with its step number in a
    // HashMap. When a node is revisited, cycle length = step difference.
    // ============================================================
    static class BruteForce {
        public int countNodesinLoop(ListNode head) {
            Map<ListNode, Integer> visited = new HashMap<>();
            ListNode curr = head;
            int step = 0;
            while (curr != null) {
                if (visited.containsKey(curr)) {
                    return step - visited.get(curr);
                }
                visited.put(curr, step);
                curr = curr.next;
                step++;
            }
            return 0;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Floyd's Cycle Detection + Count
    // Time: O(n)  |  Space: O(1)
    //
    // Phase 1: slow/fast pointers find the meeting point.
    // Phase 2: From meeting point, traverse until we return to it,
    //          counting steps = cycle length.
    // ============================================================
    static class Optimal {
        public int countNodesinLoop(ListNode head) {
            ListNode slow = head, fast = head;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) {
                    // Count loop length
                    int length = 1;
                    ListNode curr = slow.next;
                    while (curr != slow) {
                        length++;
                        curr = curr.next;
                    }
                    return length;
                }
            }
            return 0;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same Floyd's, explicit two-phase structure
    // Time: O(n)  |  Space: O(1)
    //
    // Same algorithm, but the two phases are separated clearly
    // with a boolean flag -- easier to follow in an interview.
    // ============================================================
    static class Best {
        public int countNodesinLoop(ListNode head) {
            ListNode slow = head, fast = head;
            boolean hasCycle = false;

            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) { hasCycle = true; break; }
            }
            if (!hasCycle) return 0;

            ListNode meeting = slow;
            int length = 1;
            ListNode ptr = meeting.next;
            while (ptr != meeting) {
                length++;
                ptr = ptr.next;
            }
            return length;
        }
    }

    // --------------- helpers ---------------
    static ListNode buildCycle(int[] vals, int cyclePos) {
        if (vals.length == 0) return null;
        ListNode[] nodes = new ListNode[vals.length];
        for (int i = 0; i < vals.length; i++) nodes[i] = new ListNode(vals[i]);
        for (int i = 0; i < vals.length - 1; i++) nodes[i].next = nodes[i + 1];
        if (cyclePos >= 0) nodes[vals.length - 1].next = nodes[cyclePos];
        return nodes[0];
    }

    public static void main(String[] args) {
        System.out.println("=== Length of Loop in Linked List ===\n");

        int[][] vals      = {{1,2,3,4,5}, {1,2,3,4,5}, {1,2,3}, {1}};
        int[]   cyclePoss = {1,           0,            -1,       0 };
        int[]   exp       = {4,           5,             0,        1 };

        for (int t = 0; t < vals.length; t++) {
            ListNode h1 = buildCycle(vals[t], cyclePoss[t]);
            ListNode h2 = buildCycle(vals[t], cyclePoss[t]);
            ListNode h3 = buildCycle(vals[t], cyclePoss[t]);
            int b  = new BruteForce().countNodesinLoop(h1);
            int o  = new Optimal().countNodesinLoop(h2);
            int bt = new Best().countNodesinLoop(h3);
            String st = (b == exp[t] && o == exp[t] && bt == exp[t]) ? "OK" : "MISMATCH";
            System.out.printf("cyclePos=%d  Expected=%d  Brute=%d  Optimal=%d  Best=%d  [%s]%n",
                    cyclePoss[t], exp[t], b, o, bt, st);
        }
    }
}
