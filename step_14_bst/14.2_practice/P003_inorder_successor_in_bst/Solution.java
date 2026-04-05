/**
 * Problem: Inorder Successor in BST
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Full Inorder Traversal
    // Time: O(n)  |  Space: O(n) for storing traversal
    // Do complete inorder, find target, return next element.
    // ============================================================
    public static TreeNode bruteForce(TreeNode root, TreeNode target) {
        if (root == null || target == null) return null;

        List<TreeNode> inorder = new ArrayList<>();
        inorderTraversal(root, inorder);

        for (int i = 0; i < inorder.size(); i++) {
            if (inorder.get(i) == target && i + 1 < inorder.size()) {
                return inorder.get(i + 1);
            }
        }
        return null;
    }

    private static void inorderTraversal(TreeNode node, List<TreeNode> list) {
        if (node == null) return;
        inorderTraversal(node.left, list);
        list.add(node);
        inorderTraversal(node.right, list);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- BST Property Traversal
    // Time: O(h)  |  Space: O(1)
    // If target has right subtree, go right then all the way left.
    // Else, track last ancestor where we went left.
    // ============================================================
    public static TreeNode optimal(TreeNode root, TreeNode target) {
        if (root == null || target == null) return null;

        // Case 1: target has right subtree
        if (target.right != null) {
            TreeNode node = target.right;
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }

        // Case 2: walk from root, track last left-turn ancestor
        TreeNode successor = null;
        TreeNode curr = root;
        while (curr != null) {
            if (target.val < curr.val) {
                successor = curr;
                curr = curr.left;
            } else if (target.val > curr.val) {
                curr = curr.right;
            } else {
                break;  // Found target
            }
        }

        return successor;
    }

    // ============================================================
    // APPROACH 3: BEST -- BST Search (clean single-pass)
    // Time: O(h)  |  Space: O(1)
    // Find smallest value greater than target.val by BST walk.
    // ============================================================
    public static TreeNode best(TreeNode root, TreeNode target) {
        if (root == null || target == null) return null;

        TreeNode successor = null;
        TreeNode curr = root;

        while (curr != null) {
            if (curr.val > target.val) {
                successor = curr;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        return successor;
    }

    public static void main(String[] args) {
        System.out.println("=== Inorder Successor in BST ===");

        //       20
        //      /  \
        //     8    22
        //    / \
        //   4  12
        //     /  \
        //    10  14
        TreeNode n4 = new TreeNode(4);
        TreeNode n10 = new TreeNode(10);
        TreeNode n14 = new TreeNode(14);
        TreeNode n12 = new TreeNode(12, n10, n14);
        TreeNode n8 = new TreeNode(8, n4, n12);
        TreeNode n22 = new TreeNode(22);
        TreeNode root = new TreeNode(20, n8, n22);

        // Successor of 8 -> 10
        TreeNode r1 = bruteForce(root, n8);
        System.out.println("Brute (8):   " + (r1 != null ? r1.val : "null"));  // 10

        TreeNode r2 = optimal(root, n8);
        System.out.println("Optimal (8): " + (r2 != null ? r2.val : "null"));  // 10

        TreeNode r3 = best(root, n8);
        System.out.println("Best (8):    " + (r3 != null ? r3.val : "null"));  // 10

        // Successor of 14 -> 20
        TreeNode r4 = optimal(root, n14);
        System.out.println("Optimal (14): " + (r4 != null ? r4.val : "null")); // 20

        // Successor of 22 -> null (largest)
        TreeNode r5 = optimal(root, n22);
        System.out.println("Optimal (22): " + (r5 != null ? r5.val : "null")); // null
    }
}
