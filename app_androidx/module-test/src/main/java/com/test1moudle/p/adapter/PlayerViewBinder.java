package com.test1moudle.p.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.test1moudle.R;

import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.base.base.player.NiceVideoPlayer;
import met.hx.com.base.base.player.TxVideoPlayerController;


/**
 * Created by huxu on 2017/12/1.
 */
public class PlayerViewBinder extends ItemViewBinder<Player, PlayerViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.m_test_item_video, parent, false);
        ViewHolder viewHolder=new ViewHolder(root);
        TxVideoPlayerController controller = new TxVideoPlayerController(getActivity());
        viewHolder.setController(controller);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Player player) {
        holder.bindData(player);
    }

   public static class ViewHolder extends RecyclerView.ViewHolder {
        public TxVideoPlayerController mController;
        public NiceVideoPlayer mVideoPlayer;

        ViewHolder(View itemView) {
            super(itemView);

            mVideoPlayer = itemView.findViewById(R.id.nice_video_player);
            // 将列表中的每个视频设置为默认16:9的比例
            ViewGroup.LayoutParams params = mVideoPlayer.getLayoutParams();
            params.width = itemView.getResources().getDisplayMetrics().widthPixels; // 宽度为屏幕宽度
            params.height = (int) (params.width * 9f / 16f);    // 高度为宽度的9/16
            mVideoPlayer.setLayoutParams(params);
        }

        public void setController(TxVideoPlayerController controller) {
            mController = controller;
            mVideoPlayer.setController(mController);
        }

        public void bindData(Player video) {
            mController.setTitle(video.getTitle());
            mController.setLenght(video.getLength());
            Glide.with(itemView.getContext())
                    .load(video.getImageUrl())
                    .placeholder(R.drawable.m_test_img_default)
                    .crossFade()
                    .into(mController.imageView());
            mVideoPlayer.setUp(video.getVideoUrl(), null);
        }
    }
}
