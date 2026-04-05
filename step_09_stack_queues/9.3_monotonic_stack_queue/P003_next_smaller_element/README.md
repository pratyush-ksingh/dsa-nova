# Next Smaller Element

> **Step 09.9.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an array of integers, for each element find the **next smaller element** to its right. The next smaller element of an element `x` is the first element to its right that is smaller than `x`. If no such element exists, return `-1`.

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[4, 5, 2, 10, 8]` | `[2, 2, -1, 8, -1]` | 4->2, 5->2, 2->none, 10->8, 8->none |
| `[1, 3, 2, 4]` | `[-1, 2, -1, -1]` | 1->none, 3->2, 2->none, 4->none |
| `[5, 4, 3, 2, 1]` | `[4, 3, 2, 1, -1]` | Strictly decreasing: each element's NSE is its right neighbor |
| `[1, 2, 3, 4, 5]` | `[-1, -1, -1, -1, -1]` | Strictly increasing: no element has a NSE |

**Constraints:**
- `1 <= n <= 10^5`
- `-10^9 <= arr[i] <= 10^9`

### Real-Life Analogy
> *Imagine standing in a queue of people of different heights. You want to know: "Who is the first shorter person standing behind me?" If nobody behind you is shorter, the answer is "nobody." A monotonic stack efficiently answers this for everyone in the queue simultaneously.*

### Key Observations
1. For each element, we need the first smaller value to its right -- this is the classic **monotonic stack** pattern.
2. A brute force scan would check every element to the right, but many of these comparisons are redundant.
3. The stack maintains a decreasing sequence of "candidates." When a new element comes in, it resolves all pending elements that are larger.

---

## APPROACH LADDER

### Approach 1: Brute Force -- Scan Right
**Intuition:** For each element, linearly scan all elements to its right until we find one that is smaller.

**Steps:**
1. For each index `i` from 0 to n-1:
   - For each index `j` from i+1 to n-1:
     - If `arr[j] < arr[i]`, set `result[i] = arr[j]` and break.
2. If no smaller element found, result stays -1.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) extra |

---

### Approach 2: Optimal -- Monotonic Stack (Right to Left)
**Intuition:** Traverse from right to left, maintaining a stack of elements in increasing order from top. For each element, pop all stack elements that are >= current (they cannot be the NSE for anything further left). The new top is the NSE. Then push the current element.

**Steps:**
1. Initialize an empty stack and result array filled with -1.
2. Traverse from index n-1 to 0:
   - While stack is non-empty and `stack.top() >= arr[i]`: pop.
   - If stack is non-empty: `result[i] = stack.top()`.
   - Push `arr[i]`.
3. Return result.

**Dry Run:** `[4, 5, 2, 10, 8]`
```
i=4: arr[4]=8,  stack=[]       -> result[4]=-1,  push 8.   stack=[8]
i=3: arr[3]=10, stack=[8]      -> result[3]=8,   push 10.  stack=[8,10]
i=2: arr[2]=2,  stack=[8,10]   -> pop 10, pop 8  -> result[2]=-1, push 2. stack=[2]
i=1: arr[1]=5,  stack=[2]      -> result[1]=2,   push 5.   stack=[2,5]
i=0: arr[0]=4,  stack=[2,5]    -> pop 5 -> result[0]=2,  push 4. stack=[2,4]
```
Result: `[2, 2, -1, 8, -1]`

**Why O(n)?** Each element is pushed and popped at most once, giving 2n total operations.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

### Approach 3: Best -- Monotonic Stack (Left to Right Variant)
**Intuition:** Same time complexity, different traversal direction. Traverse left to right, storing indices on the stack. When we encounter an element smaller than the top, it IS the NSE for the top element. Pop and assign until the stack top is no longer larger.

**Steps:**
1. Initialize stack (stores indices) and result array filled with -1.
2. For each index `i` from 0 to n-1:
   - While stack is non-empty and `arr[stack.top()] > arr[i]`: pop `idx`, set `result[idx] = arr[i]`.
   - Push `i`.
3. Remaining stack elements have no NSE (already -1).

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Using >= vs >:** The condition should be `>=` when popping (elements equal to current cannot be NSE since we want strictly smaller). But for the left-to-right variant, use `>` when determining if current is the NSE.
2. **Wrong traversal direction:** Right-to-left maintains a stack for lookup; left-to-right resolves elements as answers arrive. Mixing up the logic leads to bugs.
3. **Forgetting to push the current element:** Every element must be pushed onto the stack.

### Edge Cases
- Single element: `[5]` -> `[-1]`
- All same elements: `[3, 3, 3]` -> `[-1, -1, -1]`
- Strictly decreasing: each element's NSE is the next element
- Strictly increasing: all -1

---

## Real-World Use Case
The Next Smaller Element pattern is used in **stock price analysis** (finding the next day the price drops below current), **histogram problems** (largest rectangle in histogram uses NSE on both sides), and **temperature monitoring** (finding the next cooler day). It is a fundamental building block for monotonic stack problems.

## Interview Tips
- Immediately recognize the **monotonic stack** pattern when you see "next smaller/greater element."
- Explain why the stack is O(n) overall: each element enters and leaves the stack exactly once.
- Start with brute force to show understanding, then optimize with the stack.
- Know both directions (right-to-left and left-to-right) as interviewers may ask for variations.
- Follow-up: "What about next smaller on the LEFT?" -- Same approach, just reverse the traversal direction.
- Follow-up: "Largest Rectangle in Histogram" uses NSE on both sides.
