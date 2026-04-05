/**
 * Problem: Zig Zag Traversal (Binary Tree Level Order Alternating)
 * Difficulty: MEDIUM | XP: 25
 *
 * Return the zigzag level order traversal of a binary tree.
 * Odd levels: left→right. Even levels: right→left.
 * Real-life use: Tree serialization, network topology visualization, game trees.
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
    // APPROACH 1: BRUTE FORCE
    // Standard BFS level order; then reverse every other level.
    // Time: O(N)  |  Space: O(N)
    // ============================================================
    static class BruteForce {
        public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            if (root == null) return result;
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            int level = 0;
            while (!queue.isEmpty()) {
                int size = queue.size();
                List<Integer> row = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    TreeNode node = queue.poll();
                    row.add(node.val);
                    if (node.left  != null) queue.offer(node.left);
                    if (node.right != null) queue.offer(node.right);
                }
                if (level % 2 == 1) Collections.reverse(row);
                result.add(row);
                level++;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // BFS with deque per level: add to front or back depending on direction.
    // Avoids the post-reverse step.
    // Time: O(N)  |  Space: O(N)
    // ============================================================
    static class Optimal {
        public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            if (root == null) return result;
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            boolean leftToRight = true;
            while (!queue.isEmpty()) {
                int size = queue.size();
                Deque<Integer> row = new ArrayDeque<>();
                for (int i = 0; i < size; i++) {
                    TreeNode node = queue.poll();
                    if (leftToRight) row.addLast(node.val);
                    else             row.addFirst(node.val);
                    if (node.left  != null) queue.offer(node.left);
                    if (node.right != null) queue.offer(node.right);
                }
                result.add(new ArrayList<>(row));
                leftToRight = !leftToRight;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Two-stack approach: one stack for current level, one for next.
    // Alternate push order (left-right vs right-left) to get zigzag naturally.
    // Time: O(N)  |  Space: O(N) — elegant, no extra queue
    // ============================================================
    static class Best {
        public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            if (root == null) return result;
            Deque<TreeNode> curr = new ArrayDeque<>();
            curr.push(root);
            boolean leftToRight = true;
            while (!curr.isEmpty()) {
                int size = curr.size();
                Deque<TreeNode> next = new ArrayDeque<>();
                List<Integer> row = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    TreeNode node = curr.pop();
                    row.add(node.val);
                    if (leftToRight) {
                        if (node.left  != null) next.push(node.left);
                        if (node.right != null) next.push(node.right);
                    } else {
                        if (node.right != null) next.push(node.right);
                        if (node.left  != null) next.push(node.left);
                    }
                }
                result.add(row);
                leftToRight = !leftToRight;
                curr = next;
            }
            return result;
        }
    }

    // Helper: build tree from level-order array (null = absent)
    static TreeNode buildTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) return null;
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < arr.length) {
            TreeNode node = q.poll();
            if (i < arr.length && arr[i] != null) { node.left = new TreeNode(arr[i]); q.offer(node.left); }
            i++;
            if (i < arr.length && arr[i] != null) { node.right = new TreeNode(arr[i]); q.offer(node.right); }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== Zig Zag Traversal ===");

        Integer[][] trees = {
            {3, 9, 20, null, null, 15, 7},
            {1},
            {1, 2, 3, 4, 5},
        };

        for (Integer[] arr : trees) {
            TreeNode root = buildTree(arr);
            System.out.printf("%nTree(level-order)=%s%n", Arrays.toString(arr));
            System.out.println("  Brute  : " + BruteForce.zigzagLevelOrder(root));
            System.out.println("  Optimal: " + Optimal.zigzagLevelOrder(root));
            System.out.println("  Best   : " + Best.zigzagLevelOrder(root));
        }
    }
}
