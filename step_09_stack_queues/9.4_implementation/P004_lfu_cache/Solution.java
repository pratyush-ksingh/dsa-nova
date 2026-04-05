/**
 * Problem: LFU Cache
 * Difficulty: HARD | XP: 50
 *
 * Design a Least Frequently Used (LFU) cache. get and put must be O(1).
 * When capacity is full, evict the entry with the lowest frequency.
 * Ties broken by least recently used.
 *
 * Core idea: HashMap<key,value> + HashMap<key,freq> + HashMap<freq, LinkedHashSet<key>>
 * Track minFreq so we always know which bucket to evict from.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Frequency map + sort on eviction
    // Time: O(n log n) per put (eviction)  |  Space: O(capacity)
    // Maintain freq map; on eviction, find min-freq key by linear scan
    // ============================================================
    static class LFUCacheBrute {
        private final int cap;
        private final Map<Integer, Integer> vals;
        private final Map<Integer, Integer> freq;
        private final Map<Integer, Long> lastUsed;
        private long time;

        public LFUCacheBrute(int capacity) {
            cap = capacity;
            vals = new HashMap<>();
            freq = new HashMap<>();
            lastUsed = new HashMap<>();
            time = 0;
        }

        public int get(int key) {
            if (!vals.containsKey(key)) return -1;
            freq.merge(key, 1, Integer::sum);
            lastUsed.put(key, time++);
            return vals.get(key);
        }

        public void put(int key, int value) {
            if (cap <= 0) return;
            if (vals.containsKey(key)) {
                vals.put(key, value);
                get(key);  // updates freq and time
                vals.put(key, value); // restore after get
                return;
            }
            if (vals.size() == cap) {
                // evict: lowest freq, then oldest (smallest lastUsed)
                int evictKey = vals.keySet().stream()
                    .min(Comparator.comparingInt((Integer k) -> freq.getOrDefault(k, 0))
                         .thenComparingLong(k -> lastUsed.getOrDefault(k, Long.MAX_VALUE)))
                    .orElseThrow();
                vals.remove(evictKey);
                freq.remove(evictKey);
                lastUsed.remove(evictKey);
            }
            vals.put(key, value);
            freq.put(key, 1);
            lastUsed.put(key, time++);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Three HashMaps + minFreq tracking
    // Time: O(1)  |  Space: O(capacity)
    // keyVal: key->value, keyFreq: key->freq,
    // freqKeys: freq->LinkedHashSet<key> (insertion-ordered for LRU tie-break)
    // minFreq tracks current minimum so eviction is O(1)
    // ============================================================
    static class LFUCacheOptimal {
        private final int cap;
        private int minFreq;
        private final Map<Integer, Integer> keyVal;
        private final Map<Integer, Integer> keyFreq;
        private final Map<Integer, LinkedHashSet<Integer>> freqKeys;

        public LFUCacheOptimal(int capacity) {
            cap = capacity;
            minFreq = 0;
            keyVal  = new HashMap<>();
            keyFreq = new HashMap<>();
            freqKeys = new HashMap<>();
        }

        public int get(int key) {
            if (!keyVal.containsKey(key)) return -1;
            incrementFreq(key);
            return keyVal.get(key);
        }

        public void put(int key, int value) {
            if (cap <= 0) return;
            if (keyVal.containsKey(key)) {
                keyVal.put(key, value);
                incrementFreq(key);
            } else {
                if (keyVal.size() == cap) evict();
                keyVal.put(key, value);
                keyFreq.put(key, 1);
                freqKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
                minFreq = 1;
            }
        }

        private void incrementFreq(int key) {
            int f = keyFreq.get(key);
            keyFreq.put(key, f + 1);
            freqKeys.get(f).remove(key);
            if (freqKeys.get(f).isEmpty()) {
                freqKeys.remove(f);
                if (minFreq == f) minFreq = f + 1;
            }
            freqKeys.computeIfAbsent(f + 1, k -> new LinkedHashSet<>()).add(key);
        }

        private void evict() {
            LinkedHashSet<Integer> minSet = freqKeys.get(minFreq);
            int lru = minSet.iterator().next(); // oldest in this freq bucket
            minSet.remove(lru);
            if (minSet.isEmpty()) freqKeys.remove(minFreq);
            keyVal.remove(lru);
            keyFreq.remove(lru);
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Same as Optimal with DLL nodes instead of LinkedHashSet
    // Time: O(1)  |  Space: O(capacity)
    // Each frequency bucket uses a true doubly-linked list for constant-time
    // add/remove. This is the implementation used in high-performance systems.
    // ============================================================
    static class LFUCacheBest {
        static class DNode {
            int key, val, freq;
            DNode prev, next;
            DNode(int k, int v, int f) { key = k; val = v; freq = f; }
        }

        static class DList {
            DNode head, tail;
            int size;
            DList() {
                head = new DNode(0, 0, 0);
                tail = new DNode(0, 0, 0);
                head.next = tail; tail.prev = head;
            }
            void addFront(DNode node) {
                node.next = head.next; node.prev = head;
                head.next.prev = node; head.next = node;
                size++;
            }
            void remove(DNode node) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                size--;
            }
            DNode removeLast() {
                if (size == 0) return null;
                DNode lru = tail.prev;
                remove(lru);
                return lru;
            }
        }

        private final int cap;
        private int minFreq;
        private final Map<Integer, DNode> keyNode;
        private final Map<Integer, DList> freqList;

        public LFUCacheBest(int capacity) {
            cap = capacity; minFreq = 0;
            keyNode  = new HashMap<>();
            freqList = new HashMap<>();
        }

        public int get(int key) {
            if (!keyNode.containsKey(key)) return -1;
            DNode node = keyNode.get(key);
            update(node);
            return node.val;
        }

        public void put(int key, int value) {
            if (cap <= 0) return;
            if (keyNode.containsKey(key)) {
                DNode node = keyNode.get(key);
                node.val = value;
                update(node);
            } else {
                if (keyNode.size() == cap) {
                    DList minList = freqList.get(minFreq);
                    DNode evict = minList.removeLast();
                    if (evict != null) keyNode.remove(evict.key);
                }
                DNode node = new DNode(key, value, 1);
                keyNode.put(key, node);
                freqList.computeIfAbsent(1, k -> new DList()).addFront(node);
                minFreq = 1;
            }
        }

        private void update(DNode node) {
            int f = node.freq;
            freqList.get(f).remove(node);
            if (freqList.get(f).size == 0) {
                freqList.remove(f);
                if (minFreq == f) minFreq = f + 1;
            }
            node.freq++;
            freqList.computeIfAbsent(node.freq, k -> new DList()).addFront(node);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== LFU Cache ===");

        for (String label : new String[]{"Brute", "Optimal", "Best"}) {
            System.out.println("\n-- " + label + " --");
            Object cache = label.equals("Brute") ? new LFUCacheBrute(2)
                         : label.equals("Optimal") ? new LFUCacheOptimal(2)
                         : new LFUCacheBest(2);

            // Unified test via reflection-style dispatch
            if (cache instanceof LFUCacheBrute c) {
                c.put(1, 1); c.put(2, 2);
                System.out.println("get(1)=" + c.get(1));   // 1   freq(1)=2
                c.put(3, 3);                                  // evict key 2 (freq 1)
                System.out.println("get(2)=" + c.get(2));   // -1
                System.out.println("get(3)=" + c.get(3));   // 3
                c.put(4, 4);                                  // evict key 1 or 3 (both freq 1... 3 was recently used so evict 1? no: 1 has freq 2, 3 has freq 2 -> evict 4 victim)
                System.out.println("get(1)=" + c.get(1));
                System.out.println("get(3)=" + c.get(3));
                System.out.println("get(4)=" + c.get(4));
            } else if (cache instanceof LFUCacheOptimal c) {
                c.put(1, 1); c.put(2, 2);
                System.out.println("get(1)=" + c.get(1));
                c.put(3, 3);
                System.out.println("get(2)=" + c.get(2));
                System.out.println("get(3)=" + c.get(3));
                c.put(4, 4);
                System.out.println("get(1)=" + c.get(1));
                System.out.println("get(3)=" + c.get(3));
                System.out.println("get(4)=" + c.get(4));
            } else if (cache instanceof LFUCacheBest c) {
                c.put(1, 1); c.put(2, 2);
                System.out.println("get(1)=" + c.get(1));
                c.put(3, 3);
                System.out.println("get(2)=" + c.get(2));
                System.out.println("get(3)=" + c.get(3));
                c.put(4, 4);
                System.out.println("get(1)=" + c.get(1));
                System.out.println("get(3)=" + c.get(3));
                System.out.println("get(4)=" + c.get(4));
            }
        }
    }
}
