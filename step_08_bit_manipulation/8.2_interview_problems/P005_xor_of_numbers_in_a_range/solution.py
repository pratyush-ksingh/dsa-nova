"""
Problem: XOR of Numbers in a Range [L, R]
Difficulty: MEDIUM | XP: 25

Given two integers L and R, find the XOR of all numbers from L to R (inclusive).
"""


# ============================================================
# APPROACH 1: BRUTE FORCE — XOR all numbers in range
# Time: O(n)  where n = R - L + 1  |  Space: O(1)
# ============================================================
def brute_force(L: int, R: int) -> int:
    """
    Simply XOR every integer from L to R.
    Straightforward but linear time.
    """
    result = 0
    for i in range(L, R + 1):
        result ^= i
    return result


# ============================================================
# HELPER: xor_from_0_to_n
# XOR of all numbers from 0 to n follows a 4-cycle pattern:
#   n % 4 == 0  ->  n
#   n % 4 == 1  ->  1
#   n % 4 == 2  ->  n + 1
#   n % 4 == 3  ->  0
# ============================================================
def _xor_upto(n: int) -> int:
    """Return XOR of all integers from 0 to n in O(1)."""
    mod = n % 4
    if mod == 0: return n
    if mod == 1: return 1
    if mod == 2: return n + 1
    return 0          # mod == 3


# ============================================================
# APPROACH 2: OPTIMAL — Use prefix XOR pattern (O(1))
# Time: O(1)  |  Space: O(1)
# ============================================================
def optimal(L: int, R: int) -> int:
    """
    XOR(L..R) = XOR(0..R) XOR XOR(0..L-1)
    because XOR is its own inverse and numbers 0..L-1 appear in both
    prefix sums and cancel out.

    Each prefix XOR xor_upto(n) follows a 4-cycle pattern derivable by
    observing that XOR of 4 consecutive integers starting at 4k is always 0:
      4k XOR (4k+1) XOR (4k+2) XOR (4k+3)
      = 4k XOR 4k XOR 1 XOR 4k XOR 2 XOR 4k XOR 3  (in the 2-bit pattern)
      = 0 (at upper bits) XOR 0 XOR 1 XOR 2 XOR 3 = 0
    """
    return _xor_upto(R) ^ _xor_upto(L - 1)


# ============================================================
# APPROACH 3: BEST — Same O(1) approach (inline with pattern table)
# Time: O(1)  |  Space: O(1)
# ============================================================
def best(L: int, R: int) -> int:
    """
    Same as Approach 2 but with the helper inlined for a compact one-function
    solution. The 4-cycle lookup table makes the pattern explicit.
    """
    def prefix_xor(n: int) -> int:
        # Pattern repeats every 4 numbers
        rem = n % 4
        return [n, 1, n + 1, 0][rem]

    return prefix_xor(R) ^ prefix_xor(L - 1)


if __name__ == "__main__":
    print("=== XOR of Numbers in a Range ===")
    test_cases = [(1, 5), (3, 8), (0, 0), (2, 4), (5, 5)]
    for L, R in test_cases:
        b = brute_force(L, R)
        o = optimal(L, R)
        bst = best(L, R)
        print(f"[{L},{R}]  Brute={b}  Optimal={o}  Best={bst}  Match={b==o==bst}")
