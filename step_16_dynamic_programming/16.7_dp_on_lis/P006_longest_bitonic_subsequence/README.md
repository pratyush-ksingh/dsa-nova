# Longest Bitonic Subsequence

> **Step 16.7** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an array `nums`, find the length of the **longest bitonic subsequence**. A subsequence is bitonic if it first strictly increases and then strictly decreases. A pure increasing or pure decreasing sequence does **not** count as bitonic (both phases must be present).

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [1,11,2,10,4,5,2,1] | 6 | [1,2,10,4,2,1] — up to 10, then down |
| [12,11,40,5,3,1] | 5 | [11,40,5,3,1] — up to 40, then down |
| [80,60,30] | 1 | Pure decreasing — no valid bitonic, return 1 |
| [1,2,3,4,5] | 1 | Pure increasing — no valid bitonic, return 1 |

### Constraints
- `1 <= nums.length <= 1000`
- `1 <= nums[i] <= 10^4`

---

## APPROACH LADDER

### Approach 1: Brute Force -- Try All Subsequences

**Intuition:** Generate every possible subsequence, check if it is bitonic (strictly increasing then strictly decreasing), and track the maximum length.

**Steps:**
1. For each subset of indices (2^n possibilities), extract the subsequence
2. Check bitonic: find the peak, verify increasing before peak and decreasing after; both phases must exist
3. Track maximum length across all valid bitonic subsequences
4. Return maximum (or 1 if none found)

| Metric | Value |
|--------|-------|
| Time   | O(2^n * n) |
| Space  | O(n) |

---

### Approach 2: Optimal -- LIS from Left + LIS from Right

**Intuition:** A bitonic subsequence has a peak element. For each element as the peak, compute:
- `lis[i]` = length of longest strictly increasing subsequence **ending at i** (left side of bitonic)
- `lds[i]` = length of longest strictly decreasing subsequence **starting at i** (right side of bitonic)

The answer is `max(lis[i] + lds[i] - 1)` over all valid peaks (where both sides exist, i.e., both > 1).

**Steps:**
1. Compute `lis[i]` left-to-right: for each i, check all j < i with `nums[j] < nums[i]`
   - `lis[i] = max(lis[j] + 1)` over valid j
2. Compute `lds[i]` right-to-left: for each i, check all j > i with `nums[j] < nums[i]`
   - `lds[i] = max(lds[j] + 1)` over valid j
3. For each i where `lis[i] > 1` AND `lds[i] > 1`: candidate = `lis[i] + lds[i] - 1`
4. Return max candidate (or 1 if no valid peak)

```
Dry-run: nums=[1,11,2,10,4,5,2,1]
         idx:  0   1  2   3  4  5  6  7

lis (L→R):    [1,  2, 2,  3, 3, 4, 2, 2]
  lis[5]=4: nums[0]<nums[5], nums[3]>nums[5] skip, nums[4]<nums[5]
  → lis[5] = max(lis[0]+1, lis[4]+1) = max(2, 4) = 4

lds (R→L):    [4,  4, 3,  3, 2, 3, 2, 1]
  lds[0]=4: nums[2]<nums[0]? No(2<1? no). nums[3]? No. nums[6]<nums[0]? yes lds[6]=2 → 3
            Actually: nums=[1,11,2,10,4,5,2,1], lds[0]: check j>0 where nums[j]<nums[0]=1 → none → lds[0]=1

  Corrected lds: [1, 4, 3, 3, 2, 3, 2, 1]
  Peak candidates:
    i=1: lis[1]=2>1, lds[1]=4>1 → 2+4-1=5
    i=3: lis[3]=3>1, lds[3]=3>1 → 3+3-1=5
    i=5: lis[5]=4>1, lds[5]=3>1 → 4+3-1=6 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n) |

---

### Approach 3: Best -- Same O(n^2) with Note on O(n log n)

**Intuition:** Identical algorithm to Approach 2 but noted: the LIS computation can be done in O(n log n) using patience sorting (binary search on a maintained array). However, combining two such LIS arrays for the bitonic problem requires careful reconstruction, so O(n^2) is the standard interview answer.

**Steps:** Same as Approach 2.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) -- O(n log n) possible with patience sorting |
| Space  | O(n) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| All equal elements | No valid peak | All lis=lds=1, return 1 |
| Pure increasing | lds[i] = 1 always | No valid peak, return 1 |
| Pure decreasing | lis[i] = 1 always | No valid peak, return 1 |
| Peak at first/last element | lis[0]=1 or lds[n-1]=1 | Both > 1 required |

**Common Mistakes:**
- Allowing pure increasing or pure decreasing (not bitonic); must require both `lis[i] > 1` AND `lds[i] > 1`
- Computing `lds` with the wrong direction or comparison sign
- Off-by-one: the peak is counted once so subtract 1 from the combined sum

---

## Real-World Use Case
**Mountain profile detection:** In terrain analysis, a bitonic subsequence models a mountain-shaped elevation profile (ascending then descending). Finding the longest such subsequence in GPS data identifies the most prominent mountain ridge in a route — useful in topographic mapping and hiking app features.

## Interview Tips
- Frame it as "two LIS problems": LIS ending at each element + LIS starting at each element
- The condition `lis[i] > 1 AND lds[i] > 1` is crucial — easy to miss, always mention it
- Relate to: Longest Mountain in Array (array must be contiguous), vs this (subsequence, non-contiguous)
- If asked about O(n log n): yes, patience sorting can do LIS in O(n log n), but reconstructing the count for bitonic requires extra work
- Common variant: count number of bitonic subsequences (use count arrays similar to Number of LIS)
