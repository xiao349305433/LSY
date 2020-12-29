package com.rokid.glass.videorecorder.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.SparseIntArray;

import androidx.annotation.NonNull;


import com.rokid.glass.videorecorder.utils.ByteArrayPool;
import com.rokid.glass.videorecorder.utils.ContextUtil;
import com.rokid.glass.videorecorder.utils.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static android.hardware.camera2.CameraDevice.TEMPLATE_STILL_CAPTURE;

/**
 * Camera2 Manager
 */
public class Camera2Manager {
    private static final SparseIntArray INTERNAL_FACINGS = new SparseIntArray();

    static {
        INTERNAL_FACINGS.put(0, CameraCharacteristics.LENS_FACING_BACK);
        INTERNAL_FACINGS.put(1, CameraCharacteristics.LENS_FACING_FRONT);
    }

    private final CameraManager mCameraManager;
    private int mFacing;
    private String mCameraId;
    private CameraCharacteristics mCameraCharacteristics;
    CameraDevice mCamera;
    CameraCaptureSession mCaptureSession;
    CaptureRequest.Builder mPreviewRequestBuilder;

    private ImageReader mImageReader;
    private int imageW = 1280;
    private int imageH = 720;
    private float cameraLength = imageW * imageH * 1.5f;
    private byte[] mCameraNV21Data;
    private Handler backgroundHandler, mainHandler, imageHandler;
    private HandlerThread handlerThread,imageThread;
//    private PreviewCallback mPreviewCallback;
    private Handler pictureHandler;
    private Map<Integer, PreviewCallback> previewCallbackMap = new ConcurrentHashMap<>();
    private static float ZOOM_LEVEL = 1f;

    private Camera2Manager() {
        mCameraManager = (CameraManager) ContextUtil.getApplicationContext()
                .getSystemService(Context.CAMERA_SERVICE);
        HandlerThread pictureThread = new HandlerThread("picture");
        pictureThread.start();
        pictureHandler = new Handler(pictureThread.getLooper());
    }

    private static final class Holder {
        private static final Camera2Manager INSTANCE = new Camera2Manager();
    }

    public static Camera2Manager getInstance() {
        return Holder.INSTANCE;
    }

    public void init(int width, int height) {

        this.imageW = width;
        this.imageH = height;
        cameraLength = imageW * imageH * 1.5f;
        //TODO
        if (!chooseCameraIdByFacing()) {
            return;
        }
        if(handlerThread == null){
            handlerThread = new HandlerThread("camera2");
            handlerThread.start();
            backgroundHandler = new Handler(handlerThread.getLooper());
        }
        if(imageThread == null){
            imageThread = new HandlerThread("image reader");
            imageThread.start();
            imageHandler = new Handler(imageThread.getLooper());
        }
        if(mainHandler == null){
            mainHandler = new Handler(Looper.getMainLooper());
        }

        prepareImageReader();
        startOpeningCamera();
    }

    public void destroy() {
        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
        if (mCamera != null) {
            mCamera.close();
            mCamera = null;
        }
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
    }

    private boolean chooseCameraIdByFacing() {
        try {
            int internalFacing = INTERNAL_FACINGS.get(mFacing);
            final String[] ids = mCameraManager.getCameraIdList();
            if (ids.length == 0) { // No camera
                throw new RuntimeException("No camera available.");
            }
            for (String id : ids) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(id);
                Integer level = characteristics.get(
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                if (level == null ||
                        level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                    continue;
                }
                Integer internal = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (internal == null) {
                    throw new NullPointerException("Unexpected state: LENS_FACING null");
                }
                if (internal == internalFacing) {
                    mCameraId = id;
                    mCameraCharacteristics = characteristics;
                    return true;
                }
            }
            // Not found
            mCameraId = ids[0];
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            Integer level = mCameraCharacteristics.get(
                    CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
            if (level == null ||
                    level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                return false;
            }
            Integer internal = mCameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (internal == null) {
                throw new NullPointerException("Unexpected state: LENS_FACING null");
            }
            for (int i = 0, count = INTERNAL_FACINGS.size(); i < count; i++) {
                if (INTERNAL_FACINGS.valueAt(i) == internal) {
                    mFacing = INTERNAL_FACINGS.keyAt(i);
                    return true;
                }
            }
            // The operation can reach here when the only camera device is an external one.
            // We treat it as facing back.
            mFacing = 0;
            return true;
        } catch (CameraAccessException e) {
            throw new RuntimeException("Failed to get a list of camera devices", e);
        }
    }

    private void prepareImageReader() {
        if (mImageReader != null) {
            mImageReader.close();
        }
        mImageReader = ImageReader.newInstance(imageW, imageH, ImageFormat.YUV_420_888, 1);
        mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, imageHandler);
    }

    private ImageReader mPictureReader;

    private ImageReader.OnImageAvailableListener mPictureListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireNextImage();
            ByteBuffer source = image.getPlanes()[0].getBuffer();
            byte[] imageData = ByteArrayPool.defaultPool().getBuf(source.remaining());
            source.get(imageData);
            reader.close();

//            BitmapUtils.saveData2File(imageData, PathUtils.generateFileName(PathUtils.FILE_TYPE.JPEG));

        }
    };

    private void preparePictureReader() {
        if (null != mPictureReader) {
            mPictureReader.close();
        }
        mImageReader = ImageReader.newInstance(imageW, imageH, ImageFormat.YUV_420_888, 1);
        mImageReader.setOnImageAvailableListener(mPictureListener, pictureHandler);
    }


    public void takePicture() {
        startOpeningCamera();
        preparePictureReader();
        try {
            final CaptureRequest.Builder captureRequestBuilder = mCamera.createCaptureRequest(TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_OFF);
            final CaptureRequest captureRequest = captureRequestBuilder.build();

            final CameraCaptureSession.StateCallback mPictureCallback = new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureRequest, null, pictureHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Logger.i("拍照失败");
                }
            };
            mCamera.createCaptureSession(Arrays.asList(mPictureReader.getSurface()),
                    mPictureCallback, backgroundHandler);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void startOpeningCamera() {
        try {
            mCameraManager.openCamera(mCameraId, mCameraDeviceCallback, mainHandler);
        } catch (CameraAccessException e) {
            //TODO
            throw new RuntimeException("Failed to open camera: " + mCameraId, e);
        }
    }

    void startCaptureSession() {
        if (!isCameraOpened() || mImageReader == null) {
            return;
        }

        try {
            mPreviewRequestBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(mImageReader.getSurface());
            mCamera.createCaptureSession(Arrays.asList(mImageReader.getSurface()),
                    mSessionCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            throw new RuntimeException("Failed to start camera session");
        }
    }

    boolean isCameraOpened() {
        return mCamera != null;
    }

    //Listener
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            try (Image image = reader.acquireNextImage()) {
                mCameraNV21Data = getDateFromImage(image);
                if (mCameraNV21Data.length <= cameraLength) {
                    //data
                    if(previewCallbackMap != null && previewCallbackMap.size() > 0){
                        Iterator<Map.Entry<Integer, PreviewCallback>> iterator = previewCallbackMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<Integer, PreviewCallback> toEvict = iterator.next();
                            PreviewCallback value = toEvict.getValue();
                            value.onPreviewTaken(mCameraNV21Data);
                        }
                    }
                    image.close();
                } else {
                    Logger.e("##### buffer length error");
                }
            }

        }
    };

    private final CameraDevice.StateCallback mCameraDeviceCallback
            = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCamera = camera;
//            mCallback.onCameraOpened();
            startCaptureSession();
        }

        @Override
        public void onClosed(@NonNull CameraDevice camera) {
//            mCallback.onCameraClosed();
            destroy();
            mCamera = null;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            destroy();
            mCamera = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Logger.e("onError: " + camera.getId() + " (" + error + ")");
            destroy();
            mCamera = null;
            init(imageW,imageH);
//            System.exit(6);
        }
    };

    private final CameraCaptureSession.StateCallback mSessionCallback
            = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            if (mCamera == null) {
                return;
            }
            mCaptureSession = session;
            Rect rect = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            float maxZoom = mCameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
            Logger.d("startCaptureSession--------->max zoom = " + maxZoom);
            if(ZOOM_LEVEL > maxZoom){
                ZOOM_LEVEL = maxZoom;
            }
            float ratio = 1f / ZOOM_LEVEL;
            int croppedWidth = rect.width() - Math.round((float) rect.width() * ratio);
            int croppedHeight = rect.height() - Math.round((float) rect.height() * ratio);

            Rect mZoom = new Rect(croppedWidth / 2, croppedHeight / 2,
                    rect.width() - croppedWidth / 2, rect.height() - croppedHeight / 2);
            mPreviewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION,mZoom);
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_OFF);
            try {
                mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(),
                        null, backgroundHandler);
            } catch (CameraAccessException e) {
                Logger.e("Failed to start camera preview because it couldn't access camera");
            } catch (IllegalStateException e) {
                Logger.e("Failed to start camera preview.");
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Logger.e("Failed to configure capture session.");
        }

        @Override
        public void onClosed(@NonNull CameraCaptureSession session) {
            if (mCaptureSession != null && mCaptureSession.equals(session)) {
                mCaptureSession = null;
            }
        }
    };

    public byte[] getDateFromImage(final Image image) {
        if (image != null) {
            Image.Plane[] planes = image.getPlanes();
            if (planes.length > 0) {
                ByteBuffer buffer = planes[0].getBuffer();
                int size = image.getWidth() * image.getHeight();
                int bufferSize = (size * 3) >> 1;
                if (mCameraNV21Data == null || mCameraNV21Data.length != bufferSize) {
                    if (null != mCameraNV21Data) {
                        ByteArrayPool.defaultPool().returnBuf(mCameraNV21Data);
                    }
                    mCameraNV21Data = ByteArrayPool.defaultPool().getBuf(bufferSize);
                }
                buffer.get(mCameraNV21Data, 0, size);
                ByteBuffer buffer2 = planes[2].getBuffer();
                buffer2.get(mCameraNV21Data, size, buffer2.remaining());
                return mCameraNV21Data;
            }
            return mCameraNV21Data;
        }
        return null;
    }

    public void registerPreviewCallBack(@CameraClient int client, PreviewCallback previewCallback){
        if(previewCallbackMap == null){
            previewCallbackMap = new HashMap<>();
        }
        previewCallbackMap.put(client,previewCallback);
    }

    /**
     * 反注册回调
     * @param client
     */
    public void unregisterPreviewCallBack(@CameraClient int client){
        if(previewCallbackMap != null && previewCallbackMap.containsKey(client)){
            previewCallbackMap.remove(client);
        }
    }

    // 解绑，是否相机
    public void release() {
        previewCallbackMap.clear();
        destroy();
        if(backgroundHandler != null){
            backgroundHandler.removeCallbacksAndMessages(null);
        }
        if(handlerThread != null){
            handlerThread.quitSafely();
        }

        if(imageHandler != null){
            imageHandler.removeCallbacksAndMessages(null);
        }
        if(imageThread != null){
            imageThread.quitSafely();
        }
        imageHandler = null;
        backgroundHandler = null;
        mainHandler = null;
    }
}
