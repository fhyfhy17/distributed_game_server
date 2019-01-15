package com.util.test;

/**
 * 此类主要目的:
 * 0、节点的信息和操作
 *
 * @author <a href="mailto:liu_jing_wei@sohu.com">Dreams Liu </a>
 * @version $Revision 1.1 $ 2018年8月10日 上午8:40:44
 */
public interface Node<T> {
    /**
     * 计算node的虚拟节点名称
     */
    public String getVirtualNodeName(int index);

    /**
     * 返回node权重
     *
     * @return
     */
    public int getWeight();

    /**
     * 返回node管理的资源
     *
     * @return
     */
    public T getResource();

}