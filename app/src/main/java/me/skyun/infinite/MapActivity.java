package me.skyun.infinite;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.skyun.base.BaseActivity;
import me.skyun.infinite.main.GoodsFragment;
import me.skyun.infinite.user.User;
import me.skyun.widget.MapItem;
import me.skyun.widget.MapView;

public class MapActivity extends BaseActivity {

    private MapView mFloorView = mViewBinder.add("mFloorView", R.id.map_view_floor);
    private Button mEditBtn = mViewBinder.add("mEditBtn", R.id.map_btn_edit);
    private Button mMyGoodsBtn = mViewBinder.add("mMyGoodsBtn", R.id.map_btn_my_goods);
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mUser = User.fromPref(this);

        mFloorView.addItems(
                new MapItem(R.drawable.bothy, new Rect(0, 0, 300, 300)),
                new MapItem(R.drawable.bothy, new Rect(500, 600, 1000, 1000))
        );

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloorView.setEditingMap(!mFloorView.isEditingMap());
                if (mFloorView.isEditingMap()) {
                    mEditBtn.setText("取消编辑");
                } else {
                    mEditBtn.setText("编辑");
                }
            }
        });

        mMyGoodsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsFragment fragment = new GoodsFragment();
                fragment.show(getFragmentManager(), fragment.getClass().getName());
            }
        });
    }
}
