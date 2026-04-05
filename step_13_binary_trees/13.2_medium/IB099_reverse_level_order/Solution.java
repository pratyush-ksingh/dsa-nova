import java.util.*;

/**
 * Problem: Reverse Level Order Traversal
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given a binary tree, return its reverse level order traversal --
 * node values level by level from bottom to top, left to right within
 * each level. Return as a list of lists.
 *
 * @author DSA_Nova
 */
public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- BFS + Reverse the Result
    // Time: O(N)  |  Space: O(N)
    // Standard level order traversal using a queue, then reverse
    // the list of levels at the end.
    // ============================================================
    static class BruteForce {
        public static List<List<Integer>> reverseLevelOrder(TreeNode root) {
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

            Collections.reverse(result);
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- BFS with LinkedList (addFirst each level)
    // Time: O(N)  |  Space: O(N)
    // Instead of reversing at the end, insert each level at the
    // front of a LinkedList so the result is already bottom-up.
    // ============================================================
    static class Optimal {
        public static List<List<Integer>> reverseLevelOrder(TreeNode root) {
            LinkedList<List<Integer>> result = new LinkedList<>();
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
                result.addFirst(currentLevel);
            }

            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- DFS with Level Tracking + Reverse
    // Time: O(N)  |  Space: O(N)
    // DFS records each node's value into a map keyed by depth,
    // then iterates levels from max depth to 0.
    // ============================================================
    static class Best {
        public static List<List<Integer>> reverseLevelOrder(TreeNode root) {
            if (root == null) return new ArrayList<>();

            Map<Integer, List<Integer>> levels = new HashMap<>();
            int[] maxDepth = {0};
            dfs(root, 0, levels, maxDepth);

            List<List<Integer>> result = new ArrayList<>();
            for (int d = maxDepth[0]; d >= 0; d--) {
                result.add(levels.get(d));
            }
            return result;
        }

        private static void dfs(TreeNode node, int depth,
                                Map<Integer, List<Integer>> levels, int[] maxDepth) {
            if (node == null) return;
            levels.computeIfAbsent(depth, k -> new ArrayList<>()).add(node.val);
            maxDepth[0] = Math.max(maxDepth[0], depth);
            dfs(node.left, depth + 1, levels, maxDepth);
            dfs(node.right, depth + 1, levels, maxDepth);
        }
    }

    // ============================================================
    // HELPERS
    // ============================================================
    static TreeNode buildTree(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) return null;
        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty() && i < values.length) {
            TreeNode node = queue.poll();
            if (i < values.length && values[i] != null) {
                node.left = new TreeNode(values[i]);
                queue.offer(node.left);
            }
            i++;
            if (i < values.length && values[i] != null) {
                node.right = new TreeNode(values[i]);
                queue.offer(node.right);
            }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== Reverse Level Order Traversal ===");

        Integer[][] testCases = {
            {3, 9, 20, null, null, 15, 7},
            {1},
            {},
            {1, 2, 3, 4, 5, 6, 7},
            {1, 2, null, 3, null, 4, null}
        };
        String[] descs = {
            "Standard tree", "Single node", "Empty tree",
            "Complete binary tree", "Left-skewed tree"
        };

        for (int t = 0; t < testCases.length; t++) {
            TreeNode root = buildTree(testCases[t]);
            System.out.printf("%nTree: %s  (%s)%n", Arrays.toString(testCases[t]), descs[t]);
            System.out.printf("  Brute Force: %s%n", BruteForce.reverseLevelOrder(root));
            System.out.printf("  Optimal:     %s%n", Optimal.reverseLevelOrder(root));
            System.out.printf("  Best:        %s%n", Best.reverseLevelOrder(root));
        }
    }
}
