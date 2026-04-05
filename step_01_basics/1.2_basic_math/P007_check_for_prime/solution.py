"""
Problem: Check for Prime
Difficulty: EASY | XP: 10

Determine whether N is a prime number.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check 2 to N-1
# Time: O(N)  |  Space: O(1)
# Try every candidate divisor from 2 to N-1.
# ============================================================
def brute_force(n: int) -> bool:
    if n <= 1:
        return False
    for i in range(2, n):
        if n % i == 0:
            return False
    return True


# ============================================================
# APPROACH 2: OPTIMAL -- Check up to sqrt(N)
# Time: O(sqrt(N))  |  Space: O(1)
# If N has a factor, one of the pair is <= sqrt(N).
# ============================================================
def optimal(n: int) -> bool:
    if n <= 1:
        return False
    if n <= 3:
        return True
    i = 2
    while i * i <= n:
        if n % i == 0:
            return False
        i += 1
    return True


# ============================================================
# APPROACH 3: BEST -- sqrt(N) with 6k +/- 1 Skip
# Time: O(sqrt(N)) with ~3x constant improvement  |  Space: O(1)
# After checking 2 and 3, only check i and i+2 stepping by 6.
# ============================================================
def best(n: int) -> bool:
    if n <= 1:
        return False
    if n <= 3:
        return True
    if n % 2 == 0 or n % 3 == 0:
        return False

    i = 5
    while i * i <= n:
        if n % i == 0 or n % (i + 2) == 0:
            return False
        i += 6
    return True


# ============================================================
# TESTS
# ============================================================
if __name__ == "__main__":
    test_cases = [
        (0, False), (1, False), (2, True), (3, True), (4, False),
        (7, True), (12, False), (13, True), (97, True), (100, False),
        (9973, True),
    ]

    print("=== Check for Prime ===\n")
    for n, expected in test_cases:
        b = brute_force(n)
        o = optimal(n)
        bt = best(n)
        status = "PASS" if b == o == bt == expected else "FAIL"
        print(f"[{status}] N={n:<10} | Brute={b!s:<5} | Optimal={o!s:<5} | Best={bt!s:<5} | Expected={expected!s:<5}")
