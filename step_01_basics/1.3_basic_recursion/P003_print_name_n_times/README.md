# Print Name N Times (Recursion)

> **Batch 1 of 12** | **Topic:** Recursion | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a positive integer **N** and a name (string), print the name **N times** using **recursion only** (no loops allowed).

### Analogy
Imagine an **echo in a canyon**. You shout your name once, and the canyon echoes it back N times. Each echo triggers the next one, getting quieter (smaller N) until silence (N = 0). Each echo is one recursive call that prints the name and delegates the remaining echoes.

### Key Observations
1. **This is the simplest possible recursion pattern.** Do one thing (print), then recurse with a smaller problem (N-1). *Aha: If you understand this, you understand the skeleton of ALL recursion -- base case + action + recursive call.*
2. **The action is always the same.** Unlike Print 1 to N where the output changes, here every call prints the same string. *Aha: This makes it the cleanest example to isolate and understand the recursion mechanism itself.*
3. **Count up or count down -- both work.** Count from 1 to N (incrementing) or from N to 0 (decrementing). The output is identical since every call prints the same thing. *Aha: When the action is the same regardless of the counter value, ascending vs descending recursion produces identical output.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Algorithm** | Simple recursion with counter |
| **Why** | Recursion fundamentals exercise |
| **Pattern cue** | "Repeat an action N times without loops" -- recursion with a decrementing counter. |

---

## 3. APPROACH LADDER

### Approach 1: Count Down from N
**Intuition:** Print the name, then ask recursion to handle the remaining N-1 prints.

**Steps:**
1. Define `printName(n, name)`.
2. Base case: if `n <= 0`, return.
3. Print `name`.
4. Call `printName(n - 1, name)`.

**Dry Run Trace (N = 3, name = "Nova"):**
```
printName(3, "Nova") -> print "Nova", call printName(2, "Nova")
  printName(2, "Nova") -> print "Nova", call printName(1, "Nova")
    printName(1, "Nova") -> print "Nova", call printName(0, "Nova")
      printName(0, "Nova") -> 0 <= 0, RETURN

Output:
Nova
Nova
Nova
```

### Approach 2: Count Up from 1 to N
**Intuition:** Start from counter = 1, print name, recurse with counter + 1, stop when counter > N.

**Steps:**
1. Define `printName(i, n, name)`.
2. Base case: if `i > n`, return.
3. Print `name`.
4. Call `printName(i + 1, n, name)`.

**Dry Run Trace (N = 3, name = "Nova"):**
```
printName(1, 3, "Nova") -> print "Nova", call printName(2, 3, "Nova")
  printName(2, 3, "Nova") -> print "Nova", call printName(3, 3, "Nova")
    printName(3, 3, "Nova") -> print "Nova", call printName(4, 3, "Nova")
      printName(4, 3, "Nova") -> 4 > 3, RETURN

Output:
Nova
Nova
Nova
```

### Approach 3: Both are optimal at O(n).
Since we must print N times, O(n) is the minimum possible.

---

## 4. COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why |
|----------|------|-------|-----|
| Count Down | O(n) | O(n) | n calls, each prints once. Stack depth = n. |
| Count Up | O(n) | O(n) | Same -- n calls, n stack frames. |

*"You need to print N times. Each recursive call prints once and makes one more call. That is N calls = O(n) time. Each call sits on the stack = O(n) space."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| N = 0 | Nothing to print | Base case returns immediately |
| N = 1 | Print name once | Single call, single print |
| Empty name "" | Print empty lines | Works, but arguably should check |
| Very large N | Stack overflow | Python default limit ~1000, Java ~10K |

**Common Mistakes:**
- Forgetting the base case -- infinite recursion.
- Using `n == 0` check with `n--` decrement but forgetting that `n` is passed by value (in Java/Python), so the original call's `n` does not change.
- Mixing up parameter (decrement n vs increment counter) leading to wrong number of prints.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Why is this useful?** | It is the recursion "hello world." Builds muscle memory for base case + recursive call pattern. |
| **Can you avoid O(n) stack space?** | Not with pure recursion. Tail-call optimization (TCO) could, but Java does not support TCO. Python does not either by default. |
| **What if N is negative?** | The `n <= 0` base case handles it -- returns immediately, prints nothing. |
| **How is this different from Print 1 to N?** | The action (print) does not depend on the counter value, so the recursion direction does not matter. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Print 1 to N | Same recursion skeleton, but output depends on counter |
| Print N to 1 | Same skeleton, different print order |
| Factorial | Replace "print" with "multiply" -- same recursive structure |
| Power Function | Replace "print" with "multiply by base" |
| Recursive Tree Drawing | Each branch is a "print name N times" with N decreasing |

---

## Real-World Use Case
**Log flooding and stress testing:** Tools like Apache JMeter or Locust send repeated identical requests to simulate load. Printing a name N times mirrors sending the same API payload N times -- a base case stops the recursion once the target count is reached, just like a rate limiter caps requests.
