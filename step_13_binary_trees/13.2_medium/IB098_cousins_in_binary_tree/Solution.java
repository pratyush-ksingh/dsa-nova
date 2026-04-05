import java.util.*;

/**
 * Cousins in Binary Tree (LeetCode #993)
 *
 * Two nodes are cousins if they have the same depth but different parents.
 */
public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Two separate DFS calls
    // Time: O(N)  |  Space: O(H)
    // ============================================================
    static int depthX, depthY;
    static TreeNode parentX, parentY;

    public static boolean bruteForce(TreeNode root, int x, int y) {
        depthX = depthY = -1;
        parentX = parentY = null;
        findNode(root, null, 0, x, true);
        findNode(root, null, 0, y, false);
        return depthX == depthY && parentX != parentY;
    }

    private static void findNode(TreeNode node, TreeNode parent, int depth, int target, boolean isX) {
        if (node == null) return;
        if (node.val == target) {
            if (isX) { depthX = depth; parentX = parent; }
            else { depthY = depth; parentY = parent; }
            return;
        }
        findNode(node.left, node, depth + 1, target, isX);
        findNode(node.right, node, depth + 1, target, isX);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Single DFS for both nodes
    // Time: O(N)  |  Space: O(H)
    // ============================================================
    public static boolean optimal(TreeNode root, int x, int y) {
        int[] xInfo = new int[]{-1, -1}; // {depth, parent_val}
        int[] yInfo = new int[]{-1, -1};
        dfs(root, -1, 0, x, y, xInfo, yInfo);
        return xInfo[0] == yInfo[0] && xInfo[1] != yInfo[1];
    }

    private static void dfs(TreeNode node, int parentVal, int depth,
                            int x, int y, int[] xInfo, int[] yInfo) {
        if (node == null) return;
        if (node.val == x) { xInfo[0] = depth; xInfo[1] = parentVal; }
        if (node.val == y) { yInfo[0] = depth; yInfo[1] = parentVal; }
        dfs(node.left, node.val, depth + 1, x, y, xInfo, yInfo);
        dfs(node.right, node.val, depth + 1, x, y, xInfo, yInfo);
    }

    // ============================================================
    // APPROACH 3: BEST -- BFS level-order with sibling check
    // Time: O(N)  |  Space: O(W)
    // ============================================================
    public static boolean best(TreeNode root, int x, int y) {
        if (root == null) return false;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean foundX = false, foundY = false;

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();

                // Sibling check: if both x and y are children of same node
                if (node.left != null && node.right != null) {
                    if ((node.left.val == x && node.right.val == y) ||
                        (node.left.val == y && node.right.val == x)) {
                        return false; // siblings, not cousins
                    }
                }

                if (node.val == x) foundX = true;
                if (node.val == y) foundY = true;

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            if (foundX && foundY) return true;   // same level, not siblings
            if (foundX || foundY) return false;   // different levels
        }
        return false;
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        //      1
        //     / \
        //    2   3
        //     \   \
        //      4   5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.right = new TreeNode(4);
        root.right.right = new TreeNode(5);

        System.out.println("=== Cousins in Binary Tree ===");
        System.out.println("Brute (4,5):   " + bruteForce(root, 4, 5));  // true
        System.out.println("Optimal (4,5): " + optimal(root, 4, 5));      // true
        System.out.println("Best (4,5):    " + best(root, 4, 5));          // true

        System.out.println("Brute (2,3):   " + bruteForce(root, 2, 3));  // false (siblings)
        System.out.println("Optimal (2,3): " + optimal(root, 2, 3));      // false
        System.out.println("Best (2,3):    " + best(root, 2, 3));          // false
    }
}
