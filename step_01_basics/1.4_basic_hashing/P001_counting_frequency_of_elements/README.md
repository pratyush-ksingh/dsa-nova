# Counting Frequency of Elements

> **Batch 1 of 12** | **Topic:** Hashing | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an array of N integers, count the frequency (number of occurrences) of each element and print each element with its frequency.

**Example:**
```
Input: arr = [1, 3, 2, 1, 3, 1, 2]
Output:
1 -> 3
3 -> 2
2 -> 2
```

| Input                  | Output                              | Explanation                                    |
|------------------------|-------------------------------------|------------------------------------------------|
| [1, 3, 2, 1, 3, 1, 2] | 1->3, 2->2, 3->2                   | 1 appears 3 times, 2 and 3 appear 2 times each |
| [5]                    | 5->1                                | Single element appears once                    |
| [7, 7, 7]             | 7->3                                | All same element                               |
| [1, 2, 3, 4, 5]       | 1->1, 2->1, 3->1, 4->1, 5->1      | All unique                                     |

### Real-Life Analogy
Imagine you are a teacher collecting homework. You have a stack of papers with student names. To count how many papers each student submitted, you can either (a) pick a name, go through the entire stack counting matches, then repeat for every name -- very slow; or (b) create a tally chart -- scan each paper once and put a mark next to that student's name. The tally chart is a **hash map**. One pass through the stack and you are done.

### Key Observations
1. The brute force checks each element against every other -- O(N^2).
2. A hash map stores `element -> count`, enabling single-pass counting.
3. **Aha moment:** Hashing trades space for time. By spending O(N) memory on a frequency map, we reduce time from O(N^2) to O(N). This space-time tradeoff is the fundamental lesson of hashing.

### Constraints
- 1 <= N <= 10^5
- -10^9 <= arr[i] <= 10^9

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why HashMap?
We need to associate each unique element with a count. A hash map provides O(1) average lookup and insertion, making it ideal. An array could work if element values are small and non-negative (count-sort style), but a hash map handles any range.

### Pattern Recognition
**Classification cue:** "Count occurrences" or "frequency of elements" --> immediately think HashMap/Dictionary. This is the most fundamental hashing pattern and appears as a subroutine in hundreds of problems (two-sum, anagram check, top-K frequent, etc.).

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Nested Loop Counting
**Intuition:** For each element, scan the entire array to count how many times it appears. Use a visited array or check to avoid printing duplicates.

**Steps:**
1. For each index `i` from 0 to N-1:
   a. If arr[i] has already been counted (check all previous indices), skip.
   b. Otherwise, count occurrences of arr[i] by scanning the full array.
   c. Print arr[i] and its count.

**Dry Run Trace (arr = [1, 3, 2, 1]):**

| i | arr[i] | Already seen? | Full scan count | Output  |
|---|--------|---------------|-----------------|---------|
| 0 | 1      | No            | 2               | 1 -> 2  |
| 1 | 3      | No            | 1               | 3 -> 1  |
| 2 | 2      | No            | 1               | 2 -> 1  |
| 3 | 1      | Yes (i=0)     | skip            | --      |

| Metric | Value          |
|--------|----------------|
| Time   | O(N^2)         |
| Space  | O(1) extra (or O(N) if using a visited array) |

**BUD Transition:** The bottleneck is re-scanning the array for each element. If we could "remember" counts as we go, we would only need one pass. That is exactly what a hash map does.

---

### Approach 2: Optimal -- HashMap (Single Pass)
**Intuition:** Walk through the array once. For each element, increment its count in a hash map. After the pass, the map contains every element's frequency.

**Steps:**
1. Create an empty hash map `freq`.
2. For each element `x` in arr:
   - `freq[x] = freq.getOrDefault(x, 0) + 1`
3. Iterate over `freq` and print each key-value pair.

**Dry Run Trace (arr = [1, 3, 2, 1, 3, 1, 2]):**

| Index | Element | freq map state                    |
|-------|---------|-----------------------------------|
| 0     | 1       | {1: 1}                            |
| 1     | 3       | {1: 1, 3: 1}                     |
| 2     | 2       | {1: 1, 3: 1, 2: 1}              |
| 3     | 1       | {1: 2, 3: 1, 2: 1}              |
| 4     | 3       | {1: 2, 3: 2, 2: 1}              |
| 5     | 1       | {1: 3, 3: 2, 2: 1}              |
| 6     | 2       | {1: 3, 3: 2, 2: 2}              |

| Metric | Value          |
|--------|----------------|
| Time   | O(N) average   |
| Space  | O(K) where K = number of unique elements |

---

### Approach 3: Best -- Sorting + Linear Scan
**Intuition:** Sort the array first. Equal elements are now adjacent, so a single scan can count consecutive runs. This uses O(1) extra space (in-place sort) but O(N log N) time. Best when you want to avoid hash map overhead or need sorted output.

**Steps:**
1. Sort the array.
2. Walk through, counting consecutive equal elements.
3. When the element changes (or array ends), print the element and its count.

**Dry Run Trace (arr = [1, 3, 2, 1, 3, 1, 2] --> sorted: [1, 1, 1, 2, 2, 3, 3]):**

| Index | Element | Count | Action           |
|-------|---------|-------|------------------|
| 0     | 1       | 1     | --               |
| 1     | 1       | 2     | --               |
| 2     | 1       | 3     | --               |
| 3     | 2       | 1     | Print 1->3       |
| 4     | 2       | 2     | --               |
| 5     | 3       | 1     | Print 2->2       |
| 6     | 3       | 2     | --               |
| end   | --      | --    | Print 3->2       |

| Metric | Value          |
|--------|----------------|
| Time   | O(N log N)     |
| Space  | O(1) extra (in-place sort) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N) for HashMap?** We visit each element exactly once. Each hash map operation (lookup + insert) is O(1) on average. So total is N * O(1) = O(N).

**Why O(N^2) for brute force?** For each of N elements, we potentially scan all N elements. That is N * N = O(N^2).

**Why O(N log N) for sorting?** Sorting dominates. The counting pass after sorting is O(N), but the sort itself is O(N log N).

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Printing duplicate entries in brute force | Not tracking which elements have been counted | Use a visited set or check previous indices |
| Using array instead of map for large/negative values | Array index cannot be negative or very large | Use HashMap/dict which handles any key |
| Forgetting to print the last group in sorting approach | The loop ends before printing the final element's count | Add a print after the loop ends |

### Edge Cases Checklist
- Empty array --> no output
- Single element --> `x -> 1`
- All elements identical --> one entry with count N
- All elements unique --> N entries each with count 1
- Negative numbers --> hash map handles them naturally

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Return a map? Print? What about order? Range of values?"
2. **M**atch: "Frequency counting --> hash map pattern."
3. **P**lan: "Single pass with a hash map. Increment counts as we go."
4. **I**mplement: Use HashMap (Java) or Counter/dict (Python).
5. **R**eview: Trace with [1, 3, 2, 1].
6. **E**valuate: "O(N) time, O(K) space. Cannot beat O(N) since we must read every element."

### Follow-Up Questions
- "What if we need the top K most frequent elements?" --> Use a heap on the frequency map (Heap + HashMap pattern).
- "What if elements are in range [0, maxVal]?" --> Use a counting array instead of hash map for better cache performance.
- "What if the array is too large for memory?" --> External sorting or streaming with limited-memory hash maps.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Array traversal basics |
| **Same pattern** | Two Sum (hash map for complement lookup), Anagram Check |
| **Harder variant** | Top K Frequent Elements, Frequency Sort |
| **Unlocks** | All hash-map-based problems; forms the counting subroutine for many algorithms |
