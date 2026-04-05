import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Inorder to list, pick from end
// Time: O(N)  |  Space: O(N)
// Collect inorder (sorted ascending), kth largest = element at (n-k)
// ============================================================
class BruteForce {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public static int solve(TreeNode root, int k) {
        List<Integer> inorder = new ArrayList<>();
        inorderTraversal(root, inorder);
        int n = inorder.size();
        return inorder.get(n - k);
    }

    private static void inorderTraversal(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorderTraversal(node.left, list);
        list.add(node.val);
        inorderTraversal(node.right, list);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Reverse inorder traversal (right, root, left)
// Time: O(H + k)  |  Space: O(H)
// Reverse inorder gives descending order; stop at kth element
// ============================================================
class Optimal {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    private static int count, result;

    public static int solve(TreeNode root, int k) {
        count = 0;
        result = -1;
        reverseInorder(root, k);
        return result;
    }

    private static void reverseInorder(TreeNode node, int k) {
        if (node == null || count >= k) return;
        reverseInorder(node.right, k);
        count++;
        if (count == k) { result = node.val; return; }
        reverseInorder(node.left, k);
    }
}

// ============================================================
// APPROACH 3: BEST - Iterative reverse inorder with early exit
// Time: O(H + k)  |  Space: O(H)
// Iterative to avoid stack overflow on deep trees
// ============================================================
class Best {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public static int solve(TreeNode root, int k) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        int count = 0;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.right;  // go right first for reverse inorder
            }
            cur = stack.pop();
            count++;
            if (count == k) return cur.val;
            cur = cur.left;
        }
        return -1;  // k > number of nodes
    }
}

public class Solution {
    public static void main(String[] args) {
        //       5
        //      / \
        //     3   6
        //    / \
        //   2   4
        //  /
        // 1
        // Inorder: [1,2,3,4,5,6]  k=3 largest -> 4
        BruteForce.TreeNode r1 = new BruteForce.TreeNode(5);
        r1.left = new BruteForce.TreeNode(3);
        r1.right = new BruteForce.TreeNode(6);
        r1.left.left = new BruteForce.TreeNode(2);
        r1.left.right = new BruteForce.TreeNode(4);
        r1.left.left.left = new BruteForce.TreeNode(1);
        System.out.println("BruteForce k=3: " + BruteForce.solve(r1, 3));  // 4

        Optimal.TreeNode r2 = new Optimal.TreeNode(5);
        r2.left = new Optimal.TreeNode(3);
        r2.right = new Optimal.TreeNode(6);
        r2.left.left = new Optimal.TreeNode(2);
        r2.left.right = new Optimal.TreeNode(4);
        r2.left.left.left = new Optimal.TreeNode(1);
        System.out.println("Optimal   k=3: " + Optimal.solve(r2, 3));  // 4
        System.out.println("Optimal   k=1: " + Optimal.solve(r2, 1));  // 6

        Best.TreeNode r3 = new Best.TreeNode(5);
        r3.left = new Best.TreeNode(3);
        r3.right = new Best.TreeNode(6);
        r3.left.left = new Best.TreeNode(2);
        r3.left.right = new Best.TreeNode(4);
        r3.left.left.left = new Best.TreeNode(1);
        System.out.println("Best      k=3: " + Best.solve(r3, 3));  // 4
        System.out.println("Best      k=1: " + Best.solve(r3, 1));  // 6
    }
}
