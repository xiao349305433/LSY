package com.rokid.glass.videorecorder.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ByteArrayPool {
    private List<byte[]> mBuffersByLastUse = new LinkedList<>();
    private List<byte[]> mBuffersBySize = new ArrayList<>(64);
    private int mCurrentSize = 0;
    private final int mSizeLimit;
    protected static final Comparator<byte[]> BUF_COMPARATOR = new Comparator<byte[]>() {
        public int compare(byte[] lhs, byte[] rhs) {
            return lhs.length - rhs.length;
        }
    };

    public ByteArrayPool(int sizeLimit) {
        this.mSizeLimit = sizeLimit;
    }

    public static ByteArrayPool defaultPool() {
        return ByteArrayPool.DEFAULT_HOLDER.DEFAULT_POOL;
    }

    public synchronized byte[] getBuf(int len) {
        for(int i = 0; i < this.mBuffersBySize.size(); ++i) {
            byte[] buf = (byte[])this.mBuffersBySize.get(i);
            if (buf.length >= len) {
                this.mCurrentSize -= buf.length;
                this.mBuffersBySize.remove(i);
                this.mBuffersByLastUse.remove(buf);
                return buf;
            }
        }

        return new byte[len];
    }


    public synchronized void returnBuf(byte[] buf) {
        if (buf != null && buf.length <= this.mSizeLimit) {
            this.mBuffersByLastUse.add(buf);
            int pos = Collections.binarySearch(this.mBuffersBySize, buf, BUF_COMPARATOR);
            if (pos < 0) {
                pos = -pos - 1;
            }

            this.mBuffersBySize.add(pos, buf);
            this.mCurrentSize += buf.length;
            this.trim();
        }
    }

    private synchronized void trim() {
        while(this.mCurrentSize > this.mSizeLimit) {
            Logger.e("ByteArrayPool trigger trim!!!!");
            byte[] buf = (byte[])this.mBuffersByLastUse.remove(0);
            this.mBuffersBySize.remove(buf);
            this.mCurrentSize -= buf.length;
        }

    }

    private static class DEFAULT_HOLDER {
        private static ByteArrayPool DEFAULT_POOL = new ByteArrayPool(150 * 1024 * 1024);

        private DEFAULT_HOLDER() {
        }
    }
}

