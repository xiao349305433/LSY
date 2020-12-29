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

public class PopShijianViewBinder extends ItemViewBinder<String, PopShijianViewBinder.ViewHolder> {
    private OnPopShijianListener onPopShijianListener;

    public PopShijianViewBinder(OnPopShijianListener onPopShijianListener) {
        this.onPopShijianListener = onPopShijianListener;
    }

    public interface OnPopShijianListener {
        void onPopClick(int position, String item);
    }


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_repair_pop_shijian_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String item) {
        holder.textShijian.setText(item);
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
