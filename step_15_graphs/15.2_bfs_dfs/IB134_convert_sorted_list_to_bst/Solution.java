import java.util.*;

/**
 * Problem: Convert Sorted List to BST
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Convert a sorted singly linked list to a height-balanced BST.
 * The middle element of the list becomes the root.
 *
 * @author DSA_Nova
 */

class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
}

class BSTNodeLL {
    int val;
    BSTNodeLL left, right;
    BSTNodeLL(int val) { this.val = val; }
}

// ============================================================
// APPROACH 1: BRUTE FORCE
// Convert list to array, then apply sorted array -> BST
// Time: O(N)  |  Space: O(N) for array
// ============================================================
class BruteForce {
    static BSTNodeLL sortedListToBST(ListNode head) {
        List<Integer> arr = new ArrayList<>();
        while (head != null) { arr.add(head.val); head = head.next; }
        return helper(arr, 0, arr.size() - 1);
    }

    static BSTNodeLL helper(List<Integer> arr, int left, int right) {
        if (left > right) return null;
        int mid = (left + right) / 2;
        BSTNodeLL node = new BSTNodeLL(arr.get(mid));
        node.left = helper(arr, left, mid - 1);
        node.right = helper(arr, mid + 1, right);
        return node;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Find middle using slow/fast pointers, recurse on halves
// Time: O(N log N)  |  Space: O(log N)
// ============================================================
class Optimal {
    static BSTNodeLL sortedListToBST(ListNode head) {
        if (head == null) return null;
        if (head.next == null) return new BSTNodeLL(head.val);

        // Find middle (and node before middle for disconnecting left half)
        ListNode prev = null, slow = head, fast = head;
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        // slow is now the middle
        if (prev != null) prev.next = null; // disconnect left half

        BSTNodeLL root = new BSTNodeLL(slow.val);
        root.left = (prev == null) ? null : sortedListToBST(head);
        root.right = sortedListToBST(slow.next);
        return root;
    }
}

// ============================================================
// APPROACH 3: BEST
// Inorder simulation — O(N) time by building BST during inorder traversal
// Uses a pointer that advances as the tree is built
// Time: O(N)  |  Space: O(log N)
// ============================================================
class Best {
    static ListNode curr;

    static int countList(ListNode head) {
        int count = 0;
        while (head != null) { count++; head = head.next; }
        return count;
    }

    static BSTNodeLL convert(int left, int right) {
        if (left > right) return null;
        int mid = (left + right) / 2;
        BSTNodeLL leftChild = convert(left, mid - 1);
        BSTNodeLL root = new BSTNodeLL(curr.val);
        curr = curr.next;
        root.left = leftChild;
        root.right = convert(mid + 1, right);
        return root;
    }

    static BSTNodeLL sortedListToBST(ListNode head) {
        curr = head;
        int n = countList(head);
        return convert(0, n - 1);
    }
}

public class Solution {
    static ListNode makeList(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    static List<Integer> inorder(BSTNodeLL root) {
        List<Integer> res = new ArrayList<>();
        Deque<BSTNodeLL> stack = new ArrayDeque<>();
        BSTNodeLL cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) { stack.push(cur); cur = cur.left; }
            cur = stack.pop(); res.add(cur.val); cur = cur.right;
        }
        return res;
    }

    static int height(BSTNodeLL root) {
        if (root == null) return 0;
        return 1 + Math.max(height(root.left), height(root.right));
    }

    public static void main(String[] args) {
        System.out.println("=== Convert Sorted List to BST ===");

        int[] vals = {-10, -3, 0, 5, 9};
        BSTNodeLL r1 = BruteForce.sortedListToBST(makeList(vals));
        BSTNodeLL r2 = Optimal.sortedListToBST(makeList(vals));
        BSTNodeLL r3 = Best.sortedListToBST(makeList(vals));

        System.out.println("BruteForce inorder: " + inorder(r1) + " height=" + height(r1));
        System.out.println("Optimal    inorder: " + inorder(r2) + " height=" + height(r2));
        System.out.println("Best       inorder: " + inorder(r3) + " height=" + height(r3));

        System.out.println("Single: " + inorder(Best.sortedListToBST(makeList(1))));
        System.out.println("Two:    " + inorder(Best.sortedListToBST(makeList(1, 2))));
    }
}
