package com.util.test;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpNode implements Node<String> {

    private String name;
    private String ip;
    private int weight;

    @Override
    public String getVirtualNodeName(int index) {
        return name + "#" + index;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getResource() {
        return ip;
    }
}
