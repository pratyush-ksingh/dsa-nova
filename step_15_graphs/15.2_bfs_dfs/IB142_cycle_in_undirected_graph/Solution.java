import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - DFS with parent tracking
// Time: O(V + E)  |  Space: O(V)
// ============================================================
// Run DFS from each unvisited node. Track the parent of each
// node in the DFS tree. If we reach an already-visited neighbor
// that is not the parent, a cycle exists.
// ============================================================

class BruteForce {
    boolean[] vis;
    List<List<Integer>> adj;

    private boolean dfs(int node, int parent) {
        vis[node] = true;
        for (int nb : adj.get(node)) {
            if (!vis[nb]) {
                if (dfs(nb, node)) return true;
            } else if (nb != parent) {
                return true; // back edge => cycle
            }
        }
        return false;
    }

    public int solve(int V, int[][] edges) {
        adj = new ArrayList<>();
        for (int i = 0; i <= V; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }
        vis = new boolean[V + 1];
        for (int i = 1; i <= V; i++) {
            if (!vis[i] && dfs(i, -1)) return 1;
        }
        return 0;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - BFS with parent tracking
// Time: O(V + E)  |  Space: O(V)
// ============================================================
// BFS-based cycle detection. For each BFS node, check its
// neighbors. If a neighbor is already visited and is not the
// parent, a cycle exists.
// ============================================================

class Optimal {
    public int solve(int V, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= V; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }
        boolean[] vis = new boolean[V + 1];

        for (int start = 1; start <= V; start++) {
            if (vis[start]) continue;
            Queue<int[]> q = new LinkedList<>(); // [node, parent]
            q.add(new int[]{start, -1});
            vis[start] = true;
            while (!q.isEmpty()) {
                int[] curr = q.poll();
                int node = curr[0], parent = curr[1];
                for (int nb : adj.get(node)) {
                    if (!vis[nb]) {
                        vis[nb] = true;
                        q.add(new int[]{nb, node});
                    } else if (nb != parent) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
}

// ============================================================
// APPROACH 3: BEST - Union-Find (Disjoint Set Union)
// Time: O(E * alpha(V)) ~ O(E)  |  Space: O(V)
// ============================================================
// For each edge (u,v): if find(u)==find(v), both nodes are
// already in the same component => adding this edge creates a
// cycle. Union-Find with path compression + rank is near O(1)
// per operation.
// Real-life use: detecting redundant network connections in
// Kruskal's MST, or circular dependencies in package managers.
// ============================================================

class Best {
    int[] parent, rank;

    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false; // already same component => cycle
        if (rank[px] < rank[py]) { int t = px; px = py; py = t; }
        parent[py] = px;
        if (rank[px] == rank[py]) rank[px]++;
        return true;
    }

    public int solve(int V, int[][] edges) {
        parent = new int[V + 1];
        rank   = new int[V + 1];
        for (int i = 1; i <= V; i++) parent[i] = i;

        for (int[] e : edges) {
            if (!union(e[0], e[1])) return 1;
        }
        return 0;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Cycle in Undirected Graph ===");

        // Test 1: cycle exists: 1-2-3-4-1
        int[][] e1 = {{1,2},{2,3},{3,4},{4,1}};
        System.out.println("Test1 Brute   (expect 1): " + new BruteForce().solve(4, e1));
        System.out.println("Test1 Optimal (expect 1): " + new Optimal().solve(4, e1));
        System.out.println("Test1 Best    (expect 1): " + new Best().solve(4, e1));

        // Test 2: no cycle: tree 1-2, 2-3, 3-4
        int[][] e2 = {{1,2},{2,3},{3,4}};
        System.out.println("Test2 Brute   (expect 0): " + new BruteForce().solve(4, e2));
        System.out.println("Test2 Optimal (expect 0): " + new Optimal().solve(4, e2));
        System.out.println("Test2 Best    (expect 0): " + new Best().solve(4, e2));

        // Test 3: disconnected, one component has cycle
        int[][] e3 = {{1,2},{3,4},{4,5},{5,3}};
        System.out.println("Test3 Best    (expect 1): " + new Best().solve(5, e3));

        // Test 4: single node
        int[][] e4 = {};
        System.out.println("Test4 Best    (expect 0): " + new Best().solve(1, e4));
    }
}
