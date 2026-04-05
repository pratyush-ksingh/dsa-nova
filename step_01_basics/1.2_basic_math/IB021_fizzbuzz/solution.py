"""
Problem: FizzBuzz
Difficulty: EASY | XP: 10
Source: InterviewBit

Print numbers from 1 to n, replacing:
  - multiples of 3 with "Fizz"
  - multiples of 5 with "Buzz"
  - multiples of both 3 and 5 with "FizzBuzz"

Example: n=15
1, 2, Fizz, 4, Buzz, Fizz, 7, 8, Fizz, Buzz, 11, Fizz, 13, 14, FizzBuzz
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- If-Else Chain with % Checks
# Time: O(n)  |  Space: O(n) for output list
# Most explicit: check divisibility in priority order.
# ============================================================
def brute_force(n: int) -> List[str]:
    result = []
    for i in range(1, n + 1):
        if i % 3 == 0 and i % 5 == 0:
            result.append("FizzBuzz")
        elif i % 3 == 0:
            result.append("Fizz")
        elif i % 5 == 0:
            result.append("Buzz")
        else:
            result.append(str(i))
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- String Concatenation (No Duplicate %)
# Time: O(n)  |  Space: O(n)
# Build label by concatenating "Fizz" and/or "Buzz" strings;
# avoids checking the 15-divisibility case separately.
# ============================================================
def optimal(n: int) -> List[str]:
    result = []
    for i in range(1, n + 1):
        label = ""
        if i % 3 == 0:
            label += "Fizz"
        if i % 5 == 0:
            label += "Buzz"
        result.append(label if label else str(i))
    return result


# ============================================================
# APPROACH 3: BEST -- Mapping / Rule-Table Approach
# Time: O(n)  |  Space: O(n)
# Store (divisor, word) pairs in a list; iterate over rules.
# Scales cleanly if new rules are added (e.g., 7 -> "Jazz").
# ============================================================
def best(n: int) -> List[str]:
    rules = [(3, "Fizz"), (5, "Buzz")]
    result = []
    for i in range(1, n + 1):
        label = "".join(word for div, word in rules if i % div == 0)
        result.append(label if label else str(i))
    return result


if __name__ == "__main__":
    N = 15
    print("=== FizzBuzz ===")
    print("Brute:  ", brute_force(N))
    print("Optimal:", optimal(N))
    print("Best:   ", best(N))
