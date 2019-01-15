package com.util.test;

public class Main {
    public static void main(String[] args) {
        IpNode ipNode1 = new IpNode("ipTest", "192.168.0.1", 1);
        IpNode ipNode2 = new IpNode("ipTest", "192.168.0.2", 1);
        IpNode ipNode3 = new IpNode("ipTest", "192.168.0.3", 1);


        ConsistentHashing<String> consistentHashing = new ConsistentHashing<>();
        consistentHashing.addHostNode(ipNode1);
        consistentHashing.addHostNode(ipNode2);
        consistentHashing.addHostNode(ipNode3);

        Node<String> abc = consistentHashing.getNodeByKey("abc123");
        System.out.println(abc.getVirtualNodeName(12));
        System.out.println(abc.getResource());
    }
}
