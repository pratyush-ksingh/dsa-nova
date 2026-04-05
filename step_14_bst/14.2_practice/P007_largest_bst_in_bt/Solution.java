import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Check every subtree with O(n) each
// Time: O(n^2)  |  Space: O(n) recursion
// ============================================================
// For every node, check if its subtree is a valid BST (using
// isBST helper). Track the maximum size found so far.
// ============================================================

class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int v) { val = v; }
}

class BruteForce {
    int maxSize = 0;

    private boolean isBST(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return isBST(node.left, min, node.val) && isBST(node.right, node.val, max);
    }

    private int countNodes(TreeNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    private void dfs(TreeNode node) {
        if (node == null) return;
        if (isBST(node, Long.MIN_VALUE, Long.MAX_VALUE)) {
            maxSize = Math.max(maxSize, countNodes(node));
        }
        dfs(node.left);
        dfs(node.right);
    }

    public int largestBSTSubtree(TreeNode root) {
        dfs(root);
        return maxSize;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Postorder single pass
// Time: O(n)  |  Space: O(h) recursion
// ============================================================
// Return from each subtree: (size, minVal, maxVal, isBST).
// If both children are BSTs and root.val fits (> left.max,
// < right.min), this subtree is a BST of size left+right+1.
// Otherwise propagate the largest BST size from children.
// ============================================================

class Optimal {
    // Returns [size, min, max, isBST(0/1)]
    private int[] postorder(TreeNode node) {
        if (node == null) {
            return new int[]{0, Integer.MAX_VALUE, Integer.MIN_VALUE, 1};
        }
        int[] left  = postorder(node.left);
        int[] right = postorder(node.right);

        int lSize = left[0], lMin = left[1], lMax = left[2], lBST = left[3];
        int rSize = right[0], rMin = right[1], rMax = right[2], rBST = right[3];

        if (lBST == 1 && rBST == 1 && node.val > lMax && node.val < rMin) {
            int size = lSize + rSize + 1;
            int mn   = Math.min(node.val, lMin);
            int mx   = Math.max(node.val, rMax);
            return new int[]{size, mn, mx, 1};
        }
        // Not a BST: pass largest BST size upward (no valid min/max)
        return new int[]{Math.max(lSize, rSize), 0, 0, 0};
    }

    public int largestBSTSubtree(TreeNode root) {
        return postorder(root)[0];
    }
}

// ============================================================
// APPROACH 3: BEST - Same postorder, explicit NodeInfo class
// Time: O(n)  |  Space: O(h)
// ============================================================
// Identical complexity to Optimal but uses a named class for
// clarity. Preferred in production/interviews for readability.
// Real-life use: validating partial BST indices in a database
// after partial corruption to identify the largest intact index.
// ============================================================

class Best {
    static class Info {
        int size;
        int min, max;
        boolean isBST;
        int maxBSTSize; // largest BST in subtree even if subtree isn't BST

        Info(int sz, int mn, int mx, boolean bst, int mbst) {
            size = sz; min = mn; max = mx; isBST = bst; maxBSTSize = mbst;
        }
    }

    private Info dfs(TreeNode node) {
        if (node == null) {
            return new Info(0, Integer.MAX_VALUE, Integer.MIN_VALUE, true, 0);
        }
        Info L = dfs(node.left);
        Info R = dfs(node.right);

        if (L.isBST && R.isBST && node.val > L.max && node.val < R.min) {
            int sz = L.size + R.size + 1;
            return new Info(sz,
                            Math.min(node.val, L.min),
                            Math.max(node.val, R.max),
                            true, sz);
        }
        return new Info(0, 0, 0, false, Math.max(L.maxBSTSize, R.maxBSTSize));
    }

    public int largestBSTSubtree(TreeNode root) {
        return dfs(root).maxBSTSize;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================

public class Solution {
    static TreeNode build(Integer[] arr) {
        if (arr == null || arr.length == 0) return null;
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        int i = 1;
        while (!q.isEmpty() && i < arr.length) {
            TreeNode node = q.poll();
            if (i < arr.length && arr[i] != null) {
                node.left = new TreeNode(arr[i]);
                q.add(node.left);
            }
            i++;
            if (i < arr.length && arr[i] != null) {
                node.right = new TreeNode(arr[i]);
                q.add(node.right);
            }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        //       10
        //      /  \
        //     5   15
        //    / \    \
        //   1   8   7   <- 7 violates BST (7 < 15 but in right subtree)
        // Largest BST = subtree rooted at 5 (size 3: 1,5,8)
        TreeNode t1 = build(new Integer[]{10, 5, 15, 1, 8, null, 7});
        System.out.println("Test1 Brute   (expect 3): " + new BruteForce().largestBSTSubtree(t1));
        System.out.println("Test1 Optimal (expect 3): " + new Optimal().largestBSTSubtree(t1));
        System.out.println("Test1 Best    (expect 3): " + new Best().largestBSTSubtree(t1));

        // Full BST of 7 nodes => entire tree is BST => size 7
        //      4
        //     / \
        //    2   6
        //   / \ / \
        //  1  3 5  7
        TreeNode t2 = build(new Integer[]{4, 2, 6, 1, 3, 5, 7});
        System.out.println("Test2 Best    (expect 7): " + new Best().largestBSTSubtree(t2));

        // Single node
        TreeNode t3 = build(new Integer[]{5});
        System.out.println("Test3 Best    (expect 1): " + new Best().largestBSTSubtree(t3));

        //      3
        //     / \
        //    2   5
        //   /   / \
        //  1   4   6
        // Both subtrees are BSTs; 3>2 and 3<5 so whole tree is BST? No: left max=2<3, right min=4>3. Yes BST!
        TreeNode t4 = build(new Integer[]{3, 2, 5, 1, null, 4, 6});
        System.out.println("Test4 Best    (expect 6): " + new Best().largestBSTSubtree(t4));
    }
}
