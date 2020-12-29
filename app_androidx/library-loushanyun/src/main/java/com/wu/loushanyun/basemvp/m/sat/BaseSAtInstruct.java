package com.wu.loushanyun.basemvp.m.sat;

import com.wu.loushanyun.basemvp.p.contract.SAtInstruct;

public abstract class BaseSAtInstruct implements SAtInstruct {
    protected double hardVersion;

    public BaseSAtInstruct(double hardVersion) {
        this.hardVersion = hardVersion;
    }

    public double getHardVersion() {
        return hardVersion;
    }

    public void setHardVersion(double hardVersion) {
        this.hardVersion = hardVersion;
    }
}
