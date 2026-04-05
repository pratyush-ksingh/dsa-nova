# Next Greater Element I

> **Batch 4 of 12** | **Topic:** Monotonic Stack / Queue | **Difficulty:** MEDIUM | **XP:** 25

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
**(LeetCode #496)** You are given two distinct integer arrays `nums1` and `nums2`, where `nums1` is a subset of `nums2`. For each element in `nums1`, find the **next greater element** in `nums2`. The next greater element of `nums1[i]` is the first element to the **right** of `nums1[i]` in `nums2` that is larger than `nums1[i]`. If no such element exists, return -1 for that position.

Return an array `ans` of the same length as `nums1` where `ans[i]` is the next greater element of `nums1[i]` in `nums2`.

### Examples

| # | nums1 | nums2 | Output | Explanation |
|---|-------|-------|--------|-------------|
| 1 | [4,1,2] | [1,3,4,2] | [-1,3,-1] | 4: no greater to its right in nums2 -> -1. 1: next greater is 3. 2: no greater -> -1 |
| 2 | [2,4] | [1,2,3,4] | [3,-1] | 2: next greater is 3. 4: nothing greater -> -1 |
| 3 | [1] | [1] | [-1] | Only element, nothing to its right |
| 4 | [1,3,5] | [6,5,4,3,2,1] | [-1,-1,-1] | Decreasing array, nothing is greater |

### Real-Life Analogy
Imagine standing in a crowd and looking to your right for the first person **taller** than you. If everyone to your right is shorter, you report "nobody." A **monotonic stack** acts like a "waiting line" of people who have not yet found anyone taller. When a tall person arrives, all shorter people in the waiting line get their answer at once.

### Three Key Observations (the "Aha!" Moments)
1. **Brute force is O(m * n)** -- For each element in nums1, scan right in nums2. This works but is slow.
2. **Monotonic decreasing stack processes nums2 in O(n)** -- Traverse nums2 from right to left (or left to right) maintaining a stack of elements in decreasing order. For each element, pop smaller ones (they are irrelevant) and the stack top is the next greater element.
3. **HashMap bridges nums2 answers to nums1** -- Precompute next-greater for every element in nums2, store in a map, then look up each nums1[i].

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | For each nums1[i], linear scan right in nums2 | Nested loops |
| Optimal | Monotonic stack on nums2 + HashMap | Stack + HashMap |
| Best | Same as optimal, traversing right-to-left for clarity | Stack + HashMap |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Nested Linear Scan

**Intuition:** For each element x in nums1, locate x in nums2, then scan rightward in nums2 for the first element greater than x.

**BUD Analysis:**
- **B**ottleneck: Finding x in nums2 is O(n), then scanning right is O(n). Total: O(m * n).
- **U**nnecessary work: We re-scan nums2 for every element of nums1.
- **D**uplicate work: If nums1 = [1, 2] and both 1 and 2 need to scan the same suffix of nums2.

**Steps:**
1. For each `x` in `nums1`:
   - Find index `j` of `x` in `nums2`.
   - Scan from `j+1` to end of `nums2` for first element `> x`.
   - Record that element, or -1 if none found.

**Dry-Run Trace** (nums1=[4,1,2], nums2=[1,3,4,2]):
```
x=4: found at index 2 in nums2. Scan right: [2]. None > 4. -> -1
x=1: found at index 0 in nums2. Scan right: [3,4,2]. First > 1 is 3. -> 3
x=2: found at index 3 in nums2. Scan right: []. None. -> -1
Result: [-1, 3, -1]
```

| Metric | Value |
|--------|-------|
| Time | O(m * n) where m = len(nums1), n = len(nums2) |
| Space | O(m) for result |

---

### Approach 2: Optimal -- Monotonic Stack + HashMap (Left-to-Right)

**Intuition:** Process nums2 left-to-right. Maintain a **monotonic decreasing stack**. When we encounter an element `nums2[i]` that is greater than the stack top, the stack top has found its next greater element. Pop and record. After processing all of nums2, anything remaining in the stack has no next greater element (-1). Store results in a HashMap for O(1) lookup per nums1[i].

**BUD Analysis:**
- Each element is pushed and popped at most once -> O(n) amortized.
- HashMap lookups for nums1: O(m).
- Total: O(n + m).

**Steps:**
1. Initialize empty stack and HashMap `nge`.
2. Traverse `nums2` left to right:
   - While stack is not empty AND `nums2[i] > stack.top()`:
     - Pop `val`. Set `nge[val] = nums2[i]`.
   - Push `nums2[i]` onto stack.
3. Pop remaining stack elements, set their NGE to -1.
4. For each `x` in `nums1`, `ans[i] = nge[x]`.

**Dry-Run Trace** (nums2=[1,3,4,2]):
```
i=0, val=1: stack=[], push 1.          stack=[1]
i=1, val=3: 3>1, pop 1, nge[1]=3.     stack=[]
             push 3.                    stack=[3]
i=2, val=4: 4>3, pop 3, nge[3]=4.     stack=[]
             push 4.                    stack=[4]
i=3, val=2: 2<4, push 2.              stack=[4,2]

Remaining: pop 2 -> nge[2]=-1, pop 4 -> nge[4]=-1

nge = {1:3, 3:4, 4:-1, 2:-1}
nums1=[4,1,2] -> [-1, 3, -1]
```

| Metric | Value |
|--------|-------|
| Time | O(n + m) |
| Space | O(n) for stack + HashMap |

---

### Approach 3: Best -- Monotonic Stack Right-to-Left

**Intuition:** Traverse nums2 from **right to left**. Maintain a stack of "candidates" for next greater element. For each element, pop all stack elements smaller than it (they can never be NGE for anything further left). The stack top (if it exists) is the NGE. Then push the current element. This approach is slightly more intuitive because at each step, the stack directly represents "elements to my right that could be my NGE."

**Steps:**
1. Initialize empty stack and HashMap `nge`.
2. Traverse `nums2` from right to left:
   - Pop all elements from stack that are `<= nums2[i]`.
   - If stack is not empty, `nge[nums2[i]] = stack.top()`. Else `nge[nums2[i]] = -1`.
   - Push `nums2[i]`.
3. For each `x` in `nums1`, `ans[i] = nge[x]`.

**Dry-Run Trace** (nums2=[1,3,4,2]):
```
i=3, val=2: stack=[]. nge[2]=-1. push 2.       stack=[2]
i=2, val=4: pop 2 (2<=4). stack=[]. nge[4]=-1. push 4. stack=[4]
i=1, val=3: 4>3, keep. nge[3]=4. push 3.       stack=[4,3]
i=0, val=1: 3>1, keep. nge[1]=3. push 1.       stack=[4,3,1]

nge = {2:-1, 4:-1, 3:4, 1:3}
nums1=[4,1,2] -> [-1, 3, -1]
```

| Metric | Value |
|--------|-------|
| Time | O(n + m) |
| Space | O(n) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) for the stack approach:** Each of the n elements in nums2 is pushed onto the stack exactly once and popped at most once. So across all iterations, the total number of push + pop operations is at most 2n. This is the hallmark of amortized O(n) for monotonic stack problems.

**Space O(n):** The HashMap stores one entry per element in nums2, and the stack holds at most n elements at any time.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| nums1 = nums2 | NGE for each element | No special handling needed |
| Strictly decreasing nums2 | All -1 | Stack never pops during traversal |
| Strictly increasing nums2 | Each element's NGE is the next one, last is -1 | All pops happen immediately |
| Single element | [-1] | Stack left with one element |
| nums1 has one element at end of nums2 | [-1] | Nothing to the right |

**Common Mistakes:**
- Using `<` instead of `<=` when popping (problem says "greater", not "greater or equal").
- Forgetting to handle elements remaining in the stack after traversal (they map to -1).
- Confusing the direction: left-to-right vs. right-to-left changes the pop condition.

---

## 6. INTERVIEW LENS

**Why interviewers ask this:** It is the canonical introduction to monotonic stacks -- a pattern that appears in stock span, daily temperatures, largest rectangle in histogram, and many more.

**Follow-ups to expect:**
- "What if the array is circular?" -> Next Greater Element II (LC #503), use 2n traversal.
- "Next smaller element?" -> Flip the comparison; use a monotonic increasing stack.
- "Can you do it without a HashMap?" -> Only if nums1 == nums2 or you use index mapping.

**Talking points:**
- Explain the amortized O(n) argument clearly -- each element pushed/popped at most once.
- Mention that "monotonic stack" means the stack invariant is maintained (decreasing in this case).

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Next Greater Element II (LC #503) | Circular variant -- traverse 2n with modulo |
| Daily Temperatures (LC #739) | Same pattern: next warmer day = next greater element |
| Stock Span Problem | Monotonic stack from the other direction |
| Largest Rectangle in Histogram | Uses next-smaller-element, dual of this problem |
| Next Smaller Element | Same approach with flipped comparison |
