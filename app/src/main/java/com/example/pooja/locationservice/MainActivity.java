package com.example.pooja.locationservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements LocationListener{

    private EditText host;
    private EditText username;
    private double latitude = 0;
    private double longitude = 0;
    private LocationManager locationManager;
    private ToggleButton startStopTracker;
    private TextView resultView;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startStopTracker = (ToggleButton) findViewById(R.id.tracker);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        username = (EditText) findViewById(R.id.username1);
        host = (EditText) findViewById(R.id.host1);
        resultView = (TextView) findViewById(R.id.resultView);

        startStopTracker.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if (ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates("network",500,1,MainActivity.this);
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location == null) {
                            Log.i("LocationService", "Location not Found!");
                        }
                        else {
                            Log.i("LocationService","Name : " + username.getText() + " Latitude : "+ String.valueOf(location.getLatitude()) + " Longitude : " + String.valueOf(location.getLongitude()));
                            resultView.setText(String.valueOf(username.getText()) + " Latitude : "+ String.valueOf(location.getLatitude()) + " Longitude : " + String.valueOf(location.getLongitude()));
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Enable GPS and try again", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.i("LocationService", "Tracking Stopped");
                }
            }
        });

        doesHavePermissions();
    }

    public void doesHavePermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }else {
            Log.i("LocationService","Already has permissions");
        }
    }

    private Location getCurrentLocation(){
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null ;
        }
        locationManager.requestLocationUpdates("network",500,1,this);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location==null){
            Toast.makeText(getApplicationContext(), "Enable GPS and try again", Toast.LENGTH_LONG).show();
        }else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        return location;

    }

    @Override
    public void onProviderEnabled(String provider) {
//        getCurrentLocation();
    }

    @Override
    public void onProviderDisabled(String provider) {
//        getCurrentLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        getCurrentLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        getCurrentLocation();
    }

}
