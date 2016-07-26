package me.skyun.infinite.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.skyun.base.BaseActivity;
import me.skyun.infinite.MainActivity;
import me.skyun.infinite.R;
import me.skyun.infinite.global.GlobalPref;
import me.skyun.infinite.global.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private UserApi mUserApi = RetrofitUtils.getInstance().create(UserApi.class);
    private EditText mUsernameView = findViewByIdPre("mUsernameView", R.id.login_et_username);
    private EditText mPasswordView = findViewByIdPre("mPasswordView", R.id.login_et_password);
    private Button mSubmitBtn = findViewByIdPre("mSubmitBtn", R.id.login_btn_submit);

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
    }

    private void onSubmitClick() {
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();
        Call<User> call = mUserApi.login(username, password);
        RetrofitUtils.enqueueCall(this, call, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                GlobalPref.set(LoginActivity.this, GlobalPref.USER_ID, response.body().id);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
