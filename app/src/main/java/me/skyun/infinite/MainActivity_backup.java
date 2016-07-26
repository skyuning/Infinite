//package me.skyun.infinite;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.Checkable;
//import android.widget.RadioGroup;
//
//public class MainActivity extends AppCompatActivity implements
//        RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
//
//    private ViewPager mViewPager;
//    private RadioGroup mTabLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
//        mTabLayout = (RadioGroup) findViewById(R.id.main_tab_layout);
//
//        mViewPager.setAdapter(new _PagerAdapter(getSupportFragmentManager()));
//        mViewPager.addOnPageChangeListener(this);
//        mTabLayout.setOnCheckedChangeListener(this);
//    }
//
//    private class _PagerAdapter extends FragmentPagerAdapter {
//
//        public _PagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            Bundle args = new Bundle();
//            args.putInt(Const.EXTRA_INDEX, position);
//            Fragment fragment = new MainFragment();
//            fragment.setArguments(args);
//            switch (position) {
//                case 0:
//                    break;
//                case 1:
//                    break;
//                case 2:
//                    break;
//                case 3:
//                    fragment = new UserFragment();
//                    break;
//                default:
//                    break;
//            }
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 4;
//        }
//    }
//
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        int index = group.indexOfChild(group.findViewById(checkedId));
//        mViewPager.setCurrentItem(index);
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        Checkable checkable = (Checkable) mTabLayout.getChildAt(position);
//        checkable.setChecked(true);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//    }
//}
