package com.pwds.ultimate.securityassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pwds.ultimate.securityassistant.Adapter.MyRecyclerViewAdapter;
import com.pwds.ultimate.securityassistant.Api.Api;
import com.pwds.ultimate.securityassistant.Api.ApiClient;
import com.pwds.ultimate.securityassistant.Model.CommunityPostDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Community extends AppCompatActivity {

    private String postTitle, postBody;
    private EditText titleInput, bodyInput;
    private Button postButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        titleInput = findViewById(R.id.postTitleInput);
        bodyInput = findViewById(R.id.postBodyInput);
        postButton = findViewById(R.id.postBtn);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final ArrayList<CommunityPostDto> communityPostDtoArrayList = new ArrayList<>();


        //------------------- Api call for community posts ---------------//
//
        Api api = ApiClient.getApiClient().create(Api.class);
        Call <ArrayList<CommunityPostDto>> call = api.getCommunityPost();
        call.enqueue(new Callback<ArrayList<CommunityPostDto>>() {
            @Override
            public void onResponse(Call<ArrayList<CommunityPostDto>> call, Response<ArrayList<CommunityPostDto>> response) {
                communityPostDtoArrayList.addAll(response.body());
                MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(Community.this, communityPostDtoArrayList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false ));
            }

            @Override
            public void onFailure(Call<ArrayList<CommunityPostDto>> call, Throwable t) {
                Toast.makeText(Community.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // ----------------- Community Post ------------------------- //

       postButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               postCommunityMessage();
           }
       });
    }

    public void postCommunityMessage(){
        String title = titleInput.getText().toString();
        final String body = bodyInput.getText().toString();

        CommunityPostDto communityPostDto= new CommunityPostDto(title, body);
        Long userId = new Long(1);

        //Calling Api using Retrofit for Community Post
        Api apiService =
                ApiClient.getClient().create(Api.class);
        Call<String> call = apiService.postCommunityMessage(userId, communityPostDto);
        ApiClient.getClient().create(Api.class);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    titleInput.setText(null);
                    bodyInput.setText(null);
                    Toast.makeText(Community.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());


                } else {
                    Toast.makeText(Community.this, "Failed to post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Community.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
