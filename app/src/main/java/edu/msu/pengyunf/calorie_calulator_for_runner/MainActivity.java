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

        EditText name = (EditText) findViewById(R.id.editName);
        EditText cal = (EditText) findViewById(R.id.editCal);
        EditText speed = (EditText) findViewById(R.id.editSpeed);
        EditText weightO = (EditText) findViewById(R.id.editWeight);
        if (!name.getText().toString().equals("") && !cal.getText().toString().equals("") && !speed.getText().toString().equals("") && !weightO.getText().toString().equals("")) {
            String namee = name.getText().toString();
            double weight = Double.parseDouble(weightO.getText().toString());
            double calNum = Double.parseDouble(cal.getText().toString());
            double speedy = Double.parseDouble(speed.getText().toString());

            Intent intent = new Intent(this,RunActivity.class);
            intent.putExtra("user_weight",weight);
            intent.putExtra("cal",calNum);
            intent.putExtra("speed",speedy);
            intent.putExtra("user", namee);

            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "PLEASE FILL IN ALL THE BLANKS", Toast.LENGTH_SHORT).show();
        }

    }
}
