package me.skyun.test;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import me.skyun.base.BaseActivity;
import me.skyun.infinite.R;
import me.skyun.infinite.global.Conf;
import me.skyun.infinite.global.GlobalPref;
import me.skyun.infinite.global.RetrofitUtils;

public class HostActivity extends BaseActivity {

    private EditText mHostView = findViewByIdPre("mHostView", R.id.host_et_host);
    private Button mOkBtn = findViewByIdPre("mOkBtn", R.id.host_btn_ok);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        String host = GlobalPref.get(this, "host");
        if (TextUtils.isEmpty(host)) {
            host = Conf.sHost;
        }
        mHostView.setText(host);

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conf.sHost = mHostView.getText().toString();
                RetrofitUtils.init(v.getContext(), Conf.sHost);
            }
        });
    }
}
