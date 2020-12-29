package wu.loushanyun.com.fivemoduleapp.p.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.LogUtils;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.libraryfive.m.YuanChuanSaveData;


public class SaveDataViewBinder extends ItemViewBinder<YuanChuanSaveData, SaveDataViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_five_item_save_data, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull YuanChuanSaveData yuanChuanSaveData) {
        LogUtils.i("数据=="+yuanChuanSaveData.toString());
        holder.sn.setText("物联SN："+yuanChuanSaveData.getSn());
        holder.time.setText("保存时间："+yuanChuanSaveData.getTime());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sn;
        private TextView time;
        private TextView upload;
        private TextView delete;

        ViewHolder(View view) {
            super(view);
            sn = (TextView) view.findViewById(R.id.sn);
            time = (TextView) view.findViewById(R.id.time);
            upload = (TextView) view.findViewById(R.id.upload);
            delete = (TextView) view.findViewById(R.id.delete);
            upload.setOnClickListener(v -> {
                try {
                    YuanChuanSaveData yuanChuanSaveData = getContentByViewHolder(ViewHolder.this);
                    ARouter.getInstance().build(K.LocationActivity)
                            .withLong("yuanChuanSaveDataId",yuanChuanSaveData.getBaseObjId())
                            .withString("oldSn", "").navigation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            delete.setOnClickListener(v -> {
                try {
                    YuanChuanSaveData yuanChuanSaveData = getContentByViewHolder(ViewHolder.this);
                    yuanChuanSaveData.deleteAsync().listen(rowsAffected -> {
                        getAdapter().getItems().remove(getAdapterPosition());
                        getAdapter().notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }
    }
}
