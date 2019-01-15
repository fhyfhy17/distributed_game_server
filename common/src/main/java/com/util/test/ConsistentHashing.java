package com.util.test;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Slf4j
public class ConsistentHashing<T> {


    /**
     * 任务锁
     */
    private ReentrantReadWriteLock rennlock = new ReentrantReadWriteLock();

    /**
     * 虚拟节点到真实节点的映射
     */
    private TreeMap<Integer, Node<T>> nodeMap = new TreeMap<>();

    /**
     * 真实Node
     */
    private List<Node<T>> nodeList = new ArrayList<>();

    /**
     * 构造函数
     */
    public ConsistentHashing() {
    }

    /**
     * 返回节点个数
     *
     * @return
     */
    public int getNodeSize() {
        return nodeList.size();
    }

    /**
     * 返回虚拟节点个数
     *
     * @return
     */
    public int getVirtualNodeSize() {
        return nodeMap.size();
    }

    /**
     * 动态增加Node
     *
     * @param node
     */
    public void addHostNode(Node<T> node) {
        rennlock.writeLock()
                .lock();
        try {
            log.info("增加主机" + node + "的变化:");
            for (int i = 0; i < node.getWeight(); i++)
                nodeMap.put(hashcode(node.getVirtualNodeName(i)), node);
            nodeList.add(node);
        } finally {
            rennlock.writeLock()
                    .unlock();
        }//end try

    }


    /**
     * 删除真实节点是
     *
     * @param node
     */
    public void removeNode(Node<T> node) {
        if (node == null) {
            return;
        }
        rennlock.writeLock()
                .lock();

        try {

            log.info("删除主机" + node + "的变化:");
            for (int i = 0; i < node.getWeight(); i++) {
                //定位s节点的第i的虚拟节点的位置
                String virtualNodeName = node.getVirtualNodeName(i);
                nodeMap.remove(hashcode(virtualNodeName));
            }//end for i
            nodeList.remove(node);

        } finally {
            rennlock.writeLock()
                    .unlock();
        }//end try
    }

    /**
     * 映射key到node
     *
     * @param key
     * @return
     */
    public Node<T> getNodeByKey(String key) {

        rennlock.readLock()
                .lock();
        try {

            /*
             * 沿环的顺时针找到一个虚拟节点
             */
            SortedMap<Integer, Node<T>> tail = nodeMap.tailMap(hashcode(key));
            /*
             * 如果没有比当前key大的节点，因为是环形结构，就使用第一个节点
             */
            if (tail.size() == 0) {
                Entry<Integer, Node<T>> firstEntry = nodeMap.firstEntry();
                if (firstEntry != null) {
                    return firstEntry.getValue();
                } else {
                    return null;
                }
            } else {
                return tail.get(tail.firstKey());
            }

        } finally {
            rennlock.readLock()
                    .unlock();
        }//end try
    }

    /**
     * 根据指定key获取node提供的资源
     *
     * @param key
     * @return
     */
    public T getResourceByKey(String key) {
        return getNodeByKey(key).getResource();
    }

    private int hashcode(String key) {

        HashFunction hashFunction = Hashing.murmur3_32();
        HashCode hashCode = hashFunction.hashString(key, Charset.forName("utf-8"));
        return hashCode.asInt();
    }
}
