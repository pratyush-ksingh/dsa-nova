/**
 * Problem: Min Depth of Binary Tree (LeetCode #111)
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
    // APPROACH 1: RECURSIVE DFS
    // Handle three cases: no left, no right, both exist.
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    static class RecursiveDFS {
        public static int minDepth(TreeNode root) {
            if (root == null) return 0;
            // If no left child, must go right
            if (root.left == null) return 1 + minDepth(root.right);
            // If no right child, must go left
            if (root.right == null) return 1 + minDepth(root.left);
            // Both children exist: take the shorter path
            return 1 + Math.min(minDepth(root.left), minDepth(root.right));
        }
    }

    // ============================================================
    // APPROACH 2 & 3: BFS with Early Termination
    // First leaf encountered = minimum depth. Stops early.
    // Time: O(n) worst, O(k) best  |  Space: O(w)
    // ============================================================
    static class BFS {
        public static int minDepth(TreeNode root) {
            if (root == null) return 0;

            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            int depth = 1;

            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    TreeNode node = queue.poll();
                    // First leaf found = minimum depth
                    if (node.left == null && node.right == null) {
                        return depth;
                    }
                    if (node.left != null) queue.offer(node.left);
                    if (node.right != null) queue.offer(node.right);
                }
                depth++;
            }
            return depth; // Should not reach here if tree is non-empty
        }
    }

    // ============================================================
    // APPROACH 3 (ALT): ITERATIVE DFS with Stack
    // Track min depth across all leaf nodes.
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    static class IterativeDFS {
        public static int minDepth(TreeNode root) {
            if (root == null) return 0;

            Deque<Object[]> stack = new ArrayDeque<>();
            stack.push(new Object[]{root, 1});
            int minD = Integer.MAX_VALUE;

            while (!stack.isEmpty()) {
                Object[] pair = stack.pop();
                TreeNode node = (TreeNode) pair[0];
                int depth = (int) pair[1];

                if (node.left == null && node.right == null) {
                    minD = Math.min(minD, depth);
                }
                if (node.right != null) {
                    stack.push(new Object[]{node.right, depth + 1});
                }
                if (node.left != null) {
                    stack.push(new Object[]{node.left, depth + 1});
                }
            }
            return minD;
        }
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
        System.out.println("=== Min Depth of Binary Tree ===\n");

        Object[][] tests = {
            {new Integer[]{3, 9, 20, null, null, 15, 7}, 2},
            {new Integer[]{2, null, 3, null, 4, null, 5, null, 6}, 5},
            {new Integer[]{1}, 1},
            {new Integer[]{1, 2}, 2},
            {new Integer[]{}, 0},
            {new Integer[]{1, 2, 3, 4, 5}, 2},
        };

        for (Object[] test : tests) {
            Integer[] arr = (Integer[]) test[0];
            int expected = (int) test[1];
            TreeNode root = buildTree(arr);

            int rec = RecursiveDFS.minDepth(root);
            int bfs = BFS.minDepth(root);
            int dfs = IterativeDFS.minDepth(root);

            String status = (rec == expected && bfs == expected && dfs == expected) ? "PASS" : "FAIL";
            System.out.printf("Tree: %-45s%n", Arrays.toString(arr));
            System.out.printf("  Recursive: %d, BFS: %d, Iterative DFS: %d (expected %d) %s%n%n",
                    rec, bfs, dfs, expected, status);
        }
    }
}
