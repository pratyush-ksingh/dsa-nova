"""
Problem: Bulls and Cows
LeetCode 299 | Difficulty: MEDIUM | XP: 25

You have a secret number and a friend's guess (both same-length strings of digits).
Bulls: digits in the correct position.
Cows:  digits in the secret but in the wrong position (not already counted as bulls).
Return the hint as "xAyB".
"""
from typing import List
from collections import Counter


# ============================================================
# APPROACH 1: BRUTE FORCE  -  Two-pass with lists
# Time: O(n)  |  Space: O(n)
# ============================================================
def brute_force(secret: str, guess: str) -> str:
    """
    Pass 1: Count bulls (exact position matches).
            Collect non-bull digits from both secret and guess.
    Pass 2: For each non-bull digit in guess, check if it appears
            in the remaining secret digits (O(n) inner scan => O(n^2) total
            in naive form, but we use a list here).
    """
    bulls = 0
    secret_remaining: List[str] = []
    guess_remaining: List[str] = []

    for s, g in zip(secret, guess):
        if s == g:
            bulls += 1
        else:
            secret_remaining.append(s)
            guess_remaining.append(g)

    # Count cows: for each guess digit find and remove from secret_remaining
    cows = 0
    for g in guess_remaining:
        if g in secret_remaining:
            cows += 1
            secret_remaining.remove(g)   # remove first occurrence

    return f"{bulls}A{cows}B"


# ============================================================
# APPROACH 2: OPTIMAL  -  Single pass with digit-frequency arrays
# Time: O(n)  |  Space: O(1)  (arrays of size 10 for digits 0-9)
# ============================================================
def optimal(secret: str, guess: str) -> str:
    """
    Keep two frequency arrays of size 10 (one for each digit 0-9).
    Single pass:
      - Matching positions -> increment bulls.
      - Non-matching:
          if secret[i] digit was previously excess in guess  -> increment cows.
          if guess[i]  digit was previously excess in secret -> increment cows.
        Then record the excess digits in their respective arrays.
    """
    bulls = 0
    cows = 0
    secret_count = [0] * 10
    guess_count = [0] * 10

    for s, g in zip(secret, guess):
        if s == g:
            bulls += 1
        else:
            # A previous excess guess digit matching current secret digit
            if guess_count[int(s)] > 0:
                cows += 1
                guess_count[int(s)] -= 1
            else:
                secret_count[int(s)] += 1

            # A previous excess secret digit matching current guess digit
            if secret_count[int(g)] > 0:
                cows += 1
                secret_count[int(g)] -= 1
            else:
                guess_count[int(g)] += 1

    return f"{bulls}A{cows}B"


# ============================================================
# APPROACH 3: BEST  -  Counter-based (clean, Pythonic)
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(secret: str, guess: str) -> str:
    """
    Use Counter for frequency counting.
    Bulls: exact matches.
    Cows:  sum of min(secret_freq[d], guess_freq[d]) for all digits,
           then subtract bulls (those were already counted).
    """
    bulls = sum(s == g for s, g in zip(secret, guess))

    # Count digit frequencies only for non-bull positions
    secret_ctr = Counter(s for s, g in zip(secret, guess) if s != g)
    guess_ctr  = Counter(g for s, g in zip(secret, guess) if s != g)

    cows = sum((secret_ctr & guess_ctr).values())  # intersection = min counts

    return f"{bulls}A{cows}B"


if __name__ == "__main__":
    print("=== Bulls and Cows ===")
    print(f"Brute  ('1807','7810'): {brute_force('1807', '7810')}")  # 1A3B
    print(f"Optimal('1123','0111'): {optimal('1123', '0111')}")      # 1A1B
    print(f"Best   ('1807','7810'): {best('1807', '7810')}")         # 1A3B
