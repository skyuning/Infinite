package me.skyun.infinite.goods;

import java.util.List;

import me.skyun.IDL;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by linyun on 16/7/30.
 */
public interface GoodsApi {
    @GET("/goods")
    Call<List<IDL.Goods>> getGoods(@Query("uid") String uid);
}
