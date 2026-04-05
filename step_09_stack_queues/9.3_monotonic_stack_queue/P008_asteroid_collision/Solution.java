/**
 * Problem: Asteroid Collision (LeetCode 735)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array of integers representing asteroids in a row:
 * - Absolute value = size, Sign = direction (+ right, - left).
 * - All move at the same speed.
 * - Collision: positive (right) meets negative (left).
 *   Smaller explodes; equal size -> both explode; same direction -> no collision.
 *
 * Return the state of the asteroids after all collisions.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Simulate repeatedly until stable
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        public int[] asteroidCollision(int[] asteroids) {
            List<Integer> list = new ArrayList<>();
            for (int a : asteroids) list.add(a);

            boolean changed = true;
            while (changed) {
                changed = false;
                for (int i = 0; i < list.size() - 1; i++) {
                    int left = list.get(i), right = list.get(i + 1);
                    if (left > 0 && right < 0) {
                        changed = true;
                        if (Math.abs(left) > Math.abs(right)) {
                            list.remove(i + 1);
                        } else if (Math.abs(left) < Math.abs(right)) {
                            list.remove(i);
                        } else {
                            list.remove(i + 1);
                            list.remove(i);
                        }
                        i--; // recheck current position
                        break; // restart scan
                    }
                }
            }

            return list.stream().mapToInt(Integer::intValue).toArray();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Single-pass stack simulation
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class Optimal {
        /**
         * Stack stores surviving asteroids.
         * For each asteroid:
         * - Moving right (>0) or stack empty or stack top also left (<0): push.
         * - Moving left (<0): check against stack top which moves right.
         *   - Top smaller: pop (top destroyed), continue.
         *   - Top equal: pop (both destroyed), mark current as dead.
         *   - Top larger: mark current as dead.
         */
        public int[] asteroidCollision(int[] asteroids) {
            Deque<Integer> stack = new ArrayDeque<>();

            for (int asteroid : asteroids) {
                boolean alive = true;
                while (alive && asteroid < 0 && !stack.isEmpty() && stack.peek() > 0) {
                    int top = stack.peek();
                    if (top < -asteroid) {
                        stack.pop();           // top destroyed
                    } else if (top == -asteroid) {
                        stack.pop();           // both destroyed
                        alive = false;
                    } else {
                        alive = false;         // current destroyed
                    }
                }
                if (alive) stack.push(asteroid);
            }

            // Stack is LIFO; collect in correct order
            int[] result = new int[stack.size()];
            for (int i = result.length - 1; i >= 0; i--)
                result[i] = stack.pop();
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Stack with explicit collision condition
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class Best {
        /**
         * Same stack approach as Optimal, reorganized for interview clarity.
         * Uses a while-loop-with-break pattern that mirrors the logical cases
         * more explicitly:
         *   1. top explodes -> pop, continue (inner while)
         *   2. both explode -> pop, break (current also dead)
         *   3. current explodes -> break (current dead)
         * Uses an ArrayList as a stack for easy index-based result construction.
         */
        public int[] asteroidCollision(int[] asteroids) {
            List<Integer> stack = new ArrayList<>();

            for (int asteroid : asteroids) {
                boolean currentAlive = true;
                while (currentAlive && !stack.isEmpty()
                        && stack.get(stack.size() - 1) > 0 && asteroid < 0) {
                    int top = stack.get(stack.size() - 1);
                    if (top < -asteroid) {
                        stack.remove(stack.size() - 1); // top explodes, retry
                    } else if (top == -asteroid) {
                        stack.remove(stack.size() - 1); // both explode
                        currentAlive = false;
                    } else {
                        currentAlive = false;            // current explodes
                    }
                }
                if (currentAlive) stack.add(asteroid);
            }

            return stack.stream().mapToInt(Integer::intValue).toArray();
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Asteroid Collision ===");
        int[][] tests = {
            {5, 10, -5},
            {8, -8},
            {10, 2, -5},
            {-2, -1, 1, 2},
            {1, -1, -2},
        };

        for (int[] t : tests) {
            System.out.printf("Input: %s%n", Arrays.toString(t));
            System.out.printf("  Brute:   %s%n", Arrays.toString(new BruteForce().asteroidCollision(t.clone())));
            System.out.printf("  Optimal: %s%n", Arrays.toString(new Optimal().asteroidCollision(t.clone())));
            System.out.printf("  Best:    %s%n", Arrays.toString(new Best().asteroidCollision(t.clone())));
        }
    }
}
