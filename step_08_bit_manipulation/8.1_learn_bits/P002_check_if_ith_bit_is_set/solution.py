"""
Problem: Check if ith Bit is Set
Difficulty: EASY | XP: 10

Given N and position i, check if the ith bit is set (1) or not (0).
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Repeated Division)
# Time: O(i) | Space: O(1)
# ============================================================
def brute_force(n: int, i: int) -> bool:
    """Divide by 2 i times, then check if remainder is 1."""
    for _ in range(i):
        n = n // 2
    return (n % 2) == 1


# ============================================================
# APPROACH 2: OPTIMAL (Left-Shift Mask)
# Time: O(1) | Space: O(1)
# ============================================================
def optimal(n: int, i: int) -> bool:
    """AND with (1 << i). Non-zero means bit is set."""
    return (n & (1 << i)) != 0


# ============================================================
# APPROACH 3: BEST (Right-Shift Number)
# Time: O(1) | Space: O(1)
# ============================================================
def best(n: int, i: int) -> bool:
    """Shift right by i, check if LSB is 1."""
    return ((n >> i) & 1) == 1


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Check if ith Bit is Set ===\n")

    tests = [
        # (N, i, expected)
        (5, 0, True),    # 101, bit 0 = 1
        (5, 1, False),   # 101, bit 1 = 0
        (5, 2, True),    # 101, bit 2 = 1
        (8, 3, True),    # 1000, bit 3 = 1
        (8, 0, False),   # 1000, bit 0 = 0
        (1, 0, True),    # 1, bit 0 = 1
        (1, 1, False),   # 1, bit 1 = 0
        (0, 0, False),   # edge: n=0
        (1023, 9, True), # 1111111111, bit 9 = 1
        (1024, 10, True),# 10000000000, bit 10 = 1
    ]

    for n, i, expected in tests:
        b = brute_force(n, i)
        o = optimal(n, i)
        be = best(n, i)
        status = "PASS" if b == expected and o == expected and be == expected else "FAIL"
        print(f"  N={n} ({bin(n)}), i={i} -> "
              f"Brute:{b} Optimal:{o} Best:{be} Expected:{expected} [{status}]")
