# Implement Stack using Linked List

> **Batch 1 of 12** | **Topic:** Stack & Queue | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Implement a **Stack** data structure using a **singly linked list**. Support these operations:
- `push(x)` -- Add element x to the top
- `pop()` -- Remove and return the top element
- `peek()` / `top()` -- Return the top element without removing
- `isEmpty()` -- Check if the stack is empty
- `size()` -- Return the number of elements

### Analogy
Instead of stacking plates on a shelf (array), imagine a **chain of paper clips**. Each clip (node) hooks onto the one below it (next pointer). To add a clip, hook it onto the top (push at head). To remove, unhook the top clip (pop from head). The chain grows and shrinks dynamically -- no fixed shelf needed.

### Key Observations
1. **Push and pop happen at the head of the linked list.** The head IS the top of the stack. *Aha: Insert-at-head and delete-at-head are both O(1) -- a perfect match for stack operations.*
2. **No fixed capacity.** Unlike the array stack, this grows dynamically. *Aha: No overflow possible (until you run out of memory). Trade-off is extra memory per node for the pointer.*
3. **Each node stores data + pointer.** For small data (like int), the pointer overhead can double the memory. *Aha: Array stack is more memory-efficient for known-size stacks; linked list stack is better when size is unpredictable.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Singly Linked List |
| **Why** | Dynamic size, O(1) push/pop at head, no resizing needed |
| **Pattern cue** | "Stack with unknown size," "no capacity limit," or when you already have a linked list infrastructure. |

---

## 3. APPROACH LADDER

### Approach 1: Stack via Singly Linked List (Head = Top)
**Intuition:** The head of the linked list is the top of the stack. Push inserts at head. Pop removes from head. Both are O(1).

**Steps:**
1. Initialize `top = null`, `size = 0`.
2. `push(x)`: create node, `node.next = top`, `top = node`, `size++`.
3. `pop()`: if `top == null`, underflow. Else `val = top.val`, `top = top.next`, `size--`, return val.
4. `peek()`: if `top == null`, underflow. Else return `top.val`.
5. `isEmpty()`: return `top == null`.
6. `size()`: return `size`.

**BUD Analysis:**
- **B**ottleneck: None -- all O(1).
- **U**nnecessary work: None.
- **D**uplicate work: None.

**Dry Run Trace:**
```
top = null, size = 0

push(10):  Node(10) -> null         top = Node(10), size = 1
push(20):  Node(20) -> Node(10)     top = Node(20), size = 2
push(30):  Node(30) -> Node(20)     top = Node(30), size = 3
peek():    top.val = 30
pop():     val = 30, top = Node(20), size = 2
           List: Node(20) -> Node(10)
pop():     val = 20, top = Node(10), size = 1
pop():     val = 10, top = null, size = 0
isEmpty(): true
pop():     UNDERFLOW (top == null)
```

### Approach 2: Why NOT Push/Pop at Tail?
**Intuition (counter-example):** If we pushed at tail and popped at tail, pop would require O(n) to find the second-to-last node (singly linked list has no prev pointer).

*This is why head-based operations are chosen for SLL stack.*

---

## 4. COMPLEXITY INTUITIVELY

| Operation | Time | Space |
|-----------|------|-------|
| push | O(1) | O(1) per node |
| pop | O(1) | O(1) |
| peek | O(1) | O(1) |
| isEmpty | O(1) | O(1) |
| size | O(1) | O(1) |
| Overall space | -- | O(n) for n elements + O(n) pointer overhead |

*"Every operation touches only the head node. One pointer update = O(1). The linked list version trades memory efficiency for dynamic sizing."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Pop from empty stack | top is null | Check before accessing, return -1 or throw |
| Peek on empty stack | top is null | Check before accessing |
| Push single, pop single | Stack goes empty to one to empty | top goes null -> node -> null |
| Very large stack | Memory fills up | Java: OutOfMemoryError. Python: MemoryError. No graceful fix. |

**Common Mistakes:**
- Forgetting to update `top` after pop (top still points to removed node).
- Not updating `size` counter.
- Memory leak in languages without GC -- must free the popped node.
- Pushing at tail instead of head -- makes pop O(n).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **When to use LL stack over array stack?** | When maximum size is unknown or highly variable. Array stack wastes space if over-allocated. |
| **Memory overhead?** | Each node has an extra pointer (8 bytes on 64-bit). For int data (4 bytes), that is 3x the raw data size. |
| **Can you implement with doubly linked list?** | Yes, but unnecessary. SLL is sufficient since we only need head operations. DLL adds wasted prev pointers. |
| **Garbage collection concern?** | In Java/Python, popped nodes are auto-collected. In C/C++, you must explicitly free them. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Stack using Arrays | Same interface, compare implementations |
| Queue using Linked List | Same backing structure, different access pattern |
| Min Stack (LC #155) | Can use LL nodes with an extra `min` field |
| Reverse a Linked List | Pop all into stack, push back -- reversal via stack |
| Function Call Stack | Conceptually identical -- each frame is a "node" |
