import java.util.*;

/**
 * Problem: Time to Burn Binary Tree
 * Difficulty: HARD | XP: 50
 *
 * Find minimum time for fire to burn entire tree starting from a target node.
 * Fire spreads to adjacent nodes (parent, left child, right child) each second.
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
// BFS from target after building parent map via DFS
// Time: O(N)  |  Space: O(N)
// ============================================================
class BruteForce {
    static Map<TreeNode, TreeNode> parentMap = new HashMap<>();

    static void buildParentMap(TreeNode root) {
        if (root == null) return;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (node.left != null) {
                parentMap.put(node.left, node);
                q.offer(node.left);
            }
            if (node.right != null) {
                parentMap.put(node.right, node);
                q.offer(node.right);
            }
        }
    }

    static TreeNode findTarget(TreeNode root, int target) {
        if (root == null) return null;
        if (root.val == target) return root;
        TreeNode left = findTarget(root.left, target);
        if (left != null) return left;
        return findTarget(root.right, target);
    }

    static int timeToBurn(TreeNode root, int target) {
        buildParentMap(root);
        TreeNode targetNode = findTarget(root, target);
        Set<TreeNode> visited = new HashSet<>();
        Queue<TreeNode> bfsQueue = new LinkedList<>();
        bfsQueue.offer(targetNode);
        visited.add(targetNode);
        int time = 0;
        while (!bfsQueue.isEmpty()) {
            int size = bfsQueue.size();
            boolean spread = false;
            for (int i = 0; i < size; i++) {
                TreeNode cur = bfsQueue.poll();
                if (cur.left != null && !visited.contains(cur.left)) {
                    visited.add(cur.left);
                    bfsQueue.offer(cur.left);
                    spread = true;
                }
                if (cur.right != null && !visited.contains(cur.right)) {
                    visited.add(cur.right);
                    bfsQueue.offer(cur.right);
                    spread = true;
                }
                TreeNode par = parentMap.get(cur);
                if (par != null && !visited.contains(par)) {
                    visited.add(par);
                    bfsQueue.offer(par);
                    spread = true;
                }
            }
            if (spread) time++;
        }
        return time;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Single DFS that simultaneously builds parent map + finds target,
// then BFS from target. Cleaner pass structure.
// Time: O(N)  |  Space: O(N)
// ============================================================
class Optimal {
    static TreeNode targetNode = null;

    static void dfs(TreeNode node, TreeNode parent, int target, Map<TreeNode, TreeNode> parentMap) {
        if (node == null) return;
        if (node.val == target) targetNode = node;
        parentMap.put(node, parent);
        dfs(node.left, node, target, parentMap);
        dfs(node.right, node, target, parentMap);
    }

    static int timeToBurn(TreeNode root, int target) {
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        targetNode = null;
        dfs(root, null, target, parentMap);

        Queue<TreeNode> queue = new LinkedList<>();
        Set<TreeNode> visited = new HashSet<>();
        queue.offer(targetNode);
        visited.add(targetNode);
        int time = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean anyNew = false;
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.poll();
                for (TreeNode neighbor : new TreeNode[]{cur.left, cur.right, parentMap.get(cur)}) {
                    if (neighbor != null && !visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                        anyNew = true;
                    }
                }
            }
            if (anyNew) time++;
        }
        return time;
    }
}

// ============================================================
// APPROACH 3: BEST
// Recursive DFS computing max depth + tracking answer when target found.
// Returns distance from subtree to target; computes burn time during traversal.
// Time: O(N)  |  Space: O(H) stack space
// ============================================================
class Best {
    static int ans = 0;

    // Returns:
    //  >= 0: distance from this node to target (target is in this subtree)
    //  -1:   target not found in this subtree, return depth as negative
    //        Actually: positive = found target, value = depth from target
    //                  negative = not found, abs = depth of subtree
    static int dfs(TreeNode node, int target) {
        if (node == null) return -1; // depth 0 subtree, not found => -(0) but use -1 as sentinel?
        // Convention: return positive distance if target found, negative if not (abs = height)
        if (node.val == target) {
            ans = Math.max(ans, height(node));
            return 0; // distance from target to itself
        }
        int left = dfs(node.left, target);
        int right = dfs(node.right, target);

        if (left >= 0) {
            // target found in left subtree
            int distToRight = height(node.right) + 1;
            ans = Math.max(ans, left + 1 + distToRight);
            return left + 1;
        }
        if (right >= 0) {
            int distToLeft = height(node.left) + 1;
            ans = Math.max(ans, right + 1 + distToLeft);
            return right + 1;
        }
        return -1;
    }

    static int height(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    static int timeToBurn(TreeNode root, int target) {
        ans = 0;
        dfs(root, target);
        return ans;
    }
}

public class Solution {
    static TreeNode build(Integer[] vals) {
        if (vals == null || vals.length == 0) return null;
        TreeNode root = new TreeNode(vals[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < vals.length) {
            TreeNode cur = q.poll();
            if (i < vals.length && vals[i] != null) { cur.left = new TreeNode(vals[i]); q.offer(cur.left); }
            i++;
            if (i < vals.length && vals[i] != null) { cur.right = new TreeNode(vals[i]); q.offer(cur.right); }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== Time to Burn Binary Tree ===");

        // Tree: 1->2,3; 2->4,5; target=2 => burn time=3
        Integer[] vals = {1, 2, 3, 4, 5, null, 6};
        int target = 2;

        TreeNode r1 = build(vals);
        System.out.println("BruteForce (target=" + target + "): " + BruteForce.timeToBurn(r1, target)); // 3

        TreeNode r2 = build(vals);
        System.out.println("Optimal   (target=" + target + "): " + Optimal.timeToBurn(r2, target));   // 3

        TreeNode r3 = build(vals);
        System.out.println("Best      (target=" + target + "): " + Best.timeToBurn(r3, target));      // 3

        // Edge: single node
        TreeNode single = new TreeNode(1);
        System.out.println("Single node (target=1): " + BruteForce.timeToBurn(single, 1)); // 0
    }
}
