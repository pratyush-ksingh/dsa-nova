"""
Problem: Distinct Numbers in Window
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given array A of N integers and window size K, return array of size N-K+1
where result[i] = number of distinct integers in A[i..i+K-1].
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE - Use set for each window
# Time: O(N * K)  |  Space: O(K)
# ============================================================
def brute_force(A: List[int], k: int) -> List[int]:
    """Count distinct in each window by building a new set each time."""
    n = len(A)
    if k > n:
        return []
    return [len(set(A[i:i+k])) for i in range(n - k + 1)]


# ============================================================
# APPROACH 2: OPTIMAL - Sliding window with frequency HashMap
# Time: O(N)  |  Space: O(K)
# Add new element, remove outgoing. Track distinct count via map size.
# ============================================================
def optimal(A: List[int], k: int) -> List[int]:
    """
    Maintain a frequency dict for the current window.
    When freq[x] drops to 0, del it (so len(freq) == distinct count).
    """
    n = len(A)
    if k > n:
        return []
    freq = defaultdict(int)
    result = []

    for i in range(k):
        freq[A[i]] += 1
    result.append(len(freq))

    for i in range(k, n):
        freq[A[i]] += 1
        out = A[i - k]
        freq[out] -= 1
        if freq[out] == 0:
            del freq[out]
        result.append(len(freq))

    return result


# ============================================================
# APPROACH 3: BEST - Counter-based sliding window (Pythonic)
# Time: O(N)  |  Space: O(K)
# Use collections.Counter for clean code; same asymptotic complexity.
# ============================================================
def best(A: List[int], k: int) -> List[int]:
    """Counter-based sliding window for distinct count."""
    from collections import Counter
    n = len(A)
    if k > n:
        return []
    window = Counter(A[:k])
    result = [len(window)]
    for i in range(k, n):
        window[A[i]] += 1
        window[A[i - k]] -= 1
        if window[A[i - k]] == 0:
            del window[A[i - k]]
        result.append(len(window))
    return result


if __name__ == "__main__":
    print("=== Distinct Numbers in Window ===")
    tests = [
        ([1, 2, 1, 3, 4, 3], 3, [2, 3, 3, 3]),
        ([1, 1, 1, 1], 2, [1, 1, 1]),
        ([1, 2, 3, 4, 5], 3, [3, 3, 3]),
        ([1], 1, [1]),
    ]
    for A, k, exp in tests:
        b = brute_force(A, k)
        o = optimal(A, k)
        be = best(A, k)
        status = "OK" if b == o == be == exp else f"FAIL(b={b},o={o},be={be})"
        print(f"A={A}, k={k}: {b} (exp={exp}) [{status}]")
