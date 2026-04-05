import java.util.*;

/**
 * Sum Root to Leaf Numbers (LeetCode #129)
 *
 * Each root-to-leaf path forms a number. Return the total sum of all numbers.
 */
public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Collect all paths as strings
    // Time: O(N * H)  |  Space: O(N * H)
    // ============================================================
    public static int bruteForce(TreeNode root) {
        if (root == null) return 0;

        List<String> paths = new ArrayList<>();
        collectPaths(root, "", paths);

        int total = 0;
        for (String path : paths) {
            total += Integer.parseInt(path);
        }
        return total;
    }

    private static void collectPaths(TreeNode node, String path, List<String> paths) {
        if (node == null) return;
        path += node.val;
        if (node.left == null && node.right == null) {
            paths.add(path);
            return;
        }
        collectPaths(node.left, path, paths);
        collectPaths(node.right, path, paths);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Top-down DFS with running number
    // Time: O(N)  |  Space: O(H)
    // ============================================================
    public static int optimal(TreeNode root) {
        return dfs(root, 0);
    }

    private static int dfs(TreeNode node, int currentNum) {
        if (node == null) return 0;

        currentNum = currentNum * 10 + node.val;

        // Leaf node: return the complete number
        if (node.left == null && node.right == null) {
            return currentNum;
        }

        // Internal node: sum of left and right subtree numbers
        return dfs(node.left, currentNum) + dfs(node.right, currentNum);
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative BFS with running numbers
    // Time: O(N)  |  Space: O(W)
    // ============================================================
    public static int best(TreeNode root) {
        if (root == null) return 0;

        int totalSum = 0;
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        Queue<Integer> numQueue = new LinkedList<>();
        nodeQueue.offer(root);
        numQueue.offer(root.val);

        while (!nodeQueue.isEmpty()) {
            TreeNode node = nodeQueue.poll();
            int num = numQueue.poll();

            if (node.left == null && node.right == null) {
                totalSum += num;
                continue;
            }

            if (node.left != null) {
                nodeQueue.offer(node.left);
                numQueue.offer(num * 10 + node.left.val);
            }
            if (node.right != null) {
                nodeQueue.offer(node.right);
                numQueue.offer(num * 10 + node.right.val);
            }
        }

        return totalSum;
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        //      4
        //     / \
        //    9   0
        //   / \
        //  5   1
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(9);
        root.right = new TreeNode(0);
        root.left.left = new TreeNode(5);
        root.left.right = new TreeNode(1);

        System.out.println("=== Sum Root to Leaf Numbers ===");
        System.out.println("Brute:   " + bruteForce(root));  // 1026
        System.out.println("Optimal: " + optimal(root));      // 1026
        System.out.println("Best:    " + best(root));          // 1026
        // Paths: 495 + 491 + 40 = 1026
    }
}
