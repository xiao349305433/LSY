package wu.loushanyun.com.modulechiptest.p.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.librarybase.some_utils.TimeUtils;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.m.SelectallInfo;

public class OrderViewBinder extends ItemViewBinder<SelectallInfo.DataBean, OrderViewBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_chip_item_order, parent, false);
        return new OrderViewBinder.ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull SelectallInfo.DataBean item) {

        String orderStatus = "";
        if (item.getBussnessState() == 0) {
            orderStatus = "已下单";
        } else if (item.getBussnessState() == 1) {
            orderStatus = "已发货";
        } else if (item.getBussnessState() == 2) {
            orderStatus = "交易关闭";
        } else if (item.getBussnessState() == 3) {
            orderStatus = "交易成功";
        }
        holder.orderTime.setText("订单时间 ：" + TimeUtils.milliseconds2String(item.getOrderTime()));
        holder.orderNum.setText(Html.fromHtml("订单号 ： " + item.getOrderNumber() + "&nbsp;<font >(<b>" + item.getProductNumber() + "," + orderStatus + "</b>)" + "</font>"));
        holder.orderCompany.setText("服务企业 ：" + item.getSupplier());
        String mt1, mt2, mt3, mt4;
        if (item.getMt1() != 0) {
            mt1 = "LSY-MO1<font >(<b>" + item.getMt1() + "</b>)" + "</font>  ";
        } else {
            mt1 = "";
        }
        if (item.getMt2() != 0) {
            mt2 = "&nbsp;&nbsp;LSY-MO2<font >(<b>" + item.getMt2() + "</b>)" + "</font>  ";
        } else {
            mt2 = "";
        }
        if (item.getMt3() != 0) {
            mt3 = "&nbsp;&nbsp;LSY-MO3<font >(<b>" + item.getMt3() + "</b>)" + "</font>  ";
        } else {
            mt3 = "";
        }
        if (item.getMt4() != 0) {
            mt4 = "&nbsp;&nbsp;LSY-MO4<font >(<b>" + item.getMt4() + "</b>)" + "</font>  ";
        } else {
            mt4 = "";
        }
        holder.orderMo.setText(Html.fromHtml(mt1 + mt2 + mt3 + mt4));
        //   holder.orderMo.setText("LSY-MO1(" + item.getMt1() + ")、LSY-MO2(" + item.getMt2() + ")、LSY-MO3(" + item.getMt3() + ")、LSY-MO4(" + item.getMt4() + ")");
        switch (item.getInspectiontype()) {
            //质检状态（0：未质检，1：正在质检，2：已质检）
            case 0:
                holder.orderState.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.wzj));
                break;
            case 1:
                holder.orderState.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.zzzj));
                break;
            case 2:
                holder.orderState.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.yzj));
                break;
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout order_linear;
        private TextView orderNum;
        private TextView orderCompany;
        private TextView orderTime;
        private TextView orderMo;
        private ImageView orderState;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_linear = itemView.findViewById(R.id.order_linear);
            orderNum = (TextView) itemView.findViewById(R.id.order_num);
            orderCompany = (TextView) itemView.findViewById(R.id.order_company);
            orderTime = (TextView) itemView.findViewById(R.id.order_time);
            orderMo = (TextView) itemView.findViewById(R.id.order_mo);
            orderState = itemView.findViewById(R.id.order_state);
            order_linear.setOnClickListener(v -> {
                try {
                    ARouter.getInstance().build(K.OrderDetailActivity).withParcelable("order", getContentByViewHolder(this)).navigation();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }
    }
}
