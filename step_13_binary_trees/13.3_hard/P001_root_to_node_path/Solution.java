/**
 * Problem: Root to Node Path in Binary Tree
 * Difficulty: MEDIUM | XP: 25
 * Source: Striver A2Z / GFG / Custom
 *
 * Given the root of a binary tree and a target value, return the path
 * (list of node values) from root to the node containing the target.
 * Return an empty list if the target is not present.
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
    // APPROACH 1: BRUTE FORCE -- BFS with parent tracking
    // Time: O(n)  |  Space: O(n) -- parent map stores all nodes
    // ============================================================
    public static List<Integer> rootToNodeBrute(TreeNode root, int target) {
        if (root == null) return new ArrayList<>();

        Map<TreeNode, TreeNode> parent = new HashMap<>();
        parent.put(root, null);
        TreeNode targetNode = null;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node.val == target) {
                targetNode = node;
                break;
            }
            if (node.left != null) {
                parent.put(node.left, node);
                queue.offer(node.left);
            }
            if (node.right != null) {
                parent.put(node.right, node);
                queue.offer(node.right);
            }
        }

        if (targetNode == null) return new ArrayList<>();

        // Backtrack from target to root via parent map
        List<Integer> path = new ArrayList<>();
        TreeNode curr = targetNode;
        while (curr != null) {
            path.add(curr.val);
            curr = parent.get(curr);
        }
        Collections.reverse(path);
        return path;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DFS recursive with backtracking
    // Time: O(n)  |  Space: O(h) -- only active path on stack
    // ============================================================
    public static List<Integer> rootToNodeOptimal(TreeNode root, int target) {
        List<Integer> path = new ArrayList<>();
        dfsOptimal(root, target, path);
        return path;
    }

    private static boolean dfsOptimal(TreeNode node, int target, List<Integer> path) {
        if (node == null) return false;
        path.add(node.val);
        if (node.val == target) return true;
        if (dfsOptimal(node.left, target, path) || dfsOptimal(node.right, target, path)) {
            return true;
        }
        path.remove(path.size() - 1);  // backtrack
        return false;
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative DFS with explicit stack
    // Time: O(n)  |  Space: O(h) -- no recursion overhead
    // ============================================================
    public static List<Integer> rootToNodeBest(TreeNode root, int target) {
        if (root == null) return new ArrayList<>();

        // Stack stores [node, pathToNode]
        Deque<Object[]> stack = new ArrayDeque<>();
        List<Integer> initPath = new ArrayList<>();
        initPath.add(root.val);
        stack.push(new Object[]{root, initPath});

        while (!stack.isEmpty()) {
            Object[] entry = stack.pop();
            TreeNode node = (TreeNode) entry[0];
            @SuppressWarnings("unchecked")
            List<Integer> pathSoFar = (List<Integer>) entry[1];

            if (node.val == target) return pathSoFar;

            // Push right first so left is processed first
            if (node.right != null) {
                List<Integer> newPath = new ArrayList<>(pathSoFar);
                newPath.add(node.right.val);
                stack.push(new Object[]{node.right, newPath});
            }
            if (node.left != null) {
                List<Integer> newPath = new ArrayList<>(pathSoFar);
                newPath.add(node.left.val);
                stack.push(new Object[]{node.left, newPath});
            }
        }
        return new ArrayList<>();
    }

    // ============================================================
    // Helpers
    // ============================================================
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
        System.out.println("=== Root to Node Path ===\n");

        Integer[] treeArr = {1, 2, 3, 4, 5, 6, 7};
        int[] targets    = {5,      7,      1,   4,      9};
        int[][] expected = {{1,2,5},{1,3,7},{1},{1,2,4},{}};

        for (int t = 0; t < targets.length; t++) {
            int target = targets[t];
            TreeNode root = buildTree(treeArr);
            List<Integer> r1 = rootToNodeBrute(root, target);
            List<Integer> r2 = rootToNodeOptimal(root, target);
            List<Integer> r3 = rootToNodeBest(root, target);

            List<Integer> exp = new ArrayList<>();
            for (int v : expected[t]) exp.add(v);

            System.out.printf("target=%d%n", target);
            System.out.printf("  Brute:   %s%n", r1);
            System.out.printf("  Optimal: %s%n", r2);
            System.out.printf("  Best:    %s%n", r3);
            System.out.printf("  Expected:%s%n", exp);
            System.out.printf("  %s%n%n",
                r1.equals(exp) && r2.equals(exp) && r3.equals(exp) ? "PASS" : "FAIL");
        }
    }
}
