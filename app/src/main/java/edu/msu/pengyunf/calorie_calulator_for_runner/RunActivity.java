package edu.msu.pengyunf.calorie_calulator_for_runner;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class RunActivity extends AppCompatActivity {

    private double latitude = 0;
    private double longitude = 0;
    private double inital_latitude = 0;
    private double inital_longitude = 0;
    private float[] distance = new float[1];
    private double cal = 0;
    private double user_enter_weight = 0;
    private LocationManager locationManager = null;
    private String username = "";
    private String gender;
    private double age;
    private double height;

    private ActiveListener activeListener = new ActiveListener();

    private double speed = 0;
    private double expectSpeed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        // Get the location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Force the screen to say on and bright
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        expectSpeed = getIntent().getDoubleExtra("speed",0);
        user_enter_weight = getIntent().getDoubleExtra("user_weight",0);
        username = getIntent().getStringExtra("user");
        age = getIntent().getDoubleExtra("age",0);
        height = getIntent().getDoubleExtra("height",0);
        gender = getIntent().getStringExtra("gender");


        //System.out.println("callll" + ));

        distance[0] = 0;
        registerListeners();
        setUI();

    }


// expect time = cal / 1.036 / weight / speed
    private int expecTime(double expectSpeed, double user_enter_weight) {
        double time = calculateExpectedCal() / 1.036 / user_enter_weight * 1000 / expectSpeed / 60;
        return (int)time; // mins
    }


    private void setUI()
    {
        //TextView lat = (TextView)findViewById(R.id.textLat);
        //TextView log = (TextView)findViewById(R.id.textLong);
        TextView expect = (TextView)findViewById(R.id.expectTime);
        TextView DistanceRemaining = (TextView)findViewById(R.id.textDistanceRemainshow);
        String tempp = String.valueOf(expecTime(expectSpeed, user_enter_weight))+" minutes";
        expect.setText(tempp);
        TextView dis = (TextView)findViewById(R.id.textDistance);
        TextView caltext = (TextView)findViewById(R.id.textcalutedcal);
        //lat.setText(String.valueOf(inital_latitude));
        //log.setText(String.valueOf(inital_longitude));
        String temppp = String.format("%1.2f",calucateDistanceRemain());
        DistanceRemaining.setText(temppp + " m");
        String temp2 = String.valueOf(distance[0]) + " m";
        dis.setText(temp2);
        String temp3 = String.valueOf(cal) + " kcal";
        caltext.setText(temp3);
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
    private double calucateDistanceRemain()
    {
        return ((calculateExpectedCal()/user_enter_weight/1.036*1000)-distance[0]);
    }
    // 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
    private double calculateCal()
    {
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
                locationManager.requestLocationUpdates(bestAvailable, 5000, 5, activeListener);
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

            System.out.println("time::" + location.getTime());
            Log.d("speed",String.valueOf(location.getSpeed()));
            if (checkCheating(location.getSpeed())) {
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
                setUI();
                System.out.println("location changed: latitude is " + latitude  + ", longitude is " + longitude);
                System.out.println("Cal" + cal  + ", distance " + distance[0]);
                //do distance calculate here

            } else {
                Toast.makeText(getApplicationContext(), "PLEASE DO NOT USE THIS APP WHILE NOT RUNNING", Toast.LENGTH_SHORT).show();
            }
            System.out.println("Cal: " + cal + " goal cal: " + calculateExpectedCal());
            // go to result
            if (finish(cal)) {
                Intent k = new Intent(RunActivity.this,ResultActivity.class);
                k.putExtra("distancee",distance[0]);
                k.putExtra("call",cal);
                //k.putExtra("timee",location.getTime());
                k.putExtra("userr",username);

                startActivity(k);
            }


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

    // check if you are cheating. and stop recording
    // 12.5171m/s
    // Famous runner, Usain Bolt has been clocked at over 28mph

    private boolean checkCheating(float speed) {
        if (speed > 8) {
            return false;
        } else {
            return true;
        }
    }

    private boolean finish(double cal) {
        if (cal >= calculateExpectedCal()) {
            return true;
        } else {
            return false;
        }
    }

    private double calculateExpectedCal()
    {
        if(gender.equals("Male"))
        {
            return (66+1.38*user_enter_weight+5*height-6.8*age)*1.2;
        }
        else
        {
            return ((655+9.6*user_enter_weight+1.9*height-4.7*age))*1.2;
        }
    }

}
