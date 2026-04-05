# Sieve of Eratosthenes

> **Step 08.8.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement
TODO - Add problem description here

## Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| TODO  | TODO   | TODO        |

## Constraints
- TODO

---

## Approach 1: Brute Force
**Intuition:** TODO

**Steps:**
1. TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Approach 2: Optimal
**Intuition:** TODO

**Steps:**
1. TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Approach 3: Best
**Intuition:** TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Real-World Use Case
OpenSSL and BoringSSL (used by Chrome) use the Sieve of Eratosthenes to generate small prime lookup tables that accelerate primality testing during RSA key generation. Before running expensive Miller-Rabin tests on large candidate primes, the library checks divisibility against sieved primes up to 10,000, eliminating over 90% of composite candidates instantly.

## Interview Tips
- TODO
