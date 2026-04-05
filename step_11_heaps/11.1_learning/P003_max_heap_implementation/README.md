# Max Heap Implementation

> **Step 11.1** | **Topic:** Heaps | **Difficulty:** MEDIUM | **XP:** 25

---

## 1. UNDERSTAND

### Problem Statement
Implement a **Max-Heap** data structure that supports:
- **insert(val):** Add a new element to the heap.
- **extractMax():** Remove and return the largest element.
- **heapify(arr):** Convert an arbitrary array into a valid max-heap.

### Analogy
Imagine a **tournament bracket** where the strongest player always rises to the top. When the champion (root) is removed, the weakest remaining player temporarily takes the throne and is quickly defeated (bubbled down) until the next strongest reaches the top.

### Key Observations
1. A max-heap is a complete binary tree where every parent >= its children. **Aha:** Only the root is guaranteed to be the global maximum.
2. Stored in an array: parent of `i` is `(i-1)/2`, children at `2i+1` and `2i+2`. **Aha:** Mirror of min-heap with flipped comparisons.
3. Bottom-up heapify is O(n) using Floyd's algorithm. **Aha:** Most nodes are leaves and need zero work.

### Examples

| Operation | Heap State (array) | Return |
|-----------|--------------------|--------|
| insert(3) | [3] | - |
| insert(5) | [5, 3] | - |
| insert(1) | [5, 3, 1] | - |
| insert(7) | [7, 5, 1, 3] | - |
| extractMax() | [5, 3, 1] | 7 |
| extractMax() | [3, 1] | 5 |
| heapify([1,5,3,7,2]) | [7, 5, 3, 1, 2] | - |

### Constraints
- 1 <= number of operations <= 10^5
- -10^9 <= element value <= 10^9

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (sorted array) | Array + sort | Keep array sorted descending; extractMax = pop front. Simple but O(n) insert. |
| Optimal (binary max-heap) | Array-backed complete binary tree | Sift-up/sift-down give O(log n) insert and extract. |
| Best (Floyd's heapify) | Array-backed complete binary tree | Batch build in O(n) using bottom-up siftDown. |

**Pattern cue:** Whenever you need repeated access to the maximum element with dynamic insertions, think **max-heap / priority queue**.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Sorted Array Descending)
**Intuition:** Keep array sorted in descending order. ExtractMax pops index 0.

**Steps:**
1. On `insert(val)`: binary-search for position, shift elements, place val.
2. On `extractMax()`: remove and return `arr[0]`.
3. On `heapify(arr)`: sort descending.

| Metric | Value |
|--------|-------|
| Time -- insert | O(n) |
| Time -- extractMax | O(n) |
| Time -- heapify | O(n log n) |
| Space | O(n) |

### BUD Transition
**Bottleneck:** Shifting on insert/extract is O(n). We only need the max, not full order. A max-heap maintains parent >= children for O(log n) operations.

### Approach 2 -- Optimal (Binary Max-Heap)
**Intuition:** Store in array as complete binary tree. After insert, bubble up. After extractMax, move last to root and bubble down.

**Steps:**
1. **insert(val):** Append to end. SiftUp: while val > parent, swap.
2. **extractMax():** Save root. Move last element to root. SiftDown: while current < larger child, swap.
3. **heapify(arr):** From `n/2-1` down to 0, siftDown.

| Metric | Value |
|--------|-------|
| Time -- insert | O(log n) |
| Time -- extractMax | O(log n) |
| Time -- heapify | O(n) |
| Space | O(n) |

### Approach 3 -- Best (Same as Optimal)
Binary max-heap with Floyd's heapify is already optimal. O(log n) insert/extract cannot be beaten in comparison-based models.

| Metric | Value |
|--------|-------|
| Time -- insert | O(log n) |
| Time -- extractMax | O(log n) |
| Time -- heapify | O(n) |
| Space | O(n) |

---

## 4. COMPLEXITY INTUITIVELY
- **O(log n):** Tree has log n levels; each sift moves one level per step.
- **O(n) heapify:** Bottom-heavy distribution -- n/2 leaves sift 0, n/4 sift 1, etc. Sum converges to O(n).

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| extractMax on empty | Return -1 or throw |
| Single element | Works trivially |
| All identical values | Heap valid; sifts terminate immediately |
| Negative numbers | Comparisons work the same |

**Common mistakes:**
- Using min-heap comparisons (< instead of >) -- flip all comparisons for max-heap.
- Off-by-one in child indices (0-indexed: `2*i+1`, `2*i+2`).
- Forgetting to check right child existence in siftDown.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Min-heap variant? | Flip all comparisons. |
| In-place heapify? | Yes, works on the input array directly. |
| Java built-in? | `PriorityQueue` is min-heap; use `Collections.reverseOrder()` for max-heap. |
| Python built-in? | `heapq` is min-heap; negate values for max-heap. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Min Heap | Same structure, flipped comparisons |
| Kth Largest Element | Use min-heap of size k, or max-heap with k extractions |
| Heap Sort | Build max-heap, repeatedly extractMax |
| Find Median | Max-heap for lower half, min-heap for upper half |

---

## Real-World Use Case
**Video streaming bitrate selection:** Netflix's adaptive streaming algorithm maintains a max-heap of available video quality levels. At each buffer check, it extracts the maximum bitrate the bandwidth can support. The max-heap ensures the highest possible quality is always selected in O(log N) time.
