/**
 * Problem: Merge K Sorted Lists
 * Difficulty: HARD | XP: 50
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
// Time: O(n log n)  |  Space: O(n)
// ============================================================
// Collect every node value from all k lists into an ArrayList,
// sort it, then build a fresh linked list from sorted values.
// Simple but ignores the "already sorted" structure.
class BruteForce {
    public ListNode mergeKLists(ListNode[] lists) {
        java.util.List<Integer> vals = new java.util.ArrayList<>();
        for (ListNode head : lists) {
            ListNode curr = head;
            while (curr != null) {
                vals.add(curr.val);
                curr = curr.next;
            }
        }
        java.util.Collections.sort(vals);

        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        for (int v : vals) {
            curr.next = new ListNode(v);
            curr = curr.next;
        }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n log k)  |  Space: O(k)
// ============================================================
// Min-heap of size at most k (one entry per list).
// Seed it with each list's head, then repeatedly:
//   1. Poll the minimum node from the heap.
//   2. Append it to the result.
//   3. If it has a next node, push that into the heap.
// Each of the n nodes is pushed/popped exactly once: O(n log k).
class Optimal {
    public ListNode mergeKLists(ListNode[] lists) {
        java.util.PriorityQueue<ListNode> minHeap =
            new java.util.PriorityQueue<>((a, b) -> a.val - b.val);

        for (ListNode head : lists) {
            if (head != null) minHeap.offer(head);
        }

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (!minHeap.isEmpty()) {
            ListNode node = minHeap.poll();
            tail.next = node;
            tail = tail.next;
            if (node.next != null) minHeap.offer(node.next);
        }
        return dummy.next;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n log k)  |  Space: O(log k)  (recursion stack)
// ============================================================
// Divide and conquer: split the k lists into two halves,
// recursively merge each half, then merge the two results.
// There are log k levels; merging at each level touches all n
// nodes => O(n log k) total. No heap overhead; cache-friendly.
class Best {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return divideAndConquer(lists, 0, lists.length - 1);
    }

    private ListNode divideAndConquer(ListNode[] lists, int lo, int hi) {
        if (lo == hi) return lists[lo];
        int mid = lo + (hi - lo) / 2;
        ListNode left  = divideAndConquer(lists, lo, mid);
        ListNode right = divideAndConquer(lists, mid + 1, hi);
        return mergeTwoSorted(left, right);
    }

    // Standard O(m+n) in-place merge of two sorted lists
    private ListNode mergeTwoSorted(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) { tail.next = l1; l1 = l1.next; }
            else                  { tail.next = l2; l2 = l2.next; }
            tail = tail.next;
        }
        tail.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }
}

public class Solution {

    static ListNode buildList(int... vals) {
        ListNode dummy = new ListNode(0), curr = dummy;
        for (int v : vals) { curr.next = new ListNode(v); curr = curr.next; }
        return dummy.next;
    }

    static String listToString(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        for (ListNode c = head; c != null; c = c.next) {
            sb.append(c.val);
            if (c.next != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Merge K Sorted Lists ===\n");

        // Standard test: [[1,4,5],[1,3,4],[2,6]] => [1,1,2,3,4,4,5,6]
        System.out.println("[Approach 1 - BruteForce: Collect & Sort]");
        ListNode[] t1 = { buildList(1, 4, 5), buildList(1, 3, 4), buildList(2, 6) };
        System.out.println("Result: " + listToString(new BruteForce().mergeKLists(t1)));

        System.out.println("\n[Approach 2 - Optimal: Min-Heap]");
        ListNode[] t2 = { buildList(1, 4, 5), buildList(1, 3, 4), buildList(2, 6) };
        System.out.println("Result: " + listToString(new Optimal().mergeKLists(t2)));

        System.out.println("\n[Approach 3 - Best: Divide & Conquer]");
        ListNode[] t3 = { buildList(1, 4, 5), buildList(1, 3, 4), buildList(2, 6) };
        System.out.println("Result: " + listToString(new Best().mergeKLists(t3)));

        // Edge cases
        System.out.println("\n[Edge cases]");
        System.out.println("Empty array:   "
            + listToString(new Best().mergeKLists(new ListNode[]{})));
        System.out.println("Single empty:  "
            + listToString(new Best().mergeKLists(new ListNode[]{null})));
        System.out.println("Single list:   "
            + listToString(new Best().mergeKLists(new ListNode[]{buildList(3, 7, 9)})));
        System.out.println("With negatives: "
            + listToString(new Best().mergeKLists(new ListNode[]{
                buildList(-3, -1, 2), buildList(-5, 0, 4)})));
    }
}
