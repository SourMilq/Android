package com.sourmilq.sourmilq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sourmilq.sourmilq.DataModel.Model;

import java.util.TimerTask;

/**
 * Created by ajanthan on 16-10-15.
 */

public class SplashScreenActivity extends Activity {
    private Handler mHandler;
    private Model model;

    private Button signUpBtn;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        model = Model.getInstance(getApplicationContext());

        if (model.getToken() != null) {
//            mHandler = new Handler();
//            mHandler.postDelayed(mUpdateTimeTask, 2000);
        }

        signUpBtn = (Button) findViewById(R.id.signupBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signUpBtn.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.VISIBLE);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreenActivity.this, RecipeActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //Synchronization
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                model.updateGroceryList();
                model.updatePantryList();
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnable, 100);

    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            Intent intent = new Intent(SplashScreenActivity.this, ItemsActivity.class);
            startActivity(intent);
        }
    };
}
