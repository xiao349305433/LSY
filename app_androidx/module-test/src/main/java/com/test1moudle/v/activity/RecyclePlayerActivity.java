package com.test1moudle.v.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.test1moudle.R;
import com.test1moudle.p.adapter.Player;
import com.test1moudle.p.adapter.PlayerViewBinder;
import com.test1moudle.p.util.DataUtil;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseRefreshActivity;
import met.hx.com.base.base.multitype.Items;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.player.NiceVideoPlayer;
import met.hx.com.base.base.player.NiceVideoPlayerManager;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;

/**
 * Created by huxu on 2017/12/1.
 */
@Route(path = C.RecyclePlayerActivity)
public class RecyclePlayerActivity extends BaseRefreshActivity {

    private MultiTypeAdapter adapter;
    private RecyclerView mRecyclerView;
    private PlayerViewBinder playerViewBinder;

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        playerViewBinder = new PlayerViewBinder();
        registerLifeCycle(playerViewBinder);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mHasTitle = true;
        ba.mTitleText = "播放列表";
    }

    @Override
    protected void initView() {
        super.initView();
        refreshView.hideEmptyData();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new MultiTypeAdapter();
        Items items = new Items();
        items.addAll(DataUtil.getVideoListData());
        adapter.register(Player.class, playerViewBinder);
        adapter.setItems(items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                NiceVideoPlayer niceVideoPlayer = ((PlayerViewBinder.ViewHolder) holder).mVideoPlayer;
                if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                    NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                }
            }
        });
    }

    @Override
    protected int initRefreshLayoutId() {
        return R.layout.m_test_activity_recycle_player;
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NiceVideoPlayerManager.instance().suspendNiceVideoPlayer();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer().isPaused()) {
            NiceVideoPlayerManager.instance().resumeNiceVideoPlayer();
        }
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }
}
