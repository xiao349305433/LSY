package wu.loushanyun.com.module_initthree.p.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

import com.flyco.roundview.RoundRelativeLayout;

import met.hx.com.base.base.basebinder.BaseItemViewBinder;
import met.hx.com.base.base.basebinder.ChildContentHolder;
import wu.loushanyun.com.module_initthree.R;
import wu.loushanyun.com.module_initthree.m.ThirdHandData;


public class ThirdHandViewBinder extends BaseItemViewBinder<ThirdHandData,ThirdHandViewBinder.HandHolder > {
    private Activity context;
    private OnChongZhiListener onChongZhiListener;
    private OnDuBiaoListener onDuBiaoListener;
    private OnLouChaoListener onLouChaoListener;
    private OnStartTextListener startTextListener;
    private OnEndTextListener endTextListener;
    private OnSwtichListener onSwtichListener;
    public ThirdHandViewBinder(Activity context) {
        super(context);
        this.context = context;
    }

    public interface OnChongZhiListener {
        void onChongZhi(ThirdHandData thirdHandData);
    }
    public void setChongZhiListener(OnChongZhiListener onChongZhiListener) {
        this.onChongZhiListener = onChongZhiListener;
    }

    public interface OnDuBiaoListener {
        void onDuBiao(ThirdHandData thirdHandData);
    }
    public void setDuBiaoListener(OnDuBiaoListener onDuBiaoListener) {
        this.onDuBiaoListener = onDuBiaoListener;
    }

    public interface OnLouChaoListener {
        void onLouChao(ThirdHandData thirdHandData);
    }
    public void setLouChaoListener(OnLouChaoListener louChaoListener) {
        this.onLouChaoListener = louChaoListener;
    }


    public interface OnStartTextListener {
        void onTextChanged(String str);
    }


    public interface OnSwtichListener {
        void IsOpen(boolean IsOpen);
    }
    public void setSwtichListener(OnSwtichListener swtichListener) {
        this.onSwtichListener = swtichListener;
    }

    //设置自定义接口成员变量
    public void setOnStartTextListener(OnStartTextListener onStartTextListener){
        this.startTextListener=onStartTextListener;
    }

    public interface OnEndTextListener {
        void onTextChanged(String str);
    }
    //设置自定义接口成员变量
    public void setOnEndTextListener(OnEndTextListener onEndTextListener){
        this.endTextListener=onEndTextListener;
    }


    @Override
    protected ChildContentHolder onCreateContentChildHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        View root = inflater.inflate(R.layout.m_chip_view_thirdhand, parent, false);

        return new HandHolder(root);
    }

    @Override
    protected void onBindContentChildHolder(@NonNull HandHolder holder, @NonNull ThirdHandData thirdHandData) {
                holder.viewAccept.setText(thirdHandData.getAccept());
                holder.viewDisAccept.setText(thirdHandData.getDisAccept());
                holder.viewMeterMiss.setText(thirdHandData.getMeterMiss());

        holder.viewRoundChongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChongZhiListener.onChongZhi(thirdHandData);
                holder.viewMeterReadLayout.setEnabled(true);
                holder.viewMeterReadLayout.getDelegate().setBackgroundColor(getActivity().getResources().getColor(R.color.l_five_Q));

            }
        });
        holder.viewMeterReadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDuBiaoListener.onDuBiao(thirdHandData);
                holder.viewMeterReadLayout.getDelegate().setBackgroundColor(getActivity().getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
            }
        });
        holder.viewMeterReadMissLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLouChaoListener.onLouChao(thirdHandData);
            }
        });

        holder.switchCheckedId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onSwtichListener.IsOpen(buttonView.isChecked());
            }
        });

    }


     class EndTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            endTextListener.onTextChanged(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable != null) {
                endTextListener.onTextChanged(editable.toString());
            }
        }
    }

     class StartTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             startTextListener.onTextChanged(charSequence.toString());

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable != null) {
                startTextListener.onTextChanged(editable.toString());
            }
        }
    }


     class HandHolder extends ChildContentHolder {
        private RoundRelativeLayout num;
        private EditText viewNumStart;
        private View divide;
        private EditText viewNumEnd;
        private RoundRelativeLayout viewRoundChongzhi;
        private RoundRelativeLayout viewMeterReadLayout;
        private RoundRelativeLayout viewMeterReadMissLayout;
        private TextView viewAccept;
        private TextView viewDisAccept;
        private TextView viewMeterMiss;
        private StartTextWatcher startTextWatcher;
        private EndTextWatcher endTextWatcher;
         private SwitchCompat switchCheckedId;




        public HandHolder(View itemView) {
            super(itemView);
            num = (RoundRelativeLayout)itemView.findViewById(R.id.num);
            viewNumStart = (EditText) itemView.findViewById(R.id.view_num_start);
            divide = (View) itemView.findViewById(R.id.divide);
            viewNumEnd = (EditText)itemView. findViewById(R.id.view_num_end);
            viewRoundChongzhi = (RoundRelativeLayout) itemView.findViewById(R.id.view_round_chongzhi);
            viewMeterReadLayout = (RoundRelativeLayout) itemView.findViewById(R.id.view_meter_read_layout);
            viewMeterReadMissLayout = (RoundRelativeLayout) itemView.findViewById(R.id.view_meter_read_miss_layout);
            viewAccept = (TextView) itemView.findViewById(R.id.view_accept);
            viewDisAccept = (TextView) itemView.findViewById(R.id.view_dis_accept);
            viewMeterMiss = (TextView) itemView.findViewById(R.id.view_meter_miss);
            switchCheckedId=itemView.findViewById(R.id.view_switch_checked_id);
                startTextWatcher=new StartTextWatcher();
                endTextWatcher=new EndTextWatcher();
//            startTextWatcher=new StartTextWatcher(viewNumStart);
//            endTextWatcher=new EndTextWatcher(viewNumEnd);



            viewNumStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        viewNumStart.addTextChangedListener(startTextWatcher);
                    }else {
                        viewNumStart.removeTextChangedListener(startTextWatcher);
                    }
                }
            });
            viewNumEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        viewNumEnd.addTextChangedListener(endTextWatcher);
                    }else {
                        viewNumEnd.removeTextChangedListener(endTextWatcher);
                    }
                }
            });

        }
    }
}
