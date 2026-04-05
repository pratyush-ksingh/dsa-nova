/**
 * Problem: Right Side View
 * LeetCode 199 | Difficulty: MEDIUM | XP: 25
 *
 * Given the root of a binary tree, return the values of nodes visible from
 * the right side, ordered top to bottom.
 *
 * Key Insight: The rightmost node at each level is visible.
 *   - BFS: take the last node of each level.
 *   - DFS (right-first): the first node seen at each depth is the rightmost.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE  (BFS — last node per level)
    // Time: O(n)  |  Space: O(n)  [queue holds up to n/2 nodes]
    // ============================================================
    static class BruteForce {
        /**
         * Standard BFS level-order traversal.
         * At each level, process all nodes; the last one is the rightmost visible.
         */
        public List<Integer> rightSideView(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            if (root == null) return result;
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    TreeNode node = queue.poll();
                    if (i == size - 1) result.add(node.val);
                    if (node.left  != null) queue.offer(node.left);
                    if (node.right != null) queue.offer(node.right);
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (DFS right-first, record first node per depth)
    // Time: O(n)  |  Space: O(h)  [h = tree height, recursion stack]
    // ============================================================
    static class Optimal {
        private List<Integer> result = new ArrayList<>();

        /**
         * Pre-order DFS, always visiting right child before left.
         * When we first reach a new depth (result.size() == depth), the node
         * we are at is the rightmost one at that level — record it.
         */
        public List<Integer> rightSideView(TreeNode root) {
            dfs(root, 0);
            return result;
        }

        private void dfs(TreeNode node, int depth) {
            if (node == null) return;
            if (depth == result.size()) result.add(node.val);
            dfs(node.right, depth + 1);
            dfs(node.left,  depth + 1);
        }
    }

    // ============================================================
    // APPROACH 3: BEST  (Iterative DFS — avoids recursion stack overflow)
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    static class Best {
        /**
         * Iterative pre-order DFS using an explicit Deque of (node, depth) pairs.
         * Push LEFT before RIGHT so RIGHT is popped first (LIFO = right-first DFS).
         * Same invariant: first visit at a new depth records the rightmost node.
         * Avoids Java stack overflow on heavily unbalanced trees.
         */
        public List<Integer> rightSideView(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            if (root == null) return result;
            Deque<Object[]> stack = new ArrayDeque<>();
            stack.push(new Object[]{root, 0});
            while (!stack.isEmpty()) {
                Object[] top  = stack.pop();
                TreeNode node = (TreeNode) top[0];
                int depth     = (int) top[1];
                if (depth == result.size()) result.add(node.val);
                if (node.left  != null) stack.push(new Object[]{node.left,  depth + 1});
                if (node.right != null) stack.push(new Object[]{node.right, depth + 1});
            }
            return result;
        }
    }

    /** Build tree from level-order array (0 = null sentinel). */
    private static TreeNode buildTree(int[] vals) {
        if (vals == null || vals.length == 0) return null;
        TreeNode root = new TreeNode(vals[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < vals.length) {
            TreeNode node = q.poll();
            if (i < vals.length && vals[i] != 0) { node.left  = new TreeNode(vals[i]); q.offer(node.left);  } i++;
            if (i < vals.length && vals[i] != 0) { node.right = new TreeNode(vals[i]); q.offer(node.right); } i++;
        }
        return root;
    }

    public static void main(String[] args) {
        int[][] trees    = {{1,2,3,0,5,0,4}, {1,0,3}, {}, {1}};
        int[][] expected = {{1,3,4},          {1,3},   {}, {1}};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        System.out.println("=== Right Side View ===");
        for (int t = 0; t < trees.length; t++) {
            TreeNode root = buildTree(trees[t]);
            List<Integer> b  = bf.rightSideView(root);
            // Re-build for each approach since Optimal has instance state
            root = buildTree(trees[t]);
            List<Integer> o  = new Optimal().rightSideView(root);
            root = buildTree(trees[t]);
            List<Integer> be = bst.rightSideView(root);
            int[] exp = expected[t];
            System.out.printf("  brute=%s  opt=%s  best=%s  (exp=%s)%n",
                b, o, be, Arrays.toString(exp));
        }
    }
}
