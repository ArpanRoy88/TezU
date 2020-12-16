package com.example.tezu;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StaffRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Toolbar myToolbar;

    Spinner schoolSpinner;
    Spinner departmentSpinner;
    Spinner positionSpinner;


    ArrayAdapter<CharSequence> adapterDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_registration);

        myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Staff's Registration");
        setSupportActionBar(myToolbar);

        schoolSpinner = findViewById(R.id.spinnerSchool);
        departmentSpinner = findViewById(R.id.spinnerDept);
        positionSpinner = findViewById(R.id.spinnerPosition);

        //dropdown school
        ArrayAdapter<CharSequence> adapterSchool = ArrayAdapter.createFromResource(this,R.array.schools,android.R.layout.simple_spinner_item);
        adapterSchool.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(adapterSchool);
        schoolSpinner.setOnItemSelectedListener(this);

        //dropdown Position
        ArrayAdapter<CharSequence> adapterPosition = ArrayAdapter.createFromResource(this,R.array.staff_position,android.R.layout.simple_spinner_item);
        adapterPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(adapterPosition);
       // positionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(schoolSpinner.getSelectedItem().equals("SoE")){
            adapterDepartment = ArrayAdapter.createFromResource(this,R.array.SoE_departments,android.R.layout.simple_spinner_item);
        }
        else if (schoolSpinner.getSelectedItem().equals("Humanities")){
            adapterDepartment = ArrayAdapter.createFromResource(this,R.array.Humanities_departments,android.R.layout.simple_spinner_item);
        }
        else {
            adapterDepartment = ArrayAdapter.createFromResource(this,R.array.Science_departments,android.R.layout.simple_spinner_item);
        }
        departmentSpinner.setAdapter(adapterDepartment);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}