# Kth Largest Element in Stream

> **Batch 1 of 12** | **Topic:** Heaps | **Difficulty:** EASY | **XP:** 10

**LeetCode #703**

---

## 1. UNDERSTAND

### Problem Statement
Design a class `KthLargest` that finds the **kth largest** element in a stream.

- `KthLargest(int k, int[] nums)` -- Initialize with integer `k` and initial stream `nums`.
- `int add(int val)` -- Append `val` to the stream and return the kth largest element.

The kth largest is the kth element in **sorted descending** order (1-indexed). It is not the kth distinct element.

### Analogy
Imagine a **"Top k" leaderboard** at an arcade. The board only displays k scores. When a new score comes in: if it beats the lowest displayed score, the lowest gets bumped off and the new score goes on. The kth largest is always the *bottom* of that leaderboard -- the entry-level score to make the cut.

### Key Observations
1. We only care about the **top k** elements at any time. Everything smaller is irrelevant. **Aha:** We do not need to store or sort the entire stream.
2. Among the top k elements, we need quick access to the **smallest** of them (i.e., the kth largest overall). **Aha:** A min-heap of size k keeps the kth largest right at the root.
3. When a new element arrives: if it is larger than the heap root, pop the root and push the new element. **Aha:** The heap self-maintains the top-k invariant with O(log k) per add.

### Examples

| Operation | Heap State | Return |
|-----------|-----------|--------|
| KthLargest(3, [4,5,8,2]) | [4,5,8] (min-heap, root=4) | - |
| add(3) | [4,5,8] (3 < 4, ignored) | 4 |
| add(5) | [5,5,8] (5 >= 4, replace 4) | 5 |
| add(10) | [5,8,10] (10 >= 5, replace 5) | 5 |
| add(9) | [8,9,10] (9 >= 5, replace 5) | 8 |
| add(4) | [8,9,10] (4 < 8, ignored) | 8 |

### Constraints
- 1 <= k <= 10^4
- 0 <= nums.length <= 10^4
- -10^4 <= val <= 10^4
- At most 10^4 calls to `add`
- At the time of each `add` call, the stream has at least k elements

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (sorted list) | ArrayList / list with sort | Sort after every add, return kth element. Simple but O(n log n) per call. |
| Optimal (min-heap of size k) | Min-Heap / PriorityQueue | Maintains top-k elements; root is the answer. O(log k) per add. |
| Best (same as optimal) | Min-Heap of size k | Cannot beat O(log k) for dynamic insert with order queries. |

**Pattern cue:** "Kth largest/smallest in a stream" = min-heap of size k (for largest) or max-heap of size k (for smallest).

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Sorted List)
**Intuition:** Store all elements. On each add, sort and return the kth from the end.

**Steps:**
1. Store all initial `nums` in a list.
2. On `add(val)`: append val, sort the list, return `list[len - k]`.

| Metric | Value |
|--------|-------|
| Time -- constructor | O(1) |
| Time -- add | O(n log n) per call |
| Space | O(n) total elements |

### BUD Transition (Brute -> Optimal)
**Bottleneck:** Sorting the entire list each time. We only need the kth largest, so keeping just the top k elements in a min-heap lets us answer in O(log k) per add.

### Approach 2 -- Optimal (Min-Heap of Size k)
**Intuition:** A min-heap of size k always keeps the k largest elements seen so far. The root (minimum of those k) is the kth largest overall.

**Steps:**
1. **Constructor:** Add all nums to a min-heap. If heap size > k, pop the minimum until size == k.
2. **add(val):** If heap size < k, push val. Else if val > heap root, pop root and push val. Return heap root.

**Dry-Run Trace -- KthLargest(3, [4,5,8,2]):**

| Step | Action | Heap (min at left) | Size |
|------|--------|--------------------|------|
| init | push 4,5,8,2 | [2,4,5,8] | 4 |
| trim | pop 2 (size > 3) | [4,5,8] | 3 |
| add(3) | 3 < 4 (root), skip | [4,5,8] | 3 -> return 4 |
| add(5) | 5 >= 4, pop 4, push 5 | [5,5,8] | 3 -> return 5 |
| add(10) | 10 >= 5, pop 5, push 10 | [5,8,10] | 3 -> return 5 |
| add(9) | 9 >= 5, pop 5, push 9 | [8,9,10] | 3 -> return 8 |
| add(4) | 4 < 8, skip | [8,9,10] | 3 -> return 8 |

| Metric | Value |
|--------|-------|
| Time -- constructor | O(n log k) |
| Time -- add | O(log k) |
| Space | O(k) |

### Approach 3 -- Best (Same as Optimal)
The min-heap of size k is already optimal. You cannot maintain a dynamic top-k with better than O(log k) per update in comparison-based models.

| Metric | Value |
|--------|-------|
| Time -- add | O(log k) |
| Space | O(k) |

---

## 4. COMPLEXITY INTUITIVELY

- **O(log k) per add:** The heap has at most k elements. Inserting or removing from a heap of size k takes O(log k) -- one traversal from root to leaf (or vice versa) in a tree of height log k.
- **O(k) space:** We never store more than k elements in the heap. All smaller elements are discarded.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| nums is empty | Heap starts empty; first k adds just push without popping |
| k == 1 | Heap holds exactly 1 element -- always the overall maximum |
| k == len(nums) | The kth largest is the minimum of all elements |
| Duplicate values | Heap handles duplicates naturally; kth largest counts duplicates |
| val equals heap root | Still push (it replaces equal value); answer unchanged |

**Common mistakes:**
- Using a max-heap instead of min-heap (max-heap gives the largest, not kth largest).
- Forgetting to trim the heap to size k after initial construction.
- Off-by-one: the problem is 1-indexed (1st largest = maximum).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Why min-heap and not max-heap? | We want the smallest of the top-k, which is the kth largest. Min-heap gives that at the root in O(1). |
| Can we use quickselect? | Quickselect gives O(n) per query but requires all data -- not suitable for streaming. |
| Thread-safe version? | Wrap heap operations in a synchronized block or use ConcurrentPriorityQueue. |
| Follow-up: kth smallest? | Use a max-heap of size k instead. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Kth Largest Element in Array (LC #215) | One-time query: quickselect O(n). Stream version needs heap. |
| Top K Frequent Elements (LC #347) | Heap of size k on frequency counts |
| Find Median from Data Stream (LC #295) | Two heaps: max-heap for lower half + min-heap for upper half |
| Last Stone Weight (LC #1046) | Max-heap to always access the two heaviest stones |

---

## Real-World Use Case
**Real-time leaderboard in online gaming:** Games like Fortnite track the top-K scorers using a min-heap of size K. Each new score is compared against the heap's minimum; if it's larger, it replaces the root. This gives O(log K) updates per event, powering live leaderboards for millions of concurrent players.
