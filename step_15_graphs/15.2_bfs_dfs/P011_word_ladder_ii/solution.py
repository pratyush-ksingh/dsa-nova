"""
Problem: Word Ladder II
Difficulty: HARD | XP: 50

Find ALL shortest transformation sequences from beginWord to endWord,
changing one letter at a time, where each intermediate word must be in wordList.
"""

from typing import List
from collections import deque, defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE
# BFS to compute distances from beginWord, then DFS to collect all shortest paths
# Time: O(N * L * 26 + paths)  |  Space: O(N * L)
# ============================================================
def brute_force(beginWord: str, endWord: str, wordList: List[str]) -> List[List[str]]:
    word_set = set(wordList)
    result = []
    if endWord not in word_set:
        return result

    # BFS to build dist map
    dist = {beginWord: 0}
    queue = deque([beginWord])

    while queue:
        word = queue.popleft()
        d = dist[word]
        for i in range(len(word)):
            for c in 'abcdefghijklmnopqrstuvwxyz':
                if c == word[i]:
                    continue
                next_word = word[:i] + c + word[i+1:]
                if next_word in word_set and next_word not in dist:
                    dist[next_word] = d + 1
                    queue.append(next_word)

    if endWord not in dist:
        return result

    min_len = dist[endWord]
    path = [beginWord]

    def dfs(curr):
        if curr == endWord:
            result.append(path[:])
            return
        if len(path) > min_len + 1:
            return
        d = dist[curr]
        for i in range(len(curr)):
            for c in 'abcdefghijklmnopqrstuvwxyz':
                if c == curr[i]:
                    continue
                next_word = curr[:i] + c + curr[i+1:]
                if next_word in word_set and dist.get(next_word) == d + 1:
                    path.append(next_word)
                    dfs(next_word)
                    path.pop()

    dfs(beginWord)
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# BFS layer-by-layer building parents map, then backtrack from endWord
# Remove words per layer to avoid cycles; allows multiple parents per word
# Time: O(N * L * 26)  |  Space: O(N * L)
# ============================================================
def optimal(beginWord: str, endWord: str, wordList: List[str]) -> List[List[str]]:
    word_set = set(wordList)
    result = []
    if endWord not in word_set:
        return result

    parents = defaultdict(list)
    curr_level = {beginWord}
    found = False

    while curr_level and not found:
        word_set -= curr_level  # remove current level to avoid revisiting
        next_level = set()
        for word in curr_level:
            for i in range(len(word)):
                for c in 'abcdefghijklmnopqrstuvwxyz':
                    if c == word[i]:
                        continue
                    next_word = word[:i] + c + word[i+1:]
                    if next_word in word_set:
                        next_level.add(next_word)
                        parents[next_word].append(word)
                        if next_word == endWord:
                            found = True
        curr_level = next_level

    if not found:
        return result

    def backtrack(curr, path):
        if curr == beginWord:
            result.append(path[::-1])
            return
        for parent in parents[curr]:
            path.append(parent)
            backtrack(parent, path)
            path.pop()

    backtrack(endWord, [endWord])
    return result


# ============================================================
# APPROACH 3: BEST
# Same as Optimal but with precomputed word pattern grouping for faster neighbors
# Groups words by pattern e.g. "h*t" -> ["hit","hot"]
# Time: O(N * L)  |  Space: O(N * L)
# ============================================================
def best(beginWord: str, endWord: str, wordList: List[str]) -> List[List[str]]:
    from collections import defaultdict

    word_set = set(wordList)
    if endWord not in word_set:
        return []

    L = len(beginWord)
    # Build pattern map
    pattern_map = defaultdict(list)
    for word in wordList:
        for i in range(L):
            pattern_map[word[:i] + '*' + word[i+1:]].append(word)

    parents = defaultdict(list)
    curr_level = {beginWord}
    found = False

    while curr_level and not found:
        word_set -= curr_level
        next_level = set()
        for word in curr_level:
            for i in range(L):
                pattern = word[:i] + '*' + word[i+1:]
                for next_word in pattern_map[pattern]:
                    if next_word in word_set:
                        next_level.add(next_word)
                        parents[next_word].append(word)
                        if next_word == endWord:
                            found = True
        curr_level = next_level

    if not found:
        return []

    result = []

    def backtrack(curr, path):
        if curr == beginWord:
            result.append(path[::-1])
            return
        for parent in parents[curr]:
            path.append(parent)
            backtrack(parent, path)
            path.pop()

    backtrack(endWord, [endWord])
    return result


if __name__ == "__main__":
    print("=== Word Ladder II ===")

    begin, end = "hit", "cog"
    word_list = ["hot", "dot", "dog", "lot", "log", "cog"]

    print(f"BruteForce: {brute_force(begin, end, word_list)}")
    print(f"Optimal:    {optimal(begin, end, word_list)}")
    print(f"Best:       {best(begin, end, word_list)}")
    # Expected: [["hit","hot","dot","dog","cog"],["hit","hot","lot","log","cog"]]

    # No path case
    print(f"No path: {optimal('hit', 'cog', ['hot','dot','dog','lot','log'])}")  # []
