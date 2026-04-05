"""
Problem: Armstrong Number
Difficulty: EASY | XP: 10

Check if N equals the sum of its digits each raised to the
power of the total digit count.
e.g. 153 = 1^3 + 5^3 + 3^3
"""
import math


# ============================================================
# APPROACH 1: BRUTE FORCE -- Two-Pass Division
# Time: O(d)  |  Space: O(1)
# First pass counts digits, second pass computes power sum.
# ============================================================
def brute_force(n: int) -> bool:
    if n < 0:
        return False
    original = n

    # Pass 1: count digits
    d = 0
    temp = n
    if temp == 0:
        d = 1
    while temp > 0:
        d += 1
        temp //= 10

    # Pass 2: compute power sum
    total = 0
    temp = n
    while temp > 0:
        digit = temp % 10
        total += digit ** d
        temp //= 10

    return total == original


# ============================================================
# APPROACH 2: OPTIMAL -- Log10 Digit Count + Single Pass
# Time: O(d)  |  Space: O(1)
# Use log10 to get digit count in O(1), then one extraction pass.
# ============================================================
def optimal(n: int) -> bool:
    if n < 0:
        return False
    if n == 0:
        return True

    d = int(math.log10(n)) + 1
    total = 0
    temp = n

    while temp > 0:
        digit = temp % 10
        total += digit ** d
        temp //= 10

    return total == n


# ============================================================
# APPROACH 3: BEST -- String-Based Pythonic
# Time: O(d)  |  Space: O(d)
# Convert to string, iterate chars, compute power sum.
# ============================================================
def best(n: int) -> bool:
    if n < 0:
        return False
    s = str(n)
    d = len(s)
    return sum(int(ch) ** d for ch in s) == n


# ============================================================
# TESTS
# ============================================================
if __name__ == "__main__":
    test_cases = [
        (0, True), (1, True), (5, True), (10, False),
        (123, False), (153, True), (370, True), (371, True),
        (407, True), (9474, True), (9475, False),
    ]

    print("=== Armstrong Number ===\n")
    for n, expected in test_cases:
        b = brute_force(n)
        o = optimal(n)
        bt = best(n)
        status = "PASS" if b == o == bt == expected else "FAIL"
        print(f"[{status}] N={n:<10} | Brute={b!s:<5} | Optimal={o!s:<5} | Best={bt!s:<5} | Expected={expected!s:<5}")
