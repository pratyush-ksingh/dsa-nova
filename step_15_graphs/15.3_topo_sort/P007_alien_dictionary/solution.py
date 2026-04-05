"""Problem: Alien Dictionary
Difficulty: HARD | XP: 50

Given a list of words sorted in an alien language's lexicographic order,
determine the character ordering of the alien language using topological sort.
"""
from collections import defaultdict, deque
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - DFS topological sort
# Time: O(C + E) where C = total chars, E = ordering constraints
# Space: O(C)
# ============================================================
def brute_force(words: List[str]) -> str:
    chars = set(c for w in words for c in w)
    graph = defaultdict(set)
    # Build edges
    for i in range(len(words) - 1):
        w1, w2 = words[i], words[i + 1]
        found = False
        for c1, c2 in zip(w1, w2):
            if c1 != c2:
                graph[c1].add(c2)
                found = True
                break
        if not found and len(w1) > len(w2):
            return ""  # invalid ordering

    result = []
    # 0=unvisited, 1=visiting, 2=done
    state = {c: 0 for c in chars}

    def dfs(c) -> bool:
        state[c] = 1
        for nb in graph[c]:
            if state[nb] == 1:
                return False  # cycle
            if state[nb] == 0 and not dfs(nb):
                return False
        state[c] = 2
        result.append(c)
        return True

    for c in chars:
        if state[c] == 0:
            if not dfs(c):
                return ""
    return ''.join(reversed(result))


# ============================================================
# APPROACH 2: OPTIMAL - Kahn's BFS (in-degree topological sort)
# Time: O(C + E)  |  Space: O(C)
# ============================================================
def optimal(words: List[str]) -> str:
    adj = defaultdict(set)
    in_degree = {c: 0 for w in words for c in w}

    for i in range(len(words) - 1):
        w1, w2 = words[i], words[i + 1]
        found = False
        for c1, c2 in zip(w1, w2):
            if c1 != c2:
                if c2 not in adj[c1]:
                    adj[c1].add(c2)
                    in_degree[c2] += 1
                found = True
                break
        if not found and len(w1) > len(w2):
            return ""

    queue = deque(c for c in in_degree if in_degree[c] == 0)
    result = []
    while queue:
        c = queue.popleft()
        result.append(c)
        for nb in adj[c]:
            in_degree[nb] -= 1
            if in_degree[nb] == 0:
                queue.append(nb)
    return ''.join(result) if len(result) == len(in_degree) else ""


# ============================================================
# APPROACH 3: BEST - Kahn's with sorted queue (deterministic output)
# Time: O(C log C + E)  |  Space: O(C)
# ============================================================
def best(words: List[str]) -> str:
    import heapq
    adj = defaultdict(set)
    in_degree = {c: 0 for w in words for c in w}

    for i in range(len(words) - 1):
        w1, w2 = words[i], words[i + 1]
        found = False
        for c1, c2 in zip(w1, w2):
            if c1 != c2:
                if c2 not in adj[c1]:
                    adj[c1].add(c2)
                    in_degree[c2] += 1
                found = True
                break
        if not found and len(w1) > len(w2):
            return ""

    heap = [c for c in in_degree if in_degree[c] == 0]
    heapq.heapify(heap)
    result = []
    while heap:
        c = heapq.heappop(heap)
        result.append(c)
        for nb in sorted(adj[c]):
            in_degree[nb] -= 1
            if in_degree[nb] == 0:
                heapq.heappush(heap, nb)
    return ''.join(result) if len(result) == len(in_degree) else ""


if __name__ == "__main__":
    tests = [
        (["wrt", "wrf", "er", "ett", "rftt"], True),   # valid ordering
        (["z", "x"], True),
        (["z", "x", "z"], False),   # cycle -> ""
        (["abc", "ab"], False),     # invalid prefix -> ""
        (["a", "b", "c"], True),
    ]
    for words, valid in tests:
        bf = brute_force(words)
        opt = optimal(words)
        be = best(words)
        if valid:
            status = "OK" if bf and opt and be else "FAIL"
        else:
            status = "OK" if bf == opt == be == "" else "FAIL"
        print(f"[{status}] words={words} -> Brute='{bf}', Optimal='{opt}', Best='{be}'")
