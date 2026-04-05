import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Build graph from adjacent word pairs, DFS topo sort
// Time: O(C) where C = total characters  |  Space: O(1) since at most 26 chars
// Compare adjacent words to extract ordering constraints, then topological sort
// ============================================================
class BruteForce {
    static Map<Character, Set<Character>> graph;
    static Map<Character, Integer> state; // 0=unvisited, 1=visiting, 2=visited

    public static String solve(String[] words) {
        graph = new HashMap<>();
        state = new HashMap<>();
        Set<Character> chars = new HashSet<>();
        for (String w : words) for (char c : w.toCharArray()) chars.add(c);
        for (char c : chars) { graph.put(c, new HashSet<>()); state.put(c, 0); }

        // Build edges from adjacent word comparisons
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i + 1];
            int minLen = Math.min(w1.length(), w2.length());
            boolean found = false;
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    graph.get(w1.charAt(j)).add(w2.charAt(j));
                    found = true;
                    break;
                }
            }
            // If w1 is prefix of w2 but w1.length > w2.length -> invalid
            if (!found && w1.length() > w2.length()) return "";
        }

        StringBuilder result = new StringBuilder();
        for (char c : chars) {
            if (state.get(c) == 0) {
                if (!dfs(c, result)) return "";
            }
        }
        return result.reverse().toString();
    }

    private static boolean dfs(char c, StringBuilder sb) {
        state.put(c, 1);
        for (char nb : graph.get(c)) {
            if (state.get(nb) == 1) return false; // cycle
            if (state.get(nb) == 0 && !dfs(nb, sb)) return false;
        }
        state.put(c, 2);
        sb.append(c);
        return true;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Kahn's BFS topological sort
// Time: O(C + E) where C = total chars, E = ordering constraints
// Space: O(1) - at most 26 nodes
// ============================================================
class Optimal {
    public static String solve(String[] words) {
        Map<Character, Set<Character>> adj = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        // Initialize all chars
        for (String w : words)
            for (char c : w.toCharArray()) {
                adj.putIfAbsent(c, new HashSet<>());
                inDegree.putIfAbsent(c, 0);
            }

        // Build graph from adjacent word pairs
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i + 1];
            int minLen = Math.min(w1.length(), w2.length());
            boolean found = false;
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    char from = w1.charAt(j), to = w2.charAt(j);
                    if (!adj.get(from).contains(to)) {
                        adj.get(from).add(to);
                        inDegree.merge(to, 1, Integer::sum);
                    }
                    found = true;
                    break;
                }
            }
            if (!found && w1.length() > w2.length()) return "";
        }

        // Kahn's BFS
        Queue<Character> queue = new LinkedList<>();
        for (char c : inDegree.keySet()) if (inDegree.get(c) == 0) queue.offer(c);
        StringBuilder result = new StringBuilder();
        while (!queue.isEmpty()) {
            char c = queue.poll();
            result.append(c);
            for (char nb : adj.get(c)) {
                inDegree.merge(nb, -1, Integer::sum);
                if (inDegree.get(nb) == 0) queue.offer(nb);
            }
        }
        return result.length() == inDegree.size() ? result.toString() : "";
    }
}

// ============================================================
// APPROACH 3: BEST - Kahn's with sorted queue for lexicographically smallest result
// Time: O(C log C + E)  |  Space: O(1)
// Use a priority queue to get consistent/deterministic ordering
// ============================================================
class Best {
    public static String solve(String[] words) {
        Map<Character, Set<Character>> adj = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();
        for (String w : words) for (char c : w.toCharArray()) {
            adj.putIfAbsent(c, new HashSet<>());
            inDegree.putIfAbsent(c, 0);
        }
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i + 1];
            int minLen = Math.min(w1.length(), w2.length());
            boolean found = false;
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    char from = w1.charAt(j), to = w2.charAt(j);
                    if (!adj.get(from).contains(to)) {
                        adj.get(from).add(to);
                        inDegree.merge(to, 1, Integer::sum);
                    }
                    found = true;
                    break;
                }
            }
            if (!found && w1.length() > w2.length()) return "";
        }
        PriorityQueue<Character> pq = new PriorityQueue<>();
        for (char c : inDegree.keySet()) if (inDegree.get(c) == 0) pq.offer(c);
        StringBuilder result = new StringBuilder();
        while (!pq.isEmpty()) {
            char c = pq.poll();
            result.append(c);
            for (char nb : adj.get(c)) {
                inDegree.merge(nb, -1, Integer::sum);
                if (inDegree.get(nb) == 0) pq.offer(nb);
            }
        }
        return result.length() == inDegree.size() ? result.toString() : "";
    }
}

public class Solution {
    public static void main(String[] args) {
        // ["wrt","wrf","er","ett","rftt"] -> "wertf"
        String[] words1 = {"wrt", "wrf", "er", "ett", "rftt"};
        System.out.println("Test 1 BruteForce: " + BruteForce.solve(words1));
        System.out.println("Test 1 Optimal:    " + Optimal.solve(words1));
        System.out.println("Test 1 Best:       " + Best.solve(words1));

        // ["z","x"] -> "zx"
        String[] words2 = {"z", "x"};
        System.out.println("Test 2 Optimal: " + Optimal.solve(words2));  // zx

        // Cycle: ["a","b","a"]
        String[] words3 = {"a", "b", "a"};
        System.out.println("Test 3 (cycle) Optimal: '" + Optimal.solve(words3) + "'");  // ""

        // Invalid prefix
        String[] words4 = {"abc", "ab"};
        System.out.println("Test 4 (invalid prefix) Optimal: '" + Optimal.solve(words4) + "'");  // ""
    }
}
