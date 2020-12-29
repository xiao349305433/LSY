package com.wu.loushanyun.basemvp.p.contract;

import java.util.HashMap;

public interface SAtInstruct {

    /**
     * 1号模组设置命令
     *
     * @param params
     * @return
     */
    byte[] getOneWriteBytes(String... params) throws Exception;

    /**
     * 2号模组设置命令
     *
     * @param params
     * @return
     */
    byte[] getTwoWriteBytes(String... params) throws Exception;

    /**
     * 3号模组设置命令
     *
     * @param params
     * @return
     */
    byte[] getThreeWriteBytes(String... params) throws Exception;

    /**
     * 4号模组设置命令
     *
     * @param params
     * @return
     */
    byte[] getFourWriteBytes(String... params) throws Exception;

    /**
     * 1号模组读取命令
     *
     * @param params
     * @return
     */
    byte[] getOneReadBytes(String... params) throws Exception;

    /**
     * 2号模组读取命令
     *
     * @param params
     * @return
     */
    byte[] getTwoReadBytes(String... params) throws Exception;

    /**
     * 3号模组读取命令
     *
     * @param params
     * @return
     */
    byte[] getThreeReadBytes(String... params) throws Exception;

    /**
     * 4号模组读取命令
     *
     * @param params
     * @return
     */
    byte[] getFourReadBytes(String... params) throws Exception;


    HashMap<String, String> parseOneWNotifyBytes(byte[] bytes) throws Exception;
    HashMap<String, String> parseOneRNotifyBytes(byte[] bytes) throws Exception;

    HashMap<String, String> parseTwoWNotifyBytes(byte[] bytes) throws Exception;
    HashMap<String, String> parseTwoRNotifyBytes(byte[] bytes) throws Exception;

    HashMap<String, String> parseThreeWNotifyBytes(byte[] bytes) throws Exception;
    HashMap<String, String> parseThreeRNotifyBytes(byte[] bytes) throws Exception;

    HashMap<String, String> parseFourWNotifyBytes(byte[] bytes) throws Exception;
    HashMap<String, String> parseFourRNotifyBytes(byte[] bytes) throws Exception;
}
