package me.skyun.infinite.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by linyun on 16/7/20.
 */
public class User {

    @SerializedName("id")
    public String id;

    @SerializedName("username")
    public String username;

    @SerializedName("avatar")
    public String avatar;
}
