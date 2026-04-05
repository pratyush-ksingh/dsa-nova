# Check if Array is Heap

> **Batch 2 of 12** | **Topic:** Heaps | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array of integers, determine whether it represents a valid **min-heap** or **max-heap**. In a min-heap, every parent node is less than or equal to its children. In a max-heap, every parent is greater than or equal to its children. The array uses 0-based indexing where for node at index `i`, the left child is at `2*i + 1` and the right child is at `2*i + 2`.

**Constraints:**
- `1 <= n <= 10^5`
- `-10^9 <= arr[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[10, 20, 30, 25, 35]` | `Min-Heap` | Every parent <= both children |
| `[90, 70, 60, 50, 40]` | `Max-Heap` | Every parent >= both children |
| `[10, 50, 20, 55, 5]` | `Neither` | arr[1]=50 > arr[4]=5 violates min; arr[0]=10 < arr[1]=50 violates max |

### Real-Life Analogy
> *Think of a company org chart stored in an array. In a min-heap, every manager has a lower rank number than their direct reports -- the CEO (rank 1) is at the top. To verify the org chart is valid, you just walk through the managers and check that no manager has a higher rank number than their reports. You only need to check internal nodes since leaf nodes have no one reporting to them.*

### Key Observations
1. **Only check internal nodes:** Leaf nodes (indices `n/2` to `n-1`) have no children, so they trivially satisfy the heap property.
2. **Parent-child index math:** For 0-based indexing, parent at `i` has children at `2i+1` and `2i+2`. This is the core formula for array-based heaps.
3. **One pass suffices:** You just iterate through indices 0 to `n/2 - 1` and verify the heap property for each parent. If any parent violates it, you have your answer. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- The heap is stored as an **array** (implicit complete binary tree). No tree construction needed.
- The parent-child relationship is purely **index arithmetic** -- `2*i+1` and `2*i+2`.
- A single linear scan over parent nodes checks the entire structure.

### Pattern Recognition
- **Pattern:** Array traversal with index-based tree navigation
- **Classification Cue:** "When you see _verify a property of an array-based tree_ --> think _linear scan over parent indices_"

---

## APPROACH LADDER

### Approach 1: Check All Parent-Child Pairs
**Idea:** Iterate through all non-leaf nodes. For each, verify that the heap property holds with both children.

**Steps:**
1. For `i = 0` to `n/2 - 1` (all internal nodes):
   - Compute `left = 2*i + 1`, `right = 2*i + 2`.
   - For **min-heap check**: verify `arr[i] <= arr[left]` and (if right exists) `arr[i] <= arr[right]`.
   - For **max-heap check**: verify `arr[i] >= arr[left]` and (if right exists) `arr[i] >= arr[right]`.
2. If all pass for min-heap, return "Min-Heap". If all pass for max-heap, return "Max-Heap". Otherwise "Neither".

**Dry Run:** `arr = [10, 20, 30, 25, 35]`, n=5, check indices 0..1

| i | arr[i] | left (2i+1) | right (2i+2) | arr[i] <= children? | arr[i] >= children? |
|---|--------|-------------|--------------|---------------------|---------------------|
| 0 | 10 | 20 | 30 | 10<=20, 10<=30: YES | 10>=20: NO |
| 1 | 20 | 25 | 35 | 20<=25, 20<=35: YES | 20>=25: NO |

Min-heap: all YES. Result: **Min-Heap**.

| Time | Space |
|------|-------|
| O(n) | O(1) |

This problem is inherently O(n) -- you must look at every parent-child pair at least once. There is no better asymptotic complexity.

---

## COMPLEXITY -- INTUITIVELY
**O(n) time:** "We visit each of the ~n/2 internal nodes once and do O(1) work per node."
**O(1) space:** "We only use a few variables -- no extra data structures."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to bounds-check the **right child** -- the last internal node may only have a left child.
2. Using 1-based indexing formulas (`2i`, `2i+1`) with a 0-based array.
3. Only checking min-heap and returning false, forgetting to also check max-heap (or vice versa).

### Edge Cases to Test
- [ ] Single element array --> always a valid heap
- [ ] Two elements --> only one parent-child pair to check
- [ ] All elements equal --> both min-heap and max-heap
- [ ] Sorted ascending --> min-heap
- [ ] Sorted descending --> max-heap

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "0-based or 1-based indexing? Should I check min-heap, max-heap, or both? What to return if it is both?"
2. **Key Insight:** "Only need to check internal nodes (first n/2 elements). Leaves have no children."
3. **Code:** Single loop, clean index arithmetic.

### Follow-Up Questions
- "What if the array is 1-indexed?" --> Children at `2i` and `2i+1`, parent at `i/2`.
- "How would you fix a broken heap?" --> Run heapify on the violating node (leads to build-heap).
- "Can you check if it is a valid BST stored in array?" --> Different property (in-order must be sorted).

---

## CONNECTIONS
- **Prerequisite:** Understanding heap data structure, array-based binary tree representation
- **Same Pattern:** Build Heap (uses same parent-child index math)
- **This Unlocks:** Convert Min Heap to Max Heap (P005), Heap Sort
- **Related:** Validate BST (different property, same tree-in-array idea)
