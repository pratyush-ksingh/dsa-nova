/**
 * Problem: Kth Node From Middle
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given a singly linked list and integer k, find the kth node from
 * the middle towards the beginning. Middle index = (n-1)/2.
 * Return the node value, or -1 if invalid.
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

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Three logical steps
    // Time: O(n)  |  Space: O(1)
    //
    // Pass 1: count n.
    // Compute target = (n-1)/2 - k.
    // Pass 2: walk to target index.
    // ============================================================
    public static int bruteForce(ListNode head, int k) {
        if (head == null) return -1;

        // Step 1: count length
        int n = 0;
        ListNode curr = head;
        while (curr != null) {
            n++;
            curr = curr.next;
        }

        // Step 2: compute target index
        int mid = (n - 1) / 2;
        int target = mid - k;
        if (target < 0) return -1;

        // Step 3: walk to target
        curr = head;
        for (int i = 0; i < target; i++) {
            curr = curr.next;
        }
        return curr.val;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Array Storage
    // Time: O(n)  |  Space: O(n)
    //
    // Store all values in an array for O(1) random access.
    // Then directly index array[(n-1)/2 - k].
    // ============================================================
    public static int optimal(ListNode head, int k) {
        if (head == null) return -1;

        // Collect values
        java.util.List<Integer> values = new java.util.ArrayList<>();
        ListNode curr = head;
        while (curr != null) {
            values.add(curr.val);
            curr = curr.next;
        }

        int n = values.size();
        int mid = (n - 1) / 2;
        int target = mid - k;

        if (target < 0) return -1;
        return values.get(target);
    }

    // ============================================================
    // APPROACH 3: BEST -- Clean Two-Pass, O(1) Space
    // Time: O(n)  |  Space: O(1)
    //
    // Same as brute force, written cleanly. Count n, compute
    // target = (n-1)/2 - k, walk to it. Theoretically optimal
    // for a singly linked list.
    // ============================================================
    public static int best(ListNode head, int k) {
        // Count length
        int n = 0;
        for (ListNode c = head; c != null; c = c.next) n++;

        int target = (n - 1) / 2 - k;
        if (target < 0 || n == 0) return -1;

        // Walk to target index
        ListNode c = head;
        for (int i = 0; i < target; i++) c = c.next;
        return c.val;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Kth Node From Middle ===\n");

        int[] list5 = {1, 2, 3, 4, 5};
        int[] list4 = {1, 2, 3, 4};
        int[] list1 = {5};

        int[][] testCases = {
            // {listId, k, expected}
            {5, 0, 3}, {5, 1, 2}, {5, 2, 1}, {5, 3, -1},
            {4, 0, 2}, {4, 1, 1},
            {1, 0, 5}, {1, 1, -1}
        };

        for (int[] tc : testCases) {
            int[] arr = tc[0] == 5 ? list5 : tc[0] == 4 ? list4 : list1;
            int k = tc[1];
            int expected = tc[2];
            int result = best(buildList(arr), k);
            String status = result == expected ? "PASS" : "FAIL";
            System.out.printf("[%s] list=%d-node, k=%d -> %d (expected %d)%n",
                    status, tc[0], k, result, expected);
        }

        // Also verify all three approaches match
        System.out.println("\n--- Cross-check all approaches ---");
        ListNode h = buildList(list5);
        System.out.println("Brute(k=1):   " + bruteForce(buildList(list5), 1));
        System.out.println("Optimal(k=1): " + optimal(buildList(list5), 1));
        System.out.println("Best(k=1):    " + best(buildList(list5), 1));
    }
}
