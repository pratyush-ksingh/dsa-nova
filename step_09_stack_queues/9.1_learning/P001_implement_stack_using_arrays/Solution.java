/**
 * Problem: Implement Stack using Arrays
 * Difficulty: EASY | XP: 10
 *
 * Implement a stack with push, pop, peek, isEmpty, size using an array.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // STACK IMPLEMENTATION (Fixed-Size Array)
    // All operations: O(1) time, O(1) space per operation
    // Overall space: O(capacity)
    // ============================================================
    static class Stack {
        private int[] arr;
        private int top;
        private int capacity;

        Stack(int capacity) {
            this.capacity = capacity;
            this.arr = new int[capacity];
            this.top = -1;  // -1 means empty
        }

        /**
         * Push element onto the stack.
         * Time: O(1)
         */
        void push(int val) {
            if (top == capacity - 1) {
                System.out.println("Stack Overflow! Cannot push " + val);
                return;
            }
            top++;
            arr[top] = val;
        }

        /**
         * Remove and return the top element.
         * Time: O(1)
         */
        int pop() {
            if (top == -1) {
                System.out.println("Stack Underflow! Cannot pop.");
                return -1;
            }
            int val = arr[top];
            top--;
            return val;
        }

        /**
         * Return the top element without removing it.
         * Time: O(1)
         */
        int peek() {
            if (top == -1) {
                System.out.println("Stack is empty! Cannot peek.");
                return -1;
            }
            return arr[top];
        }

        /**
         * Check if stack is empty.
         * Time: O(1)
         */
        boolean isEmpty() {
            return top == -1;
        }

        /**
         * Return number of elements in the stack.
         * Time: O(1)
         */
        int size() {
            return top + 1;
        }

        /**
         * Print stack contents (bottom to top).
         */
        void printStack() {
            if (isEmpty()) {
                System.out.println("Stack: (empty)");
                return;
            }
            StringBuilder sb = new StringBuilder("Stack (bottom->top): [");
            for (int i = 0; i <= top; i++) {
                sb.append(arr[i]);
                if (i < top) sb.append(", ");
            }
            sb.append("]");
            System.out.println(sb.toString());
        }
    }

    // ============================================================
    // DYNAMIC STACK (with resizing) - Amortized O(1)
    // ============================================================
    static class DynamicStack {
        private int[] arr;
        private int top;
        private int capacity;

        DynamicStack() {
            this.capacity = 4;  // start small
            this.arr = new int[capacity];
            this.top = -1;
        }

        void push(int val) {
            if (top == capacity - 1) {
                resize(capacity * 2);
            }
            arr[++top] = val;
        }

        int pop() {
            if (top == -1) return -1;
            return arr[top--];
        }

        int peek() {
            if (top == -1) return -1;
            return arr[top];
        }

        boolean isEmpty() {
            return top == -1;
        }

        int size() {
            return top + 1;
        }

        private void resize(int newCapacity) {
            int[] newArr = new int[newCapacity];
            for (int i = 0; i <= top; i++) {
                newArr[i] = arr[i];
            }
            arr = newArr;
            capacity = newCapacity;
            System.out.println("  (Resized to capacity " + newCapacity + ")");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Implement Stack using Arrays ===\n");

        // Fixed-size stack demo
        System.out.println("--- Fixed-Size Stack (capacity=5) ---");
        Stack stack = new Stack(5);

        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.printStack();

        System.out.println("Peek: " + stack.peek());     // 30
        System.out.println("Size: " + stack.size());      // 3
        System.out.println("Pop:  " + stack.pop());       // 30
        System.out.println("Pop:  " + stack.pop());       // 20
        stack.printStack();

        System.out.println("isEmpty: " + stack.isEmpty()); // false
        System.out.println("Pop:  " + stack.pop());        // 10
        System.out.println("isEmpty: " + stack.isEmpty()); // true

        // Overflow test
        System.out.println("\n--- Overflow Test ---");
        Stack small = new Stack(2);
        small.push(1);
        small.push(2);
        small.push(3);  // Overflow!

        // Underflow test
        System.out.println("\n--- Underflow Test ---");
        Stack empty = new Stack(3);
        empty.pop();    // Underflow!
        empty.peek();   // Empty!

        // Dynamic stack demo
        System.out.println("\n--- Dynamic Stack (auto-resize) ---");
        DynamicStack dStack = new DynamicStack();
        for (int i = 1; i <= 10; i++) {
            dStack.push(i * 10);
            System.out.println("Pushed " + (i * 10) + ", size=" + dStack.size());
        }
        System.out.println("Peek: " + dStack.peek());     // 100
        System.out.println("Pop:  " + dStack.pop());       // 100
        System.out.println("Size: " + dStack.size());      // 9
    }
}
