# Longest String Chain

> **Step 16.16.7** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #1048)** You are given an array of `words` where each word consists of lowercase English letters. Word `wordA` is a **predecessor** of `wordB` if you can insert exactly one letter anywhere in `wordA` to make it equal to `wordB`. A **word chain** is a sequence of words where each word is a predecessor of the next. Return the **length of the longest possible word chain**.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| ["a","b","ba","bca","bda","bdca"] | 4 | Chain: "a" -> "ba" -> "bda" -> "bdca" |
| ["xbc","pcxbcf","xb","cxbc","pcxbc"] | 5 | Chain: "xb" -> "xbc" -> "cxbc" -> "pcxbc" -> "pcxbcf" |
| ["abcd","dbqca"] | 1 | No valid chain between the two words |

### Constraints
- `1 <= words.length <= 1000`
- `1 <= words[i].length <= 16`
- `words[i]` consists of lowercase English letters only

### Key Insight
Instead of trying to insert a character into a shorter word to check if it matches a longer word, it is more efficient to **remove** each character from the longer word and check if the resulting string exists in our set.

---

## APPROACH LADDER

### Approach 1: Brute Force -- Sort + Pairwise DP

**Intuition:** Sort words by length. Use LIS-style DP: for each word, check all shorter words to see if they are predecessors. This requires an O(L) check per pair.

**Steps:**
1. Sort words by length
2. `dp[i]` = longest chain ending at word `i`
3. For each word `i`, check all `j < i`: if `words[j]` is predecessor of `words[i]`, update `dp[i]`
4. Predecessor check: two-pointer with at most one skip
5. Return max of dp array

```
Dry-run: ["a","b","ba","bca","bda","bdca"]
Sorted by length: ["a","b","ba","bca","bda","bdca"]

dp = [1, 1, 1, 1, 1, 1]
i=2 "ba": j=0 "a" is predecessor -> dp[2]=2
i=3 "bca": j=2 "ba" is predecessor -> dp[3]=3
i=4 "bda": j=2 "ba" is predecessor -> dp[4]=3
i=5 "bdca": j=3 "bca" is predecessor -> dp[5]=4
             j=4 "bda" is predecessor -> dp[5]=max(4,4)=4

Answer = 4
```

| Metric | Value |
|--------|-------|
| Time   | O(n^2 * L) -- n^2 pairs, L per predecessor check |
| Space  | O(n) |

---

### Approach 2: Optimal -- Sort + HashMap DP

**Intuition:** Instead of checking all pairs, for each word generate all possible predecessors (by removing one character at each position) and look them up in a HashMap. This trades the O(n) inner loop for O(L) predecessor generations, each costing O(L) for string creation.

**Steps:**
1. Sort words by length
2. HashMap `dp`: word -> longest chain ending at this word
3. For each word, try removing each of its L characters
4. If the resulting predecessor exists in `dp`, update: `dp[word] = max(dp[word], dp[pred] + 1)`
5. Return maximum value in dp

```
Dry-run: ["a","b","ba","bca","bda","bdca"]

"a": no pred found, dp["a"]=1
"b": no pred found, dp["b"]=1
"ba": remove 'b'->"a" exists! dp["ba"]=2. remove 'a'->"b" exists! dp["ba"]=max(2,2)=2
"bca": remove 'b'->"ca" no. remove 'c'->"ba" yes! dp["bca"]=3. remove 'a'->"bc" no.
"bda": remove 'b'->"da" no. remove 'd'->"ba" yes! dp["bda"]=3. remove 'a'->"bd" no.
"bdca": remove 'b'->"dca" no. remove 'd'->"bca" yes! dp["bdca"]=4.
        remove 'c'->"bda" yes! dp["bdca"]=max(4,4)=4. remove 'a'->"bdc" no.

Answer = 4
```

| Metric | Value |
|--------|-------|
| Time   | O(n * L^2) -- n words, L predecessors each costing O(L) to create |
| Space  | O(n * L) -- HashMap storing all words |

---

### Approach 3: Best -- Same HashMap Approach

**Intuition:** The HashMap approach is already optimal for this problem. The O(n * L^2) time dominates O(n^2 * L) when L << n, which is the typical case (L <= 16, n <= 1000). No further algorithmic improvement is known.

| Metric | Value |
|--------|-------|
| Time   | O(n * L^2) |
| Space  | O(n * L) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| Single word | Answer = 1 | No chain possible |
| All same length | Answer = 1 | No predecessor relationships |
| All words form one chain | Answer = n | Each is predecessor of next |
| Duplicate words | Each has chain length 1 | Duplicates are not predecessors |

**Common Mistakes:**
- Forgetting to sort by length (processing longer words before shorter ones gives wrong dp values)
- Checking insertion instead of deletion (deletion is simpler and equivalent)
- Not handling the case where multiple predecessors exist (must take max)

---

## Real-World Use Case
**Evolutionary linguistics:** Tracking how words evolve over time by single-character additions. Finding the longest chain of word evolution helps linguists understand language development patterns. Similarly used in bioinformatics to track mutation chains in DNA sequences.

## Interview Tips
- Recognize this as a variant of Longest Increasing Subsequence (LIS) on strings
- The key optimization: generate predecessors by deletion rather than checking all pairs
- Sorting by length is essential -- ensures we process shorter words first
- The HashMap lookup makes predecessor checking O(1) amortized
- Mention that L <= 16 makes O(L^2) per word very fast in practice
- Follow-up: What if we want the actual chain? Track parent pointers in the HashMap
