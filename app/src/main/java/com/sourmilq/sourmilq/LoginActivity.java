package com.sourmilq.sourmilq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                try {
                    jsonObject.put("username", usernameET.getText());
                    jsonObject.put("password", passwordET.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(NetworkUtil.isConnected(getApplicationContext())) {
                    Authentication sendDataTask = new Authentication(LoginActivity.this, Authentication.AuthType.LOGIN, model);
                    sendDataTask.execute(jsonObject);
                }else{
                    Toast.makeText(getApplicationContext(), "No Internet, Unable to Login",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onTaskCompleted(String token) {
        model.setToken(token);
        if (token == null || token.isEmpty()) {
            Toast.makeText(getApplicationContext(), "login failure",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "login success",
                    Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(LoginActivity.this, ItemsActivity.class);
        startActivity(intent);
    }
}
