# Page Faults in LRU

> **Step 12 | 12.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given a sequence of page references of length `n` and a cache with capacity `c`, simulate the **LRU (Least Recently Used)** page replacement algorithm and return the total number of **page faults**.

A **page fault** occurs when a requested page is not currently in the cache. When a page fault happens and the cache is full, the **least recently used** page is evicted to make room for the new page.

**Constraints:**
- `1 <= n <= 10^5`
- `1 <= c <= 10^3`
- `1 <= pages[i] <= 10^5`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `pages=[7,0,1,2,0,3,0,4,2,3,0,3,2], c=4` | `6` | 6 cache misses with LRU eviction |
| `pages=[1,2,3,4,1,2,5,1,2,3,4,5], c=3` | `10` | 10 faults with capacity 3 |
| `pages=[1,2,3,1,2,3,1,2,3], c=3` | `3` | First 3 are faults, then all hits |
| `pages=[1,2,1,2,1], c=2` | `2` | Pages 1,2 loaded once each |

### Real-Life Analogy
> *Imagine a desk with c open books (cache). When you need a book that is on the desk, you're fine. When you need a book not on the desk (page fault), you must go fetch it from the shelf. If the desk is full, you put back the book you opened least recently. LRU mimics human working memory: keep what you've used recently, discard what you haven't.*

### Key Observations
1. On each page access, check if it's already in the cache — O(1) with a HashSet.
2. On a hit, mark the page as "most recently used."
3. On a miss, evict the least recently used page and insert the new one.
4. Tracking LRU order requires a data structure that supports O(1) insert, delete, and move-to-front.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why OrderedDict / LinkedHashMap?
- We need O(1) lookup (HashSet), plus O(1) ordered insertion/deletion to track recency (doubly-linked list).
- Python's `OrderedDict` and Java's `LinkedHashMap(accessOrder=true)` combine both into a single structure.
- This is identical to the LRU Cache implementation (LeetCode 146).

### Pattern Recognition
- **Pattern:** LRU Cache simulation — classic OS / systems design problem.
- **Classification Cue:** "LRU eviction policy → OrderedDict or HashMap + DoublyLinkedList."

---

## APPROACH LADDER

### Approach 1: Brute Force — List-Based Cache
**Idea:** Keep the cache as a plain ordered list. The front is MRU, the back is LRU. Use linear search for membership checking.

**Steps:**
1. For each page reference:
   - If page is in the list (linear scan O(c)): remove it and re-insert at front.
   - If not (page fault): increment counter. If full, remove the last element. Insert at front.
2. Return total fault count.

**BUD Transition — Bottleneck:** Linear search O(c) per page reference makes total O(n * c). Unnecessary when we can look up in O(1) with a HashSet.

| Metric | Value |
|--------|-------|
| Time   | O(n * c) |
| Space  | O(c) |

---

### Approach 2: Optimal — HashSet + Deque
**What changed:** Use a HashSet for O(1) membership lookup. Use a deque to maintain LRU order (front = MRU, back = LRU).

**Steps:**
1. Maintain `cache_set` (HashSet) and `cache_deque` (Deque, front = MRU).
2. For each page:
   - **Hit** (in set): remove from deque, push to front.
   - **Miss** (page fault): if full, pop from deque back (LRU) and remove from set. Push new page to front, add to set.
3. Return fault count.

**Note:** `deque.remove(page)` is still O(c) in the worst case since deques don't support O(1) arbitrary removal. The true O(n) solution requires a doubly-linked list with direct node references (Approach 3).

| Metric | Value |
|--------|-------|
| Time   | O(n * c) worst case (deque removal), O(n) lookup |
| Space  | O(c) |

---

### Approach 3: Best — OrderedDict (Python) / LinkedHashMap (Java)
**What changed:** Use Python's `OrderedDict` or Java's `LinkedHashMap(accessOrder=true)`. These internally use a doubly-linked list + hashmap, giving O(1) for all operations.

**Steps:**
1. Maintain an `OrderedDict` where insertion order tracks recency (last = MRU, first = LRU).
2. For each page:
   - **Hit**: `move_to_end(page, last=True)` — O(1) relinking.
   - **Miss** (page fault): if full, `popitem(last=False)` evicts LRU. Insert page at end.
3. Return fault count.

**Correctness:** `OrderedDict.move_to_end` relinks the node in the internal doubly-linked list in O(1). This is exactly how the classic LRU Cache is implemented.

| Metric | Value |
|--------|-------|
| Time   | O(n) — O(1) amortized per page reference |
| Space  | O(c) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Not updating recency on a cache **hit** — the LRU candidate must change when a page is re-accessed.
2. Off-by-one when checking `len(cache) == capacity` before insertion.
3. In Java `LinkedHashMap`, using `accessOrder=false` (default) keeps insertion order, not access order — must pass `true` for LRU.

### Edge Cases to Test
- [ ] Capacity = 1 (every new page is a fault)
- [ ] All pages the same (only 1 fault)
- [ ] All pages distinct (every access is a fault)
- [ ] Capacity >= number of distinct pages (all pages fit, no eviction after warm-up)
- [ ] Repeated alternating pattern `[1,2,1,2,1]` with capacity 2

---

## Real-World Use Case
**CPU / OS virtual memory management:** Operating systems use page replacement algorithms (LRU, FIFO, Optimal) to decide which memory pages to evict when RAM is full. LRU is the gold standard because recently accessed pages are likely to be accessed again (temporal locality). The same pattern appears in CPU L1/L2/L3 cache replacement, database buffer pool management (PostgreSQL uses a variant of LRU called "clock sweep"), and CDN cache eviction.

## Interview Tips
- This problem tests your understanding of the LRU Cache data structure. Treat it as "implement LRU Cache + count misses."
- Approach 3 (OrderedDict / LinkedHashMap) is the expected answer in an interview for O(n) time.
- Mention that a true O(1) implementation requires a HashMap + DoublyLinkedList — which is what `OrderedDict` does internally.
- Follow-up: "How would you implement LRU without using language built-ins?" → Implement your own HashMap + DLL.
- Distinguish LRU from LFU (Least Frequently Used) — LFU requires a frequency map and is O(1) with a more complex structure.
