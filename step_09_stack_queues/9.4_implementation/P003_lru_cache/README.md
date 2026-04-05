# LRU Cache

> **Step 09.9.4** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED

## Problem Statement
TODO - Add problem description here

## Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| TODO  | TODO   | TODO        |

## Constraints
- TODO

---

## Approach 1: Brute Force
**Intuition:** TODO

**Steps:**
1. TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Approach 2: Optimal
**Intuition:** TODO

**Steps:**
1. TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Approach 3: Best
**Intuition:** TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Real-World Use Case
Memcached and Redis, used by Facebook, Twitter, and Instagram, implement LRU (Least Recently Used) cache eviction using a hash map combined with a doubly linked list -- exactly the data structure in this problem. When the cache is full, the least recently accessed item (tail of the linked list) is evicted in O(1) time, enabling these systems to serve millions of cache lookups per second.

## Interview Tips
- TODO
