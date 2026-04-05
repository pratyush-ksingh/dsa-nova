/**
 * Problem: Level Order Traversal
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given a binary tree, return values grouped by level (BFS order).
 * Output: List<List<Integer>> where output.get(i) = all values at depth i.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode l, TreeNode r) { this.val = val; left = l; right = r; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE — DFS with depth parameter
    // Time: O(n)  |  Space: O(n) result + O(h) stack
    // Preorder DFS; append each node value to its depth bucket.
    // ============================================================
    static class BruteForce {
        public List<List<Integer>> levelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            dfs(root, 0, result);
            return result;
        }

        private void dfs(TreeNode node, int depth, List<List<Integer>> result) {
            if (node == null) return;
            if (depth == result.size()) {
                result.add(new ArrayList<>());  // new level discovered
            }
            result.get(depth).add(node.val);
            dfs(node.left,  depth + 1, result);
            dfs(node.right, depth + 1, result);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — BFS with queue size snapshot
    // Time: O(n)  |  Space: O(n) — queue holds at most one full level
    // Snapshot queue.size() at start of each level iteration.
    // ============================================================
    static class Optimal {
        public List<List<Integer>> levelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            if (root == null) return result;

            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);

            while (!queue.isEmpty()) {
                int levelSize = queue.size();          // nodes in this level
                List<Integer> level = new ArrayList<>();

                for (int i = 0; i < levelSize; i++) {
                    TreeNode node = queue.poll();
                    level.add(node.val);
                    if (node.left  != null) queue.offer(node.left);
                    if (node.right != null) queue.offer(node.right);
                }
                result.add(level);
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — BFS with null sentinel
    // Time: O(n)  |  Space: O(n)
    // Uses null as an end-of-level marker in the queue.
    // Same complexity as Optimal; an alternative style for interviews.
    // ============================================================
    static class Best {
        public List<List<Integer>> levelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            if (root == null) return result;

            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            queue.offer(null);   // null = end-of-level sentinel

            List<Integer> level = new ArrayList<>();

            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();

                if (node == null) {
                    // Level complete
                    result.add(new ArrayList<>(level));
                    level.clear();
                    if (!queue.isEmpty()) {
                        queue.offer(null);  // sentinel for next level
                    }
                } else {
                    level.add(node.val);
                    if (node.left  != null) queue.offer(node.left);
                    if (node.right != null) queue.offer(node.right);
                }
            }
            return result;
        }
    }

    // ----------------------------------------------------------------
    // Test helpers
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

    public static void main(String[] args) {
        System.out.println("=== Level Order Traversal ===");

        //        3
        //       / \
        //      9  20
        //        /  \
        //       15   7
        Integer[] vals = {3, 9, 20, null, null, 15, 7};

        System.out.println("Brute:   " + new BruteForce().levelOrder(build(vals)));  // [[3],[9,20],[15,7]]
        System.out.println("Optimal: " + new Optimal().levelOrder(build(vals)));      // [[3],[9,20],[15,7]]
        System.out.println("Best:    " + new Best().levelOrder(build(vals)));          // [[3],[9,20],[15,7]]

        System.out.println("\nSingle node [1]:");
        System.out.println("Brute:   " + new BruteForce().levelOrder(build(new Integer[]{1})));
        System.out.println("Optimal: " + new Optimal().levelOrder(build(new Integer[]{1})));
        System.out.println("Best:    " + new Best().levelOrder(build(new Integer[]{1})));
    }
}
