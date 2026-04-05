# Implement Queue using Stack

> **Batch 2 of 12** | **Topic:** Stacks & Queues | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Implement a first-in-first-out (FIFO) queue using only stack operations. The implemented queue should support:
- `push(x)` -- Push element x to the back of the queue.
- `pop()` -- Remove and return the front element.
- `peek()` -- Return the front element without removing.
- `empty()` -- Return whether the queue is empty.

You may only use standard stack operations: `push` (to top), `pop` (from top), `peek` (top element), `size`, and `isEmpty`.

**LeetCode #232**

**Constraints:**
- `1 <= x <= 9`
- At most 100 calls to push, pop, peek, and empty
- All calls to pop and peek are valid (queue is non-empty)

**Examples:**

| Operations | Returns | Queue State (front...rear) |
|------------|---------|---------------------------|
| `push(1)` | - | [1] |
| `push(2)` | - | [1, 2] |
| `peek()` | `1` | [1, 2] |
| `pop()` | `1` | [2] |
| `empty()` | `false` | [2] |

### Real-Life Analogy
> *Imagine two buckets of tennis balls (stacks). You receive balls into the "inbox" bucket. When someone asks for the oldest ball, you pour the entire inbox bucket into the "outbox" bucket -- this reverses the order so the first ball in is now on top of the outbox. You serve from the outbox. Only when the outbox is empty do you pour again. This lazy transfer is why amortized cost is O(1).*

### Key Observations
1. A stack reverses order. Two stacks reverse it twice, restoring FIFO order. The "inbox" stack collects new elements; the "outbox" stack serves them in queue order. <-- This is the "aha" insight
2. The amortized O(1) trick: only transfer from inbox to outbox when outbox is EMPTY. Each element is moved at most twice in its lifetime (once into inbox, once into outbox), so total cost across n operations is O(n), giving amortized O(1) per operation.
3. The brute-force approach (transfer on every pop) does O(n) per pop. The lazy transfer avoids redundant moves.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- The problem constrains us to **only stack operations**. Two stacks can simulate a queue because two reversals cancel out.
- The "lazy transfer" optimization makes this practically efficient -- most pops are O(1).

### Pattern Recognition
- **Pattern:** Data Structure Simulation with Amortized Analysis
- **Classification Cue:** "Implement queue using stacks --> two stacks (inbox/outbox) with lazy transfer for amortized O(1)."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Single Stack, Costly Pop
**Idea:** Use one main stack and one temporary stack. For pop, transfer all elements to the temp stack (reversing order), pop the top (which is the queue front), then transfer everything back.

**Steps:**
1. `push(x)`: Push x onto main stack. O(1).
2. `pop()`:
   - Move all from main to temp (reverses order).
   - Pop from temp (this is the oldest element).
   - Move everything back from temp to main (restore order).
   - O(n) every time.
3. `peek()`: Same as pop but push the element back. O(n).
4. `empty()`: Return `main.isEmpty()`. O(1).

**BUD Transition -- Unnecessary Work:** We transfer ALL elements back after every pop. What if we kept them reversed and only transferred when needed?

| Time | Space |
|------|-------|
| push O(1), pop O(n), peek O(n) | O(n) |

### Approach 2: Optimal -- Two Stacks, Eager Transfer on Pop
**What changed:** Use inbox (for pushes) and outbox (for pops). On pop, if outbox is empty, transfer all from inbox to outbox. Do NOT transfer back -- elements stay in outbox for future pops.

**Steps:**
1. `push(x)`: Push onto inbox. O(1).
2. `pop()`:
   - If outbox is empty, move all from inbox to outbox.
   - Pop from outbox.
   - Amortized O(1).
3. `peek()`:
   - If outbox is empty, move all from inbox to outbox.
   - Peek at outbox top.
   - Amortized O(1).
4. `empty()`: Return `inbox.isEmpty() && outbox.isEmpty()`. O(1).

**Dry Run:** push(1), push(2), pop(), push(3), pop(), pop()

| Operation | Inbox (top...bot) | Outbox (top...bot) | Transfer? | Returned |
|-----------|-------------------|--------------------|-----------|----------|
| push(1) | [1] | [] | no | - |
| push(2) | [2, 1] | [] | no | - |
| pop() | [] | [1, 2] | yes: inbox->outbox | 1 |
| push(3) | [3] | [2] | no | - |
| pop() | [3] | [] | no (outbox has 2) | 2 |
| pop() | [] | [3] | yes: inbox->outbox | 3 |

**Amortized analysis:** Each element is pushed into inbox once and popped from inbox once (during transfer), then pushed into outbox once and popped from outbox once. Total: 4 stack operations per element across its lifetime. So n queue operations cost O(n) stack operations total --> amortized O(1) per queue operation.

| Time | Space |
|------|-------|
| push O(1), pop amortized O(1), peek amortized O(1) | O(n) |

### Approach 3: Best -- Same as Optimal (Amortized O(1) is the theoretical best)
**What changed:** The two-stack lazy transfer IS the best known approach. You cannot achieve worst-case O(1) for all operations using only stacks.

**Enhancement -- track front during push:** To make `peek()` O(1) even without transfer, cache the front element. Update it during push (when inbox is empty, the pushed element is the new front).

**Steps:**
1. Maintain a `front` variable.
2. `push(x)`: If inbox is empty, `front = x`. Push onto inbox.
3. `peek()`: If outbox is not empty, return outbox top. Else return `front`. O(1) worst-case.
4. `pop()`: Same as Optimal Approach 2.

| Time | Space |
|------|-------|
| push O(1), pop amortized O(1), peek O(1) worst-case | O(n) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** Amortized O(1) per operation -- "Each element makes exactly two trips: inbox->outbox (once) and out of outbox (once). Over n operations, total work is O(n), so each operation averages O(1)."
**Space:** O(n) -- "All n elements are stored across the two stacks."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Transferring back after pop:** The whole point of the two-stack approach is to NOT transfer back. Elements stay in outbox.
2. **Transferring when outbox is NOT empty:** Only transfer when outbox is empty. Otherwise you break the FIFO order of elements already in outbox.
3. **Forgetting empty() must check BOTH stacks:** The queue is empty only when both inbox and outbox are empty.

### Edge Cases to Test
- [ ] Push then immediately pop (single element)
- [ ] Multiple pushes, then multiple pops (batch behavior)
- [ ] Interleaved push and pop operations
- [ ] Pop when outbox is not empty (no transfer needed)
- [ ] Peek then pop (peek should not consume the element)
- [ ] empty() after draining all elements

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to implement a FIFO queue using only LIFO stack operations."
2. **Match:** "Two stacks: one for input, one for output. Double reversal restores FIFO order."
3. **Plan:** "Push goes to inbox. Pop checks outbox first -- if empty, transfer all from inbox. This lazy transfer gives amortized O(1)."
4. **Implement:** Write the two-stack version. Emphasize the `if outbox.empty()` guard before transfer.
5. **Review:** Dry-run push(1), push(2), pop(), push(3), pop() showing transfers.
6. **Evaluate:** "Amortized O(1) per operation, O(n) space. This is optimal for stack-based queue simulation."

### Follow-Up Questions
- "Can you prove the amortized O(1) claim?" --> Each element is pushed/popped exactly twice (once per stack). Total cost for n elements is 4n = O(n).
- "What about worst-case O(1)?" --> Not possible with standard stacks. Would need a persistent data structure.
- "What if you need thread safety?" --> Use locks on both stacks, or use a lock-free approach with atomic operations.

---

## CONNECTIONS
- **Prerequisite:** Stack operations, Queue ADT concept
- **Same Pattern:** Implement Stack using Queue (P005)
- **Harder Variant:** Design Circular Deque (LC #641), Design Hit Counter (LC #362)
- **This Unlocks:** Amortized analysis thinking -- the same technique appears in dynamic arrays, splay trees, and union-find
