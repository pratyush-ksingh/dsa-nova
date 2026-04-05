# Highest and Lowest Frequency Element

> **Batch 2 of 12** | **Topic:** Hashing | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an array of N integers, find the elements that have the **highest** and **lowest** frequency. If multiple elements share the same highest (or lowest) frequency, return any one of them.

**Example:**
```
Input: arr = [10, 5, 10, 15, 10, 5]
Output: Highest frequency: 10, Lowest frequency: 15
```

| Input | Output | Explanation |
|---|---|---|
| [10, 5, 10, 15, 10, 5] | Highest: 10, Lowest: 15 | 10 appears 3x, 5 appears 2x, 15 appears 1x |
| [1, 1, 1, 1] | Highest: 1, Lowest: 1 | Only one unique element |
| [3, 2, 3, 2] | Highest: 3, Lowest: 3 | Both have freq 2, so both are "highest" and "lowest" |
| [7] | Highest: 7, Lowest: 7 | Single element is both |

### Real-Life Analogy
You run a restaurant and want to know which dish is ordered the most (so you can stock up) and which dish is ordered the least (so you can consider removing it). You go through today's order slips, tally each dish, then simply look for the tallest and shortest tally marks. Building the tally is the **hashing** step; scanning for max/min is a linear pass over the tally chart.

### Key Observations
1. We first need to **count** every element's frequency -- this is the "Counting Frequency" problem from P001.
2. Once we have a frequency map, finding the max and min frequency is a simple linear scan over the map's values.
3. **Aha moment:** The problem reduces to two sub-problems chained together -- frequency counting (O(N)) then max/min finding (O(K)). Recognizing that decomposition keeps the overall solution O(N).

### Constraints
- 1 <= N <= 10^5
- -10^9 <= arr[i] <= 10^9
- Array is non-empty

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why HashMap?
We need to count occurrences of each element. A hash map gives O(1) average insert/lookup, letting us build the frequency table in one pass. After that, scanning the map entries for max/min frequency is O(K) where K = unique elements.

### Pattern Recognition
**Classification cue:** Anytime you see "most frequent" or "least frequent," think HashMap for counting followed by a scan or heap. This extends the basic frequency-counting pattern with a min/max extraction step.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Nested Loop Count then Scan
**Intuition:** For each unique element, count its occurrences by scanning the whole array. Track the element with the highest and lowest count as you go.

**Steps:**
1. For each index `i`, skip if arr[i] was already processed (check prior indices).
2. Count occurrences of arr[i] by scanning the full array.
3. Update `maxFreqElem` and `minFreqElem` if this count exceeds/undercuts the current best.

**Dry Run Trace (arr = [10, 5, 10, 15, 10, 5]):**

| i | arr[i] | Seen? | Count | maxFreq (elem) | minFreq (elem) |
|---|--------|-------|-------|-----------------|-----------------|
| 0 | 10 | No | 3 | 3 (10) | 3 (10) |
| 1 | 5 | No | 2 | 3 (10) | 2 (5) |
| 2 | 10 | Yes | skip | -- | -- |
| 3 | 15 | No | 1 | 3 (10) | 1 (15) |
| 4 | 10 | Yes | skip | -- | -- |
| 5 | 5 | Yes | skip | -- | -- |

**Result:** Highest = 10, Lowest = 15

| Metric | Value |
|--------|-------|
| Time | O(N^2) |
| Space | O(1) extra (or O(N) with visited array) |

**BUD Transition:** The **B**ottleneck is re-scanning the array for every unique element. If we could count all elements in one pass (hash map), the total work drops to O(N). The max/min extraction is just an O(K) epilogue.

---

### Approach 2: Optimal -- HashMap + Linear Scan
**Intuition:** Build a frequency map in one pass, then iterate over the map to find the keys with maximum and minimum frequency.

**Steps:**
1. Create a hash map `freq`. Walk the array: `freq[x] += 1`.
2. Initialize `maxFreq = 0, minFreq = INF` and their corresponding elements.
3. For each entry `(key, count)` in freq:
   - If `count > maxFreq`, update `maxFreq = count`, `maxElem = key`.
   - If `count < minFreq`, update `minFreq = count`, `minElem = key`.
4. Return `(maxElem, minElem)`.

**Dry Run Trace (arr = [10, 5, 10, 15, 10, 5]):**

| Step | Action | freq map | maxFreq | minFreq |
|------|--------|----------|---------|---------|
| Pass 1 | Build map | {10:3, 5:2, 15:1} | -- | -- |
| Scan 10 | count=3 | -- | 3 (10) | 3 (10) |
| Scan 5 | count=2 | -- | 3 (10) | 2 (5) |
| Scan 15 | count=1 | -- | 3 (10) | 1 (15) |

| Metric | Value |
|--------|-------|
| Time | O(N) average |
| Space | O(K) where K = unique elements |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N) for the optimal approach?** We walk the array once to build the map (N operations, each O(1) on average). Then we walk the map (at most N entries). Two linear passes = O(N).

**Why O(N^2) for brute force?** For each of up to N elements, we scan the entire array of N elements. That nests to N * N.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Returning frequency instead of the element | Confusing "highest frequency element" with "highest frequency value" | Return the key, not the value |
| Not handling ties | Multiple elements may share the max or min frequency | Problem says return any one; document this |
| Initializing minFreq to 0 | No element has 0 frequency, so min never updates | Initialize minFreq to Integer.MAX_VALUE or infinity |

### Edge Cases Checklist
- Single element array --> that element is both highest and lowest
- All elements identical --> same element for both
- All elements unique --> all have frequency 1, so any element is both highest and lowest
- Two elements with same frequency --> either can be returned

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Should I return the element or the frequency? What if there are ties?"
2. **M**atch: "Frequency counting --> hash map. Then scan for max/min."
3. **P**lan: "One pass to build freq map, one pass to find extremes."
4. **I**mplement: HashMap in Java, dict in Python.
5. **R**eview: Trace with [10, 5, 10, 15, 10, 5].
6. **E**valuate: "O(N) time, O(K) space. Can't do better since we must read every element."

### Follow-Up Questions
- "What if you need the top K most frequent?" --> Use a min-heap of size K on the frequency map.
- "What if elements arrive as a stream?" --> Maintain a running frequency map and track current max/min.
- "What if you need both the element AND the frequency?" --> Return a pair/tuple.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Counting Frequency of Elements (P001) |
| **Same pattern** | Majority Element (Boyer-Moore), Top K Frequent |
| **Harder variant** | Top K Frequent Elements (LeetCode #347) |
| **Unlocks** | Frequency Sort, First Unique Character |
