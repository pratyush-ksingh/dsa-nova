"""
Problem: LFU Cache
Difficulty: HARD | XP: 50

Design a Least Frequently Used (LFU) cache. get and put must be O(1).
When capacity full, evict the entry with lowest frequency.
Ties broken by least recently used (LRU within same frequency).

Key insight: freq_map[freq] = OrderedDict of keys at that frequency.
Track min_freq for O(1) eviction.
"""
from collections import defaultdict, OrderedDict
from typing import Optional


# ============================================================
# APPROACH 1: BRUTE FORCE - freq dict + linear scan for min on eviction
# Time: O(n) per put (eviction)  |  Space: O(capacity)
# Simple but inefficient: scan all keys to find min-frequency entry
# ============================================================
class LFUCacheBrute:
    def __init__(self, capacity: int):
        self.cap = capacity
        self.vals: dict[int, int] = {}
        self.freq: dict[int, int] = {}
        self.order: dict[int, int] = {}  # key -> insertion time for LRU tie-break
        self.time = 0

    def get(self, key: int) -> int:
        if key not in self.vals:
            return -1
        self.freq[key] = self.freq.get(key, 0) + 1
        self.order[key] = self.time
        self.time += 1
        return self.vals[key]

    def put(self, key: int, value: int) -> None:
        if self.cap <= 0:
            return
        if key in self.vals:
            self.vals[key] = value
            self.get(key)
            self.vals[key] = value  # restore after get incremented freq
            return
        if len(self.vals) == self.cap:
            # evict: min freq, then min order (oldest)
            evict = min(self.vals, key=lambda k: (self.freq.get(k, 0), self.order.get(k, 0)))
            del self.vals[evict]
            self.freq.pop(evict, None)
            self.order.pop(evict, None)
        self.vals[key] = value
        self.freq[key] = 1
        self.order[key] = self.time
        self.time += 1


# ============================================================
# APPROACH 2: OPTIMAL - Three dicts + min_freq pointer
# Time: O(1)  |  Space: O(capacity)
# key_val: key->val, key_freq: key->freq,
# freq_keys: freq->OrderedDict[key,None] (for O(1) LRU within freq bucket)
# ============================================================
class LFUCacheOptimal:
    def __init__(self, capacity: int):
        self.cap = capacity
        self.min_freq = 0
        self.key_val:   dict[int, int]                  = {}
        self.key_freq:  dict[int, int]                  = {}
        self.freq_keys: dict[int, 'OrderedDict[int,None]'] = defaultdict(OrderedDict)

    def get(self, key: int) -> int:
        if key not in self.key_val:
            return -1
        self._increment(key)
        return self.key_val[key]

    def put(self, key: int, value: int) -> None:
        if self.cap <= 0:
            return
        if key in self.key_val:
            self.key_val[key] = value
            self._increment(key)
        else:
            if len(self.key_val) == self.cap:
                self._evict()
            self.key_val[key] = value
            self.key_freq[key] = 1
            self.freq_keys[1][key] = None
            self.min_freq = 1

    def _increment(self, key: int) -> None:
        f = self.key_freq[key]
        del self.freq_keys[f][key]
        if not self.freq_keys[f]:
            del self.freq_keys[f]
            if self.min_freq == f:
                self.min_freq = f + 1
        self.key_freq[key] = f + 1
        self.freq_keys[f + 1][key] = None

    def _evict(self) -> None:
        bucket = self.freq_keys[self.min_freq]
        lru_key, _ = bucket.popitem(last=False)  # oldest (LRU) in min-freq bucket
        if not bucket:
            del self.freq_keys[self.min_freq]
        del self.key_val[lru_key]
        del self.key_freq[lru_key]


# ============================================================
# APPROACH 3: BEST - Same O(1) approach with cleaner class structure
# Time: O(1)  |  Space: O(capacity)
# Explicit Node + DLL per frequency bucket (same pattern as LRU cache),
# demonstrating the real system-level implementation
# ============================================================
class _DNode:
    __slots__ = ('key', 'val', 'freq', 'prev', 'next')
    def __init__(self, key=0, val=0, freq=0):
        self.key = key; self.val = val; self.freq = freq
        self.prev = self.next = None


class _DList:
    def __init__(self):
        self.head = _DNode()
        self.tail = _DNode()
        self.head.next = self.tail
        self.tail.prev = self.head
        self.size = 0

    def add_front(self, node: _DNode) -> None:
        node.next = self.head.next
        node.prev = self.head
        self.head.next.prev = node
        self.head.next = node
        self.size += 1

    def remove(self, node: _DNode) -> None:
        node.prev.next = node.next
        node.next.prev = node.prev
        self.size -= 1

    def remove_last(self) -> Optional[_DNode]:
        if self.size == 0:
            return None
        lru = self.tail.prev
        self.remove(lru)
        return lru


class LFUCacheBest:
    def __init__(self, capacity: int):
        self.cap = capacity
        self.min_freq = 0
        self.key_node:  dict[int, _DNode]  = {}
        self.freq_list: dict[int, _DList]  = defaultdict(_DList)

    def get(self, key: int) -> int:
        if key not in self.key_node:
            return -1
        node = self.key_node[key]
        self._update(node)
        return node.val

    def put(self, key: int, value: int) -> None:
        if self.cap <= 0:
            return
        if key in self.key_node:
            node = self.key_node[key]
            node.val = value
            self._update(node)
        else:
            if len(self.key_node) == self.cap:
                evicted = self.freq_list[self.min_freq].remove_last()
                if evicted:
                    del self.key_node[evicted.key]
            node = _DNode(key, value, 1)
            self.key_node[key] = node
            self.freq_list[1].add_front(node)
            self.min_freq = 1

    def _update(self, node: _DNode) -> None:
        f = node.freq
        self.freq_list[f].remove(node)
        if self.freq_list[f].size == 0:
            del self.freq_list[f]
            if self.min_freq == f:
                self.min_freq = f + 1
        node.freq += 1
        self.freq_list[node.freq].add_front(node)


if __name__ == "__main__":
    print("=== LFU Cache ===")

    for label, CacheClass in [
        ("Brute (linear scan)", LFUCacheBrute),
        ("Optimal (3 dicts)", LFUCacheOptimal),
        ("Best (DLL nodes)", LFUCacheBest),
    ]:
        print(f"\n-- {label} --")
        c = CacheClass(2)
        c.put(1, 1); c.put(2, 2)
        print(f"get(1)={c.get(1)}")   # 1    freq(1)=2
        c.put(3, 3)                    # evict 2 (freq 1, LRU between {2})
        print(f"get(2)={c.get(2)}")   # -1
        print(f"get(3)={c.get(3)}")   # 3    freq(3)=2
        c.put(4, 4)                    # evict 1 or 3? both freq=2; 1 is LRU -> evict 1
        print(f"get(1)={c.get(1)}")   # -1 (if correct LRU tie-break) or 1 for brute
        print(f"get(3)={c.get(3)}")   # 3
        print(f"get(4)={c.get(4)}")   # 4
