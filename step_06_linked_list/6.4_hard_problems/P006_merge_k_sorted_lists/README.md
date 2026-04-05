# Merge K Sorted Lists

> **Step 06.6.4** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED

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
Elasticsearch merges results from multiple shards using a k-way merge of sorted posting lists during distributed search queries. Each shard returns a sorted list of document IDs ranked by relevance, and the coordinating node uses a min-heap to merge k sorted lists in O(N log k) time, enabling sub-second search across billions of documents.

## Interview Tips
- TODO
