"""
Problem: Word Search (LeetCode 79)
Difficulty: MEDIUM | XP: 25

Given an m x n grid of characters and a word, return True if the word
exists in the grid. The word can be constructed from letters of
sequentially adjacent cells (horizontally or vertically). The same
cell may not be used more than once.
"""
from typing import List
from collections import Counter


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(m*n * 4^L)  |  Space: O(L) recursion stack
#
# Try starting DFS from every cell. At each step try all 4 directions.
# Use a visited set to avoid reusing cells.
# ============================================================
def brute_force(board: List[List[str]], word: str) -> bool:
    """DFS from every cell with a visited set."""
    rows, cols = len(board), len(board[0])
    visited = set()

    def dfs(r: int, c: int, idx: int) -> bool:
        if idx == len(word):
            return True
        if r < 0 or r >= rows or c < 0 or c >= cols:
            return False
        if (r, c) in visited:
            return False
        if board[r][c] != word[idx]:
            return False

        visited.add((r, c))
        found = (dfs(r + 1, c, idx + 1) or
                 dfs(r - 1, c, idx + 1) or
                 dfs(r, c + 1, idx + 1) or
                 dfs(r, c - 1, idx + 1))
        visited.remove((r, c))
        return found

    for r in range(rows):
        for c in range(cols):
            if dfs(r, c, 0):
                return True
    return False


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(m*n * 4^L)  |  Space: O(L) recursion stack
#
# Same DFS but mark visited cells in-place by replacing char with '#'.
# Avoids the overhead of a separate visited set.
# Early pruning: if board char != word char, don't recurse.
# ============================================================
def optimal(board: List[List[str]], word: str) -> bool:
    """DFS with in-place marking using '#' as visited sentinel."""
    rows, cols = len(board), len(board[0])

    def dfs(r: int, c: int, idx: int) -> bool:
        if idx == len(word):
            return True
        if r < 0 or r >= rows or c < 0 or c >= cols:
            return False
        if board[r][c] != word[idx]:
            return False

        # Mark as visited
        temp = board[r][c]
        board[r][c] = '#'

        found = (dfs(r + 1, c, idx + 1) or
                 dfs(r - 1, c, idx + 1) or
                 dfs(r, c + 1, idx + 1) or
                 dfs(r, c - 1, idx + 1))

        # Restore
        board[r][c] = temp
        return found

    for r in range(rows):
        for c in range(cols):
            if dfs(r, c, 0):
                return True
    return False


# ============================================================
# APPROACH 3: BEST
# Time: O(m*n * 4^L)  |  Space: O(L) recursion stack
#
# Same in-place DFS with two additional optimizations:
# 1. Frequency check: if board doesn't have enough of each letter,
#    return False immediately.
# 2. Direction optimization: if the last character of the word is
#    rarer on the board than the first, reverse the word and search
#    from the rarer end to prune more branches early.
# ============================================================
def best(board: List[List[str]], word: str) -> bool:
    """DFS with in-place marking + frequency pruning + direction optimization."""
    rows, cols = len(board), len(board[0])

    # Frequency check
    board_count = Counter(c for row in board for c in row)
    word_count = Counter(word)
    for ch, cnt in word_count.items():
        if board_count[ch] < cnt:
            return False

    # Direction optimization: start from the rarer end
    if board_count[word[0]] > board_count[word[-1]]:
        word = word[::-1]

    def dfs(r: int, c: int, idx: int) -> bool:
        if idx == len(word):
            return True
        if r < 0 or r >= rows or c < 0 or c >= cols:
            return False
        if board[r][c] != word[idx]:
            return False

        temp = board[r][c]
        board[r][c] = '#'

        found = (dfs(r + 1, c, idx + 1) or
                 dfs(r - 1, c, idx + 1) or
                 dfs(r, c + 1, idx + 1) or
                 dfs(r, c - 1, idx + 1))

        board[r][c] = temp
        return found

    for r in range(rows):
        for c in range(cols):
            if dfs(r, c, 0):
                return True
    return False


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Word Search ===\n")

    tests = [
        (
            [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]],
            "ABCCED", True
        ),
        (
            [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]],
            "SEE", True
        ),
        (
            [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]],
            "ABCB", False
        ),
        (
            [["a"]],
            "a", True
        ),
        (
            [["a","b"],["c","d"]],
            "abdc", True
        ),
    ]

    for board, word, expected in tests:
        import copy
        b1 = copy.deepcopy(board)
        b2 = copy.deepcopy(board)
        b3 = copy.deepcopy(board)
        r1 = brute_force(b1, word)
        r2 = optimal(b2, word)
        r3 = best(b3, word)
        status = "PASS" if r1 == r2 == r3 == expected else "FAIL"
        print(f"Word: {word:<10s} Expected: {str(expected):<6} "
              f"Brute: {str(r1):<6} Optimal: {str(r2):<6} Best: {str(r3):<6}  [{status}]")
