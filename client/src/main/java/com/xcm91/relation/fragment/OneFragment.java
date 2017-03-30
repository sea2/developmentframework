package com.xcm91.relation.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xcm91.relation.R;
import com.xcm91.relation.activity.Main2Activity;
import com.xcm91.relation.common.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author lhy
 */
public class OneFragment extends BaseFragment {
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private PullToRefreshListView pullToRefreshListView;
    private static final int UPDATE_NEWS = 1; // 下拉刷新
    private static final int MORE_NEWS = 2; // 上拉加载更多

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_one_layout, container, false);
            Log.e(TAG, "oneFragment--onCreateView");
            isPrepared = true;
            lazyLoad();

            pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pullToRefreshListView);
            pullToRefreshListView.setPullLoadEnabled(false);
            pullToRefreshListView.setScrollLoadEnabled(true);

            ListView listview = pullToRefreshListView.getRefreshableView();
            ArrayList<HashMap<String, Object>> imagelist = new ArrayList<HashMap<String, Object>>();
            // 使用HashMap将图片添加到一个数组中，注意一定要是HashMap<String,Object>类型的，因为装到map中的图片要是资源ID，而不是图片本身
            // 如果是用findViewById(R.drawable.image)这样把真正的图片取出来了，放到map中是无法正常显示的
            for (int i = 0; i < 8; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("name", "呵呵笑");
                imagelist.add(map);
            }
            SimpleAdapter simplead = new SimpleAdapter(getActivity(), imagelist, R.layout.item_toast, new String[]{"name"}, new int[]{R.id.message});
            listview.setAdapter(simplead);
        }
        return view;
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        startAc(Main2Activity.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
    }


    private void initListener() {
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute(UPDATE_NEWS);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute(MORE_NEWS);
            }
        });
    }


    // 实际的异步处理位置，这里去做刷新工作
    private class GetDataTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected void onPreExecute() {
        }

        // 先触发这里
        @Override
        protected Integer doInBackground(Integer... params) {
            switch (params[0]) {
                // 刷新
                case UPDATE_NEWS:
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return UPDATE_NEWS;

                // 更多
                case MORE_NEWS:
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return MORE_NEWS;
                default:
                    return 0;
            }
        }

        // doInBackground完成后到这里
        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case UPDATE_NEWS:
                    // 刷新结束

                    pullToRefreshListView.onPullDownRefreshComplete();
                    pullToRefreshListView.onPullUpRefreshComplete();

                    break;

                case MORE_NEWS:
                    // 获取更多结束

                    pullToRefreshListView.onPullDownRefreshComplete();
                    pullToRefreshListView.onPullUpRefreshComplete();
                    pullToRefreshListView.setHasMoreData(false);

                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
    }

}
