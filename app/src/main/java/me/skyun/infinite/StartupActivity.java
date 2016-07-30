package me.skyun.infinite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Locale;

import me.skyun.infinite.global.Conf;
import me.skyun.infinite.main.MainActivity;
import me.skyun.infinite.user.LoginActivity;
import me.skyun.infinite.user.User;
import me.skyun.utils.Utils;

/**
 * Created by linyun on 16/7/19.
 */
public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        Point screenSize = Utils.getScreenSize(this);
        String imageUrl = "welcome_bg.png";
        String scaledUrl = String.format(Locale.getDefault(), "%s/%s@%dw_%dh_1e_1c",
                Conf.IMAGE_HOST, imageUrl, screenSize.x / 2, screenSize.y / 2);

        Picasso.with(this).load(Uri.parse(scaledUrl)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                getWindow().getDecorView().setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = User.fromPref(StartupActivity.this);
                if (user == null) {
                    startActivity(new Intent(StartupActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(StartupActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 1000);
    }
}
