/**
 * Problem: All Traversals in One Pass
 * Difficulty: HARD | XP: 50
 *
 * Given a binary tree, compute preorder, inorder, and postorder traversals
 * in a single iterative pass using a stack with a visit counter per node.
 *
 * @author DSA_Nova
 */
import java.util.*;

class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

// ============================================================
// Approach 1: Brute Force (Three separate recursive traversals)
// Time: O(3n) = O(n) | Space: O(n) recursion stack × 3
// ============================================================
class BruteForce {
    public static int[][] solve(TreeNode root) {
        List<Integer> pre = new ArrayList<>(), ino = new ArrayList<>(), post = new ArrayList<>();
        preorder(root, pre);
        inorder(root, ino);
        postorder(root, post);
        return new int[][]{toArray(pre), toArray(ino), toArray(post)};
    }

    private static void preorder(TreeNode node, List<Integer> res) {
        if (node == null) return;
        res.add(node.val);
        preorder(node.left, res);
        preorder(node.right, res);
    }

    private static void inorder(TreeNode node, List<Integer> res) {
        if (node == null) return;
        inorder(node.left, res);
        res.add(node.val);
        inorder(node.right, res);
    }

    private static void postorder(TreeNode node, List<Integer> res) {
        if (node == null) return;
        postorder(node.left, res);
        postorder(node.right, res);
        res.add(node.val);
    }

    static int[] toArray(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).toArray();
    }
}

// ============================================================
// Approach 2: Optimal (Single iterative pass with (node, state) stack)
// Time: O(n) | Space: O(n)
// ============================================================
class Optimal {
    public static int[][] solve(TreeNode root) {
        if (root == null) return new int[][]{new int[]{}, new int[]{}, new int[]{}};

        List<Integer> pre = new ArrayList<>(), ino = new ArrayList<>(), post = new ArrayList<>();
        // Stack stores int[] {nodeRef(via index), state} — we use an object stack
        Deque<int[]> stack = new ArrayDeque<>();
        // We store node objects via a wrapper
        Deque<Object[]> objStack = new ArrayDeque<>();
        objStack.push(new Object[]{root, 1});

        while (!objStack.isEmpty()) {
            Object[] top = objStack.pop();
            TreeNode node = (TreeNode) top[0];
            int state = (int) top[1];

            if (state == 1) {
                pre.add(node.val);
                objStack.push(new Object[]{node, 2});
                if (node.left != null) objStack.push(new Object[]{node.left, 1});
            } else if (state == 2) {
                ino.add(node.val);
                objStack.push(new Object[]{node, 3});
                if (node.right != null) objStack.push(new Object[]{node.right, 1});
            } else {
                post.add(node.val);
            }
        }

        return new int[][]{BruteForce.toArray(pre), BruteForce.toArray(ino), BruteForce.toArray(post)};
    }
}

// ============================================================
// Approach 3: Best (Same single-pass — named state constants)
// Time: O(n) | Space: O(n)
// ============================================================
class Best {
    static final int PRE = 1, IN = 2, POST = 3;

    public static int[][] solve(TreeNode root) {
        if (root == null) return new int[][]{new int[]{}, new int[]{}, new int[]{}};

        List<Integer> pre = new ArrayList<>(), ino = new ArrayList<>(), post = new ArrayList<>();
        Deque<Object[]> stack = new ArrayDeque<>();
        stack.push(new Object[]{root, PRE});

        while (!stack.isEmpty()) {
            Object[] top = stack.pop();
            TreeNode node = (TreeNode) top[0];
            int state = (int) top[1];

            if (state == PRE) {
                pre.add(node.val);
                stack.push(new Object[]{node, IN});
                if (node.left != null) stack.push(new Object[]{node.left, PRE});
            } else if (state == IN) {
                ino.add(node.val);
                stack.push(new Object[]{node, POST});
                if (node.right != null) stack.push(new Object[]{node.right, PRE});
            } else {
                post.add(node.val);
            }
        }

        return new int[][]{BruteForce.toArray(pre), BruteForce.toArray(ino), BruteForce.toArray(post)};
    }
}

// Main driver
public class Solution {
    // Build tree from level-order array (null = no node)
    static TreeNode buildTree(Integer[] vals) {
        if (vals == null || vals.length == 0 || vals[0] == null) return null;
        TreeNode root = new TreeNode(vals[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int i = 1;
        while (!queue.isEmpty() && i < vals.length) {
            TreeNode node = queue.poll();
            if (i < vals.length && vals[i] != null) {
                node.left = new TreeNode(vals[i]);
                queue.add(node.left);
            }
            i++;
            if (i < vals.length && vals[i] != null) {
                node.right = new TreeNode(vals[i]);
                queue.add(node.right);
            }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== All Traversals in One Pass ===\n");

        Object[][][] testCases = {
            // {levelOrder, expPre, expIn, expPost}
            {new Integer[]{1, 2, 3, 4, 5}, new int[]{1,2,4,5,3}, new int[]{4,2,5,1,3}, new int[]{4,5,2,3,1}},
            {new Integer[]{1}, new int[]{1}, new int[]{1}, new int[]{1}},
            {new Integer[]{1, 2, null, 3}, new int[]{1,2,3}, new int[]{3,2,1}, new int[]{3,2,1}},
        };

        for (Object[][] tc : testCases) {
            Integer[] vals = (Integer[]) tc[0];
            int[] expPre = (int[]) tc[1], expIn = (int[]) tc[2], expPost = (int[]) tc[3];

            TreeNode root = buildTree(vals);
            int[][] b = BruteForce.solve(root);
            int[][] o = Optimal.solve(root);
            int[][] h = Best.solve(root);

            boolean pass = Arrays.equals(b[0], expPre) && Arrays.equals(b[1], expIn) && Arrays.equals(b[2], expPost)
                        && Arrays.equals(o[0], expPre) && Arrays.equals(o[1], expIn) && Arrays.equals(o[2], expPost)
                        && Arrays.equals(h[0], expPre) && Arrays.equals(h[1], expIn) && Arrays.equals(h[2], expPost);

            System.out.println("Tree: " + Arrays.toString(vals));
            System.out.println("Brute:   pre=" + Arrays.toString(b[0]) + " in=" + Arrays.toString(b[1]) + " post=" + Arrays.toString(b[2]));
            System.out.println("Optimal: pre=" + Arrays.toString(o[0]) + " in=" + Arrays.toString(o[1]) + " post=" + Arrays.toString(o[2]));
            System.out.println("Best:    pre=" + Arrays.toString(h[0]) + " in=" + Arrays.toString(h[1]) + " post=" + Arrays.toString(h[2]));
            System.out.println("Pass: " + pass);
            System.out.println();
        }
    }
}
