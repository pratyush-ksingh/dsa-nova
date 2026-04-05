/**
 * Problem: Number of Provinces (LeetCode #547)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an adjacency matrix isConnected, find the number of provinces
 * (connected components).
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: DFS
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    public static int findCircleNumDFS(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int provinces = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                provinces++;
                dfs(i, isConnected, visited);
            }
        }
        return provinces;
    }

    private static void dfs(int node, int[][] isConnected, boolean[] visited) {
        visited[node] = true;
        for (int j = 0; j < isConnected.length; j++) {
            if (isConnected[node][j] == 1 && !visited[j]) {
                dfs(j, isConnected, visited);
            }
        }
    }

    // ============================================================
    // APPROACH 2: BFS
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    public static int findCircleNumBFS(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int provinces = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                provinces++;
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(i);
                visited[i] = true;

                while (!queue.isEmpty()) {
                    int node = queue.poll();
                    for (int j = 0; j < n; j++) {
                        if (isConnected[node][j] == 1 && !visited[j]) {
                            visited[j] = true;
                            queue.offer(j);
                        }
                    }
                }
            }
        }
        return provinces;
    }

    // ============================================================
    // APPROACH 3: UNION-FIND
    // Time: O(n^2 * alpha(n)) ~ O(n^2)  |  Space: O(n)
    // ============================================================
    public static int findCircleNumUnionFind(int[][] isConnected) {
        int n = isConnected.length;
        int[] parent = new int[n];
        int[] rank = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected[i][j] == 1) {
                    union(parent, rank, i, j);
                }
            }
        }

        int provinces = 0;
        for (int i = 0; i < n; i++) {
            if (find(parent, i) == i) {
                provinces++;
            }
        }
        return provinces;
    }

    private static int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

    private static void union(int[] parent, int[] rank, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        if (rootX == rootY) return;

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Number of Provinces ===\n");

        int[][] m1 = {{1,1,0}, {1,1,0}, {0,0,1}};
        System.out.println("Test 1: Expected 2");
        System.out.println("  DFS:        " + findCircleNumDFS(m1));
        System.out.println("  BFS:        " + findCircleNumBFS(m1));
        System.out.println("  Union-Find: " + findCircleNumUnionFind(m1));

        int[][] m2 = {{1,0,0}, {0,1,0}, {0,0,1}};
        System.out.println("\nTest 2: Expected 3");
        System.out.println("  DFS:        " + findCircleNumDFS(m2));

        int[][] m3 = {{1,1,1}, {1,1,1}, {1,1,1}};
        System.out.println("\nTest 3: Expected 1");
        System.out.println("  DFS:        " + findCircleNumDFS(m3));

        int[][] m4 = {{1}};
        System.out.println("\nTest 4: Expected 1");
        System.out.println("  DFS:        " + findCircleNumDFS(m4));
    }
}
