/**
 * Problem: Implement Queue using Linked List
 * Difficulty: EASY | XP: 10
 *
 * Implement queue using singly linked list with enqueue, dequeue, front, isEmpty.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // NODE DEFINITION
    // ============================================================
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    // ============================================================
    // QUEUE IMPLEMENTATION USING LINKED LIST
    // All operations: O(1) time, O(1) space per operation
    // ============================================================
    static class QueueLL {
        private ListNode front; // dequeue end (head)
        private ListNode rear;  // enqueue end (tail)
        private int size;

        public QueueLL() {
            this.front = null;
            this.rear = null;
            this.size = 0;
        }

        /**
         * Add element to the back of the queue. O(1).
         */
        public void enqueue(int val) {
            ListNode newNode = new ListNode(val);

            if (isEmpty()) {
                // First element: both front and rear point to it
                front = rear = newNode;
            } else {
                // Attach after rear, then move rear forward
                rear.next = newNode;
                rear = newNode;
            }
            size++;
        }

        /**
         * Remove and return the front element. O(1).
         * Returns -1 if queue is empty.
         */
        public int dequeue() {
            if (isEmpty()) {
                System.out.println("Queue Underflow! Cannot dequeue.");
                return -1;
            }

            int val = front.val;
            front = front.next;

            // CRITICAL: if queue is now empty, reset rear too
            if (front == null) {
                rear = null;
            }

            size--;
            return val;
        }

        /**
         * Return the front element without removing. O(1).
         */
        public int peek() {
            if (isEmpty()) {
                System.out.println("Queue is empty! Cannot peek.");
                return -1;
            }
            return front.val;
        }

        /**
         * Check if queue is empty. O(1).
         */
        public boolean isEmpty() {
            return front == null;
        }

        /**
         * Return the number of elements. O(1).
         */
        public int size() {
            return size;
        }

        /**
         * Print queue contents from front to rear.
         */
        public void display() {
            if (isEmpty()) {
                System.out.println("Queue: (empty)");
                return;
            }
            StringBuilder sb = new StringBuilder("Queue front -> [");
            ListNode current = front;
            while (current != null) {
                sb.append(current.val);
                if (current.next != null) sb.append(", ");
                current = current.next;
            }
            sb.append("] <- rear");
            System.out.println(sb.toString());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Implement Queue using Linked List ===\n");

        QueueLL queue = new QueueLL();

        // Enqueue operations
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.display(); // front -> [10, 20, 30] <- rear

        // Peek
        System.out.println("Peek: " + queue.peek()); // 10
        System.out.println("Size: " + queue.size());   // 3

        // Dequeue operations
        System.out.println("\nDequeue: " + queue.dequeue()); // 10
        queue.display(); // front -> [20, 30] <- rear

        System.out.println("Dequeue: " + queue.dequeue()); // 20
        queue.display(); // front -> [30] <- rear

        System.out.println("Dequeue: " + queue.dequeue()); // 30
        queue.display(); // (empty)

        // Edge: isEmpty after all dequeues
        System.out.println("\nisEmpty: " + queue.isEmpty()); // true

        // Edge: dequeue from empty queue
        System.out.println("Dequeue empty: " + queue.dequeue()); // -1

        // Edge: enqueue after emptying
        System.out.println("\nEnqueue after emptying:");
        queue.enqueue(99);
        queue.display(); // front -> [99] <- rear
        System.out.println("Peek: " + queue.peek()); // 99

        // Verify front and rear are same for single element
        System.out.println("Size: " + queue.size()); // 1
    }
}
