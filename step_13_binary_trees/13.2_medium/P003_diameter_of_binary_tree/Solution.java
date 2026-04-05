/**
 * Problem: Diameter of Binary Tree (LeetCode #543)
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
    // At each node, diameter through it = height(left) + height(right).
    // Recurse into children and take max.
    // Time: O(n^2) worst case  |  Space: O(h)
    // ============================================================
    static class BruteForce {
        public static int diameterOfBinaryTree(TreeNode root) {
            if (root == null) return 0;
            int throughRoot = height(root.left) + height(root.right);
            int leftDia = diameterOfBinaryTree(root.left);
            int rightDia = diameterOfBinaryTree(root.right);
            return Math.max(throughRoot, Math.max(leftDia, rightDia));
        }

        private static int height(TreeNode node) {
            if (node == null) return 0;
            return 1 + Math.max(height(node.left), height(node.right));
        }
    }

    // ============================================================
    // APPROACH 2 & 3: OPTIMAL -- Bottom-Up Single Pass
    // Postorder: compute height, update global max diameter.
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    static class Optimal {
        private static int maxDiameter;

        public static int diameterOfBinaryTree(TreeNode root) {
            maxDiameter = 0;
            height(root);
            return maxDiameter;
        }

        private static int height(TreeNode node) {
            if (node == null) return 0;
            int leftH = height(node.left);
            int rightH = height(node.right);
            maxDiameter = Math.max(maxDiameter, leftH + rightH);
            return 1 + Math.max(leftH, rightH);
        }
    }

    // ============================================================
    // APPROACH 3 (ALT): ITERATIVE Postorder with Height Map
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class Iterative {
        public static int diameterOfBinaryTree(TreeNode root) {
            if (root == null) return 0;

            Deque<TreeNode> stack = new ArrayDeque<>();
            Map<TreeNode, Integer> heightMap = new HashMap<>();
            TreeNode curr = root, lastVisited = null;
            int maxDia = 0;

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
                    maxDia = Math.max(maxDia, leftH + rightH);
                    heightMap.put(top, 1 + Math.max(leftH, rightH));
                    lastVisited = top;
                }
            }
            return maxDia;
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
        System.out.println("=== Diameter of Binary Tree ===\n");

        Object[][] tests = {
            {new Integer[]{1, 2, 3, 4, 5}, 3},
            {new Integer[]{1, 2}, 1},
            {new Integer[]{1}, 0},
            {new Integer[]{}, 0},
            {new Integer[]{1, 2, 3, 4, 5, null, null, 6, null, null, 7}, 5},
            {new Integer[]{1, 2, null, 3, null, 4}, 3},
        };

        for (Object[] test : tests) {
            Integer[] arr = (Integer[]) test[0];
            int expected = (int) test[1];
            TreeNode root = buildTree(arr);

            int brute = BruteForce.diameterOfBinaryTree(root);
            int optimal = Optimal.diameterOfBinaryTree(root);
            int iter = Iterative.diameterOfBinaryTree(root);

            String status = (brute == expected && optimal == expected && iter == expected) ? "PASS" : "FAIL";
            System.out.printf("Tree: %-45s%n", Arrays.toString(arr));
            System.out.printf("  Brute: %d, Optimal: %d, Iterative: %d (expected %d) %s%n%n",
                    brute, optimal, iter, expected, status);
        }
    }
}
