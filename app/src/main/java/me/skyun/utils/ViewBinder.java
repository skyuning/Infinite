package me.skyun.utils;

import android.view.View;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linyun on 16/7/30.
 */
public class ViewBinder {
    private Map<String, Integer> mViewIdMap = new HashMap<>();

    public <T extends View> T add(String name, int id) {
        mViewIdMap.put(name, id);
        return null;
    }

    public void bind(Object object, View view) {
        if (mViewIdMap.isEmpty()) {
            return;
        }
        for (String name : mViewIdMap.keySet()) {
            int viewId = mViewIdMap.get(name);
            try {
                Field viewField = object.getClass().getDeclaredField(name);
                viewField.setAccessible(true);
                View value = view.findViewById(viewId);
                viewField.set(object, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
