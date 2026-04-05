# Implement Queue using Linked List

> **Batch 2 of 12** | **Topic:** Stacks & Queues | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Implement a queue data structure using a singly linked list. Support the following operations:
- `enqueue(val)` -- Add an element to the back of the queue.
- `dequeue()` -- Remove and return the front element.
- `peek()` / `front()` -- Return the front element without removing it.
- `isEmpty()` -- Check whether the queue is empty.
- `size()` -- Return the number of elements.

All operations must run in O(1) time.

**Constraints:**
- `0 <= val <= 10^9`
- At most `10^5` operations
- `dequeue` and `peek` on an empty queue should be handled gracefully

**Examples:**

| Operations | Result | Queue State (front...rear) |
|------------|--------|---------------------------|
| `enqueue(10)` | - | [10] |
| `enqueue(20)` | - | [10, 20] |
| `enqueue(30)` | - | [10, 20, 30] |
| `peek()` | `10` | [10, 20, 30] |
| `dequeue()` | `10` | [20, 30] |
| `dequeue()` | `20` | [30] |
| `isEmpty()` | `false` | [30] |
| `dequeue()` | `30` | [] |
| `isEmpty()` | `true` | [] |

### Real-Life Analogy
> *Think of a line at a coffee shop. New customers join at the back (enqueue). The barista serves the person at the front (dequeue). You can see who is next (peek) without pulling them out of line. The linked list is like people holding hands in a chain -- each person knows who is behind them (next pointer), and the shop knows who is first (front pointer) and last (rear pointer).*

### Key Observations
1. A queue is FIFO (First In, First Out). We add at one end and remove from the other. A linked list naturally supports O(1) insertion at tail (if we track it) and O(1) removal from head. <-- This is the "aha" insight
2. We need TWO pointers: `front` (head, for dequeue) and `rear` (tail, for enqueue). Without a rear pointer, enqueue would require O(n) traversal.
3. The critical edge case is when the queue becomes empty after a dequeue -- we must reset BOTH `front` and `rear` to null, otherwise `rear` becomes a dangling reference.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- A **singly linked list** gives O(1) insertion at tail (with rear pointer) and O(1) deletion at head -- perfect for queue operations.
- Unlike an array-based queue, a linked list has no fixed capacity -- it grows and shrinks dynamically with no wasted space.
- Unlike a circular array queue, there is no need to handle wrap-around logic.

### Pattern Recognition
- **Pattern:** Linked List as backing store for an ADT (Abstract Data Type)
- **Classification Cue:** "Implement a data structure with O(1) add/remove at specific ends --> linked list with head/tail pointers."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Array-Based Queue
**Idea:** Use a dynamic array. Enqueue appends to the end. Dequeue removes from the front by shifting all elements left.

**Steps:**
1. `enqueue(val)`: Append to end of array. O(1) amortized.
2. `dequeue()`: Remove index 0, shift everything left. O(n).
3. `peek()`: Return element at index 0. O(1).

**Why it is inefficient:** Dequeue is O(n) because shifting n-1 elements left after removing the front.

**BUD Transition -- Bottleneck:** The shift operation is unnecessary if we use a data structure where removing the head is O(1). A linked list removes the head by simply moving the pointer.

| Time (per op) | Space |
|----------------|-------|
| enqueue O(1), dequeue O(n) | O(n) |

### Approach 2: Optimal -- Linked List with Front and Rear Pointers
**What changed:** Use a singly linked list. Maintain `front` (head) and `rear` (tail) pointers. Enqueue creates a new node and links it after rear. Dequeue advances front to front.next.

**Steps:**
1. `enqueue(val)`:
   - Create new node.
   - If empty: `front = rear = newNode`.
   - Else: `rear.next = newNode`, then `rear = newNode`.
   - Increment size.
2. `dequeue()`:
   - If empty: return error.
   - Save `front.val`, advance `front = front.next`.
   - If `front` becomes null: set `rear = null` too (critical!).
   - Decrement size. Return saved value.
3. `peek()`: Return `front.val`.
4. `isEmpty()`: Return `front == null`.

**Dry Run:** enqueue(10), enqueue(20), dequeue(), enqueue(30)

| Operation | front | rear | List | Returned |
|-----------|-------|------|------|----------|
| enqueue(10) | Node(10) | Node(10) | 10 | - |
| enqueue(20) | Node(10) | Node(20) | 10->20 | - |
| dequeue() | Node(20) | Node(20) | 20 | 10 |
| enqueue(30) | Node(20) | Node(30) | 20->30 | - |

| Time (per op) | Space |
|----------------|-------|
| All O(1) | O(n) total for n elements |

### Approach 3: Best -- Same as Optimal (Linked List is the ideal backing store)
**What changed:** For a basic queue, the singly linked list with front/rear pointers IS the best approach. No further optimization needed.

**Enhancements for production code:**
- Add a `size` counter to avoid O(n) traversal for size queries.
- Use sentinel/dummy nodes to simplify edge case handling (empty queue).
- For thread safety, add locks around operations (or use lock-free CAS for concurrent queues).

| Time (per op) | Space |
|----------------|-------|
| All O(1) | O(n) total |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(1) per operation -- "Enqueue appends after the tail pointer (one link update). Dequeue advances the head pointer (one pointer move). No traversal needed for either."
**Space:** O(n) total -- "Each of the n elements in the queue occupies one linked list node."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting to reset `rear` when queue empties:** After dequeuing the last element, `front` becomes null but `rear` still points to the deleted node. Next enqueue will not work correctly.
2. **Not handling dequeue/peek on empty queue:** Must check `isEmpty()` first and return an error or sentinel value.
3. **Losing the size count:** Forgetting to increment/decrement the size counter during enqueue/dequeue.

### Edge Cases to Test
- [ ] Enqueue on empty queue (first element -- front and rear must both point to it)
- [ ] Dequeue until empty (rear must be reset to null)
- [ ] Enqueue after emptying (queue should work normally again)
- [ ] Peek on empty queue (handle gracefully)
- [ ] Single element: enqueue, peek, dequeue (front == rear the whole time)
- [ ] Large number of operations (ensure no memory leaks)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to implement a queue supporting enqueue, dequeue, peek, isEmpty -- all in O(1)."
2. **Match:** "FIFO semantics + O(1) at both ends --> singly linked list with front (head) and rear (tail) pointers."
3. **Plan:** "Enqueue: create node, link after rear, update rear. Dequeue: save front value, advance front, reset rear if empty."
4. **Implement:** Write the class. Emphasize the `rear = null` reset in dequeue.
5. **Review:** Walk through enqueue(10), enqueue(20), dequeue() to show pointer movements.
6. **Evaluate:** "O(1) per operation, O(n) total space. No fixed capacity, no wasted space."

### Follow-Up Questions
- "What if you need a thread-safe queue?" --> Use a lock or implement a lock-free queue (Michael-Scott queue with CAS).
- "Array-based vs. linked-list queue?" --> Array has better cache locality but needs resizing. Linked list has no capacity limit but more memory overhead per element (next pointer).
- "Can you implement with a doubly linked list?" --> Yes, but overkill. Singly linked list suffices because we only remove from head and add to tail.

---

## CONNECTIONS
- **Prerequisite:** Singly Linked List basics, Queue ADT concept
- **Same Pattern:** Implement Stack using Linked List (P003)
- **Harder Variant:** Implement Queue using Stacks (P006), Circular Queue (LC #622)
- **This Unlocks:** BFS (uses queue), Level-order traversal, Task scheduling problems
