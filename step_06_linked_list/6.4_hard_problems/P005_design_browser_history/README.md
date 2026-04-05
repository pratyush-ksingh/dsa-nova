# Design Browser History

> **Step 06.6.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
You have a browser with one tab. You start on `homepage`. Implement the `BrowserHistory` class:
- `BrowserHistory(homepage)` -- initialize with the homepage.
- `visit(url)` -- visit `url` from the current page. All forward history is cleared.
- `back(steps)` -- move `steps` back in history. If you can only go back `x < steps`, go back `x` steps. Return the current URL.
- `forward(steps)` -- move `steps` forward in history. Clamped. Return the current URL.

**LeetCode #1472**

### Examples
| Operation | Result | Note |
|-----------|--------|------|
| `BrowserHistory("leetcode.com")` | -- | Start: leetcode.com |
| `visit("google.com")` | -- | History: leet->goog |
| `visit("facebook.com")` | -- | History: leet->goog->fb |
| `visit("youtube.com")` | -- | History: leet->goog->fb->yt |
| `back(1)` | `"facebook.com"` | Move back 1 |
| `back(1)` | `"google.com"` | Move back 1 |
| `forward(1)` | `"facebook.com"` | Move forward 1 |
| `visit("linkedin.com")` | -- | Clears yt, adds li |
| `forward(2)` | `"linkedin.com"` | Clamped, no forward pages |
| `back(2)` | `"google.com"` | Back 2 from linkedin |
| `back(7)` | `"leetcode.com"` | Clamped to beginning |

### Constraints
- `1 <= homepage.length, url.length <= 20`
- `1 <= steps <= 100`
- `At most 5000 calls to visit, back, forward`

### Real-Life Analogy
A web browser's back/forward buttons. Visiting a new page from the middle of history discards the forward stack -- exactly like clicking a link in the middle of your browser history.

### 3 Key Observations
1. **"aha" -- visit is a stack push with forward-clear:** After visiting, forward history is invalid. This is the crux of the design.
2. **"aha" -- back and forward are just pointer moves:** You never delete the history array on back/forward; you just move an index.
3. **"aha" -- array is the most cache-friendly choice:** Sequential memory access for back/forward. Doubly linked list has pointer indirection overhead.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **ArrayList + index (brute):** Simple, but `visit` needs to truncate the list (O(n) worst case).
- **Doubly Linked List (optimal):** O(1) visit by cutting forward chain. Back/forward walk the list.
- **Array + two pointers (best):** O(1) all operations. `curr` = current page, `last` = last valid page (forward boundary). Visit invalidates forward by setting `last = curr`. Cache-friendly.

### Pattern Recognition Cue
Whenever you need a history stack with back/forward, think: **two pointers in an array** (curr and last). Visit moves curr and resets last; back/forward clamp-move curr.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- ArrayList with Current Index
**Intuition:** Store all pages in a list. `curr` is the current index. On visit, truncate the list from `curr+1` onward, then append. Back and forward clamp-move `curr`.

**Steps:**
1. `__init__`: list = [homepage], curr = 0.
2. `visit(url)`: remove elements from index curr+1 to end, append url, curr++.
3. `back(steps)`: `curr = max(0, curr - steps)`, return `history[curr]`.
4. `forward(steps)`: `curr = min(len-1, curr + steps)`, return `history[curr]`.

**Complexity note:** `visit` truncation is O(n) in the worst case when the forward history is long.

| Metric | Value |
|--------|-------|
| Time   | visit O(n), back/forward O(1) |
| Space  | O(n)  |

---

### Approach 2: Optimal -- Doubly Linked List
**Intuition:** Each page is a DLL node with `prev` and `next` pointers. `curr` points to the current page. On visit, create a new node, set its `prev = curr`, set `curr.next = new_node` (this silently discards the forward chain), move `curr = new_node`. Back/forward walk the pointer.

**Steps:**
1. `__init__`: `curr = Page(homepage)`.
2. `visit(url)`: `new_node.prev = curr; curr.next = new_node; curr = new_node`.
3. `back(steps)`: walk `curr = curr.prev` up to `steps` times while `curr.prev` exists.
4. `forward(steps)`: walk `curr = curr.next` up to `steps` times while `curr.next` exists.

| Metric | Value |
|--------|-------|
| Time   | visit O(1), back/forward O(steps) |
| Space  | O(n) nodes |

---

### Approach 3: Best -- Single Array with curr and last Pointers
**Intuition:** Use a fixed-size array. `curr` is the current page index. `last` is the last valid forward index. On visit: `curr++`, store url at `history[curr]`, set `last = curr` (invalidating any prior forward pages). Back/forward clamp-move `curr`.

**Steps:**
1. `__init__`: `history[0] = homepage, curr = last = 0`.
2. `visit(url)`: `curr++; history[curr] = url; last = curr`.
3. `back(steps)`: `curr = max(0, curr - steps)`, return `history[curr]`.
4. `forward(steps)`: `curr = min(last, curr + steps)`, return `history[curr]`.

**Why best:** All three operations are O(1). Array access is cache-friendly. No node allocation overhead.

| Metric | Value |
|--------|-------|
| Time   | visit O(1), back/forward O(1) |
| Space  | O(n) -- the array |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(1) for Best:** Accessing `history[curr]` is a direct array index. Updating `curr` and `last` is arithmetic.
- **Space O(n):** All three approaches store n pages. The array approach pre-allocates but avoids fragmentation.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected | Why It Trips People |
|-----------|----------|---------------------|
| `back(100)` from start | homepage | Clamp to 0 |
| `forward(100)` from last page | last page | Clamp to `last` |
| `visit` after `back` | Clears forward | Old forward history should be gone |
| Multiple `back` then `visit` | Clears all forward | Not just one page forward |

**Common Mistakes:**
- In the array approach, using `history.length - 1` as the forward clamp instead of `last`.
- In the DLL approach, forgetting that `curr.next = new_node` discards the old forward chain (this is intentional!).
- Not clamping `curr >= 0` on back, causing negative index.

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "Browser history with visit, back (clamped), forward (clamped)."
2. **Match:** "Array with two pointers is the cleanest. `curr` = position, `last` = forward boundary."
3. **Plan:** visit sets last=curr (invalidating forward), back/forward clamp.
4. **Implement:** Show the array version as primary, mention DLL as alternative.
5. **Review:** Trace the LeetCode example.
6. **Evaluate:** O(1) all operations, O(n) space.

### Follow-Up Questions
- *"What if we need to display the full forward/back history?"* -- DLL is better since you can traverse in both directions.
- *"What if URLs can be very long?"* -- Space cost is O(n × max_url_len). Same approach, just memory usage increases.
- *"Can you implement undo/redo with the same structure?"* -- Yes! Back = undo, forward = redo, visit = perform action. Same array+pointer pattern.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Design Stack / Queue -- basic ADT design |
| **Same Pattern** | LRU Cache (LC #146) -- design with custom linked list |
| **Same Pattern** | Undo/Redo functionality -- browser history analog |
| **Harder** | Browser History with Tab Support -- extend to multiple history stacks |
