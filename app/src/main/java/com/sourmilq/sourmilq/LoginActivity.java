package com.sourmilq.sourmilq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Tasks.Authentication;
import com.sourmilq.sourmilq.Utilities.NetworkUtil;
import com.sourmilq.sourmilq.callBacks.onCallCompleted;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ajanthan on 16-10-15.
 */

public class LoginActivity extends Activity implements onCallCompleted {
    private Handler mHandler;
    private Model model;

    private EditText usernameET;
    private EditText passwordET;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);
        model = Model.getInstance(getApplicationContext());

        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()) {
                    setUI(false);
                    try {
                        jsonObject.put("username", username);
                        jsonObject.put("password", password);
                    } catch (JSONException e) {
                        setUI(true);
                        e.printStackTrace();
                    }
                    if (NetworkUtil.isConnected(getApplicationContext())) {
                        Authentication sendDataTask = new Authentication(LoginActivity.this, Authentication.AuthType.LOGIN, model);
                        sendDataTask.execute(jsonObject);
                    } else {
                        Snackbar.make(v, "No Internet, unable to login", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(v, "Please enter username and password", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }


            }
        });
    }

    public void setUI(boolean clickable) {
        usernameET.setEnabled(clickable);
        passwordET.setEnabled(clickable);
        loginBtn.setEnabled(clickable);
    }

    @Override
    public void onTaskCompleted(boolean success) {
        if (success) {
            Intent intent = new Intent(LoginActivity.this, ItemsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Invalid username or password", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            setUI(true);
        }

    }
}
