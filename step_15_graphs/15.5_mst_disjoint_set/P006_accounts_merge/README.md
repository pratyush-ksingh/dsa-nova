# Accounts Merge

> **Step 15.5** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #721)** Given a list of `accounts` where `accounts[i] = [name_i, email_1, email_2, ...]`, **merge** accounts that belong to the same person (i.e., they share at least one email address).

Note: Two accounts may have the same name but belong to different people (different email sets). Merging is transitive: if A and B share an email, and B and C share an email, then A, B, C all belong to the same person.

Return the merged accounts in any order. Each account should be formatted as `[name, email_1, email_2, ...]` with the emails **sorted lexicographically**.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [["John","js@m.com","jny@m.com"],["John","js@m.com","j00@m.com"],["Mary","mary@m.com"],["John","jb@m.com"]] | [["John","j00@m.com","jny@m.com","js@m.com"],["John","jb@m.com"],["Mary","mary@m.com"]] | First two Johns share js@m.com; third John has no overlap |

```
Accounts:
  [John, johnsmith@mail.com, john_newyork@mail.com]   <- account 0
  [John, johnsmith@mail.com, john00@mail.com]          <- account 1 (shares johnsmith@)
  [Mary, mary@mail.com]                                <- account 2
  [John, johnnybravo@mail.com]                         <- account 3

Merge 0 and 1 (shared johnsmith@mail.com):
  [John, john00@mail.com, john_newyork@mail.com, johnsmith@mail.com]

Account 2 and 3 have no overlap with anything:
  [Mary, mary@mail.com]
  [John, johnnybravo@mail.com]
```

### Constraints
- `1 <= accounts.length <= 1000`
- `2 <= accounts[i].length <= 10`
- `1 <= accounts[i][j].length <= 30`
- `accounts[i][0]` is a name consisting of English letters
- `accounts[i][j]` for `j > 0` is an email in valid format

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Grouping by shared property | Union-Find | Transitively group accounts sharing emails |
| Alternative | BFS/DFS on email graph | Build graph of email -> emails in same account, BFS connected components |
| Final step | Sort emails per group | Problem requirement |
| Complexity | Union-Find preferred | Near-linear vs O(n^2) brute force |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Pairwise account comparison with repeated merging

**Intuition:** Repeatedly scan all pairs of accounts. If two share any email, merge them. Repeat until a full pass produces no merges (convergence).

**Steps:**
1. Convert each account to `(name, Set<email>)`
2. Repeat:
   a. For each pair `(i, j)` where `i < j`:
      - If `emails_i ∩ emails_j ≠ ∅`, merge: `emails_i = emails_i ∪ emails_j`, remove j
   b. If no merge happened in this pass, stop
3. Build result: for each group, prepend name, sort emails

**BUD Analysis:**
- **Bottleneck:** O(n^2) pairs checked per pass, O(n) passes
- **Unnecessary:** Re-checking pairs that haven't changed
- **Duplicated:** Same email intersection computed multiple times

| Metric | Value |
|--------|-------|
| Time   | O(n^2 * m) per pass * O(n) passes = O(n^3 * m) worst case |
| Space  | O(n * m) -- email sets |

---

### Approach 2: Optimal -- Union-Find on account indices

**Intuition:** Use Union-Find where the elements being unioned are **account indices** (0 to n-1). A shared email links two accounts together. Map each email to the first account index that claims it. When a second account claims the same email, union their indices.

**Steps:**
1. Initialize DSU with n elements (one per account)
2. Iterate over accounts; for each email:
   - If `email_to_account[email]` exists: `union(current_account_index, email_to_account[email])`
   - Else: `email_to_account[email] = current_account_index`
3. Group all emails by their root account index: `root -> Set<email>`
4. For each root, prepend the account name and sort emails

```
Dry-run with example:

Account 0: John [johnsmith@, john_newyork@]
Account 1: John [johnsmith@, john00@]
Account 2: Mary [mary@]
Account 3: John [johnnybravo@]

Process account 0:
  johnsmith@ -> not seen -> email_to_account[johnsmith@] = 0
  john_newyork@ -> not seen -> email_to_account[john_newyork@] = 0

Process account 1:
  johnsmith@ -> seen at acc 0 -> union(1, 0)  [now 0 and 1 are same component]
  john00@ -> not seen -> email_to_account[john00@] = 1

Process account 2:
  mary@ -> not seen -> email_to_account[mary@] = 2

Process account 3:
  johnnybravo@ -> not seen -> email_to_account[johnnybravo@] = 3

Roots: find(0)=0, find(1)=0, find(2)=2, find(3)=3

Group by root:
  root 0: {johnsmith@, john_newyork@, john00@} -> John
  root 2: {mary@} -> Mary
  root 3: {johnnybravo@} -> John

Output (sorted emails per group):
  [John, john00@, john_newyork@, johnsmith@]
  [John, johnnybravo@]
  [Mary, mary@]  ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(n * m * alpha(n)) -- n accounts, m emails each, near-O(1) DSU ops |
| Space  | O(n * m) -- email map + DSU arrays |

---

### Approach 3: Best -- Union-Find on email strings directly

**Intuition:** Instead of unioning integer indices, use a `HashMap<String, String>` DSU where each email maps to its parent email. Within each account, union all emails with the first email. Then group by root email.

**Advantage:** No need to track account indices at all -- email strings serve as the DSU elements directly.

**Steps:**
1. Initialize: for each account, for each email `e`, set `parent[e] = e`, `email_to_name[e] = name`
2. For each account, union all emails with `acc[1]` (first email)
3. Group emails by root (using `find(email)`)
4. For each root, prepend `email_to_name[root]` and sort

| Metric | Value |
|--------|-------|
| Time   | O(n * m * alpha(n * m)) -- same as approach 2 asymptotically |
| Space  | O(n * m) -- HashMap DSU |

---

## COMPLEXITY INTUITIVELY

**Why Union-Find?** The relationship "same person" is transitive and symmetric -- exactly what connected components in a graph capture, which is what Union-Find tracks efficiently.

**Why not BFS/DFS?** You CAN use BFS/DFS: build a graph where each account's emails are fully connected, then find connected components. Union-Find is typically simpler to code and slightly faster in practice.

**Why alpha(n) not O(log n)?** Path compression + union by rank gives inverse Ackermann amortized time, which is effectively O(1) for all practical input sizes.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| Same name, different people | Not merged (no shared email) | Name is NOT the merge key; only shared email triggers merge |
| Account with one email | Stands alone unless another account shares it | Works naturally |
| Transitive merges | A-B share email, B-C share email => A-B-C merged | Union-Find handles transitivity automatically |
| All accounts same person | All get merged into one group | Single root |

**Common Mistakes:**
- Merging by name instead of shared email (wrong!)
- Forgetting to sort emails in the output
- Not handling the case where `email_to_account` already maps an email (the union step)
- Using account name as the key in the email map (same name != same person)

---

## Real-World Use Case

**Identity Resolution / Entity Deduplication:** In databases, the same person may have multiple accounts (different usernames, same email). This algorithm merges them. Used in customer master data management (MDM), fraud detection (linking accounts by shared attributes), and social network graph construction.

**Contact Merging:** Phone contacts with overlapping emails or phone numbers get merged -- this is the exact problem. Used in Google Contacts, Apple Contacts, LinkedIn profile deduplication.

---

## Interview Tips
- State the key insight immediately: **merge key is shared email, not name**. Interviewers want to see you catch this.
- Explain why Union-Find is the natural data structure: we're grouping elements (accounts) by a shared property (email), which is exactly connected components.
- Walk through a small dry-run showing the `email_to_account` map and union operations.
- Mention the BFS/DFS alternative: build a graph (each email in an account is an edge between account nodes), then BFS connected components. Same complexity.
- Follow-up: "What if accounts can be merged by phone number OR email?" -- Add phone numbers to the DSU alongside emails; same algorithm.
- Follow-up: "What if merging creates cycles?" -- Union-Find handles cycles inherently (find() detects same root).
