/**
 * Problem: Children Sum Property
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a binary tree, convert it so every node's value equals the sum of
 * its children's values. You may ONLY increase node values (never decrease).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ----------------------------------------------------------------
    // Tree Node definition
    // ----------------------------------------------------------------
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
    // Time: O(n^2)  |  Space: O(h)
    // Validate only: recursively check every node satisfies the property.
    // ============================================================
    static class BruteForce {
        public boolean childrenSumProperty(TreeNode root) {
            return check(root);
        }

        private boolean check(TreeNode node) {
            if (node == null) return true;
            if (node.left == null && node.right == null) return true; // leaf

            int childSum = 0;
            if (node.left  != null) childSum += node.left.val;
            if (node.right != null) childSum += node.right.val;

            if (node.val != childSum) return false;
            return check(node.left) && check(node.right);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(n)  |  Space: O(h)
    // Single DFS: going DOWN inflate children to match parent;
    // coming back UP set parent = actual sum of children.
    // ============================================================
    static class Optimal {
        public void changeTree(TreeNode root) {
            dfs(root);
        }

        private void dfs(TreeNode node) {
            if (node == null) return;
            if (node.left == null && node.right == null) return; // leaf

            // Current child sum
            int childSum = 0;
            if (node.left  != null) childSum += node.left.val;
            if (node.right != null) childSum += node.right.val;

            // Going DOWN: if children are smaller, inflate left child
            if (childSum < node.val) {
                if (node.left  != null) node.left.val  = node.val;
                else                    node.right.val = node.val;
            }

            dfs(node.left);
            dfs(node.right);

            // Going UP: fix current node = sum of actual children
            int total = 0;
            if (node.left  != null) total += node.left.val;
            if (node.right != null) total += node.right.val;
            node.val = total;
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n)  |  Space: O(h)
    // Identical complexity to Optimal - this IS the optimal approach.
    // Rewritten with clearer deficit-propagation for interviews.
    // ============================================================
    static class Best {
        public void changeTree(TreeNode root) {
            dfs(root);
        }

        private void dfs(TreeNode node) {
            if (node == null || (node.left == null && node.right == null)) return;

            int leftVal  = (node.left  != null) ? node.left.val  : 0;
            int rightVal = (node.right != null) ? node.right.val : 0;
            int childSum = leftVal + rightVal;

            // Propagate deficit downward so children are at least as large as parent
            if (childSum < node.val) {
                int diff = node.val - childSum;
                if (node.left  != null) node.left.val  += diff;
                else                    node.right.val += diff;
            }

            dfs(node.left);
            dfs(node.right);

            // After children are fully resolved, pull their sum up
            int total = 0;
            if (node.left  != null) total += node.left.val;
            if (node.right != null) total += node.right.val;
            node.val = total;
        }
    }

    // ----------------------------------------------------------------
    // Test driver
    // ----------------------------------------------------------------
    static TreeNode build(Integer[] vals) {
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

    static String levelOrder(TreeNode root) {
        if (root == null) return "[]";
        java.util.Queue<TreeNode> q = new java.util.LinkedList<>();
        q.add(root);
        StringBuilder sb = new StringBuilder("[");
        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            sb.append(n.val).append(", ");
            if (n.left  != null) q.add(n.left);
            if (n.right != null) q.add(n.right);
        }
        sb.setLength(sb.length() - 2);
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Children Sum Property ===");

        // Brute: check validity
        TreeNode t1 = build(new Integer[]{10, 8, 2, 3, 5, 1, 1});
        System.out.println("Brute (valid tree):        " + new BruteForce().childrenSumProperty(t1)); // true

        TreeNode t2 = build(new Integer[]{1, 2, 3});
        System.out.println("Brute (invalid tree):      " + new BruteForce().childrenSumProperty(t2)); // false

        // Optimal: convert
        TreeNode t3 = build(new Integer[]{2, 35, 10, 2, 3, 5, 2});
        new Optimal().changeTree(t3);
        System.out.println("Optimal (converted):       " + levelOrder(t3));
        System.out.println("  Valid after conversion:  " + new BruteForce().childrenSumProperty(t3));

        // Best: convert
        TreeNode t4 = build(new Integer[]{2, 35, 10, 2, 3, 5, 2});
        new Best().changeTree(t4);
        System.out.println("Best (converted):          " + levelOrder(t4));
        System.out.println("  Valid after conversion:  " + new BruteForce().childrenSumProperty(t4));
    }
}
