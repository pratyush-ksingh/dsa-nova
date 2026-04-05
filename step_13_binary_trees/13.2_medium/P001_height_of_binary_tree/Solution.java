/**
 * Problem: Height of Binary Tree / Maximum Depth (LeetCode #104)
 * Difficulty: EASY | XP: 10
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
    // APPROACH 1 & 3: RECURSIVE DFS (Postorder)
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    public static int maxDepthRecursive(TreeNode root) {
        if (root == null) return 0;
        int leftDepth = maxDepthRecursive(root.left);
        int rightDepth = maxDepthRecursive(root.right);
        return 1 + Math.max(leftDepth, rightDepth);
    }

    // ============================================================
    // APPROACH 2: ITERATIVE BFS (Level-Order)
    // Time: O(n)  |  Space: O(w)
    // ============================================================
    public static int maxDepthBFS(TreeNode root) {
        if (root == null) return 0;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            depth++;
        }
        return depth;
    }

    // ============================================================
    // BONUS: Iterative DFS with explicit stack
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    public static int maxDepthIterativeDFS(TreeNode root) {
        if (root == null) return 0;

        Deque<Object[]> stack = new ArrayDeque<>();
        stack.push(new Object[]{root, 1});
        int maxDepth = 0;

        while (!stack.isEmpty()) {
            Object[] pair = stack.pop();
            TreeNode node = (TreeNode) pair[0];
            int depth = (int) pair[1];
            maxDepth = Math.max(maxDepth, depth);

            if (node.right != null) stack.push(new Object[]{node.right, depth + 1});
            if (node.left != null) stack.push(new Object[]{node.left, depth + 1});
        }
        return maxDepth;
    }

    // Helper: build tree from level-order array
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

    public static void main(String[] args) {
        System.out.println("=== Height of Binary Tree ===\n");

        Object[][] tests = {
            {new Integer[]{3, 9, 20, null, null, 15, 7}, 3},
            {new Integer[]{1, null, 2}, 2},
            {new Integer[]{}, 0},
            {new Integer[]{1}, 1},
            {new Integer[]{1, 2, 3, 4, null, null, null, 5}, 4},
        };

        for (Object[] test : tests) {
            Integer[] arr = (Integer[]) test[0];
            int expected = (int) test[1];
            TreeNode root = buildTree(arr);

            int rec = maxDepthRecursive(root);
            int bfs = maxDepthBFS(root);
            int dfs = maxDepthIterativeDFS(root);

            String status = (rec == expected && bfs == expected && dfs == expected) ? "PASS" : "FAIL";
            System.out.printf("Tree: %s%n", Arrays.toString(arr));
            System.out.printf("  Recursive: %d, BFS: %d, Iterative DFS: %d (expected %d) %s%n%n",
                    rec, bfs, dfs, expected, status);
        }
    }
}
