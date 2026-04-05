/**
 * Problem: Kth Smallest in BST
 * Difficulty: MEDIUM | XP: 25
 * LeetCode 230
 *
 * Given the root of a BST and integer k, return the kth smallest value
 * (1-indexed) among all node values in the tree.
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
    // APPROACH 1: BRUTE FORCE
    // Time: O(n)  |  Space: O(n)
    // Collect full inorder into list, return element at index k-1.
    // ============================================================
    static class BruteForce {
        public int kthSmallest(TreeNode root, int k) {
            List<Integer> sorted = new ArrayList<>();
            inorder(root, sorted);
            return sorted.get(k - 1);
        }

        private void inorder(TreeNode node, List<Integer> list) {
            if (node == null) return;
            inorder(node.left, list);
            list.add(node.val);
            inorder(node.right, list);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(k + h)  |  Space: O(h)
    // Iterative inorder; early exit after visiting k nodes.
    // ============================================================
    static class Optimal {
        public int kthSmallest(TreeNode root, int k) {
            Deque<TreeNode> stack = new ArrayDeque<>();
            int count = 0;
            TreeNode curr = root;

            while (curr != null || !stack.isEmpty()) {
                // Descend as far left as possible
                while (curr != null) {
                    stack.push(curr);
                    curr = curr.left;
                }

                // Process node
                curr = stack.pop();
                count++;
                if (count == k) return curr.val;

                // Move to right subtree
                curr = curr.right;
            }

            return -1; // unreachable for valid input
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n)  |  Space: O(1)
    // Morris Inorder Traversal — no stack, no recursion overhead.
    // Uses temporary threads (right-pointer of predecessor) to navigate.
    // ============================================================
    static class Best {
        public int kthSmallest(TreeNode root, int k) {
            int count = 0;
            TreeNode curr = root;

            while (curr != null) {
                if (curr.left == null) {
                    // Visit this node
                    count++;
                    if (count == k) return curr.val;
                    curr = curr.right;
                } else {
                    // Find inorder predecessor
                    TreeNode pred = curr.left;
                    while (pred.right != null && pred.right != curr) {
                        pred = pred.right;
                    }

                    if (pred.right == null) {
                        // Create thread, go left
                        pred.right = curr;
                        curr = curr.left;
                    } else {
                        // Remove thread, visit curr, go right
                        pred.right = null;
                        count++;
                        if (count == k) return curr.val;
                        curr = curr.right;
                    }
                }
            }

            return -1; // unreachable for valid input
        }
    }

    // ----------------------------------------------------------------
    // Test helpers
    // ----------------------------------------------------------------
    static TreeNode buildFromLevelOrder(Integer[] vals) {
        if (vals == null || vals.length == 0) return null;
        TreeNode[] nodes = new TreeNode[vals.length];
        for (int i = 0; i < vals.length; i++)
            if (vals[i] != null) nodes[i] = new TreeNode(vals[i]);
        for (int i = 0; i < vals.length; i++) {
            if (nodes[i] == null) continue;
            int l = 2 * i + 1, r = 2 * i + 2;
            if (l < vals.length) nodes[i].left  = nodes[l];
            if (r < vals.length) nodes[i].right = nodes[r];
        }
        return nodes[0];
    }

    public static void main(String[] args) {
        System.out.println("=== Kth Smallest in BST ===");

        // BST:   3
        //       / \
        //      1   4
        //       \
        //        2
        // Sorted: 1, 2, 3, 4
        Integer[] vals1 = {3, 1, 4, null, 2};

        BruteForce bf = new BruteForce();
        Optimal    op = new Optimal();
        Best       be = new Best();

        for (int k = 1; k <= 4; k++) {
            System.out.printf("k=%d  Brute=%d  Optimal=%d  Best=%d%n",
                k,
                bf.kthSmallest(buildFromLevelOrder(vals1), k),
                op.kthSmallest(buildFromLevelOrder(vals1), k),
                be.kthSmallest(buildFromLevelOrder(vals1), k));
        }

        // BST: 5,3,6,2,4,null,null,1 — sorted: 1,2,3,4,5,6
        Integer[] vals2 = {5, 3, 6, 2, 4, null, null, 1};
        System.out.println();
        System.out.println("Larger BST, k=3: Brute="   + bf.kthSmallest(buildFromLevelOrder(vals2), 3));
        System.out.println("Larger BST, k=3: Optimal=" + op.kthSmallest(buildFromLevelOrder(vals2), 3));
        System.out.println("Larger BST, k=3: Best="    + be.kthSmallest(buildFromLevelOrder(vals2), 3));
    }
}
