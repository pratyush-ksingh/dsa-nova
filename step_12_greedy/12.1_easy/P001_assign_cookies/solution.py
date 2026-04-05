"""
Problem: Assign Cookies (LeetCode #455)
Difficulty: EASY | XP: 10
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Try all assignments (with pruning)
# Time: O(2^m * n)  |  Space: O(m)
# ============================================================
def brute_force(g: List[int], s: List[int]) -> int:
    g.sort()
    s.sort()
    used = [False] * len(s)

    def helper(child_idx: int) -> int:
        if child_idx >= len(g):
            return 0
        best = helper(child_idx + 1)  # skip this child
        for j in range(len(s)):
            if not used[j] and s[j] >= g[child_idx]:
                used[j] = True
                best = max(best, 1 + helper(child_idx + 1))
                used[j] = False
                break  # smallest adequate cookie is optimal (pruning)
        return best

    return helper(0)


# ============================================================
# APPROACH 2 & 3: OPTIMAL -- Sort + Two Pointers
# Time: O(n log n + m log m)  |  Space: O(1)
# ============================================================
def find_content_children(g: List[int], s: List[int]) -> int:
    g.sort()
    s.sort()
    i = 0  # child pointer
    j = 0  # cookie pointer
    while i < len(g) and j < len(s):
        if s[j] >= g[i]:
            i += 1  # child i is content
        j += 1  # move to next cookie regardless
    return i


if __name__ == "__main__":
    print("=== Assign Cookies ===\n")

    tests = [
        ([1, 2, 3], [1, 1], 1),
        ([1, 2], [1, 2, 3], 2),
        ([10, 9, 8, 7], [5, 6, 7, 8], 2),
        ([1], [], 0),
        ([], [1, 2], 0),
    ]

    for g, s, expected in tests:
        brute = brute_force(g[:], s[:])
        optimal = find_content_children(g[:], s[:])
        status = "PASS" if optimal == expected else "FAIL"
        print(f"g={g}, s={s} -> brute={brute}, optimal={optimal} (expected={expected}) {status}")
