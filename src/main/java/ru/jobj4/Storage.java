package ru.jobj4;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ru.job4j.storage
 *
 * @author romanvohmin
 * @since 21.07.2020
 */
public class Storage {
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> storage = new ConcurrentHashMap<>();
    private final Queue<String> queue = new ConcurrentLinkedQueue<>();
    private final Queue<Integer> keys = new ConcurrentLinkedQueue<>();
    private final List<String> result = new CopyOnWriteArrayList<>();

    public ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> getStorage() {
        return storage;
    }

    public Queue<String> getQueue() {
        return queue;
    }

    public Queue<Integer> getKeys() {
        return keys;
    }

    public List<String> getResult() {
        return result;
    }
}
