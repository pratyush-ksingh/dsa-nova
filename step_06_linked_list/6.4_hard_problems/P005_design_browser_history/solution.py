"""
Problem: Design Browser History (LeetCode #1472)
Difficulty: MEDIUM | XP: 25
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- ArrayList with current index
# Time: visit O(n) worst (trim), back/forward O(1)  |  Space: O(n)
#
# Store history in a Python list. Keep a pointer (curr_idx).
# On visit: truncate anything after curr_idx, then append.
# On back/forward: clamp-move the pointer.
# ============================================================
class BrowserHistoryBrute:
    def __init__(self, homepage: str):
        self.history = [homepage]
        self.curr = 0

    def visit(self, url: str) -> None:
        # Remove forward history
        self.history = self.history[:self.curr + 1]
        self.history.append(url)
        self.curr += 1

    def back(self, steps: int) -> str:
        self.curr = max(0, self.curr - steps)
        return self.history[self.curr]

    def forward(self, steps: int) -> str:
        self.curr = min(len(self.history) - 1, self.curr + steps)
        return self.history[self.curr]


# ============================================================
# APPROACH 2: OPTIMAL -- Doubly Linked List
# Time: visit O(1), back O(steps), forward O(steps)  |  Space: O(n)
#
# Each node holds a URL and prev/next pointers.
# Visit: create new node, link to current, move current forward
#        (discarding forward chain).
# Back/Forward: walk the pointer by steps (clamped).
# ============================================================
class _DLLNode:
    def __init__(self, url: str):
        self.url = url
        self.prev: '_DLLNode | None' = None
        self.next: '_DLLNode | None' = None


class BrowserHistoryOptimal:
    def __init__(self, homepage: str):
        self.curr = _DLLNode(homepage)

    def visit(self, url: str) -> None:
        node = _DLLNode(url)
        node.prev = self.curr
        self.curr.next = node   # forward chain cut here (old next discarded)
        self.curr = node

    def back(self, steps: int) -> str:
        while steps > 0 and self.curr.prev:
            self.curr = self.curr.prev
            steps -= 1
        return self.curr.url

    def forward(self, steps: int) -> str:
        while steps > 0 and self.curr.next:
            self.curr = self.curr.next
            steps -= 1
        return self.curr.url


# ============================================================
# APPROACH 3: BEST -- Single array + pointer (most cache-friendly)
# Time: visit O(1), back O(1), forward O(1)  |  Space: O(n)
#
# Use a list as a circular buffer (just truncate on visit).
# curr_idx tracks position; last_idx tracks the last valid page.
# This avoids node allocations and pointer chasing -- best
# cache locality of all three approaches.
# ============================================================
class BrowserHistoryBest:
    def __init__(self, homepage: str):
        self.history = [homepage]
        self.curr = 0
        self.last = 0   # last valid index (forward boundary)

    def visit(self, url: str) -> None:
        self.curr += 1
        if self.curr < len(self.history):
            self.history[self.curr] = url
        else:
            self.history.append(url)
        self.last = self.curr   # invalidate forward history

    def back(self, steps: int) -> str:
        self.curr = max(0, self.curr - steps)
        return self.history[self.curr]

    def forward(self, steps: int) -> str:
        self.curr = min(self.last, self.curr + steps)
        return self.history[self.curr]


if __name__ == "__main__":
    print("=== Design Browser History ===\n")

    # LeetCode example
    def run_test(BH):
        bh = BH("leetcode.com")
        bh.visit("google.com")
        bh.visit("facebook.com")
        bh.visit("youtube.com")
        r1 = bh.back(1)    # facebook.com
        r2 = bh.back(1)    # google.com
        r3 = bh.forward(1) # facebook.com
        bh.visit("linkedin.com")
        r4 = bh.forward(2) # linkedin.com (no forward pages)
        r5 = bh.back(2)    # google.com
        r6 = bh.back(7)    # leetcode.com
        return [r1, r2, r3, r4, r5, r6]

    expected = ["facebook.com", "google.com", "facebook.com",
                "linkedin.com", "google.com", "leetcode.com"]

    for name, cls in [("Brute", BrowserHistoryBrute),
                      ("Optimal", BrowserHistoryOptimal),
                      ("Best", BrowserHistoryBest)]:
        result = run_test(cls)
        status = "OK" if result == expected else "MISMATCH"
        print(f"{name}: {result}  [{status}]")
