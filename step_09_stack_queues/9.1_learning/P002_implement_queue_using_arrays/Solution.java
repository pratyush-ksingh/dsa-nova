/**
 * Problem: Implement Queue using Arrays
 * Difficulty: EASY | XP: 10
 *
 * Implement queue with enqueue, dequeue, front, isEmpty, size using circular array.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Linear Array with Shift
    // enqueue: O(1)  |  dequeue: O(n) due to shifting
    // ============================================================
    static class NaiveQueue {
        private int[] arr;
        private int size;
        private int capacity;

        NaiveQueue(int capacity) {
            this.capacity = capacity;
            this.arr = new int[capacity];
            this.size = 0;
        }

        void enqueue(int val) {
            if (size == capacity) {
                System.out.println("Queue Overflow!");
                return;
            }
            arr[size++] = val;
        }

        int dequeue() {
            if (size == 0) {
                System.out.println("Queue Underflow!");
                return -1;
            }
            int val = arr[0];
            // Shift all elements left -- O(n)
            for (int i = 0; i < size - 1; i++) {
                arr[i] = arr[i + 1];
            }
            size--;
            return val;
        }

        int front() {
            if (size == 0) return -1;
            return arr[0];
        }

        boolean isEmpty() { return size == 0; }
        int size() { return size; }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Circular Array Queue
    // All operations: O(1) time
    // Uses modulo for wrap-around indexing.
    // ============================================================
    static class CircularQueue {
        private int[] arr;
        private int front;
        private int rear;
        private int size;
        private int capacity;

        CircularQueue(int capacity) {
            this.capacity = capacity;
            this.arr = new int[capacity];
            this.front = 0;
            this.rear = 0;
            this.size = 0;
        }

        /**
         * Add element to the rear of the queue.
         * Time: O(1)
         */
        void enqueue(int val) {
            if (size == capacity) {
                System.out.println("Queue Overflow! Cannot enqueue " + val);
                return;
            }
            arr[rear] = val;
            rear = (rear + 1) % capacity;  // circular wrap
            size++;
        }

        /**
         * Remove and return the front element.
         * Time: O(1)
         */
        int dequeue() {
            if (size == 0) {
                System.out.println("Queue Underflow!");
                return -1;
            }
            int val = arr[front];
            front = (front + 1) % capacity;  // circular wrap
            size--;
            return val;
        }

        /**
         * Return front element without removing.
         * Time: O(1)
         */
        int front() {
            if (size == 0) {
                System.out.println("Queue is empty!");
                return -1;
            }
            return arr[front];
        }

        /**
         * Check if queue is empty.
         * Time: O(1)
         */
        boolean isEmpty() {
            return size == 0;
        }

        /**
         * Return number of elements.
         * Time: O(1)
         */
        int size() {
            return size;
        }

        /**
         * Print queue contents from front to rear.
         */
        void printQueue() {
            if (isEmpty()) {
                System.out.println("Queue: (empty)");
                return;
            }
            StringBuilder sb = new StringBuilder("Queue (front->rear): [");
            for (int i = 0; i < size; i++) {
                int index = (front + i) % capacity;
                sb.append(arr[index]);
                if (i < size - 1) sb.append(", ");
            }
            sb.append("]");
            System.out.println(sb.toString());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Implement Queue using Arrays ===\n");

        // --- Naive Queue Demo ---
        System.out.println("--- Naive Queue (dequeue is O(n)) ---");
        NaiveQueue nq = new NaiveQueue(5);
        nq.enqueue(10);
        nq.enqueue(20);
        nq.enqueue(30);
        System.out.println("Front: " + nq.front());       // 10
        System.out.println("Dequeue: " + nq.dequeue());   // 10
        System.out.println("Front: " + nq.front());       // 20
        System.out.println("Size: " + nq.size());         // 2

        // --- Circular Queue Demo ---
        System.out.println("\n--- Circular Queue (all O(1)) ---");
        CircularQueue cq = new CircularQueue(4);

        cq.enqueue(10);
        cq.enqueue(20);
        cq.enqueue(30);
        cq.printQueue();  // [10, 20, 30]

        System.out.println("Front: " + cq.front());        // 10
        System.out.println("Dequeue: " + cq.dequeue());    // 10
        System.out.println("Dequeue: " + cq.dequeue());    // 20
        cq.printQueue();  // [30]

        // Demonstrate wrap-around
        cq.enqueue(40);
        cq.enqueue(50);
        cq.enqueue(60);  // wraps around to index 0
        cq.printQueue();  // [30, 40, 50, 60]
        System.out.println("Size: " + cq.size());          // 4

        // Overflow test
        cq.enqueue(70);  // Overflow!

        // Empty it all
        System.out.println("\n--- Empty the queue ---");
        while (!cq.isEmpty()) {
            System.out.println("Dequeue: " + cq.dequeue());
        }
        System.out.println("isEmpty: " + cq.isEmpty());    // true

        // Underflow test
        cq.dequeue();  // Underflow!
    }
}
