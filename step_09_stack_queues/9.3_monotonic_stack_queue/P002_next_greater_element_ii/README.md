# Next Greater Element II

> **Batch 4 of 12** | **Topic:** Monotonic Stack / Queue | **Difficulty:** MEDIUM | **XP:** 25

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
**(LeetCode #503)** Given a **circular** integer array `nums`, find the next greater element for every element. The next greater element of `nums[i]` is the first element greater than `nums[i]` traversing the array in circular order (i.e., after the end, wrap around to the beginning). If no such element exists, return -1.

Return an array of the same length where `result[i]` is the next greater element of `nums[i]`.

### Examples

| # | nums | Output | Explanation |
|---|------|--------|-------------|
| 1 | [1,2,1] | [2,-1,2] | 1->2, 2->-1 (nothing bigger), 1->2 (wraps around) |
| 2 | [1,2,3,4,3] | [2,3,4,-1,4] | 4 is the max, so -1. Last 3 wraps to find 4 |
| 3 | [5,4,3,2,1] | [-1,5,5,5,5] | 5 is max -> -1. All others wrap to find 5 |
| 4 | [1,1,1] | [-1,-1,-1] | All equal, no strictly greater exists |
| 5 | [3] | [-1] | Single element, wraps to itself -> not greater |

### Real-Life Analogy
Imagine people sitting in a **circular table**. Each person looks to their left (clockwise) for the first person taller than them. Even if they pass the "starting point," they keep looking around the circle. The tallest person at the table will never find anyone taller -- they get -1.

### Three Key Observations (the "Aha!" Moments)
1. **Circularity = double the traversal** -- To handle wrapping, conceptually duplicate the array: process indices `0` to `2n-1`, using `i % n` to map back to the original array. This simulates going around the circle exactly once more.
2. **Monotonic decreasing stack still works** -- Same as NGE I, but over 2n virtual elements. The stack stores indices (not values) to correctly assign results.
3. **No need to actually double the array** -- Just iterate `2n` times and use `i % n` to index into the original array. Space remains O(n).

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | For each element, scan the next n-1 elements circularly | Nested loop with modulo |
| Optimal | Monotonic stack over 2n indices (left-to-right) | Stack of indices |
| Best | Monotonic stack over 2n indices (right-to-left) | Stack of values |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Circular Linear Scan

**Intuition:** For each index `i`, scan the next `n-1` elements using `(i+1) % n`, `(i+2) % n`, etc. Return the first one greater than `nums[i]`.

**BUD Analysis:**
- **B**ottleneck: O(n) scan per element, O(n) elements -> O(n^2).
- **U**nnecessary work: Re-scanning overlapping suffixes of the circular array.
- **D**uplicate work: Element at index `j` might be scanned as a candidate for multiple `i` values.

**Steps:**
1. Initialize `result` array of size n, filled with -1.
2. For each `i` from 0 to n-1:
   - For `j` from 1 to n-1:
     - If `nums[(i+j) % n] > nums[i]`, set `result[i] = nums[(i+j) % n]` and break.

**Dry-Run Trace** (nums=[1,2,1]):
```
i=0 (val=1): scan j=1: nums[1]=2 > 1 -> result[0]=2. Break.
i=1 (val=2): scan j=1: nums[2]=1. j=2: nums[0]=1. No greater. result[1]=-1.
i=2 (val=1): scan j=1: nums[0]=1. j=2: nums[1]=2 > 1 -> result[2]=2. Break.
Result: [2, -1, 2]
```

| Metric | Value |
|--------|-------|
| Time | O(n^2) |
| Space | O(n) for result |

---

### Approach 2: Optimal -- Monotonic Stack Left-to-Right, 2n Traversal

**Intuition:** Traverse from index 0 to 2n-1. The stack stores indices of elements waiting for their NGE. When `nums[i % n]` is greater than `nums[stack.top()]`, pop and record the answer. Only assign results for indices in the first pass (i < n).

**BUD Analysis:**
- Each index is pushed and popped at most once across the 2n iterations -> O(n) amortized.
- Result array is filled during pops.

**Steps:**
1. Initialize `result` array of size n, filled with -1.
2. Initialize empty stack.
3. For `i` from 0 to 2n-1:
   - While stack is not empty AND `nums[i % n] > nums[stack.top()]`:
     - Pop `idx`. `result[idx] = nums[i % n]`.
   - If `i < n`, push `i` onto the stack (only push original indices).

**Dry-Run Trace** (nums=[1,2,1]):
```
n=3, iterate i=0..5

i=0 (val=1): stack=[], push 0.                 stack=[0]
i=1 (val=2): 2>nums[0]=1, pop 0, res[0]=2.    stack=[]
             push 1.                            stack=[1]
i=2 (val=1): 1<2, push 2.                      stack=[1,2]
i=3 (val=nums[0]=1): 1<2, 1<=1. No pop.        (i>=n, don't push)
i=4 (val=nums[1]=2): 2>nums[2]=1, pop 2, res[2]=2. stack=[1]
             2<=2. No more pops.                (i>=n, don't push)
i=5 (val=nums[2]=1): 1<2. No pop.              (i>=n, don't push)

Remaining: idx=1 stays -> res[1]=-1 (default)
Result: [2, -1, 2]
```

| Metric | Value |
|--------|-------|
| Time | O(n) amortized |
| Space | O(n) |

---

### Approach 3: Best -- Monotonic Stack Right-to-Left, 2n Traversal

**Intuition:** Traverse from `2n-1` down to 0. Maintain a stack of **values** (not indices). For each position, pop all values `<=` current (they are blocked). The stack top is the NGE. Then push the current value. Only record results when `i < n`.

This is more intuitive: at each step, the stack literally contains "the elements to my right (circularly) that could be my next greater element," in decreasing order.

**Steps:**
1. Initialize `result` array of size n, filled with -1.
2. Initialize empty stack.
3. For `i` from `2n-1` down to 0:
   - Pop all stack elements `<= nums[i % n]`.
   - If stack is not empty, `result[i % n] = stack.top()`.
   - Push `nums[i % n]`.
4. Return `result`.

**Dry-Run Trace** (nums=[1,2,1]):
```
n=3, iterate i=5..0

i=5 (idx=2, val=1): stack=[]. res[2] stays -1. push 1.   stack=[1]
i=4 (idx=1, val=2): pop 1 (1<=2). stack=[]. push 2.      stack=[2]
i=3 (idx=0, val=1): 2>1, keep. res[0]=2. push 1.         stack=[2,1]
i=2 (idx=2, val=1): pop 1 (1<=1). top=2>1. res[2]=2. push 1. stack=[2,1]
i=1 (idx=1, val=2): pop 1 (1<=2). pop 2 (2<=2). stack=[]. push 2. stack=[2]
                     res[1]=-1 (stack was empty after pops)
Wait -- let me redo: after popping 2 (2<=2), stack is empty, so res[1]=-1. push 2.
i=0 (idx=0, val=1): 2>1, keep. res[0]=2. push 1.         stack=[2,1]

Result: [2, -1, 2]
```

| Metric | Value |
|--------|-------|
| Time | O(n) amortized |
| Space | O(n) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) despite 2n iterations:** Each element is pushed at most twice (once in each "lap") and popped at most twice. Total stack operations = O(4n) = O(n).

**Why 2n is sufficient:** Going around the circle once more means every element gets a chance to "look back" at all elements. A third lap would be redundant because the maximum element has already been encountered.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| All equal [5,5,5] | [-1,-1,-1] | Need strictly greater, not >= |
| Single element [3] | [-1] | Wraps to itself, but 3 is not > 3 |
| Strictly decreasing [5,4,3,2,1] | [-1,5,5,5,5] | Everything wraps to find 5 except 5 itself |
| Two elements [1,2] | [2,-1] | Simple circular case |
| Maximum in middle [1,5,2] | [5,-1,5] | 2 wraps around to find 5 |

**Common Mistakes:**
- Only iterating n times instead of 2n (misses circular wrap).
- Pushing indices >= n into the stack (causes out-of-bounds or duplicate assignments).
- Using `<` instead of `<=` when popping (should pop equal elements too, since we need strictly greater).

---

## 6. INTERVIEW LENS

**Why interviewers ask this:** It is the natural follow-up to NGE I. Tests whether you can adapt the monotonic stack pattern to handle circularity -- a common real-world requirement (circular buffers, scheduling).

**Follow-ups to expect:**
- "What about next smaller element in a circular array?" -> Same approach with flipped comparison.
- "What if you need the index of the NGE, not the value?" -> Store indices in the stack instead of values.
- "Optimize for the case where array has many duplicates?" -> Still O(n), but discuss practical stack depth.

**Talking points:**
- The 2n trick is a general technique for circular array problems (also used in circular DP, string matching with doubled string).
- Compare with NGE I: the only difference is the circularity handling.

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Next Greater Element I (LC #496) | Non-circular version, subset query |
| Daily Temperatures (LC #739) | NGE variant: return distance instead of value |
| Largest Rectangle in Histogram | Monotonic stack, finds next smaller |
| Circular Array Loop (LC #457) | Another circular array + stack/pointer problem |
| Stock Span Problem | Monotonic stack counting consecutive smaller days |
