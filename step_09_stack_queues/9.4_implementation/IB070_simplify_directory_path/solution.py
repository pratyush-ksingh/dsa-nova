"""Problem: Simplify Directory Path
Difficulty: MEDIUM | XP: 25"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# Split on '/', process each token using a list as a stack.
# ============================================================
def brute_force(path: str) -> str:
    parts = path.split('/')
    stack = []
    for part in parts:
        if part == '..':
            if stack:
                stack.pop()
        elif part and part != '.':
            stack.append(part)
    return '/' + '/'.join(stack)


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(n)
# Same logic; uses a deque for semantic clarity. Identical complexity.
# ============================================================
from collections import deque


def optimal(path: str) -> str:
    stack = deque()
    for part in path.split('/'):
        if part == '..':
            if stack:
                stack.pop()
        elif part and part != '.':
            stack.append(part)
    return '/' + '/'.join(stack)


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(n)
# One-liner style using a generator + stack; most Pythonic form.
# ============================================================
def best(path: str) -> str:
    stack = []
    for token in path.split('/'):
        if token == '..':
            if stack:
                stack.pop()
        elif token and token != '.':
            stack.append(token)
    return '/' + '/'.join(stack)


if __name__ == "__main__":
    tests = [
        ("/home/",               "/home"),
        ("/../",                 "/"),
        ("/home//foo/",          "/home/foo"),
        ("/a/./b/../../c/",      "/c"),
        ("/a/../../b/../c//.//", "/c"),
        ("/",                    "/"),
        ("/foo/../bar",          "/bar"),
    ]
    print("=== Simplify Directory Path ===")
    for path, expected in tests:
        b  = brute_force(path)
        o  = optimal(path)
        be = best(path)
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"Input: {path!r:32} | Brute: {b!r:12} | Optimal: {o!r:12} | Best: {be!r:12} | Expected: {expected!r:12} | {status}")
