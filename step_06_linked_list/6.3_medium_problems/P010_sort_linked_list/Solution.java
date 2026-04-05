/**
 * Problem: Sort Linked List (LeetCode 148)
 * Difficulty: MEDIUM | XP: 25
 *
 * Sort a singly linked list in O(n log n) time.
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
    // APPROACH 1: BRUTE FORCE - Copy to array, sort, rebuild
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        /**
         * 1. Walk the list to count length, copy values to int[].
         * 2. Sort array with Arrays.sort (dual-pivot quicksort, O(n log n)).
         * 3. Rewrite node values in sorted order.
         * Simple but O(n) extra space.
         */
        public ListNode sortList(ListNode head) {
            if (head == null) return null;

            int n = 0;
            ListNode cur = head;
            while (cur != null) { n++; cur = cur.next; }

            int[] vals = new int[n];
            cur = head;
            for (int i = 0; i < n; i++) { vals[i] = cur.val; cur = cur.next; }

            java.util.Arrays.sort(vals);

            cur = head;
            for (int v : vals) { cur.val = v; cur = cur.next; }

            return head;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Bottom-up merge sort (O(1) extra space)
    // Time: O(n log n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Bottom-up merge sort: no recursion stack.
         * - Count list length.
         * - Merge sublists of size 1, then 2, then 4, ... until size >= length.
         * - Each pass uses split() and merge() helpers.
         * True O(1) extra space.
         */
        public ListNode sortList(ListNode head) {
            if (head == null || head.next == null) return head;

            int length = 0;
            for (ListNode c = head; c != null; c = c.next) length++;

            ListNode dummy = new ListNode(0);
            dummy.next = head;

            for (int size = 1; size < length; size <<= 1) {
                ListNode cur  = dummy.next;
                ListNode tail = dummy;

                while (cur != null) {
                    ListNode left  = cur;
                    ListNode right = split(left, size);
                    cur = split(right, size);

                    ListNode[] merged = merge(left, right);
                    tail.next = merged[0];
                    tail = merged[1];
                }
            }
            return dummy.next;
        }

        /** Cut off 'size' nodes from head, return the rest. */
        private ListNode split(ListNode head, int size) {
            ListNode cur = head;
            for (int i = 1; i < size && cur != null && cur.next != null; i++) {
                cur = cur.next;
            }
            if (cur == null) return null;
            ListNode rest = cur.next;
            cur.next = null;
            return rest;
        }

        /** Merge two sorted lists; returns {head, tail} of merged list. */
        private ListNode[] merge(ListNode l1, ListNode l2) {
            ListNode dummy = new ListNode(0), cur = dummy;
            while (l1 != null && l2 != null) {
                if (l1.val <= l2.val) { cur.next = l1; l1 = l1.next; }
                else                 { cur.next = l2; l2 = l2.next; }
                cur = cur.next;
            }
            cur.next = (l1 != null) ? l1 : l2;
            while (cur.next != null) cur = cur.next;
            return new ListNode[]{dummy.next, cur};
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Top-down merge sort (O(log n) stack space)
    // Time: O(n log n)  |  Space: O(log n)
    // ============================================================
    static class Best {
        /**
         * Classic recursive top-down merge sort.
         * 1. Find midpoint using slow/fast pointers.
         * 2. Split into two halves.
         * 3. Recursively sort each half.
         * 4. Merge sorted halves.
         * Cleaner and easier to code in interviews; O(log n) stack space.
         */
        public ListNode sortList(ListNode head) {
            if (head == null || head.next == null) return head;

            // Find midpoint
            ListNode slow = head, fast = head.next;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }

            ListNode mid = slow.next;
            slow.next = null;  // split list

            ListNode left  = sortList(head);
            ListNode right = sortList(mid);
            return merge(left, right);
        }

        private ListNode merge(ListNode l1, ListNode l2) {
            ListNode dummy = new ListNode(0), cur = dummy;
            while (l1 != null && l2 != null) {
                if (l1.val <= l2.val) { cur.next = l1; l1 = l1.next; }
                else                 { cur.next = l2; l2 = l2.next; }
                cur = cur.next;
            }
            cur.next = (l1 != null) ? l1 : l2;
            return dummy.next;
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
        System.out.println("=== Sort Linked List ===");

        int[][] inputs   = {{4,2,1,3}, {-1,5,3,4,0}, {1}, {2,1}};
        String[] expected = {"[1,2,3,4]", "[-1,0,3,4,5]", "[1]", "[1,2]"};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        for (int i = 0; i < inputs.length; i++) {
            String b  = listToStr(bf.sortList(makeList(inputs[i])));
            String o  = listToStr(opt.sortList(makeList(inputs[i])));
            String be = listToStr(bst.sortList(makeList(inputs[i])));
            String status = (b.equals(expected[i]) && o.equals(expected[i]) && be.equals(expected[i])) ? "OK" : "FAIL";
            System.out.printf("[%s] Brute=%s, Optimal=%s, Best=%s%n", status, b, o, be);
        }
    }
}
