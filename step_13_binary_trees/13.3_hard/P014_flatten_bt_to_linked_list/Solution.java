/**
 * Problem: Flatten Binary Tree to Linked List (LeetCode 114)
 * Difficulty: MEDIUM | XP: 25
 *
 * Flatten a binary tree in-place into a right-only "linked list"
 * following preorder traversal order. Left pointers must be null.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ------------------------------------------------------------------
    // Shared TreeNode definition
    // ------------------------------------------------------------------
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
        TreeNode(int v, TreeNode l, TreeNode r) { val = v; left = l; right = r; }
    }

    // Helper: build tree from level-order array (null = missing child)
    static TreeNode build(Integer[] vals) {
        if (vals == null || vals.length == 0) return null;
        TreeNode root = new TreeNode(vals[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < vals.length) {
            TreeNode node = q.poll();
            if (i < vals.length && vals[i] != null) { node.left = new TreeNode(vals[i]); q.offer(node.left); }
            i++;
            if (i < vals.length && vals[i] != null) { node.right = new TreeNode(vals[i]); q.offer(node.right); }
            i++;
        }
        return root;
    }

    // Helper: collect flattened chain into list for display
    static List<Integer> toList(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        for (TreeNode n = root; n != null; n = n.right) res.add(n.val);
        return res;
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Preorder to array, then relink
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        /**
         * Collect all nodes in preorder into a list, then wire each node's
         * right pointer to the next node and null out all left pointers.
         * Simple and readable but uses O(n) extra space for the node list.
         */
        List<TreeNode> nodes = new ArrayList<>();

        void preorder(TreeNode node) {
            if (node == null) return;
            nodes.add(node);
            preorder(node.left);
            preorder(node.right);
        }

        void flatten(TreeNode root) {
            preorder(root);
            for (int i = 0; i < nodes.size() - 1; i++) {
                nodes.get(i).left = null;
                nodes.get(i).right = nodes.get(i + 1);
            }
            if (!nodes.isEmpty()) {
                nodes.get(nodes.size() - 1).left = null;
                nodes.get(nodes.size() - 1).right = null;
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Recursive reverse-postorder
    // Time: O(n)  |  Space: O(h) call stack
    // ============================================================
    static class Optimal {
        /**
         * Process nodes in reverse preorder: right → left → root.
         * A 'prev' pointer is maintained. After processing both subtrees,
         * set node.right = prev and node.left = null, then update prev = node.
         * When we reach the root, the entire tree is already flattened on the right.
         */
        TreeNode prev = null;

        void flatten(TreeNode root) {
            if (root == null) return;
            flatten(root.right);
            flatten(root.left);
            root.right = prev;
            root.left = null;
            prev = root;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Morris-style iterative O(1) space
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * At each node that has a left child:
         *   1. Find the rightmost node of the left subtree (the preorder predecessor).
         *   2. Thread the current right subtree onto that predecessor's right.
         *   3. Move the left subtree to the right, null out left.
         * Advance to curr.right. No stack or recursion needed.
         * This mirrors Morris traversal and is truly O(1) extra space.
         */
        void flatten(TreeNode root) {
            TreeNode curr = root;
            while (curr != null) {
                if (curr.left != null) {
                    // Find rightmost of left subtree
                    TreeNode pred = curr.left;
                    while (pred.right != null) pred = pred.right;
                    // Thread right subtree
                    pred.right = curr.right;
                    // Shift left to right
                    curr.right = curr.left;
                    curr.left = null;
                }
                curr = curr.right;
            }
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Flatten Binary Tree to Linked List ===\n");
        // Tree: 1->2->5, 2->3->4, 5->6
        // Expected: [1, 2, 3, 4, 5, 6]
        Integer[] vals = {1, 2, 5, 3, 4, null, 6};

        TreeNode r1 = build(vals);
        new BruteForce().flatten(r1);
        System.out.println("Brute:         " + toList(r1));

        TreeNode r2 = build(vals);
        new Optimal().flatten(r2);
        System.out.println("Optimal:       " + toList(r2));

        TreeNode r3 = build(vals);
        new Best().flatten(r3);
        System.out.println("Best (Morris): " + toList(r3));
    }
}
