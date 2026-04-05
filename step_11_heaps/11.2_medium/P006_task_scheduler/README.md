# Task Scheduler

> **Step 11.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

You are given a list of CPU `tasks` (each task is a character `'A'` to `'Z'`) and an integer `n` representing the **cooldown period** between two same tasks.

The CPU can perform one task per unit of time. Between two executions of the **same task**, the CPU must wait at least `n` units. During the wait, it can execute other tasks or remain **idle**.

Return the **minimum number of intervals** required to finish all tasks.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `tasks=["A","A","A","B","B","B"], n=2` | `8` | A→B→idle→A→B→idle→A→B |
| `tasks=["A","A","A","B","B","B"], n=0` | `6` | No cooldown; just run all 6 tasks sequentially |
| `tasks=["A","A","A","A","A","A","B","C","D","E","F","G"], n=2` | `16` | A→B→C→A→D→E→A→F→G→A→idle→idle→A→idle→idle→A |

## Constraints

- `1 <= tasks.length <= 10^4`
- `tasks[i]` is an uppercase English letter
- `0 <= n <= 100`

---

## Approach 1: Brute Force — Simulation

**Intuition:** Directly simulate the CPU scheduler. At each time unit, always pick the most frequent task that is not on cooldown. Use a max-heap to get the best task and a cooldown queue to track when tasks become available again.

**Steps:**
1. Count frequency of each task; push counts into a max-heap.
2. Maintain a cooldown queue of `(available_time, remaining_count)` pairs.
3. At each time unit:
   - Release any tasks whose cooldown has expired back into the heap.
   - Pop the highest-frequency task from the heap; decrement its count.
   - If it still has remaining executions, enqueue with `available_time = current + n + 1`.
   - If heap is empty but cooldown queue is not, CPU idles this unit.
4. Count total time units until both heap and queue are empty.

| Metric | Value |
|--------|-------|
| Time   | O(T × 26) where T = answer (total intervals) |
| Space  | O(26) |

---

## Approach 2: Optimal — Math Formula

**Intuition:** The task with the highest frequency dictates the schedule. Arrange it in "frames" of size `(n+1)`:

```
For tasks=[A,A,A,B,B,B], n=2:
  Frame 1: [ A  B  _ ]
  Frame 2: [ A  B  _ ]
  Last:    [ A  B ]
  Total = (3-1)*(2+1) + 2 = 8
```

The formula `(maxFreq - 1) * (n + 1) + countOfMax` gives the minimum time needed to schedule the most frequent tasks with proper cooldown. But if there are enough diverse tasks to fill every slot, no idle time is needed and the answer is simply `len(tasks)`.

**Steps:**
1. Count frequencies of all tasks.
2. Find `maxFreq` = highest frequency.
3. Find `countOfMax` = how many tasks share that frequency.
4. Return `max(len(tasks), (maxFreq - 1) * (n + 1) + countOfMax)`.

| Metric | Value |
|--------|-------|
| Time   | O(26) = O(1) since only 26 possible task types |
| Space  | O(26) = O(1) |

---

## Approach 3: Best — Sorted Frequencies

**Intuition:** Same formula as Approach 2, but use sorting to find `maxFreq` and count ties cleanly. Sorting 26 elements is effectively O(1).

**Steps:**
1. Count frequencies and sort them in descending order.
2. `maxFreq = counts[0]`, `countOfMax` = number of leading elements equal to `maxFreq`.
3. Apply `max(len(tasks), (maxFreq - 1) * (n + 1) + countOfMax)`.

| Metric | Value |
|--------|-------|
| Time   | O(26 log 26) = O(1) |
| Space  | O(26) = O(1) |

---

## Real-World Use Case

**Operating System Process Scheduling:** Modern OS schedulers must respect thermal or affinity constraints — a CPU core running the same heavy workload back-to-back can overheat. The task scheduler formula directly models how to compute minimum execution time with mandatory cooldowns between identical workloads.

Also used in **rate-limited API call scheduling**: you can call the same endpoint at most once per `n` seconds; this formula tells you the minimum total time to execute a batch of API calls.

## Interview Tips

- The brute force simulation is easy to reason about but can be slow if the answer is large; always pivot to the math approach.
- The key insight for the formula: think in "frames" of size `(n+1)`. Each frame reserves one slot for the most frequent task.
- Explain `max(len(tasks), frame_formula)`: if tasks are diverse enough they fill all idle slots naturally (answer = total tasks); otherwise idle time is needed (answer = frame formula).
- `countOfMax` matters: if tasks A, B, C all appear 3 times with n=2, the last frame has 3 tasks not 1, so answer is `(3-1)*3 + 3 = 9` not 7.
- Edge case: `n=0` means no cooldown, answer is always `len(tasks)`.
- The formula is provably optimal — mention this to show depth.
- Follow-up: "What if tasks have priorities?" — weighted task scheduling, significantly harder.
