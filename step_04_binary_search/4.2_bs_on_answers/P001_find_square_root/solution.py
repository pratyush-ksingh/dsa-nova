"""
Problem: Find Square Root (LeetCode #69)
Difficulty: EASY | XP: 10

Given non-negative integer x, return floor(sqrt(x)).
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear Scan)
# Time: O(sqrt(x)) | Space: O(1)
# ============================================================
def brute_force(x: int) -> int:
    """Try every integer from 0 upward until r*r exceeds x."""
    if x < 2:
        return x
    r = 1
    while r * r <= x:
        r += 1
    return r - 1


# ============================================================
# APPROACH 2: OPTIMAL (Binary Search on Answer)
# Time: O(log x) | Space: O(1)
# ============================================================
def optimal(x: int) -> int:
    """Binary search on [1, x//2] for the largest r with r*r <= x."""
    if x < 2:
        return x
    lo, hi = 1, x // 2
    ans = 1
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if mid * mid <= x:
            ans = mid       # candidate, try larger
            lo = mid + 1
        else:
            hi = mid - 1
    return ans


# ============================================================
# APPROACH 3: BEST (Newton's Method -- Integer Version)
# Time: O(log(log(x))) | Space: O(1)
# ============================================================
def best(x: int) -> int:
    """Newton's method: r = (r + x/r) / 2, converges quadratically."""
    if x < 2:
        return x
    r = x // 2
    while r * r > x:
        r = (r + x // r) // 2
    return r


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Find Square Root ===\n")

    test_cases = [
        (0, 0),
        (1, 1),
        (4, 2),
        (8, 2),
        (15, 3),
        (16, 4),
        (100, 10),
        (2147483647, 46340),
    ]

    for x, expected in test_cases:
        b = brute_force(x)
        o = optimal(x)
        n = best(x)
        status = "PASS" if b == expected and o == expected and n == expected else "FAIL"
        print(f"Input: {x}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Newton:   {n}")
        print(f"  Expected: {expected}  [{status}]\n")
