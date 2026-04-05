/**
 * Problem: Postorder Traversal (LeetCode #145)
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
    // APPROACH 1: RECURSIVE (L - R - Root)
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    public static List<Integer> postorderRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postHelper(root, result);
        return result;
    }

    private static void postHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        postHelper(node.left, result);
        postHelper(node.right, result);
        result.add(node.val);
    }

    // ============================================================
    // APPROACH 2: ITERATIVE -- Reverse Trick
    // Modified preorder (Root-R-L) then reverse = L-R-Root
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static List<Integer> postorderReverse(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);
            // Push left FIRST, then right (so right pops first -> Root-R-L)
            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }
        Collections.reverse(result); // Root-R-L reversed = L-R-Root
        return result;
    }

    // ============================================================
    // APPROACH 3: ITERATIVE -- Single stack with prev pointer
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    public static List<Integer> postorderPrev(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        TreeNode prev = null;

        while (!stack.isEmpty()) {
            TreeNode curr = stack.peek();

            // Going down: push children
            if (prev == null || prev.left == curr || prev.right == curr) {
                if (curr.left != null) stack.push(curr.left);
                else if (curr.right != null) stack.push(curr.right);
                else { result.add(stack.pop().val); }
            }
            // Coming up from left: go right or visit
            else if (curr.left == prev) {
                if (curr.right != null) stack.push(curr.right);
                else { result.add(stack.pop().val); }
            }
            // Coming up from right: visit
            else {
                result.add(stack.pop().val);
            }
            prev = curr;
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
        System.out.println("=== Postorder Traversal ===\n");

        Integer[][] tests = {
            {},
            {1},
            {1, null, 2, 3},
            {1, 2, 3, 4, 5, null, 6},
        };
        int[][] expected = {
            {},
            {1},
            {3, 2, 1},
            {4, 5, 2, 6, 3, 1},
        };

        for (int t = 0; t < tests.length; t++) {
            TreeNode root = buildTree(tests[t]);
            List<Integer> rec = postorderRecursive(root);
            List<Integer> rev = postorderReverse(root);
            List<Integer> prev = postorderPrev(root);
            System.out.printf("Tree: %s%n", Arrays.toString(tests[t]));
            System.out.printf("  Recursive:    %s%n", rec);
            System.out.printf("  Reverse trick: %s%n", rev);
            System.out.printf("  Prev pointer:  %s%n", prev);
            System.out.printf("  Expected:      %s%n%n", Arrays.toString(expected[t]));
        }
    }
}
