/**
 * Problem: Graph Representation
 * Difficulty: EASY | XP: 10
 *
 * Implement graph using adjacency matrix and adjacency list.
 * Convert between them.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Adjacency Matrix
// Build: O(V^2 + E) | Space: O(V^2)
// Edge lookup: O(1) | Neighbor scan: O(V)
// ============================================================
class AdjacencyMatrix {
    private int[][] matrix;
    private int n;

    public AdjacencyMatrix(int n, int[][] edges) {
        this.n = n;
        this.matrix = new int[n][n];
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            matrix[u][v] = 1;
            matrix[v][u] = 1; // undirected
        }
    }

    public boolean hasEdge(int u, int v) {
        return matrix[u][v] == 1;
    }

    public List<Integer> getNeighbors(int u) {
        List<Integer> neighbors = new ArrayList<>();
        for (int v = 0; v < n; v++) {
            if (matrix[u][v] == 1) {
                neighbors.add(v);
            }
        }
        return neighbors;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    // Convert matrix to adjacency list
    public List<List<Integer>> toAdjList() {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    adj.get(i).add(j);
                }
            }
        }
        return adj;
    }

    public void print() {
        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < n; i++) {
            System.out.println("  " + Arrays.toString(matrix[i]));
        }
    }
}

// ============================================================
// Approach 2: Adjacency List (Preferred for most problems)
// Build: O(V + E) | Space: O(V + E)
// Edge lookup: O(degree) | Neighbor scan: O(degree)
// ============================================================
class AdjacencyList {
    private List<List<Integer>> adj;
    private int n;

    public AdjacencyList(int n, int[][] edges) {
        this.n = n;
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            adj.get(u).add(v);
            adj.get(v).add(u); // undirected
        }
    }

    public boolean hasEdge(int u, int v) {
        return adj.get(u).contains(v);
    }

    public List<Integer> getNeighbors(int u) {
        return adj.get(u);
    }

    public List<List<Integer>> getAdj() {
        return adj;
    }

    // Convert adjacency list to matrix
    public int[][] toMatrix() {
        int[][] matrix = new int[n][n];
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                matrix[u][v] = 1;
            }
        }
        return matrix;
    }

    public void print() {
        System.out.println("Adjacency List:");
        for (int i = 0; i < n; i++) {
            System.out.println("  " + i + " -> " + adj.get(i));
        }
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Graph Representation ===\n");

        // Test Case 1: Linear graph 0-1-2-3
        int[][] edges1 = {{0, 1}, {1, 2}, {2, 3}};
        System.out.println("--- Test 1: Linear graph (n=4) ---");
        AdjacencyMatrix mat1 = new AdjacencyMatrix(4, edges1);
        AdjacencyList list1 = new AdjacencyList(4, edges1);
        mat1.print();
        list1.print();
        System.out.println("Edge (1,2)? Matrix: " + mat1.hasEdge(1, 2)
                + " | List: " + list1.hasEdge(1, 2));
        System.out.println("Edge (0,3)? Matrix: " + mat1.hasEdge(0, 3)
                + " | List: " + list1.hasEdge(0, 3));
        System.out.println("Neighbors of 1: " + list1.getNeighbors(1));
        System.out.println();

        // Test Case 2: Complete graph K3
        int[][] edges2 = {{0, 1}, {0, 2}, {1, 2}};
        System.out.println("--- Test 2: Complete graph K3 ---");
        AdjacencyMatrix mat2 = new AdjacencyMatrix(3, edges2);
        AdjacencyList list2 = new AdjacencyList(3, edges2);
        mat2.print();
        list2.print();
        System.out.println();

        // Test Case 3: No edges
        int[][] edges3 = {};
        System.out.println("--- Test 3: No edges (n=3) ---");
        AdjacencyList list3 = new AdjacencyList(3, edges3);
        list3.print();
        System.out.println();

        // Test Case 4: Conversion
        System.out.println("--- Test 4: Conversions ---");
        System.out.println("Matrix->List conversion:");
        List<List<Integer>> converted = mat1.toAdjList();
        for (int i = 0; i < 4; i++) {
            System.out.println("  " + i + " -> " + converted.get(i));
        }
        System.out.println("List->Matrix conversion:");
        int[][] convertedMat = list1.toMatrix();
        for (int[] row : convertedMat) {
            System.out.println("  " + Arrays.toString(row));
        }
        System.out.println("\nAll tests completed.");
    }
}
