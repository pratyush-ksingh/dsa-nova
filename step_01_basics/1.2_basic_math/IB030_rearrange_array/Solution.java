import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n)  |  Space: O(n)
// Use an extra copy of the original array to fill A[i] = A[A[i]]
// ============================================================
class BruteForce {
    public static int[] solve(int[] A) {
        int n = A.length;
        int[] copy = A.clone();
        for (int i = 0; i < n; i++) A[i] = copy[copy[i]];
        return A;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(1)
// Encode: A[i] = A[i] + n * (A[A[i]] % n)
// After encoding, A[A[i]] % n gives original A[A[i]].
// Decode: A[i] = A[i] / n
// ============================================================
class Optimal {
    public static int[] solve(int[] A) {
        int n = A.length;
        // Encode: store new value in upper bits (multiply by n)
        for (int i = 0; i < n; i++) {
            A[i] += n * (A[A[i]] % n);
        }
        // Decode
        for (int i = 0; i < n; i++) {
            A[i] /= n;
        }
        return A;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(1)
// Same n*n encoding but with cleaner variable naming
// and explicit modulo to avoid any confusion.
// ============================================================
class Best {
    public static int[] solve(int[] A) {
        int n = A.length;
        // Phase 1: A[i] holds (old_A[i]) + n * (new_value)
        //          new_value = old_A[old_A[i]] = A[A[i]] % n
        for (int i = 0; i < n; i++) {
            int oldVal = A[i] % n;          // original A[i]
            int targetOld = A[oldVal] % n;  // original A[A[i]]
            A[i] += n * targetOld;
        }
        // Phase 2: extract new value
        for (int i = 0; i < n; i++) A[i] /= n;
        return A;
    }
}

public class Solution {
    static String arr2str(int[] a) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < a.length; i++) sb.append(a[i]).append(i < a.length-1 ? "," : "]");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Rearrange Array A[i] = A[A[i]] ===");

        int[] a1 = {3, 2, 0, 1};
        int[] a2 = {4, 0, 2, 1, 3};
        int[] a3 = {0};

        System.out.println("BruteForce:");
        System.out.println("  [3,2,0,1] -> " + arr2str(BruteForce.solve(a1.clone())));
        System.out.println("  [4,0,2,1,3] -> " + arr2str(BruteForce.solve(a2.clone())));

        System.out.println("Optimal:");
        System.out.println("  [3,2,0,1] -> " + arr2str(Optimal.solve(a1.clone())));
        System.out.println("  [4,0,2,1,3] -> " + arr2str(Optimal.solve(a2.clone())));

        System.out.println("Best:");
        System.out.println("  [3,2,0,1] -> " + arr2str(Best.solve(a1.clone())));
        System.out.println("  [4,0,2,1,3] -> " + arr2str(Best.solve(a2.clone())));
        // [3,2,0,1]: A[0]=A[3]=1, A[1]=A[2]=0, A[2]=A[0]=3, A[3]=A[1]=2 => [1,0,3,2]
        // [4,0,2,1,3]: A[0]=A[4]=3, A[1]=A[0]=4, A[2]=A[2]=2, A[3]=A[1]=0, A[4]=A[3]=1 => [3,4,2,0,1]
    }
}
