package me.skyun.infinite.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import me.skyun.IDL;
import me.skyun.base.BaseListFragment;
import me.skyun.infinite.R;
import me.skyun.infinite.global.RetrofitUtils;
import me.skyun.infinite.user.UserApi;
import retrofit2.Call;

/**
 * Created by linyun on 16/7/30.
 */
public class GoodsFragment extends BaseListFragment<IDL.Goods, List<IDL.Goods>> {

    private UserApi mUserApi = RetrofitUtils.getInstance().create(UserApi.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme_Fix);
        getAdapter().addViewHolder(GoodsViewHolder.class);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("我的物品");
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);

        return dialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getListView().getDivider() != null) {
            getListView().getDivider().setAlpha(30);
        }
        getListView().setDividerHeight(1);
    }

    @Override
    public Call<List<IDL.Goods>> getRequest(int start) {
        return mUserApi.getGoods();
    }
}
