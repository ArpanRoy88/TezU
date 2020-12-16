package com.example.tezu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainRegistration extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registration);

        Button btnStudentRegister = findViewById(R.id.btnStudentRegister);
        Button btnStaffRegister = findViewById(R.id.btnStaffRegister);

        btnStudentRegister.setOnClickListener(this);
        btnStaffRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnStudentRegister:
                Intent intent = new Intent(this,StudentRegistration.class);
                startActivity(intent);
                break;

            case R.id.btnStaffRegister:
                intent = new Intent(this, StaffRegistration.class);
                startActivity(intent);
                break;
        }
    }
}