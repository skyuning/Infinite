//package me.skyun.infinite;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
///**
// * Created by linyun on 16/5/31.
// */
//
//public class MainFragment extends Fragment {
//
//    private int mTabIndex;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mTabIndex = getArguments().getInt("tab_index");
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        TextView textView = new TextView(inflater.getContext());
//        textView.setText("" + mTabIndex);
//        return textView;
//    }
//}
