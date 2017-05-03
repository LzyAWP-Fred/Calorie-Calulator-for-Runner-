package edu.msu.pengyunf.calorie_calulator_for_runner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String Gender_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner dropdown = (Spinner)findViewById(R.id.spinnerGender);
        String[] items = new String[]{"Male","Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        Gender_select = dropdown.getSelectedItem().toString();
    }
    // run button
    public void onRun(View view){
        EditText name = (EditText) findViewById(R.id.editName);
        EditText speed = (EditText) findViewById(R.id.editSpeed);
        EditText weightO = (EditText) findViewById(R.id.editWeight);
        EditText age = (EditText) findViewById(R.id.editAge);
        EditText Height = (EditText) findViewById(R.id.editHeight);
        if (!name.getText().toString().equals("") && !Height.getText().toString().equals("") && !speed.getText().toString().equals("") && !weightO.getText().toString().equals("")&&!age.getText().toString().equals("")) {
            String namee = name.getText().toString();
            double weight = Double.parseDouble(weightO.getText().toString());
            double speedy = Double.parseDouble(speed.getText().toString());
            double final_age = Double.parseDouble(age.getText().toString());
            double final_height = Double.parseDouble(Height.getText().toString());
            if (check0(weight) && check0(speedy) && check0(final_age) && check0(final_height)) {
                Intent intent = new Intent(this,RunActivity.class);
                intent.putExtra("gender",Gender_select);
                intent.putExtra("user_weight",weight);
                intent.putExtra("height",final_height);
                intent.putExtra("age",final_age);
                intent.putExtra("speed",speedy);
                intent.putExtra("user", namee);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "NUMBER YOU INPUTED IS NOT CORRECT", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "PLEASE FILL IN ALL THE BLANKS", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean check0(double num) {
        if (num == 0) {
            return false;
        }
        else {
            return true;
        }
    }

}
