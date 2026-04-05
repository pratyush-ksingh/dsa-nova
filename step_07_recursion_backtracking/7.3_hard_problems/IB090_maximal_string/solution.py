"""
Problem: Maximal String
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a string S and integer K, perform at most K swaps (any two indices)
to get the lexicographically largest string possible.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Recursive try all beneficial swaps
# Time: O(K * N^2)  |  Space: O(N * K) recursion depth
# ============================================================
def brute_force(s: str, k: int) -> str:
    """Try all swaps recursively, track the lexicographically largest result."""
    best = [s]

    def helper(arr: List[str], swaps_left: int):
        cur = ''.join(arr)
        if cur > best[0]:
            best[0] = cur
        if swaps_left == 0:
            return
        n = len(arr)
        for i in range(n - 1):
            for j in range(i + 1, n):
                if arr[j] > arr[i]:
                    arr[i], arr[j] = arr[j], arr[i]
                    helper(arr, swaps_left - 1)
                    arr[i], arr[j] = arr[j], arr[i]

    helper(list(s), k)
    return best[0]


# ============================================================
# APPROACH 2: OPTIMAL - Greedy Backtracking with Pruning
# Time: O(N^2 * K)  |  Space: O(N)
# At each position find max char ahead, try all occurrences of that max.
# ============================================================
def optimal(s: str, k: int) -> str:
    """Greedy backtracking: at each position try placing the maximum available char."""
    best_str = [s]

    def helper(arr: List[str], pos: int, swaps: int):
        if swaps == 0 or pos == len(arr):
            cur = ''.join(arr)
            if cur > best_str[0]:
                best_str[0] = cur
            return
        max_ch = max(arr[pos:])
        if arr[pos] == max_ch:
            helper(arr, pos + 1, swaps)
            return
        for i in range(pos + 1, len(arr)):
            if arr[i] == max_ch:
                arr[pos], arr[i] = arr[i], arr[pos]
                helper(arr, pos + 1, swaps - 1)
                arr[pos], arr[i] = arr[i], arr[pos]
        cur = ''.join(arr)
        if cur > best_str[0]:
            best_str[0] = cur

    helper(list(s), 0, k)
    return best_str[0]


# ============================================================
# APPROACH 3: BEST - Pure Greedy O(N^2) with adjacent swaps
# Time: O(N^2)  |  Space: O(N)
# For each position find the rightmost maximum, bubble it left.
# Each bubble step is one swap. Greedy: always maximize current position.
# ============================================================
def best(s: str, k: int) -> str:
    """
    Greedy: for each position i, find rightmost max char from i..n-1.
    Bubble it left to position i using adjacent swaps (each costs 1 swap).
    """
    arr = list(s)
    n = len(arr)
    for i in range(n - 1):
        if k == 0:
            break
        # Find rightmost max from i+1..n-1
        max_idx = i
        for j in range(i + 1, n):
            if arr[j] >= arr[max_idx]:  # >= to prefer rightmost
                max_idx = j
        if max_idx == i:
            continue
        # Bubble arr[max_idx] left to i
        while max_idx > i and k > 0:
            arr[max_idx], arr[max_idx - 1] = arr[max_idx - 1], arr[max_idx]
            max_idx -= 1
            k -= 1
    return ''.join(arr)


if __name__ == "__main__":
    print("=== Maximal String ===")

    tests = [
        ("dcab", 2),
        ("abcd", 4),
        ("abcd", 1),
        ("aaz",  1),
        ("ba",   1),
    ]
    for s, k in tests:
        b = brute_force(s, k)
        o = optimal(s, k)
        best_ans = best(s, k)
        print(f"s={s!r}, k={k}: brute={b}, optimal={o}, best={best_ans}")
