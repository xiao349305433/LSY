package com.test1moudle.m;

import org.litepal.annotation.Encrypt;
import org.litepal.crud.DataSupport;

/**
 * Created by huxu on 2018/2/2.
 */

public class LitePalTest extends DataSupport {
    private int tag;
    @Encrypt(algorithm = AES)
    private String name;

    @Override
    public String toString() {
        return "LitePalTest{" +
                "tag=" + tag +
                ", name='" + name + '\'' +
                '}';
    }

    public LitePalTest(int tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
