# Intro to Linked List

> **Batch 1 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an array of integers, **create a singly linked list** from it, then **traverse** the list and print each element. Implement the fundamental operations: creation from array, traversal, and printing.

### Analogy
Think of a **treasure hunt** where each clue (node) tells you the location of the next clue (pointer). You start at the first clue (head) and follow the chain until a clue says "THE END" (null). The list itself is nothing but this chain of clues -- no one knows the 5th clue directly; you must follow from the start.

### Key Observations
1. **A linked list is NOT an array.** Unlike arrays where elements sit in contiguous memory, each node is an independent object pointing to the next. *Aha: This means no random access -- you must walk from head to reach any node.*
2. **The head is everything.** Lose the head pointer and you lose the entire list. *Aha: Every linked list operation revolves around maintaining and updating the head reference.*
3. **Each node = data + next pointer.** The simplest data structure is just two fields bundled together. *Aha: A linked list is recursively defined -- a node pointing to another linked list (or null).*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Singly Linked List (Node class with `val` and `next`) |
| **Why** | This IS the data structure we are learning. We create it from scratch. |
| **Pattern cue** | Whenever you hear "dynamic size," "frequent insert/delete at head," or "no need for index access," think linked list. |

---

## 3. APPROACH LADDER

### Approach 1: Array to Linked List (Iterative Construction)
**Intuition:** Walk through the array left to right, creating a new node for each element and linking it to the chain.

**Steps:**
1. If array is empty, return null.
2. Create the head node from `arr[0]`.
3. Keep a `current` pointer starting at head.
4. For each remaining element `arr[i]`: create a new node, set `current.next = newNode`, move `current` forward.
5. Return head.

**BUD Analysis:**
- **B**ottleneck: We must visit every element once -- unavoidable.
- **U**nnecessary work: None -- each element is processed exactly once.
- **D**uplicate work: None.

**Dry Run Trace:**
```
arr = [1, 2, 3, 4, 5]

Step 1: head = Node(1),          current = head          List: 1 -> null
Step 2: new = Node(2),           current.next = new      List: 1 -> 2 -> null
        current = new
Step 3: new = Node(3),           current.next = new      List: 1 -> 2 -> 3 -> null
        current = new
Step 4: new = Node(4),           current.next = new      List: 1 -> 2 -> 3 -> 4 -> null
        current = new
Step 5: new = Node(5),           current.next = new      List: 1 -> 2 -> 3 -> 4 -> 5 -> null
        current = new

Traversal: Start at head(1), print 1, move to 2, print 2, ... , print 5, next is null -> stop.
Output: 1 -> 2 -> 3 -> 4 -> 5
```

### Approach 2: Recursive Construction
**Intuition:** Use recursion to build the list -- create a node for `arr[i]` and set its next to the result of recursively building from `arr[i+1:]`.

**Steps:**
1. Base case: if index >= array length, return null.
2. Create node from `arr[index]`.
3. `node.next = buildList(arr, index + 1)`.
4. Return node.

### Approach 3: Head Insertion (Reverse Build)
**Intuition:** Insert each element at the head -- simpler but produces a reversed list. Useful to understand head insertion.

**Steps:**
1. Start with `head = null`.
2. For each element (right to left if you want original order): create node, set `node.next = head`, update `head = node`.

---

## 4. COMPLEXITY INTUITIVELY

| Operation | Time | Space | Why |
|-----------|------|-------|-----|
| Build from array | O(n) | O(n) | Visit each element once, create n nodes |
| Traverse/Print | O(n) | O(1) | Walk each node once, no extra space |
| Recursive build | O(n) | O(n) + O(n) call stack | n recursive calls on the stack |

*"You have n items. You touch each exactly once. That's O(n). The nodes themselves take O(n) space because you are building the list."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Empty array `[]` | No list to create | Return null/None |
| Single element `[5]` | List with one node | Head is the only node, next = null |
| Large values | Node stores large int | No special handling needed |
| Negative values | Valid data | Works normally |

**Common Mistakes:**
- Forgetting to move the `current` pointer forward after linking -- creates a list of only 2 nodes.
- Returning `current` instead of `head` -- you lose the beginning of the list.
- Not handling empty array -- NullPointerException on `arr[0]`.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Why not just use an array?** | Linked lists offer O(1) insertion/deletion at head without shifting elements. Arrays require O(n) shifts for insert at beginning. |
| **When is a linked list better?** | When you need frequent insertions/deletions at arbitrary positions and don't need random access. |
| **Follow-up: Can you do it recursively?** | Yes -- see Approach 2. Each call creates one node and delegates the rest. |
| **What's the overhead vs array?** | Each node stores an extra pointer (8 bytes on 64-bit). For small data, this can double memory usage. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Doubly Linked List | Same idea but each node also points backward |
| Reverse Linked List | Requires mastering traversal + pointer manipulation |
| Linked List Cycle | Floyd's algorithm builds on traversal |
| LRU Cache | Uses doubly linked list + hash map |
| Array vs Linked List | Classic tradeoff question in interviews |
