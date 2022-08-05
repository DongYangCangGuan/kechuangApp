package com.cloud.servicemanage.entity;

/**
 * @author ： lijun
 * @date ：Created in 2019/5/3 12:09 AM
 * @description：
 * @modified By：
 * @version: $
 */

public class BaseAppBean extends BaseEntity {

    private int left;
    private int top;
    private int right;
    private int bottom;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

}
