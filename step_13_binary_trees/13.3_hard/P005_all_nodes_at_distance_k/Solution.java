import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Convert tree to graph, then BFS
// Time: O(N)  |  Space: O(N)
// Build adjacency list (parent pointers), BFS from target
// ============================================================
class BruteForce {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public static List<Integer> solve(TreeNode root, TreeNode target, int k) {
        // Build parent map
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        buildParentMap(root, null, parent);

        // BFS from target
        Queue<TreeNode> queue = new LinkedList<>();
        Set<TreeNode> visited = new HashSet<>();
        queue.offer(target);
        visited.add(target);
        int dist = 0;

        while (!queue.isEmpty()) {
            if (dist == k) {
                List<Integer> result = new ArrayList<>();
                for (TreeNode node : queue) result.add(node.val);
                return result;
            }
            int size = queue.size();
            dist++;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null && !visited.contains(node.left)) {
                    visited.add(node.left);
                    queue.offer(node.left);
                }
                if (node.right != null && !visited.contains(node.right)) {
                    visited.add(node.right);
                    queue.offer(node.right);
                }
                TreeNode par = parent.get(node);
                if (par != null && !visited.contains(par)) {
                    visited.add(par);
                    queue.offer(par);
                }
            }
        }
        return new ArrayList<>();
    }

    private static void buildParentMap(TreeNode node, TreeNode par, Map<TreeNode, TreeNode> map) {
        if (node == null) return;
        map.put(node, par);
        buildParentMap(node.left, node, map);
        buildParentMap(node.right, node, map);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - DFS to find distance from target, collect nodes
// Time: O(N)  |  Space: O(N)
// Use a single DFS that returns distance from target, and collects nodes
// at distance K during backtracking
// ============================================================
class Optimal {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    private static List<Integer> result;
    private static int K;
    private static int targetVal;

    public static List<Integer> solve(TreeNode root, TreeNode target, int k) {
        result = new ArrayList<>();
        K = k;
        targetVal = target.val;
        dfs(root);
        return result;
    }

    // Returns distance from current node to target, or -1 if target not in subtree
    private static int dfs(TreeNode node) {
        if (node == null) return -1;
        if (node.val == targetVal) {
            collectAtDist(node, 0);
            return 1;
        }
        int left = dfs(node.left);
        if (left != -1) {
            // target is in left subtree at distance left from current node
            if (left == K) result.add(node.val);
            // collect from right subtree at distance K - left - 1
            collectAtDist(node.right, left + 1);
            return left + 1;
        }
        int right = dfs(node.right);
        if (right != -1) {
            if (right == K) result.add(node.val);
            collectAtDist(node.left, right + 1);
            return right + 1;
        }
        return -1;
    }

    private static void collectAtDist(TreeNode node, int dist) {
        if (node == null || dist > K) return;
        if (dist == K) { result.add(node.val); return; }
        collectAtDist(node.left, dist + 1);
        collectAtDist(node.right, dist + 1);
    }
}

// ============================================================
// APPROACH 3: BEST - Parent map + BFS (clean and readable)
// Time: O(N)  |  Space: O(N)
// Same as BruteForce but cleaner using HashMap<Integer, TreeNode>
// ============================================================
class Best {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public static List<Integer> solve(TreeNode root, TreeNode target, int k) {
        Map<Integer, TreeNode> parentMap = new HashMap<>();
        buildParents(root, null, parentMap);

        Queue<TreeNode> q = new LinkedList<>();
        Set<Integer> seen = new HashSet<>();
        q.offer(target);
        seen.add(target.val);
        int dist = 0;

        while (!q.isEmpty()) {
            if (dist == k) {
                List<Integer> res = new ArrayList<>();
                for (TreeNode n : q) res.add(n.val);
                Collections.sort(res);
                return res;
            }
            dist++;
            for (int sz = q.size(); sz > 0; sz--) {
                TreeNode cur = q.poll();
                for (TreeNode nb : Arrays.asList(cur.left, cur.right, parentMap.get(cur.val))) {
                    if (nb != null && !seen.contains(nb.val)) {
                        seen.add(nb.val);
                        q.offer(nb);
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    private static void buildParents(TreeNode node, TreeNode par, Map<Integer, TreeNode> map) {
        if (node == null) return;
        map.put(node.val, par);
        buildParents(node.left, node, map);
        buildParents(node.right, node, map);
    }
}

public class Solution {
    public static void main(String[] args) {
        // Tree:      3
        //           / \
        //          5   1
        //         / \ / \
        //        6  2 0  8
        //          / \
        //         7   4
        // target=5, k=2 -> [7,4,1]
        BruteForce.TreeNode root = new BruteForce.TreeNode(3);
        root.left = new BruteForce.TreeNode(5);
        root.right = new BruteForce.TreeNode(1);
        root.left.left = new BruteForce.TreeNode(6);
        root.left.right = new BruteForce.TreeNode(2);
        root.right.left = new BruteForce.TreeNode(0);
        root.right.right = new BruteForce.TreeNode(8);
        root.left.right.left = new BruteForce.TreeNode(7);
        root.left.right.right = new BruteForce.TreeNode(4);
        List<Integer> r1 = BruteForce.solve(root, root.left, 2);
        Collections.sort(r1);
        System.out.println("BruteForce: " + r1);  // [1, 4, 7]

        Optimal.TreeNode root2 = new Optimal.TreeNode(3);
        root2.left = new Optimal.TreeNode(5);
        root2.right = new Optimal.TreeNode(1);
        root2.left.left = new Optimal.TreeNode(6);
        root2.left.right = new Optimal.TreeNode(2);
        root2.right.left = new Optimal.TreeNode(0);
        root2.right.right = new Optimal.TreeNode(8);
        root2.left.right.left = new Optimal.TreeNode(7);
        root2.left.right.right = new Optimal.TreeNode(4);
        List<Integer> r2 = Optimal.solve(root2, root2.left, 2);
        Collections.sort(r2);
        System.out.println("Optimal:    " + r2);  // [1, 4, 7]

        Best.TreeNode root3 = new Best.TreeNode(3);
        root3.left = new Best.TreeNode(5);
        root3.right = new Best.TreeNode(1);
        root3.left.left = new Best.TreeNode(6);
        root3.left.right = new Best.TreeNode(2);
        root3.right.left = new Best.TreeNode(0);
        root3.right.right = new Best.TreeNode(8);
        root3.left.right.left = new Best.TreeNode(7);
        root3.left.right.right = new Best.TreeNode(4);
        System.out.println("Best:       " + Best.solve(root3, root3.left, 2));  // [1, 4, 7]
    }
}
