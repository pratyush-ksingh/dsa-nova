"""
Problem: Snake Ladder Problem
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a board with snakes and ladders, find the minimum number of dice
throws to reach the last cell from cell 1.
"""
from typing import List, Optional
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE — BFS on board
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(board: List[int]) -> int:
    """
    BFS from cell 0 to cell N-1. board[i] = destination if snake/ladder
    at cell i, else -1. Board is 0-indexed, size N.
    Each turn: roll dice 1-6 and move.
    Returns minimum dice throws, or -1 if unreachable.
    """
    n = len(board)
    visited = [False] * n
    queue = deque([(0, 0)])  # (cell, moves)
    visited[0] = True

    while queue:
        cell, moves = queue.popleft()
        if cell == n - 1:
            return moves
        for dice in range(1, 7):
            nxt = cell + dice
            if nxt >= n:
                continue
            if board[nxt] != -1:
                nxt = board[nxt]
            if not visited[nxt]:
                visited[nxt] = True
                queue.append((nxt, moves + 1))
    return -1


# ============================================================
# APPROACH 2: OPTIMAL — BFS with snakes/ladders as dict lookup
# Time: O(N)  |  Space: O(N)
# ============================================================
def optimal(n: int, ladders: List[List[int]], snakes: List[List[int]]) -> int:
    """
    Same BFS but takes snakes and ladders as separate lists of [start, end].
    Board size is n (1-indexed, cells 1 to n).
    """
    moves_map = {}
    for start, end in ladders:
        moves_map[start] = end
    for start, end in snakes:
        moves_map[start] = end

    visited = set()
    queue = deque([(1, 0)])  # (cell, dice_throws)
    visited.add(1)

    while queue:
        cell, throws = queue.popleft()
        if cell == n:
            return throws
        for dice in range(1, 7):
            nxt = cell + dice
            if nxt > n:
                continue
            nxt = moves_map.get(nxt, nxt)
            if nxt not in visited:
                visited.add(nxt)
                queue.append((nxt, throws + 1))
    return -1


# ============================================================
# APPROACH 3: BEST — BFS with bidirectional potential (still BFS, cleaner)
# Time: O(N)  |  Space: O(N)
# ============================================================
def best(n: int, ladders: List[List[int]], snakes: List[List[int]]) -> int:
    """
    Clean BFS approach using level-based traversal for clarity.
    Returns minimum dice throws from cell 1 to cell n.
    """
    moves_map = {}
    for start, end in ladders:
        moves_map[start] = end
    for start, end in snakes:
        moves_map[start] = end

    visited = set([1])
    queue = deque([1])
    throws = 0

    while queue:
        throws += 1
        for _ in range(len(queue)):
            cell = queue.popleft()
            for dice in range(1, 7):
                nxt = cell + dice
                if nxt > n:
                    continue
                nxt = moves_map.get(nxt, nxt)
                if nxt == n:
                    return throws
                if nxt not in visited:
                    visited.add(nxt)
                    queue.append(nxt)
    return -1


if __name__ == "__main__":
    print("=== Snake Ladder Problem ===")

    # Classic 30-cell board example
    # Ladders: 3->22, 5->8, 11->26, 20->29
    # Snakes: 27->1, 21->9, 17->4, 19->7
    ladders = [[3, 22], [5, 8], [11, 26], [20, 29]]
    snakes = [[27, 1], [21, 9], [17, 4], [19, 7]]

    # Board array approach (0-indexed, size 30)
    board = [-1] * 30
    for s, e in ladders:
        board[s - 1] = e - 1
    for s, e in snakes:
        board[s - 1] = e - 1

    print(f"Brute (array):  {brute_force(board)}")
    print(f"Optimal:        {optimal(30, ladders, snakes)}")
    print(f"Best:           {best(30, ladders, snakes)}")
