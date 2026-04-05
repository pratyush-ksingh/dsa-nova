import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Kosaraju's Two-Pass DFS
// Time: O(V + E)  |  Space: O(V + E)
// Pass 1: DFS on original graph, record finish order (stack)
// Pass 2: DFS on transpose graph in reverse finish order
// Each DFS tree in Pass 2 = one SCC
// ============================================================
class BruteForce {
    static int V;
    static List<List<Integer>> graph, transGraph;
    static boolean[] visited;
    static Deque<Integer> stack;

    public static int solve(int v, int[][] edges) {
        V = v;
        graph = new ArrayList<>();
        transGraph = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>());
            transGraph.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            graph.get(e[0]).add(e[1]);
            transGraph.get(e[1]).add(e[0]);
        }

        // Pass 1: Fill stack with finish order
        visited = new boolean[V];
        stack = new ArrayDeque<>();
        for (int i = 0; i < V; i++) {
            if (!visited[i]) dfs1(i);
        }

        // Pass 2: DFS on transpose in reverse finish order
        visited = new boolean[V];
        int sccCount = 0;
        while (!stack.isEmpty()) {
            int node = stack.pop();
            if (!visited[node]) {
                dfs2(node);
                sccCount++;
            }
        }
        return sccCount;
    }

    private static void dfs1(int node) {
        visited[node] = true;
        for (int nb : graph.get(node)) if (!visited[nb]) dfs1(nb);
        stack.push(node);
    }

    private static void dfs2(int node) {
        visited[node] = true;
        for (int nb : transGraph.get(node)) if (!visited[nb]) dfs2(nb);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Kosaraju's with explicit SCC groups returned
// Time: O(V + E)  |  Space: O(V + E)
// Same algorithm but collects and returns the actual SCC groups
// ============================================================
class Optimal {
    public static List<List<Integer>> solve(int v, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>(), radj = new ArrayList<>();
        for (int i = 0; i < v; i++) { adj.add(new ArrayList<>()); radj.add(new ArrayList<>()); }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            radj.get(e[1]).add(e[0]);
        }

        boolean[] visited = new boolean[v];
        Deque<Integer> order = new ArrayDeque<>();

        // Pass 1
        for (int i = 0; i < v; i++) {
            if (!visited[i]) {
                Deque<int[]> dfsStack = new ArrayDeque<>();
                dfsStack.push(new int[]{i, 0});
                visited[i] = true;
                while (!dfsStack.isEmpty()) {
                    int[] top = dfsStack.peek();
                    int node = top[0];
                    List<Integer> nbrs = adj.get(node);
                    if (top[1] < nbrs.size()) {
                        int nb = nbrs.get(top[1]++);
                        if (!visited[nb]) {
                            visited[nb] = true;
                            dfsStack.push(new int[]{nb, 0});
                        }
                    } else {
                        order.push(node);
                        dfsStack.pop();
                    }
                }
            }
        }

        // Pass 2 on transpose
        visited = new boolean[v];
        List<List<Integer>> sccs = new ArrayList<>();
        while (!order.isEmpty()) {
            int start = order.pop();
            if (!visited[start]) {
                List<Integer> scc = new ArrayList<>();
                Deque<Integer> dfsStack = new ArrayDeque<>();
                dfsStack.push(start);
                visited[start] = true;
                while (!dfsStack.isEmpty()) {
                    int node = dfsStack.pop();
                    scc.add(node);
                    for (int nb : radj.get(node)) {
                        if (!visited[nb]) {
                            visited[nb] = true;
                            dfsStack.push(nb);
                        }
                    }
                }
                sccs.add(scc);
            }
        }
        return sccs;
    }
}

// ============================================================
// APPROACH 3: BEST - Iterative Kosaraju's (avoids stack overflow on large graphs)
// Time: O(V + E)  |  Space: O(V + E)
// Fully iterative version of Kosaraju's algorithm
// ============================================================
class Best {
    public static int solve(int v, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>(), radj = new ArrayList<>();
        for (int i = 0; i < v; i++) { adj.add(new ArrayList<>()); radj.add(new ArrayList<>()); }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            radj.get(e[1]).add(e[0]);
        }

        // Pass 1: iterative DFS to get finish order
        boolean[] visited = new boolean[v];
        Deque<Integer> finishOrder = new ArrayDeque<>();
        for (int i = 0; i < v; i++) {
            if (!visited[i]) {
                Deque<int[]> stk = new ArrayDeque<>();
                stk.push(new int[]{i, 0});
                visited[i] = true;
                while (!stk.isEmpty()) {
                    int[] top = stk.peek();
                    int node = top[0];
                    List<Integer> nbrs = adj.get(node);
                    if (top[1] < nbrs.size()) {
                        int nb = nbrs.get(top[1]++);
                        if (!visited[nb]) {
                            visited[nb] = true;
                            stk.push(new int[]{nb, 0});
                        }
                    } else {
                        finishOrder.push(node);
                        stk.pop();
                    }
                }
            }
        }

        // Pass 2: iterative DFS on transpose
        visited = new boolean[v];
        int sccCount = 0;
        while (!finishOrder.isEmpty()) {
            int start = finishOrder.pop();
            if (!visited[start]) {
                sccCount++;
                Deque<Integer> stk = new ArrayDeque<>();
                stk.push(start);
                visited[start] = true;
                while (!stk.isEmpty()) {
                    int node = stk.pop();
                    for (int nb : radj.get(node)) {
                        if (!visited[nb]) {
                            visited[nb] = true;
                            stk.push(nb);
                        }
                    }
                }
            }
        }
        return sccCount;
    }
}

public class Solution {
    public static void main(String[] args) {
        // Graph: 0->1->2->0 (cycle), 1->3, 3->4->5->3 (cycle), 5->2
        // SCCs: {0,1,2}, {3,4,5}  -> 2 SCCs? Let me use a clearer example.
        // Classic example: 5 vertices
        // 1->0, 0->2, 2->1, 0->3, 3->4
        // SCCs: {0,1,2}, {3}, {4} -> 3 SCCs
        int[][] edges = {{1,0},{0,2},{2,1},{0,3},{3,4}};
        System.out.println("Test 1 (5 vertices): expected 3 SCCs");
        System.out.println("  BruteForce = " + BruteForce.solve(5, edges));
        List<List<Integer>> sccs = Optimal.solve(5, edges);
        System.out.println("  Optimal SCCs = " + sccs.size() + " -> " + sccs);
        System.out.println("  Best = " + Best.solve(5, edges));

        // No edges -> each node is its own SCC
        int[][] noEdges = {};
        System.out.println("Test 2 (4 isolated nodes): expected 4 SCCs");
        System.out.println("  BruteForce = " + BruteForce.solve(4, noEdges));
        System.out.println("  Best = " + Best.solve(4, noEdges));
    }
}
