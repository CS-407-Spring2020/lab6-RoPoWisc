package com.example.android.lab_6;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.android.gms:play-services-location:15.0.1;

public class MainActivity extends FragmentActivity {

    private final LatLng mDestinationLatLng = new LatLng(43.0755516, -89.4042859);

    private FusedLocationProviderClient mFusedLocationProviderClient; // Save the instance
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 12;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                googleMap.addMarker(new MarkerOptions()
                        .position(mDestinationLatLng)
                        .title("Bascom Hall"));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDestinationLatLng,10));
                displayMyLocation();
            }
        });
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }

    /*private void initLocation() {
        //check permission
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionTwo = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        //if not ask
        if (Build.VERSION.SDK_INT < 23) {
            //DON'T NEED TO ASK
            startListening();
        } else {
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                if (permissionTwo != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                } else {
                    mFusedLocationProviderClient.getLastLocation()
                            .addOnCompleteListener(this, task -> {
                                Location mLastKnownLocation = task.getResult();
                                if(task.isSuccessful() && mLastKnownLocation != null){
                                     new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude());
                                }
                            });

                }
            }
        }
    }*/

    private void displayMyLocation(){
        //check permission
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        //if not ask
        if (Build.VERSION.SDK_INT < 23) {
            //DON'T NEED TO ASK
            startListening();
        } else {
            if (permission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {

                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDestinationLatLng,15));
                    //else display current location
                    mFusedLocationProviderClient.getLastLocation()
                            .addOnCompleteListener(this, task -> {
                                Location mLastKnownLocation = task.getResult();
                                if (task.isSuccessful() && mLastKnownLocation != null) {
                                    mMap.addPolyline(new PolylineOptions().add(
                                            new LatLng(mLastKnownLocation.getLatitude(),
                                                    mLastKnownLocation.getLongitude()), mDestinationLatLng));
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(mLastKnownLocation.getLatitude(),
                                                    mLastKnownLocation.getLongitude()))
                                            .title("Point B"));
                                    //polyline1.setTag("A")
                                }
                            });
            }
        }
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }
}