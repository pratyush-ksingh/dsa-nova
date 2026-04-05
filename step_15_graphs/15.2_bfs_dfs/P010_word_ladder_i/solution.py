"""Problem: Word Ladder I
Difficulty: HARD | XP: 50

Find shortest transformation sequence from beginWord to endWord.
Each step changes exactly one letter, and intermediate words must be in wordList.
"""
from collections import deque
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - BFS with visited set
# Time: O(N * L * 26)  |  Space: O(N * L)
# ============================================================
def brute_force(beginWord: str, endWord: str, wordList: List[str]) -> int:
    word_set = set(wordList)
    if endWord not in word_set:
        return 0
    queue = deque([(beginWord, 1)])
    visited = {beginWord}
    while queue:
        word, steps = queue.popleft()
        for i in range(len(word)):
            for c in 'abcdefghijklmnopqrstuvwxyz':
                if c == word[i]:
                    continue
                new_word = word[:i] + c + word[i+1:]
                if new_word == endWord:
                    return steps + 1
                if new_word in word_set and new_word not in visited:
                    visited.add(new_word)
                    queue.append((new_word, steps + 1))
    return 0


# ============================================================
# APPROACH 2: OPTIMAL - BFS, remove from set instead of visited set
# Time: O(N * L * 26)  |  Space: O(N * L)
# Removing from wordSet is cleaner and slightly faster
# ============================================================
def optimal(beginWord: str, endWord: str, wordList: List[str]) -> int:
    word_set = set(wordList)
    if endWord not in word_set:
        return 0
    queue = deque([beginWord])
    word_set.discard(beginWord)
    steps = 1
    while queue:
        steps += 1
        for _ in range(len(queue)):
            word = queue.popleft()
            for i in range(len(word)):
                for c in 'abcdefghijklmnopqrstuvwxyz':
                    new_word = word[:i] + c + word[i+1:]
                    if new_word == endWord:
                        return steps
                    if new_word in word_set:
                        word_set.discard(new_word)
                        queue.append(new_word)
    return 0


# ============================================================
# APPROACH 3: BEST - Bidirectional BFS
# Time: O(N * L * 26)  |  Space: O(N * L)
# Expand from both ends; dramatically reduces search space
# ============================================================
def best(beginWord: str, endWord: str, wordList: List[str]) -> int:
    word_set = set(wordList)
    if endWord not in word_set:
        return 0
    front = {beginWord}
    back = {endWord}
    word_set.discard(endWord)
    steps = 1
    while front:
        steps += 1
        # Always expand the smaller frontier
        if len(front) > len(back):
            front, back = back, front
        nxt = set()
        for word in front:
            for i in range(len(word)):
                for c in 'abcdefghijklmnopqrstuvwxyz':
                    new_word = word[:i] + c + word[i+1:]
                    if new_word in back:
                        return steps
                    if new_word in word_set:
                        word_set.discard(new_word)
                        nxt.add(new_word)
        front = nxt
    return 0


if __name__ == "__main__":
    tests = [
        ("hit", "cog", ["hot", "dot", "dog", "lot", "log", "cog"], 5),
        ("hit", "cog", ["hot", "dot", "dog", "lot", "log"], 0),
        ("a", "c", ["a", "b", "c"], 2),
        ("hot", "dog", ["hot", "dog", "dot"], 3),
    ]
    for begin, end, wl, expected in tests:
        bf = brute_force(begin, end, wl)
        opt = optimal(begin, end, wl)
        be = best(begin, end, wl)
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] {begin}->{end}: Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
