package me.skyun.infinite.global;

import com.google.gson.annotations.SerializedName;

/**
 * Created by linyun on 16/7/26.
 */
public class SimpleResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("err_msg")
    public String errMsg;
}
