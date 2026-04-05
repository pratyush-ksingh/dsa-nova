# Implement Stack using Queue

> **Batch 2 of 12** | **Topic:** Stacks & Queues | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Implement a last-in-first-out (LIFO) stack using only queue operations. The implemented stack should support:
- `push(x)` -- Push element x onto the stack.
- `pop()` -- Remove and return the top element.
- `top()` -- Return the top element without removing.
- `empty()` -- Return whether the stack is empty.

You may only use standard queue operations: `enqueue` (push to back), `dequeue` (pop from front), `peek` (front element), `size`, and `isEmpty`.

**LeetCode #225**

**Constraints:**
- `1 <= x <= 9`
- At most 100 calls to push, pop, top, and empty
- All calls to pop and top are valid (stack is non-empty)

**Examples:**

| Operations | Returns | Stack State (top...bottom) |
|------------|---------|---------------------------|
| `push(1)` | - | [1] |
| `push(2)` | - | [2, 1] |
| `top()` | `2` | [2, 1] |
| `pop()` | `2` | [1] |
| `empty()` | `false` | [1] |

### Real-Life Analogy
> *Imagine you have a single-file tunnel (queue) where people can only enter from the back and exit from the front. You want to simulate a stack (last person in should be first out). When a new person enters, you make everyone already in the tunnel exit from the front and re-enter from the back, so the new person ends up at the front of the tunnel. Now the "most recent" person is always at the exit. This is the costly-push approach.*

### Key Observations
1. A queue is FIFO, a stack is LIFO. The key trick is to **rotate** elements so the most recently added element reaches the front of the queue. <-- This is the "aha" insight
2. You can make EITHER push costly (rotate after each push) OR pop costly (rotate during each pop). Making push costly is simpler because the "top" element is always at the queue front.
3. With the costly-push approach using a single queue, push is O(n) but pop and top are O(1). This is the standard LeetCode-accepted solution.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- The problem constrains us to use **only queue operations**. We simulate stack behavior by manipulating element order within the queue.
- A single queue suffices -- after pushing a new element, rotate all previous elements behind it.

### Pattern Recognition
- **Pattern:** Data Structure Simulation (implement one ADT using another)
- **Classification Cue:** "Implement X using only Y operations --> figure out how to reorder elements to match X's access pattern using Y's primitives."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Two Queues, Costly Pop
**Idea:** Use two queues `q1` (main) and `q2` (helper). Push goes to `q1`. For pop, move all but the last element from `q1` to `q2`, then dequeue the last element from `q1`. Swap `q1` and `q2`.

**Steps:**
1. `push(x)`: Enqueue x into `q1`. O(1).
2. `pop()`:
   - Move all elements except the last from `q1` to `q2` (n-1 moves).
   - Dequeue the remaining element from `q1` (this is the "top").
   - Swap `q1` and `q2`.
   - Return the dequeued element. O(n).
3. `top()`: Same as pop but re-enqueue the last element into `q2` before swapping. O(n).
4. `empty()`: Return `q1.isEmpty()`. O(1).

**BUD Transition -- Unnecessary Work:** We use two queues and top() is also O(n). We can do better with a single queue where top() is O(1).

| Time | Space |
|------|-------|
| push O(1), pop O(n), top O(n) | O(n) |

### Approach 2: Optimal -- Two Queues, Costly Push
**What changed:** Make push O(n) instead. Push new element into `q2`, then move all of `q1` into `q2`, then swap. Now the newest element is always at the front, giving O(1) pop and top.

**Steps:**
1. `push(x)`:
   - Enqueue x into `q2`.
   - Move all elements from `q1` to `q2`.
   - Swap `q1` and `q2`.
   - O(n).
2. `pop()`: Dequeue from `q1`. O(1).
3. `top()`: Peek at front of `q1`. O(1).
4. `empty()`: Return `q1.isEmpty()`. O(1).

**Dry Run:** push(1), push(2), push(3), pop()

| Operation | q1 (front...back) | q2 | Returned |
|-----------|--------------------|----|----------|
| push(1) | [1] | [] | - |
| push(2) | [2, 1] | [] | - |
| push(3) | [3, 2, 1] | [] | - |
| pop() | [2, 1] | [] | 3 |

| Time | Space |
|------|-------|
| push O(n), pop O(1), top O(1) | O(n) |

### Approach 3: Best -- Single Queue, Costly Push
**What changed:** Use only ONE queue. After enqueueing the new element, rotate the queue by dequeueing and re-enqueueing the previous n-1 elements. The new element ends up at the front.

**Steps:**
1. `push(x)`:
   - Enqueue x.
   - Rotate: dequeue and re-enqueue (size - 1) times.
   - O(n).
2. `pop()`: Dequeue. O(1).
3. `top()`: Peek. O(1).
4. `empty()`: Return `q.isEmpty()`. O(1).

**Dry Run:** push(1), push(2), push(3)

| Operation | Queue State (front...back) | Rotations |
|-----------|---------------------------|-----------|
| push(1) | enqueue 1 -> [1], rotate 0 times -> [1] | 0 |
| push(2) | enqueue 2 -> [1, 2], rotate 1 time -> [2, 1] | 1 |
| push(3) | enqueue 3 -> [2, 1, 3], rotate 2 times -> [3, 2, 1] | 2 |

After push(3), front is 3 (most recent), which is correct LIFO order.

| Time | Space |
|------|-------|
| push O(n), pop O(1), top O(1) | O(n) -- single queue |

---

## COMPLEXITY -- INTUITIVELY
**Time:** Push is O(n) because we rotate n-1 elements. Pop and top are O(1) because the "top" is always at the queue front.
**Space:** O(n) -- "We store all n elements in one queue. No auxiliary storage beyond the queue itself."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting the rotation count:** After push, rotate `size - 1` times (not `size` times, which would put the element back where it started).
2. **Off-by-one in two-queue approach:** Swapping q1 and q2 at the wrong time -- always swap AFTER all moves are done.
3. **Not considering that top() should not modify state:** top/peek must not dequeue permanently.

### Edge Cases to Test
- [ ] Push one element, then top (single element)
- [ ] Push one element, then pop (queue becomes empty)
- [ ] Push, pop, push, pop (alternating)
- [ ] Multiple pushes then multiple pops (batch operations)
- [ ] empty() after popping all elements

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to implement a LIFO stack using only FIFO queue operations."
2. **Match:** "The fundamental mismatch is FIFO vs LIFO. I need to reorder elements so the newest is at the front."
3. **Plan:** "I will use a single queue. On push, I enqueue the element, then rotate the previous n-1 elements behind it. Pop and top just read from the front."
4. **Implement:** Write the single-queue version. Note the rotation loop: `for i in range(size - 1)`.
5. **Review:** Dry-run push(1), push(2), push(3) showing the queue state after each rotation.
6. **Evaluate:** "Push O(n), pop/top O(1), space O(n). If pops are more frequent than pushes, this is efficient."

### Follow-Up Questions
- "Can you make both push and pop O(1)?" --> Not possible with standard queues. One of them must be O(n) to reverse the order.
- "What if push is called much more often than pop?" --> Use the costly-pop approach (Approach 1) so pushes are O(1).
- "How about implementing a queue using stacks?" --> See P006. Two stacks with amortized O(1) dequeue.

---

## CONNECTIONS
- **Prerequisite:** Queue operations, Stack ADT concept
- **Same Pattern:** Implement Queue using Stack (P006)
- **Harder Variant:** Min Stack (LC #155), Max Frequency Stack (LC #895)
- **This Unlocks:** Understanding ADT simulation, a common interview topic for testing data structure fundamentals
