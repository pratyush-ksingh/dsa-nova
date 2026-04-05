import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - DFS with recursive clone + HashMap
// Time: O(V + E)  |  Space: O(V)
// ============================================================
// Recursively clone each node. Use a HashMap to store already
// cloned nodes (original -> clone) to avoid infinite loops in
// cycles and to reuse already-created clone references.
// ============================================================

class Node {
    public int val;
    public List<Node> neighbors;
    public Node(int v) { val = v; neighbors = new ArrayList<>(); }
}

class BruteForce {
    Map<Node, Node> visited = new HashMap<>();

    public Node cloneGraph(Node node) {
        if (node == null) return null;
        if (visited.containsKey(node)) return visited.get(node);

        Node clone = new Node(node.val);
        visited.put(node, clone);

        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(cloneGraph(neighbor));
        }
        return clone;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - BFS with HashMap
// Time: O(V + E)  |  Space: O(V)
// ============================================================
// BFS processes nodes level by level. For each node dequeued,
// clone it and enqueue unvisited neighbors. HashMap maps
// original -> clone to handle back-edges without re-cloning.
// ============================================================

class Optimal {
    public Node cloneGraph(Node node) {
        if (node == null) return null;

        Map<Node, Node> map = new HashMap<>();
        Queue<Node> q = new LinkedList<>();

        map.put(node, new Node(node.val));
        q.add(node);

        while (!q.isEmpty()) {
            Node curr = q.poll();
            for (Node nb : curr.neighbors) {
                if (!map.containsKey(nb)) {
                    map.put(nb, new Node(nb.val));
                    q.add(nb);
                }
                map.get(curr).neighbors.add(map.get(nb));
            }
        }
        return map.get(node);
    }
}

// ============================================================
// APPROACH 3: BEST - Iterative DFS with explicit stack
// Time: O(V + E)  |  Space: O(V)
// ============================================================
// Iterative DFS avoids recursion stack overflow for very deep
// or large graphs. Uses explicit stack. Same asymptotic cost
// as recursive DFS but safer in production for large inputs.
// Real-life use: deep-copying dependency graphs in build
// systems or duplicating social network friend-lists in memory.
// ============================================================

class Best {
    public Node cloneGraph(Node node) {
        if (node == null) return null;

        Map<Node, Node> map = new HashMap<>();
        Deque<Node> stack = new ArrayDeque<>();

        map.put(node, new Node(node.val));
        stack.push(node);

        while (!stack.isEmpty()) {
            Node curr = stack.pop();
            for (Node nb : curr.neighbors) {
                if (!map.containsKey(nb)) {
                    map.put(nb, new Node(nb.val));
                    stack.push(nb);
                }
                map.get(curr).neighbors.add(map.get(nb));
            }
        }
        return map.get(node);
    }
}

// ============================================================
// TEST HARNESS
// ============================================================

public class Solution {
    // Build graph from adjacency list (1-indexed)
    static Node[] buildGraph(int[][] adj) {
        Node[] nodes = new Node[adj.length + 1];
        for (int i = 1; i <= adj.length; i++) nodes[i] = new Node(i);
        for (int i = 0; i < adj.length; i++) {
            for (int nb : adj[i]) {
                nodes[i + 1].neighbors.add(nodes[nb]);
            }
        }
        return nodes;
    }

    static void printGraph(Node start, int n) {
        Map<Node, Boolean> vis = new HashMap<>();
        Queue<Node> q = new LinkedList<>();
        q.add(start);
        vis.put(start, true);
        StringBuilder sb = new StringBuilder("Graph: ");
        while (!q.isEmpty()) {
            Node cur = q.poll();
            sb.append(cur.val).append("->[");
            for (Node nb : cur.neighbors) {
                sb.append(nb.val).append(",");
                if (!vis.containsKey(nb)) { vis.put(nb, true); q.add(nb); }
            }
            sb.append("] ");
        }
        System.out.println(sb);
    }

    public static void main(String[] args) {
        System.out.println("=== Clone Graph ===");

        // Test 1: 4-node cycle: 1-2-3-4-1
        // adj: 1:[2,4], 2:[1,3], 3:[2,4], 4:[3,1]
        int[][] adj1 = {{2,4},{1,3},{2,4},{3,1}};
        Node[] g1 = buildGraph(adj1);

        Node orig = g1[1];
        Node cloneBFS = new Optimal().cloneGraph(orig);
        System.out.print("Original  "); printGraph(orig, 4);
        System.out.print("CloneBFS  "); printGraph(cloneBFS, 4);
        System.out.println("Different objects? " + (orig != cloneBFS));

        Node cloneDFS = new Best().cloneGraph(orig);
        System.out.print("CloneDFS  "); printGraph(cloneDFS, 4);

        // Test 2: Single node no neighbors
        Node single = new Node(1);
        Node cloneSingle = new Best().cloneGraph(single);
        System.out.println("Single node clone val (expect 1): " + cloneSingle.val);
        System.out.println("Single different obj? " + (single != cloneSingle));

        // Test 3: null input
        System.out.println("Null input (expect null): " + new Best().cloneGraph(null));
    }
}
