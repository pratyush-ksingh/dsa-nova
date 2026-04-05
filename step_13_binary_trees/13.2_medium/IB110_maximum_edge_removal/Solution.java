/**
 * Problem: Maximum Edge Removal
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given an undirected tree with N nodes, find the maximum number of edges
 * that can be removed such that every connected component has an even number of nodes.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (DFS subtree sizes, count even subtrees)
// Same algorithm as Optimal but written verbosely with explicit visited array.
// Time: O(n) | Space: O(n)
// ============================================================
class BruteForce {
    static List<Integer>[] adj;
    static boolean[] visited;
    static int count;

    @SuppressWarnings("unchecked")
    public static int solve(int n, int[][] edges) {
        if (n % 2 == 1) return 0;

        adj = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) adj[i] = new ArrayList<>();
        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        visited = new boolean[n + 1];
        count = 0;
        dfs(1);
        return count;
    }

    static int dfs(int node) {
        visited[node] = true;
        int size = 1;
        for (int neighbor : adj[node]) {
            if (!visited[neighbor]) {
                int childSize = dfs(neighbor);
                size += childSize;
                if (childSize % 2 == 0) count++;
            }
        }
        return size;
    }
}

// ============================================================
// Approach 2: Optimal (DFS with parent tracking, no visited array)
// Time: O(n) | Space: O(n)
// ============================================================
class Optimal {
    static List<Integer>[] adj2;
    static int ans;

    @SuppressWarnings("unchecked")
    public static int solve(int n, int[][] edges) {
        if (n % 2 == 1) return 0;

        adj2 = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) adj2[i] = new ArrayList<>();
        for (int[] e : edges) {
            adj2[e[0]].add(e[1]);
            adj2[e[1]].add(e[0]);
        }

        ans = 0;
        dfs(1, -1);
        return ans;
    }

    static int dfs(int node, int parent) {
        int size = 1;
        for (int neighbor : adj2[node]) {
            if (neighbor != parent) {
                int childSize = dfs(neighbor, node);
                size += childSize;
                if (childSize % 2 == 0) ans++;
            }
        }
        return size;
    }
}

// ============================================================
// Approach 3: Best (Iterative DFS — avoids stack overflow for large n)
// Time: O(n) | Space: O(n)
// ============================================================
class Best {
    @SuppressWarnings("unchecked")
    public static int solve(int n, int[][] edges) {
        if (n % 2 == 1) return 0;

        List<Integer>[] adj = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) adj[i] = new ArrayList<>();
        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        int[] parent = new int[n + 1];
        Arrays.fill(parent, -1);
        int[] size = new int[n + 1];
        Arrays.fill(size, 1);
        boolean[] visited = new boolean[n + 1];

        // Iterative DFS to determine post-order
        Deque<Integer> stack = new ArrayDeque<>();
        List<Integer> order = new ArrayList<>();
        stack.push(1);
        visited[1] = true;

        // Simple DFS order (not post-order from stack directly — use two-stack trick)
        // Use explicit parent tracking for bottom-up processing
        Deque<Integer> toVisit = new ArrayDeque<>();
        toVisit.push(1);
        visited[1] = true;
        List<Integer> dfsOrder = new ArrayList<>();

        // BFS-like but DFS order using stack
        Deque<Integer> dfsStack = new ArrayDeque<>();
        boolean[] inStack = new boolean[n + 1];
        dfsStack.push(1);
        inStack[1] = true;

        // Two-pointer DFS for post-order
        int[] iterIdx = new int[n + 1]; // index into adj list for each node
        Deque<Integer> postStack = new ArrayDeque<>();
        boolean[] vis2 = new boolean[n + 1];
        postStack.push(1);
        vis2[1] = true;
        List<Integer> postOrder = new ArrayList<>();

        while (!postStack.isEmpty()) {
            int cur = postStack.peek();
            boolean expanded = false;
            List<Integer> neighbors = adj[cur];
            while (iterIdx[cur] < neighbors.size()) {
                int nb = neighbors.get(iterIdx[cur]++);
                if (!vis2[nb]) {
                    vis2[nb] = true;
                    parent[nb] = cur;
                    postStack.push(nb);
                    expanded = true;
                    break;
                }
            }
            if (!expanded) {
                postOrder.add(postStack.pop());
            }
        }

        // Compute subtree sizes bottom-up using post-order
        int count = 0;
        for (int i = 0; i < postOrder.size() - 1; i++) { // exclude root (last)
            int node = postOrder.get(i);
            int par = parent[node];
            size[par] += size[node];
            if (size[node] % 2 == 0) count++;
        }
        return count;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Maximum Edge Removal ===\n");

        int[] ns = {10, 4, 6, 2};
        int[][][] edgesArr = {
            {{2,1},{3,1},{4,3},{5,2},{6,1},{7,2},{8,6},{9,8},{10,8}},
            {{1,2},{2,3},{3,4}},
            {{1,2},{1,3},{1,4},{4,5},{4,6}},
            {{1,2}},
        };
        int[] expecteds = {2, 1, 2, 1};

        for (int t = 0; t < ns.length; t++) {
            int b = BruteForce.solve(ns[t], edgesArr[t]);
            int o = Optimal.solve(ns[t], edgesArr[t]);
            int h = Best.solve(ns[t], edgesArr[t]);
            boolean pass = b == expecteds[t] && o == expecteds[t] && h == expecteds[t];

            System.out.println("n=" + ns[t]);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + h);
            System.out.println("Expected: " + expecteds[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
