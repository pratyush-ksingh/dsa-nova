"""
Problem: Lemonade Change (LeetCode #860)
Difficulty: EASY | XP: 10

Each lemonade costs $5. Customers pay $5, $10, or $20.
Return true if you can give correct change to every customer.
Greedy: prefer $10+$5 over $5+$5+$5 for $20 change.
"""
from typing import List


# ============================================================
# GREEDY SIMULATION
# Time: O(n) | Space: O(1)
# ============================================================
def lemonade_change(bills: List[int]) -> bool:
    fives = tens = 0

    for bill in bills:
        if bill == 5:
            fives += 1
        elif bill == 10:
            if fives == 0:
                return False
            fives -= 1
            tens += 1
        else:  # bill == 20
            # Prefer giving $10 + $5 (preserves $5 bills)
            if tens > 0 and fives > 0:
                tens -= 1
                fives -= 1
            elif fives >= 3:
                fives -= 3
            else:
                return False
            # No need to track $20 bills -- never used as change

    return True


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Lemonade Change ===\n")

    test_cases = [
        ([5, 5, 5, 10, 20], True),
        ([5, 5, 10, 10, 20], False),
        ([5, 5, 10], True),
        ([10], False),
        ([5], True),
        ([5, 5, 5, 5, 20, 20], True),
        ([5, 5, 5, 10, 5, 5, 10, 20, 20, 20], False),
    ]

    for bills, expected in test_cases:
        result = lemonade_change(bills)
        passes = result == expected
        print(f"bills = {bills}")
        print(f"  Result: {result} | Expected: {expected} | Pass: {passes}\n")
