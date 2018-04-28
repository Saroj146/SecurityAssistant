package com.pwds.ultimate.securityassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pwds.ultimate.securityassistant.Api.Api;
import com.pwds.ultimate.securityassistant.Api.ApiClient;
import com.pwds.ultimate.securityassistant.Model.LoginDto;
import com.pwds.ultimate.securityassistant.Model.RegisterDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private TextView navigateToRegister;
    private EditText mUsername, mPassword;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        SharedPreferences prefs = getSharedPreferences("LOGIN_TEST", MODE_PRIVATE);
        String name = prefs.getString("username", "");//"No name defined" is the default value.
        if(!name.equals("")){
            startActivity(new Intent(Login.this, MapsActivity.class));
    }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((mUsername.getText().toString()).equals("") || (mPassword.getText().toString()).equals("")){
                    Toast.makeText(getApplicationContext(), "Both Username and Password are Required",
                            Toast.LENGTH_LONG).show();
                }else {
//                    SharedPreferences.Editor editor = getSharedPreferences("LOGIN_TEST", MODE_PRIVATE).edit();
//                    editor.putString("username", mUsername.getText().toString());
//                    editor.apply();
//                    startActivity(new Intent(Login.this, MapsActivity.class));
                     login();
                }
            }
        });

    }

    private void initialize(){
        navigateToRegister = findViewById(R.id.navigateRegister);
        mUsername = findViewById(R.id.usernameField);
        mPassword = findViewById(R.id.passwordField);
        login = findViewById(R.id.loginButton);
        navigateToRegister();

    }

    public void navigateToRegister(){
        navigateToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }




    public void login(){
        final String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        LoginDto loginDto = new LoginDto(username, password);

        //Calling Api using Retrofit for Login
        Api apiService =
                ApiClient.getClient().create(Api.class);
        Call<RegisterDto> call = apiService.sendUsernameAndPassword(loginDto);
        ApiClient.getClient().create(Api.class);

        call.enqueue(new Callback<RegisterDto>() {
            @Override
            public void onResponse(Call<RegisterDto> call, Response<RegisterDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = getSharedPreferences("LOGIN_TEST", MODE_PRIVATE).edit();
                    editor.putString("username", username);
                    editor.putLong("userId", response.body().getId());
                    editor.apply();

                    Intent mapActivity = new Intent(Login.this, MapsActivity.class);
                    finish();


                    startActivity(mapActivity);


                } else {
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterDto> call, Throwable t) {
//                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
