/**
 * Problem: Rotate a Linked List (LeetCode #61)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Rotate One at a Time
    // Time: O(n * k)  |  Space: O(1)
    //
    // Perform k individual right-rotations.
    // Each rotation moves the last node to the front.
    // ============================================================
    static class BruteForce {
        public ListNode rotateRight(ListNode head, int k) {
            if (head == null || head.next == null || k == 0) return head;

            // Find length to reduce k
            int length = 0;
            ListNode curr = head;
            while (curr != null) { length++; curr = curr.next; }
            k = k % length;
            if (k == 0) return head;

            for (int i = 0; i < k; i++) {
                ListNode prev = null;
                curr = head;
                while (curr.next != null) {
                    prev = curr;
                    curr = curr.next;
                }
                prev.next = null;
                curr.next = head;
                head = curr;
            }
            return head;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Find Length, Make Circular, Break
    // Time: O(n)  |  Space: O(1)
    //
    // Compute length, k %= length, connect tail->head (circular),
    // walk (len-k) steps from tail to find new tail, break circle.
    // ============================================================
    static class Optimal {
        public ListNode rotateRight(ListNode head, int k) {
            if (head == null || head.next == null || k == 0) return head;

            // Find length and tail
            int length = 1;
            ListNode tail = head;
            while (tail.next != null) {
                length++;
                tail = tail.next;
            }

            k = k % length;
            if (k == 0) return head;

            // Make circular
            tail.next = head;

            // Walk (length - k) steps from tail to new tail
            int steps = length - k;
            ListNode newTail = tail;
            for (int i = 0; i < steps; i++) {
                newTail = newTail.next;
            }

            ListNode newHead = newTail.next;
            newTail.next = null;
            return newHead;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same Approach, Minimal Code
    // Time: O(n)  |  Space: O(1)
    //
    // Same circular trick, most concise Java implementation.
    // ============================================================
    static class Best {
        public ListNode rotateRight(ListNode head, int k) {
            if (head == null || head.next == null) return head;

            ListNode tail = head;
            int len = 1;
            while (tail.next != null) { tail = tail.next; len++; }

            k %= len;
            if (k == 0) return head;

            tail.next = head;  // circular
            for (int i = 0; i < len - k; i++) tail = tail.next;
            head = tail.next;
            tail.next = null;
            return head;
        }
    }

    // ---- Helpers ----
    static ListNode buildList(int[] vals) {
        if (vals.length == 0) return null;
        ListNode head = new ListNode(vals[0]);
        ListNode curr = head;
        for (int i = 1; i < vals.length; i++) {
            curr.next = new ListNode(vals[i]);
            curr = curr.next;
        }
        return head;
    }

    static String toString(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(", ");
            head = head.next;
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Rotate a Linked List ===\n");

        int[][] values = {{1,2,3,4,5}, {0,1,2}, {1}, {1,2}};
        int[] ks = {2, 4, 0, 1};
        String[] expected = {"[4, 5, 1, 2, 3]", "[2, 0, 1]", "[1]", "[2, 1]"};

        for (int t = 0; t < values.length; t++) {
            System.out.println("Input: " + java.util.Arrays.toString(values[t]) + ", k=" + ks[t]);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Brute:    " + toString(new BruteForce().rotateRight(buildList(values[t]), ks[t])));
            System.out.println("Optimal:  " + toString(new Optimal().rotateRight(buildList(values[t]), ks[t])));
            System.out.println("Best:     " + toString(new Best().rotateRight(buildList(values[t]), ks[t])));
            System.out.println();
        }
    }
}
