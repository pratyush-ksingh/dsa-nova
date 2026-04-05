/**
 * Problem: Celebrity Problem
 * Difficulty: MEDIUM | XP: 25
 *
 * In a party of n people, find the celebrity -- known by everyone but knows nobody.
 * Given a knows(a, b) API.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Check All Pairs)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static int findCelebrity(int n, int[][] matrix) {
        for (int candidate = 0; candidate < n; candidate++) {
            boolean knowsSomeone = false;
            for (int j = 0; j < n; j++) {
                if (candidate != j && matrix[candidate][j] == 1) {
                    knowsSomeone = true;
                    break;
                }
            }
            if (knowsSomeone) continue;

            boolean knownByAll = true;
            for (int j = 0; j < n; j++) {
                if (candidate != j && matrix[j][candidate] != 1) {
                    knownByAll = false;
                    break;
                }
            }
            if (knownByAll) return candidate;
        }
        return -1;
    }
}

// ============================================================
// Approach 2: Optimal (Stack Elimination)
// Time: O(n) | Space: O(n)
// ============================================================
class Optimal {
    public static int findCelebrity(int n, int[][] matrix) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            stack.push(i);
        }

        // Elimination phase
        while (stack.size() > 1) {
            int a = stack.pop();
            int b = stack.pop();
            if (matrix[a][b] == 1) {
                // a knows b -> a not celebrity
                stack.push(b);
            } else {
                // a doesn't know b -> b not celebrity
                stack.push(a);
            }
        }

        int candidate = stack.pop();

        // Verification phase
        for (int i = 0; i < n; i++) {
            if (i != candidate) {
                if (matrix[candidate][i] == 1 || matrix[i][candidate] != 1) {
                    return -1;
                }
            }
        }
        return candidate;
    }
}

// ============================================================
// Approach 3: Best (Two-Pointer / Single Candidate Elimination)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static int findCelebrity(int n, int[][] matrix) {
        // Find candidate by elimination
        int candidate = 0;
        for (int i = 1; i < n; i++) {
            if (matrix[candidate][i] == 1) {
                candidate = i;
            }
        }

        // Verify the candidate
        for (int i = 0; i < n; i++) {
            if (i != candidate) {
                if (matrix[candidate][i] == 1 || matrix[i][candidate] != 1) {
                    return -1;
                }
            }
        }
        return candidate;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Celebrity Problem ===\n");

        // Test 1: Person 1 is celebrity
        int[][] m1 = {{0,1,0},{0,0,0},{0,1,0}};
        // Test 2: No celebrity
        int[][] m2 = {{0,1,0},{0,0,1},{1,0,0}};
        // Test 3: Person 2 is celebrity
        int[][] m3 = {{0,0,1,0},{0,0,1,0},{0,0,0,0},{0,0,1,0}};
        // Test 4: Single person
        int[][] m4 = {{0}};

        int[][][] matrices = {m1, m2, m3, m4};
        int[] sizes = {3, 3, 4, 1};
        int[] expected = {1, -1, 2, 0};

        for (int t = 0; t < matrices.length; t++) {
            int b = BruteForce.findCelebrity(sizes[t], matrices[t]);
            int o = Optimal.findCelebrity(sizes[t], matrices[t]);
            int r = Best.findCelebrity(sizes[t], matrices[t]);
            boolean pass = b == expected[t] && o == expected[t] && r == expected[t];

            System.out.println("n = " + sizes[t] + ", expected = " + expected[t]);
            System.out.println("  Brute:   " + b);
            System.out.println("  Optimal: " + o);
            System.out.println("  Best:    " + r);
            System.out.println("  " + (pass ? "PASS" : "FAIL"));
            System.out.println();
        }
    }
}
