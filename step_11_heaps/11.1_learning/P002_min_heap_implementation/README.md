# Min Heap Implementation

> **Batch 3 of 12** | **Topic:** Heaps | **Difficulty:** MEDIUM | **XP:** 25

## UNDERSTAND

### Problem Statement
Implement a **Min Heap** from scratch using an array. Support the following operations:
- `insert(val)` -- Insert a new element.
- `extractMin()` -- Remove and return the minimum element.
- `getMin()` -- Return the minimum without removing.
- `decreaseKey(i, newVal)` -- Decrease the value at index `i` to `newVal`.
- `delete(i)` -- Delete the element at index `i`.
- `heapify(arr)` -- Build a heap from an unsorted array.

### Examples

| Operation | Heap State | Return | Explanation |
|-----------|-----------|--------|-------------|
| `insert(10)` | `[10]` | - | First element |
| `insert(5)` | `[5,10]` | - | 5 bubbles up to root |
| `insert(15)` | `[5,10,15]` | - | 15 stays as child |
| `insert(3)` | `[3,5,15,10]` | - | 3 bubbles up through 10, then 5 |
| `getMin()` | `[3,5,15,10]` | `3` | Root is always minimum |
| `extractMin()` | `[5,10,15]` | `3` | Remove 3, move 10 to root, bubble down |
| `decreaseKey(2, 1)` | `[1,10,5]` | - | Change 15->1, bubble up to root |

### Analogy
Think of a priority queue at a hospital ER. The most critical patient (lowest severity number) is always at the front. When a new patient arrives, they're initially placed at the end and "bubble up" if their condition is more critical. When the most critical patient is treated (extracted), the last patient in line moves to the front and "bubbles down" to find their correct priority position.

### 3 Key Observations
1. **"Aha" -- Array as a complete binary tree:** Index `i`'s left child is at `2i+1`, right child at `2i+2`, parent at `(i-1)/2`. No pointers needed -- the array IS the tree.
2. **"Aha" -- Two fundamental operations: bubble-up and bubble-down:** Every heap operation is built from these two primitives. Insert uses bubble-up. ExtractMin uses bubble-down. DecreaseKey uses bubble-up. Delete uses both (decreaseKey to -infinity, then extractMin).
3. **"Aha" -- Build heap is O(n), not O(n log n):** Calling `insert` n times is O(n log n). But calling `bubbleDown` from the last internal node to the root is O(n). This is because most nodes are near the bottom and bubble down very little.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Sorted array | Insert: find position, shift. Extract: remove first | Simple but O(n) insert |
| Optimal | Unsorted array + heap property | Bubble-up / bubble-down | O(log n) operations |
| Best | Same as optimal + O(n) heapify | Bottom-up heap construction | Optimal build time |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Sorted Array
**Intuition:** Keep the array always sorted. The minimum is always at index 0. Insertion requires finding the correct position (binary search) and shifting elements.

**Steps:**
- `insert`: Binary search for position, shift elements right, insert. O(n) due to shifting.
- `extractMin`: Remove first element, shift left. O(n).
- `getMin`: Return `arr[0]`. O(1).

| Metric | Value |
|--------|-------|
| Insert | O(n) -- shifting |
| ExtractMin | O(n) -- shifting |
| GetMin | O(1) |
| Space | O(n) |

---

### Approach 2: Optimal -- Array-Based Min Heap
**Intuition:** Maintain the heap property: every parent is smaller than or equal to its children. When the property is violated, restore it using bubble-up (for insertions / key decreases) or bubble-down (for extractions).

**BUD Optimization:**
- **B**ottleneck: Sorted array's O(n) insert. Heap gives O(log n) for both insert and extract.
- **Key insight:** We only need partial order (heap property), not full sort.

**Steps for each operation:**

**Insert(val):**
1. Add `val` to end of array.
2. Bubble-up: compare with parent, swap if smaller, repeat until heap property holds.

**ExtractMin():**
1. Save root value (minimum).
2. Move last element to root.
3. Remove last element (shrink array).
4. Bubble-down root: compare with children, swap with smaller child, repeat.

**DecreaseKey(i, newVal):**
1. Set `arr[i] = newVal`.
2. Bubble-up from index `i`.

**Delete(i):**
1. `decreaseKey(i, -infinity)` -- bubbles to root.
2. `extractMin()` -- removes root.

**Heapify(arr):** See Approach 3.

**Dry-run: Insert sequence [10, 5, 15, 3]:**
```
Insert 10: arr=[10]
Insert 5:  arr=[10,5] -> bubble up: 5<10, swap -> arr=[5,10]
Insert 15: arr=[5,10,15] -> 15>5, no swap
Insert 3:  arr=[5,10,15,3] -> parent of 3 is 10 (index 1). 3<10, swap: [5,3,15,10]
           -> parent of 3 is 5 (index 0). 3<5, swap: [3,5,15,10]
```

**Dry-run: ExtractMin from [3,5,15,10]:**
```
Save min=3. Move last (10) to root: [10,5,15]
Bubble down 10: children are 5,15. Min child=5. 10>5, swap: [5,10,15]
Bubble down 10: no children. Done.
Return 3. Heap: [5,10,15]
```

| Metric | Value |
|--------|-------|
| Insert | O(log n) |
| ExtractMin | O(log n) |
| GetMin | O(1) |
| DecreaseKey | O(log n) |
| Delete | O(log n) |
| Space | O(n) |

---

### Approach 3: Best -- O(n) Bottom-Up Heapify
**Intuition:** Building a heap from an unsorted array. Instead of inserting elements one by one (O(n log n)), start from the last internal node and bubble-down each node. Leaf nodes (bottom half of the array) already satisfy the heap property trivially.

**Steps:**
1. Start from index `n/2 - 1` (last internal node) down to 0.
2. For each index, call bubble-down.

**Why O(n):** Nodes at height h do O(h) work. There are `n / 2^(h+1)` nodes at height h. Total work = sum over h from 0 to log(n) of `(n/2^(h+1)) * h` = O(n).

**Dry-run: Heapify [10, 5, 15, 3, 8]:**
```
n=5. Last internal node = index 1 (5).
i=1: node=5, children=[3,8]. Min=3. 5>3, swap: [10,3,15,5,8]
i=0: node=10, children=[3,15]. Min=3. 10>3, swap: [3,10,15,5,8]
  Continue: node=10 at index 1, children=[5,8]. Min=5. 10>5, swap: [3,5,15,10,8]
Final: [3,5,15,10,8] -- valid min heap!
```

| Metric | Value |
|--------|-------|
| Heapify | O(n) |
| All other ops | Same as Approach 2 |

---

## COMPLEXITY INTUITIVELY

**Why insert/extract are O(log n):** The heap is a complete binary tree with height log n. Bubble-up and bubble-down each traverse at most one root-to-leaf path, doing O(1) work per level.

**Why heapify is O(n), not O(n log n):** Most nodes are at the bottom of the tree and don't need to move far. Formally: half the nodes are leaves (0 work), a quarter move at most 1 level, an eighth move at most 2 levels, etc. The series sums to O(n).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Extract from empty heap | Error/exception | Must check size first |
| Insert into empty heap | Single element, it's the root | No bubble-up needed |
| All same elements | Valid heap, any order | Bubble-up/down stop immediately |
| Decrease key to less than parent | Must bubble up to maintain property |
| Delete root | Same as extractMin | Index 0 |
| Single element heap | Extract returns it, heap becomes empty |

### Common Mistakes
- Off-by-one in parent/child index formulas (parent is `(i-1)/2`, NOT `i/2`).
- Bubble-down: comparing with only one child when both exist (must pick the SMALLER child).
- Bubble-down: forgetting to check if children exist (leaf node).
- Heapify: starting from index `n-1` instead of `n/2 - 1` (wasting time on leaves).
- Delete: forgetting to handle the case where the element moves DOWN after being replaced.

---

## INTERVIEW LENS

**Frequency:** High -- fundamental data structure implementation.
**Follow-ups the interviewer might ask:**
- "How is this different from a max-heap?" (Flip comparisons)
- "How does Java's PriorityQueue work internally?" (Array-based binary heap, exactly this)
- "What about a d-ary heap?" (Each node has d children instead of 2; better for decrease-key-heavy workloads)
- "Fibonacci heap?" (O(1) amortized decrease-key; used in Dijkstra's)

**What they're really testing:** Can you implement a core data structure from scratch? Do you understand the array-to-tree mapping and heap invariant maintenance?

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Kth Largest Element (LC #215) | Uses a heap as a black box |
| Merge K Sorted Lists (LC #23) | Min-heap to efficiently select the next smallest |
| Dijkstra's Algorithm | Relies on decrease-key operation |
| Median from Data Stream (LC #295) | Two heaps (max-heap + min-heap) |
| Heap Sort | Uses heapify + repeated extractMax |

### Real-World Use Case
**Task scheduling in operating systems:** The OS scheduler uses a priority queue (min-heap by priority/deadline) to decide which process runs next. When a process's priority changes (nice value), `decreaseKey` is called. When a process completes, `extractMin` removes it. Linux's CFS (Completely Fair Scheduler) uses a red-black tree, but simpler embedded systems use binary heaps for their O(log n) guaranteed scheduling decisions.
