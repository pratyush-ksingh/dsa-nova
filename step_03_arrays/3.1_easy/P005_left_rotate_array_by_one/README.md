# Left Rotate Array by One

> **Batch 3 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array `arr` of size `n`, rotate the array to the left by one position. The first element moves to the last position, and every other element shifts one position to the left.

**Constraints:**
- `1 <= n <= 10^5`
- `1 <= arr[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1, 2, 3, 4, 5]` | `[2, 3, 4, 5, 1]` | 1 moves to end, everything shifts left |
| `[7]` | `[7]` | Single element -- nothing changes |
| `[3, 9]` | `[9, 3]` | 3 goes to end, 9 takes position 0 |

### Real-Life Analogy
> *Picture a queue at a ticket counter. The person at the front gets served and then walks to the back of the line. Everyone else steps forward by one position. That single cycle of movement is exactly a left rotation by one.*

### Key Observations
1. Every element at index `i` moves to index `i - 1` -- this is a simple shift operation.
2. The only "special" element is `arr[0]`, which wraps around to `arr[n - 1]`.
3. If we save `arr[0]` first, we can shift everything left in one pass and place the saved value at the end. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- We operate on the array **in-place** -- no need for auxiliary structures.
- A deque would make this O(1) via `popleft` + `append`, but converting to/from a deque is O(n) anyway, so we gain nothing for a one-time rotation.

### Pattern Recognition
- **Pattern:** In-Place Array Manipulation (save, shift, place)
- **Classification Cue:** "When you see _rotate/shift array elements_ --> think _save the boundary element, shift the rest, put it back_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Extra Array)
**Idea:** Create a new array where each element is placed at its rotated position.

**Steps:**
1. Create a new array `temp` of size `n`.
2. Copy `arr[1..n-1]` into `temp[0..n-2]`.
3. Place `arr[0]` at `temp[n-1]`.
4. Copy `temp` back into `arr`.

**Why it works:** We explicitly place each element at its new index.

**BUD -- Unnecessary Space:** We allocate an entire new array when every element just moves one slot. We can do this in-place.

| Time | Space |
|------|-------|
| O(n) | O(n) |

### Approach 2: Optimal -- In-Place Shift
**What changed:** Save the first element, shift everything left by one, then place the saved element at the end. No extra array needed.

**Steps:**
1. Store `first = arr[0]`.
2. For `i` from `0` to `n - 2`: set `arr[i] = arr[i + 1]`.
3. Set `arr[n - 1] = first`.

**Dry Run:** Input = `[1, 2, 3, 4, 5]`

| Step | i | arr[i] <- arr[i+1] | Array State |
|------|---|---------------------|-------------|
| Save | - | first = 1 | `[1, 2, 3, 4, 5]` |
| 1 | 0 | arr[0] = 2 | `[2, 2, 3, 4, 5]` |
| 2 | 1 | arr[1] = 3 | `[2, 3, 3, 4, 5]` |
| 3 | 2 | arr[2] = 4 | `[2, 3, 4, 4, 5]` |
| 4 | 3 | arr[3] = 5 | `[2, 3, 4, 5, 5]` |
| Place | - | arr[4] = 1 | `[2, 3, 4, 5, 1]` |

**Result:** `[2, 3, 4, 5, 1]`

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 3: Best -- Pythonic / Language Trick
**What changed:** Many languages provide slice or rotation primitives that express the intent more clearly.

**Steps (Python):** `arr[:] = arr[1:] + arr[:1]`
**Steps (Java):** Use `Collections.rotate(list, -1)` for List types.

Under the hood this still does O(n) work, but the code is a single expressive line. In an interview, mention this but implement Approach 2 to show understanding.

| Time | Space |
|------|-------|
| O(n) | O(n) internally for slicing |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We touch each element exactly once to shift it one position. You cannot rotate without moving every element."
**Space:** O(1) for the optimal approach -- "We only store one extra variable (`first`), regardless of array size."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to save `arr[0]` before overwriting it -- the value gets lost.
2. Shifting right instead of left -- direction of the loop matters.
3. Off-by-one: iterating up to `n-1` inclusive causes out-of-bounds access (`arr[n]`).

### Edge Cases to Test
- [ ] Single element `[42]` --> stays `[42]`
- [ ] Two elements `[1, 2]` --> `[2, 1]`
- [ ] All identical `[5, 5, 5]` --> `[5, 5, 5]`
- [ ] Large array at constraint limit (n = 10^5)

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Should I rotate in-place or can I return a new array? Is it always exactly one rotation?"
2. **Approach:** "I'll save the first element, shift everything left by one, and place the saved element at the end. This is O(n) time, O(1) space."
3. **Code:** Write the 3-line solution. Narrate the boundary handling.
4. **Test:** Walk through `[1, 2, 3, 4, 5]` showing each shift step.

### Follow-Up Questions
- "What if you need to rotate by k positions?" --> Reverse-based approach: reverse(0, k-1), reverse(k, n-1), reverse(0, n-1). O(n) time, O(1) space.
- "What about right rotation?" --> Same idea, save last element, shift right, place at front.

---

## CONNECTIONS
- **Prerequisite:** Basic array traversal, in-place manipulation
- **Same Pattern:** Rotate Array by K (reverse trick), Cyclic Sort
- **Harder Variant:** Rotate Array by K Positions (LeetCode #189)
- **This Unlocks:** Understanding rotation as a building block for problems like search in rotated sorted array
