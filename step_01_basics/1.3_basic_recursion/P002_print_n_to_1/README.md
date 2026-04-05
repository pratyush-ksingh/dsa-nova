# Print N to 1 (Recursion)

> **Batch 1 of 12** | **Topic:** Recursion | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a positive integer **N**, print all numbers from **N down to 1** using **recursion only** (no loops allowed).

### Analogy
Imagine you are at **floor N of a building** and must announce each floor number as you walk down the stairs. You announce your current floor, then walk down one flight and repeat. When you reach the ground floor (0), you stop. Each announcement happens before you descend -- that is why you get descending order.

### Key Observations
1. **Print BEFORE the recursive call.** To get descending order, print N first, then recurse for N-1. *Aha: This is the mirror of "Print 1 to N" where you print AFTER the recursive call.*
2. **Same base case.** When N reaches 0, stop. *Aha: The base case is identical to Print 1 to N. Only the print placement changes.*
3. **Alternative: count up but print on the way back.** Start from 1, recurse to N, then print as each call returns. *Aha: Understanding pre-recursion vs post-recursion printing is key to mastering recursion order.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Algorithm** | Simple recursion |
| **Why** | Recursion learning exercise -- understand call stack unwinding |
| **Pattern cue** | "Count down without loops" -- recurse with decrementing parameter, print before recursion. |

---

## 3. APPROACH LADDER

### Approach 1: Counting Down (Print Before Recursion)
**Intuition:** Print N, then ask recursion to handle N-1 down to 1.

**Steps:**
1. Define `printNumbers(n)`.
2. Base case: if `n < 1`, return.
3. Print `n`.
4. Call `printNumbers(n - 1)`.

**Dry Run Trace (N = 5):**
```
printNumbers(5) -> print 5, call printNumbers(4)
  printNumbers(4) -> print 4, call printNumbers(3)
    printNumbers(3) -> print 3, call printNumbers(2)
      printNumbers(2) -> print 2, call printNumbers(1)
        printNumbers(1) -> print 1, call printNumbers(0)
          printNumbers(0) -> 0 < 1, RETURN

Output: 5 4 3 2 1
```

### Approach 2: Counting Up with Print After Recursion
**Intuition:** Count up from 1 to N, but print AFTER returning from the recursive call. The last call (N) prints first.

**Steps:**
1. Define `printNumbers(i, n)`.
2. Base case: if `i > n`, return.
3. Call `printNumbers(i + 1, n)`.
4. Print `i` (after recursion returns).

**Dry Run Trace (N = 5):**
```
printNumbers(1, 5) -> call printNumbers(2, 5)
  printNumbers(2, 5) -> call printNumbers(3, 5)
    printNumbers(3, 5) -> call printNumbers(4, 5)
      printNumbers(4, 5) -> call printNumbers(5, 5)
        printNumbers(5, 5) -> call printNumbers(6, 5)
          printNumbers(6, 5) -> 6 > 5, RETURN
        print 5
      print 4
    print 3
  print 2
print 1

Output: 5 4 3 2 1
```

### Approach 3: Both approaches are O(n) and optimal.
Must print n numbers, so O(n) is the minimum.

---

## 4. COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why |
|----------|------|-------|-----|
| Counting Down | O(n) | O(n) | n recursive calls, each O(1) work |
| Counting Up (backtracking) | O(n) | O(n) | n recursive calls, print on unwind |

*"Exactly n calls, each does O(1) work (one print). Stack holds n frames = O(n) space."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| N = 0 | Nothing to print | Base case returns immediately |
| N = 1 | Print just "1" | One recursive call, one print |
| Very large N | Stack overflow risk | Python: ~1000 default limit. Java: ~10K. |
| Negative N | Nothing to print | Base case handles it (n < 1) |

**Common Mistakes:**
- Printing AFTER the recursive call -- gives 1 to N (ascending) instead of N to 1.
- Using `n == 0` as base case when N could be negative.
- Forgetting to decrement: `printNumbers(n)` instead of `printNumbers(n - 1)` causes infinite recursion.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **How does this relate to Print 1 to N?** | They are mirrors. Print before recursion = descending. Print after recursion = ascending. |
| **Tail recursion?** | Approach 1 is tail-recursive (print then recurse). Can be optimized into a loop by compilers. |
| **Can you do both 1-to-N and N-to-1 in one function?** | Yes -- print N before recursion (descending) AND print N after recursion (ascending). Prints both sequences. |
| **What is the call stack doing?** | Each call pushes a frame. When base case is hit, frames pop in LIFO order. Printing during push = descending. Printing during pop = ascending. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Print 1 to N | Mirror problem -- swap print position |
| Print Name N Times | Same countdown pattern |
| Reverse a String | Print characters in reverse = print after recursion |
| Tower of Hanoi | More complex recursion with pre/post actions |
| Countdown Timer | Real-world analogy of this exact pattern |

---

## Real-World Use Case
**Undo history in editors:** Applications like VS Code and Google Docs maintain an operation stack. Walking backward from the most recent action (N) to the oldest (1) is conceptually the same recursive countdown. Each undo step "peels off" one operation, exactly like this N-to-1 recursion.
