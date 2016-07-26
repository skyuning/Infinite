package me.skyun.infinite.global;

import android.app.ProgressDialog;
import android.content.Context;

import junit.framework.Assert;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by linyun on 16/7/20.
 */
public class RetrofitUtils {

    private static Retrofit sRetrofit;

    public static Retrofit getInstance() {
        Assert.assertNotNull("确保在Application的onCreate调用init", sRetrofit);
        return sRetrofit;
    }

    public static Retrofit init(final Context context, String baseUrl) {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor myInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                String uid = GlobalPref.get(context, GlobalPref.USER_ID);
                Request request = chain.request().newBuilder().addHeader("Cookie", "uid=" + uid).build();
                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .addInterceptor(myInterceptor)
                .build();

        sRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return sRetrofit;
    }

    public static <T> void enqueueCall(Context context, Call<T> call, final Callback<T> callback) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                progressDialog.dismiss();
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                progressDialog.dismiss();
                callback.onFailure(call, t);
            }
        });
    }
}

