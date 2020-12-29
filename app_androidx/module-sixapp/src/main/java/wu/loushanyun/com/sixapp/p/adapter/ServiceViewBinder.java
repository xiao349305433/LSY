package wu.loushanyun.com.sixapp.p.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.entity.SensoroDevice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.m.GetComInfo;

public class ServiceViewBinder extends ItemViewBinder<GetComInfo.DatasBean, ServiceViewBinder.ViewHolder> {

    private OnServiceListener onServiceListener;
    private String keyWord;
    private Context context;
    public void setKeyWord(String keyWrold) {
        this.keyWord = keyWrold;
        if(getAdapter()!=null){
            getAdapter().notifyDataSetChanged();
        }

    }


    public ServiceViewBinder(OnServiceListener onServiceListener) {
        this.onServiceListener = onServiceListener;
    }

    public interface OnServiceListener {
        void onService();
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root=inflater.inflate(R.layout.m_six_item_service,parent, false);
        return new ViewHolder(root);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GetComInfo.DatasBean datasBean) {
        String str = datasBean.getBusinessName();
        holder.textView.setText(datasBean.getBusinessName());

        holder.setVisibility(false);
        if (XHStringUtil.isEmpty(keyWord, true)) {
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                    str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textView.setText(datasBean.getBusinessName());
            holder.setVisibility(true);
        } else {
            Pattern p = Pattern.compile(keyWord);
            Matcher m = p.matcher(str);
            SpannableString spannableString = new SpannableString(str);
            while (m.find()) {
                if (str.contains(m.group())) {
                    spannableString.setSpan(
                            new ForegroundColorSpan(0xffec8b44), m.start(),
                            m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.textView.setText(datasBean.getBusinessName());
                holder.setVisibility(true);
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private TextView textView;

        ViewHolder(View view) {
            super(view);
            linearLayout=view.findViewById(R.id.search_item_layout);
            textView = (TextView) view.findViewById(R.id.search_item_tv);
            linearLayout.setOnClickListener(v -> {
                try {
                    onServiceListener.onService();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }
}
