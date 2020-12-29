package com.rokid.remote.record.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rokid.recordapi.RecordEntity;
import com.rokid.remote.record.util.Constants;

import java.util.List;

/**
 * Author: heshun
 * Date: 2020/7/7 11:16 AM
 * gmail: shunhe1991@gmail.com
 */
@Dao
public interface RecordDao {


    @Query("SELECT * FROM " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE media_type = :mediaType ORDER BY id ASC")
    List<RecordEntity> queryAllByMediaType(String mediaType);

    @Query("SELECT * FROM " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE media_type = :mediaType ORDER BY id DESC")
    LiveData<List<RecordEntity>> queryByMediaType(String mediaType);

    @Query("SELECT * FROM  " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE file_path IN (:path) ORDER BY id ASC")
    List<RecordEntity> queryAllByPath(List<String> path);

    @Query("SELECT COUNT(*) FROM " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE media_type = :mediaType")
    int queryTotalCountByMediaType(String mediaType);

    @Query("SELECT COUNT(*) FROM " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE media_type = :mediaType AND create_time >= :startTimestamp AND create_time <= :endTimestamp")
    int queryTotalCountByMediaTypeWithTimestamp(String mediaType, long startTimestamp, long endTimestamp);

    @Query("SELECT * FROM "+ Constants.MEDIA_RECORD_TABLE_NAME + " WHERE create_time >= :startTimestamp AND create_time <= :endTimeStamp AND media_type = :mediaType ORDER BY id ASC LIMIT :limit OFFSET :offset")
    List<RecordEntity> queryPageWithMediaTypeAndTimestamp(String mediaType, long startTimestamp, long endTimeStamp, int limit, int offset);

    @Query("SELECT * FROM " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE create_time >= :startTimestamp AND create_time <= :endTimeStamp AND media_type = :mediaType ORDER BY id ASC")
    List<RecordEntity> queryWithMediaTypeAndTimestamp(String mediaType, long startTimestamp, long endTimeStamp);

    @Query("DELETE FROM " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE id IN (:ids)")
    void deleteAllById(int[] ids);

    @Query("DELETE FROM " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE media_type = :mediaType")
    void deleteAllByMediaType(String mediaType);

    @Query("DELETE FROM " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE create_time >= :startTime AND create_time <= :endTime")
    void deleteAllByTimestamp(long startTime, long endTime);

    @Query("SELECT * FROM " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE media_type = :mediaType ORDER BY id ASC LIMIT :limit OFFSET :offset")
    List<RecordEntity> queryPageWithMediaType(String mediaType, int limit, int offset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRecord(RecordEntity recordEntity);

    @Query("SELECT * FROM " + Constants.MEDIA_RECORD_TABLE_NAME + " WHERE id IN (:ids) ORDER BY id ASC")
    List<RecordEntity> queryAllByIds(int[] ids);
}
