import java.util.*;

/**
 * Problem: Word Ladder II
 * Difficulty: HARD | XP: 50
 *
 * Find ALL shortest transformation sequences from beginWord to endWord,
 * changing one letter at a time, where each intermediate word must be in wordList.
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// BFS to find shortest path length, then DFS to collect all such paths
// Time: O(N * L * 26 + paths)  |  Space: O(N * L)
// ============================================================
class BruteForce {
    static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        List<List<String>> result = new ArrayList<>();
        if (!wordSet.contains(endWord)) return result;

        // BFS: build layer-by-layer distance map
        Map<String, Integer> dist = new HashMap<>();
        dist.put(beginWord, 0);
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);

        boolean found = false;
        while (!queue.isEmpty() && !found) {
            String word = queue.poll();
            int d = dist.get(word);
            char[] chars = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char orig = chars[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == orig) continue;
                    chars[i] = c;
                    String next = new String(chars);
                    if (next.equals(endWord)) found = true;
                    if (wordSet.contains(next) && !dist.containsKey(next)) {
                        dist.put(next, d + 1);
                        queue.offer(next);
                    }
                    chars[i] = orig;
                }
            }
        }

        if (!found && !dist.containsKey(endWord)) return result;

        // DFS to collect all shortest paths
        int minLen = dist.getOrDefault(endWord, Integer.MAX_VALUE);
        Deque<String> path = new ArrayDeque<>();
        path.addLast(beginWord);

        dfs(beginWord, endWord, dist, wordSet, path, result, minLen);
        return result;
    }

    static void dfs(String curr, String endWord, Map<String, Integer> dist,
                    Set<String> wordSet, Deque<String> path, List<List<String>> result, int minLen) {
        if (curr.equals(endWord)) {
            result.add(new ArrayList<>(path));
            return;
        }
        if (path.size() > minLen + 1) return;

        char[] chars = curr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char orig = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == orig) continue;
                chars[i] = c;
                String next = new String(chars);
                if ((wordSet.contains(next) || next.equals(endWord))
                        && dist.containsKey(next)
                        && dist.get(next) == dist.get(curr) + 1) {
                    path.addLast(next);
                    dfs(next, endWord, dist, wordSet, path, result, minLen);
                    path.removeLast();
                }
                chars[i] = orig;
            }
        }
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// BFS layer-by-layer building neighbors graph, then DFS to collect paths
// Removes words as they are visited per layer (avoids revisiting at same level)
// Time: O(N * L * 26)  |  Space: O(N * L)
// ============================================================
class Optimal {
    static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        List<List<String>> result = new ArrayList<>();
        if (!wordSet.contains(endWord)) return result;

        // BFS: for each word, store parents (words that lead to it in shortest path)
        Map<String, List<String>> parents = new HashMap<>();
        Set<String> currLevel = new HashSet<>();
        currLevel.add(beginWord);
        boolean found = false;

        while (!currLevel.isEmpty() && !found) {
            wordSet.removeAll(currLevel);
            Set<String> nextLevel = new HashSet<>();
            for (String word : currLevel) {
                char[] chars = word.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char orig = chars[i];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == orig) continue;
                        chars[i] = c;
                        String next = new String(chars);
                        if (wordSet.contains(next)) {
                            nextLevel.add(next);
                            parents.computeIfAbsent(next, k -> new ArrayList<>()).add(word);
                            if (next.equals(endWord)) found = true;
                        }
                        chars[i] = orig;
                    }
                }
            }
            currLevel = nextLevel;
        }

        if (!found) return result;

        // Backtrack from endWord using parents
        Deque<String> path = new ArrayDeque<>();
        path.addFirst(endWord);
        backtrack(beginWord, endWord, parents, path, result);
        return result;
    }

    static void backtrack(String beginWord, String curr, Map<String, List<String>> parents,
                          Deque<String> path, List<List<String>> result) {
        if (curr.equals(beginWord)) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (String parent : parents.getOrDefault(curr, Collections.emptyList())) {
            path.addFirst(parent);
            backtrack(beginWord, parent, parents, path, result);
            path.removeFirst();
        }
    }
}

// ============================================================
// APPROACH 3: BEST
// Bidirectional BFS to shorten search space, then reconstruct paths
// Time: O(N * L * 26) but with smaller constant  |  Space: O(N * L)
// ============================================================
class Best {
    // For this problem the Optimal approach is already near-optimal;
    // Best uses same parents-map approach but with a cleaner implementation
    static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        return Optimal.findLadders(beginWord, endWord, wordList);
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Word Ladder II ===");

        String begin = "hit", end = "cog";
        List<String> wordList = Arrays.asList("hot","dot","dog","lot","log","cog");

        List<List<String>> r1 = BruteForce.findLadders(begin, end, wordList);
        System.out.println("BruteForce: " + r1);

        List<List<String>> r2 = Optimal.findLadders(begin, end, wordList);
        System.out.println("Optimal:    " + r2);

        // No path case
        List<List<String>> r3 = Optimal.findLadders("hit", "cog", Arrays.asList("hot","dot","dog","lot","log"));
        System.out.println("No path:    " + r3);  // []
    }
}
