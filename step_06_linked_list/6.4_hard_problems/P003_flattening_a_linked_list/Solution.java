/**
 * Problem: Flattening a Linked List (GeeksForGeeks)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static class Node {
        int data;
        Node next;
        Node bottom;
        Node(int data) { this.data = data; }
    }

    // ---- Shared helper: merge two sorted bottom-lists ----
    static Node mergeTwoLists(Node a, Node b) {
        Node dummy = new Node(0);
        Node curr = dummy;
        while (a != null && b != null) {
            if (a.data <= b.data) {
                curr.bottom = a;
                a = a.bottom;
            } else {
                curr.bottom = b;
                b = b.bottom;
            }
            curr = curr.bottom;
        }
        curr.bottom = (a != null) ? a : b;
        return dummy.bottom;
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Collect All, Sort, Rebuild
    // Time: O(N log N)  |  Space: O(N)
    //
    // Traverse entire structure collecting all values, sort them,
    // rebuild a single linked list using bottom pointers.
    // ============================================================
    static class BruteForce {
        public Node flatten(Node head) {
            if (head == null) return null;

            List<Integer> values = new ArrayList<>();
            Node curr = head;
            while (curr != null) {
                Node down = curr;
                while (down != null) {
                    values.add(down.data);
                    down = down.bottom;
                }
                curr = curr.next;
            }

            Collections.sort(values);

            Node dummy = new Node(0);
            Node tail = dummy;
            for (int val : values) {
                tail.bottom = new Node(val);
                tail = tail.bottom;
            }
            return dummy.bottom;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Iterative Merge from Right
    // Time: O(N * k)  |  Space: O(1)
    //
    // Collect top-level nodes, merge bottom-lists pairwise
    // from right to left.
    // ============================================================
    static class Optimal {
        public Node flatten(Node head) {
            if (head == null) return null;

            // Collect top-level heads
            List<Node> heads = new ArrayList<>();
            Node curr = head;
            while (curr != null) {
                heads.add(curr);
                Node nxt = curr.next;
                curr.next = null;  // disconnect
                curr = nxt;
            }

            // Merge from right to left
            Node result = heads.get(heads.size() - 1);
            for (int i = heads.size() - 2; i >= 0; i--) {
                result = mergeTwoLists(heads.get(i), result);
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Recursive Merge from Right
    // Time: O(N * k)  |  Space: O(k) recursion stack
    //
    // Recursively flatten head.next, then merge head's bottom
    // list with the flattened result. Clean recursion.
    // ============================================================
    static class Best {
        public Node flatten(Node head) {
            if (head == null || head.next == null) return head;

            // Recursively flatten the rest
            head.next = flatten(head.next);

            // Merge current list with flattened next
            head = mergeTwoLists(head, head.next);

            return head;
        }
    }

    // ---- Helpers for testing ----
    static Node buildFlatList(int[][] columns) {
        if (columns.length == 0) return null;
        Node[] heads = new Node[columns.length];
        for (int c = 0; c < columns.length; c++) {
            heads[c] = new Node(columns[c][0]);
            Node curr = heads[c];
            for (int r = 1; r < columns[c].length; r++) {
                curr.bottom = new Node(columns[c][r]);
                curr = curr.bottom;
            }
        }
        for (int i = 0; i < heads.length - 1; i++) {
            heads[i].next = heads[i + 1];
        }
        return heads[0];
    }

    static String toBottomString(Node head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) {
            sb.append(head.data);
            if (head.bottom != null) sb.append(", ");
            head = head.bottom;
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Flattening a Linked List ===\n");

        int[][] columns = {{5,7,8,30}, {10,20}, {19,22,50}, {28,35,40,45}};
        String expected = "[5, 7, 8, 10, 19, 20, 22, 28, 30, 35, 40, 45, 50]";

        System.out.println("Expected: " + expected);
        System.out.println("Brute:    " + toBottomString(new BruteForce().flatten(buildFlatList(columns))));
        System.out.println("Optimal:  " + toBottomString(new Optimal().flatten(buildFlatList(columns))));
        System.out.println("Best:     " + toBottomString(new Best().flatten(buildFlatList(columns))));
        System.out.println();

        // Test 2: Single column
        int[][] single = {{3, 6, 9}};
        System.out.println("Test 2: Single column [3,6,9]");
        System.out.println("  Brute:   " + toBottomString(new BruteForce().flatten(buildFlatList(single))));
        System.out.println("  Optimal: " + toBottomString(new Optimal().flatten(buildFlatList(single))));
        System.out.println("  Best:    " + toBottomString(new Best().flatten(buildFlatList(single))));
        System.out.println();

        // Test 3: Empty
        System.out.println("Test 3: Empty");
        System.out.println("  Brute:   " + toBottomString(new BruteForce().flatten(null)));
        System.out.println("  Optimal: " + toBottomString(new Optimal().flatten(null)));
        System.out.println("  Best:    " + toBottomString(new Best().flatten(null)));
    }
}
