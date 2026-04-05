import java.util.*;

/**
 * Problem: Most Stones Removed with Same Row or Column (LeetCode #947)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- DFS to Find Connected Components
    // Time: O(n^2)  |  Space: O(n)
    // Two stones are connected if they share a row or column.
    // DFS over stones with O(n^2) neighbor check.
    // ============================================================
    public static int bruteForce(int[][] stones) {
        int n = stones.length;
        boolean[] visited = new boolean[n];
        int components = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                components++;
                dfsBrute(i, stones, visited);
            }
        }

        return n - components;
    }

    private static void dfsBrute(int i, int[][] stones, boolean[] visited) {
        visited[i] = true;
        for (int j = 0; j < stones.length; j++) {
            if (!visited[j]) {
                if (stones[i][0] == stones[j][0] || stones[i][1] == stones[j][1]) {
                    dfsBrute(j, stones, visited);
                }
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Union-Find Grouping Stones
    // Time: O(n * alpha(n))  |  Space: O(n)
    // Union stones sharing row or column via row/col maps.
    // ============================================================
    static int[] parent, rank;

    static void initDSU(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false;
        if (rank[px] < rank[py]) { int t = px; px = py; py = t; }
        parent[py] = px;
        if (rank[px] == rank[py]) rank[px]++;
        return true;
    }

    public static int optimal(int[][] stones) {
        int n = stones.length;
        initDSU(n);
        Map<Integer, Integer> rowMap = new HashMap<>();
        Map<Integer, Integer> colMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int r = stones[i][0], c = stones[i][1];
            if (rowMap.containsKey(r)) {
                union(i, rowMap.get(r));
            } else {
                rowMap.put(r, i);
            }
            if (colMap.containsKey(c)) {
                union(i, colMap.get(c));
            } else {
                colMap.put(c, i);
            }
        }

        Set<Integer> roots = new HashSet<>();
        for (int i = 0; i < n; i++) roots.add(find(i));
        return n - roots.size();
    }

    // ============================================================
    // APPROACH 3: BEST -- Union-Find with Coordinate Mapping
    // Time: O(n * alpha(n))  |  Space: O(n)
    // Map rows and columns into single namespace (col + offset).
    // ============================================================
    static Map<Integer, Integer> parentMap = new HashMap<>();
    static Map<Integer, Integer> rankMap = new HashMap<>();

    static int findMap(int x) {
        parentMap.putIfAbsent(x, x);
        rankMap.putIfAbsent(x, 0);
        if (parentMap.get(x) != x) {
            parentMap.put(x, findMap(parentMap.get(x)));
        }
        return parentMap.get(x);
    }

    static void unionMap(int x, int y) {
        int px = findMap(x), py = findMap(y);
        if (px == py) return;
        int rx = rankMap.get(px), ry = rankMap.get(py);
        if (rx < ry) { int t = px; px = py; py = t; }
        parentMap.put(py, px);
        if (rx == ry) rankMap.put(px, rx + 1);
    }

    public static int best(int[][] stones) {
        parentMap.clear();
        rankMap.clear();
        int OFFSET = 100001;

        for (int[] s : stones) {
            unionMap(s[0], s[1] + OFFSET);
        }

        Set<Integer> roots = new HashSet<>();
        for (int[] s : stones) {
            roots.add(findMap(s[0]));
        }
        return stones.length - roots.size();
    }

    public static void main(String[] args) {
        System.out.println("=== Most Stones Removed ===\n");

        int[][] s1 = {{0,0},{0,1},{1,0},{1,2},{2,1},{2,2}};
        System.out.println("Brute:   " + bruteForce(s1));   // 5
        System.out.println("Optimal: " + optimal(s1));       // 5
        System.out.println("Best:    " + best(s1));          // 5

        int[][] s2 = {{0,0},{0,2},{1,1},{2,0},{2,2}};
        System.out.println("\nBrute:   " + bruteForce(s2));  // 3
        System.out.println("Optimal: " + optimal(s2));       // 3
        System.out.println("Best:    " + best(s2));          // 3
    }
}
