package com.example.hikers_watch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView lat_textView;
    TextView long_textView;
    TextView accuracy_textView;
    TextView altitude_textView;
    TextView address_textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        hideActionBar();
        setupTextViews();
        defineLocationListener();
        checkPermissions();
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.hide();
    }

    private void setupTextViews() {
        lat_textView = (TextView) findViewById(R.id.lat_textView);
        long_textView = (TextView) findViewById(R.id.long_textView);
        accuracy_textView = (TextView) findViewById(R.id.accuracy_textView);
        altitude_textView = (TextView) findViewById(R.id.altitude_textView);
        address_textView = (TextView) findViewById(R.id.address_textView);
    }

    private void defineLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                lat_textView.setText("Latitude: " + location.getLatitude());
                long_textView.setText("Longitude: " + location.getLongitude());
                accuracy_textView.setText("Accuracy: " + location.getLatitude());
                altitude_textView.setText("Altitude: " + location.getLatitude());

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if(listAddresses != null && listAddresses.size() > 0){
                        String address = "Address:\n";

                        if(listAddresses.get(0).getSubThoroughfare() != null){
                            address += listAddresses.get(0).getSubThoroughfare() + " ";
                        }

                        if(listAddresses.get(0).getThoroughfare() != null){
                            address += listAddresses.get(0).getThoroughfare() + "\n";
                        }

                        if(listAddresses.get(0).getPostalCode() != null){
                            address += listAddresses.get(0).getPostalCode() + " ";
                        }

                        if(listAddresses.get(0).getAdminArea() != null){
                            address += listAddresses.get(0).getAdminArea() + "\n";
                        }

                        if(listAddresses.get(0).getCountryName() != null){
                            address += listAddresses.get(0).getCountryName();
                        }

                        address_textView.setText(address);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        };
    }

    private void checkPermissions() {
        if(Build.VERSION.SDK_INT < 23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 , 0, locationListener);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }
}