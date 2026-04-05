"""
Problem: Pascal's Triangle (LeetCode #118)
Difficulty: EASY | XP: 10

Given an integer numRows, return the first numRows rows of Pascal's Triangle.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Using nCr formula)
# Time: O(numRows^2) | Space: O(numRows^2)
# ============================================================
def brute_force(num_rows: int) -> List[List[int]]:
    """Compute each element as C(row, col) using the combination formula."""

    def ncr(n: int, k: int) -> int:
        """Compute C(n, k) using multiplicative formula."""
        if k > n - k:
            k = n - k
        result = 1
        for i in range(k):
            result = result * (n - i) // (i + 1)
        return result

    triangle = []
    for r in range(num_rows):
        row = [ncr(r, c) for c in range(r + 1)]
        triangle.append(row)
    return triangle


# ============================================================
# APPROACH 2: OPTIMAL (Iterative Row Construction)
# Time: O(numRows^2) | Space: O(numRows^2)
# ============================================================
def optimal(num_rows: int) -> List[List[int]]:
    """Build each row from the previous row. Interior elements are sums of pairs above."""
    triangle = []

    for r in range(num_rows):
        row = [1]  # First element is always 1

        if r > 0:
            prev_row = triangle[r - 1]
            for j in range(1, r):
                row.append(prev_row[j - 1] + prev_row[j])
            row.append(1)  # Last element is always 1

        triangle.append(row)

    return triangle


# ============================================================
# APPROACH 3: BEST (Row-wise nCr without previous row reference)
# Time: O(numRows^2) | Space: O(numRows^2)
# Each element derived from previous element in same row:
#   C(r, c) = C(r, c-1) * (r - c + 1) / c
# ============================================================
def best(num_rows: int) -> List[List[int]]:
    """Each row element computed from the previous element in the same row."""
    triangle = []

    for r in range(num_rows):
        row = []
        val = 1
        for c in range(r + 1):
            row.append(val)
            val = val * (r - c) // (c + 1)
        triangle.append(row)

    return triangle


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Pascal's Triangle ===\n")

    test_cases = [1, 2, 3, 5, 7]

    for num_rows in test_cases:
        b = brute_force(num_rows)
        o = optimal(num_rows)
        be = best(num_rows)

        match = b == o == be
        status = "PASS" if match else "FAIL"

        print(f"numRows = {num_rows}  [{status}]")
        print(f"  Result: {o}")

        # Pretty print the triangle
        for r in range(len(o)):
            indent = "  " * (num_rows - r - 1)
            values = "   ".join(f"{v:>2}" for v in o[r])
            print(f"  {indent}{values}")
        print()
