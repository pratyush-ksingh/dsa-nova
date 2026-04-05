/**
 * Problem: Implement Stack using Linked List
 * Difficulty: EASY | XP: 10
 *
 * Implement stack using singly linked list.
 * Head of the list = top of the stack.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // NODE DEFINITION
    // ============================================================
    static class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
            this.next = null;
        }
    }

    // ============================================================
    // STACK USING SINGLY LINKED LIST
    // All operations: O(1) time
    // Space: O(n) overall for n elements
    // ============================================================
    static class LinkedListStack {
        private Node top;   // head of the list = top of stack
        private int size;

        LinkedListStack() {
            this.top = null;
            this.size = 0;
        }

        /**
         * Push element onto the stack.
         * Insert at head of linked list.
         * Time: O(1)
         */
        void push(int val) {
            Node newNode = new Node(val);
            newNode.next = top;
            top = newNode;
            size++;
        }

        /**
         * Remove and return the top element.
         * Delete head of linked list.
         * Time: O(1)
         */
        int pop() {
            if (top == null) {
                System.out.println("Stack Underflow! Cannot pop.");
                return -1;
            }
            int val = top.val;
            top = top.next;  // old top is now unreferenced (GC will collect it)
            size--;
            return val;
        }

        /**
         * Return top element without removing.
         * Time: O(1)
         */
        int peek() {
            if (top == null) {
                System.out.println("Stack is empty! Cannot peek.");
                return -1;
            }
            return top.val;
        }

        /**
         * Check if stack is empty.
         * Time: O(1)
         */
        boolean isEmpty() {
            return top == null;
        }

        /**
         * Return the number of elements.
         * Time: O(1)
         */
        int size() {
            return size;
        }

        /**
         * Print stack from top to bottom.
         */
        void printStack() {
            if (isEmpty()) {
                System.out.println("Stack: (empty)");
                return;
            }
            StringBuilder sb = new StringBuilder("Stack (top->bottom): [");
            Node current = top;
            while (current != null) {
                sb.append(current.val);
                if (current.next != null) sb.append(", ");
                current = current.next;
            }
            sb.append("]");
            System.out.println(sb.toString());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Implement Stack using Linked List ===\n");

        LinkedListStack stack = new LinkedListStack();

        // Push operations
        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.push(40);
        stack.printStack();  // [40, 30, 20, 10]

        // Peek
        System.out.println("Peek: " + stack.peek());       // 40
        System.out.println("Size: " + stack.size());        // 4

        // Pop operations
        System.out.println("Pop:  " + stack.pop());         // 40
        System.out.println("Pop:  " + stack.pop());         // 30
        stack.printStack();  // [20, 10]

        // Continue popping
        System.out.println("Pop:  " + stack.pop());         // 20
        System.out.println("Pop:  " + stack.pop());         // 10
        System.out.println("isEmpty: " + stack.isEmpty());  // true

        // Underflow test
        stack.pop();   // Underflow!
        stack.peek();  // Empty!

        // No overflow -- linked list grows dynamically
        System.out.println("\n--- No overflow (dynamic sizing) ---");
        LinkedListStack bigStack = new LinkedListStack();
        for (int i = 1; i <= 10; i++) {
            bigStack.push(i * 100);
        }
        System.out.println("Pushed 10 elements. Size: " + bigStack.size());
        bigStack.printStack();

        // Pop all
        System.out.println("\n--- Pop all ---");
        while (!bigStack.isEmpty()) {
            System.out.println("Pop: " + bigStack.pop());
        }
        System.out.println("isEmpty: " + bigStack.isEmpty());
    }
}
