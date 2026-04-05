/**
 * Problem: Intersection of Two Linked Lists (LeetCode 160)
 * Difficulty: MEDIUM | XP: 25
 *
 * Find the node at which two singly linked lists intersect.
 * Return null if they do not intersect.
 *
 * @author DSA_Nova
 */
import java.util.HashSet;

public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE — HashSet
    // Time: O(m + n)  |  Space: O(m)
    // ============================================================
    static class BruteForce {
        /**
         * Store all nodes of list A in a HashSet.
         * Traverse list B; the first node found in the set is the intersection.
         */
        public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            HashSet<ListNode> visited = new HashSet<>();
            ListNode curr = headA;
            while (curr != null) {
                visited.add(curr);
                curr = curr.next;
            }
            curr = headB;
            while (curr != null) {
                if (visited.contains(curr)) return curr;
                curr = curr.next;
            }
            return null;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Length Difference + Align
    // Time: O(m + n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Compute lengths of both lists. Advance the pointer of the
         * longer list by the difference so both are equidistant from
         * the end, then walk together until they meet.
         */
        public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            int lenA = getLength(headA);
            int lenB = getLength(headB);

            ListNode currA = headA, currB = headB;

            while (lenA > lenB) { currA = currA.next; lenA--; }
            while (lenB > lenA) { currB = currB.next; lenB--; }

            while (currA != currB) {
                currA = currA.next;
                currB = currB.next;
            }
            return currA; // null if no intersection
        }

        private int getLength(ListNode node) {
            int len = 0;
            while (node != null) { len++; node = node.next; }
            return len;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Two Pointer Switch Heads
    // Time: O(m + n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Two pointers start at headA and headB.
         * When one reaches the end it switches to the OTHER list's head.
         * After at most (m + n) steps they either meet at the intersection
         * or both reach null simultaneously (no intersection).
         *
         * Key insight: both pointers travel the same total distance
         * (lenA + lenB), so they align at the intersection node.
         */
        public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            if (headA == null || headB == null) return null;
            ListNode pA = headA, pB = headB;
            while (pA != pB) {
                pA = (pA != null) ? pA.next : headB;
                pB = (pB != null) ? pB.next : headA;
            }
            return pA;
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        // Shared tail: 8 -> 4 -> 5
        ListNode shared = new ListNode(8, new ListNode(4, new ListNode(5, null)));
        // List A: 4 -> 1 -> [shared]
        ListNode headA = new ListNode(4, new ListNode(1, shared));
        // List B: 5 -> 6 -> 1 -> [shared]
        ListNode headB = new ListNode(5, new ListNode(6, new ListNode(1, shared)));

        System.out.println("=== Intersection of Two Linked Lists ===");

        ListNode res1 = new BruteForce().getIntersectionNode(headA, headB);
        System.out.println("Brute:   intersects at val = " + (res1 != null ? res1.val : "null"));

        ListNode res2 = new Optimal().getIntersectionNode(headA, headB);
        System.out.println("Optimal: intersects at val = " + (res2 != null ? res2.val : "null"));

        ListNode res3 = new Best().getIntersectionNode(headA, headB);
        System.out.println("Best:    intersects at val = " + (res3 != null ? res3.val : "null"));
    }
}
