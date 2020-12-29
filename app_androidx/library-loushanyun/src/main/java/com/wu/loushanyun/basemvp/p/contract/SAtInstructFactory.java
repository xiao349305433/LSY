package com.wu.loushanyun.basemvp.p.contract;

public interface SAtInstructFactory {
    SAtInstruct getSAtInstruct(String sAtParams);

    String getSAtTypeString(byte[] result);
}
