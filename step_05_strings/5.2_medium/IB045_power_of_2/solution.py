"""Problem: Power of 2
Difficulty: MEDIUM | XP: 25"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(log n)  |  Space: O(1)
# Parse to int; repeatedly divide by 2. Python ints are arbitrary precision.
# ============================================================
def brute_force(A: str) -> int:
    try:
        n = int(A)
    except ValueError:
        return 0
    if n <= 0:
        return 0
    while n > 1:
        if n % 2 != 0:
            return 0
        n //= 2
    return 1


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(log n)  |  Space: O(1)
# A power of 2 has exactly one bit set: bin(n).count('1') == 1.
# ============================================================
def optimal(A: str) -> int:
    try:
        n = int(A)
    except ValueError:
        return 0
    if n <= 0:
        return 0
    return 1 if bin(n).count('1') == 1 else 0


# ============================================================
# APPROACH 3: BEST
# Time: O(log n)  |  Space: O(1)
# n & (n-1) == 0 iff n is a power of 2 (with n > 0).
# ============================================================
def best(A: str) -> int:
    try:
        n = int(A)
    except ValueError:
        return 0
    if n <= 0:
        return 0
    return 1 if (n & (n - 1)) == 0 else 0


if __name__ == "__main__":
    tests = [
        ("1",          1),
        ("2",          1),
        ("4",          1),
        ("1000",       0),
        ("1024",       1),
        ("3",          0),
        ("0",          0),
        ("2147483648", 1),
        ("9999999999999999999999999999998", 0),
    ]
    print("=== Power of 2 ===")
    for A, expected in tests:
        b  = brute_force(A)
        o  = optimal(A)
        be = best(A)
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"Input: {A[:30]:30} | Brute: {b} | Optimal: {o} | Best: {be} | Expected: {expected} | {status}")
