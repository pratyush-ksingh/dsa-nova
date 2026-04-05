/**
 * Problem: Check if Tree is Balanced (LeetCode #110)
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
    // APPROACH 1: BRUTE FORCE -- Top-Down
    // At each node, compute height of left & right subtrees,
    // check balance, then recurse into children.
    // Time: O(n^2) worst case  |  Space: O(h)
    // ============================================================
    static class BruteForce {
        public static boolean isBalanced(TreeNode root) {
            if (root == null) return true;
            int leftH = height(root.left);
            int rightH = height(root.right);
            if (Math.abs(leftH - rightH) > 1) return false;
            return isBalanced(root.left) && isBalanced(root.right);
        }

        private static int height(TreeNode node) {
            if (node == null) return 0;
            return 1 + Math.max(height(node.left), height(node.right));
        }
    }

    // ============================================================
    // APPROACH 2 & 3: OPTIMAL -- Bottom-Up Single Pass
    // Postorder traversal: compute height and check balance in
    // one pass. Return -1 as sentinel for "unbalanced".
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    static class Optimal {
        public static boolean isBalanced(TreeNode root) {
            return check(root) != -1;
        }

        private static int check(TreeNode node) {
            if (node == null) return 0;

            int leftH = check(node.left);
            if (leftH == -1) return -1;

            int rightH = check(node.right);
            if (rightH == -1) return -1;

            if (Math.abs(leftH - rightH) > 1) return -1;

            return 1 + Math.max(leftH, rightH);
        }
    }

    // ============================================================
    // APPROACH 3 (ALT): ITERATIVE Postorder with Height Map
    // Explicit stack postorder + HashMap storing computed heights.
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class Iterative {
        public static boolean isBalanced(TreeNode root) {
            if (root == null) return true;

            Deque<TreeNode> stack = new ArrayDeque<>();
            Map<TreeNode, Integer> heightMap = new HashMap<>();
            TreeNode curr = root, lastVisited = null;

            while (curr != null || !stack.isEmpty()) {
                while (curr != null) {
                    stack.push(curr);
                    curr = curr.left;
                }
                TreeNode top = stack.peek();
                if (top.right != null && top.right != lastVisited) {
                    curr = top.right;
                } else {
                    stack.pop();
                    int leftH = heightMap.getOrDefault(top.left, 0);
                    int rightH = heightMap.getOrDefault(top.right, 0);
                    if (Math.abs(leftH - rightH) > 1) return false;
                    heightMap.put(top, 1 + Math.max(leftH, rightH));
                    lastVisited = top;
                }
            }
            return true;
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
        System.out.println("=== Check if Tree is Balanced ===\n");

        Object[][] tests = {
            {new Integer[]{3, 9, 20, null, null, 15, 7}, true},
            {new Integer[]{1, 2, 2, 3, 3, null, null, 4, 4}, false},
            {new Integer[]{}, true},
            {new Integer[]{1}, true},
            {new Integer[]{1, 2, null, 3}, false},
            {new Integer[]{1, 2, 3, 4, 5, 6, 7}, true},
        };

        for (Object[] test : tests) {
            Integer[] arr = (Integer[]) test[0];
            boolean expected = (boolean) test[1];
            TreeNode root = buildTree(arr);

            boolean brute = BruteForce.isBalanced(root);
            boolean optimal = Optimal.isBalanced(root);
            boolean iter = Iterative.isBalanced(root);

            String status = (brute == expected && optimal == expected && iter == expected) ? "PASS" : "FAIL";
            System.out.printf("Tree: %-45s%n", Arrays.toString(arr));
            System.out.printf("  Brute: %s, Optimal: %s, Iterative: %s (expected %s) %s%n%n",
                    brute, optimal, iter, expected, status);
        }
    }
}
