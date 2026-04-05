/**
 * Problem: Level Order Traversal (LeetCode #102)
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
    // APPROACH 1: DFS with Depth Tracking
    // Preorder DFS, append node.val to result[depth].
    // Time: O(n)  |  Space: O(h) + O(n) result
    // ============================================================
    static class DFSApproach {
        public static List<List<Integer>> levelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            dfs(root, 0, result);
            return result;
        }

        private static void dfs(TreeNode node, int depth, List<List<Integer>> result) {
            if (node == null) return;
            // Create new level list if needed
            if (depth == result.size()) {
                result.add(new ArrayList<>());
            }
            result.get(depth).add(node.val);
            dfs(node.left, depth + 1, result);
            dfs(node.right, depth + 1, result);
        }
    }

    // ============================================================
    // APPROACH 2 & 3: BFS with Queue (Standard)
    // Snapshot queue size per level. Process level by level.
    // Time: O(n)  |  Space: O(w) + O(n) result
    // ============================================================
    static class BFSApproach {
        public static List<List<Integer>> levelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            if (root == null) return result;

            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);

            while (!queue.isEmpty()) {
                int levelSize = queue.size();
                List<Integer> currentLevel = new ArrayList<>();

                for (int i = 0; i < levelSize; i++) {
                    TreeNode node = queue.poll();
                    currentLevel.add(node.val);
                    if (node.left != null) queue.offer(node.left);
                    if (node.right != null) queue.offer(node.right);
                }
                result.add(currentLevel);
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3 (ALT): ITERATIVE with Two Lists (no queue)
    // Alternate between "current level" and "next level" lists.
    // Time: O(n)  |  Space: O(w) + O(n) result
    // ============================================================
    static class TwoListApproach {
        public static List<List<Integer>> levelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            if (root == null) return result;

            List<TreeNode> currentLevel = new ArrayList<>();
            currentLevel.add(root);

            while (!currentLevel.isEmpty()) {
                List<Integer> values = new ArrayList<>();
                List<TreeNode> nextLevel = new ArrayList<>();

                for (TreeNode node : currentLevel) {
                    values.add(node.val);
                    if (node.left != null) nextLevel.add(node.left);
                    if (node.right != null) nextLevel.add(node.right);
                }

                result.add(values);
                currentLevel = nextLevel;
            }
            return result;
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
        System.out.println("=== Level Order Traversal ===\n");

        Object[][] tests = {
            {new Integer[]{3, 9, 20, null, null, 15, 7}},
            {new Integer[]{1}},
            {new Integer[]{}},
            {new Integer[]{1, 2, 3, 4, 5}},
            {new Integer[]{1, 2, null, 3, null, 4}},
            {new Integer[]{1, 2, 3, 4, 5, 6, 7, 8}},
        };

        for (Object[] test : tests) {
            Integer[] arr = (Integer[]) test[0];
            TreeNode root = buildTree(arr);

            List<List<Integer>> dfs = DFSApproach.levelOrder(root);
            List<List<Integer>> bfs = BFSApproach.levelOrder(root);
            List<List<Integer>> twoList = TwoListApproach.levelOrder(root);

            boolean match = dfs.equals(bfs) && bfs.equals(twoList);
            String status = match ? "PASS" : "FAIL";

            System.out.printf("Tree: %-40s%n", Arrays.toString(arr));
            System.out.printf("  DFS:      %s%n", dfs);
            System.out.printf("  BFS:      %s%n", bfs);
            System.out.printf("  TwoList:  %s %s%n%n", twoList, status);
        }
    }
}
