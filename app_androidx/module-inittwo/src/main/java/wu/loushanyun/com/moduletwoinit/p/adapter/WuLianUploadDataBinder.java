package wu.loushanyun.com.moduletwoinit.p.adapter;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wu.loushanyun.base.config.K;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.ToastUtils;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.InsideSaveOnetoOneData;
import wu.loushanyun.com.moduletwoinit.m.OnetoOneConverter;
import wu.loushanyun.com.moduletwoinit.m.WuLianUploadData;
import wu.loushanyun.com.moduletwoinit.p.runner.SaveOnetoOneMeterRunner;

public class WuLianUploadDataBinder extends ItemViewBinder<WuLianUploadData, WuLianUploadDataBinder.ViewHolder> {


    @Override
    protected void initEventRunner() {
        registerEventRunner(MInitTwoCode.SaveOnetoOneMeterRunner, new SaveOnetoOneMeterRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        super.onEventRunEnd(event, code);
        if (code == MInitTwoCode.SaveOnetoOneMeterRunner) {
            int position = (int) event.getParamAtIndex(1);
            int codeR = (int) event.getReturnParamAtIndex(0);
            String msg = (String) event.getReturnParamAtIndex(1);
            if (codeR == 0) {
                ToastUtils.showLong("上传成功");
                WuLianUploadData wuLianUploadData = (WuLianUploadData) getAdapter().getItems().get(position);
//                wuLianUploadData.deleteAsync().listen(rowsAffected -> {
//                    getAdapter().getItems().remove(position);
//                    getAdapter().notifyItemRemoved(position);
//                });
            } else {
                ToastUtils.showLong(msg);
            }
        }
    }



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.m_twoinit_item_wulianlist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull WuLianUploadData wuLianUploadData) {
        holder.textQuyuhao.setText("网格号:" + wuLianUploadData.getAreaNumber());
        holder.time.setText("保存时间:" + wuLianUploadData.getTime());
        holder.textQuyumingcheng.setText("网格名称:" + wuLianUploadData.getAreaName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textQuyuhao;
        private TextView textQuyumingcheng;
        private TextView time;
        private TextView upload;
        private TextView delete;

        public ViewHolder(View view) {
            super(view);
            textQuyuhao = (TextView) view.findViewById(R.id.text_quyuhao);
            textQuyumingcheng = (TextView) view.findViewById(R.id.text_quyumingcheng);
            time = (TextView) view.findViewById(R.id.time);
            upload = (TextView) view.findViewById(R.id.upload);
            delete = (TextView) view.findViewById(R.id.delete);

            view.setOnClickListener(v -> {
                try {
                    WuLianUploadData wuLianUploadData = getContentByViewHolder(this);
                    Bundle bundle = new Bundle();
                    bundle.putLong("id", wuLianUploadData.getBaseObjId());
                    ARouter.getInstance().build(K.UpdateLocationActivity).with(bundle).navigation();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            upload.setOnClickListener(v -> {
                try {
                    WuLianUploadData wuLianUploadData = getContentByViewHolder(ViewHolder.this);
                    compositeDisposable.add(Flowable.create(
                            (FlowableOnSubscribe<InsideSaveOnetoOneData>) emitter -> {
                                InsideSaveOnetoOneData data = new InsideSaveOnetoOneData();
                                data.setAreaName(wuLianUploadData.getAreaName());
                                data.setAreaNumber(wuLianUploadData.getAreaNumber());
                                data.setBusinessLicense(wuLianUploadData.getBusinessLicense());
                                data.setCities(wuLianUploadData.getCities());
                                data.setCounties(wuLianUploadData.getCounties());
                                data.setLat(wuLianUploadData.getLat());
                                data.setLon(wuLianUploadData.getLon());
                                data.setLoginId(wuLianUploadData.getLoginId());
                                data.setProvinces(wuLianUploadData.getProvinces());
                                Log.i("yunanhao", data.getLat() + "--" + data.getLon());

                                ArrayList<String> list = new Gson().fromJson(wuLianUploadData.getJsonImage(), new TypeToken<ArrayList<String>>() {
                                }.getType());
                                data.setImage(list);
                                int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                                try {
                                    List<OnetoOneConverter> arrayList = LitePal.where("areaNumber = ? and loginid = ?", wuLianUploadData.getAreaNumber(), loginid + "")
                                            .find(OnetoOneConverter.class);
                                    if (arrayList.size() == 0) {
                                        emitter.onError(new Throwable());
                                    } else {

                                        data.setConverter(arrayList);
                                        emitter.onNext(data);
                                    }
                                } catch (Exception e) {
                                    emitter.onError(e);
                                    LogUtils.e(e);
                                }
                            }
                            , BackpressureStrategy.ERROR)
                            .compose(RxSchedulers.io_main())
                            .subscribe(data -> {

                                pushEvent(MInitTwoCode.SaveOnetoOneMeterRunner, new Gson().toJson(data), getAdapterPosition());
                            }, throwable -> {
                                ToastManager.getInstance(getActivity()).show("未查找到该区域下保存的数据，请添加数据之后再上传");
                            }));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            delete.setOnClickListener(v -> {
                try {
                    WuLianUploadData wuLianUploadData = getContentByViewHolder(ViewHolder.this);
                    wuLianUploadData.deleteAsync().listen(rowsAffected -> {
                        getAdapter().getItems().remove(getAdapterPosition());
                        getAdapter().notifyItemRemoved(getAdapterPosition());
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
