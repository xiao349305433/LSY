package com.test1moudle.p.presenter;


import android.graphics.Bitmap;

import com.test1moudle.init.TestEventCode;
import com.test1moudle.p.contract.TestContract;
import com.test1moudle.p.runner.GeneralFromLoginRunner;
import com.test1moudle.p.runner.TestRunner1;
import com.test1moudle.p.runner.TestRunner2;
import com.test1moudle.p.runner.TestRunner3;
import com.test1moudle.p.util.VideoConstant;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.MediaUtils;

/**
 * Created by baixiaokang on 16/5/4.
 */
public class TestPresenter extends TestContract.Presenter {
    @Override
    protected void initPresenter() {
        registerEventRunner(TestEventCode.HTTP_Test, new GeneralFromLoginRunner());
        registerEventRunner(TestEventCode.HTTP_Test1, new TestRunner1());
        registerEventRunner(TestEventCode.HTTP_Test2, new TestRunner2());
        registerEventRunner(TestEventCode.HTTP_Test3, new TestRunner3());
    }


    @Override
    public void test() {
        LogUtils.i("hahahahahh");
        pushEvent(TestEventCode.HTTP_Test, "15527919058", "111111");
        pushEvent(TestEventCode.HTTP_Test, "15527919058", "111111");
        pushEvent(TestEventCode.HTTP_Test, "15527919058", "111111");
        pushEvent(TestEventCode.HTTP_Test, "15527919058", "111111");

    }

    @Override
    public Bitmap getFirstBitMap() {
      compositeDisposable.add(Flowable.create(
              (FlowableOnSubscribe<Bitmap>) emitter ->{
                  Bitmap bitmap=  MediaUtils.getNetVideoBitmap(VideoConstant.videoUrlList[0]);
                  if(bitmap!=null){
                      emitter.onNext(bitmap);
                      emitter.onComplete();
                  }else {
                      emitter.onError(new NullPointerException());
                  }
              }
              , BackpressureStrategy.ERROR)
              .compose(RxSchedulers.io_main())
              .subscribe(bitMap ->{
                  //加载到ImageView控件上
                  LinkedHashMap map = new LinkedHashMap();
                  map.put("高清", VideoConstant.videoUrlList[0]);
                  map.put("标清", VideoConstant.videoUrlList[1]);
                  map.put("普清", VideoConstant.videoUrlList[2]);
                  Object[] objects = new Object[3];
                  objects[0] = map;
                  objects[1] = true;
                  objects[2] = new HashMap<>();
                  ((HashMap) objects[2]).put("key", "value");
                  mView.setBitmap(bitMap,objects);
              } ));
        return null;
    }


    @Override
    public void onEventRunEnd(Event event, int code) {
        if (code == TestEventCode.HTTP_Test) {
            String text= (String) event.getParamAtIndex(0);
            mView.setText(text);
        } else if (code == TestEventCode.HTTP_Test1) {
            HashMap<String, String> mapValues = (HashMap<String, String>) event.getReturnParamAtIndex(0);
        } else if (code == TestEventCode.HTTP_Test2) {
            HashMap<String, String> mapValues = (HashMap<String, String>) event.getReturnParamAtIndex(0);
        }
    }

    @Override
    public void testPermission(String text) {
        pushEvent(TestEventCode.HTTP_Test, text, "111111");
//        RxPermissions rxPermissions = new RxPermissions(getActivity());
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
//        if (rxPermissions.isRevoked(Manifest.permission.CAMERA)) {
//            getActivity().startActivity(intent);
//        } else {
//            rxPermissions.request(Manifest.permission.CAMERA).subscribe(aBoolean -> {
//                if (aBoolean) {
//                    mView.setText(text);
//                } else {
//                    ToastManager.getInstance(getActivity()).showToast("没有权限");
//                }
//            });
//
//        }
    }
}



