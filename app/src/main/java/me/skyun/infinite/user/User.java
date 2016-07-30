package me.skyun.infinite.user;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

import me.skyun.infinite.global.GlobalPref;

/**
 * Created by linyun on 16/7/20.
 */
public class User {

    private static final String PREF_KEY = User.class.getName();

    @SerializedName("id")
    public String id;

    @SerializedName("username")
    public String username;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("avatar")
    public String avatar;

    public static User fromJson(String json) {
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        return new Gson().fromJson(reader, User.class);
    }

    public static User fromPref(Context context) {
        String json = GlobalPref.get(context, PREF_KEY);
        try {
            return fromJson(json);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        GlobalPref.set(context, PREF_KEY, null);
        return null;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public void toPref(Context context) {
        GlobalPref.set(context, PREF_KEY, toJson());
    }

    public static void logout(Context context) {
        GlobalPref.set(context, PREF_KEY, null);
    }
}
