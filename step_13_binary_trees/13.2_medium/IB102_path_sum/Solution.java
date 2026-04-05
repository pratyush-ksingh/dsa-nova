/**
 * Problem: Path Sum (LeetCode #112)
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
    // APPROACH 1 & 3: RECURSIVE DFS -- Subtract Target
    // At each node, subtract val from targetSum.
    // At a leaf, check if remainder == 0.
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    static class RecursiveDFS {
        public static boolean hasPathSum(TreeNode root, int targetSum) {
            if (root == null) return false;
            targetSum -= root.val;
            if (root.left == null && root.right == null) {
                return targetSum == 0;
            }
            return hasPathSum(root.left, targetSum) || hasPathSum(root.right, targetSum);
        }
    }

    // ============================================================
    // APPROACH 2: ITERATIVE BFS
    // Queue stores (node, remainingSum). Check at leaves.
    // Time: O(n)  |  Space: O(w)
    // ============================================================
    static class IterativeBFS {
        public static boolean hasPathSum(TreeNode root, int targetSum) {
            if (root == null) return false;

            Queue<Object[]> queue = new LinkedList<>();
            queue.offer(new Object[]{root, targetSum - root.val});

            while (!queue.isEmpty()) {
                Object[] pair = queue.poll();
                TreeNode node = (TreeNode) pair[0];
                int remaining = (int) pair[1];

                if (node.left == null && node.right == null && remaining == 0) {
                    return true;
                }
                if (node.left != null) {
                    queue.offer(new Object[]{node.left, remaining - node.left.val});
                }
                if (node.right != null) {
                    queue.offer(new Object[]{node.right, remaining - node.right.val});
                }
            }
            return false;
        }
    }

    // ============================================================
    // APPROACH 3 (ALT): ITERATIVE DFS with Stack
    // Same logic as BFS but with stack (DFS order).
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    static class IterativeDFS {
        public static boolean hasPathSum(TreeNode root, int targetSum) {
            if (root == null) return false;

            Deque<Object[]> stack = new ArrayDeque<>();
            stack.push(new Object[]{root, targetSum - root.val});

            while (!stack.isEmpty()) {
                Object[] pair = stack.pop();
                TreeNode node = (TreeNode) pair[0];
                int remaining = (int) pair[1];

                if (node.left == null && node.right == null && remaining == 0) {
                    return true;
                }
                if (node.right != null) {
                    stack.push(new Object[]{node.right, remaining - node.right.val});
                }
                if (node.left != null) {
                    stack.push(new Object[]{node.left, remaining - node.left.val});
                }
            }
            return false;
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
        System.out.println("=== Path Sum ===\n");

        Object[][] tests = {
            {new Integer[]{5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1}, 22, true},
            {new Integer[]{1, 2, 3}, 5, false},
            {new Integer[]{1, 2}, 1, false},
            {new Integer[]{}, 0, false},
            {new Integer[]{1}, 1, true},
            {new Integer[]{1, 2, null, 3, null, 4, null, 5}, 15, true},
            {new Integer[]{-2, null, -3}, -5, true},
        };

        for (Object[] test : tests) {
            Integer[] arr = (Integer[]) test[0];
            int target = (int) test[1];
            boolean expected = (boolean) test[2];
            TreeNode root = buildTree(arr);

            boolean rec = RecursiveDFS.hasPathSum(root, target);
            boolean bfs = IterativeBFS.hasPathSum(root, target);
            boolean dfs = IterativeDFS.hasPathSum(root, target);

            String status = (rec == expected && bfs == expected && dfs == expected) ? "PASS" : "FAIL";
            System.out.printf("Tree: %-50s target=%d%n", Arrays.toString(arr), target);
            System.out.printf("  Recursive: %s, BFS: %s, DFS: %s (expected %s) %s%n%n",
                    rec, bfs, dfs, expected, status);
        }
    }
}
