# Kth Largest Element in an Array

> **Batch 3 of 12** | **Topic:** Heaps | **Difficulty:** MEDIUM | **XP:** 25

## UNDERSTAND

### Problem Statement
Given an integer array `nums` and an integer `k`, return the **kth largest** element in the array. Note: it is the kth largest in sorted order, not the kth distinct element. *(LeetCode #215)*

### Examples

| Input | k | Output | Explanation |
|-------|---|--------|-------------|
| `[3,2,1,5,6,4]` | 2 | `5` | Sorted: [6,5,4,3,2,1]. 2nd largest = 5 |
| `[3,2,3,1,2,4,5,5,6]` | 4 | `4` | Sorted: [6,5,5,4,3,3,2,2,1]. 4th = 4 |
| `[1]` | 1 | `1` | Only element |
| `[7,7,7,7]` | 2 | `7` | Duplicates count as separate positions |

### Analogy
You're a talent scout watching a race with 1000 runners. You need the 3rd fastest runner. You could time everyone and sort (expensive). Or you could keep a "top 3" leaderboard: as each runner finishes, if their time beats anyone in the top 3, swap them in. At the end, the slowest in your top-3 leaderboard is the 3rd fastest. That leaderboard is a min-heap of size k.

### 3 Key Observations
1. **"Aha" -- Min-heap of size k:** Maintain a min-heap with k elements. The top of the heap (minimum) is always the kth largest seen so far. If a new element is larger than the heap top, swap it in.
2. **"Aha" -- Quickselect avoids full sorting:** You don't need to sort the entire array. You only need to find the element that would be at position `n-k` in sorted order. Quickselect partitions around a pivot, then recurses only into the relevant half.
3. **"Aha" -- kth largest = (n-k)th smallest:** Converting between "kth largest" and index position in a sorted array: the kth largest is at index `n-k` (0-indexed). This simplifies the Quickselect target.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Array | Sort, return `arr[n-k]` | Simple but O(n log n) |
| Optimal | Min-Heap (size k) | Maintain k largest elements | O(n log k) time |
| Best | Array (in-place) | Quickselect with random pivot | O(n) average |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Sort
**Intuition:** Sort the array in descending order and return the element at index `k-1` (or sort ascending and return at index `n-k`).

**Steps:**
1. Sort the array.
2. Return `nums[nums.length - k]`.

**Dry-run trace** with `[3,2,1,5,6,4]`, k=2:
```
Sort: [1,2,3,4,5,6]
Index n-k = 6-2 = 4. nums[4] = 5.
Return 5.
```

| Metric | Value |
|--------|-------|
| Time | O(n log n) |
| Space | O(1) to O(n) depending on sort algorithm |

---

### Approach 2: Optimal -- Min-Heap of Size k
**Intuition:** A min-heap of size k naturally keeps the k largest elements. The heap's root (minimum of those k elements) is the kth largest overall. Process each element: if the heap has fewer than k elements, add it. Otherwise, if the new element exceeds the heap's root, remove the root and add the new element.

**BUD Optimization:**
- **B**ottleneck: Sorting processes all n log n comparisons. With a heap, we only do n log k operations.
- When k is much smaller than n (e.g., k=10, n=1,000,000), this is a massive improvement.

**Steps:**
1. Create a min-heap.
2. For each element in nums:
   - If heap size < k, add it.
   - Else if element > heap's min, remove min and add element.
3. Return heap's min (that's the kth largest).

**Dry-run trace** with `[3,2,1,5,6,4]`, k=2:
```
Process 3: heap=[3] (size<2, add)
Process 2: heap=[2,3] (size==2 now)
Process 1: 1 < heap.min=2, skip. heap=[2,3]
Process 5: 5 > 2, remove 2, add 5. heap=[3,5]
Process 6: 6 > 3, remove 3, add 6. heap=[5,6]
Process 4: 4 < 5, skip. heap=[5,6]
Return heap.min = 5.
```

| Metric | Value |
|--------|-------|
| Time | O(n log k) -- n elements, each heap op is O(log k) |
| Space | O(k) -- heap size |

---

### Approach 3: Best -- Quickselect (Randomized)
**Intuition:** Based on quicksort's partition. Choose a random pivot, partition the array such that all elements >= pivot are on one side. If the pivot lands at position `n-k`, we're done. Otherwise, recurse into the correct half. Average case is O(n) because each recursive call processes half the remaining elements (n + n/2 + n/4 + ... = 2n).

**Steps:**
1. Target index = `n - k`.
2. Pick a random pivot from the current range.
3. Partition: move elements >= pivot to the left, < pivot to the right (or standard Lomuto/Hoare partition).
4. If pivot's final position == target, return it.
5. If target < pivot position, recurse left.
6. If target > pivot position, recurse right.

**Dry-run trace** with `[3,2,1,5,6,4]`, k=2 (target index=4):
```
Random pivot: 4 (index 5). Partition around 4:
  [5,6,4,3,2,1] -> pivot at index 2 (elements >= 4: [5,6,4], elements < 4: [3,2,1])
  Pivot position = 2. Target = 4. Target > 2, recurse right: [3,2,1].

Wait, target in right half means we want index 4 - 3 = 1 in [3,2,1].
Random pivot: 2. Partition: [3,2,1] -> [3,2,1], pivot at index 1.
Target = 1. pivot position = 1. Found! Return 2?

(Note: exact positions depend on partition implementation; the key idea is we narrow in on the target index.)

Actually with 0-indexed Lomuto on full array targeting index 4:
After first partition with pivot=4: ..., pivotIndex=3. Target=4 > 3, search right.
After partition in right half with pivot=5: pivotIndex=4. Target=4. Found! Return nums[4]=5.
```

| Metric | Value |
|--------|-------|
| Time | O(n) average, O(n^2) worst (mitigated by random pivot) |
| Space | O(1) iterative, O(log n) recursive |

---

## COMPLEXITY INTUITIVELY

**Why Quickselect is O(n) on average:** First call processes n elements, second call processes ~n/2, third ~n/4, etc. Total: n + n/2 + n/4 + ... = 2n = O(n). Random pivot ensures balanced partitions with high probability.

**Why min-heap of size k gives O(n log k):** We process all n elements, but each heap operation (insert/remove) takes O(log k), not O(log n). When k << n, this is much better than sorting.

**When to prefer which:**
- k is very small relative to n: Heap approach is practical and simple.
- Need guaranteed O(n) average: Quickselect.
- Need guaranteed O(n log n) worst: Sort (or introselect).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| k = 1 | Maximum element | Just find max in O(n) |
| k = n | Minimum element | Just find min in O(n) |
| All same elements | That element | Quickselect still works |
| Array of size 1 | Only element | k must be 1 |
| Duplicates | Include duplicates in ranking | "kth largest" counts dupes |

### Common Mistakes
- Using a MAX-heap instead of MIN-heap (max-heap requires adding all elements = O(n log n), same as sorting).
- Off-by-one: confusing "kth largest" with "kth index." kth largest = index `n-k` in sorted array.
- Quickselect: not randomizing the pivot (sorted input gives O(n^2) with fixed pivot).
- Quickselect: modifying the array when the problem expects it unchanged.

---

## INTERVIEW LENS

**Frequency:** Very high -- top 10 most-asked heap/sorting problem.
**Follow-ups the interviewer might ask:**
- "What if the array is a stream?" (Use min-heap of size k, online)
- "What if k changes?" (Augmented BST or order-statistic tree)
- "Can you guarantee O(n)?" (Median of medians pivot selection, but complex)
- "What about kth smallest?" (Same techniques, flip the comparison)

**What they're really testing:** Do you know the heap vs quickselect tradeoffs? Can you implement a clean partition?

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Top K Frequent Elements (LC #347) | Heap of size k, but on frequencies |
| Find Median from Data Stream (LC #295) | Two heaps (max + min) for streaming median |
| Sort Colors / Dutch National Flag | Partition subroutine is shared with Quickselect |
| K Closest Points to Origin (LC #973) | Same min-heap-of-size-k pattern |

### Real-World Use Case
**Monitoring systems / alerting:** Systems like Prometheus or Datadog track the "top-k most expensive queries" or "kth percentile response time" in real-time across millions of requests. They use min-heaps of size k to efficiently maintain a running top-k without sorting the entire dataset. This same pattern powers "trending topics" features in social media.
