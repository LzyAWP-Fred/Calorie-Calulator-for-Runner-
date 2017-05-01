package edu.msu.pengyunf.calorie_calulator_for_runner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private double Cal = 0;
    private String username = "";
    private float distance = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        username = getIntent().getStringExtra("userr");
        Cal = getIntent().getDoubleExtra("call",0);
        distance = getIntent().getFloatExtra("distancee",0);
        TextView namee = (TextView)findViewById(R.id.nameee);
        //lint
        String temp = "Dear " + username;
        namee.setText(temp);
        TextView call = (TextView)findViewById(R.id.cal);
        call.setText(String.valueOf(Cal + " Cal"));
        TextView dis = (TextView)findViewById(R.id.distance);
        dis.setText(String.valueOf(distance + " m"));
        System.out.println("fffff" + username + Cal + distance);


    }

    // Say: you are finished!!!

    // Say: have used xxxx Calorie

    // Say: you have ran xxxx meters.

    // Say: Wish you have healthy body

    /**
     *  GO back to main activity
     */
    @Override
    public void onBackPressed() {
        Intent k = new Intent(ResultActivity.this,MainActivity.class);
        startActivity(k);
    }
}
