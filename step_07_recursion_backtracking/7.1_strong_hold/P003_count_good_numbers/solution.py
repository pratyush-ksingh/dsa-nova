"""
Problem: Count Good Numbers (LeetCode #1922)
Difficulty: MEDIUM | XP: 25
"""
MOD = 10 ** 9 + 7


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion counting all strings
# Time: O(5^(n/2) * 4^(n/2))  |  Space: O(n) stack
#
# Recursively build digit strings: at each even index choose
# from 5 even digits, at each odd index from 4 prime digits.
# Count all valid completions. Only feasible for tiny n.
# ============================================================
def brute_force(n: int) -> int:
    def recurse(pos: int) -> int:
        if pos == n:
            return 1
        choices = 5 if pos % 2 == 0 else 4
        return (choices * recurse(pos + 1)) % MOD

    return recurse(0)


# ============================================================
# APPROACH 2: OPTIMAL -- Math + Fast Modular Exponentiation
# Time: O(log n)  |  Space: O(1)
#
# Even positions: indices 0, 2, 4, ... -> ceil(n/2) positions,
#   each has 5 choices (0,2,4,6,8).
# Odd positions: indices 1, 3, 5, ... -> floor(n/2) positions,
#   each has 4 choices (2,3,5,7).
# Answer = 5^ceil(n/2) * 4^floor(n/2)  (mod 10^9+7)
# Use Python's built-in pow(base, exp, mod) for O(log n).
# ============================================================
def optimal(n: int) -> int:
    even_count = (n + 1) // 2   # ceil(n/2)
    odd_count = n // 2           # floor(n/2)
    return (pow(5, even_count, MOD) * pow(4, odd_count, MOD)) % MOD


# ============================================================
# APPROACH 3: BEST -- Same math, explicit fast-power function
# Time: O(log n)  |  Space: O(1)
#
# Same formula but with a hand-written fast_pow to make the
# binary exponentiation explicit -- useful in Java/C++ interviews
# where built-in modular pow may not be available.
# ============================================================
def best(n: int) -> int:
    def fast_pow(base: int, exp: int, mod: int) -> int:
        result = 1
        base %= mod
        while exp > 0:
            if exp & 1:
                result = result * base % mod
            base = base * base % mod
            exp >>= 1
        return result

    even_count = (n + 1) // 2
    odd_count = n // 2
    return fast_pow(5, even_count, MOD) * fast_pow(4, odd_count, MOD) % MOD


if __name__ == "__main__":
    print("=== Count Good Numbers ===\n")

    tests = [
        (1, 5),
        (4, 400),
        (50, 564908303),
        (3, 100),   # 5*4*5 = 100
        (2, 20),    # 5*4 = 20
    ]

    for n, expected in tests:
        b = brute_force(n) if n <= 20 else "SKIPPED(too large)"
        o = optimal(n)
        bt = best(n)
        ok_o = (o == expected)
        ok_bt = (bt == expected)
        print(f"n={n}  Expected={expected}  Brute={b}  "
              f"Optimal={o}({'OK' if ok_o else 'FAIL'})  "
              f"Best={bt}({'OK' if ok_bt else 'FAIL'})")
