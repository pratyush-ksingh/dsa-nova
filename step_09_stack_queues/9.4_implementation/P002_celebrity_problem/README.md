# Celebrity Problem

> **Step 09.9.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
In a party of `n` people (labeled 0 to n-1), a **celebrity** is defined as someone who is known by everyone else but does not know anyone. Given a helper function `knows(a, b)` that returns `true` if person `a` knows person `b`, find the celebrity. Return their index, or `-1` if no celebrity exists.

There can be at most one celebrity (if A and B are both celebrities, A must know B and B must know A, contradicting the definition).

**Examples:**

| Input (knows matrix) | Output | Explanation |
|-------|--------|-------------|
| `[[0,1,0],[0,0,0],[0,1,0]]` | `1` | Person 1 knows nobody; persons 0 and 2 know person 1 |
| `[[0,1,0],[0,0,1],[1,0,0]]` | `-1` | Everyone knows someone, no celebrity |
| `[[0,0,1,0],[0,0,1,0],[0,0,0,0],[0,0,1,0]]` | `2` | Person 2 is known by all and knows nobody |

**Constraints:**
- `1 <= n <= 1000`
- `knows(a, b)` is O(1)
- Minimize the number of calls to `knows()`

### Real-Life Analogy
> *At a Hollywood party, the celebrity is the person everyone recognizes but who does not recognize anyone else. To find them efficiently, you can compare two people: if A recognizes B, then A is not the celebrity. If A does not recognize B, then B is not the celebrity. Each comparison eliminates one person, so after n-1 comparisons you have a single candidate to verify.*

### Key Observations
1. Each call to `knows(a, b)` eliminates exactly one person as a potential celebrity.
2. With n-1 eliminations, we get a single candidate. Then we verify in O(n).
3. Total: 3(n-1) calls to `knows()` in the worst case.

---

## APPROACH LADDER

### Approach 1: Brute Force -- Check All Pairs
**Intuition:** For each person, check if they know nobody and everyone knows them. This requires O(n) checks per person, O(n^2) total.

**Steps:**
1. For each candidate from 0 to n-1:
   - Check if candidate knows anyone (scan row). If yes, skip.
   - Check if everyone knows candidate (scan column). If yes, return candidate.
2. If no candidate found, return -1.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1)  |

---

### Approach 2: Optimal -- Stack Elimination
**Intuition:** Push all n people onto a stack. Pop two at a time: if A knows B, discard A (not a celebrity). Otherwise, discard B. After n-1 rounds, one candidate remains. Verify it.

**Steps:**
1. Push 0 to n-1 onto stack.
2. While stack has more than one element:
   - Pop `a` and `b`.
   - If `knows(a, b)`: push `b` (a eliminated).
   - Else: push `a` (b eliminated).
3. Candidate = last remaining element.
4. Verify: candidate knows nobody AND everyone knows candidate.

**Dry Run:** n=3, celebrity=1, matrix `[[0,1,0],[0,0,0],[0,1,0]]`
```
Stack: [0, 1, 2]
Pop 2, 1: knows(2,1) = true -> push 1. Stack: [0, 1]
Pop 1, 0: knows(1,0) = false -> push 1. Stack: [1]
Candidate: 1
Verify: knows(1,0)=false, knows(1,2)=false, knows(0,1)=true, knows(2,1)=true -> CELEBRITY = 1
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) -- stack |

---

### Approach 3: Best -- Single Candidate Elimination (Two-Pointer Logic)
**Intuition:** We do not even need a stack. Start with candidate = 0. Scan through 1 to n-1: if candidate knows person i, update candidate to i (old candidate is eliminated). After one pass, we have the only possible celebrity. Then verify.

**Steps:**
1. Set `candidate = 0`.
2. For `i` from 1 to n-1:
   - If `knows(candidate, i)`: set `candidate = i`.
3. Verify candidate: check that candidate knows nobody and everyone knows candidate.
4. Return candidate if valid, else -1.

**Why it works:** If the real celebrity is `c`, then for all `i != c`, `knows(c, i) = false`, so candidate never moves away from `c` once it reaches `c`. And it must reach `c` because anyone before `c` will be eliminated when we check `knows(candidate, c) = true`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting to verify:** The elimination phase only finds a CANDIDATE. You must verify they actually know nobody and are known by everyone.
2. **Assuming a celebrity always exists:** Return -1 if verification fails.
3. **Self-comparison:** Do not check `knows(i, i)` -- skip when i == candidate.

### Edge Cases
- n = 1: the single person is the celebrity by default
- No celebrity exists: all n people know at least one other person
- Everyone knows everyone: no celebrity (celebrity must know nobody)

---

## Real-World Use Case
The elimination strategy is used in **leader election** in distributed systems -- processes compare pairwise, and one is eliminated. It also models **tournament-style selection** where each comparison eliminates a contender. The O(n) single-pass approach is a template for many "find the special element" problems.

## Interview Tips
- Start with brute force O(n^2) to show you understand the problem.
- Key insight: "Each `knows()` call eliminates one person." This leads directly to the O(n) solution.
- The stack approach and the linear scan approach are essentially the same algorithm -- the stack just makes the elimination more explicit.
- Mention that at most one celebrity can exist, and prove why.
- Follow-up: "What if knows() is expensive (network call)?" -- Minimize total calls. The O(n) approach uses ~3n calls, which is optimal.
