//
//  RemoteDataListFragment.java
//  DoctorCommon
//
//  Created by Eden He on 2013-8-29
//  Copyright (c) 2013 Chunyu.mobi
//  All rights reserved
//
//

package me.skyun.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import me.skyun.infinite.R;
import me.skyun.utils.Utils;
import me.skyun.utils.ViewBinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseListFragment<ITEM, RESULT> extends Fragment implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, Callback<RESULT> {

    public enum Status {STATE_IDLE, STATE_LOADING, STATE_EMPTY, STATE_ERROR}

    private static final int DEFAULT_PAGE_COUNT = 20;
    private ListView mListView;
    private BaseArrayAdapter<ITEM> mAdapter;
    protected StatusManager mStatusManager;
    private boolean mAutoStartLoading = true;

    public ListView getListView() {
        return mListView;
    }

    protected BaseArrayAdapter<ITEM> createAdapter(Context context) {
        return new BaseArrayAdapter<>(context);
    }

    public BaseArrayAdapter<ITEM> getAdapter() {
        return mAdapter;
    }

    public StatusManager getStatusManager() {
        return mStatusManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity().getApplicationContext();
        mAdapter = createAdapter(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_list, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = (ListView) view.findViewById(R.id.base_list_view);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mStatusManager = new StatusManager(view);
        mStatusManager.setStatus(Status.STATE_IDLE);
        if (mAutoStartLoading) {
            loadingData(true);
        }
    }

    /**
     * 列表中每一项的点击响应
     *
     * @return
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    /**
     * 从Server获取数据，在回调中显示数据，并合理设置页面状态
     */
    public void loadingData(boolean isRefresh) {
        int start;
        if (isRefresh) {
            start = 0;
        } else {
            start = getDataCount();
        }
        Call<RESULT> req = getRequest(start);
        req.enqueue(this);
        if (getDataCount() == 0) {
            mStatusManager.setStatus(Status.STATE_LOADING);
        }
    }

    public abstract Call<RESULT> getRequest(int start);

    /**
     * mAdatper里的数据并不全是从服务器加载到的，也有本地处理得到的数据
     * 例如item之前显示的时间，所以需要计算一下data count
     */
    public int getDataCount() {
        return mAdapter.getCount();
    }

    protected int getPageCount() {
        return DEFAULT_PAGE_COUNT;
    }

    @Override
    public void onResponse(Call<RESULT> call, Response<RESULT> response) {
        List<ITEM> items;
        boolean isRefresh = "0".equals(call.request().url().queryParameter("start"));
        RESULT result = response.body();
        if (result instanceof List) {
            items = (List<ITEM>) result;
        } else {
            items = parseResult(result, isRefresh);
        }

        try {
            updateList(isRefresh, items);
            updateListStatus(true);
        } catch (Error e) {
            updateListStatus(false);
        }
    }

    @Override
    public void onFailure(Call<RESULT> call, Throwable t) {
        if (getActivity() != null && !Utils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "网络不可用", Toast.LENGTH_SHORT).show();
        }
        updateListStatus(false);
    }

    /**
     * 刷新列表展示状态
     */
    protected void updateListStatus(boolean isSuccess) {
        if (mAdapter.getCount() > 0) {
            mStatusManager.setStatus(Status.STATE_IDLE);
        } else {
            mStatusManager.setStatus(isSuccess ? Status.STATE_EMPTY : Status.STATE_ERROR);
        }
    }

    /**
     * 更新列表内容及页面状态
     *
     * @param isRefresh 是刷新还是加载更多
     */
    public void updateList(boolean isRefresh, List<ITEM> items) {
        if (items == null) {
            return;
        }

        if (isRefresh) {
            mAdapter.clear();
        }

        mAdapter.addAll(items);
        mAdapter.notifyDataSetChanged();
        //刷新后默认滚动到尾部
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(mListView.getCount());
            }
        });
    }

    /**
     * 从response中解析出List<ITEM>返回,同时也可以解析别的数据给成员变量
     *
     * @param RESULT
     * @return
     */
    protected List<ITEM> parseResult(RESULT RESULT, boolean isRefresh) {
        throw new Error("如果dat不是list,需要重载些函数,并从data中解析出list");
    }

    public void setStatus(Status status) {
        mStatusManager.setStatus(status);
    }

    public class StatusManager {

        private int mErrorIconRes = R.drawable.image_error;
        private int mEmptyIconRes = R.drawable.image_error;

        private ViewBinder mViewBinder = new ViewBinder();
        private View mContentView;
        private ListView mListView = mViewBinder.add("mListView", R.id.base_list_view);
        private ProgressBar mProgressBar = mViewBinder.add("mProgressBar", R.id.base_list_progress_loading);
        private ImageView mErrorView = mViewBinder.add("mErrorView", R.id.base_list_iv_error);
        private ImageView mEmptyView = mViewBinder.add("mEmptyView", R.id.base_list_iv_empty);
        private TextView mTipView = mViewBinder.add("mTipView", R.id.base_list_tv_tips);

        private String mLoadingMsg = "正在加载";
        private String mEmptyMsg = "暂无数据";
        private String mErrorMsg = "加载出错";

        private Status mStatus;

        public StatusManager(View contentView) {
            mContentView = contentView;
            mViewBinder.bind(this, mContentView);
        }

        public void setEmptyMsg(String emptyMsg) {
            mEmptyMsg = emptyMsg;
        }

        public Status getStatus() {
            return mStatus;
        }

        /**
         * 设置界面状态
         */
        public void setStatus(Status status) {
            if (mStatus == status) {
                return;
            }

            mListView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mErrorView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            mTipView.setVisibility(View.GONE);
            switch (status) {
                case STATE_IDLE:
                    mListView.setVisibility(View.VISIBLE);
                    break;
                case STATE_LOADING:
                    mProgressBar.setVisibility(View.VISIBLE);
                    mTipView.setVisibility(View.VISIBLE);
                    mTipView.setText(mLoadingMsg);
                    break;
                case STATE_ERROR:
                    mErrorView.setVisibility(View.VISIBLE);
                    mTipView.setVisibility(View.VISIBLE);
                    mTipView.setText(mErrorMsg);
                    mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadingData(true);
                        }
                    });
                    break;
                case STATE_EMPTY:
                    mEmptyView.setVisibility(View.VISIBLE);
                    mTipView.setVisibility(View.VISIBLE);
                    mTipView.setText(mEmptyMsg);
                    mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadingData(true);
                        }
                    });
                    break;
                default:
                    break;
            }
            mStatus = status;
        }
    }
}

