package me.skyun.infinite;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

import me.skyun.infinite.global.Conf;
import me.skyun.infinite.global.RetrofitUtils;
import me.skyun.widget.ImageViewEx;

/**
 * Created by linyun on 16/6/1.
 */

public class InfiniteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageViewEx.setImageHost(Conf.IMAGE_HOST);
        FlowManager.init(this);
        RetrofitUtils.init(getApplicationContext(), Conf.Host);
    }
}
