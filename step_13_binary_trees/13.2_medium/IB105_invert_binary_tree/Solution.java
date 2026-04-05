/**
 * Problem: Invert Binary Tree (LeetCode #226)
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
    // APPROACH 1 & 3: RECURSIVE DFS (Preorder Swap)
    // Swap children, then recurse into both subtrees.
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    static class RecursiveDFS {
        public static TreeNode invertTree(TreeNode root) {
            if (root == null) return null;
            // Swap children
            TreeNode temp = root.left;
            root.left = root.right;
            root.right = temp;
            // Recurse
            invertTree(root.left);
            invertTree(root.right);
            return root;
        }
    }

    // ============================================================
    // APPROACH 2: ITERATIVE BFS
    // Level-by-level: dequeue, swap children, enqueue children.
    // Time: O(n)  |  Space: O(w)
    // ============================================================
    static class IterativeBFS {
        public static TreeNode invertTree(TreeNode root) {
            if (root == null) return null;

            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);

            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();
                // Swap children
                TreeNode temp = node.left;
                node.left = node.right;
                node.right = temp;
                // Enqueue children
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            return root;
        }
    }

    // ============================================================
    // APPROACH 3 (ALT): ITERATIVE DFS with Stack
    // Same as BFS but using a stack.
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    static class IterativeDFS {
        public static TreeNode invertTree(TreeNode root) {
            if (root == null) return null;

            Deque<TreeNode> stack = new ArrayDeque<>();
            stack.push(root);

            while (!stack.isEmpty()) {
                TreeNode node = stack.pop();
                // Swap children
                TreeNode temp = node.left;
                node.left = node.right;
                node.right = temp;
                // Push children
                if (node.left != null) stack.push(node.left);
                if (node.right != null) stack.push(node.right);
            }
            return root;
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

    // Helper: convert tree to level-order list for comparison
    public static List<Integer> treeToList(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node != null) {
                result.add(node.val);
                queue.offer(node.left);
                queue.offer(node.right);
            } else {
                result.add(null);
            }
        }
        // Trim trailing nulls
        while (!result.isEmpty() && result.get(result.size() - 1) == null) {
            result.remove(result.size() - 1);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Invert Binary Tree ===\n");

        Object[][] tests = {
            {new Integer[]{4, 2, 7, 1, 3, 6, 9}, new Integer[]{4, 7, 2, 9, 6, 3, 1}},
            {new Integer[]{2, 1, 3}, new Integer[]{2, 3, 1}},
            {new Integer[]{1}, new Integer[]{1}},
            {new Integer[]{}, new Integer[]{}},
            {new Integer[]{1, 2}, new Integer[]{1, null, 2}},
        };

        String[] methods = {"Recursive", "BFS", "Iterative DFS"};

        for (Object[] test : tests) {
            Integer[] arr = (Integer[]) test[0];
            Integer[] expectedArr = (Integer[]) test[1];
            List<Integer> expected = Arrays.asList(expectedArr);

            // Test each approach on a fresh tree
            for (int m = 0; m < 3; m++) {
                TreeNode root = buildTree(arr);
                TreeNode result;
                switch (m) {
                    case 0: result = RecursiveDFS.invertTree(root); break;
                    case 1: result = IterativeBFS.invertTree(root); break;
                    default: result = IterativeDFS.invertTree(root); break;
                }
                List<Integer> actual = treeToList(result);
                String status = actual.equals(expected) ? "PASS" : "FAIL";
                if (m == 0) {
                    System.out.printf("Tree: %s -> Expected: %s%n", Arrays.toString(arr), expected);
                }
                System.out.printf("  %s: %s %s%n", methods[m], actual, status);
            }
            System.out.println();
        }
    }
}
