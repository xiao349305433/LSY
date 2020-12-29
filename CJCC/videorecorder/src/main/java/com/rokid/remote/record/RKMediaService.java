package com.rokid.remote.record;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;

import com.rokid.glass.videorecorder.utils.JSONHelper;
import com.rokid.glass.videorecorder.utils.Logger;
import com.rokid.recordapi.IRKMediaService;
import com.rokid.recordapi.RKMediaCallback;
import com.rokid.recordapi.RecordEntity;
import com.rokid.remote.record.database.RecordDatabase;
import com.rokid.remote.record.mediacodec.RecordManager;
import com.rokid.remote.record.mediacodec.base.GBMediaParam;
import com.rokid.remote.record.mediacodec.callback.AudioCallback;
import com.rokid.remote.record.mediacodec.callback.VideoCallback;
import com.rokid.remote.record.model.RecordState;
import com.rokid.remote.record.model.StateModel;
import com.rokid.remote.record.util.CameraManager;
import com.rokid.remote.record.util.PathUtils;
import com.rokid.remote.record.util.RKSingleton;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RKMediaService extends Service {

    private static GBMediaParam gbMediaParam;
    private RKMediaCallback mediaCallback;
    private StateModel mState = new StateModel();
    public static final String WATER_PATTERN = "yyyy-MM-dd HH:mm:ss:SSS";
    private static AtomicBoolean hasInit = new AtomicBoolean(false);

    static {
        gbMediaParam = new GBMediaParam.Builder()
                .setAudioBitRate(64_000)
                .setAudioChannel(2)
                .setAudioChannelMode(AudioFormat.CHANNEL_IN_STEREO)
                .setAudioFormat(AudioFormat.ENCODING_PCM_16BIT)
                .setAudioMimeType(MediaFormat.MIMETYPE_AUDIO_AAC)
                .setAudioSampleRate(44100)
                .setVideoWidth(1280)
                .setVideoHeight(720)
                .setVideoBitRate(10 * 1024 * 1024)  //bps
                .setVideoFrameRate(30)
                .setVideoGOP(2)
                .build();
    }

    private RKSingleton<RKMediaServiceProxy> PROXY = new RKSingleton<RKMediaServiceProxy>() {
        @Override
        public RKMediaServiceProxy create() {
            return new RKMediaServiceProxy();
        }
    };


    public RKMediaService() {
    }

    public static final String KEY_TYPE = "type";
    public static final String KEY_ACTION = "action";
    public static final String AUDIO = "audio";
    public static final String VIDEO = "video";
    public static final String PHOTO = "photo";
    public static final String ACTION_START = "start";
    public static final String ACTION_STOP = "stop";
    public static final String ACTION_SWITCH = "switch";

    public static void startTask(Context context, Intent data) {
        if (null == data) return;
        String type = data.getStringExtra(KEY_TYPE);
        String action = data.getStringExtra(KEY_ACTION);
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(action)) return;


        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(context, RKMediaService.class);
        intent.setComponent(componentName);

        if (!type.equals(AUDIO) && !type.equals(VIDEO) && !type.equals(PHOTO)) return;
        intent.putExtra(KEY_TYPE, type);

        intent.putExtra(KEY_ACTION, action);
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return PROXY.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("onStartCommand");
        modifyForAndroidO();
        handleIntent(intent);
        return START_STICKY;
    }

    private static long startTime = 0;

    private void handleIntent(Intent intent) {
        if (null == intent) return;

        String type = intent.getStringExtra(KEY_TYPE);
        String action = intent.getStringExtra(KEY_ACTION);
        Logger.i("handleIntent", type, action);

        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(action)) return;

        _init();
        String currentState = mState.getCurrentState();
        if (type.equals(VIDEO)) {
            if (currentState.equals(RecordState.RECORDING_AUDIO)) {
                _stopRecordAudio();
            }

            if (currentState.equals(RecordState.RECORDING_VIDEO)) {
                _stopRecordVideo();

            } else {
                startTime = SystemClock.elapsedRealtime();
                _startRecordVideo();
            }

//            if (action.equals(ACTION_START) && !currentState.equals(RecordState.RECORDING_VIDEO)) {
//                startTime = SystemClock.elapsedRealtime();
//                RecordWindowManager.getInstance().show(startTime, this);
//                _startRecordVideo();
//            } else if (action.equals(ACTION_STOP) && currentState.equals(RecordState.RECORDING_VIDEO)) {
//
//                _stopRecordVideo();
//                RecordWindowManager.getInstance().hide();
//            }
        } else if (type.equals(AUDIO)) {
            if (currentState.equals(RecordState.RECORDING_VIDEO)) {
                Logger.i("current state is recording video");
                return;
            }
            if (action.equals(ACTION_START) && !currentState.equals(RecordState.RECORDING_AUDIO)) {
                startTime = SystemClock.elapsedRealtime();
                _startRecordAudio();
            } else if (action.equals(ACTION_STOP) && currentState.equals(RecordState.RECORDING_AUDIO)) {

                _stopRecordAudio();
            }
        } else if (type.equals(PHOTO)) {
            _takePicture();
        }
    }

    private void _init() {
        if (hasInit.get()) {
            if (null != mediaCallback) {
                try {
                    mediaCallback.onServiceReady(true, null);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        hasInit.getAndSet(true);

        RecordManager.getInstance().init(this.getApplicationContext(), gbMediaParam);

        CameraManager.getInstance().init(getApplicationContext());


        YuvOsdUtils.initOsd(100, 50, WATER_PATTERN.length(), 1280, 720, 0, 17, 23);

        if (null != mediaCallback) {
            try {
                mediaCallback.onServiceReady(true, null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断当前状态是否可以进行录音
     * 定义：在录制视频状态下不可以进行音频的录制
     *
     * @return
     */
    public boolean canAudio() {
        String currentState = mState.getCurrentState();
        return !currentState.equals(RecordState.RECORDING_VIDEO);
    }

    /**
     * 判断当前是否可以进行视频录制
     * 定义：如果当前正在进行音频录制 要求先暂停音频的录制，然后启动视频的录制
     * 因此 在任意时刻都将被允许开启视频的录制，只是音频需要先暂停音频的录制，然后才开启视频的录制
     *
     * @return
     */
    public boolean canVideo() {
        return true;
    }

    /**
     * 判断当前状态是否可以进行拍照
     * 定义：当在录像状态时，需要被允许快捷方式进行拍照(要求照片的分辨率与视频的分辨率一致)，目前的策略可以是取视频录制中的一帧数据
     * 理论上在任何状态下都被允许进行拍照
     *
     * @return
     */
    public boolean canPicture() {
        return true;
    }

    private void _stopRecordAudio() {
        String currentState = mState.getCurrentState();

        if (currentState.equals(RecordState.RECORDING_AUDIO)) {
            RecordManager.getInstance().stopAudioRecording();
        } else {
            if (null != mediaCallback) {
                try {
                    mediaCallback.onAudioFailed(-1, "something error");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 进行录制音频动作
     */
    private void _startRecordAudio() {
        if (!canAudio()) {
            Logger.w("current state can not record audio!");
            return;
        }

        mState.setCurrentState(RecordState.RECORDING_AUDIO);
        RecordManager.getInstance().startAudioRecording(new AudioCallback() {
            @Override
            public void onAudioRecordStart() {
                mState.setCurrentState(RecordState.RECORDING_AUDIO);
                if (null != mediaCallback) {
                    try {
                        mediaCallback.onAudioStart();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onAudioRecordStopped(String filePath) {
                mState.setCurrentState(RecordState.AUDIO);
                startTime = 0;
                if (null != mediaCallback) {
                    try {
                        mediaCallback.onAudioStopped(filePath);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onAudioRecordFailed() {
                mState.setCurrentState(RecordState.AUDIO);
                if (null != mediaCallback) {
                    try {
                        mediaCallback.onAudioFailed(-1, null);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 进行拍照动作
     */
    private void _takePicture() {
        RecordManager.getInstance().takePhoto(mediaCallback);
    }

    /**
     * 停止视频录制动作
     */
    private void _stopRecordVideo() {
        String currentState = mState.getCurrentState();

        if (currentState.equals(RecordState.RECORDING_VIDEO)) {
            RecordManager.getInstance().stopVideoRecording();
        } else {
            if (null != mediaCallback) {
                try {
                    mediaCallback.onVideoFailed(-1, "something error");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 开始视频录制动作
     */
    private void _startRecordVideo() {
        if (!canVideo()) {
            Logger.w("can not record video");
            return;
        }

        String currentState = mState.getCurrentState();
        if (currentState.equals(RecordState.RECORDING_AUDIO)) {
            RecordManager.getInstance().stopAudioRecording();
        }

        mState.setCurrentState(RecordState.RECORDING_VIDEO);
        RecordManager.getInstance().startVideoRecording(new VideoCallback() {
            @Override
            public void onVideoStart() {
                mState.setCurrentState(RecordState.RECORDING_VIDEO);
                if (null != mediaCallback) {
                    try {
                        mediaCallback.onVideoStart();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onVideoStopped(String filePath) {
                startTime = 0;
                mState.setCurrentState(RecordState.VIDEO);
                if (null != mediaCallback) {
                    try {
                        mediaCallback.onVideoStopped(filePath);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onVideoFailed(int errorCode, String errorMessage) {
                mState.setCurrentState(RecordState.VIDEO);
                if (null != mediaCallback) {
                    try {
                        mediaCallback.onVideoFailed(errorCode, errorMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String _getCurrentState() {
        return mState.getCurrentState();
    }

    private class RKMediaServiceProxy extends IRKMediaService.Stub {

        @Override
        public void init() throws RemoteException {
            _init();
        }


        @Override
        public void startRecordVideo() throws RemoteException {
            _startRecordVideo();
        }

        @Override
        public void stopRecordVideo() throws RemoteException {
            _stopRecordVideo();
        }

        @Override
        public void startRecordAudio() throws RemoteException {
            _startRecordAudio();
        }

        @Override
        public void stopRecordAudio() throws RemoteException {
            _stopRecordAudio();
        }

        @Override
        public void takePicture() throws RemoteException {
            _takePicture();
        }

        @Override
        public void addMediaCallback(RKMediaCallback callback) throws RemoteException {
            mediaCallback = callback;
        }

        @Override
        public void removeMediaCallback(RKMediaCallback callback) throws RemoteException {
            mediaCallback = null;
        }

        @Override
        public String getCurrentState() throws RemoteException {
            return _getCurrentState();
        }

        @Override
        public long getRecordStartTime() throws RemoteException {
            return startTime;
        }

        @Override
        public int queryRecordCount(String mediaType) throws RemoteException {
            if(TextUtils.isEmpty(mediaType)){
                Logger.w("queryRecordCount------>mediaType is empty");
                return -1;
            }
            int count = RecordDatabase.getInstance().recordDao().queryTotalCountByMediaType(mediaType.toUpperCase());

            Logger.d("queryRecordCount:", String.valueOf(count));

            return count;
        }

        @Override
        public int queryRecordCountWithTimestamp(String mediaType, long startTime, long endTime) throws RemoteException {
            if(TextUtils.isEmpty(mediaType)){
                Logger.w("queryRecordCountWithTimestamp------>mediaType is empty");
                return -1;
            }
            return RecordDatabase.getInstance().recordDao().queryTotalCountByMediaTypeWithTimestamp(mediaType.toUpperCase(), startTime, endTime);
        }

        @Override
        public List<RecordEntity> queryRecord(String mediaType) throws RemoteException {
            if(TextUtils.isEmpty(mediaType)){
                Logger.w("queryRecord------>mediaType is empty");
                return null;
            }
            List<RecordEntity> recordEntities = RecordDatabase.getInstance().recordDao().queryAllByMediaType(mediaType.toUpperCase());
            Logger.d("queryRecord:", JSONHelper.toJson(recordEntities));
            return recordEntities;
        }

        @Override
        public List<RecordEntity> queryRecordWithTimestamp(String mediaType, long startTime, long endTime) throws RemoteException {
            if(TextUtils.isEmpty(mediaType)){
                Logger.w("queryRecordWithTimestamp------>mediaType is empty");
                return null;
            }
            List<RecordEntity> recordEntities = RecordDatabase.getInstance().recordDao().queryWithMediaTypeAndTimestamp(mediaType.toUpperCase(), startTime, endTime);

            Logger.d("queryRecordWithTimestamp:", JSONHelper.toJson(recordEntities));
            return recordEntities;
        }

        @Override
        public List<RecordEntity> queryRecordWithTimestampAndPage(String mediaType, long startTime,
                                                                  long endTime, int page, int pageCount) throws RemoteException {
            if(TextUtils.isEmpty(mediaType)){
                Logger.w("queryRecordWithTimestampAndPage------>mediaType is empty");
                return null;
            }
            if (startTime == -1) {
                List<RecordEntity> recordEntities = RecordDatabase.getInstance().recordDao().queryPageWithMediaType(mediaType.toUpperCase(), pageCount, (page - 1) * pageCount);
                Logger.d("queryRecordWithTimestampAndPage:", JSONHelper.toJson(recordEntities));
                return recordEntities;
            }
            List<RecordEntity> recordEntities = RecordDatabase.getInstance().recordDao().queryPageWithMediaTypeAndTimestamp(mediaType.toUpperCase(), startTime, endTime, pageCount, (page - 1) * pageCount);
            Logger.d("queryRecordWithTimestampAndPage:", JSONHelper.toJson(recordEntities));
            return recordEntities;
        }

        @Override
        public void deleteAll(String mediaType) throws RemoteException {
            List<RecordEntity> recordEntities = queryRecord(mediaType);
            for (RecordEntity recordEntity : recordEntities) {
                File file = new File(recordEntity.getFilePath());
                if (recordEntity.getMediaType().equalsIgnoreCase(PathUtils.FILE_TYPE.VIDEO.prefix)) {
                    File thumbnailFile = new File(recordEntity.getThumbnailPath());
                    if (thumbnailFile.exists()) thumbnailFile.delete();
                }
                if (file.exists()) file.delete();
            }
            RecordDatabase.getInstance().recordDao().deleteAllByMediaType(mediaType);
        }

        @Override
        public void deleteWithTime(String mediaType, long startTime, long endTime) throws RemoteException {
            List<RecordEntity> recordEntities = queryRecordWithTimestamp(mediaType, startTime, endTime);
            for (RecordEntity recordEntity : recordEntities) {
                File file = new File(recordEntity.getFilePath());
                if (recordEntity.getMediaType().equalsIgnoreCase(PathUtils.FILE_TYPE.VIDEO.prefix)) {
                    File thumbnailFile = new File(recordEntity.getThumbnailPath());
                    if (thumbnailFile.exists()) thumbnailFile.delete();
                }
                if (file.exists()) file.delete();
            }
            RecordDatabase.getInstance().recordDao().deleteAllByTimestamp(startTime, endTime);
        }

        @Override
        public void deleteWithIds(int[] ids) throws RemoteException {
            List<RecordEntity> recordEntities = RecordDatabase.getInstance().recordDao().queryAllByIds(ids);
            for (RecordEntity recordEntity : recordEntities) {
                Logger.d("deleteWithIds----->" + JSONHelper.toJson(recordEntity));
                File file = new File(recordEntity.getFilePath());
                if (recordEntity.getMediaType().equalsIgnoreCase(PathUtils.FILE_TYPE.VIDEO.prefix)) {
                    File thumbnailFile = new File(recordEntity.getThumbnailPath());
                    if (thumbnailFile.exists()) {
                        Logger.d("deleteWithIds----->thumbnail File: " + thumbnailFile.getPath());
                        thumbnailFile.delete();
                    }
                }
                if (file.exists()) {
                    Logger.d("deleteWithIds-----> File: " + file.getPath());
                    file.delete();
                }
            }
            RecordDatabase.getInstance().recordDao().deleteAllById(ids);
        }

    }

    private void modifyForAndroidO() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001", "channel01", NotificationManager.IMPORTANCE_MIN);
            channel.enableVibration(false);//去除振动

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) manager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(getApplicationContext(), "001");
            startForeground(1, builder.build());//id must not be 0,即禁止是0
        }
    }

}
