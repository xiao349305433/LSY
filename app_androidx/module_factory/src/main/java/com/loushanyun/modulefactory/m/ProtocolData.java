package com.loushanyun.modulefactory.m;

import java.util.Objects;

/**
 * 一号模组自定义51字节长度协议的子元素封装类
 * @author 喻南豪
 */
public class ProtocolData {
    private int index;
    private int length;
    private String name;

    public ProtocolData() {
        index = 0;
        length = 4;
        name = "测试项目";
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtocolData that = (ProtocolData) o;
        return index == that.index &&
                length == that.length &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, length, name);
    }

    @Override
    public String toString() {
        return "ProtocolData{" +
                "index=" + index +
                ", length=" + length +
                ", name='" + name + '\'' +
                '}';
    }
}
