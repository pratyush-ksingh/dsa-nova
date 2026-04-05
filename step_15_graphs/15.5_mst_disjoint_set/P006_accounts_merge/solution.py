"""
Problem: Accounts Merge (LeetCode #721)
Difficulty: MEDIUM | XP: 25

Given a list of accounts where accounts[i] = [name, email1, email2, ...],
merge accounts that share at least one email. Return merged accounts with
emails sorted and the account name prepended. Two accounts belong to the
same person if they share any email.
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE -- Pairwise account comparison
# Time: O(n^2 * m) where n=accounts, m=avg emails per account
# Space: O(n * m)
# For each pair of accounts, check if they share any email.
# If yes, merge them. Repeat until no more merges are possible.
# ============================================================
def brute_force(accounts: List[List[str]]) -> List[List[str]]:
    # Represent each account as a set of emails (index 0 is the name)
    groups = [(acc[0], set(acc[1:])) for acc in accounts]
    merged = True

    while merged:
        merged = False
        new_groups = []
        used = [False] * len(groups)

        for i in range(len(groups)):
            if used[i]:
                continue
            name_i, emails_i = groups[i]
            for j in range(i + 1, len(groups)):
                if used[j]:
                    continue
                name_j, emails_j = groups[j]
                if emails_i & emails_j:  # common email exists
                    emails_i = emails_i | emails_j
                    used[j] = True
                    merged = True
            new_groups.append((name_i, emails_i))
            used[i] = True

        groups = new_groups

    result = []
    for name, emails in groups:
        result.append([name] + sorted(emails))
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Union-Find (DSU) on account indices
# Time: O(n * m * alpha(n))  |  Space: O(n * m)
# Map each email to the index of the FIRST account that owns it.
# For each subsequent account that also owns that email, union the
# two account indices. Collect all emails per root and sort.
# ============================================================
def optimal(accounts: List[List[str]]) -> List[List[str]]:
    n = len(accounts)
    parent = list(range(n))
    rank = [0] * n

    def find(x: int) -> int:
        if parent[x] != x:
            parent[x] = find(parent[x])  # path compression
        return parent[x]

    def union(x: int, y: int) -> None:
        rx, ry = find(x), find(y)
        if rx == ry:
            return
        if rank[rx] < rank[ry]:
            parent[rx] = ry
        elif rank[rx] > rank[ry]:
            parent[ry] = rx
        else:
            parent[ry] = rx
            rank[rx] += 1

    # Map email -> first account index that owns it
    email_to_account: dict = {}

    for i, acc in enumerate(accounts):
        for email in acc[1:]:
            if email in email_to_account:
                # This email was seen before -> same person -> union
                union(i, email_to_account[email])
            else:
                email_to_account[email] = i

    # Group all emails by root account index
    root_to_emails: dict = defaultdict(set)
    for email, acc_idx in email_to_account.items():
        root = find(acc_idx)
        root_to_emails[root].add(email)

    # Build result: [name, sorted emails...]
    result = []
    for root, emails in root_to_emails.items():
        name = accounts[root][0]
        result.append([name] + sorted(emails))

    return result


# ============================================================
# APPROACH 3: BEST -- Union-Find with email-string DSU (no index)
# Time: O(n * m * alpha(n * m))  |  Space: O(n * m)
# Instead of unioning account indices, union email strings directly.
# Each email's root is mapped to an account name for reconstruction.
# ============================================================
def best(accounts: List[List[str]]) -> List[List[str]]:
    parent: dict = {}

    def find(x: str) -> str:
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]

    def union(x: str, y: str) -> None:
        rx, ry = find(x), find(y)
        if rx != ry:
            parent[rx] = ry

    email_to_name: dict = {}

    for acc in accounts:
        name = acc[0]
        first_email = acc[1]
        # Initialize all emails from this account
        for email in acc[1:]:
            if email not in parent:
                parent[email] = email
            email_to_name[email] = name
            # Union all emails in this account with the first one
            union(first_email, email)

    # Group by root
    root_to_emails: dict = defaultdict(list)
    for email in parent:
        root = find(email)
        root_to_emails[root].append(email)

    result = []
    for root, emails in root_to_emails.items():
        name = email_to_name[root]
        result.append([name] + sorted(emails))

    return result


if __name__ == "__main__":
    print("=== Accounts Merge ===\n")

    accounts1 = [
        ["John","johnsmith@mail.com","john_newyork@mail.com"],
        ["John","johnsmith@mail.com","john00@mail.com"],
        ["Mary","mary@mail.com"],
        ["John","johnnybravo@mail.com"]
    ]

    r1 = brute_force([acc[:] for acc in accounts1])
    r2 = optimal([acc[:] for acc in accounts1])
    r3 = best([acc[:] for acc in accounts1])

    print("Brute:")
    for acc in sorted(r1): print(" ", acc)

    print("\nOptimal:")
    for acc in sorted(r2): print(" ", acc)

    print("\nBest:")
    for acc in sorted(r3): print(" ", acc)

    # Expected:
    # ["John","john00@mail.com","john_newyork@mail.com","johnsmith@mail.com"]
    # ["John","johnnybravo@mail.com"]
    # ["Mary","mary@mail.com"]
