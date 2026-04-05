# Isomorphic Strings

> **Batch 2 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given two strings `s` and `t`, determine if they are **isomorphic**. Two strings are isomorphic if the characters in `s` can be replaced to get `t`, where:
- Each character in `s` maps to **exactly one** character in `t` (and vice versa).
- The mapping must be **one-to-one** (no two characters in `s` map to the same character in `t`).
- The **order of characters is preserved**.

**LeetCode #205**

**Constraints:**
- `1 <= s.length <= 5 * 10^4`
- `s.length == t.length`
- `s` and `t` consist of any valid ASCII characters.

**Examples:**

| Input (s, t) | Output | Explanation |
|--------------|--------|-------------|
| `"egg", "add"` | `true` | e->a, g->d. One-to-one mapping. |
| `"foo", "bar"` | `false` | o would need to map to both 'a' and 'r'. |
| `"paper", "title"` | `true` | p->t, a->i, e->l, r->e. One-to-one. |
| `"badc", "baba"` | `false` | d->b and b->b means two chars map to 'b'. |
| `"ab", "aa"` | `false` | a->a, b->a. Two chars map to 'a' (not one-to-one). |

### Real-Life Analogy
> *Think of a substitution cipher, like the ones used in spy movies. Each letter in the plaintext is replaced by exactly one letter in the ciphertext, and no two plaintext letters map to the same ciphertext letter. If you try to decode "egg" and get "add", the cipher works: e=a, g=d. But if "foo" should decode to "bar", the cipher breaks because 'o' would need to be both 'a' and 'r' at the same time. Isomorphism checks if a valid cipher exists.*

### Key Observations
1. We need a **bidirectional mapping**: s[i] -> t[i] AND t[i] -> s[i]. Checking only one direction misses cases like `"ab" -> "aa"`. <-- This is the "aha" insight
2. Strings must be the same length (given by constraints).
3. The mapping is defined by position: s[i] always maps to t[i] for all occurrences.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- **Two HashMaps** (or arrays for ASCII): one for s->t mapping, one for t->s mapping. O(1) lookup per character.
- Alternatively, we can encode the "pattern" of each string and compare patterns.

### Pattern Recognition
- **Pattern:** Dual HashMap / Character Mapping
- **Classification Cue:** "When you see _check if two sequences have the same structure_ --> think _map characters bidirectionally and verify consistency_"

---

## APPROACH LADDER

### Approach 1: Brute Force -- Transform to Pattern
**Idea:** Convert each string to a canonical "pattern" based on first-occurrence indices, then compare patterns.

For `"egg"`: e is first seen at 0, g at 1, g at 1 --> pattern = `[0, 1, 1]`
For `"add"`: a is first seen at 0, d at 1, d at 1 --> pattern = `[0, 1, 1]`
Patterns match --> isomorphic.

**Steps:**
1. Build pattern for `s`: for each char, record the index of its first occurrence.
2. Build pattern for `t` the same way.
3. Return whether the two patterns are equal.

**Why it works:** Two strings are isomorphic if and only if their first-occurrence patterns are identical.

| Time | Space |
|------|-------|
| O(n) | O(n) for storing the patterns |

### Approach 2: Optimal -- Dual HashMap
**What changed:** Instead of building intermediate patterns, validate the mapping on the fly with two hash maps.

**Steps:**
1. Create `map_st` (s -> t) and `map_ts` (t -> s).
2. For each index `i`:
   - If `s[i]` is in `map_st`:
     - If `map_st[s[i]] != t[i]`, return `false`.
   - Else: set `map_st[s[i]] = t[i]`.
   - If `t[i]` is in `map_ts`:
     - If `map_ts[t[i]] != s[i]`, return `false`.
   - Else: set `map_ts[t[i]] = s[i]`.
3. Return `true`.

**Dry Run:** `s = "paper"`, `t = "title"`

| i | s[i] | t[i] | map_st | map_ts | Action |
|---|------|------|--------|--------|--------|
| 0 | p | t | {p:t} | {t:p} | New mapping |
| 1 | a | i | {p:t, a:i} | {t:p, i:a} | New mapping |
| 2 | p | t | check p->t? Yes | check t->p? Yes | Consistent |
| 3 | e | l | {p:t, a:i, e:l} | {t:p, i:a, l:e} | New mapping |
| 4 | r | e | {p:t, a:i, e:l, r:e} | {t:p, i:a, l:e, e:r} | New mapping |

**Result:** `true`

**Dry Run 2:** `s = "ab"`, `t = "aa"`

| i | s[i] | t[i] | map_st | map_ts | Action |
|---|------|------|--------|--------|--------|
| 0 | a | a | {a:a} | {a:a} | New mapping |
| 1 | b | a | -- | map_ts[a] = a != b | Return false! |

**Result:** `false` (caught by reverse mapping)

| Time | Space |
|------|-------|
| O(n) | O(k) where k = size of character set (at most 256 for ASCII) |

### Approach 3: Elegant -- Last-Seen Index Comparison
**Idea:** For each position, compare when `s[i]` was last seen in `s` vs when `t[i]` was last seen in `t`. If isomorphic, these must always match.

**Steps:**
1. Create two arrays `last_s[256]` and `last_t[256]`, initialized to -1.
2. For each index `i`:
   - If `last_s[s[i]] != last_t[t[i]]`, return `false`.
   - Set `last_s[s[i]] = i` and `last_t[t[i]] = i`.
3. Return `true`.

| Time | Space |
|------|-------|
| O(n) | O(1) -- fixed 256-size arrays |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We scan both strings once in lockstep, doing O(1) map operations per character."
**Space:** O(1) effectively -- "The maps hold at most 256 entries (ASCII character set), which is constant regardless of string length."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Only checking one direction (s->t) without checking (t->s). This misses cases like `"ab"->"aa"`.
2. Using a single map and forgetting that different keys can't map to the same value.
3. Assuming strings are only lowercase letters -- the problem allows any ASCII character.

### Edge Cases to Test
- [ ] Single character `"a", "b"` --> `true`
- [ ] Same string `"abc", "abc"` --> `true`
- [ ] All same chars `"aaa", "bbb"` --> `true`
- [ ] Forward conflict `"foo", "bar"` --> `false` (o maps to a and r)
- [ ] Reverse conflict `"ab", "aa"` --> `false` (a and b both map to a)
- [ ] Full alphabet mapping `"abcdefg", "bcdefga"` --> `true`

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to check if there's a valid one-to-one character mapping from s to t. The mapping must be bidirectional."
2. **Match:** "This is a character mapping problem -- I need two hash maps."
3. **Plan:** "I'll maintain two maps: s->t and t->s. At each position, I verify consistency or add a new mapping."
4. **Implement:** Write the dual-map loop.
5. **Review:** Trace through "paper"/"title" and "ab"/"aa" to show both pass and fail cases.
6. **Evaluate:** "O(n) time, O(1) space (bounded by charset size)."

### Follow-Up Questions
- "What about word-level isomorphism?" --> LC #290 Word Pattern. Same idea but map words instead of characters.
- "What if you need to find all isomorphic pairs in a list?" --> Group strings by their canonical pattern (Approach 1).
- "How does this relate to group isomorphism in algebra?" --> Same concept: a bijective structure-preserving mapping.

---

## CONNECTIONS
- **Prerequisite:** HashMap basics, string traversal
- **Same Pattern:** Word Pattern (LC #290), Anagram checking
- **Related:** Character frequency problems, encoding/decoding
- **This Unlocks:** Understanding bijective mappings, which appear in cipher problems and graph isomorphism
