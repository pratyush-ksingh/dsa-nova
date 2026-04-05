# Two Out of Three

> **Step 01 | 1.4** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit (LeetCode 2032)

---

## 1. UNDERSTAND

### Problem Statement
Given three integer arrays **nums1**, **nums2**, **nums3**, return a sorted list of all distinct values that appear in **at least two** of the three arrays. Each value should appear at most once in the result regardless of how many times it repeats within a single array.

**Example:**
```
nums1 = [1, 1, 3, 2]
nums2 = [2, 3]
nums3 = [3]
Output: [2, 3]   -- 2 is in nums1 & nums2; 3 is in all three
```

| Input                                | Output   | Explanation                               |
|--------------------------------------|----------|-------------------------------------------|
| [1,1,3,2], [2,3], [3]               | [2,3]    | 2 in arr1+arr2; 3 in arr1+arr2+arr3       |
| [3,1], [2,3], [1,2]                 | [1,2,3]  | 1 in arr1+arr3; 2 in arr2+arr3; 3 in arr1+arr2 |
| [1,2,3], [4,5,6], [7,8,9]          | []       | No value shared by any two arrays         |

### Real-Life Analogy
Three friends each list their favourite movies. You want movies that at least two friends like. Convert each list to a set (deduplicate), then find common items between any two of the three sets. This is exactly what the pairwise intersection approach does.

### Key Observations
1. Duplicates within one array don't matter -- we use sets to deduplicate first.
2. An element counts once per array, not once per occurrence.
3. **Aha moment:** If we count, per array, how many arrays contain each distinct value, then filter for count >= 2, we solve it in one pass per array.
4. Pairwise intersection union: `(A∩B) ∪ (A∩C) ∪ (B∩C)` = elements in at least 2 of {A, B, C}.

### Constraints
- 1 <= nums1.length, nums2.length, nums3.length <= 100
- 1 <= nums1[i], nums2[i], nums3[i] <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Sets / HashMap?
Sets provide O(1) average-case membership lookup. Converting each array to a set handles intra-array duplicates automatically. A HashMap (occurrence counter) generalises this to any number of arrays cleanly.

### Pattern Recognition
**Classification cue:** "Elements common to >= K out of N collections" --> per-collection set deduplication + occurrence counting, or pairwise intersection union.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Linear Search for Each Candidate
**Intuition:** Collect all unique values across all three arrays. For each candidate value, linearly search through each array to count how many arrays contain it. Return those with count >= 2.

**Steps:**
1. Build `candidates = set(nums1) | set(nums2) | set(nums3)`.
2. For each `val` in candidates: count how many arrays contain it.
3. If count >= 2, add to result.
4. Sort and return.

**Dry Run ([1,1,3,2], [2,3], [3]):**
- candidates = {1,2,3}
- val=1: in arr1? Yes. in arr2? No. in arr3? No. count=1. Skip.
- val=2: in arr1? Yes. in arr2? Yes. count=2. Include.
- val=3: in arr1? Yes. in arr2? Yes. in arr3? Yes. count=3. Include.
- Result: [2, 3].

| Metric | Value                        |
|--------|------------------------------|
| Time   | O((n+m+k)^2) worst case      |
| Space  | O(n+m+k)                     |

**BUD Transition:** Use set operations to eliminate linear scans.

---

### Approach 2: Optimal -- Pairwise Set Intersections Union
**Intuition:** Convert each array to a set (handles duplicates). The set of values in at least 2 arrays equals `(s1∩s2) ∪ (s1∩s3) ∪ (s2∩s3)`. Set operations run in O(min(|A|,|B|)) time.

**Steps:**
1. `s1, s2, s3 = set(nums1), set(nums2), set(nums3)`.
2. `result = (s1 & s2) | (s1 & s3) | (s2 & s3)`.
3. Return `sorted(result)`.

| Metric | Value         |
|--------|---------------|
| Time   | O(n + m + k)  |
| Space  | O(n + m + k)  |

---

### Approach 3: Best -- Occurrence Counter (Single HashMap)
**Intuition:** Iterate over each array; for each distinct value in that array, increment a counter. After processing all arrays, values with counter >= 2 are the answer. Generalises to "at least K out of N arrays" by changing the threshold.

**Steps:**
1. Initialise `count = {}` (default 0).
2. For each array: convert to set, then for each value in the set, `count[val] += 1`.
3. Collect `val` where `count[val] >= 2`.
4. Sort and return.

**Why `set(arr)` inside the loop?** Ensures each value is counted at most once per array, even if it appears multiple times.

| Metric | Value         |
|--------|---------------|
| Time   | O(n + m + k)  |
| Space  | O(n + m + k)  |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n+m+k)?** Building three sets takes O(n), O(m), O(k). Set intersection/union or map iteration is O(size of sets) which is bounded by O(n+m+k). Sorting the result is O(R log R) where R <= 100 (constraint), so effectively O(1) here.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake                                      | Why it happens                            | Fix                                           |
|--------------------------------------------|-------------------------------------------|-----------------------------------------------|
| Not deduplicating within arrays             | Counting value twice in same array        | Convert each array to set before counting     |
| Using list `in` instead of set `in`         | O(n) per lookup instead of O(1)           | Always convert to set for membership checks  |
| Returning duplicates in result              | Not deduplicating the output              | Use a set or dict for result collection       |

### Edge Cases Checklist
- All arrays contain the same single element: result = [that element]
- No overlap between any two arrays: result = []
- All values identical within one array (duplicates): deduplicate via set

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Find values in at least 2 of 3 arrays. Duplicates within one array don't matter."
2. **M**atch: "Set-based problem -- deduplication and intersection."
3. **P**lan: "Convert to sets. Union of pairwise intersections."
4. **I**mplement: 3 lines of set operations or a counter map.
5. **R**eview: Trace example [1,1,3,2],[2,3],[3] -> {2,3}.
6. **E**valuate: "O(n+m+k) time and space."

### Follow-Up Questions
- "Extend to N arrays, find elements in at least K of them?" --> Use the counter approach: threshold = K.
- "What if arrays are very large (streaming)?" --> HyperLogLog for approximate set membership at scale.
- "Sort the result?" --> Output must be sorted; add `Collections.sort()` / `sorted()`.

---

## 7. CONNECTIONS

| Relationship      | Problem                                                          |
|-------------------|------------------------------------------------------------------|
| **Prerequisite**  | Set operations; HashMap counting                               |
| **Same pattern**  | Intersection of two arrays (LeetCode 349/350)                  |
| **Harder variant**| Elements common to at least K of N arrays (generalised)        |
| **Unlocks**       | Set-cover problems; Venn diagram logic; stream deduplication   |
