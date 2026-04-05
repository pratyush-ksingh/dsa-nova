# Majority Element II

> **Step 03 | 3.3** | **Difficulty:** HARD | **XP:** 40 | **LeetCode:** #229 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an integer array `nums` of size `n`, return all elements that appear **more than** `floor(n/3)` times.

The result can have **at most 2 elements** (since at most 2 values can each appear more than n/3 times).

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[3, 2, 3]` | `[3]` | 3 appears 2 times, n/3 = 1, 2 > 1 |
| `[1]` | `[1]` | Only element; 1 > 0 |
| `[1, 2]` | `[1, 2]` | Both appear once, n/3=0, 1 > 0 |
| `[1,1,1,3,3,2,2,2]` | `[1, 2]` | n=8, threshold=2; 1 appears 3x, 2 appears 3x |
| `[1, 2, 3, 4]` | `[]` | n=4, need > 1 occurrence; each appears once |

**Constraints:**
- `1 <= nums.length <= 5 * 10^4`
- `-10^9 <= nums[i] <= 10^9`

### Real-Life Analogy
> *Imagine an election with 3 parties. For a party to win a "majority" they need more than 1/3 of the votes. At most 2 parties can achieve this. The Boyer-Moore voting algorithm is like a debate hall: when two different candidates appear, they cancel each other out. The candidates left standing after all the cancellations are the potential majority holders -- you then verify them by counting votes.*

### Key Observations
1. There are at most 2 elements appearing more than n/3 times (pigeonhole: if 3 each appeared > n/3 times, total would exceed n).
2. A HashMap approach gives O(n) time but O(n) space. Boyer-Moore achieves O(1) space.
3. Boyer-Moore needs 2 candidates (not 1 as in the n/2 variant). The key insight: every time we see 3 different values, we cancel one of each. The majority candidates survive.
4. **Verification is mandatory:** After phase 1, the 2 survivors are only potential candidates. They must be verified in a second pass.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- Brute force: no extra space, but O(n^2).
- Hash map: O(n) time, O(n) space. Good but can we do O(1)?
- Boyer-Moore Extended: O(n) time, O(1) space using just 4 variables.

### Pattern Recognition
- **Pattern:** Boyer-Moore Extended Voting / Majority Vote
- **Classification Cue:** "Find elements appearing > n/k times --> maintain k-1 candidates with Boyer-Moore extended."

---

## APPROACH LADDER

### Approach 1: Brute Force (Count each element)
**Idea:** For each unique element, count its total occurrences. If count > n/3, add it to the result.

**Steps:**
1. Maintain a `seen` set to avoid recounting.
2. For each element `x` not yet seen:
   - Count how many times `x` appears in `nums`.
   - If count > n/3, add to result.
3. Return sorted result.

**Why it works:** Directly checks the condition for every element.

**Why we move on:** O(n^2) due to nested counting. Can use a hash map for O(n) count but O(n) space. Boyer-Moore achieves O(1) space.

| Time | Space |
|------|-------|
| O(n^2) | O(n) |

---

### Approach 2: Optimal (Boyer-Moore Extended - 2 candidates)
**What changed:** Maintain only 2 candidates with their vote counts. When we see a 3rd distinct value and both counts are positive, decrement both (triple cancellation).

**Steps:**

**Phase 1 (Candidate Selection):**
1. `cand1, cand2 = None, None`; `cnt1, cnt2 = 0, 0`.
2. For each `num`:
   - If `num == cand1`: `cnt1 += 1`.
   - Else if `num == cand2`: `cnt2 += 1`.
   - Else if `cnt1 == 0`: `cand1 = num, cnt1 = 1`.
   - Else if `cnt2 == 0`: `cand2 = num, cnt2 = 1`.
   - Else: `cnt1 -= 1, cnt2 -= 1` (cancel out 3 different elements).

**Phase 2 (Verification):**
3. Count exact occurrences of `cand1` and `cand2` in `nums`.
4. Add to result if count > n/3.

**Dry Run:** `[1, 1, 1, 3, 3, 2, 2, 2]`, n=8

| num | cand1 | cnt1 | cand2 | cnt2 | Action |
|-----|-------|------|-------|------|--------|
| 1   | 1     | 1    | None  | 0    | cand1=1 |
| 1   | 1     | 2    | None  | 0    | cnt1++ |
| 1   | 1     | 3    | None  | 0    | cnt1++ |
| 3   | 1     | 3    | 3     | 1    | cand2=3 |
| 3   | 1     | 3    | 3     | 2    | cnt2++ |
| 2   | 1     | 2    | 3     | 1    | cnt1--, cnt2-- |
| 2   | 1     | 1    | 3     | 0    | cnt1--, cnt2-- |
| 2   | 1     | 1    | 2     | 1    | cand2=2 |

Candidates: 1 and 2. Verify: 1 appears 3x (>8/3=2 YES), 2 appears 3x (>2 YES), 3 appears 2x (not >2). **Result: [1, 2]**

| Time | Space |
|------|-------|
| O(n) | O(1) |

---

### Approach 3: Best (Same Boyer-Moore, single verification loop)
**What changed:** The verification phase uses a single explicit loop (counting both candidates simultaneously) rather than two separate `.count()` calls. Slightly fewer constant-factor operations; same Big-O. This is the canonical optimal solution.

*This IS the best known solution for this problem: O(n) time, O(1) space.*

| Time | Space |
|------|-------|
| O(n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We traverse the array at most 3 times total (1 selection pass + 1 or 2 verification passes). Each pass is O(n)."
**Space:** O(1) -- "Only 4 variables for candidates and counts, regardless of n."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting the verification phase:** After Boyer-Moore phase 1, the candidates might NOT actually appear > n/3 times. For example, `[1, 2, 3]` -- after phase 1 some candidates survive but none appear > 1 time. Always verify.
2. **Comparing candidates with uninitialized sentinel:** In Java, using `Integer.MIN_VALUE` as "no candidate" can cause bugs if the actual array contains `Integer.MIN_VALUE`. Use `null` (boxed Integer) or a boolean flag instead.
3. **Order of conditions in phase 1:** Check `num == cand1` BEFORE checking `cnt1 == 0`. If you check `cnt1 == 0` first and cnt1 is currently 0, you would overwrite cand1 even if num should have matched cand2.
4. **Returning duplicates:** If `cand1 == cand2` after phase 1 (can happen in some implementations), ensure you only add once.

### Edge Cases to Test
- [ ] All same elements: `[1,1,1]` --> `[1]`
- [ ] Two distinct elements equally split: `[1,2,1,2]` --> both appear > n/3=1 time
- [ ] Three or more distinct elements, none qualifying: `[1,2,3,4]`
- [ ] n = 1: single element always qualifies
- [ ] Negative numbers: `[-1, -1, 2]` --> `-1`

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Return all elements appearing more than floor(n/3) times. At most 2 such elements exist."
2. **Brute approach:** "Count each element -- O(n^2) with O(1) space. Or use hash map: O(n) time, O(n) space."
3. **Optimal approach:** "Boyer-Moore extended for 2 candidates. Maintain 2 candidates and cancel when 3 different values meet. Then verify in a second pass."
4. **Key point to say:** "Verification is critical -- the candidates after phase 1 are only potential majority elements."
5. **Test:** Walk through `[1,1,1,3,3,2,2,2]` showing the candidate table.

### Follow-Up Questions
- "What about elements appearing > n/k times?" --> Generalize to k-1 candidates; same Boyer-Moore extended.
- "Can you do it in a single pass?" --> No -- you must verify after phase 1, which requires a 2nd pass. A single pass is not sufficient to guarantee correctness.
- "Why does Boyer-Moore work?" --> Key invariant: each cancellation step removes one occurrence of 3 distinct elements. Any element appearing > n/3 times cannot be fully cancelled out.

---

## CONNECTIONS
- **Prerequisite:** Majority Element I (LC #169) -- Boyer-Moore for n/2
- **Same Pattern:** Boyer-Moore Voting Algorithm
- **Harder Variant:** Majority Element for n/k (generalize to k-1 candidates)
- **This Unlocks:** Frequency-based problems, voting algorithms, streaming data analysis
