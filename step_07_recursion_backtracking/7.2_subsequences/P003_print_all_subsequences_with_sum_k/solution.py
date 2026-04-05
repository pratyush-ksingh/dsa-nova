"""
Problem: Print All Subsequences with Sum K
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(2^n * n)  |  Space: O(2^n * n)
# Generate every possible subsequence, filter by sum == K.
# ============================================================
def brute_force(arr: List[int], k: int) -> List[List[int]]:
    """
    Enumerate all 2^n subsets via bitmask, collect those
    whose element-sum equals k.
    """
    n = len(arr)
    result = []
    for mask in range(1 << n):          # 0 .. 2^n - 1
        subseq = [arr[i] for i in range(n) if mask & (1 << i)]
        if sum(subseq) == k:
            result.append(subseq)
    return result


# ============================================================
# APPROACH 2: OPTIMAL — Backtracking with pruning
# Time: O(2^n)  |  Space: O(n) recursion depth
# Prune branches early when running sum already exceeds k
# (valid only when all elements are non-negative).
# ============================================================
def _backtrack(arr: List[int], index: int, current: List[int],
               current_sum: int, k: int, result: List[List[int]]) -> None:
    # Found a valid subsequence
    if current_sum == k:
        result.append(current[:])
        # Don't return yet — there may be zeros left to include
    if index == len(arr):
        return
    # Pruning: for non-negative arrays, no point going further
    if current_sum > k:
        return
    for i in range(index, len(arr)):
        current.append(arr[i])
        _backtrack(arr, i + 1, current, current_sum + arr[i], k, result)
        current.pop()


def optimal(arr: List[int], k: int) -> List[List[int]]:
    result: List[List[int]] = []
    _backtrack(arr, 0, [], 0, k, result)
    return result


# ============================================================
# APPROACH 3: BEST — Backtracking with include/exclude pattern
# Time: O(2^n)  |  Space: O(n)
# Classic binary-decision tree: at each index decide include or
# exclude.  Cleaner recursion, same pruning.
# ============================================================
def _rec(arr: List[int], index: int, current: List[int],
         current_sum: int, k: int, result: List[List[int]]) -> None:
    if index == len(arr):
        if current_sum == k:
            result.append(current[:])
        return
    # Early exit: already exceeded k (non-negative elements)
    if current_sum > k:
        return

    # Include arr[index]
    current.append(arr[index])
    _rec(arr, index + 1, current, current_sum + arr[index], k, result)
    current.pop()

    # Exclude arr[index]
    _rec(arr, index + 1, current, current_sum, k, result)


def best(arr: List[int], k: int) -> List[List[int]]:
    result: List[List[int]] = []
    _rec(arr, 0, [], 0, k, result)
    return result


if __name__ == "__main__":
    print("=== Print All Subsequences with Sum K ===")
    arr = [1, 2, 1]
    k = 2
    print(f"Input: arr={arr}, k={k}")
    print(f"Brute:   {brute_force(arr, k)}")
    print(f"Optimal: {optimal(arr, k)}")
    print(f"Best:    {best(arr, k)}")

    arr2 = [1, 2, 3, 4, 5]
    k2 = 7
    print(f"\nInput: arr={arr2}, k={k2}")
    print(f"Best:    {best(arr2, k2)}")
