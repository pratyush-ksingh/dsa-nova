import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n! * n)  |  Space: O(n!)
// Generate all permutations of digits, sort, find next entry
// ============================================================
class BruteForce {
    private static List<String> perms = new ArrayList<>();

    private static void generate(char[] arr, int start) {
        if (start == arr.length) { perms.add(new String(arr)); return; }
        for (int i = start; i < arr.length; i++) {
            char t = arr[start]; arr[start] = arr[i]; arr[i] = t;
            generate(arr, start + 1);
            t = arr[start]; arr[start] = arr[i]; arr[i] = t;
        }
    }

    public static String solve(String num) {
        perms.clear();
        generate(num.toCharArray(), 0);
        // deduplicate & sort
        TreeSet<String> sorted = new TreeSet<>(perms);
        // remove leading-zero entries
        sorted.removeIf(s -> s.charAt(0) == '0');
        String higher = sorted.higher(num);
        return higher == null ? "-1" : higher;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n log n)  |  Space: O(n)
// Classic next-permutation: find pivot, swap with successor,
// sort the suffix in ascending order.
// ============================================================
class Optimal {
    public static String solve(String num) {
        char[] d = num.toCharArray();
        int n = d.length;
        int pivot = -1;
        for (int i = n - 2; i >= 0; i--) {
            if (d[i] < d[i + 1]) { pivot = i; break; }
        }
        if (pivot == -1) return "-1";
        // find smallest digit > d[pivot] in suffix
        int best = pivot + 1;
        for (int i = pivot + 2; i < n; i++)
            if (d[i] > d[pivot] && d[i] <= d[best]) best = i;
        char t = d[pivot]; d[pivot] = d[best]; d[best] = t;
        Arrays.sort(d, pivot + 1, n);
        return new String(d);
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(1)
// Next-permutation in-place: suffix after pivot is guaranteed
// to be in descending order, so reverse it instead of sorting.
// ============================================================
class Best {
    public static String solve(String num) {
        char[] d = num.toCharArray();
        int n = d.length;
        // 1. Find rightmost i where d[i] < d[i+1]
        int i = n - 2;
        while (i >= 0 && d[i] >= d[i + 1]) i--;
        if (i < 0) return "-1";
        // 2. Find rightmost j > i where d[j] > d[i]
        int j = n - 1;
        while (d[j] <= d[i]) j--;
        // 3. Swap
        char t = d[i]; d[i] = d[j]; d[j] = t;
        // 4. Reverse suffix [i+1 .. n-1]
        int lo = i + 1, hi = n - 1;
        while (lo < hi) {
            t = d[lo]; d[lo] = d[hi]; d[hi] = t;
            lo++; hi--;
        }
        return new String(d);
    }
}

public class Solution {
    public static void main(String[] args) {
        String[] tests = {"218765", "1234", "4321", "534976", "999", "11"};
        System.out.println("=== Next Similar Number ===");
        System.out.printf("%-12s %-12s %-12s %-12s%n", "Input", "Brute", "Optimal", "Best");
        for (String t : tests) {
            System.out.printf("%-12s %-12s %-12s %-12s%n",
                t, BruteForce.solve(t), Optimal.solve(t), Best.solve(t));
        }
        // Expected: 251678, 1243, -1, 536479, -1, -1
    }
}
