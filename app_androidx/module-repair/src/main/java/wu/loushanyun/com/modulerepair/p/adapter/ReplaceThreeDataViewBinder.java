package wu.loushanyun.com.modulerepair.p.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.litepal.LitePal;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.modulerepair.R;
import wu.loushanyun.com.modulerepair.init.MRepairCode;
import wu.loushanyun.com.modulerepair.m.InsideModifyThirdMeterInfo;
import wu.loushanyun.com.modulerepair.p.runner.MRepairModifiyThirdMeterRunner;

public class ReplaceThreeDataViewBinder extends ItemViewBinder<InsideModifyThirdMeterInfo, ReplaceThreeDataViewBinder.ViewHolder> {
    private Context context;
    private MDDialog.Builder mdDialogBuilder;
    private String[] messages = new String[1];

    public ReplaceThreeDataViewBinder(Context context) {
        this.context = context;
    }

    @Override
    protected void initEventRunner() {
        registerEventRunner(MRepairCode.MRepairModifiyThirdMeterRunner, new MRepairModifiyThirdMeterRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MRepairCode.MRepairModifiyThirdMeterRunner) {
            if (event.isSuccess()) {
                int codeResult = (int) event.getReturnParamAtIndex(0);
                ToastUtils.showShort(String.valueOf(event.getReturnParamAtIndex(1)));
                if (codeResult == 0) {
                    int position = (int) event.getParamAtIndex(1);
                    InsideModifyThirdMeterInfo insideModifyThirdMeterInfo = (InsideModifyThirdMeterInfo) getAdapter().getItems().get(position);
//                    LitePal.deleteAsync(InsideModifyThirdMeterInfo.class, insideModifyThirdMeterInfo.getBaseObjId()).listen(new UpdateOrDeleteCallback() {
//                        @Override
//                        public void onFinish(int rowsAffected) {
//                            getAdapter().getItems().remove(insideModifyThirdMeterInfo);
//                            getAdapter().notifyItemRemoved(position);
//                        }
//                    });
                } else {
                    ToastUtils.showLong(String.valueOf(event.getReturnParamAtIndex(1)));
                }
            }
        }
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_repair_item_replace_three_data, parent, false);
        mdDialogBuilder = new MDDialog.Builder(context).setTitle("温馨提示");
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull InsideModifyThirdMeterInfo insideModifyThirdMeterInfo) {
        holder.textSn.setText("SN号： " + insideModifyThirdMeterInfo.getSn());
        holder.biaohao.setText("表号：" + insideModifyThirdMeterInfo.getMeterNumber());
        holder.newId.setText("新设备ID：" + insideModifyThirdMeterInfo.getNewMeterId());
        holder.oldId.setText("旧设备ID：" + insideModifyThirdMeterInfo.getOldMeterId());
        holder.time.setText("保存时间：" + insideModifyThirdMeterInfo.getTime());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linear;
        private TextView textSn;
        private TextView biaohao;
        private TextView oldId;
        private TextView newId;
        private TextView time;
        private TextView upload;
        private TextView delete;

        ViewHolder(View view) {
            super(view);
            linear = (LinearLayout) view.findViewById(R.id.linear);
            textSn = (TextView) view.findViewById(R.id.text_sn);
            biaohao = (TextView) view.findViewById(R.id.biaohao);
            oldId = (TextView) view.findViewById(R.id.oldId);
            newId = (TextView) view.findViewById(R.id.newId);
            time = (TextView) view.findViewById(R.id.time);
            upload = (TextView) view.findViewById(R.id.upload);
            delete = (TextView) view.findViewById(R.id.delete);


            upload.setOnClickListener(view1 -> {
                try {
                    InsideModifyThirdMeterInfo insideModifyThirdMeterInfo = getContentByViewHolder(this);
                    pushEvent(MRepairCode.MRepairModifiyThirdMeterRunner, new Gson().toJson(insideModifyThirdMeterInfo), getAdapterPosition());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            delete.setOnClickListener(view1 -> {
                try {
                    InsideModifyThirdMeterInfo insideModifyThirdMeterInfo = getContentByViewHolder(this);
                    messages[0] = "确定删除设备：" + insideModifyThirdMeterInfo.getSn() + "下" + insideModifyThirdMeterInfo.getMeterNumber() + "表的更换数据？";
                    mdDialogBuilder.setMessages(messages).setPositiveButton(view2 -> {
                        LitePal.deleteAsync(InsideModifyThirdMeterInfo.class, insideModifyThirdMeterInfo.getBaseObjId()).listen(new UpdateOrDeleteCallback() {
                            @Override
                            public void onFinish(int rowsAffected) {
                                ToastUtils.showShort("删除成功");
                                getAdapter().getItems().remove(insideModifyThirdMeterInfo);
                                getAdapter().notifyItemRemoved(getAdapterPosition());
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
