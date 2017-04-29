package edu.msu.pengyunf.calorie_calulator_for_runner;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RunActivity extends AppCompatActivity {

    private double latitude = 0;
    private double longitude = 0;
    private double inital_latitude = 0;
    private double inital_longitude = 0;
    private float[] distance = new float[1];
    private double cal = 0;
    private Location location;
    private double user_enter_weight = 0;
    private LocationManager locationManager = null;

    private ActiveListener activeListener = new ActiveListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        // Get the location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Force the screen to say on and bright
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        distance[0] = 0;
        registerListeners();
        setUI();


    }
    private void setUI()
    {
        TextView lat = (TextView)findViewById(R.id.textLat);
        TextView log = (TextView)findViewById(R.id.textLong);
        TextView dis = (TextView)findViewById(R.id.textDistance);
        TextView caltext = (TextView)findViewById(R.id.textcalutedcal);
        lat.setText(String.valueOf(inital_latitude));
        log.setText(String.valueOf(inital_longitude));
        dis.setText(String.valueOf(distance[0]));
        caltext.setText(String.valueOf(cal));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListeners();
    }

    @Override
    protected void onPause() {
        unregisterListeners();
        super.onPause();
    }
    private void getinitallocation()
    {
        inital_latitude = latitude;
        inital_longitude = longitude;
    }

    private float[] calculateDistance()
    {
        float[] result = new float[1];
        result[0] = 0;
        Location.distanceBetween(inital_latitude,inital_longitude,latitude,longitude,result);
        return result;
    }
    private double calculateCal()
    {
        user_enter_weight = getIntent().getDoubleExtra("user_weight",0);
        System.out.println(user_enter_weight);
        return 0.001*distance[0]*user_enter_weight*1.036;
    }

    private void registerListeners() {
        unregisterListeners();

        // Create a Criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);


        String bestAvailable = locationManager.getBestProvider(criteria, true);

        if(bestAvailable != null) {
            try{
                locationManager.requestLocationUpdates(bestAvailable, 5, 5, activeListener);
                Location location = locationManager.getLastKnownLocation(bestAvailable);
                if(inital_latitude==0 && inital_longitude==0)
                {
                    inital_latitude = location.getLatitude();
                    inital_longitude = location.getLongitude();
                    setUI();
                }
            }
            catch(SecurityException e){
                System.out.println("SecurityException");
            }

        }
    }

    private void unregisterListeners() {
        locationManager.removeUpdates(activeListener);
    }


    private class ActiveListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            inital_latitude = latitude;
            inital_longitude = longitude;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if(inital_longitude == 0.0 && inital_latitude==0.0)
            {
                inital_longitude = longitude;
                inital_latitude = latitude;
            }
            distance[0] += calculateDistance()[0];
            cal += calculateCal();
            System.out.println(cal);
            setUI();
            System.out.println("location changed: latitude is " + latitude  + ", longitude is " + longitude);
            //do distance calculate here


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            registerListeners();
        }
    }

}
