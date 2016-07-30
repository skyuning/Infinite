package me.skyun.infinite;

import android.os.Bundle;

import me.skyun.base.BaseActivity;
import me.skyun.infinite.user.User;
import me.skyun.widget.MapView;

public class MapActivity extends BaseActivity {

    private MapView mFloorView = mViewBinder.add("mFloorView", R.id.map_view_floor);
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mUser = User.fromPref(this);
    }
}
