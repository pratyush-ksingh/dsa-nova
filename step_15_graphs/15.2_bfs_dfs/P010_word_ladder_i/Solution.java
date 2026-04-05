import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - BFS, generate all neighbors naively
// Time: O(N * L^2)  |  Space: O(N * L)
// For each word in queue, try replacing each character with a-z
// ============================================================
class BruteForce {
    public static int solve(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);
        int steps = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            steps++;
            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                char[] arr = word.toCharArray();
                for (int j = 0; j < arr.length; j++) {
                    char orig = arr[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == orig) continue;
                        arr[j] = c;
                        String next = new String(arr);
                        if (next.equals(endWord)) return steps;
                        if (wordSet.contains(next) && !visited.contains(next)) {
                            visited.add(next);
                            queue.offer(next);
                        }
                    }
                    arr[j] = orig;
                }
            }
        }
        return 0;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - BFS with wordSet for O(1) lookup
// Time: O(N * L * 26)  |  Space: O(N * L)
// Remove visited words from set to avoid revisiting
// ============================================================
class Optimal {
    public static int solve(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        wordSet.remove(beginWord);
        int steps = 1;
        while (!queue.isEmpty()) {
            steps++;
            for (int size = queue.size(); size > 0; size--) {
                String word = queue.poll();
                char[] arr = word.toCharArray();
                for (int j = 0; j < arr.length; j++) {
                    char orig = arr[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == orig) continue;
                        arr[j] = c;
                        String next = new String(arr);
                        if (next.equals(endWord)) return steps;
                        if (wordSet.contains(next)) {
                            wordSet.remove(next);
                            queue.offer(next);
                        }
                    }
                    arr[j] = orig;
                }
            }
        }
        return 0;
    }
}

// ============================================================
// APPROACH 3: BEST - Bidirectional BFS
// Time: O(N * L * 26)  |  Space: O(N * L)
// Expand from both ends; much faster in practice (reduces search space)
// ============================================================
class Best {
    public static int solve(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;
        Set<String> beginSet = new HashSet<>(), endSet = new HashSet<>();
        beginSet.add(beginWord);
        endSet.add(endWord);
        wordSet.remove(endWord);
        int steps = 1;
        while (!beginSet.isEmpty()) {
            steps++;
            // Always expand the smaller set
            if (beginSet.size() > endSet.size()) {
                Set<String> tmp = beginSet; beginSet = endSet; endSet = tmp;
            }
            Set<String> nextSet = new HashSet<>();
            for (String word : beginSet) {
                char[] arr = word.toCharArray();
                for (int j = 0; j < arr.length; j++) {
                    char orig = arr[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == orig) continue;
                        arr[j] = c;
                        String next = new String(arr);
                        if (endSet.contains(next)) return steps;
                        if (wordSet.contains(next)) {
                            wordSet.remove(next);
                            nextSet.add(next);
                        }
                    }
                    arr[j] = orig;
                }
            }
            beginSet = nextSet;
        }
        return 0;
    }
}

public class Solution {
    public static void main(String[] args) {
        List<String> wordList1 = Arrays.asList("hot","dot","dog","lot","log","cog");
        System.out.println("Test 1 (hit->cog): expected 5");
        System.out.println("  BruteForce = " + BruteForce.solve("hit", "cog", new ArrayList<>(wordList1)));
        System.out.println("  Optimal    = " + Optimal.solve("hit", "cog", new ArrayList<>(wordList1)));
        System.out.println("  Best       = " + Best.solve("hit", "cog", new ArrayList<>(wordList1)));

        List<String> wordList2 = Arrays.asList("hot","dot","dog","lot","log");
        System.out.println("Test 2 (hit->cog, no cog in list): expected 0");
        System.out.println("  Optimal    = " + Optimal.solve("hit", "cog", new ArrayList<>(wordList2)));

        List<String> wordList3 = Arrays.asList("a","b","c");
        System.out.println("Test 3 (a->c): expected 2");
        System.out.println("  Optimal    = " + Optimal.solve("a", "c", new ArrayList<>(wordList3)));
    }
}
