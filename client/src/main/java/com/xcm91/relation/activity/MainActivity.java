package com.xcm91.relation.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.xcm91.relation.R;
import com.xcm91.relation.adapter.FragmentAdapter;
import com.xcm91.relation.common.BaseActivity;
import com.xcm91.relation.fragment.FourFragment;
import com.xcm91.relation.fragment.OneFragment;
import com.xcm91.relation.fragment.ThreeFragment;
import com.xcm91.relation.fragment.TwoFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FRAGMENT_TAGS = "fragmentTags";
    private static final String CURR_INDEX = "currIndex";
    private static int currIndex = 0;

    private RadioGroup group;
    //fragment
    private Fragment mFragmentOne, mFragmentTwo, mFragmentThree, mFragmentFour;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> fragmentTags;
    private FragmentAdapter homePageFragmentAdapter;
    private ViewPager viewpager_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUserDefinedTitle = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            initView();
            initData();
        } else {
            initFromSavedInstantsState(savedInstanceState);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_INDEX, currIndex);
        outState.putStringArrayList(FRAGMENT_TAGS, fragmentTags);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initFromSavedInstantsState(savedInstanceState);
    }

    private void initFromSavedInstantsState(Bundle savedInstanceState) {
        currIndex = savedInstanceState.getInt(CURR_INDEX);
        fragmentTags = savedInstanceState.getStringArrayList(FRAGMENT_TAGS);

    }

    private void initData() {
        currIndex = 0;
        fragmentTags = new ArrayList<>(Arrays.asList("OneFragment", "TwoFragment", "ThreeFragment", "FourFragment"));

        mFragmentOne = new OneFragment();
        mFragmentTwo = new TwoFragment();
        mFragmentThree = new ThreeFragment();
        mFragmentFour = new FourFragment();
        mFragmentList.add(mFragmentOne);
        mFragmentList.add(mFragmentTwo);
        mFragmentList.add(mFragmentThree);
        mFragmentList.add(mFragmentFour);


        homePageFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);
        viewpager_container.setOffscreenPageLimit(3);
        viewpager_container.setAdapter(homePageFragmentAdapter);
    }

    private void initView() {
        group = (RadioGroup) findViewById(R.id.group);
        viewpager_container = (ViewPager) findViewById(R.id.viewpager_container);
        viewpager_container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        group.check(R.id.foot_bar_home);
                        break;
                    case 1:
                        group.check(R.id.foot_bar_im);
                        break;
                    case 2:
                        group.check(R.id.foot_bar_interest);
                        break;
                    case 3:
                        group.check(R.id.main_footbar_user);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.foot_bar_home:
                        viewpager_container.setCurrentItem(0, false);
                        break;
                    case R.id.foot_bar_im:
                        viewpager_container.setCurrentItem(1, false);
                        break;
                    case R.id.foot_bar_interest:
                        viewpager_container.setCurrentItem(2, false);
                        break;
                    case R.id.main_footbar_user:
                        viewpager_container.setCurrentItem(3, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
