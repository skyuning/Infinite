package me.skyun.infinite.main;

import android.content.Context;
import android.widget.TextView;

import me.skyun.IDL;
import me.skyun.base.ViewHolder;
import me.skyun.infinite.R;

/**
 * Created by linyun on 16/7/30.
 */
public class GoodsViewHolder extends ViewHolder<IDL.Goods> {

    private TextView mNameView = mViewBinder.add("mNameView", R.id.goods_tv_name);
    private TextView mAmountView = mViewBinder.add("mAmountView", R.id.goods_tv_amount);
    private TextView mTypeView = mViewBinder.add("mTypeView", R.id.goods_tv_type);

    @Override
    public int getLayoutId(IDL.Goods var1) {
        return R.layout.item_main_goods;
    }

    @Override
    protected void render(Context context, IDL.Goods goods) {
        mNameView.setText(goods.name);
        mAmountView.setText("" + goods.amount);
        mTypeView.setText(goods.type.name);
    }
}
