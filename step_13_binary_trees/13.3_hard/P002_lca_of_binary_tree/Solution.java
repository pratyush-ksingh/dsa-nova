/**
 * Problem: LCA of Binary Tree (LeetCode 236)
 * Difficulty: HARD | XP: 25
 *
 * Given the root of a binary tree and two nodes p and q, find their
 * Lowest Common Ancestor (LCA). The LCA is the deepest node that is
 * an ancestor of both p and q. A node can be an ancestor of itself.
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
    // APPROACH 1: BRUTE FORCE — Find paths, then compare
    // Time: O(n)  |  Space: O(n) for path storage
    // ============================================================
    /**
     * Find the path (list of nodes) from root to p, and from root to q.
     * Walk both paths together; the last node they share before diverging is the LCA.
     */
    public static TreeNode bruteForce(TreeNode root, TreeNode p, TreeNode q) {
        List<TreeNode> pathP = new ArrayList<>();
        List<TreeNode> pathQ = new ArrayList<>();
        findPath(root, p, pathP);
        findPath(root, q, pathQ);

        TreeNode lca = null;
        int minLen = Math.min(pathP.size(), pathQ.size());
        for (int i = 0; i < minLen; i++) {
            if (pathP.get(i) == pathQ.get(i)) {
                lca = pathP.get(i);
            } else {
                break;
            }
        }
        return lca;
    }

    private static boolean findPath(TreeNode root, TreeNode target, List<TreeNode> path) {
        if (root == null) return false;
        path.add(root);
        if (root == target) return true;
        if (findPath(root.left, target, path) || findPath(root.right, target, path)) return true;
        path.remove(path.size() - 1);
        return false;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Single recursive pass (elegant)
    // Time: O(n)  |  Space: O(h) recursion stack
    // ============================================================
    /**
     * Post-order recursion:
     * - Base: if node is null/p/q, return node.
     * - Recurse left and right.
     * - If both subtrees return non-null: this node is the LCA.
     * - If only one subtree returns non-null: bubble it up (both targets are in same subtree,
     *   or this is one of the targets and its match is above us).
     * - If both null: neither target in this subtree.
     */
    public static TreeNode optimal(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;

        TreeNode left  = optimal(root.left,  p, q);
        TreeNode right = optimal(root.right, p, q);

        if (left != null && right != null) return root; // p and q on opposite sides
        return left != null ? left : right;             // both in same subtree
    }

    // ============================================================
    // APPROACH 3: BEST — Iterative with parent-pointer map
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Iterative DFS to build a parent-pointer map for all nodes.
     * Collect all ancestors of p into a set.
     * Walk up from q using parent pointers until reaching a node in p's ancestor set.
     * That node is the LCA. Avoids recursion stack overflow for very deep trees.
     */
    public static TreeNode best(TreeNode root, TreeNode p, TreeNode q) {
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        parent.put(root, null);

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        // Build parent map until both p and q are discovered
        while (!parent.containsKey(p) || !parent.containsKey(q)) {
            TreeNode node = stack.pop();
            if (node.left != null) {
                parent.put(node.left, node);
                stack.push(node.left);
            }
            if (node.right != null) {
                parent.put(node.right, node);
                stack.push(node.right);
            }
        }

        // Collect all ancestors of p
        Set<TreeNode> ancestors = new HashSet<>();
        TreeNode cur = p;
        while (cur != null) {
            ancestors.add(cur);
            cur = parent.get(cur);
        }

        // Walk up from q to find first ancestor in p's set
        cur = q;
        while (!ancestors.contains(cur)) {
            cur = parent.get(cur);
        }
        return cur;
    }

    // Helper: build tree from level-order array
    private static Map<Integer, TreeNode> nodeMap = new HashMap<>();
    private static TreeNode buildTree(Integer[] vals) {
        nodeMap.clear();
        if (vals == null || vals.length == 0 || vals[0] == null) return null;
        TreeNode root = new TreeNode(vals[0]);
        nodeMap.put(vals[0], root);
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < vals.length) {
            TreeNode node = q.poll();
            if (i < vals.length && vals[i] != null) {
                node.left = new TreeNode(vals[i]);
                nodeMap.put(vals[i], node.left);
                q.offer(node.left);
            }
            i++;
            if (i < vals.length && vals[i] != null) {
                node.right = new TreeNode(vals[i]);
                nodeMap.put(vals[i], node.right);
                q.offer(node.right);
            }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== LCA of Binary Tree ===");

        //       3
        //      / \
        //     5   1
        //    / \ / \
        //   6  2 0  8
        //     / \
        //    7   4
        TreeNode root = buildTree(new Integer[]{3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});

        int[][] tests = {{5, 1}, {5, 4}, {3, 5}};
        int[] expected = {3, 5, 3};

        for (int i = 0; i < tests.length; i++) {
            TreeNode p = nodeMap.get(tests[i][0]);
            TreeNode q = nodeMap.get(tests[i][1]);
            System.out.println("LCA(" + tests[i][0] + "," + tests[i][1] + "):");
            System.out.println("  Brute:   " + bruteForce(root, p, q).val + " (expected " + expected[i] + ")");
            System.out.println("  Optimal: " + optimal(root, p, q).val);
            System.out.println("  Best:    " + best(root, p, q).val);
        }
    }
}
