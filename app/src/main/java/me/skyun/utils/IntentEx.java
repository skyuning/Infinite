package me.skyun.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import junit.framework.Assert;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by linyun on 16/2/27.
 */
public class IntentEx extends Intent {

    public IntentEx(String action) {
        super(action);
    }

    public IntentEx setAction(String action) {
        return (IntentEx) super.setAction(action);
    }

    public IntentEx setAction(Class actionType) {
        setAction(actionType.getCanonicalName());
        return this;
    }

    public IntentEx addCategories(Class... categories) {
        for (Class category : categories) {
            addCategory(category.getCanonicalName());
        }
        return this;
    }

    public IntentEx putKeyValueExtras(Object... keyValues) {
        for (int i = 0; i < keyValues.length; i += 2) {
            putExtraEx((String) keyValues[i], keyValues[i + 1]);
        }
        return this;
    }

    public IntentEx putObjectExtras(Object... objects) {
        for (Object obj : objects) {
            if (obj != null) {
                if (obj instanceof Uri) { // Uri需要特殊处理
                    putExtra(Uri.class, obj);
                } else {
                    // 必需要是public的类
                    Assert.assertTrue("The receiver has no way to receive Data of none public class",
                        Modifier.isPublic(obj.getClass().getModifiers()));
                    putExtraEx(obj.getClass().getCanonicalName(), obj);
                }
            }
        }
        return this;
    }

    public IntentEx putExtra(Class key, Object value) {
        putExtraEx(key.getCanonicalName(), value);
        return this;
    }

    private IntentEx putExtraEx(String key, Object value) {
        if (value instanceof Serializable) {
            return (IntentEx) putExtra(key, (Serializable) value);
        } else if (value instanceof Parcelable) {
            return (IntentEx) putExtra(key, (Parcelable) value);
        }

        try {
            Method putExtraMethod = getClass().getMethod("putExtra", String.class, value.getClass());
            putExtraMethod.setAccessible(true);
            putExtraMethod.invoke(this, key, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void localBroadcast(Context context) {
        if (context != null) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(this);
        }
    }

}
