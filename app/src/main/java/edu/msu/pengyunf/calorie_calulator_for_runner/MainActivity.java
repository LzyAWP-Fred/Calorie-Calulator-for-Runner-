package edu.msu.pengyunf.calorie_calulator_for_runner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRun(View view){
        Intent intent = new Intent(this,RunActivity.class);

        EditText name = (EditText) findViewById(R.id.editName);
        EditText weight = (EditText) findViewById(R.id.editWeight);
        EditText cal = (EditText) findViewById(R.id.editCal);
        EditText speed = (EditText) findViewById(R.id.editSpeed);
        startActivity(intent);

    }
}
