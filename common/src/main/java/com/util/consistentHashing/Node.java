package com.util.consistentHashing;


public interface Node<T> {


    public String getVirtualNodeName(int index);


    public int getWeight();


    public T getResource();

}