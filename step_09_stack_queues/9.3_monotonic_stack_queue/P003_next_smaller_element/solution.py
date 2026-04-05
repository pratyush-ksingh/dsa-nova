"""
Problem: Next Smaller Element
Difficulty: MEDIUM | XP: 25

For each element in the array, find the next smaller element to its right.
Return -1 if no smaller element exists.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Scan Right for Each Element)
# Time: O(n^2)  |  Space: O(1) extra (O(n) for result)
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    """For each element, scan all elements to its right to find the first smaller one."""
    n = len(arr)
    result = [-1] * n

    for i in range(n):
        for j in range(i + 1, n):
            if arr[j] < arr[i]:
                result[i] = arr[j]
                break

    return result


# ============================================================
# APPROACH 2: OPTIMAL (Monotonic Stack -- Right to Left)
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    """Traverse from right to left, maintain an increasing monotonic stack."""
    n = len(arr)
    result = [-1] * n
    stack = []  # stores elements, maintains increasing order from top

    for i in range(n - 1, -1, -1):
        # Pop elements that are >= current element (they can't be "next smaller")
        while stack and stack[-1] >= arr[i]:
            stack.pop()

        # If stack is non-empty, top is the next smaller element
        if stack:
            result[i] = stack[-1]

        # Push current element
        stack.append(arr[i])

    return result


# ============================================================
# APPROACH 3: BEST (Same Monotonic Stack -- Left to Right Variant)
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(arr: List[int]) -> List[int]:
    """Traverse left to right, stack holds indices of elements awaiting their NSE."""
    n = len(arr)
    result = [-1] * n
    stack = []  # stores indices

    for i in range(n):
        # Current element is the NSE for all stack elements >= arr[i]
        while stack and arr[stack[-1]] > arr[i]:
            idx = stack.pop()
            result[idx] = arr[i]

        stack.append(i)

    # Elements remaining in stack have no NSE (already -1)
    return result


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Next Smaller Element ===\n")

    test_cases = [
        ([4, 5, 2, 10, 8], [2, 2, -1, 8, -1]),
        ([1, 3, 2, 4], [-1, 2, -1, -1]),
        ([5, 4, 3, 2, 1], [4, 3, 2, 1, -1]),
        ([1, 2, 3, 4, 5], [-1, -1, -1, -1, -1]),
        ([3], [-1]),
        ([2, 1, 4, 3], [1, -1, 3, -1]),
    ]

    for arr, expected in test_cases:
        b = brute_force(arr)
        o = optimal(arr)
        r = best(arr)
        status = "PASS" if o == expected and r == expected and b == expected else "FAIL"
        print(f"Input:    {arr}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {r}")
        print(f"  Expected: {expected}  [{status}]\n")
