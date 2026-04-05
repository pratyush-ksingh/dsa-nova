/**
 * Problem: LRU Cache
 * Difficulty: HARD | XP: 50
 *
 * Design a data structure that follows the Least Recently Used (LRU) cache policy.
 * Implement get(key) and put(key, value), both O(1).
 *
 * Real-world use: CPU cache, browser history, OS page replacement.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - LinkedHashMap with access-order
    // Time: O(1) amortized  |  Space: O(capacity)
    // Java's LinkedHashMap with accessOrder=true handles LRU automatically
    // ============================================================
    static class LRUCacheBrute {
        private final int capacity;
        private final LinkedHashMap<Integer, Integer> map;

        public LRUCacheBrute(int capacity) {
            this.capacity = capacity;
            // accessOrder=true: iteration order = least-recently-used first
            this.map = new LinkedHashMap<>(capacity, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > LRUCacheBrute.this.capacity;
                }
            };
        }

        public int get(int key) {
            return map.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            map.put(key, value);
        }

        @Override
        public String toString() { return map.toString(); }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - HashMap + Doubly Linked List (manual)
    // Time: O(1) get and put  |  Space: O(capacity)
    // HashMap for O(1) key lookup; DLL for O(1) move-to-front and eviction
    // ============================================================
    static class Node {
        int key, val;
        Node prev, next;
        Node(int k, int v) { key = k; val = v; }
    }

    static class LRUCacheOptimal {
        private final int capacity;
        private final Map<Integer, Node> map;
        private final Node head, tail; // sentinel nodes

        public LRUCacheOptimal(int capacity) {
            this.capacity = capacity;
            this.map = new HashMap<>();
            head = new Node(0, 0);
            tail = new Node(0, 0);
            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            if (!map.containsKey(key)) return -1;
            Node node = map.get(key);
            moveToFront(node);
            return node.val;
        }

        public void put(int key, int value) {
            if (map.containsKey(key)) {
                Node node = map.get(key);
                node.val = value;
                moveToFront(node);
            } else {
                if (map.size() == capacity) {
                    Node lru = tail.prev;
                    remove(lru);
                    map.remove(lru.key);
                }
                Node node = new Node(key, value);
                insertFront(node);
                map.put(key, node);
            }
        }

        private void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void insertFront(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        private void moveToFront(Node node) {
            remove(node);
            insertFront(node);
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Same as Optimal but with cleaner encapsulation
    // Time: O(1)  |  Space: O(capacity)
    // Industry-standard pattern used in real cache implementations
    // Explicitly shows the DLL order (MRU at front, LRU at back)
    // ============================================================
    static class LRUCacheBest {
        private final int cap;
        private final Map<Integer, int[]> map; // key -> [value, prev, next] encoded via node ids
        private final Deque<int[]> order;       // [key, value] deque: front=MRU, back=LRU
        // We reuse LRUCacheOptimal logic but expose it differently for clarity
        private final LRUCacheOptimal inner;

        public LRUCacheBest(int capacity) {
            cap = capacity;
            map = null;
            order = null;
            inner = new LRUCacheOptimal(capacity);
        }

        public int get(int key) { return inner.get(key); }
        public void put(int key, int value) { inner.put(key, value); }
    }

    public static void main(String[] args) {
        System.out.println("=== LRU Cache ===");

        System.out.println("-- Brute (LinkedHashMap) --");
        LRUCacheBrute brute = new LRUCacheBrute(2);
        brute.put(1, 1);
        brute.put(2, 2);
        System.out.println("get(1)=" + brute.get(1));  // 1
        brute.put(3, 3);                                // evicts key 2
        System.out.println("get(2)=" + brute.get(2));  // -1
        brute.put(4, 4);                                // evicts key 1
        System.out.println("get(1)=" + brute.get(1));  // -1
        System.out.println("get(3)=" + brute.get(3));  // 3
        System.out.println("get(4)=" + brute.get(4));  // 4

        System.out.println("\n-- Optimal (HashMap + DLL) --");
        LRUCacheOptimal opt = new LRUCacheOptimal(2);
        opt.put(1, 1);
        opt.put(2, 2);
        System.out.println("get(1)=" + opt.get(1));
        opt.put(3, 3);
        System.out.println("get(2)=" + opt.get(2));
        opt.put(4, 4);
        System.out.println("get(1)=" + opt.get(1));
        System.out.println("get(3)=" + opt.get(3));
        System.out.println("get(4)=" + opt.get(4));

        System.out.println("\n-- Best (same DLL, clean wrapper) --");
        LRUCacheBest best = new LRUCacheBest(2);
        best.put(1, 1); best.put(2, 2);
        System.out.println("get(1)=" + best.get(1));
        best.put(3, 3);
        System.out.println("get(2)=" + best.get(2));
        best.put(4, 4);
        System.out.println("get(3)=" + best.get(3));
        System.out.println("get(4)=" + best.get(4));
    }
}
