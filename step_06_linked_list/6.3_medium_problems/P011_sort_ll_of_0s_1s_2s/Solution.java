/**
 * Problem: Sort a Linked List of 0s, 1s, and 2s
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a linked list containing only 0s, 1s, and 2s, sort it.
 *
 * @author DSA_Nova
 */
public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Count and overwrite values
    // Time: O(2n) = O(n)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Pass 1: Count occurrences of 0, 1, and 2.
         * Pass 2: Overwrite node values in sorted order.
         * Simple and correct; nodes retain original pointers, only values change.
         */
        public ListNode sortList(ListNode head) {
            int[] count = new int[3];
            ListNode cur = head;
            while (cur != null) {
                count[cur.val]++;
                cur = cur.next;
            }

            cur = head;
            for (int val = 0; val < 3; val++) {
                while (count[val]-- > 0) {
                    cur.val = val;
                    cur = cur.next;
                }
            }
            return head;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Three dummy lists, link buckets at end
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Create three dummy-headed sub-lists (one per value: 0, 1, 2).
         * Traverse original list; append each node to its corresponding sub-list.
         * Link: 0s-tail -> 1s-head -> 1s-tail -> 2s-head.
         * Terminate 2s-tail -> null.
         * Single pass, only 6 pointer variables, preserves node identity (no value rewrite).
         */
        public ListNode sortList(ListNode head) {
            ListNode d0 = new ListNode(0), d1 = new ListNode(0), d2 = new ListNode(0);
            ListNode t0 = d0, t1 = d1, t2 = d2;

            ListNode cur = head;
            while (cur != null) {
                ListNode nxt = cur.next;
                cur.next = null;
                if      (cur.val == 0) { t0.next = cur; t0 = t0.next; }
                else if (cur.val == 1) { t1.next = cur; t1 = t1.next; }
                else                  { t2.next = cur; t2 = t2.next; }
                cur = nxt;
            }

            // Chain: 0s -> 1s -> 2s
            t0.next = (d1.next != null) ? d1.next : d2.next;
            t1.next = d2.next;
            t2.next = null;

            return d0.next;
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Three-bucket partition (array of dummies)
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Identical correctness to Approach 2.
         * Uses arrays of dummy heads and tails to make the bucket logic
         * more systematic and easier to generalize (e.g., to k buckets).
         */
        public ListNode sortList(ListNode head) {
            ListNode[] heads = {new ListNode(-1), new ListNode(-1), new ListNode(-1)};
            ListNode[] tails = {heads[0], heads[1], heads[2]};

            ListNode cur = head;
            while (cur != null) {
                int v = cur.val;
                tails[v].next = cur;
                tails[v] = cur;
                cur = cur.next;
            }
            tails[2].next = null;

            // Connect buckets: 0s -> 1s -> 2s
            tails[0].next = (heads[1].next != null) ? heads[1].next : heads[2].next;
            tails[1].next = heads[2].next;

            return heads[0].next;
        }
    }

    // ============================================================
    // DRIVER
    // ============================================================
    static ListNode makeList(int... vals) {
        ListNode dummy = new ListNode(0), cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    static String listToStr(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(",");
            head = head.next;
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Sort LL of 0s 1s 2s ===");

        int[][] inputs   = {{1,2,0,2,1,0}, {0,1,2}, {2,1,0}, {0}, {2,2,2}, {1,1,2,0,0}};
        String[] expected = {"[0,0,1,1,2,2]", "[0,1,2]", "[0,1,2]", "[0]", "[2,2,2]", "[0,0,1,1,2]"};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        for (int i = 0; i < inputs.length; i++) {
            String b  = listToStr(bf.sortList(makeList(inputs[i])));
            String o  = listToStr(opt.sortList(makeList(inputs[i])));
            String be = listToStr(bst.sortList(makeList(inputs[i])));
            String status = (b.equals(expected[i]) && o.equals(expected[i]) && be.equals(expected[i])) ? "OK" : "FAIL";
            System.out.printf("[%s] Brute=%s, Optimal=%s, Best=%s (expected %s)%n",
                status, b, o, be, expected[i]);
        }
    }
}
