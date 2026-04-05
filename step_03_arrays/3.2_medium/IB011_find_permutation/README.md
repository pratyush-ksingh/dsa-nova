# Find Permutation

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given a string `S` of length `n-1` consisting of the characters `'D'` (decrease) and `'I'` (increase), find the **lexicographically smallest** permutation of integers `[1, 2, ..., n]` such that:

- `S[i] == 'I'` implies `perm[i] < perm[i+1]`
- `S[i] == 'D'` implies `perm[i] > perm[i+1]`

## Examples

| Input S | n | Output       | Explanation |
|---------|---|--------------|-------------|
| "I"     | 2 | [1, 2]       | 1 < 2 (increase) |
| "D"     | 2 | [2, 1]       | 2 > 1 (decrease) |
| "DI"    | 3 | [2, 1, 3]    | 2>1 (D), 1<3 (I) |
| "ID"    | 3 | [1, 3, 2]    | 1<3 (I), 3>2 (D) |
| "DDIID" | 6 | [3,2,1,4,6,5]| DD: 3>2>1, II: 1<4<6, D: 6>5 |
| "IDID"  | 5 | [1,3,2,5,4]  | I:1<3, D:3>2, I:2<5, D:5>4 |

## Constraints

- 1 <= |S| <= 10^5
- S consists only of 'D' and 'I'

---

## Approach 1: Brute Force

**Intuition:** Generate all permutations of [1..n] in lexicographic order (using next_permutation or itertools.permutations). Since they are generated in order, the first valid permutation is automatically the lexicographically smallest. Totally impractical beyond n=10 due to O(n!) growth.

**Steps:**
1. Generate all permutations of [1..n] in lexicographic order.
2. For each permutation, check all n-1 constraints.
3. Return the first valid one.

| Metric | Value |
|--------|-------|
| Time   | O(n! * n) |
| Space  | O(n) |

---

## Approach 2: Optimal (Stack-based)

**Intuition:** Consider what happens at a run of consecutive 'D's. For example "DDI": we need perm[0] > perm[1] > perm[2] < perm[3]. The smallest such values are [3, 2, 1, 4] — the next three consecutive integers in reverse, then the next integer ascending.

The stack captures this naturally:
- Push numbers 1, 2, 3, ... one by one.
- When we see 'I' (or reach the end), drain the stack entirely.
- The stack's LIFO nature reverses any accumulated run, producing a descending sequence.

**Steps:**
1. Create an empty stack and result list.
2. For i from 1 to n:
   a. Push i onto the stack.
   b. If `i == n` or `S[i-1] == 'I'`: pop the entire stack into result.
3. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## Approach 3: Best (Reverse D-segments)

**Intuition:** Start with the identity permutation [1, 2, ..., n]. This already satisfies all 'I' constraints since consecutive numbers are increasing. For each maximal run of 'D's at positions [i..j] in S (affecting elements [i..j+1] in the result), reverse that segment. Reversing a consecutive range of integers makes it descending, satisfying all D constraints. The identity [1..n] is the smallest possible starting point; we only reverse where forced by D's.

**Steps:**
1. Build `result = [1, 2, ..., n]`.
2. Scan S from left to right.
3. When a 'D' is found at index i, find the end of the D-run (index j).
4. Reverse `result[i .. j+1]` (inclusive on both ends).
5. Skip to index j and continue.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## Real-World Use Case

**Scheduling and sequencing problems:** When you need to arrange tasks with ordering constraints (task A must come before B, but C must come after D), this pattern of constructing the lexicographically smallest valid sequence appears in job scheduling, tournament bracket generation, and test case ordering. It also appears in **generating test permutations** for verifying sorting algorithms — you construct the hardest case for a given comparator pattern.

## Interview Tips

- The stack approach is the canonical interview solution — practice until you can write it from memory in 5 lines.
- Key insight for the stack: "we push numbers until we need to output them; 'I' forces output because we must go up next, so we flush the buffer."
- The reverse-segments approach (Best) is easier to explain intuitively — start with the smallest permutation, patch the D-runs.
- Always trace through "DI" and "ID" by hand before writing code — these small examples catch index-off-by-one errors.
- For the stack approach: be careful about when to drain — it should happen when `S[i-1] == 'I'` OR when `i == n` (last element).
- Follow-up: "lexicographically largest"? Answer: start with [n..1] (reverse identity) and reverse I-segments.
