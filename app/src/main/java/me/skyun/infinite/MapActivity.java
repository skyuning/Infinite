package me.skyun.infinite;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import me.skyun.IDL;
import me.skyun.base.BaseActivity;
import me.skyun.infinite.main.GoodsFragment;
import me.skyun.infinite.user.User;
import me.skyun.widget.MapItem;
import me.skyun.widget.MapView;

public class MapActivity extends BaseActivity {

    private MapView mMapView = mViewBinder.add("mMapView", R.id.map_view);
    private Button mEditBtn = mViewBinder.add("mEditBtn", R.id.map_btn_edit);
    private Button mMyGoodsBtn = mViewBinder.add("mMyGoodsBtn", R.id.map_btn_my_goods);
    private EditText mScaleView = mViewBinder.add("mScaleView", R.id.map_et_scale);
    private Button mScaleBtn = mViewBinder.add("mScaleBtn", R.id.map_btn_scale);
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mUser = User.fromPref(this);

        mMapView.addItems(
                new MapItem(R.drawable.bothy, new Rect(0, 0, 300, 300)),
                new MapItem(R.drawable.bothy, new Rect(500, 600, 1000, 1000))
        );

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.setEditingMap(!mMapView.isEditingMap());
                if (mMapView.isEditingMap()) {
                    mEditBtn.setText("取消编辑");
                } else {
                    mEditBtn.setText("编辑");
                }
            }
        });

        mMyGoodsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GoodsFragment fragment = new GoodsFragment();
                fragment.show(getFragmentManager(), fragment.getClass().getName());
                fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        IDL.Goods goods = fragment.getAdapter().getItem(position);
                        onGoodsClick(goods);
                    }
                });
            }
        });

        mScaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float scale = Float.valueOf(mScaleView.getText().toString());
                mMapView.setMapScale(scale, mMapView.getWidth(), mMapView.getHeight());
            }
        });
    }

    private void onGoodsClick(IDL.Goods goods) {
        mMapView.setCurItem(new MapItem(R.drawable.bothy, 300, 100));
    }
}
