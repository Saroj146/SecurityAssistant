package com.pwds.ultimate.securityassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pwds.ultimate.securityassistant.Adapter.GPSTracker;
import com.pwds.ultimate.securityassistant.Adapter.NearbyViewAdapter;
import com.pwds.ultimate.securityassistant.Api.Api;
import com.pwds.ultimate.securityassistant.Api.ApiClient;
import com.pwds.ultimate.securityassistant.Model.RegisterDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyPeople extends AppCompatActivity {
    private Button map, community;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_people);
        map = findViewById(R.id.mapViewButton);
        community = findViewById(R.id.communityButton);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(NearbyPeople.this, MapsActivity.class));
            }
        });

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NearbyPeople.this, Community.class));
            }
        });

        getNearByPeople();
    }

    public void getNearByPeople(){
        //------------------- Api call for nearby people ---------------//

        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final ArrayList<RegisterDto> registerDtoList = new ArrayList<>();
        Api api = ApiClient.getApiClient().create(Api.class);
        Call<ArrayList<RegisterDto>> call = api.getNearBY(latitude, longitude);
        call.enqueue(new Callback<ArrayList<RegisterDto>>() {
            @Override
            public void onResponse(Call<ArrayList<RegisterDto>> call, Response<ArrayList<RegisterDto>> response) {
                registerDtoList.addAll(response.body());
                NearbyViewAdapter adapter = new NearbyViewAdapter(NearbyPeople.this, registerDtoList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false ));

            //    Toast.makeText(NearbyPeople.this, "Data Received", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<RegisterDto>> call, Throwable t) {
                Toast.makeText(NearbyPeople.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
