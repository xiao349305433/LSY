// IRKMediaService.aidl
package com.rokid.recordapi;

import com.rokid.recordapi.RKMediaCallback;
import com.rokid.recordapi.RecordEntity;



interface IRKMediaService {
  void init();

    void startRecordVideo();

    void stopRecordVideo();

    void startRecordAudio();

    void stopRecordAudio();

    void takePicture();

    void addMediaCallback(RKMediaCallback callback);

    void removeMediaCallback(RKMediaCallback callback);

    String getCurrentState();

    long getRecordStartTime();

    int queryRecordCount(String mediaType);

    int queryRecordCountWithTimestamp(String mediaType, long startTime, long endTime);

    List<RecordEntity> queryRecord(String mediaType);

    List<RecordEntity> queryRecordWithTimestamp(String mediaType, long startTime, long endTime);

    List<RecordEntity> queryRecordWithTimestampAndPage(String mediaType, long startTime, long endTime, int page, int pageCount);

    void deleteAll(String mediaType);

    void deleteWithTime(String mediaType, long startTime, long endTime);

    void deleteWithIds(inout int[] ids);
}
