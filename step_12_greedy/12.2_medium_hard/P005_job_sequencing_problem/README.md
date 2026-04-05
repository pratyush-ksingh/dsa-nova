# Job Sequencing Problem

> **Step 12 | 12.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given `N` jobs, each with an `id`, `deadline`, and `profit`, find a sequence of jobs that maximizes total profit. Each job takes exactly **1 unit of time**, and a job must be completed **at or before** its deadline. You can execute at most one job per time unit.

Return `[number_of_jobs_done, max_profit]`.

**Constraints:**
- `1 <= N <= 10^5`
- `1 <= deadline[i] <= N`
- `1 <= profit[i] <= 10^4`

**Examples:**

| Input (id, deadline, profit) | Output | Explanation |
|------------------------------|--------|-------------|
| `[(1,4,20),(2,1,10),(3,1,40),(4,1,30)]` | `[2, 60]` | Job3 at t=1 (profit 40), Job1 at t=4 (profit 20) |
| `[(1,2,100),(2,1,19),(3,2,27),(4,1,25),(5,1,15)]` | `[2, 127]` | Job1 at t=2 (100), Job3 at t=1 (27) |
| `[(1,1,5),(2,1,10)]` | `[1, 10]` | Only 1 slot before deadline 1; take profit 10 |
| `[(1,3,50),(2,2,30),(3,1,20)]` | `[3, 100]` | All fit: t=3, t=2, t=1 |

### Real-Life Analogy
> *You are a freelancer with N projects, each paying a fixed fee if delivered before a deadline. You can only work on one project per day. The greedy insight: always pick the most profitable project you haven't scheduled yet, and slot it into the latest available day before its deadline. This keeps earlier slots open for other projects with tighter deadlines.*

### Key Observations
1. We want to maximize profit, so we should prioritize high-profit jobs.
2. When scheduling a job, we should place it at the **latest available slot** before its deadline — this preserves earlier slots for jobs with tighter deadlines.
3. The greedy choice (sort by profit desc, place at latest free slot) is provably optimal.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why Greedy?
- Greedy correctness proof (exchange argument): suppose an optimal solution skips a high-profit job J in favor of a lower-profit job J'. We can swap J' for J in the schedule and get equal or better profit — contradiction.
- Placing at the **latest** free slot is critical: it avoids "blocking" earlier slots needed by jobs with smaller deadlines.

### Pattern Recognition
- **Pattern:** Greedy scheduling with deadline constraint — classic activity selection variant.
- **Classification Cue:** "Maximize profit subject to deadline with unit-time jobs → sort by profit desc, assign to latest available slot."

---

## APPROACH LADDER

### Approach 1: Brute Force — Try All Permutations
**Idea:** Try every possible ordering of jobs. For each permutation, greedily assign each job to its earliest available slot. Track the permutation yielding the best profit.

**Steps:**
1. Generate all N! permutations of job indices.
2. For each permutation, simulate scheduling: for each job in order, find the first free slot at or before its deadline.
3. Track max profit and corresponding count.

**BUD Transition — Bottleneck:** O(N!) is completely infeasible for N > 12. We need to exploit the greedy property.

| Metric | Value |
|--------|-------|
| Time   | O(n! * n) |
| Space  | O(n) |

---

### Approach 2: Optimal — Sort by Profit, Assign to Latest Free Slot
**What changed:** Greedy insight. Sort jobs by profit descending. For each job, scan from its deadline down to slot 1 for the latest free slot.

**Steps:**
1. Sort jobs by `profit` descending.
2. Compute `max_deadline` = maximum deadline across all jobs. Initialize a `slots` boolean array of size `max_deadline + 1`.
3. For each job in sorted order:
   - Scan from `min(deadline, max_deadline)` down to 1.
   - At the first free slot `t`, mark it occupied and add the profit.
4. Return `(count, total_profit)`.

**Dry Run:** Jobs sorted by profit: (3,1,40), (4,1,30), (1,4,20), (2,1,10)
- Job3 (d=1,p=40): scan slot 1 → free → assign. slots=[F,T,F,F,F]
- Job4 (d=1,p=30): scan slot 1 → occupied → no slot found. Skip.
- Job1 (d=4,p=20): scan slot 4 → free → assign. slots=[F,T,F,F,T]
- Job2 (d=1,p=10): scan slot 1 → occupied → skip.
- Result: (2, 60)

| Metric | Value |
|--------|-------|
| Time   | O(n²) in worst case (each job scans up to n slots) |
| Space  | O(n) |

---

### Approach 3: Best — Greedy + Union-Find for O(α(n)) Slot Lookup
**What changed:** Replace the O(n) linear scan for a free slot with Union-Find path compression. Each slot index `t` has a `parent[t]`. After using slot `t`, set `parent[t] = t - 1`. `find(t)` then returns the latest free slot at or before `t` in near-O(1) amortized time.

**Steps:**
1. Sort jobs by profit descending.
2. Initialize `parent[i] = i` for all `i` from 0 to `max_deadline`.
3. For each job with deadline `d`:
   - `free_slot = find(d)` — returns latest free slot ≤ d.
   - If `free_slot > 0`: assign job, add profit, then `parent[free_slot] = free_slot - 1`.
4. Return `(count, total_profit)`.

**Union-Find invariant:** `find(t)` always returns the largest free slot at or before `t`. When slot `t` is used, we point it to `t-1`, so future `find(t)` skips directly over used slots.

| Metric | Value |
|--------|-------|
| Time   | O(n log n + n * α(n)) ≈ O(n log n) |
| Space  | O(n) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Scanning for the **earliest** free slot instead of the **latest** — this blocks earlier slots needed by future jobs with tighter deadlines.
2. Not capping deadline at `max_deadline` when the deadline array may contain values larger than needed.
3. Union-Find: forgetting the `free_slot > 0` guard — slot 0 is a sentinel for "no free slot available."

### Edge Cases to Test
- [ ] All jobs have the same deadline
- [ ] Only one slot available (max_deadline = 1)
- [ ] All jobs can fit (deadlines spread across N slots)
- [ ] Large profits on jobs with early deadlines
- [ ] N = 1

---

## Real-World Use Case
**Cloud task scheduling:** Cloud platforms (AWS Lambda, Google Cloud Tasks) must schedule compute jobs with deadlines and associated revenue. The greedy job-sequencing algorithm determines which jobs to accept and when to schedule them to maximize revenue — used in SLA-based scheduling systems where each task takes one time unit (one server slot).

## Interview Tips
- Clearly state the greedy strategy: "sort by profit descending, assign to the latest free slot."
- Prove correctness with an exchange argument: replacing any lower-profit job with a higher-profit one never worsens the solution.
- Approach 2 (O(n²)) is sufficient for most interviews. Mention Approach 3 (Union-Find) to stand out.
- Distinguish this from the "Activity Selection Problem" (maximize number of jobs, not profit).
- Common follow-up: "What if jobs have different durations?" — this becomes NP-hard.
