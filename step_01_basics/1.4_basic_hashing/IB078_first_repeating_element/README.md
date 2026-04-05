# First Repeating Element

> **Step 01 | 1.4** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit

---

## 1. UNDERSTAND

### Problem Statement
Given an integer array `A` of size N, find the **first repeating element** -- the element that occurs more than once and whose **first occurrence** has the smallest index. Return the element itself. If no element repeats, return **-1**.

**Example:**
```
Input:  A = [10, 5, 3, 4, 3, 5, 6]
Output: 5
Explanation: Both 5 and 3 repeat. First occurrence of 5 is index 1,
             first occurrence of 3 is index 2. So 5 is the answer.
```

| Input | Output | Explanation |
|-------|--------|-------------|
| [10, 5, 3, 4, 3, 5, 6] | 5 | 5 repeats (indices 1,5), 3 repeats (indices 2,4). 5 appears first at index 1. |
| [6, 10, 5, 4, 9, 120] | -1 | No element repeats |
| [1, 2, 3, 1, 2] | 1 | 1 repeats at indices 0,3. 2 repeats at indices 1,4. 1 comes first. |

### Real-Life Analogy
Imagine a **teacher taking attendance**. Students call out their names one by one. The teacher wants to know which student's name was called out first among those who were called more than once (indicating a duplicate entry). She needs the earliest name in the original calling order that has a repeat somewhere later.

### Key Observations
1. We want the element with the **smallest first-occurrence index** among all repeating elements.
2. A brute force approach checks every pair -- O(N^2).
3. **Aha moment:** Scanning right-to-left with a set, every time we find a duplicate, we update the answer. The final answer is the leftmost repeating element.

### Constraints
- 1 <= N <= 10^5
- 1 <= A[i] <= 10^9

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Hashing?
We need to detect if an element has been seen before. A HashSet/HashMap provides O(1) lookup, making the overall solution O(N).

### Pattern Recognition
**Classification cue:** "Find first/earliest duplicate" --> frequency counting with HashMap, or reverse-scan with HashSet.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Nested Loops
**Intuition:** For each element at index `i`, scan all elements after it. If a match is found, return `arr[i]` immediately -- since we scan left-to-right, the first match we find corresponds to the leftmost repeating element.

**Steps:**
1. Loop `i` from 0 to N-1.
2. Loop `j` from `i+1` to N-1.
3. If `arr[i] == arr[j]`, return `arr[i]`.
4. If no match found, return -1.

**Dry Run Trace (A = [10, 5, 3, 4, 3, 5, 6]):**

| i | j | arr[i] | arr[j] | Match? |
|---|---|--------|--------|--------|
| 0 | 1..6 | 10 | -- | No match |
| 1 | 2..5 | 5 | 5 at j=5 | Yes! Return 5 |

Result: 5

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(1)   |

**BUD Transition:** Use hashing to avoid the inner loop.

---

### Approach 2: Optimal -- HashMap Frequency Count
**Intuition:** Two-pass approach. First pass: count frequency of every element. Second pass (left-to-right): return the first element whose frequency exceeds 1.

**Steps:**
1. Build frequency map: `{element: count}`.
2. Scan array left-to-right: if `freq[arr[i]] > 1`, return `arr[i]`.
3. If no element qualifies, return -1.

| Metric | Value |
|--------|-------|
| Time   | O(N)  |
| Space  | O(N)  |

---

### Approach 3: Best -- Reverse Scan with HashSet
**Intuition:** Scan from right to left. Maintain a set of elements seen so far. If the current element is already in the set, it has a duplicate to its right -- update the result. Since we are moving left, each update moves the answer to an earlier index. The final value of `result` is the leftmost repeating element. This is elegant: single pass, single set.

**Steps:**
1. Initialize `seen = {}`, `result = -1`.
2. Loop `i` from N-1 down to 0.
3. If `arr[i]` in `seen`: set `result = arr[i]`.
4. Else: add `arr[i]` to `seen`.
5. Return `result`.

**Dry Run Trace (A = [10, 5, 3, 4, 3, 5, 6]):**

| i | arr[i] | seen | result |
|---|--------|------|--------|
| 6 | 6 | {6} | -1 |
| 5 | 5 | {6,5} | -1 |
| 4 | 3 | {6,5,3} | -1 |
| 3 | 4 | {6,5,3,4} | -1 |
| 2 | 3 | {6,5,3,4} | 3 |
| 1 | 5 | {6,5,3,4} | 5 |
| 0 | 10 | {6,5,3,4,10} | 5 |

Result: 5

| Metric | Value |
|--------|-------|
| Time   | O(N)  |
| Space  | O(N)  |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N)?** We visit each element at most twice (once to count/check, once to scan). Hash operations are O(1) average. We cannot do better than O(N) since we must inspect every element at least once.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Returning first element seen twice (left-to-right with set) | This finds the element whose *second* occurrence is earliest, not whose *first* occurrence is earliest | Use frequency map + rescan, or reverse scan |
| Confusing "first repeating" definition | Returning by second occurrence index instead of first | Clarify: we want smallest first-occurrence index |

### Edge Cases Checklist
- All elements unique --> return -1
- All elements same --> return that element
- Single element --> return -1
- Two elements, same --> return that element

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "First by first-occurrence index, among elements that repeat?"
2. **M**atch: "Frequency counting or set-based duplicate detection."
3. **P**lan: "Reverse scan with set -- elegant single pass."
4. **I**mplement: Clean code.
5. **R**eview: Dry run with example.
6. **E**valuate: "O(N) time, O(N) space."

### Follow-Up Questions
- "What if we want the index instead of the element?" --> Same approach, store index.
- "What if we want all repeating elements in order of first occurrence?" --> Use frequency map, filter.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic HashMap/HashSet usage |
| **Same pattern** | Two Sum (hash-based lookup), Contains Duplicate |
| **Harder variant** | First non-repeating element, First unique character in a stream |
| **Unlocks** | Frequency-based problems, sliding window with counts |
