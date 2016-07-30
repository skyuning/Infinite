package me.skyun.infinite.main;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import me.skyun.IDL;
import me.skyun.base.BaseListFragment;
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
        getAdapter().addViewHolder(GoodsViewHolder.class);
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
