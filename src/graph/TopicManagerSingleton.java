package graph;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

/**
 * The TopicManagerSingleton class provides a singleton instance of TopicManager to manage topics in a thread-safe manner.
 */
public class TopicManagerSingleton {

    /**
     * The TopicManager class manages topics using a ConcurrentHashMap.
     */
    public static class TopicManager {
        // The single instance of TopicManager
        private static final TopicManager instance = new TopicManager();

        // ConcurrentHashMap to hold topic name to Topic instance mapping
        private final ConcurrentHashMap<String, Topic> topics = new ConcurrentHashMap<>();

        // Private constructor to prevent instantiation
        private TopicManager() {}

        /**
         * Gets or creates a topic with the specified name.
         *
         * @param name the name of the topic
         * @return the Topic instance
         */
        public Topic getTopic(String name) {
            // computeIfAbsent ensures atomicity and thread safety
            return topics.computeIfAbsent(name, k -> new Topic(k));
        }

        /**
         * Gets all topics.
         *
         * @return a collection of all Topic instances
         */
        public Collection<Topic> getTopics() {
            return topics.values();
        }

        /**
         * Clears all topics.
         */
        public void clear() {
            topics.clear();
        }
    }

    /**
     * Gets the single instance of TopicManager.
     *
     * @return the TopicManager instance
     */
    public static TopicManager get() {
        return TopicManager.instance;
    }
}
