# Kth Smallest Element

> **Batch 4 of 12** | **Topic:** Heaps | **Difficulty:** Medium | **XP:** 25

## UNDERSTAND

### Problem Statement
Given an unsorted array of integers and a value `k`, find the **kth smallest element** in the array. The array may contain duplicates.

### Examples

**Example 1:**
```
arr = [7, 10, 4, 3, 20, 15], k = 3
Sorted: [3, 4, 7, 10, 15, 20]
3rd smallest = 7
```

**Example 2:**
```
arr = [7, 10, 4, 20, 15], k = 4
Sorted: [4, 7, 10, 15, 20]
4th smallest = 15
```

**Example 3:**
```
arr = [1], k = 1
1st smallest = 1
```

### Real-Life Analogy
A teacher has a class of students with different test scores. She wants to find the student with the 3rd lowest score to offer tutoring. She could sort all scores (expensive for a large class), keep a small "worst performers" list as she goes, or use a clever partitioning scheme.

### 3 Key Observations
1. **"Aha!" -- Max-heap of size k keeps k smallest:** Maintain a max-heap of size k. When a new element is smaller than the heap's max, swap it in. The heap's root is always the kth smallest.
2. **"Aha!" -- Quickselect gives average O(N):** Partition around a pivot. If pivot lands at position k-1, we are done. Otherwise, recurse on the correct half. No need to fully sort.
3. **"Aha!" -- Sorting is overkill:** We only need ONE element's position, but sorting determines ALL positions. Heap and Quickselect avoid this wasted work.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Array | Sort + index | Simple, guaranteed correct |
| Optimal | Max-Heap (size k) | Stream through array | O(N log k), good when k << N |
| Best | Array (in-place) | Quickselect (partition) | O(N) average, in-place |

---

## APPROACH LADDER

### Brute Force: Sort the Array
**Intuition:** Sort the array. The kth smallest is at index k-1.

**Steps:**
1. Sort the array in ascending order
2. Return arr[k-1]

**BUD Analysis:**
- **B**ottleneck: Sorting is O(N log N) -- we sort everything but only need one element
- **U**nnecessary: Ordering elements we don't care about
- **D**uplicate: None

**Dry-Run Trace (arr=[7,10,4,3,20,15], k=3):**
```
Sort: [3, 4, 7, 10, 15, 20]
Return arr[2] = 7
```

### Optimal: Max-Heap of Size k
**Intuition:** Maintain a max-heap of the k smallest elements seen so far. The root of this heap is the kth smallest. For each new element, if it is smaller than the root, remove the root and insert the new element.

**Steps:**
1. Insert first k elements into a max-heap
2. For remaining elements: if element < heap root, remove root, insert element
3. After processing all elements, heap root = kth smallest

**Dry-Run Trace (arr=[7,10,4,3,20,15], k=3):**
```
Insert 7,10,4 -> MaxHeap: [10, 7, 4] (root=10)
Process 3: 3 < 10, remove 10, insert 3 -> [7, 3, 4] (root=7)
Process 20: 20 > 7, skip
Process 15: 15 > 7, skip
Root = 7 = 3rd smallest
```

### Best: Quickselect (Lomuto/Hoare Partition)
**Intuition:** Partition the array around a pivot. If the pivot ends up at index k-1, it is the answer. If pivot is too far right, recurse on the left part. If too far left, recurse on the right part.

**Steps:**
1. Choose pivot (last element or random)
2. Partition: elements < pivot on left, elements >= pivot on right
3. If pivot position == k-1: return pivot
4. If pivot position > k-1: recurse on left portion
5. If pivot position < k-1: recurse on right portion

**Dry-Run Trace (arr=[7,10,4,3,20,15], k=3):**
```
Pivot=15, partition: [7,10,4,3] | 15 | [20]
Pivot at index 4. k-1=2 < 4, search left: [7,10,4,3]
Pivot=3, partition: [] | 3 | [7,10,4]
Pivot at index 0. k-1=2 > 0, search right: [7,10,4]
Pivot=4, partition: [] | 4 | [7,10]
Pivot at index 1 (relative). Adjusted position=2. k-1=2, FOUND! Return 7.
```

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why? |
|----------|------|-------|------|
| Brute | O(N log N) | O(1) or O(N) | Sorting dominates |
| Optimal | O(N log k) | O(k) | N elements, each heap op is O(log k) |
| Best | O(N) avg, O(N^2) worst | O(1) | Quickselect: each partition reduces search space by ~half on average |

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky? |
|-----------|----------|-------------|
| k = 1 | Minimum element | Simplest case |
| k = n | Maximum element | Must process all |
| All elements same | That element | Heap and Quickselect still work |
| k > n | Invalid input | Handle gracefully |
| Array with negatives | Works normally | No special handling needed |
| Single element, k=1 | That element | Trivial case |

**Common Mistakes:**
- Off-by-one: kth smallest is at index k-1, not k
- In Quickselect, not handling the pivot position correctly after recursion
- Using min-heap instead of max-heap (min-heap would need to be size n-k+1)
- Not randomizing pivot in Quickselect (worst case on sorted input)

---

## INTERVIEW LENS

**Why interviewers ask this:** Classic problem testing knowledge of heaps, partitioning, and time complexity tradeoffs.

**What they want to see:**
- Knowledge of all three approaches and when each is best
- Heap approach for streaming data / when k is small
- Quickselect for average O(N) in-place

**Follow-ups to prepare for:**
- Kth Largest Element (use min-heap of size k, or Quickselect for n-k+1)
- Find Median (kth smallest where k = n/2)
- Top K Frequent Elements

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Kth Largest Element | Mirror: use min-heap or find (n-k+1)th smallest |
| Find Median | Special case: k = n/2 |
| Top K Frequent | Heap selection on frequency counts |
| Sort an Almost Sorted Array | Heap-based partial sorting |
