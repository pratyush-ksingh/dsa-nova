"""Problem: Remove K Digits
Difficulty: MEDIUM | XP: 25"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n * k)  |  Space: O(n)
# Remove k digits one at a time: each time remove the first
# digit that is greater than its successor (a "peak").
# ============================================================
def brute_force(num: str, k: int) -> str:
    digits = list(num)
    for _ in range(k):
        removed = False
        for i in range(len(digits) - 1):
            if digits[i] > digits[i + 1]:
                digits.pop(i)
                removed = True
                break
        if not removed:
            digits.pop()  # remove last if already non-decreasing

    result = ''.join(digits).lstrip('0')
    return result if result else '0'


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(n)
# Monotonic increasing stack: pop digits larger than current
# (spending k removals). Remaining removes come from the end.
# ============================================================
def optimal(num: str, k: int) -> str:
    stack = []
    for c in num:
        while k > 0 and stack and stack[-1] > c:
            stack.pop()
            k -= 1
        stack.append(c)

    # If k > 0, remove from end (most significant remaining)
    stack = stack[:-k] if k else stack

    result = ''.join(stack).lstrip('0')
    return result if result else '0'


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(n)
# Same stack approach; explicitly handles all edge cases inline.
# ============================================================
def best(num: str, k: int) -> str:
    stack = []
    for c in num:
        while k and stack and stack[-1] > c:
            stack.pop()
            k -= 1
        stack.append(c)
    if k:
        stack = stack[:-k]
    result = ''.join(stack).lstrip('0')
    return result or '0'


if __name__ == "__main__":
    tests = [
        ("1432219",     3, "1219"),
        ("10200",       1, "200"),
        ("10",          2, "0"),
        ("9",           1, "0"),
        ("112",         1, "11"),
        ("1234567890",  9, "0"),
    ]
    print("=== Remove K Digits ===")
    for num, k, expected in tests:
        b  = brute_force(num, k)
        o  = optimal(num, k)
        be = best(num, k)
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"num={num!r:15} k={k} | Brute: {b!r:8} | Optimal: {o!r:8} | Best: {be!r:8} | Expected: {expected!r:8} | {status}")
