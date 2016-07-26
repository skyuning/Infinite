package me.skyun.infinite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.skyun.base.BaseActivity;
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

    private ImageViewEx mAvatarView = findViewByIdPre("mAvatarView", R.id.main_iv_avatar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAvatarView.setPlaceHolderId(R.drawable.avatar_placeholder);

        RetrofitUtils.enqueueCall(this, mUserApi.getUser(), new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mAvatarView.setRemoteUrl(response.body().avatar);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAvatarClick();
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
