"""Problem: Left Rotate Array by K Places
Difficulty: MEDIUM | XP: 25

Given an array of n elements, rotate it left by k positions.
E.g., [1,2,3,4,5], k=2 -> [3,4,5,1,2]
Real-life use: circular buffer / ring-buffer rotation in OS scheduling.
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n*k)  |  Space: O(1)
# Rotate left by 1, k times
# ============================================================
def brute_force(arr: List[int], k: int) -> List[int]:
    n = len(arr)
    k = k % n
    for _ in range(k):
        first = arr[0]
        for i in range(n - 1):
            arr[i] = arr[i + 1]
        arr[n - 1] = first
    return arr


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(k)
# Copy first k elements, shift rest left, paste k elements at end
# ============================================================
def optimal(arr: List[int], k: int) -> List[int]:
    n = len(arr)
    k = k % n
    temp = arr[:k]
    arr[:n - k] = arr[k:]
    arr[n - k:] = temp
    return arr


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1) extra (Python slicing creates copies
#   but the algorithm is O(1) extra conceptually — uses reversal)
# Reverse first k, reverse rest, reverse all -> left rotation
# ============================================================
def best(arr: List[int], k: int) -> List[int]:
    n = len(arr)
    k = k % n

    def reverse(lo: int, hi: int):
        while lo < hi:
            arr[lo], arr[hi] = arr[hi], arr[lo]
            lo += 1
            hi -= 1

    reverse(0, k - 1)
    reverse(k, n - 1)
    reverse(0, n - 1)
    return arr


if __name__ == "__main__":
    cases = [
        ([1, 2, 3, 4, 5], 2, [3, 4, 5, 1, 2]),
        ([1, 2, 3, 4, 5, 6, 7], 3, [4, 5, 6, 7, 1, 2, 3]),
        ([3, 99, -1, 100], 2, [-1, 100, 3, 99]),
        ([1], 5, [1]),
    ]
    print("=== Left Rotate Array by K Places ===")
    for inp, k, exp in cases:
        b = brute_force(inp[:], k)
        o = optimal(inp[:], k)
        bst = best(inp[:], k)
        ok = "OK" if b == o == bst == exp else "MISMATCH"
        print(f"arr={inp} k={k} => BF={b} OPT={o} BEST={bst} EXP={exp} {ok}")
