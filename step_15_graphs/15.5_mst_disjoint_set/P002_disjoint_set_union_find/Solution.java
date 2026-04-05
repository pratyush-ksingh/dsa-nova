/**
 * Problem: Disjoint Set Union Find
 * Difficulty: MEDIUM | XP: 25
 *
 * Implement Union-Find with path compression and union by rank.
 * Near O(1) amortized per operation (O(alpha(n)) technically).
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: NAIVE UNION-FIND (No Optimizations)
    // Time: O(n) per find/union  |  Space: O(n)
    // ============================================================
    static class NaiveDSU {
        int[] parent;

        NaiveDSU(int n) {
            parent = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        int find(int x) {
            while (parent[x] != x) {
                x = parent[x]; // Walk up to root -- O(n) worst case
            }
            return x;
        }

        void union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX != rootY) {
                parent[rootX] = rootY; // Arbitrary attachment
            }
        }

        boolean connected(int x, int y) {
            return find(x) == find(y);
        }
    }

    // ============================================================
    // APPROACH 2: UNION BY RANK (Without Path Compression)
    // Time: O(log n) per operation  |  Space: O(n)
    // ============================================================
    static class RankDSU {
        int[] parent;
        int[] rank;

        RankDSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        int find(int x) {
            while (parent[x] != x) {
                x = parent[x]; // No compression, just walk up
            }
            return x;
        }

        void union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX == rootY) return;

            // Attach shorter tree under taller tree
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }

        boolean connected(int x, int y) {
            return find(x) == find(y);
        }
    }

    // ============================================================
    // APPROACH 3: PATH COMPRESSION + UNION BY RANK (BEST)
    // Time: O(alpha(n)) amortized  |  Space: O(n)
    // ============================================================
    static class OptimalDSU {
        int[] parent;
        int[] rank;
        int components; // Track number of connected components

        OptimalDSU(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        /**
         * Find with PATH COMPRESSION.
         * Recursively walk to root, then set parent[x] = root for every
         * node on the path. Future finds on these nodes are O(1).
         */
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        /**
         * Union by RANK.
         * Attach shorter tree under taller tree's root.
         * If same rank, pick one as root and increment its rank.
         * Returns true if a merge happened (different components).
         */
        boolean union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX == rootY) return false; // Already in same set

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }

            components--;
            return true;
        }

        boolean connected(int x, int y) {
            return find(x) == find(y);
        }

        int getComponents() {
            return components;
        }
    }

    // ============================================================
    // BONUS: UNION BY SIZE (Alternative to rank)
    // Same amortized complexity, but size[] tracks actual subtree sizes
    // ============================================================
    static class SizeDSU {
        int[] parent;
        int[] size;

        SizeDSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        boolean union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX == rootY) return false;

            // Merge smaller set into larger set
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
            return true;
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Disjoint Set Union Find ===\n");

        // --- Test Naive DSU ---
        System.out.println("--- Naive DSU ---");
        NaiveDSU naive = new NaiveDSU(5);
        naive.union(0, 1);
        naive.union(2, 3);
        System.out.println("0-1 connected: " + naive.connected(0, 1));   // true
        System.out.println("0-2 connected: " + naive.connected(0, 2));   // false
        naive.union(1, 3);
        System.out.println("0-3 connected: " + naive.connected(0, 3));   // true
        System.out.println("0-4 connected: " + naive.connected(0, 4));   // false

        // --- Test Optimal DSU ---
        System.out.println("\n--- Optimal DSU (Path Compression + Union by Rank) ---");
        OptimalDSU dsu = new OptimalDSU(7);
        System.out.println("Initial components: " + dsu.getComponents());  // 7

        dsu.union(0, 1);
        dsu.union(2, 3);
        dsu.union(4, 5);
        System.out.println("After 3 unions: " + dsu.getComponents());     // 4

        dsu.union(0, 2);
        System.out.println("After union(0,2): " + dsu.getComponents());   // 3

        dsu.union(3, 5);
        System.out.println("After union(3,5): " + dsu.getComponents());   // 2

        System.out.println("0-5 connected: " + dsu.connected(0, 5));      // true
        System.out.println("0-6 connected: " + dsu.connected(0, 6));      // false

        dsu.union(0, 6);
        System.out.println("After union(0,6): " + dsu.getComponents());   // 1
        System.out.println("All connected: " + dsu.connected(3, 6));      // true

        // Verify path compression: print parents
        System.out.print("Parents after operations: [");
        for (int i = 0; i < 7; i++) {
            dsu.find(i); // Trigger compression
            System.out.print(dsu.parent[i] + (i < 6 ? ", " : ""));
        }
        System.out.println("]");

        // --- Test Size DSU ---
        System.out.println("\n--- Size DSU ---");
        SizeDSU sizeDsu = new SizeDSU(5);
        sizeDsu.union(0, 1);
        sizeDsu.union(2, 3);
        sizeDsu.union(0, 2);
        System.out.println("Size of group containing 0: " + sizeDsu.getSize(0)); // 4
        System.out.println("Size of group containing 4: " + sizeDsu.getSize(4)); // 1

        // --- Test redundant union ---
        System.out.println("\n--- Redundant Union Test ---");
        OptimalDSU dsu2 = new OptimalDSU(3);
        System.out.println("union(0,1) merged: " + dsu2.union(0, 1));   // true
        System.out.println("union(0,1) merged: " + dsu2.union(0, 1));   // false (redundant)
        System.out.println("Components: " + dsu2.getComponents());       // 2
    }
}
