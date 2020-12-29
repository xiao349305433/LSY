package com.wu.loushanyun.basemvp.init;

public interface SnBlueToothListener {
    public void onChildConnectFailed(int i);

    public void onChildConnectSuccess();

    public void onChildNotify(byte[] bytes);

    public void onChildWriteSuccess();

    public void onChildWriteFailure(int i);
}
