package me.skyun.base;

import android.support.v7.app.AppCompatActivity;

import me.skyun.utils.ViewBinder;

/**
 * Created by linyun on 16/7/25.
 */
public class BaseActivity extends AppCompatActivity {

    protected ViewBinder mViewBinder = new ViewBinder();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mViewBinder.bind(this, getWindow().getDecorView());
    }
}
