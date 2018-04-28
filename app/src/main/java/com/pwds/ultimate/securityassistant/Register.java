package com.pwds.ultimate.securityassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pwds.ultimate.securityassistant.Adapter.GPSTracker;
import com.pwds.ultimate.securityassistant.Api.Api;
import com.pwds.ultimate.securityassistant.Api.ApiClient;
import com.pwds.ultimate.securityassistant.Model.RegisterDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private TextView navigateToLogin;
    private Button register;
    private Spinner spinner;
    private EditText nameInput, passwordInput, mobileInput, usernameInput, contactPersonInput, contactPersonMobileInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if((contactPersonMobile.getText().toString()).equals("")){
//                    Toast.makeText(getApplicationContext(), "Family Member Contact Number is Required ",
//                            Toast.LENGTH_LONG).show();
//                }else{
//                    SharedPreferences.Editor editor = getSharedPreferences("LOGIN_TEST", MODE_PRIVATE).edit();
//                    editor.putString("mobile", contactPersonMobile.getText().toString());
//                    editor.apply();
//                    Toast.makeText(getApplicationContext(), "Registered SuccessFully",
//                            Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(Register.this, MapsActivity.class));
//                }
                if(!(nameInput.getText().toString()).equals("")&&!(spinner.getSelectedItem().toString()).equals("")&&!(usernameInput.getText().toString()).equals("")&&!(passwordInput.getText().toString()).equals("")&&!(mobileInput.getText().toString()).equals("")){
                    if((mobileInput.getText().toString()).matches("[9][8|7][4|5|6|2|1|0][0-9]{7}")){
                        if(!(contactPersonMobileInput.getText().toString()).equals("")){
                            if((contactPersonMobileInput.getText().toString()).matches("[9][8|7][4|5|6|2|1|0][0-9]{7}")){
                                 register();
                            }else{
                                Toast.makeText(Register.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            register();
                        }
                    }else{
                        Toast.makeText(Register.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Register.this, "Few fields are Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialize(){
        navigateToLogin = findViewById(R.id.navigateLogin);
        register = findViewById(R.id.registerButton);
        nameInput = findViewById(R.id.nameField);
        mobileInput = findViewById(R.id.mobileField);
        usernameInput = findViewById(R.id.usernameField);
        passwordInput = findViewById(R.id.passwordField);
        contactPersonInput = findViewById(R.id.contactPersonField);
        contactPersonMobileInput = findViewById(R.id.contactPersonNumberField);
        spinner =  findViewById(R.id.userTypeSpinner);

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.userTypeSpinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"DISABLED", "VOLUNTEER", "ORGANIZATION"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        navigateToLogin();
    }

    public void navigateToLogin(){
        navigateToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }

    public void register(){
        String name = nameInput.getText().toString();
        String phoneNumber = mobileInput.getText().toString();
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String contactPerson = contactPersonInput.getText().toString();
        String contactPersonNo = contactPersonMobileInput.getText().toString();
        String userType = spinner.getSelectedItem().toString();
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        final RegisterDto registerDto = new RegisterDto();
        registerDto.setName(name);
        registerDto.setPhoneNumber(phoneNumber);
        registerDto.setUserType(userType);
        registerDto.setUsername(username);
        registerDto.setPassword(password);
        registerDto.setContactPerson(contactPerson);
        registerDto.setContactPersonNo(contactPersonNo);
        registerDto.setLat(latitude);
        registerDto.setLng(longitude);
        //Calling Api using Retrofit for Login
        Api apiService =
                ApiClient.getClient().create(Api.class);
        Call<RegisterDto> call = apiService.userRegister(registerDto);
        ApiClient.getClient().create(Api.class);

        call.enqueue(new Callback<RegisterDto>() {
            @Override
            public void onResponse(Call<RegisterDto> call, Response<RegisterDto> response) {
                if (response.isSuccessful()) {

                    SharedPreferences.Editor editor = getSharedPreferences("LOGIN_TEST", MODE_PRIVATE).edit();
                    editor.putString("mobile", registerDto.getContactPersonNo());
                    editor.apply();
                    Toast.makeText(Register.this, "Register Success", Toast.LENGTH_SHORT).show();
                    Intent loginActivity = new Intent(Register.this, Login.class);
                    finish();


                    startActivity(loginActivity);


                } else {
                    Toast.makeText(Register.this, "Register Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterDto> call, Throwable t) {
                Log.e("error>>>>", t.toString());
                System.out.println("error>>>>>>>>>>>>>>>>" + t.toString());
//                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Toast.makeText(Register.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
