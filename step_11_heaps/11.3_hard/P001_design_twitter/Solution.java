/**
 * Problem: Design Twitter
 * Difficulty: HARD | XP: 50
 *
 * Design a simplified version of Twitter with:
 * - postTweet(userId, tweetId)
 * - getNewsFeed(userId) -> 10 most recent tweets from user + followees
 * - follow(followerId, followeeId)
 * - unfollow(followerId, followeeId)
 * Real-life use: Social media feeds, news aggregation, event streaming.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Store all tweets in a global list. For getNewsFeed, scan entire
    // list, filter by user+followees, return 10 most recent.
    // Time: postTweet O(1), getNewsFeed O(N log N), follow/unfollow O(1)
    // Space: O(total tweets + users)
    // ============================================================
    static class TwitterBrute {
        private int timestamp = 0;
        private List<int[]> allTweets = new ArrayList<>(); // [time, userId, tweetId]
        private Map<Integer, Set<Integer>> following = new HashMap<>();

        public void postTweet(int userId, int tweetId) {
            allTweets.add(new int[]{timestamp++, userId, tweetId});
        }

        public List<Integer> getNewsFeed(int userId) {
            Set<Integer> feed = following.getOrDefault(userId, new HashSet<>());
            feed.add(userId);
            List<int[]> relevant = new ArrayList<>();
            for (int[] t : allTweets) {
                if (feed.contains(t[1])) relevant.add(t);
            }
            relevant.sort((a, b) -> b[0] - a[0]);
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < Math.min(10, relevant.size()); i++) {
                result.add(relevant.get(i)[2]);
            }
            return result;
        }

        public void follow(int followerId, int followeeId) {
            following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            if (following.containsKey(followerId)) following.get(followerId).remove(followeeId);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Per-user tweet lists + min-heap merge for getNewsFeed.
    // Keep each user's tweets as a list (newest at end).
    // Merge K lists using a max-heap of size K (one pointer per user).
    // Time: postTweet O(1), getNewsFeed O(K log K) where K = followees
    // Space: O(total tweets + users)
    // ============================================================
    static class TwitterOptimal {
        private int timestamp = 0;
        // userId -> list of [time, tweetId]
        private Map<Integer, List<int[]>> userTweets = new HashMap<>();
        private Map<Integer, Set<Integer>> following = new HashMap<>();

        public void postTweet(int userId, int tweetId) {
            userTweets.computeIfAbsent(userId, k -> new ArrayList<>())
                      .add(new int[]{timestamp++, tweetId});
        }

        public List<Integer> getNewsFeed(int userId) {
            // Max-heap: [time, tweetId, userIdx (index in their list)]
            // heap entry: int[]{time, tweetId, userId, index}
            PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[0] - a[0]);
            Set<Integer> users = new HashSet<>(following.getOrDefault(userId, Collections.emptySet()));
            users.add(userId);
            for (int uid : users) {
                List<int[]> tweets = userTweets.get(uid);
                if (tweets != null && !tweets.isEmpty()) {
                    int idx = tweets.size() - 1;
                    heap.offer(new int[]{tweets.get(idx)[0], tweets.get(idx)[1], uid, idx});
                }
            }
            List<Integer> result = new ArrayList<>();
            while (!heap.isEmpty() && result.size() < 10) {
                int[] top = heap.poll();
                result.add(top[1]);
                int uid = top[2], idx = top[3] - 1;
                if (idx >= 0) {
                    List<int[]> tweets = userTweets.get(uid);
                    heap.offer(new int[]{tweets.get(idx)[0], tweets.get(idx)[1], uid, idx});
                }
            }
            return result;
        }

        public void follow(int followerId, int followeeId) {
            following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            if (following.containsKey(followerId)) following.get(followerId).remove(followeeId);
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Same heap-merge approach but uses a single global timestamp counter
    // and stores tweets as LinkedList for O(1) add + cleaner code.
    // This is the standard LeetCode-style clean implementation.
    // Time: postTweet O(1), getNewsFeed O(K log K), follow/unfollow O(1)
    // Space: O(total tweets + users)
    // ============================================================
    static class Twitter {
        private static int time = 0;

        private static class Tweet {
            int id, ts;
            Tweet next;
            Tweet(int id, int ts) { this.id = id; this.ts = ts; }
        }

        private Map<Integer, Tweet> tweetHead = new HashMap<>(); // userId -> latest tweet
        private Map<Integer, Set<Integer>> following = new HashMap<>();

        public void postTweet(int userId, int tweetId) {
            Tweet t = new Tweet(tweetId, time++);
            t.next = tweetHead.get(userId);
            tweetHead.put(userId, t);
        }

        public List<Integer> getNewsFeed(int userId) {
            // heap: [timestamp, tweet, userId] — max-heap by timestamp
            PriorityQueue<Tweet> heap = new PriorityQueue<>((a, b) -> b.ts - a.ts);
            Set<Integer> users = new HashSet<>(following.getOrDefault(userId, Collections.emptySet()));
            users.add(userId);
            for (int uid : users) {
                Tweet head = tweetHead.get(uid);
                if (head != null) heap.offer(head);
            }
            List<Integer> result = new ArrayList<>();
            while (!heap.isEmpty() && result.size() < 10) {
                Tweet t = heap.poll();
                result.add(t.id);
                if (t.next != null) heap.offer(t.next);
            }
            return result;
        }

        public void follow(int followerId, int followeeId) {
            following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            if (following.containsKey(followerId)) following.get(followerId).remove(followeeId);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Design Twitter ===");

        System.out.println("\n--- Approach 2 (Optimal) ---");
        TwitterOptimal tw = new TwitterOptimal();
        tw.postTweet(1, 5);
        System.out.println("getNewsFeed(1): " + tw.getNewsFeed(1));  // [5]
        tw.follow(1, 2);
        tw.postTweet(2, 6);
        System.out.println("getNewsFeed(1): " + tw.getNewsFeed(1));  // [6, 5]
        tw.unfollow(1, 2);
        System.out.println("getNewsFeed(1): " + tw.getNewsFeed(1));  // [5]

        System.out.println("\n--- Approach 3 (Best/LinkedList) ---");
        Twitter t3 = new Twitter();
        t3.postTweet(1, 5);
        t3.postTweet(1, 3);
        t3.postTweet(1, 101);
        t3.postTweet(1, 13);
        t3.postTweet(1, 10);
        t3.postTweet(2, 1);
        t3.postTweet(1, 7);
        t3.postTweet(1, 2);
        t3.postTweet(1, 9);
        t3.postTweet(1, 4);
        t3.postTweet(1, 8);
        System.out.println("getNewsFeed(1): " + t3.getNewsFeed(1));  // 10 most recent
        t3.follow(1, 2);
        System.out.println("getNewsFeed(1) after follow(1,2): " + t3.getNewsFeed(1));
    }
}
