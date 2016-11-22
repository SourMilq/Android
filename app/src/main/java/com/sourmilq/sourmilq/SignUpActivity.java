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

public class SignUpActivity extends Activity implements onCallCompleted {
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
        model = Model.getInstance(getApplicationContext());

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
                String firstName = firstNameET.getText().toString();
                String lastName = lastNameET.getText().toString();
                String email = emailET.getText().toString();
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();
                setUI(false);
                if(!(firstName.isEmpty() || lastName.isEmpty() ||
                        email.isEmpty() || username.isEmpty() || password.isEmpty())) {
                    try {
                        jsonObject.put("first_name", firstName);
                        jsonObject.put("last_name", lastName);
                        jsonObject.put("email", email);
                        jsonObject.put("username", username);
                        jsonObject.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (NetworkUtil.isConnected(getApplicationContext())) {
                        Authentication sendDataTask = new Authentication(SignUpActivity.this, Authentication.AuthType.SIGNUP, model);
                        sendDataTask.execute(jsonObject);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet, Unable to Sign Up",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Make sure no fields are empty",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setUI(boolean clickable){
        firstNameET.setEnabled(clickable);
        lastNameET.setEnabled(clickable);
        usernameET.setEnabled(clickable);
        passwordET.setEnabled(clickable);
        emailET.setEnabled(clickable);
        registerBtn.setEnabled(clickable);
    }


    @Override
    public void onTaskCompleted(boolean success) {
        if(success){
            Intent intent = new Intent(SignUpActivity.this, ItemsActivity.class);
            startActivity(intent);
        }else{
            setUI(true);
            Toast.makeText(getApplicationContext(), "Username already exists",
                    Toast.LENGTH_LONG).show();
        }
    }
}
