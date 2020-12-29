//
// Created by hengfeng zhuo on 2019/3/25.
//
#include <jni.h>
#include <istream>
#include <string.h>

#include "AndroidLog.h"

/**
 * 对YUV数据进行裁剪
 */
extern "C"
JNIEXPORT jint JNICALL Java_com_rokid_glass_videorecorder_camera_YUVDataHelper_cropYUV(
        JNIEnv *env, jclass type, jbyteArray src_, jint width,
        jint height, jbyteArray dst_, jint dst_width, jint dst_height,
        jint left, jint top) {
    //裁剪的区域大小不对
    if (left + dst_width > width || top + dst_height > height) {
        return -1;
    }

    //left和top必须为偶数，否则显示会有问题
    if (left % 2 != 0 || top % 2 != 0) {
        return -1;
    }

    //LOGD("cropYUV dst_width=%d, dst_height=%d, left=%d, top=%d", dst_width, dst_height, left, top);

    jint src_length = env->GetArrayLength(src_);
    jbyte *src_data = env->GetByteArrayElements(src_, NULL);
    jbyte *dst_data = env->GetByteArrayElements(dst_, NULL);

    for (int index = 0; index < dst_height; ++index) {
        memcpy(dst_data + index * dst_width, src_data + (index + top) * width + left, dst_width);
    }

    auto uvSrc = src_data + width * height;
    auto uvDst = dst_data + dst_width * dst_height;
    top = top / 2;
    for (int index = 0; index < dst_height / 2; ++index) {
        memcpy(uvDst + index * dst_width, uvSrc + left + (top + index) * width,
               dst_width);
    }

    env->ReleaseByteArrayElements(src_, src_data, 0);
    env->ReleaseByteArrayElements(dst_, dst_data, 0);
    return 0;
}