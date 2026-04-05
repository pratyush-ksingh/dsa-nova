# Amazing Subarrays

> **Batch 4 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a string `S`, count the number of **amazing substrings** of `S`. A substring is amazing if it starts with a vowel (`a, e, i, o, u`, case-insensitive). Return the count modulo `10007`.

### Examples

| Input          | Output | Explanation                                                  |
|----------------|--------|--------------------------------------------------------------|
| `"ABEC"`       | 6      | Vowel-starting substrings: A, AB, ABE, ABEC, E, EC = 6      |
| `"AAA"`        | 6      | Index 0: AAA, AA, A (3); Index 1: AA, A (2); Index 2: A (1) = 6 |
| `"BCD"`        | 0      | No character is a vowel, so zero amazing substrings          |

### Analogy
Imagine you are standing at each character in a line of people. If someone at position `i` is wearing a "vowel badge," every group starting from them to any point to their right counts as an "amazing group." So from position `i`, there are exactly `(n - i)` such groups.

### 3 Key Observations
1. **"Aha" -- Each vowel contributes a predictable count.** If character at index `i` is a vowel, it starts `(n - i)` substrings (length 1, 2, ..., up to `n - i`). We never need to enumerate the actual substrings.
2. **"Aha" -- Case insensitivity matters.** Convert to lowercase (or check both cases) before the vowel check. Forgetting this is a common mistake.
3. **"Aha" -- Modulo arithmetic.** The problem asks for result mod 10007. Apply modulo at each addition to prevent overflow (especially relevant in Java with int).

---

## DS & ALGO CHOICE

| Approach         | Data Structure | Algorithm               | Why?                                      |
|------------------|---------------|-------------------------|--------------------------------------------|
| Brute Force      | None          | Double loop, enumerate  | Check every substring explicitly           |
| Optimal          | None          | Single pass, math count | Each vowel at index i gives (n-i) subarrays|
| Best             | None          | Same + early mod        | Identical logic, tighter constant factor   |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Enumerate All Substrings
**Intuition:** Generate every substring, check if it starts with a vowel, increment counter.

**BUD Analysis:**
- **B**ottleneck: Generating O(n^2) substrings when we only need the first character of each.
- **U**nnecessary work: We build full substrings but only inspect the first character.
- **D**uplicated work: Re-checking the same starting character for different lengths.

**Steps:**
1. For each index `i` from 0 to n-1:
   - For each index `j` from `i` to n-1:
     - Extract substring `S[i..j]`
     - If `S[i]` is a vowel, increment count
2. Return `count % 10007`

**Dry-Run Trace** with `"ABEC"`:
```
i=0: S[0]='A' (vowel) -> j=0,1,2,3 -> count += 4  -> count=4
i=1: S[1]='B' (not vowel) -> skip all j  -> count=4
i=2: S[2]='E' (vowel) -> j=2,3 -> count += 2  -> count=6
i=3: S[3]='C' (not vowel) -> skip  -> count=6
Answer: 6 % 10007 = 6
```

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(1)   |

---

### Approach 2: Optimal -- Single Pass Counting
**Intuition:** We realized in brute force that for each vowel at position `i`, every ending position `j >= i` gives a valid substring. That is exactly `(n - i)` substrings. No need for the inner loop.

**BUD Analysis:**
- Eliminated the inner loop entirely.
- Pure O(n) scan with constant-time contribution per character.

**Steps:**
1. Initialize `count = 0`.
2. Define a vowel set: `{a, e, i, o, u, A, E, I, O, U}`.
3. For each index `i` from 0 to n-1:
   - If `S[i]` is a vowel, add `(n - i)` to `count`.
   - Take `count %= 10007`.
4. Return `count`.

**Dry-Run Trace** with `"ABEC"` (n=4):
```
i=0: 'A' is vowel -> count += (4-0) = 4 -> count=4
i=1: 'B' not vowel -> skip -> count=4
i=2: 'E' is vowel -> count += (4-2) = 2 -> count=6
i=3: 'C' not vowel -> skip -> count=6
Answer: 6 % 10007 = 6
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

### Approach 3: Best -- Identical to Optimal (This IS the Floor)
**Intuition:** The optimal approach is already O(n) time and O(1) space. There is no way to beat a single scan since we must inspect every character at least once. The best we can do is tighten constants: use a lookup array instead of a set for the vowel check.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## COMPLEXITY INTUITIVELY

- **Why O(n) is optimal:** We must look at every character at least once to know if it is a vowel. One pass is both necessary and sufficient.
- **Why O(1) space:** We only track a running count and a fixed-size vowel set. No auxiliary arrays grow with input.

---

## EDGE CASES & MISTAKES

| Edge Case              | What Happens                                      |
|------------------------|--------------------------------------------------|
| Empty string `""`      | Return 0. No characters, no substrings.          |
| All vowels `"AEIOU"`   | Sum = 5+4+3+2+1 = 15.                            |
| No vowels `"BCDFG"`    | Return 0.                                        |
| Single character vowel  | Return 1 % 10007 = 1.                           |
| Mixed case `"aBeIo"`   | Case-insensitive check must handle both.         |
| Large n (mod overflow)  | Apply `% 10007` at each step, not just the end.  |

**Common Mistakes:**
- Forgetting case-insensitivity (checking only lowercase).
- Not applying modulo, leading to overflow in languages with fixed-width integers.
- Off-by-one: using `(n - i - 1)` instead of `(n - i)`.

---

## INTERVIEW LENS

- **Why interviewers ask this:** Tests ability to spot the mathematical shortcut behind substring counting. The jump from O(n^2) to O(n) reveals whether you think about contributions per element.
- **Follow-ups:**
  - "What if we need subarrays ending with a vowel?" (Same idea: vowel at index `i` contributes `i + 1` subarrays.)
  - "What if we need subarrays that both start and end with a vowel?" (Two-pointer or prefix-sum of vowel indices.)
- **Communication tip:** State the key insight early: "Each vowel at position i contributes (n-i) substrings" and confirm with the interviewer before coding.

---

## CONNECTIONS

| Related Problem               | How It Connects                                        |
|-------------------------------|--------------------------------------------------------|
| Count Vowels in Substrings    | Same "contribution per index" technique                |
| Subarray Sum Equals K         | Counting subarrays via cumulative property             |
| Number of Substrings with 1s  | Contribution-based counting in a single scan           |

---

## Real-World Use Case
**Text analytics / Search engines:** Counting substrings matching a pattern (e.g., starting with a specific prefix) is the backbone of autocomplete systems. The "contribution per index" trick is used in suffix-array based text indexes to count pattern occurrences efficiently.
