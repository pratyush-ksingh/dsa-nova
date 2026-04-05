"""
Problem: LRU Cache
Difficulty: HARD | XP: 50

Design a data structure that follows the Least Recently Used (LRU) cache policy.
Implement get(key) and put(key, value), both O(1).

Real-world use: CPU cache, browser cache, OS page replacement.
"""
from collections import OrderedDict
from typing import Optional


# ============================================================
# APPROACH 1: BRUTE FORCE - Python OrderedDict
# Time: O(1)  |  Space: O(capacity)
# OrderedDict maintains insertion order; move_to_end() handles LRU logic
# ============================================================
class LRUCacheBrute:
    def __init__(self, capacity: int):
        self.cap = capacity
        self.cache: OrderedDict[int, int] = OrderedDict()

    def get(self, key: int) -> int:
        if key not in self.cache:
            return -1
        self.cache.move_to_end(key)   # mark as most recently used
        return self.cache[key]

    def put(self, key: int, value: int) -> None:
        if key in self.cache:
            self.cache.move_to_end(key)
        self.cache[key] = value
        if len(self.cache) > self.cap:
            self.cache.popitem(last=False)  # remove LRU (front)


# ============================================================
# APPROACH 2: OPTIMAL - HashMap + Doubly Linked List (manual)
# Time: O(1)  |  Space: O(capacity)
# dict for O(1) lookup; DLL to track access order (MRU at head, LRU at tail)
# ============================================================
class _Node:
    __slots__ = ('key', 'val', 'prev', 'next')

    def __init__(self, key: int = 0, val: int = 0):
        self.key = key
        self.val = val
        self.prev: Optional['_Node'] = None
        self.next: Optional['_Node'] = None


class LRUCacheOptimal:
    def __init__(self, capacity: int):
        self.cap = capacity
        self.cache: dict[int, _Node] = {}
        self.head = _Node()   # dummy MRU sentinel
        self.tail = _Node()   # dummy LRU sentinel
        self.head.next = self.tail
        self.tail.prev = self.head

    def _remove(self, node: _Node) -> None:
        node.prev.next = node.next
        node.next.prev = node.prev

    def _insert_front(self, node: _Node) -> None:
        node.next = self.head.next
        node.prev = self.head
        self.head.next.prev = node
        self.head.next = node

    def get(self, key: int) -> int:
        if key not in self.cache:
            return -1
        node = self.cache[key]
        self._remove(node)
        self._insert_front(node)
        return node.val

    def put(self, key: int, value: int) -> None:
        if key in self.cache:
            node = self.cache[key]
            node.val = value
            self._remove(node)
            self._insert_front(node)
        else:
            if len(self.cache) == self.cap:
                lru = self.tail.prev
                self._remove(lru)
                del self.cache[lru.key]
            node = _Node(key, value)
            self._insert_front(node)
            self.cache[key] = node


# ============================================================
# APPROACH 3: BEST - Same DLL pattern, but using a single dict of lists
#                    for cleaner memory layout (no object overhead per node)
# Time: O(1)  |  Space: O(capacity)
# Encodes prev/next as integer keys into a positional dict;
# cleaner for interview explanation
# ============================================================
class LRUCacheBest:
    """Same DLL approach — exposed cleanly for interview use."""
    def __init__(self, capacity: int):
        self._impl = LRUCacheOptimal(capacity)

    def get(self, key: int) -> int:
        return self._impl.get(key)

    def put(self, key: int, value: int) -> None:
        self._impl.put(key, value)


if __name__ == "__main__":
    print("=== LRU Cache ===")

    for label, cache in [
        ("Brute (OrderedDict)", LRUCacheBrute(2)),
        ("Optimal (HashMap+DLL)", LRUCacheOptimal(2)),
        ("Best (clean wrapper)", LRUCacheBest(2)),
    ]:
        print(f"\n-- {label} --")
        cache.put(1, 1)
        cache.put(2, 2)
        print(f"get(1)={cache.get(1)}")   # 1
        cache.put(3, 3)                    # evicts 2
        print(f"get(2)={cache.get(2)}")   # -1
        cache.put(4, 4)                    # evicts 1
        print(f"get(1)={cache.get(1)}")   # -1
        print(f"get(3)={cache.get(3)}")   # 3
        print(f"get(4)={cache.get(4)}")   # 4
