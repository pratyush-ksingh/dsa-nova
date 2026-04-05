# Convert Min Heap to Max Heap

> **Batch 2 of 12** | **Topic:** Heaps | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array representing a **min-heap**, convert it into a **max-heap** in-place. Use the standard bottom-up **build max-heap** algorithm (Floyd's algorithm).

**Constraints:**
- `1 <= n <= 10^5`
- `-10^9 <= arr[i] <= 10^9`
- Input is guaranteed to be a valid min-heap.

**Examples:**

| Input (Min-Heap) | Output (Max-Heap) | Explanation |
|-------------------|-------------------|-------------|
| `[1, 2, 3, 4, 5]` | `[5, 4, 3, 1, 2]` (or any valid max-heap) | Root becomes max element |
| `[1, 5, 6, 9, 10, 8]` | `[10, 9, 8, 1, 5, 6]` (or any valid max-heap) | Every parent >= children |

### Real-Life Analogy
> *Imagine a company where the lowest-paid employee is the CEO (min-heap). The board decides to restructure so the highest-paid employee becomes CEO (max-heap). Rather than firing everyone and rehiring, they reorganize from the middle managers down: each manager swaps with their highest-paid direct report if needed, and this "sift down" propagates until the hierarchy is correct. Starting from the bottom middle-managers and working up ensures each subtree is fixed before its parent is processed.*

### Key Observations
1. **Ignore the min-heap structure:** A min-heap to max-heap conversion is really just "build a max-heap from an arbitrary array." The input being a min-heap is irrelevant to the algorithm.
2. **Bottom-up heapify is O(n):** Start from the last internal node (`n/2 - 1`) and heapify-down each node. This is Floyd's algorithm and runs in O(n), not O(n log n).
3. **Heapify-down is the core operation:** For each node, swap it with the larger child and recurse downward until the max-heap property is restored. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Build-heap** is the textbook algorithm for constructing a heap from an unordered array.
- It works by fixing subtrees bottom-up, guaranteeing that when we heapify node `i`, both its children's subtrees are already valid max-heaps.
- O(n) time because nodes near the bottom (most nodes) do very little work, while only the root does O(log n) work.

### Pattern Recognition
- **Pattern:** Bottom-up heap construction (Floyd's algorithm)
- **Classification Cue:** "When you see _convert/build a heap from array_ --> think _heapify from last internal node to root_"

---

## APPROACH LADDER

### Approach 1: Naive -- Extract and Re-Insert
**Idea:** Extract all elements, insert them one by one into a max-heap.

**Steps:**
1. Copy all elements from the min-heap array.
2. Build a max-heap by inserting elements one at a time (each insertion is O(log n)).

| Time | Space |
|------|-------|
| O(n log n) | O(n) |

**BUD Transition:** We are doing unnecessary work. Build-heap is O(n) and in-place.

### Approach 2: Bottom-Up Build Max-Heap (Optimal)
**Idea:** Treat the array as unordered. Apply max-heapify-down starting from the last internal node up to the root.

**Steps:**
1. Start at index `i = n/2 - 1` (last node with children).
2. For each `i` down to 0, call `maxHeapify(arr, n, i)`.
3. `maxHeapify`: compare node with left and right children, swap with the largest, recurse on the swapped child.

**Dry Run:** `arr = [1, 2, 3, 4, 5]`, n=5, last internal = index 1

| Step | i | arr state | Action |
|------|---|-----------|--------|
| 1 | 1 | [1, **2**, 3, 4, **5**] | children: 4, 5. Max child = 5. Swap 2 and 5 |
| | | [1, **5**, 3, 4, **2**] | Recurse on index 4 (leaf, done) |
| 2 | 0 | [**1**, **5**, **3**, 4, 2] | children: 5, 3. Max child = 5. Swap 1 and 5 |
| | | [**5**, **1**, 3, 4, 2] | Recurse on index 1: children 4, 2. Max=4. Swap 1 and 4 |
| | | [5, **4**, 3, **1**, 2] | Recurse on index 3 (leaf, done) |

**Result:** `[5, 4, 3, 1, 2]` -- valid max-heap.

| Time | Space |
|------|-------|
| O(n) | O(log n) recursion stack |

**Why O(n) and not O(n log n)?** Nodes at depth `d` from bottom do at most `d` swaps. Sum = `n/4 * 1 + n/8 * 2 + n/16 * 3 + ...` = O(n).

---

## COMPLEXITY -- INTUITIVELY
**O(n) time:** "Half the nodes are leaves (0 work). A quarter do 1 swap. An eighth do 2 swaps. This geometric series sums to O(n)."
**O(log n) space:** "Recursion depth equals tree height. Iterative version uses O(1)."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Starting heapify from index 0 (top-down) instead of `n/2 - 1` (bottom-up). Top-down does not produce O(n).
2. Forgetting to bounds-check the right child before comparing.
3. Not recursing after a swap -- the swapped element may still violate the heap property further down.

### Edge Cases to Test
- [ ] Single element --> already a max-heap
- [ ] Two elements --> one swap at most
- [ ] All elements equal --> no swaps needed
- [ ] Already a max-heap --> no changes needed

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "In-place conversion? Any valid max-heap arrangement is acceptable?"
2. **Key Insight:** "Converting min-heap to max-heap is the same as build-heap on an arbitrary array."
3. **Complexity:** Explain why build-heap is O(n) using the geometric series argument.
4. **Code:** Write `maxHeapify` helper + loop from `n/2-1` down to 0.

### Follow-Up Questions
- "Can you do it iteratively?" --> Yes, replace recursion in heapify with a while loop.
- "What about max-heap to min-heap?" --> Same algorithm, just use min-heapify.
- "Why is build-heap O(n) but n insertions O(n log n)?" --> Insertions sift up (most work at leaves), build-heap sifts down (least work at leaves).

---

## CONNECTIONS
- **Prerequisite:** Check if Array is Heap (P004), Understanding heapify operation
- **Same Pattern:** Build Heap (Floyd's algorithm), Heap Sort
- **This Unlocks:** Heap Sort, Priority Queue operations
- **Related:** Kth Largest Element (uses heap construction)
