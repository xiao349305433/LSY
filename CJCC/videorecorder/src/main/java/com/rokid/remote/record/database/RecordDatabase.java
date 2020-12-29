package com.rokid.remote.record.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rokid.recordapi.RecordEntity;
import com.rokid.remote.record.util.Constants;


/**
 * Author: heshun
 * Date: 2020/7/7 11:16 AM
 * gmail: shunhe1991@gmail.com
 */
@Database(entities = {RecordEntity.class}, version = 1, exportSchema = false)
public abstract class RecordDatabase extends RoomDatabase {


    private static RecordDatabase instance;

    public abstract RecordDao recordDao();

    public static void init(Context context) {
        if (null == instance) {
            instance = Room.databaseBuilder(context.getApplicationContext(), RecordDatabase.class, Constants.MEDIA_RECORD_DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
    }

    public static RecordDatabase getInstance() {
        return instance;
    }
}
