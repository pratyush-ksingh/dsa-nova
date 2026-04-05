# Introduction to Heaps

> **Batch 1 of 12** | **Topic:** Heaps | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Implement a **Min-Heap** data structure that supports three core operations:
- **insert(val):** Add a new element to the heap.
- **extractMin():** Remove and return the smallest element.
- **heapify(arr):** Convert an arbitrary array into a valid min-heap.

### Analogy
Think of a **corporate hierarchy** where the CEO (smallest priority number = most important) always sits at the top. When a new employee joins, they start at the bottom and get promoted (bubbled up) until their rank is appropriate. When the CEO leaves, the lowest-ranked person temporarily takes the top seat and gets demoted (bubbled down) until order is restored.

### Key Observations
1. A heap is a **complete binary tree** stored in an array -- the parent of index `i` is at `(i-1)/2`, children at `2i+1` and `2i+2`. **Aha:** No pointers needed; array index math gives O(1) parent/child access.
2. The heap property says every parent <= its children (min-heap). **Aha:** Only the root is guaranteed to be the global minimum; the rest is *partially* ordered, which is what makes it cheaper than full sorting.
3. Building a heap from an array by calling siftDown from the last internal node to the root is O(n), not O(n log n). **Aha:** Most nodes are near the leaves and sift down only a short distance -- the math sums to linear.

### Examples

| Operation | Heap State (array) | Return |
|-----------|-------------------|--------|
| insert(5) | [5] | - |
| insert(3) | [3, 5] | - |
| insert(7) | [3, 5, 7] | - |
| insert(1) | [1, 3, 7, 5] | - |
| extractMin() | [3, 5, 7] | 1 |
| extractMin() | [5, 7] | 3 |
| heapify([9,4,7,1,2]) | [1, 2, 7, 9, 4] | - |

### Constraints
- 1 <= number of operations <= 10^5
- -10^9 <= element value <= 10^9

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (sorted array) | Array + sort | Keep array sorted; insert = find position + shift; extractMin = pop front. Simple but O(n) insert. |
| Optimal (binary heap) | Array-backed complete binary tree | Sift-up / sift-down give O(log n) insert and extract. Array storage is cache-friendly. |
| Best (same binary heap, Floyd's heapify) | Array-backed complete binary tree | For batch build, bottom-up heapify achieves O(n) vs O(n log n) repeated inserts. |

**Pattern cue:** Whenever you need repeated access to the min (or max) element with dynamic insertions, think **heap / priority queue**.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Sorted Array)
**Intuition:** Maintain a sorted array. Insert uses binary search + shift. ExtractMin pops index 0.

**Steps:**
1. On `insert(val)`: binary-search for position, shift elements right, place val.
2. On `extractMin()`: remove and return `arr[0]`, shift everything left.
3. On `heapify(arr)`: just sort the array.

| Metric | Value |
|--------|-------|
| Time -- insert | O(n) due to shifting |
| Time -- extractMin | O(n) due to shifting |
| Time -- heapify | O(n log n) sorting |
| Space | O(n) |

### BUD Transition (Brute -> Optimal)
**Bottleneck:** Shifting elements on every insert/extract is O(n). We only need the *minimum*, not full sorted order. A binary heap maintains just enough structure (parent <= children) to access the min in O(1) and restore order in O(log n).

### Approach 2 -- Optimal (Binary Heap with sift-up / sift-down)
**Intuition:** Store elements in an array representing a complete binary tree. After insert, bubble up. After extractMin, move last element to root and bubble down.

**Steps:**
1. **insert(val):** Append val to end. Sift up: while val < parent, swap with parent.
2. **extractMin():** Save `arr[0]`. Move `arr[last]` to index 0. Sift down: while current > smaller child, swap with that child.
3. **heapify(arr):** For each index from `n/2 - 1` down to 0, call siftDown.

**Dry-Run Trace -- insert sequence [5, 3, 7, 1]:**

| Step | Action | Array | Swaps |
|------|--------|-------|-------|
| 1 | insert(5) | [5] | none |
| 2 | insert(3) | [5,3] -> siftUp -> [3,5] | swap(0,1) |
| 3 | insert(7) | [3,5,7] | none (7>3) |
| 4 | insert(1) | [3,5,7,1] -> siftUp -> [3,1,7,5] -> [1,3,7,5] | swap(3,1) then swap(1,0) |

**Dry-Run Trace -- extractMin from [1,3,7,5]:**

| Step | Action | Array |
|------|--------|-------|
| 1 | Save min=1, move last(5) to root | [5,3,7] |
| 2 | siftDown: 5>3, swap with left child | [3,5,7] |
| 3 | 5 has no children smaller | done, return 1 |

| Metric | Value |
|--------|-------|
| Time -- insert | O(log n) |
| Time -- extractMin | O(log n) |
| Time -- heapify | O(n) (Floyd's algorithm) |
| Space | O(n) |

### Approach 3 -- Best (Same as Optimal)
For a standard min-heap, Approach 2 **is** the best. You cannot beat O(log n) for insert/extract in a comparison-based model, and Floyd's heapify is already O(n). The "best" refinement is recognizing that `heapify` should use bottom-up siftDown, not repeated inserts.

| Metric | Value |
|--------|-------|
| Time -- insert | O(log n) |
| Time -- extractMin | O(log n) |
| Time -- heapify | O(n) |
| Space | O(n) |

---

## 4. COMPLEXITY INTUITIVELY

- **O(log n) insert/extract:** The tree has log n levels. Each sift operation moves at most one level per step, so at most log n swaps.
- **O(n) heapify:** Nodes at the bottom (n/2 of them) sift down 0 levels, n/4 sift down 1 level, n/8 sift down 2 levels... The sum n * (1/4 + 2/8 + 3/16 + ...) converges to O(n).
- **O(n) space:** We store exactly n elements in the array.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| extractMin on empty heap | Return -1 or throw exception |
| Single element | Insert and extract should both work; sift has nothing to do |
| All elements identical | Heap still valid; sifting terminates immediately |
| Negative numbers | No special handling; comparison works the same |
| Large n (10^5) | O(n log n) total for n inserts is fine |

**Common mistakes:**
- Off-by-one in child index calculation: children of `i` are `2*i+1` and `2*i+2`, NOT `2*i` and `2*i+1` (that is 1-indexed).
- Forgetting to check if right child exists before comparing left vs right in siftDown.
- Using siftUp for heapify (gives O(n log n) instead of O(n)).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Can you do it in-place? | Yes. Heapify works in-place on the input array. |
| Why not use a BST? | BST gives O(log n) min too, but with higher constant factor and pointer overhead. Heap is simpler and more cache-friendly. |
| Max-heap variant? | Flip all comparisons (parent >= children). |
| Follow-up: k-way merge? | Use a min-heap of size k to efficiently merge k sorted lists. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Kth Largest Element | Use a min-heap of size k |
| Merge K Sorted Lists | Min-heap tracks the smallest head across k lists |
| Median in a Stream | Two heaps: max-heap for lower half, min-heap for upper half |
| Dijkstra's Algorithm | Priority queue (min-heap) drives the greedy shortest-path expansion |
| Heap Sort | Build max-heap, repeatedly extractMax to sort in-place |
