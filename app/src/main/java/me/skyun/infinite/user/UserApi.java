package me.skyun.infinite.user;

import me.skyun.infinite.global.SimpleResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by linyun on 16/7/20.
 */
public interface UserApi {

    @GET("/user")
    Call<User> getUser();

    @FormUrlEncoded
    @POST("/user/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/user/set_avatar")
    Call<SimpleResponse> setAvatar(@Field("avatar") String avatar);
}
