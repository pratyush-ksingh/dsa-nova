# Linear Search

> **Batch 3 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an unsorted array `arr` of size `n` and a target value `target`, find the index of `target` in the array. If the target is not present, return `-1`.

**Constraints:**
- `1 <= n <= 10^5`
- `-10^9 <= arr[i], target <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `arr = [4, 1, 7, 3, 9], target = 7` | `2` | 7 is at index 2 |
| `arr = [4, 1, 7, 3, 9], target = 5` | `-1` | 5 is not in the array |
| `arr = [10], target = 10` | `0` | Found at the only position |
| `arr = [2, 2, 2], target = 2` | `0` | Returns first occurrence |

### Real-Life Analogy
> *You lost your keys somewhere in the house. You do not know any special order -- the keys could be anywhere. So you check room by room, one at a time, starting from the first. The moment you find them, you stop. If you have checked every room and still have not found them, you conclude they are not there. That is linear search.*

### Key Observations
1. The array is unsorted, so binary search is not applicable. We must potentially check every element.
2. We can stop early the moment we find a match -- no need to continue.
3. Linear search is the fundamental baseline; it works on any data structure that supports iteration. <-- This is the "aha" insight: it is the fallback when no better structure or ordering exists.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- We need sequential access to elements. An array gives O(1) indexed access.
- A hash set would give O(1) lookup but requires O(n) preprocessing. For a single query, the total is still O(n), and linear search avoids the extra space.

### Pattern Recognition
- **Pattern:** Linear Scan (exhaustive check)
- **Classification Cue:** "When you see _find element in unsorted collection_ with _no preprocessing allowed_ --> think _linear search_"

---

## APPROACH LADDER

### Approach 1: Brute Force -- Basic Linear Search
**Idea:** Iterate through the array from index 0 to n-1. Return the index when the element matches the target.

**Steps:**
1. For each index `i` from `0` to `n - 1`:
   - If `arr[i] == target`, return `i`.
2. If the loop ends without finding, return `-1`.

**Dry Run:** `arr = [4, 1, 7, 3, 9], target = 7`

| Step | i | arr[i] | Match? | Action |
|------|---|--------|--------|--------|
| 1 | 0 | 4 | No | Continue |
| 2 | 1 | 1 | No | Continue |
| 3 | 2 | 7 | Yes | Return 2 |

**Result:** 2

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Sentinel Linear Search
**What changed:** Place the target at the end of the array as a "sentinel." This eliminates the bounds check (`i < n`) in the loop, leaving only the equality check. After the loop, check if the found index is the sentinel or a genuine match.

**Steps:**
1. Save `last = arr[n - 1]`.
2. Set `arr[n - 1] = target` (sentinel).
3. Start `i = 0`. While `arr[i] != target`, increment `i`.
4. Restore `arr[n - 1] = last`.
5. If `i < n - 1` or `last == target`, return `i`. Else return `-1`.

**Why faster in practice:** The inner loop has one comparison instead of two, roughly halving branch instructions. Asymptotically still O(n), but the constant factor improves.

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 3: Best -- Search from Both Ends
**What changed:** Check from the front and back simultaneously, effectively halving the number of iterations on average.

**Steps:**
1. Set `left = 0`, `right = n - 1`.
2. While `left <= right`:
   - If `arr[left] == target`, return `left`.
   - If `arr[right] == target`, return `right`.
   - Increment `left`, decrement `right`.
3. Return `-1`.

| Time | Space |
|------|-------|
| O(n) worst case, ~n/2 comparisons on average | O(1) |

*Note:* All three approaches are O(n). For unsorted data with a single query, you cannot beat O(n). The improvements are constant-factor optimizations.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "In the worst case (element not present or at the very end), we must check every single element."
**Space:** O(1) -- "We use only a loop counter and perhaps a sentinel variable. No extra data structures."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Returning `true/false` instead of the index -- read the problem carefully.
2. Not handling "target not found" -- forgetting to return `-1`.
3. Returning the last occurrence instead of the first (if duplicates exist).

### Edge Cases to Test
- [ ] Target at index 0 `[5, 1, 2], target=5` --> 0
- [ ] Target at last index `[1, 2, 5], target=5` --> 2
- [ ] Target not in array --> -1
- [ ] Single element, match --> 0
- [ ] Single element, no match --> -1
- [ ] All elements are the target `[3, 3, 3], target=3` --> 0 (first occurrence)
- [ ] Empty array (if allowed) --> -1

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Is the array sorted? Should I return the first occurrence? What if there are duplicates?"
2. **Approach:** "Since it is unsorted, I will do a linear scan. O(n) time, O(1) space. This is optimal for a single query on unsorted data."
3. **Code:** Simple for-loop with early return.
4. **Test:** Walk through a small example showing the early termination.

### Follow-Up Questions
- "What if the array is sorted?" --> Binary search, O(log n).
- "What if you need to search multiple times?" --> Build a hash map for O(1) lookups, O(n) space.
- "What about searching in a linked list?" --> Same O(n) linear scan, but no random access.

---

## CONNECTIONS
- **Prerequisite:** Array traversal basics
- **Same Pattern:** Find minimum/maximum, count occurrences
- **Harder Variant:** Binary Search (sorted data), Two Sum (hash map optimization)
- **This Unlocks:** Understanding why sorted data and hash structures exist -- they overcome linear search's limitations
