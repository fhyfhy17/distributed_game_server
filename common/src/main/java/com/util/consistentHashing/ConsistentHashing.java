package com.util.consistentHashing;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Slf4j
public class ConsistentHashing<T> {


    private ReentrantReadWriteLock rennlock = new ReentrantReadWriteLock();

    private TreeMap<Long, Node<T>> nodeMap = new TreeMap<>();

    private List<Node<T>> nodeList = new ArrayList<>();

    public ConsistentHashing() {
    }



    public void addNode(Node<T> node) {
        rennlock.writeLock()
                .lock();
        try {
            for (int i = 0; i < node.getWeight(); i++) {
                nodeMap.put(hashcode(node.getVirtualNodeName(i)), node);
            }
            nodeList.add(node);
        } finally {
            rennlock.writeLock()
                    .unlock();
        }

    }

    public void removeNode(Node<T> node) {
        if (node == null) {
            return;
        }
        rennlock.writeLock()
                .lock();

        try {

            for (int i = 0; i < node.getWeight(); i++) {
                String virtualNodeName = node.getVirtualNodeName(i);
                nodeMap.remove(hashcode(virtualNodeName));
            }
            nodeList.remove(node);

        } finally {
            rennlock.writeLock()
                    .unlock();
        }
    }

    public T getResourceByKey(String key) {
        return getNodeByKey(key).getResource();
    }

    public Node<T> getNodeByKey(String key) {

        rennlock.readLock()
                .lock();
        try {

            if (nodeMap.isEmpty()) {
                return null;
            }
            long hash = hashcode(key);
            if (!nodeMap.containsKey(hash)) {
                SortedMap<Long, Node<T>> tailMap = nodeMap.tailMap(hash);
                hash = tailMap.isEmpty() ? nodeMap.firstKey() : tailMap.firstKey();
            }
            return nodeMap.get(hash);
        } finally {
            rennlock.readLock()
                    .unlock();
        }
    }

    private long hashcode(String key) {

        HashFunction hashFunction = Hashing.murmur3_128(123487545);
        HashCode hashCode = hashFunction.hashString(key, Charset.forName("utf-8"));
        return hashCode.asLong();
    }

    public static void main(String[] args) {
        IpNode ipNode1 = new IpNode("ipTest1", "192.168.0.1", 1);
        IpNode ipNode2 = new IpNode("ipTest2", "192.168.0.2", 2);
        IpNode ipNode3 = new IpNode("ipTest3", "192.168.0.3", 3);


        ConsistentHashing<String> consistentHashing = new ConsistentHashing<>();
        consistentHashing.addNode(ipNode1);
        consistentHashing.addNode(ipNode2);
        consistentHashing.addNode(ipNode3);

        Node<String> abc = consistentHashing.getNodeByKey("abcde");
        System.out.println(abc.getResource());
    }
}
