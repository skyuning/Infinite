//package me.chunyu.support.base;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import java.lang.reflect.ParameterizedType;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import me.chunyu.G7Annotation.Adapter.G7ViewHolder;
//
///**
// * Created by linyun on 15-8-18.
// */
//public class AdapterEx<T> extends ArrayAdapter<T> {
//
//    /**
//     * 从item类型到view holder类型的映射
//     */
//    private Map<Class, Class<? extends G7ViewHolder>> mViewHolders = new HashMap<>();
//
//    private List<T> mData;
//
//    public AdapterEx(Context context) {
//        this(context, new ArrayList<T>());
//    }
//
//    protected AdapterEx(Context context, List<T> objects) {
//        super(context, 0, objects);
//        mData = objects;
//    }
//
//    public List<T> getData() {
//        return mData;
//    }
//
//    /**
//     * 一般情况下，holderClz的泛型参数就是itemClz，此时可以用这个addViewHolder函数
//     */
//    public void addViewHolder(Class<? extends G7ViewHolder> holderClz) {
//        ParameterizedType type = (ParameterizedType) holderClz.getGenericSuperclass();
//        Class itemClz = (Class) type.getActualTypeArguments()[0];
//        mViewHolders.put(itemClz, holderClz);
//    }
//
//    public void addViewHolder(Class itemClz, Class holderClz) {
//        mViewHolders.put(itemClz, holderClz);
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return mViewHolders.size() + 1;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        Class itemClz = getItem(position).getClass();
//        Class[] itemClasses = mViewHolders.keySet().toArray(new Class[0]);
//        for (int i = 0; i < itemClasses.length; i++) {
//            Class clz = itemClasses[i];
//            if (itemClz.equals(clz))
//                return i + 1; // 貌似需要从1开始计数
//        }
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Object item = getItem(position);
//        Class holderClz = getHolderClass(item.getClass());
//
//        if (holderClz == null) {
//            if (convertView == null) {
//                convertView = new TextView(parent.getContext());
//                convertView.setPadding(20, 20, 20, 20);
//            }
//            ((TextView) convertView).setText(item.toString());
//            return convertView;
//        }
//
//        G7ViewHolder holder = null;
//        if (convertView == null) {
//            try {
//                holder = (G7ViewHolder) holderClz.newInstance();
//                convertView = holder.inflateView(getContext(), item, parent);
//                convertView.setTag(holder);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            holder = (G7ViewHolder) convertView.getTag();
//        }
//        holder.showData(getContext(), item);
//        return convertView;
//    }
//
//    public Class getHolderClass(Class itemClz) {
//        Class holderClz = mViewHolders.get(itemClz);
//        if (holderClz == null && itemClz.getSuperclass() != null) {
//            holderClz = getHolderClass(itemClz.getSuperclass());
//        }
//        return holderClz;
//    }
//}
//
