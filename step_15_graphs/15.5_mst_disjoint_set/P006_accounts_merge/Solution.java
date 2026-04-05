import java.util.*;

/**
 * Problem: Accounts Merge (LeetCode #721)
 * Difficulty: MEDIUM | XP: 25
 *
 * Merge accounts that share at least one email address.
 * Return merged accounts with the account name and sorted email list.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Pairwise account comparison
    // Time: O(n^2 * m)  |  Space: O(n * m)
    // Repeatedly check every pair of accounts for shared emails.
    // Merge and repeat until no more merges occur (convergence).
    // ============================================================
    public static List<List<String>> bruteForce(List<List<String>> accounts) {
        // Represent each account as (name, Set<email>)
        List<String> names = new ArrayList<>();
        List<Set<String>> emailSets = new ArrayList<>();

        for (List<String> acc : accounts) {
            names.add(acc.get(0));
            Set<String> emails = new HashSet<>(acc.subList(1, acc.size()));
            emailSets.add(emails);
        }

        boolean merged = true;
        while (merged) {
            merged = false;
            List<String> newNames = new ArrayList<>();
            List<Set<String>> newSets = new ArrayList<>();
            boolean[] used = new boolean[emailSets.size()];

            for (int i = 0; i < emailSets.size(); i++) {
                if (used[i]) continue;
                String name = names.get(i);
                Set<String> emails = new HashSet<>(emailSets.get(i));

                for (int j = i + 1; j < emailSets.size(); j++) {
                    if (used[j]) continue;
                    // Check overlap
                    boolean overlap = false;
                    for (String e : emailSets.get(j)) {
                        if (emails.contains(e)) { overlap = true; break; }
                    }
                    if (overlap) {
                        emails.addAll(emailSets.get(j));
                        used[j] = true;
                        merged = true;
                    }
                }
                newNames.add(name);
                newSets.add(emails);
                used[i] = true;
            }
            names = newNames;
            emailSets = newSets;
        }

        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            List<String> sorted = new ArrayList<>(emailSets.get(i));
            Collections.sort(sorted);
            sorted.add(0, names.get(i));
            result.add(sorted);
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Union-Find on account indices
    // Time: O(n * m * alpha(n))  |  Space: O(n * m)
    // Map each email to the index of the first account that owns it.
    // Union account indices when they share an email. Group emails
    // by root account index and sort.
    // ============================================================
    public static List<List<String>> optimal(List<List<String>> accounts) {
        int n = accounts.size();
        int[] parent = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        // Map email -> first account index that contains it
        Map<String, Integer> emailToAccount = new HashMap<>();

        for (int i = 0; i < n; i++) {
            List<String> acc = accounts.get(i);
            for (int j = 1; j < acc.size(); j++) {
                String email = acc.get(j);
                if (emailToAccount.containsKey(email)) {
                    union(parent, rank, i, emailToAccount.get(email));
                } else {
                    emailToAccount.put(email, i);
                }
            }
        }

        // Group emails by root account index
        Map<Integer, List<String>> rootToEmails = new HashMap<>();
        for (Map.Entry<String, Integer> entry : emailToAccount.entrySet()) {
            int root = find(parent, entry.getValue());
            rootToEmails.computeIfAbsent(root, x -> new ArrayList<>()).add(entry.getKey());
        }

        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry : rootToEmails.entrySet()) {
            int root = entry.getKey();
            List<String> emails = entry.getValue();
            Collections.sort(emails);
            emails.add(0, accounts.get(root).get(0)); // prepend name
            result.add(emails);
        }
        return result;
    }

    private static int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]); // path compression
        return parent[x];
    }

    private static void union(int[] parent, int[] rank, int x, int y) {
        int rx = find(parent, x), ry = find(parent, y);
        if (rx == ry) return;
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
    }

    // ============================================================
    // APPROACH 3: BEST -- Union-Find on email strings directly
    // Time: O(n * m * alpha(n * m))  |  Space: O(n * m)
    // Union email strings directly using a HashMap-based DSU.
    // Each email's root email maps to the account name.
    // Avoids integer index bookkeeping.
    // ============================================================
    public static List<List<String>> best(List<List<String>> accounts) {
        Map<String, String> parent = new HashMap<>();
        Map<String, String> emailToName = new HashMap<>();

        // Initialize and union within each account
        for (List<String> acc : accounts) {
            String name = acc.get(0);
            String firstEmail = acc.get(1);
            for (int j = 1; j < acc.size(); j++) {
                String email = acc.get(j);
                parent.putIfAbsent(email, email);
                emailToName.put(email, name);
                unionStr(parent, firstEmail, email);
            }
        }

        // Group by root
        Map<String, List<String>> rootToEmails = new HashMap<>();
        for (String email : parent.keySet()) {
            String root = findStr(parent, email);
            rootToEmails.computeIfAbsent(root, x -> new ArrayList<>()).add(email);
        }

        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : rootToEmails.entrySet()) {
            String root = entry.getKey();
            List<String> emails = entry.getValue();
            Collections.sort(emails);
            emails.add(0, emailToName.get(root));
            result.add(emails);
        }
        return result;
    }

    private static String findStr(Map<String, String> parent, String x) {
        if (!parent.get(x).equals(x))
            parent.put(x, findStr(parent, parent.get(x)));
        return parent.get(x);
    }

    private static void unionStr(Map<String, String> parent, String x, String y) {
        String rx = findStr(parent, x), ry = findStr(parent, y);
        if (!rx.equals(ry)) parent.put(rx, ry);
    }

    public static void main(String[] args) {
        System.out.println("=== Accounts Merge ===\n");

        List<List<String>> accounts = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList("John","johnsmith@mail.com","john_newyork@mail.com")),
            new ArrayList<>(Arrays.asList("John","johnsmith@mail.com","john00@mail.com")),
            new ArrayList<>(Arrays.asList("Mary","mary@mail.com")),
            new ArrayList<>(Arrays.asList("John","johnnybravo@mail.com"))
        ));

        System.out.println("Brute:");
        List<List<String>> r1 = bruteForce(new ArrayList<>(accounts));
        r1.sort(Comparator.comparing(a -> a.toString()));
        r1.forEach(System.out::println);

        System.out.println("\nOptimal:");
        List<List<String>> r2 = optimal(new ArrayList<>(accounts));
        r2.sort(Comparator.comparing(a -> a.toString()));
        r2.forEach(System.out::println);

        System.out.println("\nBest:");
        List<List<String>> r3 = best(new ArrayList<>(accounts));
        r3.sort(Comparator.comparing(a -> a.toString()));
        r3.forEach(System.out::println);
    }
}
