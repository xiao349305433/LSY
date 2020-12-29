package wu.loushanyun.com.modulerepair.p.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import met.hx.com.base.base.multitype.ItemViewBinder;
import wu.loushanyun.com.modulerepair.R;
import wu.loushanyun.com.modulerepair.m.GetProductData;

public class PopChangJiaViewBinder extends ItemViewBinder<GetProductData.DataBean, PopChangJiaViewBinder.ViewHolder> {
    private OnPopShijianListener onPopShijianListener;

    public PopChangJiaViewBinder(OnPopShijianListener onPopShijianListener) {
        this.onPopShijianListener = onPopShijianListener;
    }

    public interface OnPopShijianListener {
        void onPopClick(int position, GetProductData.DataBean dataBean);
    }


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_repair_pop_shijian_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GetProductData.DataBean dataBean) {
        holder.textShijian.setText(dataBean.getSupplier());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearGongyong;
        private TextView textShijian;

        public ViewHolder(View view) {
            super(view);
            linearGongyong = (LinearLayout) view.findViewById(R.id.linear_gongyong);
            textShijian = (TextView) view.findViewById(R.id.text_shijian);
            textShijian.setOnClickListener(v -> {
                try {
                    onPopShijianListener.onPopClick(getAdapterPosition(), getContentByViewHolder(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
