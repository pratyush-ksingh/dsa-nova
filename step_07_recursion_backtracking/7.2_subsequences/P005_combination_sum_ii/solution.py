"""Problem: Combination Sum II
Difficulty: MEDIUM | XP: 25"""

from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(2^n * n)  |  Space: O(2^n * n)
# Generate all subsets; filter by sum; deduplicate with a set.
# ============================================================
def brute_force(candidates: List[int], target: int) -> List[List[int]]:
    candidates.sort()
    result_set = set()

    def generate(start, remaining, current):
        if remaining == 0:
            result_set.add(tuple(current))
            return
        for i in range(start, len(candidates)):
            if candidates[i] > remaining:
                break
            generate(i + 1, remaining - candidates[i], current + [candidates[i]])

    generate(0, target, [])
    return [list(t) for t in result_set]


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(2^n)  |  Space: O(n)
# Sort + backtracking; skip duplicate values at the same recursion level.
# ============================================================
def optimal(candidates: List[int], target: int) -> List[List[int]]:
    candidates.sort()
    result = []

    def backtrack(start, remaining, path):
        if remaining == 0:
            result.append(path[:])
            return
        for i in range(start, len(candidates)):
            if candidates[i] > remaining:
                break
            # Skip duplicates at the same level
            if i > start and candidates[i] == candidates[i - 1]:
                continue
            path.append(candidates[i])
            backtrack(i + 1, remaining - candidates[i], path)
            path.pop()

    backtrack(0, target, [])
    return result


# ============================================================
# APPROACH 3: BEST
# Time: O(2^n)  |  Space: O(n)
# Explicit include/exclude branching; skip all duplicates when excluding.
# ============================================================
def best(candidates: List[int], target: int) -> List[List[int]]:
    candidates.sort()
    result = []

    def backtrack(idx, rem, path):
        if rem == 0:
            result.append(path[:])
            return
        if idx == len(candidates) or candidates[idx] > rem:
            return

        # Include candidates[idx]
        path.append(candidates[idx])
        backtrack(idx + 1, rem - candidates[idx], path)
        path.pop()

        # Skip duplicates of candidates[idx]
        next_idx = idx + 1
        while next_idx < len(candidates) and candidates[next_idx] == candidates[idx]:
            next_idx += 1
        backtrack(next_idx, rem, path)

    backtrack(0, target, [])
    return result


if __name__ == "__main__":
    print("=== Combination Sum II ===")

    tests = [
        ([10, 1, 2, 7, 6, 1, 5], 8,  sorted([sorted(x) for x in [[1,1,6],[1,2,5],[1,7],[2,6]]])),
        ([2, 5, 2, 1, 2],        5,  sorted([sorted(x) for x in [[1,2,2],[5]]])),
        ([1, 1, 1, 1],           2,  [[1, 1]]),
    ]

    for candidates, target, expected in tests:
        b  = sorted([sorted(x) for x in brute_force(candidates[:], target)])
        o  = sorted([sorted(x) for x in optimal(candidates[:], target)])
        be = sorted([sorted(x) for x in best(candidates[:], target)])
        status = "PASS" if o == be == expected else "FAIL"
        print(f"candidates={candidates}, target={target}")
        print(f"  Optimal: {o}")
        print(f"  Best:    {be}")
        print(f"  Expected:{expected} | {status}")
