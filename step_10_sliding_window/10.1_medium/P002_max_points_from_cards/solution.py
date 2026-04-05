"""
Problem: Max Points from Cards (LeetCode #1423)
Difficulty: MEDIUM | XP: 25

Pick k cards from either end to maximize sum.
Sliding window on minimum subarray of length n-k.
"""
from typing import List


# ============================================================
# APPROACH 1: PREFIX-SUFFIX SPLIT
# Time: O(k) | Space: O(1)
# ============================================================
def prefix_suffix(card_points: List[int], k: int) -> int:
    """Try all splits: i from left, k-i from right."""
    n = len(card_points)

    # Start with all k from the left
    left_sum = sum(card_points[:k])
    max_score = left_sum
    right_sum = 0

    # Move one card at a time from left to right
    for i in range(k):
        left_sum -= card_points[k - 1 - i]
        right_sum += card_points[n - 1 - i]
        max_score = max(max_score, left_sum + right_sum)

    return max_score


# ============================================================
# APPROACH 2: OPTIMAL (Sliding Window on Minimum Subarray)
# Time: O(n) | Space: O(1)
# ============================================================
def sliding_window(card_points: List[int], k: int) -> int:
    """Minimize the sum of the n-k cards NOT taken."""
    n = len(card_points)
    total_sum = sum(card_points)

    if k == n:
        return total_sum

    window_size = n - k
    window_sum = sum(card_points[:window_size])
    min_window_sum = window_sum

    for right in range(window_size, n):
        window_sum += card_points[right] - card_points[right - window_size]
        min_window_sum = min(min_window_sum, window_sum)

    return total_sum - min_window_sum


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Max Points from Cards ===\n")

    test_cases = [
        ([1, 2, 3, 4, 5, 6, 1], 3, 12),
        ([2, 2, 2], 2, 4),
        ([9, 7, 7, 9, 7, 7, 9], 7, 55),
        ([1, 1000, 1], 1, 1),
        ([1, 79, 80, 1, 1, 1, 200, 1], 3, 202),
    ]

    all_pass = True
    for cards, k, expected in test_cases:
        ps = prefix_suffix(cards, k)
        sw = sliding_window(cards, k)

        ok = ps == sw == expected
        all_pass &= ok
        print(f"Input: {str(cards):30s} k={k} | PrefSuf={ps:3d} SlidWin={sw:3d} | "
              f"Expected={expected:3d} [{'PASS' if ok else 'FAIL'}]")

    print(f"\nAll pass: {all_pass}")
