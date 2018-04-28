package com.pwds.ultimate.securityassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pwds.ultimate.securityassistant.Adapter.GPSTracker;
import com.pwds.ultimate.securityassistant.Api.Api;
import com.pwds.ultimate.securityassistant.Api.ApiClient;
import com.pwds.ultimate.securityassistant.Model.RegisterDto;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Timer autoUpdate;
    private GoogleMap mMap;
    private Button emergencyBtn, communityBtn, viewPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        emergencyBtn = findViewById(R.id.emBtn);
        communityBtn = findViewById(R.id.communityButton);
        viewPeople = findViewById(R.id.viewNearBy);
        sendEmergencyMessage();

        communityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, Community.class);
                startActivity(intent);
            }
        });

        viewPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, NearbyPeople.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                       // Toast.makeText(MapsActivity.this, "Map Refresh", Toast.LENGTH_SHORT).show();
                        onMapReady(mMap);
                    }
                });
            }
        }, 0, 10000); // updates each 40 secs
    }

    @Override
    public void onPause() {
        autoUpdate.cancel();
        super.onPause();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Kathmandu and move the camera

        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        // Toast.makeText(getApplicationContext(), "Location"+String.valueOf(latitude) + String.valueOf(longitude) , Toast.LENGTH_LONG).show();

        LatLng ktm = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(ktm).title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        final ArrayList<RegisterDto> registerDtoList = new ArrayList<>();
        Api api = ApiClient.getApiClient().create(Api.class);
        Call<ArrayList<RegisterDto>> call = api.getNearBY(latitude, longitude);
        call.enqueue(new Callback<ArrayList<RegisterDto>>() {
            @Override
            public void onResponse(Call<ArrayList<RegisterDto>> call, Response<ArrayList<RegisterDto>> response) {
                registerDtoList.addAll(response.body());
                for (RegisterDto registerDto: registerDtoList){
                    LatLng latLng = new LatLng(registerDto.getLat(), registerDto.getLng());
                    Double distance = Double.parseDouble(registerDto.getDistance());
                    String username = registerDto.getUsername();
                    mMap.addMarker( new MarkerOptions().position(latLng).title( username + "(" + new DecimalFormat("##.#").format(distance) + " m)"));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RegisterDto>> call, Throwable t) {
                Toast.makeText(MapsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLng(ktm));
    }

    public void sendEmergencyMessage(){
        final GPSTracker gps = new GPSTracker(this);
        final Geocoder gc = new Geocoder(this);
            emergencyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences prefs = getSharedPreferences("LOGIN_TEST", MODE_PRIVATE);
                    String mobile = prefs.getString("mobile", "");//"No name defined" is the default value.
                    if(!mobile.equals("")){
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        if(gc.isPresent()){
                            try {
                                List <Address> list = gc.getFromLocation(latitude, longitude, 1);
                                Address address = list.get(0);
                                String place = address.getLocality();
                                place.concat(","+address.getFeatureName());
                                if(!place.equals("")){
                                   // Toast.makeText(getApplicationContext(), "place: " + place, Toast.LENGTH_LONG).show();
                                    sendSMS(mobile, "Emergency, Please Help Me \n My Location Latitude:" + latitude + " Longitude:" + longitude + "\n Place: " + place);
                                }else {
                                   // Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                                    sendSMS(mobile, "Emergency, Please Help Me \n My Location Latitude:" + latitude + " Longitude:" + longitude);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                           // Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                            sendSMS(mobile, "Emergency, Please Help Me \n My Location Latitude:" + latitude + " Longitude:" + longitude);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "No Family Contact Registered", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
