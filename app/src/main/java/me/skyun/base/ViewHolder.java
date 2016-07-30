package me.skyun.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import me.skyun.utils.ViewBinder;

public abstract class ViewHolder<T> {

    protected ViewBinder mViewBinder = new ViewBinder();

    public abstract int getLayoutId(T var1);

    public View inflateView(Context context, T obj, ViewGroup viewGroup) {
        View view = View.inflate(context, getLayoutId(obj), null);
        view.setTag(this);
        mViewBinder.bind(this, view);
        return view;
    }

    protected abstract void render(Context context, T data);
}
