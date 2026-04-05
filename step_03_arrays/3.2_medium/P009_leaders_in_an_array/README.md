# Leaders in an Array

> **Batch 4 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an array of integers, find all **leaders**. An element is a leader if it is **strictly greater than all elements to its right**. The rightmost element is always a leader (there are no elements to its right).

Return the leaders in the order they appear in the array.

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[16, 17, 4, 3, 5, 2]` | `[17, 5, 2]` | 17 > all to its right (4,3,5,2). 5 > 2. 2 is rightmost. |
| `[1, 2, 3, 4, 5]` | `[5]` | Only the last element; every other element has a larger one to the right |
| `[5, 4, 3, 2, 1]` | `[5, 4, 3, 2, 1]` | Descending order: every element is a leader |
| `[7]` | `[7]` | Single element is always a leader |

### Constraints
- 1 <= n <= 10^5
- -10^9 <= arr[i] <= 10^9

### Real-Life Analogy
Imagine a row of people standing in line, each holding a sign with their height. A person is a "leader" if no one taller is standing behind them (to their right). If you walk from the back of the line forward, you would only need to remember the tallest person seen so far. Anyone taller than that running maximum is a new leader.

### Key Observations
1. Checking if an element is a leader requires knowing the **maximum of all elements to its right**. Scanning right-to-left lets you compute this running maximum in one pass.
2. The brute force (for each element, scan all elements to its right) is O(n^2). But since we only need the running maximum from the right, a single reverse pass gives O(n).
3. **Aha moment:** Traverse from right to left, maintaining `max_from_right`. If the current element is greater than `max_from_right`, it is a leader. Update `max_from_right`. This single-pass reverse scan is the key insight.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Reverse Linear Scan?
The "leader" condition depends on the suffix maximum. By scanning right-to-left, we incrementally build the suffix maximum in O(1) per step. No extra data structure needed beyond the result list and a single variable.

### Pattern Recognition
**Classification cue:** "Element property depends on all elements to its right/left" --> reverse scan with a running aggregate (max, min, sum, etc.). This is the same pattern as "Next Greater Element" and "Stock Span."

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check Every Suffix
**Intuition:** For each element, scan all elements to its right. If none are greater or equal, it is a leader.

**Steps:**
1. For each index `i` from 0 to n-1:
2. Loop `j` from i+1 to n-1, check if any `arr[j] >= arr[i]`.
3. If no such j found, `arr[i]` is a leader.

**Dry Run (arr = [16, 17, 4, 3, 5, 2]):**

| i | arr[i] | Check right | Leader? |
|---|--------|-------------|---------|
| 0 | 16     | 17 >= 16? Yes | No |
| 1 | 17     | 4,3,5,2 -- none >= 17 | Yes |
| 2 | 4      | 5 >= 4? Yes | No |
| 3 | 3      | 5 >= 3? Yes | No |
| 4 | 5      | 2 -- none >= 5 | Yes |
| 5 | 2      | (none to right) | Yes |

Result: [17, 5, 2]

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) excluding output |

**BUD Transition:** The inner loop repeatedly recomputes the suffix maximum. We can precompute it by scanning once from right to left.

---

### Approach 2: Optimal -- Right-to-Left Single Pass
**Intuition:** Scan from right to left. Track the running maximum (`max_from_right`). Any element greater than the current `max_from_right` is a leader. Update `max_from_right` accordingly. Collect results in reverse, then reverse the list at the end.

**Steps:**
1. Initialize `max_from_right = Integer.MIN_VALUE`, result list.
2. Loop `i` from n-1 down to 0:
   - If `arr[i] > max_from_right`: add `arr[i]` to result.
   - Update `max_from_right = max(max_from_right, arr[i])`.
3. Reverse the result list to restore left-to-right order.

**Dry Run (arr = [16, 17, 4, 3, 5, 2]):**

| i | arr[i] | max_from_right | Leader? | Result (building) |
|---|--------|---------------|---------|-------------------|
| 5 | 2      | -inf          | Yes     | [2] |
| 4 | 5      | 2             | Yes     | [2, 5] |
| 3 | 3      | 5             | No      | [2, 5] |
| 2 | 4      | 5             | No      | [2, 5] |
| 1 | 17     | 5             | Yes     | [2, 5, 17] |
| 0 | 16     | 17            | No      | [2, 5, 17] |

Reverse: [17, 5, 2]

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) excluding output |

*This is the best possible approach -- you must examine every element at least once.*

---

### Approach 3: Best -- Same as Optimal (Stack Variant)
**Intuition:** A stack-based variant is occasionally shown but offers no improvement. You push elements onto a stack while scanning right-to-left, popping when a larger element is found. The result is the same O(n) time, but uses O(n) stack space. The single-variable approach (Approach 2) is strictly better. We include the stack variant for completeness.

**Steps:**
1. Initialize an empty stack.
2. Scan right to left. Push element onto stack if it is greater than stack top (or stack is empty).
3. The stack contents at the end are the leaders in reverse order.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) for the stack |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) is optimal?** Each element could be the leader (descending array), so you must check every element. O(n) is the information-theoretic lower bound. The reverse-scan approach achieves this with a single pass and one extra variable.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Using >= instead of > | Ambiguity: is equal to the right max a leader? | Problem says "strictly greater." Clarify with interviewer. |
| Forgetting to reverse the result | Right-to-left scan produces leaders in reverse order | Reverse at the end, or use a deque/prepend |
| Missing the rightmost element | Not including the last element as a leader | The last element is always a leader by definition |

### Edge Cases Checklist
- Single element `[7]` --> `[7]`
- All same elements `[5, 5, 5]` --> `[5]` (only rightmost, since 5 is not > 5)
- Ascending `[1, 2, 3]` --> `[3]`
- Descending `[3, 2, 1]` --> `[3, 2, 1]`
- Negative numbers `[-3, -1, -2]` --> `[-1, -2]`

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Leader = strictly greater than all to its right. Rightmost is always a leader. Return in original order?"
2. **M**atch: "Suffix maximum problem. Reverse scan with running max."
3. **P**lan: "Scan right-to-left, track max_from_right, collect leaders, reverse at end."
4. **I**mplement: Clean code, handle single-element case naturally.
5. **R**eview: Walk through [16, 17, 4, 3, 5, 2].
6. **E**valuate: "O(n) time, O(1) extra space."

### Follow-Up Questions
- "What if we need leaders from the left (greater than all to the left)?" --> Same idea, scan left-to-right with `max_from_left`.
- "What if we need the count of leaders, not the list?" --> Same scan, just increment a counter instead of appending.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic array traversal, running maximum |
| **Same pattern** | Next Greater Element, Stock Span (suffix/prefix aggregates) |
| **Harder variant** | Leaders with updates (segment tree), Count of Smaller Numbers After Self |
| **Unlocks** | Reverse-scan technique for suffix-dependent problems |
