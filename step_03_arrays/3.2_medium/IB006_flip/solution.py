"""Problem: Flip
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a binary string A, flip exactly one contiguous substring
(change 0->1 and 1->0) to maximize the number of 1s.
Return [L, R] (1-indexed) of the flipped substring.
If no flip needed (all 1s) or no gain possible, return [].
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Try every window, calculate net gain from flipping it
# ============================================================
def brute_force(A: str) -> List[int]:
    n = len(A)
    ones = A.count('1')
    best_gain = 0
    best_l = best_r = -1

    for i in range(n):
        gain = 0
        for j in range(i, n):
            gain += 1 if A[j] == '0' else -1
            if gain > best_gain:
                best_gain = gain
                best_l, best_r = i + 1, j + 1

    return [best_l, best_r] if best_l != -1 else []


# ============================================================
# APPROACH 2: OPTIMAL (Transform + Kadane's)
# Time: O(n)  |  Space: O(n)
# Map '0' -> +1, '1' -> -1; find max subarray sum (Kadane's).
# ============================================================
def optimal(A: str) -> List[int]:
    t = [1 if c == '0' else -1 for c in A]
    max_gain = 0
    cur_sum = 0
    temp_start = 0
    best_l = best_r = -1

    for i, v in enumerate(t):
        cur_sum += v
        if cur_sum > max_gain:
            max_gain = cur_sum
            best_l = temp_start + 1
            best_r = i + 1
        if cur_sum < 0:
            cur_sum = 0
            temp_start = i + 1

    return [best_l, best_r] if best_l != -1 else []


# ============================================================
# APPROACH 3: BEST (Kadane's without auxiliary array)
# Time: O(n)  |  Space: O(1)
# Same idea but computes the transform on-the-fly
# ============================================================
def best(A: str) -> List[int]:
    max_gain = 0
    cur_sum = 0
    temp_start = 0
    best_l = best_r = -1

    for i, c in enumerate(A):
        val = 1 if c == '0' else -1
        cur_sum += val
        if cur_sum > max_gain:
            max_gain = cur_sum
            best_l = temp_start + 1
            best_r = i + 1
        if cur_sum < 0:
            cur_sum = 0
            temp_start = i + 1

    return [best_l, best_r] if best_l != -1 else []


if __name__ == "__main__":
    cases = [
        ("010", [1, 1]),       # flip index 1 (0-indexed)
        ("111", []),            # already all 1s
        ("0000", [1, 4]),
        ("010101", [1, 1]),     # any single 0 works; leftmost first
        ("10001", [2, 4]),
    ]
    print("=== Flip Binary String ===")
    for A, exp in cases:
        b = brute_force(A)
        o = optimal(A)
        bst = best(A)
        ok = "OK" if b == o == bst else f"MISMATCH(BF={b},OPT={o},BEST={bst})"
        print(f"'{A}' => BF={b} OPT={o} BEST={bst} EXP={exp} {ok}")
