package me.skyun.infinite.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import me.skyun.base.BaseActivity;
import me.skyun.infinite.MapActivity;
import me.skyun.infinite.R;
import me.skyun.infinite.global.RetrofitUtils;
import me.skyun.infinite.global.SimpleResponse;
import me.skyun.infinite.user.AvatarsPopup;
import me.skyun.infinite.user.LoginActivity;
import me.skyun.infinite.user.User;
import me.skyun.infinite.user.UserApi;
import me.skyun.widget.ImageViewEx;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by linyun on 16/7/25.
 */
public class MainActivity extends BaseActivity {

    private UserApi mUserApi = RetrofitUtils.getInstance().create(UserApi.class);
    private ImageViewEx mAvatarView = mViewBinder.add("mAvatarView", R.id.main_iv_avatar);
    private ImageView mMapEntry = mViewBinder.add("mMapEntry", R.id.main_iv_map);
    private Button mLogoutBtn = mViewBinder.add("mLogoutBtn", R.id.main_btn_logout);
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = User.fromPref(this);
        mAvatarView.setRemoteUrl(mUser.avatar);

        mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAvatarClick();
            }
        });

        mMapEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logout(v.getContext());
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        });
    }

    private void onAvatarClick() {
        AvatarsPopup avatarsPopup = new AvatarsPopup(this);
        avatarsPopup.setOnAvatarClickListener(new AvatarsPopup.OnAvatarClickListener() {
            @Override
            public void onAvatarClick(String avatarUrl) {
                mAvatarView.setRemoteUrl(avatarUrl);
                setAvatar(avatarUrl);
            }
        });
        avatarsPopup.showAsDropDown(mAvatarView, 0, 20);
    }

    private void setAvatar(String avatarUrl) {
        RetrofitUtils.enqueueCall(this, mUserApi.setAvatar(avatarUrl), new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
            }
        });
    }
}
