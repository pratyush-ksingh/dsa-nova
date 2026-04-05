"""
Problem: Hand of Straights
LeetCode 846 | Difficulty: MEDIUM | XP: 25

Alice has a hand of cards. She wants to rearrange them into groups of
groupSize consecutive numbers. Return True if she can do so, else False.

Key Insight: Always start a new group from the smallest available card.
             Use a sorted frequency map (TreeMap in Java, sorted Counter in Python).
"""
from typing import List
from collections import Counter


# ============================================================
# APPROACH 1: BRUTE FORCE  (Sort + greedy scan)
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def brute_force(hand: List[int], groupSize: int) -> bool:
    """
    Sort the hand. Repeatedly find the smallest remaining card, then
    greedily remove a complete consecutive group starting from it.
    Uses list.remove() which is O(n) per call -> O(n^2) total.
    """
    if len(hand) % groupSize != 0:
        return False
    arr = sorted(hand)
    while arr:
        start = arr[0]
        for card in range(start, start + groupSize):
            if card not in arr:
                return False
            arr.remove(card)   # O(n) search + removal
    return True


# ============================================================
# APPROACH 2: OPTIMAL  (Counter + sorted keys)
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def optimal(hand: List[int], groupSize: int) -> bool:
    """
    Build a frequency map. Iterate over unique values in sorted order.
    For each value with count > 0, attempt to consume a group of
    groupSize consecutive values. If any required next value is missing,
    return False.
    """
    if len(hand) % groupSize != 0:
        return False
    freq = Counter(hand)
    for card in sorted(freq):
        count = freq[card]
        if count == 0:
            continue
        # Consume `count` groups starting at `card`
        for i in range(groupSize):
            if freq[card + i] < count:
                return False
            freq[card + i] -= count
    return True


# ============================================================
# APPROACH 3: BEST  (same O(n log n), identical optimal logic)
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def best(hand: List[int], groupSize: int) -> bool:
    """
    Identical to optimal. The bottleneck is sorting unique keys — O(n log n).
    No O(n) algorithm is known for this problem because we must process
    cards in sorted order (inherently requires a sort or priority queue).

    Minor enhancement: exit early as soon as any frequency goes negative
    rather than finishing the inner loop.
    """
    if len(hand) % groupSize != 0:
        return False
    freq = Counter(hand)
    for card in sorted(freq):
        count = freq[card]
        if count <= 0:
            continue
        for step in range(1, groupSize):
            freq[card + step] -= count
            if freq[card + step] < 0:
                return False
        freq[card] = 0
    return True


if __name__ == "__main__":
    test_cases = [
        ([1, 2, 3, 6, 2, 3, 4, 7, 8], 3, True),
        ([1, 2, 3, 4, 5], 4, False),
        ([1], 1, True),
        ([1, 2, 3, 4, 5, 6], 2, True),
        ([3, 2, 1, 2, 3, 4, 3, 4, 5, 9, 10, 11], 3, True),
    ]
    print("=== Hand of Straights ===")
    for hand, gs, expected in test_cases:
        b   = brute_force(hand, gs)
        o   = optimal(hand, gs)
        bst = best(hand, gs)
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"  hand={hand} gs={gs} => brute={b}, optimal={o}, best={bst} "
              f"(expected {expected}) [{status}]")
