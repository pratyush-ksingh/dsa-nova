"""
Problem: Valid BST from Preorder
Difficulty: MEDIUM | XP: 25
Source: InterviewBit
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n²)  |  Space: O(n)
# ============================================================
# Try to reconstruct the BST from the preorder sequence using
# a recursive range-based validator.
# For each element in preorder order, check whether it fits within
# the valid [lo, hi) BST range; greedily consume left then right.
# Returns True iff all n elements are consumed (fully valid).
def brute_force(preorder: List[int]) -> bool:
    n = len(preorder)
    idx = [0]  # mutable index shared across recursive calls

    def can_build(lo: int, hi: int) -> None:
        """Consume as many elements as fit in (lo, hi) BST range."""
        if idx[0] >= n:
            return
        val = preorder[idx[0]]
        if val <= lo or val >= hi:
            return
        idx[0] += 1          # consume root
        can_build(lo, val)   # consume left subtree
        can_build(val, hi)   # consume right subtree

    can_build(float('-inf'), float('inf'))
    return idx[0] == n


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(n)
# ============================================================
# Stack-based single-pass:
# Maintain a decreasing stack representing the current path from
# root down the left spine.  Track `lower_bound` = last popped value
# (the value we "turned right" at — future nodes must exceed it).
#
# For each value v:
#   If v < lower_bound → invalid (would need to go left of a node
#                         we already left via a right turn).
#   Pop stack while top < v (ascending right subtree), updating
#   lower_bound to the last popped value.
#   Push v.
def optimal(preorder: List[int]) -> bool:
    stack: List[int] = []
    lower_bound = float('-inf')

    for val in preorder:
        if val < lower_bound:
            return False
        while stack and stack[-1] < val:
            lower_bound = stack.pop()
        stack.append(val)

    return True


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1)  (in-place stack)
# ============================================================
# Same O(n) stack algorithm as Optimal but reuses the input list
# as the stack (overwriting consumed slots) to avoid any extra space.
def best(preorder: List[int]) -> bool:
    top = -1                       # stack pointer into preorder itself
    lower_bound = float('-inf')

    for val in preorder:
        if val < lower_bound:
            return False
        while top >= 0 and preorder[top] < val:
            lower_bound = preorder[top]
            top -= 1
        top += 1
        preorder[top] = val        # push (overwrites already-processed slot)

    return True


if __name__ == "__main__":
    print("=== Valid BST from Preorder ===\n")

    test_cases = [
        ([5, 2, 1, 3, 6],   True),   # valid BST preorder
        ([5, 2, 6, 1, 3],   False),  # 1 appears after right-turn at 6
        ([1, 2, 3],         True),   # right-skewed BST
        ([3, 2, 1],         True),   # left-skewed BST
        ([2, 1, 3],         True),   # root=2, left=1, right=3
        ([3, 4, 5, 1, 2],   False),  # 1 < lower_bound after 3->4->5
        ([10],              True),   # single element
    ]

    print(f"{'Input':<25} {'Expected':<10} {'BruteForce':<12} {'Optimal':<10} {'Best'}")
    print("-" * 70)
    for arr, expected in test_cases:
        bf_res   = brute_force(arr[:])
        opt_res  = optimal(arr[:])
        best_res = best(arr[:])      # best mutates, so pass a copy
        print(f"{str(arr):<25} {str(expected):<10} {str(bf_res):<12} "
              f"{str(opt_res):<10} {str(best_res)}")
