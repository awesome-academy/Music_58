package com.example.music_58.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

public abstract class BaseLoadMoreActivity extends AppCompatActivity {
    private static final int NUMBER_1 = 1;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLinearLayoutManager;
    protected boolean mIsScrolling = false;
    private int mTotalItem;
    private int mScrollOutItem;

    protected void setLoadMore() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalItem = mLinearLayoutManager.getItemCount();
                mScrollOutItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (mScrollOutItem == mTotalItem - NUMBER_1) {
                    loadMoreData();
                }
            }
        });
    }

    public abstract void loadMoreData();

    public abstract void initViewLoadMore();
}
