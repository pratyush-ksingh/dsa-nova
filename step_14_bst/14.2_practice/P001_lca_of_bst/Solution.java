/**
 * Problem: LCA of BST (LeetCode #235)
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
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- General Binary Tree LCA
    // Ignore BST property. Postorder search both subtrees.
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    static class BruteForce {
        public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            if (root == null || root.val == p.val || root.val == q.val) return root;
            TreeNode left = lowestCommonAncestor(root.left, p, q);
            TreeNode right = lowestCommonAncestor(root.right, p, q);
            if (left != null && right != null) return root;
            return left != null ? left : right;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- BST-Guided Recursive
    // Use BST ordering: if both < node go left, both > go right,
    // else this is the split point = LCA.
    // Time: O(h)  |  Space: O(h)
    // ============================================================
    static class OptimalRecursive {
        public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            if (p.val < root.val && q.val < root.val) {
                return lowestCommonAncestor(root.left, p, q);
            }
            if (p.val > root.val && q.val > root.val) {
                return lowestCommonAncestor(root.right, p, q);
            }
            return root; // Split point
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- BST-Guided Iterative
    // Same logic as recursive, but iterative for O(1) space.
    // Time: O(h)  |  Space: O(1)
    // ============================================================
    static class BestIterative {
        public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            TreeNode node = root;
            while (node != null) {
                if (p.val < node.val && q.val < node.val) {
                    node = node.left;
                } else if (p.val > node.val && q.val > node.val) {
                    node = node.right;
                } else {
                    return node; // Split point
                }
            }
            return null; // Should not reach if p and q exist
        }
    }

    // Helper: build BST from level-order array
    public static TreeNode buildTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) return null;
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty() && i < arr.length) {
            TreeNode curr = queue.poll();
            if (i < arr.length && arr[i] != null) {
                curr.left = new TreeNode(arr[i]);
                queue.offer(curr.left);
            }
            i++;
            if (i < arr.length && arr[i] != null) {
                curr.right = new TreeNode(arr[i]);
                queue.offer(curr.right);
            }
            i++;
        }
        return root;
    }

    // Helper: find node by value
    public static TreeNode findNode(TreeNode root, int val) {
        if (root == null) return null;
        if (root.val == val) return root;
        TreeNode left = findNode(root.left, val);
        return left != null ? left : findNode(root.right, val);
    }

    public static void main(String[] args) {
        System.out.println("=== LCA of BST ===\n");

        Integer[] bstArr = {6, 2, 8, 0, 4, 7, 9, null, null, 3, 5};

        int[][] queries = {
            {2, 8, 6},
            {2, 4, 2},
            {0, 5, 2},
            {3, 5, 4},
            {7, 9, 8},
            {0, 9, 6},
        };

        for (int[] query : queries) {
            int pVal = query[0], qVal = query[1], expected = query[2];
            TreeNode root = buildTree(bstArr);
            TreeNode p = findNode(root, pVal);
            TreeNode q = findNode(root, qVal);

            TreeNode brute = BruteForce.lowestCommonAncestor(root, p, q);
            // Rebuild for each approach to ensure clean state
            root = buildTree(bstArr);
            p = findNode(root, pVal);
            q = findNode(root, qVal);
            TreeNode optimal = OptimalRecursive.lowestCommonAncestor(root, p, q);

            root = buildTree(bstArr);
            p = findNode(root, pVal);
            q = findNode(root, qVal);
            TreeNode best = BestIterative.lowestCommonAncestor(root, p, q);

            boolean pass = brute.val == expected && optimal.val == expected && best.val == expected;
            String status = pass ? "PASS" : "FAIL";
            System.out.printf("p=%d, q=%d -> LCA expected=%d%n", pVal, qVal, expected);
            System.out.printf("  Brute: %d, Optimal: %d, Best: %d %s%n%n",
                    brute.val, optimal.val, best.val, status);
        }

        // Edge case: two-node tree
        TreeNode small = buildTree(new Integer[]{2, 1});
        TreeNode p = findNode(small, 2);
        TreeNode q = findNode(small, 1);
        TreeNode lca = BestIterative.lowestCommonAncestor(small, p, q);
        System.out.printf("Two-node tree [2,1]: p=2, q=1 -> LCA=%d (expected 2) %s%n",
                lca.val, lca.val == 2 ? "PASS" : "FAIL");
    }
}
