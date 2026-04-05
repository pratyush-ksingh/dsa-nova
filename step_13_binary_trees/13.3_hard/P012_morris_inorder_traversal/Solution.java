/**
 * Problem: Morris Inorder Traversal
 * Difficulty: HARD | XP: 50
 *
 * @author DSA_Nova
 */

class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n)  |  Space: O(n)  (recursion call stack)
// ============================================================
// Standard recursive inorder traversal — clean and correct but
// uses O(h) implicit stack space (h = tree height; O(n) worst case
// for a skewed tree).
class BruteForce {
    public java.util.List<Integer> inorderTraversal(TreeNode root) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        recurse(root, result);
        return result;
    }

    private void recurse(TreeNode node, java.util.List<Integer> result) {
        if (node == null) return;
        recurse(node.left, result);
        result.add(node.val);
        recurse(node.right, result);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(n)  (explicit stack)
// ============================================================
// Iterative inorder using an explicit stack — avoids function-call
// overhead but still O(n) space in the worst case.
class Optimal {
    public java.util.List<Integer> inorderTraversal(TreeNode root) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        java.util.Deque<TreeNode> stack = new java.util.ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            // Reach the leftmost node
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            result.add(curr.val);
            curr = curr.right;
        }
        return result;
    }
}

// ============================================================
// APPROACH 3: BEST  — Morris Inorder Traversal
// Time: O(n)  |  Space: O(1)  (no stack, no recursion)
// ============================================================
// Uses temporary "threads" in the tree to navigate back up
// without a stack.  For each node `curr`:
//
//   Case A — curr has NO left child:
//     Visit curr, move right.
//
//   Case B — curr HAS a left child:
//     Find the inorder predecessor (rightmost node in left subtree).
//     If predecessor.right == null  => create thread: predecessor.right = curr
//                                      move left (go deeper).
//     If predecessor.right == curr  => thread already exists (visited before)
//                                      remove thread (restore tree)
//                                      visit curr, move right.
//
// Each node is visited at most twice => O(n).  Tree is fully restored.
class Best {
    public java.util.List<Integer> inorderTraversal(TreeNode root) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        TreeNode curr = root;

        while (curr != null) {
            if (curr.left == null) {
                // Case A: no left child — visit and go right
                result.add(curr.val);
                curr = curr.right;
            } else {
                // Case B: find inorder predecessor
                TreeNode predecessor = curr.left;
                while (predecessor.right != null && predecessor.right != curr) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    // First visit: create thread, descend left
                    predecessor.right = curr;
                    curr = curr.left;
                } else {
                    // Second visit: remove thread, visit node, go right
                    predecessor.right = null;
                    result.add(curr.val);
                    curr = curr.right;
                }
            }
        }
        return result;
    }
}

public class Solution {

    // Build a complete binary tree from level-order array (null = missing node)
    static TreeNode buildTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) return null;
        TreeNode root = new TreeNode(arr[0]);
        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty() && i < arr.length) {
            TreeNode node = queue.poll();
            if (i < arr.length && arr[i] != null) {
                node.left = new TreeNode(arr[i]);
                queue.offer(node.left);
            }
            i++;
            if (i < arr.length && arr[i] != null) {
                node.right = new TreeNode(arr[i]);
                queue.offer(node.right);
            }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== Morris Inorder Traversal ===\n");

        // Tree:       4
        //            / \
        //           2   6
        //          / \ / \
        //         1  3 5  7
        // Expected inorder: [1, 2, 3, 4, 5, 6, 7]
        Integer[] arr1 = {4, 2, 6, 1, 3, 5, 7};
        TreeNode tree1 = buildTree(arr1);

        System.out.println("[Approach 1 - BruteForce: Recursive]");
        System.out.println("Inorder: " + new BruteForce().inorderTraversal(tree1));

        // Rebuild (BruteForce doesn't mutate, but Best does temporarily)
        tree1 = buildTree(arr1);
        System.out.println("\n[Approach 2 - Optimal: Iterative Stack]");
        System.out.println("Inorder: " + new Optimal().inorderTraversal(tree1));

        tree1 = buildTree(arr1);
        System.out.println("\n[Approach 3 - Best: Morris Threading O(1) space]");
        System.out.println("Inorder: " + new Best().inorderTraversal(tree1));

        // Verify tree is fully restored after Morris
        System.out.println("Tree restored (inorder again): "
            + new BruteForce().inorderTraversal(tree1));

        // Left-skewed tree: 3->2->1
        Integer[] arr2 = {3, 2, null, 1};
        System.out.println("\n[Left-skewed: 3->2->1]");
        System.out.println("Morris:    " + new Best().inorderTraversal(buildTree(arr2)));

        // Single node
        System.out.println("\n[Single node: 42]");
        System.out.println("Morris:    " + new Best().inorderTraversal(new TreeNode(42)));

        // Right-skewed tree: 1->2->3->4
        TreeNode r1 = new TreeNode(1);
        r1.right = new TreeNode(2);
        r1.right.right = new TreeNode(3);
        r1.right.right.right = new TreeNode(4);
        System.out.println("\n[Right-skewed: 1->2->3->4]");
        System.out.println("Morris:    " + new Best().inorderTraversal(r1));
    }
}
