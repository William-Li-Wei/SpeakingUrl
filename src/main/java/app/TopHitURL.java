package app;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by william on 03.04.16.
 */
public enum TopHitURL {
    INSTANCE;

    private static Hashtable<String, String> hashtable;
    private static Queue<String> queue;
    private static int maxSize;

    TopHitURL() {
        initTopHitURL();
    }

    private static void initTopHitURL() {
        hashtable = new Hashtable<>();
        queue = new ConcurrentLinkedQueue<>();
        maxSize = 500;
    }

    // static 'instance' method
    public static TopHitURL getInstance() {
        return INSTANCE;
    }

    /**
     * looking for a hit in the hashtable
     * @param key
     * @return the value linked by the key
     */
    public static String getEntry(String key) {
        return hashtable.get(key);
    }

    /**
     * add a (key, value) pair to the hashtable, and add the key into the queue for maintenance
     * @param key
     * @param value
     */
    public static void addEntry(String key, String value) {
        // if the top hit table reaches the max size, remove the oldest (key, value) pair
        if(queue.size() == maxSize) {
            // remove from queue
            String keyToRemove = queue.poll();
            // remove from the hashtable
            hashtable.remove(keyToRemove);
        }
        // add the (key, value) pair to hash table
        hashtable.put(key, value);
        // add the key to the queue
        queue.offer(key);
    }
}
