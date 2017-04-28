package edu.msu.pengyunf.calorie_calulator_for_runner;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ResultActivity extends AppCompatActivity {

    private LocationManager manager;
    public double user_lat;
    public double user_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        try{
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            updateLocation(location);

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10,
                    locationListener);
        }
        catch(SecurityException e){
            System.out.println("require permission for location services");
        }

    }


    private final LocationListener locationListener = new LocationListener() {//updates the current location
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }
        public void onProviderDisabled(String provider){
            updateLocation(null);

        }
        public void onProviderEnabled(String provider){

        }
        public void onStatusChanged(String provider, int status,Bundle extras){ }
    };


    private void updateLocation(Location location) {//updates the current location
        String latLng;
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            user_lat = lat;
            user_lng = lng;
            System.out.println("location changed:" + user_lat + "," + user_lng);
        }
    }

}
