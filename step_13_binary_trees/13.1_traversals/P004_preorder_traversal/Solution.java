/**
 * Problem: Preorder Traversal (LeetCode #144)
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
    // APPROACH 1: RECURSIVE (Root - L - R)
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    public static List<Integer> preorderRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preHelper(root, result);
        return result;
    }

    private static void preHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        result.add(node.val);         // Visit root
        preHelper(node.left, result);  // Left
        preHelper(node.right, result); // Right
    }

    // ============================================================
    // APPROACH 2: ITERATIVE with stack
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    public static List<Integer> preorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);
            // Push right first so left is processed first
            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: MORRIS PREORDER (O(1) space)
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    public static List<Integer> preorderMorris(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode current = root;

        while (current != null) {
            if (current.left == null) {
                result.add(current.val);
                current = current.right;
            } else {
                TreeNode predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    // Visit NOW (preorder: process before going left)
                    result.add(current.val);
                    predecessor.right = current;
                    current = current.left;
                } else {
                    // Thread exists: just remove and go right
                    predecessor.right = null;
                    current = current.right;
                }
            }
        }
        return result;
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
        System.out.println("=== Preorder Traversal ===\n");

        Integer[][] tests = {
            {},
            {1},
            {1, null, 2, 3},
            {1, 2, 3, 4, 5, null, 6},
        };
        int[][] expected = {
            {},
            {1},
            {1, 2, 3},
            {1, 2, 4, 5, 3, 6},
        };

        for (int t = 0; t < tests.length; t++) {
            TreeNode root = buildTree(tests[t]);
            List<Integer> rec = preorderRecursive(root);
            List<Integer> iter = preorderIterative(root);
            List<Integer> morris = preorderMorris(root);
            System.out.printf("Tree: %s%n", Arrays.toString(tests[t]));
            System.out.printf("  Recursive: %s%n", rec);
            System.out.printf("  Iterative: %s%n", iter);
            System.out.printf("  Morris:    %s%n", morris);
            System.out.printf("  Expected:  %s%n%n", Arrays.toString(expected[t]));
        }
    }
}
