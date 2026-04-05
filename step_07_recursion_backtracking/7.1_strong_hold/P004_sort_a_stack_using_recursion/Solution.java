/**
 * Problem: Sort a Stack using Recursion
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Convert to list, sort, push back
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    public static void bruteForce(Stack<Integer> stack) {
        List<Integer> list = new ArrayList<>();
        while (!stack.isEmpty()) list.add(stack.pop());
        Collections.sort(list);
        for (int val : list) stack.push(val);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Recursive sort + recursive insert
    // Time: O(n^2)  |  Space: O(n) recursion stack
    // ============================================================
    public static void optimal(Stack<Integer> stack) {
        if (stack.isEmpty()) return;
        int top = stack.pop();
        optimal(stack);
        insertSorted(stack, top);
    }

    private static void insertSorted(Stack<Integer> stack, int val) {
        if (stack.isEmpty() || stack.peek() <= val) {
            stack.push(val);
            return;
        }
        int top = stack.pop();
        insertSorted(stack, val);
        stack.push(top);
    }

    // ============================================================
    // APPROACH 3: BEST - Iterative using a temporary stack
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    public static void best(Stack<Integer> stack) {
        Stack<Integer> temp = new Stack<>();
        while (!stack.isEmpty()) {
            int curr = stack.pop();
            while (!temp.isEmpty() && temp.peek() > curr) {
                stack.push(temp.pop());
            }
            temp.push(curr);
        }
        // Transfer back so smallest is at bottom, largest at top
        while (!temp.isEmpty()) stack.push(temp.pop());
    }

    public static void main(String[] args) {
        System.out.println("=== Sort a Stack using Recursion ===");
        Stack<Integer> s1 = new Stack<>();
        for (int v : new int[]{5, 1, 4, 2, 3}) s1.push(v);
        bruteForce(s1);
        System.out.println("Brute Force: " + s1);

        Stack<Integer> s2 = new Stack<>();
        for (int v : new int[]{5, 1, 4, 2, 3}) s2.push(v);
        optimal(s2);
        System.out.println("Optimal:     " + s2);

        Stack<Integer> s3 = new Stack<>();
        for (int v : new int[]{5, 1, 4, 2, 3}) s3.push(v);
        best(s3);
        System.out.println("Best:        " + s3);
    }
}
