package com.xcm91.relation.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xcm91.relation.util.ArraysUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fml
 *         created at 2016/6/20 13:36
 *         description：fragment适配adapter
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    //fragment 集合
    private List<Fragment> mFragmentList = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mFragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        if (ArraysUtils.isNotEmpty(mFragmentList)) {
            return mFragmentList.size();
        }
        return 0;
    }
}
