package wu.loushanyun.com.modulechiptest.p.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;

import met.hx.com.base.base.multitype.ItemViewBinder;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.m.HomeData;

public class HomeDataViewBinder extends ItemViewBinder<HomeData, HomeDataViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_chip_item_home, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull HomeData homeData) {
        holder.textItem.setText(homeData.getItemString());
    }

     class ViewHolder extends RecyclerView.ViewHolder {
         private CardView cardMain11;
         private TextView textItem;

        ViewHolder(View view) {
            super(view);
            cardMain11 = (CardView) view.findViewById(R.id.card_main_1_1);
            textItem = (TextView) view.findViewById(R.id.text_item);
            cardMain11.setOnClickListener(view1 -> {
                try {
                    HomeData homeData=getContentByViewHolder(this);
                    if(homeData.getHomeDataListener()!=null){
                        homeData.getHomeDataListener().onHomeData();
                    }else {
                        ARouter.getInstance().build(homeData.getJumpPathString()).navigation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
