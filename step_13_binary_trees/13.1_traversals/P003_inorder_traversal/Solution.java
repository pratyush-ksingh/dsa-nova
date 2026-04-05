/**
 * Problem: Inorder Traversal (LeetCode #94)
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
    // APPROACH 1: RECURSIVE (L - Root - R)
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    public static List<Integer> inorderRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private static void inorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.val);
        inorderHelper(node.right, result);
    }

    // ============================================================
    // APPROACH 2: ITERATIVE with explicit stack
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    public static List<Integer> inorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode current = root;

        while (current != null || !stack.isEmpty()) {
            // Push all left children
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            // Pop, visit, go right
            current = stack.pop();
            result.add(current.val);
            current = current.right;
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: MORRIS TRAVERSAL (O(1) space)
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    public static List<Integer> inorderMorris(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode current = root;

        while (current != null) {
            if (current.left == null) {
                // No left subtree: visit and go right
                result.add(current.val);
                current = current.right;
            } else {
                // Find inorder predecessor (rightmost in left subtree)
                TreeNode predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    // Create thread: predecessor -> current
                    predecessor.right = current;
                    current = current.left;
                } else {
                    // Thread exists: remove it, visit current, go right
                    predecessor.right = null;
                    result.add(current.val);
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
        System.out.println("=== Inorder Traversal ===\n");

        Integer[][] tests = {
            {},
            {1},
            {1, null, 2, 3},
            {4, 2, 6, null, 5, 7, 8},
        };
        int[][] expected = {
            {},
            {1},
            {1, 3, 2},
            {2, 5, 4, 7, 6, 8},
        };

        for (int t = 0; t < tests.length; t++) {
            TreeNode root = buildTree(tests[t]);
            List<Integer> rec = inorderRecursive(root);
            List<Integer> iter = inorderIterative(root);
            List<Integer> morris = inorderMorris(root);
            System.out.printf("Tree: %s%n", Arrays.toString(tests[t]));
            System.out.printf("  Recursive: %s%n", rec);
            System.out.printf("  Iterative: %s%n", iter);
            System.out.printf("  Morris:    %s%n", morris);
            System.out.printf("  Expected:  %s%n%n", Arrays.toString(expected[t]));
        }
    }
}
