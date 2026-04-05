# Implement Stack using Arrays

> **Batch 1 of 12** | **Topic:** Stack & Queue | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Implement a **Stack** data structure using an array. Support these operations:
- `push(x)` -- Add element x to the top of the stack
- `pop()` -- Remove and return the top element
- `peek()` / `top()` -- Return the top element without removing it
- `isEmpty()` -- Check if the stack is empty
- `size()` -- Return the number of elements

### Analogy
A stack of **plates in a cafeteria**. You can only add a plate on top (push) or remove the plate on top (pop). You can look at the top plate (peek) but you cannot pull one from the middle. This is **LIFO** -- Last In, First Out.

### Key Observations
1. **A `top` pointer tracks the current position.** Initialize `top = -1` (empty). After each push, increment top. After each pop, decrement top. *Aha: The `top` variable serves double duty -- it is both the index of the top element AND the size indicator (top + 1 = size).*
2. **Fixed size vs dynamic size.** Arrays have fixed capacity. You must decide upfront or implement resizing. *Aha: Java's `Stack` class and Python's `list` handle resizing internally -- this exercise teaches you what happens under the hood.*
3. **All operations are O(1).** No traversal needed -- you always work at the top. *Aha: This is why stacks are so fast -- every operation just reads/writes at one index.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Array (fixed capacity) |
| **Why** | Contiguous memory = cache-friendly, O(1) random access by index |
| **Pattern cue** | "LIFO," "undo/redo," "matching parentheses," "function call stack" -- think stack. |

---

## 3. APPROACH LADDER

### Approach 1: Fixed-Size Array Stack
**Intuition:** Use an array of fixed capacity with a `top` pointer that tracks the index of the topmost element.

**Steps:**
1. Initialize array of given capacity, `top = -1`.
2. `push(x)`: if `top == capacity - 1`, stack overflow. Else, `top++; arr[top] = x`.
3. `pop()`: if `top == -1`, stack underflow. Else, return `arr[top--]`.
4. `peek()`: if `top == -1`, stack empty. Else, return `arr[top]`.
5. `isEmpty()`: return `top == -1`.
6. `size()`: return `top + 1`.

**BUD Analysis:**
- **B**ottleneck: None -- all operations are O(1).
- **U**nnecessary work: None.
- **D**uplicate work: None.

**Dry Run Trace:**
```
Stack capacity = 5, top = -1, arr = [_, _, _, _, _]

push(10):  top = 0, arr = [10, _, _, _, _]
push(20):  top = 1, arr = [10, 20, _, _, _]
push(30):  top = 2, arr = [10, 20, 30, _, _]
peek():    return arr[2] = 30
pop():     return arr[2] = 30, top = 1, arr = [10, 20, _, _, _]
size():    return top + 1 = 2
pop():     return arr[1] = 20, top = 0
pop():     return arr[0] = 10, top = -1
isEmpty(): top == -1 -> true
pop():     UNDERFLOW (top == -1)
```

### Approach 2: Dynamic Array Stack (Amortized O(1))
**Intuition:** When the array fills up, create a new array of double the size and copy elements over.

**Steps:**
1. Same as Approach 1, but when `top == capacity - 1`:
2. Create new array of size `2 * capacity`.
3. Copy all elements.
4. Update reference and capacity.
5. Then push normally.

*Note: Individual push can be O(n) during resize, but amortized over n pushes, each push is still O(1).*

---

## 4. COMPLEXITY INTUITIVELY

| Operation | Time | Space |
|-----------|------|-------|
| push | O(1) | O(1) |
| pop | O(1) | O(1) |
| peek | O(1) | O(1) |
| isEmpty | O(1) | O(1) |
| size | O(1) | O(1) |
| Overall space | -- | O(n) for n elements |

*"Every operation just reads or writes at index `top`. No loops, no traversal. That is O(1)."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Pop from empty stack | Underflow | Check `top == -1`, return -1 or throw |
| Push to full stack | Overflow | Check `top == capacity - 1`, return error or resize |
| Peek on empty stack | Invalid | Check `top == -1`, return -1 or throw |
| Single element push then pop | Stack returns to empty | top goes 0 -> -1, works correctly |

**Common Mistakes:**
- Off-by-one: initializing `top = 0` instead of `-1` wastes first slot or causes bugs.
- Not decrementing `top` after pop -- causes stale data to persist.
- Checking `top == capacity` instead of `top == capacity - 1` for overflow.
- Forgetting to return the value before decrementing in pop.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Array vs Linked List for stack?** | Array is cache-friendly and simpler. Linked list avoids fixed capacity. Both give O(1) operations. |
| **What about thread safety?** | This implementation is not thread-safe. Use synchronized methods or `ConcurrentStack` in production. |
| **Why `top = -1`?** | Convention that makes `top + 1 = size` and `top == -1` = empty. Some use `top = 0` with a different convention. |
| **How does Java's Stack work internally?** | Extends Vector (synchronized ArrayList). Uses dynamic array with resizing. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Stack using Linked List | Same interface, different backing store |
| Stack using Queues | Classic interview question -- implement LIFO with FIFO |
| Min Stack (LC #155) | Stack with O(1) getMin -- builds on this implementation |
| Valid Parentheses (LC #20) | Stack application -- matching brackets |
| Function Call Stack | The OS uses a stack for function calls -- this is its simplified version |

---

## Real-World Use Case
**Call stack in JavaScript engines:** V8 (Chrome's JS engine) uses an array-based stack to track function calls. Each function invocation pushes a frame; returning pops it. The fixed-size array approach mirrors the engine's pre-allocated stack memory, and stack overflow errors occur when the array capacity is exceeded.
