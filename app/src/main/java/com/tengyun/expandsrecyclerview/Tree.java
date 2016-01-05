package com.tengyun.expandsrecyclerview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/5.
 */
public class Tree<T> {
    //构造一个树形结构
    private T data; //数据
    private int level = 0; //层级,默认是0
    private List<Tree<?>> chlidren; //子数据
    private boolean expand = false;   //是否展开，默认不展开

    public Tree(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Tree<?>> getChlidren() {
        return chlidren;
    }

    public void setChlidren(List<Tree<?>> chlidren) {
        this.chlidren = chlidren;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    /**
     * 判断是否可以被展开
     * @return
     */
    public boolean isExpandable(){
        return chlidren != null && !chlidren.isEmpty();
    }

    /**
     * 添加一个子节点
     * @param child
     */
    public void addChild(Tree<?> child){
        if (chlidren == null){
            chlidren = new ArrayList<>();
        }
        //设置层级
        child.setLevel(level + 1);
        chlidren.add(child);
    }
}
