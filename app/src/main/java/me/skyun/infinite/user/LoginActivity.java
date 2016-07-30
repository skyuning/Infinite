package me.skyun.infinite.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.skyun.base.BaseActivity;
import me.skyun.infinite.R;
import me.skyun.infinite.global.RetrofitUtils;
import me.skyun.infinite.main.MainActivity;
import me.skyun.test.HostActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private UserApi mUserApi = RetrofitUtils.getInstance().create(UserApi.class);
    private EditText mUsernameView = mViewBinder.add("mUsernameView", R.id.login_et_username);
    private EditText mPasswordView = mViewBinder.add("mPasswordView", R.id.login_et_password);
    private Button mSubmitBtn = mViewBinder.add("mSubmitBtn", R.id.login_btn_submit);
    private View mHostView = mViewBinder.add("mHostView", R.id.host);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClick();
            }
        });

        mHostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HostActivity.class));
            }
        });
    }

    private void onSubmitClick() {
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();
        Call<User> call = mUserApi.login(username, password);
        RetrofitUtils.enqueueCall(this, call, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                response.body().toPref(LoginActivity.this);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
