package me.skyun.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linyun on 16/7/25.
 */
public class BaseActivity extends AppCompatActivity {

    private Map<String, Integer> mViewIdMap = new HashMap<>();

    protected <T extends View> T findViewByIdPre(String name, int id) {
        mViewIdMap.put(name, id);
        return null;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (mViewIdMap.size() > 0) {
            for (String name : mViewIdMap.keySet()) {
                int viewId = mViewIdMap.get(name);
                try {
                    Field viewField = getClass().getDeclaredField(name);
                    viewField.setAccessible(true);
                    View value = findViewById(viewId);
                    viewField.set(this, value);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
