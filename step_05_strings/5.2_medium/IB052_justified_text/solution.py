"""
Problem: Justified Text
Difficulty: HARD | XP: 50
Source: InterviewBit

Given an array of words and a line width L, format the text so that
each line has exactly L characters and is fully justified.

Rules:
- Pack as many words per line as possible (greedy).
- Distribute extra spaces evenly between words; leftmost gaps get +1 if uneven.
- Last line is left-justified (words separated by 1 space, trailing spaces to fill L).
"""
from typing import List


def build_line(words: List[str], start: int, end: int, L: int, is_last: bool) -> str:
    """Build a justified line from words[start:end]."""
    line_words = words[start:end]
    num_gaps = len(line_words) - 1
    total_word_chars = sum(len(w) for w in line_words)

    if is_last or num_gaps == 0:
        line = " ".join(line_words)
        return line + " " * (L - len(line))

    total_spaces = L - total_word_chars
    space_per_gap, extra = divmod(total_spaces, num_gaps)
    result = []
    for i, word in enumerate(line_words[:-1]):
        result.append(word)
        spaces = space_per_gap + (1 if i < extra else 0)
        result.append(" " * spaces)
    result.append(line_words[-1])
    return "".join(result)


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n * L)  |  Space: O(n * L)
# Greedy line packing with explicit length tracking.
# ============================================================
def brute_force(words: List[str], L: int) -> List[str]:
    result = []
    i = 0
    while i < len(words):
        line_len = len(words[i])
        j = i + 1
        while j < len(words) and line_len + 1 + len(words[j]) <= L:
            line_len += 1 + len(words[j])
            j += 1

        line_words = words[i:j]
        is_last = (j == len(words))

        if is_last or len(line_words) == 1:
            line = " ".join(line_words)
            result.append(line + " " * (L - len(line)))
        else:
            total_chars = sum(len(w) for w in line_words)
            total_spaces = L - total_chars
            num_gaps = len(line_words) - 1
            sp, extra = divmod(total_spaces, num_gaps)
            line = ""
            for k, w in enumerate(line_words):
                line += w
                if k < num_gaps:
                    line += " " * (sp + (1 if k < extra else 0))
            result.append(line)
        i = j
    return result


# ============================================================
# APPROACH 2: OPTIMAL — Using helper function
# Time: O(n * L)  |  Space: O(n * L)
# Same greedy algorithm but delegates to build_line helper.
# ============================================================
def optimal(words: List[str], L: int) -> List[str]:
    result = []
    i = 0
    while i < len(words):
        line_len = len(words[i])
        j = i + 1
        while j < len(words) and line_len + 1 + len(words[j]) <= L:
            line_len += 1 + len(words[j])
            j += 1
        result.append(build_line(words, i, j, L, j == len(words)))
        i = j
    return result


# ============================================================
# APPROACH 3: BEST — Pythonic with enumerate and zip
# Time: O(n * L)  |  Space: O(n * L)
# Cleaner Python style; same algorithm with list comprehensions.
# ============================================================
def best(words: List[str], L: int) -> List[str]:
    lines = []
    i = 0
    n = len(words)

    # Pack words into lines
    while i < n:
        j = i
        length = 0
        while j < n and length + len(words[j]) + (1 if j > i else 0) <= L:
            length += len(words[j]) + (1 if j > i else 0)
            j += 1

        line_words = words[i:j]
        num_gaps = len(line_words) - 1
        is_last = (j == n)

        if is_last or num_gaps == 0:
            line = " ".join(line_words).ljust(L)
        else:
            total_word_len = sum(len(w) for w in line_words)
            total_spaces = L - total_word_len
            sp, extra = divmod(total_spaces, num_gaps)
            gaps = [" " * (sp + (1 if k < extra else 0)) for k in range(num_gaps)]
            # Interleave words and gaps
            parts = []
            for k, w in enumerate(line_words):
                parts.append(w)
                if k < num_gaps:
                    parts.append(gaps[k])
            line = "".join(parts)

        lines.append(line)
        i = j

    return lines


if __name__ == "__main__":
    print("=== Justified Text ===")
    words1 = ["This", "is", "an", "example", "of", "text", "justification."]
    L1 = 16
    words2 = ["What", "must", "be", "acknowledgment", "shall", "be"]
    L2 = 16

    print("--- Test 1 (L=16) ---")
    for approach, fn in [("Brute", brute_force), ("Optimal", optimal), ("Best", best)]:
        print(f"{approach}:")
        for line in fn(words1[:], L1):
            print(f"  |{line}|")

    print("\n--- Test 2 (L=16) ---")
    print("Best:")
    for line in best(words2[:], L2):
        print(f"  |{line}|")
