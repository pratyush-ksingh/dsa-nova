"""
Problem: Page Faults in LRU
Difficulty: MEDIUM | XP: 25

Given a sequence of page references and a cache of fixed capacity,
simulate the LRU (Least Recently Used) page replacement policy and
return the total number of page faults.
A page fault occurs whenever a requested page is not in the cache.
"""
from typing import List
from collections import deque, OrderedDict


# ============================================================
# APPROACH 1: BRUTE FORCE (List-based cache, linear search)
# Time: O(n * c) | Space: O(c)
# ============================================================
def brute_force(pages: List[int], capacity: int) -> int:
    """
    Maintain the cache as a plain list.
    For each page reference:
      - If the page is in the cache (linear search), move it to the front (most recent).
      - If not (page fault), add it to the front. If cache is full, remove the last element.
    Count total page faults.
    """
    cache: List[int] = []
    faults = 0

    for page in pages:
        if page in cache:
            # Hit: move to front (most recently used position)
            cache.remove(page)
            cache.insert(0, page)
        else:
            # Miss: page fault
            faults += 1
            if len(cache) == capacity:
                cache.pop()  # evict LRU (last element)
            cache.insert(0, page)

    return faults


# ============================================================
# APPROACH 2: OPTIMAL (HashSet + Deque for O(1) lookup and eviction)
# Time: O(n) | Space: O(c)
# ============================================================
def optimal(pages: List[int], capacity: int) -> int:
    """
    Use a deque (doubly-ended queue) as the LRU cache and a set for O(1) membership.
    - Deque front = most recently used; deque back = least recently used.
    - On a hit: remove the page from its current position in the deque, append to front.
    - On a miss: if full, pop from back (LRU eviction) and remove from set.
      Then append page to front and add to set.

    Note: deque.remove() is O(c) in the worst case, but with the HashSet lookup
    we avoid the O(c) scan on hits — the total is O(n*c) in the worst case due to
    deque removal, but conceptually cleaner than Approach 1.
    The OrderedDict approach in Approach 3 achieves true O(n).
    """
    cache_set: set = set()
    cache_deque: deque = deque()
    faults = 0

    for page in pages:
        if page in cache_set:
            # Hit: move to front
            cache_deque.remove(page)
            cache_deque.appendleft(page)
        else:
            # Miss: page fault
            faults += 1
            if len(cache_deque) == capacity:
                lru = cache_deque.pop()
                cache_set.discard(lru)
            cache_deque.appendleft(page)
            cache_set.add(page)

    return faults


# ============================================================
# APPROACH 3: BEST (OrderedDict / LinkedHashMap — true O(1) per operation)
# Time: O(n) | Space: O(c)
# ============================================================
def best(pages: List[int], capacity: int) -> int:
    """
    Use Python's OrderedDict as a LinkedHashMap. It maintains insertion order
    and supports O(1) move_to_end() operations.
    - On a hit: move_to_end(page, last=True) to mark as most recently used.
    - On a miss: if full, pop the first (least recently used) item.
      Insert the new page at the end (most recently used).

    OrderedDict operations are all O(1) amortized, giving true O(n) overall.
    """
    cache: OrderedDict = OrderedDict()
    faults = 0

    for page in pages:
        if page in cache:
            # Hit: refresh to most recently used
            cache.move_to_end(page, last=True)
        else:
            # Miss: page fault
            faults += 1
            if len(cache) == capacity:
                cache.popitem(last=False)  # evict LRU (first item)
            cache[page] = True  # add as most recently used

    return faults


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Page Faults in LRU ===\n")

    test_cases = [
        ([7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2], 4, 6),
        ([1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5], 3, 10),
        ([1, 2, 3, 1, 2, 3, 1, 2, 3], 3, 3),  # warm-up then all hits
        ([1, 2, 1, 2, 1], 2, 2),
        ([1], 1, 1),
    ]

    for pages, cap, expected in test_cases:
        b = brute_force(pages[:], cap)
        o = optimal(pages[:], cap)
        h = best(pages[:], cap)
        status = "PASS" if b == expected and o == expected and h == expected else "FAIL"
        print(f"Pages={pages}, capacity={cap}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {h}")
        print(f"  Expected: {expected}  [{status}]\n")
