package com.sourmilq.sourmilq;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.Utilities.Authentication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ajanthan on 16-10-15.
 */

public class LoginActivity extends Activity {
    private Handler mHandler;
    private Model model;

    private EditText usernameET;
    private EditText passwordET;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);
        model = Model.getInstance();

        usernameET = (EditText)findViewById(R.id.username);
        passwordET = (EditText)findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username",usernameET.getText());
                    jsonObject.put("password",passwordET.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SendDataTask sendDataTask = new SendDataTask();
                sendDataTask.execute(jsonObject);
            }
        });
    }

    public class SendDataTask extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected String doInBackground(JSONObject... params) {
            String result = "";
            try {
                if(params.length>0){
                    result = Authentication.login(params[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            model.setToken(s);
            if(s==null || s.isEmpty()) {
                Toast.makeText(getApplicationContext(), "login failure",
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "login success",
                        Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(LoginActivity.this, ItemsActivity.class);
            startActivity(intent);
            super.onPostExecute(s);
        }
    }

}
