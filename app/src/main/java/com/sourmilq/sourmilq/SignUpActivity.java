package com.sourmilq.sourmilq;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Utilities.APIHelper;
import com.sourmilq.sourmilq.Utilities.Authentication;
import com.sourmilq.sourmilq.callBacks.onCallCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ajanthan on 16-10-15.
 */

public class SignUpActivity extends Activity implements onCallCompleted {
    private Handler mHandler;
    private Model model;

    private EditText firstNameET;
    private EditText lastNameET;
    private EditText usernameET;
    private EditText passwordET;
    private EditText emailET;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen_activity);
        model = Model.getInstance();

        firstNameET = (EditText) findViewById(R.id.firstName);
        lastNameET = (EditText) findViewById(R.id.lastName);
        emailET = (EditText) findViewById(R.id.email);
        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);

        registerBtn = (Button) findViewById(R.id.register);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("first_name", firstNameET.getText());
                    jsonObject.put("last_name", lastNameET.getText());
                    jsonObject.put("email", emailET.getText());
                    jsonObject.put("username", usernameET.getText());
                    jsonObject.put("password", passwordET.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Authentication sendDataTask = new Authentication(SignUpActivity.this, Authentication.AuthType.SIGNUP);
                sendDataTask.execute(jsonObject);
            }
        });
    }

    @Override
    public void onTaskCompleted(String token) {
        model.setToken(token);
        Toast.makeText(getApplicationContext(), token,
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignUpActivity.this, ItemsActivity.class);
        startActivity(intent);
    }
}
