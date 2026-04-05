"""Problem: Multiply Strings
Difficulty: MEDIUM | XP: 25"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n * m)  |  Space: O(n + m)
# Use Python's built-in arbitrary precision integers as baseline.
# ============================================================
def brute_force(num1: str, num2: str) -> str:
    return str(int(num1) * int(num2))


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n * m)  |  Space: O(n + m)
# Grade-school multiplication with a position array.
# ============================================================
def optimal(num1: str, num2: str) -> str:
    n, m = len(num1), len(num2)
    result = [0] * (n + m)

    for i in range(n - 1, -1, -1):
        for j in range(m - 1, -1, -1):
            mul  = (ord(num1[i]) - ord('0')) * (ord(num2[j]) - ord('0'))
            p1, p2 = i + j, i + j + 1
            total    = mul + result[p2]
            result[p2] = total % 10
            result[p1] += total // 10

    s = ''.join(map(str, result)).lstrip('0')
    return s if s else '0'


# ============================================================
# APPROACH 3: BEST
# Time: O(n * m)  |  Space: O(n + m)
# Same approach with early exit for zero and cleaner inner loop.
# ============================================================
def best(num1: str, num2: str) -> str:
    if num1 == '0' or num2 == '0':
        return '0'
    n, m = len(num1), len(num2)
    pos = [0] * (n + m)

    for i in range(n - 1, -1, -1):
        a = ord(num1[i]) - ord('0')
        for j in range(m - 1, -1, -1):
            mul = a * (ord(num2[j]) - ord('0')) + pos[i + j + 1]
            pos[i + j + 1] = mul % 10
            pos[i + j]    += mul // 10

    s = ''.join(map(str, pos)).lstrip('0')
    return s if s else '0'


if __name__ == "__main__":
    tests = [
        ("2",         "3",         "6"),
        ("123",       "456",       "56088"),
        ("0",         "12345",     "0"),
        ("999",       "999",       "998001"),
        ("9",         "9",         "81"),
        ("99",        "99",        "9801"),
        ("123456789", "987654321", "121932631112635269"),
    ]
    print("=== Multiply Strings ===")
    for num1, num2, expected in tests:
        b  = brute_force(num1, num2)
        o  = optimal(num1, num2)
        be = best(num1, num2)
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"{num1} * {num2} = {be} (expected {expected}) | {status}")
