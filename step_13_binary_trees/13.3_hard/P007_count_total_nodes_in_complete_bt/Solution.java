/**
 * Problem: Count Total Nodes in Complete Binary Tree
 * Difficulty: MEDIUM | XP: 25
 *
 * Given the root of a complete binary tree, count the number of nodes.
 * A complete binary tree: all levels fully filled except possibly the last,
 * which is filled from left to right.
 *
 * Naive: O(N). Optimal exploits CBT structure for O(log^2 N).
 *
 * @author DSA_Nova
 */
public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode l, TreeNode r) { this.val = val; left = l; right = r; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Simple recursive DFS count
    // Time: O(N)  |  Space: O(H) recursion
    // Visit every node; does not exploit complete tree property
    // ============================================================
    public static int bruteForce(TreeNode root) {
        if (root == null) return 0;
        return 1 + bruteForce(root.left) + bruteForce(root.right);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Exploit complete binary tree property
    // Time: O(log^2 N)  |  Space: O(log N) recursion
    // Compute left height (go left) and right height (go right).
    // If equal -> perfect subtree: count = 2^h - 1 (use bit shift).
    // Otherwise recurse on left and right subtrees.
    // ============================================================
    public static int optimal(TreeNode root) {
        if (root == null) return 0;
        int lh = leftHeight(root);
        int rh = rightHeight(root);
        if (lh == rh) {
            // Perfect binary tree: 2^lh - 1 nodes
            return (1 << lh) - 1;
        }
        return 1 + optimal(root.left) + optimal(root.right);
    }

    private static int leftHeight(TreeNode node) {
        int h = 0;
        while (node != null) { h++; node = node.left; }
        return h;
    }

    private static int rightHeight(TreeNode node) {
        int h = 0;
        while (node != null) { h++; node = node.right; }
        return h;
    }

    // ============================================================
    // APPROACH 3: BEST - Binary search on last level position
    // Time: O(log^2 N)  |  Space: O(log N)
    // Height h; last level can have 1..2^(h-1) nodes.
    // Binary search: does node at position mid exist?
    // Check existence by following bits of mid from root.
    // ============================================================
    public static int best(TreeNode root) {
        if (root == null) return 0;
        int h = 0;
        TreeNode cur = root;
        while (cur.left != null) { h++; cur = cur.left; }
        // h = height (0-indexed); last level has indices 0..2^h-1
        int lo = 0, hi = (1 << h) - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo + 1) / 2;
            if (nodeExists(root, h, mid)) lo = mid;
            else hi = mid - 1;
        }
        return (1 << h) + lo; // nodes in full levels (2^h - 1) + last level count (lo + 1)
    }

    private static boolean nodeExists(TreeNode root, int h, int idx) {
        // Navigate from root using bits of idx (MSB to LSB, h levels)
        int lo = 0, hi = (1 << h) - 1;
        TreeNode cur = root;
        for (int i = 0; i < h; i++) {
            int mid = lo + (hi - lo) / 2;
            if (idx <= mid) { cur = cur.left;  hi = mid; }
            else             { cur = cur.right; lo = mid + 1; }
            if (cur == null) return false;
        }
        return cur != null;
    }

    public static void main(String[] args) {
        System.out.println("=== Count Total Nodes in Complete BT ===");

        // Perfect tree of height 3: 7 nodes
        TreeNode root = new TreeNode(1,
            new TreeNode(2, new TreeNode(4), new TreeNode(5)),
            new TreeNode(3, new TreeNode(6), new TreeNode(7)));
        System.out.println("Perfect tree (7 nodes):");
        System.out.println("Brute:   " + bruteForce(root));   // 7
        System.out.println("Optimal: " + optimal(root));       // 7
        System.out.println("Best:    " + best(root));          // 7

        // 6-node complete tree
        TreeNode root2 = new TreeNode(1,
            new TreeNode(2, new TreeNode(4), new TreeNode(5)),
            new TreeNode(3, new TreeNode(6), null));
        System.out.println("\n6-node complete tree:");
        System.out.println("Brute:   " + bruteForce(root2));   // 6
        System.out.println("Optimal: " + optimal(root2));
        System.out.println("Best:    " + best(root2));

        // Single node
        TreeNode root3 = new TreeNode(1);
        System.out.println("\nSingle node:");
        System.out.println("Brute:   " + bruteForce(root3));   // 1
        System.out.println("Optimal: " + optimal(root3));
        System.out.println("Best:    " + best(root3));
    }
}
