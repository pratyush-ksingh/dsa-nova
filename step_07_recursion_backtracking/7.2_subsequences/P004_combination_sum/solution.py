"""
Problem: Combination Sum (LeetCode 39)
Difficulty: MEDIUM | XP: 25

Given an array of distinct integers `candidates` and a target integer `target`,
return a list of all unique combinations of candidates where the chosen numbers
sum to target. The same number may be chosen from candidates an unlimited number
of times. The answer may be returned in any order.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE — Generate all subsets (with repetition)
# Time: O(2^t * k)  where t = target, k = avg combination length
# Space: O(2^t * k) for output
# ============================================================
def brute_force(candidates: List[int], target: int) -> List[List[int]]:
    """
    Generate all possible combinations (allowing repetition) using a
    plain recursive exploration. No pruning — explores every branch.
    """
    results = []

    def generate(start: int, current: List[int], remaining: int):
        if remaining == 0:
            results.append(list(current))
            return
        if remaining < 0:
            return
        for i in range(start, len(candidates)):
            current.append(candidates[i])
            generate(i, current, remaining - candidates[i])  # allow reuse
            current.pop()

    generate(0, [], target)
    return results


# ============================================================
# APPROACH 2: OPTIMAL — Backtracking with sort + pruning
# Time: O(2^(t/min_candidate))  |  Space: O(t/min_candidate) recursion depth
# ============================================================
def optimal(candidates: List[int], target: int) -> List[List[int]]:
    """
    Sort the candidates so we can break early: if candidates[i] > remaining,
    all subsequent candidates are also too large. This prunes the search tree
    significantly, especially for large inputs.
    """
    candidates.sort()
    results = []

    def backtrack(start: int, current: List[int], remaining: int):
        if remaining == 0:
            results.append(list(current))
            return
        for i in range(start, len(candidates)):
            if candidates[i] > remaining:
                break  # sorted: no point continuing
            current.append(candidates[i])
            backtrack(i, current, remaining - candidates[i])
            current.pop()

    backtrack(0, [], target)
    return results


# ============================================================
# APPROACH 3: BEST — Backtracking (pick/skip decision tree)
# Time: O(2^(t/min_candidate))  |  Space: O(t/min_candidate)
# ============================================================
def best(candidates: List[int], target: int) -> List[List[int]]:
    """
    Classic pick/skip recursive tree:
    - At each index, decide to PICK candidates[idx] (stay at same index, reduce target)
      or SKIP it (move to next index).
    This avoids duplicates naturally since we never go back to a previous index.
    Equivalent in complexity to Approach 2, but models the decision differently.
    """
    results = []

    def backtrack(idx: int, current: List[int], remaining: int):
        if remaining == 0:
            results.append(list(current))
            return
        if idx == len(candidates) or remaining < 0:
            return

        # Pick candidates[idx] (reuse allowed — stay at same idx)
        current.append(candidates[idx])
        backtrack(idx, current, remaining - candidates[idx])
        current.pop()

        # Skip candidates[idx] — move to next
        backtrack(idx + 1, current, remaining)

    candidates.sort()
    backtrack(0, [], target)
    return results


if __name__ == "__main__":
    print("=== Combination Sum ===")
    candidates, target = [2, 3, 6, 7], 7
    print(f"Brute:   {brute_force(candidates[:], target)}")
    print(f"Optimal: {optimal(candidates[:], target)}")
    print(f"Best:    {best(candidates[:], target)}")

    candidates2, target2 = [2, 3, 5], 8
    print(f"\nBrute:   {brute_force(candidates2[:], target2)}")
    print(f"Optimal: {optimal(candidates2[:], target2)}")
    print(f"Best:    {best(candidates2[:], target2)}")
