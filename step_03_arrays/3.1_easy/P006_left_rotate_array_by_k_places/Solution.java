import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n*k)  |  Space: O(1)
// Rotate left by 1 a total of k times
// ============================================================
class BruteForce {
    public static int[] solve(int[] arr, int k) {
        int n = arr.length;
        k = k % n;
        for (int i = 0; i < k; i++) {
            int first = arr[0];
            for (int j = 0; j < n - 1; j++) arr[j] = arr[j + 1];
            arr[n - 1] = first;
        }
        return arr;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(k)
// Store first k elements in temp array, shift rest left, copy back
// ============================================================
class Optimal {
    public static int[] solve(int[] arr, int k) {
        int n = arr.length;
        k = k % n;
        int[] temp = Arrays.copyOfRange(arr, 0, k);
        System.arraycopy(arr, k, arr, 0, n - k);
        System.arraycopy(temp, 0, arr, n - k, k);
        return arr;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(1)
// Reverse the first k elements, reverse the rest, reverse all.
// This achieves left rotation in-place with no extra array.
// ============================================================
class Best {
    private static void reverse(int[] arr, int lo, int hi) {
        while (lo < hi) {
            int t = arr[lo]; arr[lo] = arr[hi]; arr[hi] = t;
            lo++; hi--;
        }
    }

    public static int[] solve(int[] arr, int k) {
        int n = arr.length;
        k = k % n;
        reverse(arr, 0, k - 1);
        reverse(arr, k, n - 1);
        reverse(arr, 0, n - 1);
        return arr;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Left Rotate Array by K Places ===");

        int[][] inputs = {{1,2,3,4,5}, {1,2,3,4,5,6,7}, {3,99,-1,100}};
        int[] ks = {2, 3, 2};
        int[][] expected = {{3,4,5,1,2}, {4,5,6,7,1,2,3}, {-1,100,3,99}};

        for (int t = 0; t < inputs.length; t++) {
            int[] bf = BruteForce.solve(inputs[t].clone(), ks[t]);
            int[] op = Optimal.solve(inputs[t].clone(), ks[t]);
            int[] bst = Best.solve(inputs[t].clone(), ks[t]);
            System.out.println("Input=" + Arrays.toString(inputs[t]) + " k=" + ks[t]);
            System.out.println("  BF  =" + Arrays.toString(bf));
            System.out.println("  OPT =" + Arrays.toString(op));
            System.out.println("  BEST=" + Arrays.toString(bst));
            System.out.println("  EXP =" + Arrays.toString(expected[t]));
        }
    }
}
