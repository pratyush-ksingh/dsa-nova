# Print 1 to N (Recursion)

> **Batch 1 of 12** | **Topic:** Recursion | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a positive integer **N**, print all numbers from **1 to N** using **recursion only** (no loops allowed).

### Analogy
Imagine a line of **dominoes** numbered 1 to N. You tell domino N: "print all numbers up to you." Domino N says: "I don't know how to print 1 through N-1, but let me ask domino N-1 to handle that, and then I will print my own number." This delegation continues all the way down to domino 1, who just prints "1" and stops. As each domino finishes delegating and prints its own number, you get 1, 2, 3, ..., N.

### Key Observations
1. **Recursion = break the problem into a smaller version of itself.** Printing 1 to N is the same as printing 1 to N-1, then printing N. *Aha: The "print after recursion" placement ensures ascending order.*
2. **Base case stops infinite recursion.** When N reaches 0 (or 1), stop. *Aha: Without a base case, the function calls itself forever and causes a stack overflow.*
3. **Two ways to think about it.** Print before the recursive call (counting up from i) or print after the recursive call (counting down from N). *Aha: The position of the print statement relative to the recursive call determines the order.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Algorithm** | Simple recursion |
| **Why** | This is a recursion learning exercise -- the goal is to understand recursive function calls |
| **Pattern cue** | "Do something N times without loops" -- think recursion with a counter parameter. |

---

## 3. APPROACH LADDER

### Approach 1: Counting Up (Pass current counter)
**Intuition:** Start from 1, print the current number, then recurse for the next number until you reach N.

**Steps:**
1. Define `printNumbers(i, n)`.
2. Base case: if `i > n`, return.
3. Print `i`.
4. Call `printNumbers(i + 1, n)`.

**Dry Run Trace (N = 5):**
```
printNumbers(1, 5) -> print 1, call printNumbers(2, 5)
  printNumbers(2, 5) -> print 2, call printNumbers(3, 5)
    printNumbers(3, 5) -> print 3, call printNumbers(4, 5)
      printNumbers(4, 5) -> print 4, call printNumbers(5, 5)
        printNumbers(5, 5) -> print 5, call printNumbers(6, 5)
          printNumbers(6, 5) -> 6 > 5, RETURN

Output: 1 2 3 4 5
```

### Approach 2: Counting Down with Print After Recursion (Backtracking)
**Intuition:** Count down from N but print AFTER the recursive call returns. The deepest call prints first.

**Steps:**
1. Define `printNumbers(n)`.
2. Base case: if `n < 1`, return.
3. Call `printNumbers(n - 1)`.
4. Print `n` (after recursion returns).

**Dry Run Trace (N = 5):**
```
printNumbers(5) -> call printNumbers(4) first
  printNumbers(4) -> call printNumbers(3) first
    printNumbers(3) -> call printNumbers(2) first
      printNumbers(2) -> call printNumbers(1) first
        printNumbers(1) -> call printNumbers(0) first
          printNumbers(0) -> 0 < 1, RETURN
        print 1  <-- deepest returns first
      print 2
    print 3
  print 4
print 5

Output: 1 2 3 4 5
```

### Approach 3: Both approaches are O(n) and optimal.
Since we must print n numbers, O(n) time and O(n) stack space is the minimum possible.

---

## 4. COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why |
|----------|------|-------|-----|
| Counting Up | O(n) | O(n) | n function calls on the stack |
| Backtracking | O(n) | O(n) | n function calls on the stack |

*"You make exactly n recursive calls, each doing O(1) work (one print). The call stack holds n frames. Both time and space are O(n)."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| N = 0 | Nothing to print | Base case returns immediately |
| N = 1 | Print just "1" | Single recursive call |
| N is very large | Stack overflow | Recursion depth limit (Java ~10K, Python ~1000 by default) |
| Negative N | Nothing to print | Base case handles it |

**Common Mistakes:**
- Forgetting the base case -- infinite recursion, stack overflow.
- Printing before recursion in the "counting down" approach -- gives N to 1 instead of 1 to N.
- Off-by-one: using `i >= n` instead of `i > n` skips the last number.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Why not just use a loop?** | This is a recursion exercise. In practice, a loop is better here (no stack overhead). |
| **Stack overflow risk?** | For large N (>10K in Java, >1000 in Python), recursion hits the stack limit. Use tail recursion or loops. |
| **Is this tail recursive?** | Approach 1 (print then recurse) is tail recursive. Some compilers optimize this into a loop. Approach 2 is not. |
| **Follow-up: Print without recursion OR loops?** | Use `goto` (C) or repeated string multiplication (Python). A trick question. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Print N to 1 | Same structure, just swap print position |
| Print Name N Times | Same pattern with different action |
| Factorial | Same "do one thing then delegate" recursive pattern |
| Fibonacci | Adds branching -- two recursive calls |
| Sum of 1 to N | Replace print with accumulation |
