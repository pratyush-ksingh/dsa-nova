"""
Problem: Pretty JSON
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a compact JSON string (no spaces), pretty-print it with proper indentation.
Rules:
  - '{' or '[': print on its own line, increase indent
  - '}' or ']': decrease indent, print on new line
  - ',': append to current line, next token goes on new line
  - Other tokens (keys, values): print with current indent
"""
from typing import List


def _format_json(json: str) -> List[str]:
    """Core formatting logic shared by all approaches."""
    result = []
    indent = 0
    i = 0
    n = len(json)

    while i < n:
        c = json[i]

        if c in ('{', '['):
            result.append('\t' * indent + c)
            indent += 1
            i += 1
        elif c in ('}', ']'):
            indent -= 1
            result.append('\t' * indent + c)
            i += 1
        elif c == ',':
            if result:
                result[-1] += c   # comma goes at end of current line
            i += 1
        else:
            # Read the full token until structural char
            token = []
            while i < n and json[i] not in ('{', '}', '[', ']', ','):
                token.append(json[i])
                i += 1
            if token:
                result.append('\t' * indent + ''.join(token))

    return result


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# ============================================================
def brute_force(json: str) -> List[str]:
    """
    Character-by-character scan with indent tracking.
    Real-life: JSON formatters in IDEs and API debugging tools.
    """
    return _format_json(json)


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(json: str) -> List[str]:
    """
    Same O(n) approach — this problem cannot be solved faster.
    Uses list for output (faster than repeated string concatenation).
    Real-life: High-speed JSON pretty-printer in logging frameworks.
    """
    return _format_json(json)


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(json: str) -> List[str]:
    """
    Same algorithm. All three approaches are asymptotically equivalent.
    Real-life: Embedded JSON formatters in microcontroller logging systems.
    """
    return _format_json(json)


if __name__ == "__main__":
    print("=== Pretty JSON ===")

    json1 = '{A:"B",C:{D:"E",F:{G:"H",I:"J"}}}'
    print(f"\nInput: {json1}")
    print("Output:")
    for line in optimal(json1):
        print(repr(line))

    json2 = '["foo",{"bar":{"baz":123,"qux":456}},"hello"]'
    print(f"\nInput: {json2}")
    print("Output:")
    for line in optimal(json2):
        print(repr(line))
