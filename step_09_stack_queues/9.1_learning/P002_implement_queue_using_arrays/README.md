# Implement Queue using Arrays

> **Batch 1 of 12** | **Topic:** Stack & Queue | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Implement a **Queue** data structure using a circular array. Support these operations:
- `enqueue(x)` -- Add element x to the rear
- `dequeue()` -- Remove and return the front element
- `front()` -- Return the front element without removing it
- `isEmpty()` -- Check if the queue is empty
- `size()` -- Return the number of elements

### Analogy
A **line at a ticket counter**. People join at the back (enqueue) and leave from the front (dequeue). The first person who arrived is the first to be served. This is **FIFO** -- First In, First Out. The circular array is like a circular waiting room where after the last seat, you wrap back to the first seat.

### Key Observations
1. **A naive array wastes space.** If you dequeue by shifting all elements left, that is O(n). If you just move the front pointer, you waste the vacated slots. *Aha: A circular array solves both problems -- no shifting AND no wasted space.*
2. **Circular indexing via modulo.** `(index + 1) % capacity` wraps the index around. *Aha: This single trick turns a linear array into a circular buffer.*
3. **Distinguish full vs empty.** Both have `front == rear` in some implementations. *Aha: Track `size` separately to avoid ambiguity, or sacrifice one slot (capacity = n+1 for n elements).*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Circular Array |
| **Why** | O(1) enqueue and dequeue without wasting space |
| **Pattern cue** | "FIFO," "BFS," "task scheduling," "producer-consumer" -- think queue. |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Linear Array with Shift
**Intuition:** Use a simple array. Enqueue at the end. Dequeue by removing index 0 and shifting everything left.

**Steps:**
1. `enqueue(x)`: place at `arr[rear++]`.
2. `dequeue()`: save `arr[0]`, shift all elements left by one, decrement rear.

*This works but dequeue is O(n) due to shifting.*

### Approach 2: Optimal -- Circular Array with Size Tracking
**Intuition:** Use `front` and `rear` pointers that wrap around using modulo. Track `size` to distinguish full from empty.

**Steps:**
1. Initialize `arr[capacity]`, `front = 0`, `rear = 0`, `size = 0`.
2. `enqueue(x)`: if `size == capacity`, overflow. Else `arr[rear] = x; rear = (rear + 1) % capacity; size++`.
3. `dequeue()`: if `size == 0`, underflow. Else `val = arr[front]; front = (front + 1) % capacity; size--; return val`.
4. `front()`: return `arr[front]` if `size > 0`.
5. `isEmpty()`: return `size == 0`.
6. `size()`: return `size`.

**BUD Analysis:**
- **B**ottleneck: None -- all O(1).
- **U**nnecessary work: Brute force shifts elements. Circular array eliminates this.
- **D**uplicate work: None.

**Dry Run Trace:**
```
Capacity = 4, front = 0, rear = 0, size = 0
arr = [_, _, _, _]

enqueue(10): arr[0]=10, rear=1, size=1    arr = [10, _, _, _]   front=0
enqueue(20): arr[1]=20, rear=2, size=2    arr = [10, 20, _, _]  front=0
enqueue(30): arr[2]=30, rear=3, size=3    arr = [10, 20, 30, _] front=0
dequeue():   val=10, front=1, size=2      arr = [10, 20, 30, _] front=1
dequeue():   val=20, front=2, size=1      arr = [10, 20, 30, _] front=2
enqueue(40): arr[3]=40, rear=0, size=2    arr = [10, 20, 30, 40] front=2  <-- rear wraps!
enqueue(50): arr[0]=50, rear=1, size=3    arr = [50, 20, 30, 40] front=2  <-- reuses slot 0!
front():     arr[2] = 30
size():      3
```

### Approach 3: Best -- Same as Optimal
The circular array approach is optimal. All operations are O(1) with O(n) space. There is no way to do better.

---

## 4. COMPLEXITY INTUITIVELY

| Operation | Time | Space | Why |
|-----------|------|-------|-----|
| enqueue | O(1) | O(1) | Write at rear index, increment |
| dequeue | O(1) | O(1) | Read at front index, increment |
| front | O(1) | O(1) | Direct index access |
| isEmpty / size | O(1) | O(1) | Just read a variable |
| Overall space | -- | O(n) | Array of capacity n |
| Brute force dequeue | O(n) | O(1) | Shifting all elements |

*"The circular array trick converts O(n) dequeue into O(1) by moving a pointer instead of moving data."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Dequeue from empty queue | Underflow | Check `size == 0`, return -1 or throw |
| Enqueue to full queue | Overflow | Check `size == capacity`, return error or resize |
| Wrap-around | rear or front exceeds capacity | Use `% capacity` for circular indexing |
| Single enqueue then dequeue | Queue returns to empty | front catches up to rear, size = 0 |
| Fill completely then empty | All slots used then freed | front and rear may not be 0 -- that is fine |

**Common Mistakes:**
- Forgetting modulo -- array index out of bounds when rear or front exceeds capacity.
- Confusing full vs empty when both `front == rear` (solved by tracking size).
- Off-by-one in capacity check.
- Not using circular indexing for `front` in dequeue.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Why circular?** | Linear array either wastes space (moving front pointer) or wastes time (shifting elements). Circular does neither. |
| **Array vs Linked List for queue?** | Array is cache-friendly. Linked list has no fixed capacity but worse cache performance. |
| **How to resize?** | Create new array of 2x capacity, copy elements starting from `front` in circular order, reset front=0, rear=size. |
| **Real-world use?** | OS process scheduling, print job spooling, BFS traversal, message queues (Kafka, RabbitMQ). |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Queue using Stacks (LC #232) | Implement FIFO using two LIFO stacks |
| Circular Queue (LC #622) | Exactly this problem on LeetCode |
| BFS | Uses queue as its core data structure |
| Sliding Window Maximum (LC #239) | Uses deque (double-ended queue) |
| Stack using Queues (LC #225) | Inverse problem -- LIFO from FIFO |

---

## Real-World Use Case
**Print job spooling:** Operating systems like Windows use an array-backed circular queue to manage print jobs. Documents are enqueued in FIFO order so the first submitted job prints first. The circular array avoids wasted space at the front, matching how the OS print spooler efficiently recycles buffer slots.
