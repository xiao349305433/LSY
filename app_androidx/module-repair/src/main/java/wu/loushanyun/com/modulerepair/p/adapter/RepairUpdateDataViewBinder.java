package wu.loushanyun.com.modulerepair.p.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.init.LSY2InitTypeCode;

import org.litepal.LitePal;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.libraryfive.m.RepairUpdateData;
import wu.loushanyun.com.modulerepair.R;

public class RepairUpdateDataViewBinder extends ItemViewBinder<RepairUpdateData, RepairUpdateDataViewBinder.ViewHolder> {
    private Context context;
    private MDDialog.Builder mdDialogBuilder;
    private String[] messages = new String[1];

    public RepairUpdateDataViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_repair_item_update_data, parent, false);
        mdDialogBuilder = new MDDialog.Builder(context).setTitle("温馨提示");
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull RepairUpdateData repairUpdateData) {
        holder.textChanpinxingshi.setText("产品形式： " + repairUpdateData.getProductName());
        holder.newSn.setText("新SN设备：" + repairUpdateData.getNewSn());
        holder.oldSn.setText("旧SN设备：" + repairUpdateData.getOldSn());
        holder.time.setText("保存时间：" + repairUpdateData.getTime());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linear;
        private TextView textChanpinxingshi;
        private TextView oldSn;
        private TextView newSn;
        private TextView time;
        private TextView upload;
        private TextView delete;

        ViewHolder(View view) {
            super(view);
            linear = (LinearLayout) view.findViewById(R.id.linear);
            textChanpinxingshi = (TextView) view.findViewById(R.id.text_chanpinxingshi);
            oldSn = (TextView) view.findViewById(R.id.oldSn);
            newSn = (TextView) view.findViewById(R.id.newSn);
            time = (TextView) view.findViewById(R.id.time);
            upload = (TextView) view.findViewById(R.id.upload);
            delete = (TextView) view.findViewById(R.id.delete);

            upload.setOnClickListener(view1 -> {
                try {
                    RepairUpdateData repairUpdateData = getContentByViewHolder(this);
                    if (repairUpdateData.getProductName().equals("远传表号接入")) {
                        Gson gson = new Gson();
                        ARouter.getInstance().build(K.LocationActivity)
                                .withString("oldSn", repairUpdateData.getOldSn())
                                .withLong("repairUpdateDataId", repairUpdateData.getBaseObjId())
                                .navigation();
                    } else if (repairUpdateData.getProductName().equals("远传物联网端")) {
                        ARouter.getInstance().build(K.LSY2LocationDetailActivity)
                                .withString("oldSn", repairUpdateData.getOldSn())
                                .withLong("deleteId", repairUpdateData.getBaseObjId())
                                .withInt("jumpType", LSY2InitTypeCode.TypeFromReplace)
                                .withString("onetoOneConverterJson", repairUpdateData.getWuLianWangDataJson())
                                .navigation();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            delete.setOnClickListener(view1 -> {
                try {
                    RepairUpdateData repairUpdateData = getContentByViewHolder(this);
                    messages[0] = "确定删除旧设备：" + repairUpdateData.getOldSn() + "的更换数据？";
                    mdDialogBuilder.setMessages(messages).setPositiveButton(view2 -> {
                        LitePal.deleteAsync(RepairUpdateData.class, repairUpdateData.getBaseObjId()).listen(new UpdateOrDeleteCallback() {
                            @Override
                            public void onFinish(int rowsAffected) {
                                ToastUtils.showShort("删除成功");
                                getAdapter().getItems().remove(repairUpdateData);
                                getAdapter().notifyDataSetChanged();
                            }
                        });
                    }).create().show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
