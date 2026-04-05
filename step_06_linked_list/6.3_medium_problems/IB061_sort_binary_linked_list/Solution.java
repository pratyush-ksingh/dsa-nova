/**
 * Problem: Sort Binary Linked List
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given a linked list of 0s and 1s, sort it so all 0s come before 1s.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ----- Linked List Node Definition -----
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; this.next = null; }
    }

    // Helper: build list from array
    static ListNode buildList(int[] arr) {
        if (arr.length == 0) return null;
        ListNode head = new ListNode(arr[0]);
        ListNode curr = head;
        for (int i = 1; i < arr.length; i++) {
            curr.next = new ListNode(arr[i]);
            curr = curr.next;
        }
        return head;
    }

    // Helper: list to string
    static String listToString(ListNode head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(" -> ");
            head = head.next;
        }
        return sb.length() == 0 ? "(empty)" : sb.toString();
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Count and Overwrite
    // Time: O(n)  |  Space: O(1)
    //
    // Count how many 0s exist. In a second pass, set the first
    // count0 nodes to 0 and the remaining to 1.
    // Caveat: modifies node values (fine if nodes have no satellite data).
    // ============================================================
    public static ListNode bruteForce(ListNode head) {
        if (head == null) return null;

        // Pass 1: count zeros
        int count0 = 0;
        ListNode curr = head;
        while (curr != null) {
            if (curr.val == 0) count0++;
            curr = curr.next;
        }

        // Pass 2: overwrite values
        curr = head;
        while (curr != null) {
            if (count0 > 0) {
                curr.val = 0;
                count0--;
            } else {
                curr.val = 1;
            }
            curr = curr.next;
        }

        return head;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Split into Two Lists
    // Time: O(n)  |  Space: O(1)
    //
    // Collect 0-nodes into one list, 1-nodes into another.
    // Connect the tail of the 0-list to the head of the 1-list.
    // Does NOT modify node values -- safe for satellite data.
    // ============================================================
    public static ListNode optimal(ListNode head) {
        if (head == null) return null;

        ListNode dummy0 = new ListNode(-1);
        ListNode dummy1 = new ListNode(-1);
        ListNode tail0 = dummy0, tail1 = dummy1;

        ListNode curr = head;
        while (curr != null) {
            if (curr.val == 0) {
                tail0.next = curr;
                tail0 = tail0.next;
            } else {
                tail1.next = curr;
                tail1 = tail1.next;
            }
            curr = curr.next;
        }

        // Connect 0-list to 1-list
        tail0.next = dummy1.next;
        // CRITICAL: terminate 1-list to avoid cycle
        tail1.next = null;

        // If no 0s exist, return 1-list head
        return dummy0.next != null ? dummy0.next : dummy1.next;
    }

    // ============================================================
    // APPROACH 3: BEST -- Clean Single-Pass Split
    // Time: O(n)  |  Space: O(1)
    //
    // Same logic as optimal, written compactly with all edge
    // cases handled inline. This is the interview-ready version.
    // ============================================================
    public static ListNode best(ListNode head) {
        ListNode d0 = new ListNode(0), d1 = new ListNode(0);
        ListNode t0 = d0, t1 = d1;

        for (ListNode c = head; c != null; c = c.next) {
            if (c.val == 0) { t0.next = c; t0 = c; }
            else            { t1.next = c; t1 = c; }
        }

        t0.next = d1.next;   // join: 0-tail -> 1-head
        t1.next = null;      // terminate to prevent cycle
        return d0.next != null ? d0.next : d1.next;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Sort Binary Linked List ===\n");

        int[][] tests = {
            {1, 0, 1, 0},
            {0, 0, 0},
            {1, 1, 1},
            {1},
            {},
            {0, 1, 0, 1, 0}
        };

        for (int[] t : tests) {
            System.out.println("Input:  " + listToString(buildList(t)));
            System.out.println("Brute:  " + listToString(bruteForce(buildList(t))));
            System.out.println("Optim:  " + listToString(optimal(buildList(t))));
            System.out.println("Best:   " + listToString(best(buildList(t))));
            System.out.println();
        }
    }
}
