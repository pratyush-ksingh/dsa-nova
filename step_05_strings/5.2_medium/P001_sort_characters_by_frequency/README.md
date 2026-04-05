# Sort Characters by Frequency

> **Step 05.5.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given a string `s`, sort it in **decreasing order** based on the **frequency** of the characters. The frequency of a character is the number of times it appears in the string. Return the sorted string. If there are multiple answers, return any of them.

**LeetCode #451**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `"tree"` | `"eert"` | `'e'` appears twice, `'t'` and `'r'` once each. `"eetr"` is also valid. |
| `"cccaaa"` | `"aaaccc"` | Both `'c'` and `'a'` appear 3 times, so `"cccaaa"` is also valid. |
| `"Aabb"` | `"bbAa"` | `'b'` appears twice, `'A'` and `'a'` once each. Note `'A'` and `'a'` are different characters. |

### Constraints
- `1 <= s.length <= 5 * 10^5`
- `s` consists of uppercase and lowercase English letters and digits

### Real-Life Analogy
Imagine sorting a deck of colored cards by how many cards of each color you have. You put the biggest pile first, then the next biggest, and so on. Each pile stays together -- you don't interleave them.

### 3 Key Observations
1. **"aha" -- frequency counting first:** Before sorting, you must know how many times each character appears. A hash map or array gives O(n) counting.
2. **"aha" -- bucket sort avoids n log n:** Since frequencies range from 1 to n, you can use bucket sort where index = frequency, giving O(n) overall.
3. **"aha" -- rebuild by repeating chars:** Once sorted by frequency, the output string is just each character repeated by its count, concatenated in order.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **HashMap** for counting character frequencies in O(n).
- **Bucket array** (size n+1) where `bucket[i]` holds all characters that appeared `i` times. This avoids comparison-based sorting.

### Pattern Recognition Cue
Any time you need to sort by frequency and the range of frequencies is bounded by n, bucket sort is the go-to optimization over comparison sort.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- HashMap + Sort
**Intuition:** Count character frequencies with a map, then sort all characters by their frequency in descending order. Rebuild the string.

**Steps:**
1. Count frequency of each character using a hash map.
2. Create a list of all unique characters.
3. Sort the list by frequency (descending).
4. Build result by repeating each character by its frequency.

**BUD Transition:** Sorting costs O(k log k) where k = unique chars (up to 62). For large n, the bottleneck is O(n) anyway, but we can avoid the sort entirely with bucket sort.

**Dry Run:** `s = "tree"`
```
freq = {'t':1, 'r':1, 'e':2}
sorted chars by freq desc: ['e', 't', 'r']  (or ['e', 'r', 't'])
result = "ee" + "t" + "r" = "eetr"
```

| Metric | Value |
|--------|-------|
| Time   | O(n log n) -- sorting characters |
| Space  | O(n) -- output string + freq map |

---

### Approach 2: Optimal -- HashMap + Bucket Sort
**Intuition:** Since frequencies range from 1 to n, create buckets where `bucket[freq]` stores characters with that frequency. Iterate buckets from n down to 1.

**Steps:**
1. Count frequency of each character using a hash map.
2. Create a bucket array of size `n + 1`.
3. Place each character in `bucket[its_frequency]`.
4. Iterate from bucket `n` down to bucket `1`. For each character in the bucket, append it `freq` times to the result.

**BUD Transition from Brute:** Eliminated comparison sort. Everything is O(n).

**Dry Run:** `s = "tree"` (n=4)
```
freq = {'t':1, 'r':1, 'e':2}
buckets: [0]=[], [1]=['t','r'], [2]=['e'], [3]=[], [4]=[]
Iterate 4->1: bucket[2]='e' -> "ee", bucket[1]='t','r' -> "eetr"
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

### Approach 3: Best -- Counter + Bucket Sort (Pythonic / Compact)
**Intuition:** Same bucket sort logic but using language features (Python's `Counter.most_common()` or manual bucket sort) for the most concise implementation.

**Steps:**
1. Use `Counter` to count frequencies.
2. Use bucket sort or `most_common()` to get chars in frequency order.
3. Build result with string multiplication.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** One pass to count frequencies. One pass through buckets (total chars across all buckets = n). Building output = O(n).
- **Space O(n):** The bucket array has n+1 slots. The output string is length n. The frequency map has at most 62 entries (letters + digits).

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected Output | Why It Trips People |
|-----------|-----------------|---------------------|
| `"a"` | `"a"` | Single character |
| `"aabb"` | `"aabb"` or `"bbaa"` | Tied frequencies -- any order valid |
| `"Aabb"` | `"bbAa"` or `"bbaA"` | Case-sensitive: `'A'` != `'a'` |
| All same char `"eee"` | `"eee"` | Only one bucket used |

**Common Mistakes:**
- Treating uppercase and lowercase as the same character.
- Forgetting to repeat the character by its frequency (only outputting it once).
- Using an array of size 26 instead of a hash map (misses digits and uppercase).

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "I need to rearrange the string so higher-frequency characters come first."
2. **Match:** "Frequency counting + sorting. Bucket sort avoids O(n log n)."
3. **Plan:** Count frequencies, bucket sort, iterate buckets top-down.
4. **Implement:** Write the bucket sort solution.
5. **Review:** Trace through "tree" example.
6. **Evaluate:** O(n) time, O(n) space.

### Follow-Up Questions
- *"What if we need stable sorting (preserve original order for ties)?"* -- Use a list of (char, first_index) and sort by (-freq, first_index).
- *"What about Unicode strings?"* -- HashMap handles arbitrary characters; bucket sort still works.
- *"Can you do it in-place?"* -- Not really, since strings are immutable in Java/Python.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Valid Anagram (LC #242) -- frequency counting |
| **Same Pattern** | Top K Frequent Elements (LC #347) -- bucket sort by frequency |
| **Harder** | Sort Array by Increasing Frequency (LC #1636) |
| **Unlocks** | Reorganize String (LC #767) -- frequency-based placement |
