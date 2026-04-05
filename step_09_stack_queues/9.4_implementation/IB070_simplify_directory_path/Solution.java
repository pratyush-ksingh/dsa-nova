import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n)  |  Space: O(n)
// Split on '/', process each token with a list acting as stack.
// Use String.replaceAll to collapse multiple slashes.
// ============================================================
class BruteForce {
    public static String solve(String path) {
        // Normalize consecutive slashes
        String[] parts = path.split("/+");
        List<String> stack = new ArrayList<>();
        for (String part : parts) {
            if (part.equals("..")) {
                if (!stack.isEmpty()) stack.remove(stack.size() - 1);
            } else if (!part.isEmpty() && !part.equals(".")) {
                stack.add(part);
            }
        }
        return "/" + String.join("/", stack);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(n)
// Use a Deque as a stack: push directory names, pop on "..".
// Build result by joining stack contents.
// ============================================================
class Optimal {
    public static String solve(String path) {
        Deque<String> stack = new ArrayDeque<>();
        for (String part : path.split("/")) {
            switch (part) {
                case "..":
                    if (!stack.isEmpty()) stack.pollLast();
                    break;
                case ".":
                case "":
                    break;
                default:
                    stack.offerLast(part);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String dir : stack) sb.append("/").append(dir);
        return sb.length() == 0 ? "/" : sb.toString();
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(n)
// Same Deque approach; identical complexity — this is the
// canonical O(n) solution. No further asymptotic improvement possible.
// ============================================================
class Best {
    public static String solve(String path) {
        Deque<String> stack = new ArrayDeque<>();
        String[] tokens = path.split("/");
        for (String token : tokens) {
            if (token.equals("..")) {
                if (!stack.isEmpty()) stack.pop();
            } else if (!token.isEmpty() && !token.equals(".")) {
                stack.push(token);
            }
        }
        // Stack is LIFO; iterate in insertion order using ArrayDeque as list
        List<String> dirs = new ArrayList<>(stack);
        Collections.reverse(dirs);
        return "/" + String.join("/", dirs);
    }
}

public class Solution {
    public static void main(String[] args) {
        String[][] tests = {
            {"/home/",                 "/home"},
            {"/../",                   "/"},
            {"/home//foo/",            "/home/foo"},
            {"/a/./b/../../c/",        "/c"},
            {"/a/../../b/../c//.//",   "/c"},
            {"/",                      "/"},
            {"/foo/../bar",            "/bar"},
        };

        System.out.println("=== Simplify Directory Path ===");
        for (String[] t : tests) {
            String b    = BruteForce.solve(t[0]);
            String o    = Optimal.solve(t[0]);
            String best = Best.solve(t[0]);
            String status = (b.equals(t[1]) && o.equals(t[1]) && best.equals(t[1])) ? "PASS" : "FAIL";
            System.out.printf("Input: %-30s | Brute: %-15s | Optimal: %-15s | Best: %-15s | Expected: %-15s | %s%n",
                    t[0], b, o, best, t[1], status);
        }
    }
}
