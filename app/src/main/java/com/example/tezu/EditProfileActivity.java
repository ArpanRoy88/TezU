package com.example.tezu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tezu.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.sql.SQLOutput;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity  implements OnItemSelectedListener{

    Spinner departmentSpinner;
    Spinner hostelSpinner;
    Spinner schoolSpinner;
    Spinner programmeSpinner;

    ArrayAdapter<CharSequence> adapterDepartment;
    ArrayAdapter<CharSequence> adapterProgramme;
    ArrayAdapter<CharSequence> adapterHostel;
    ArrayAdapter<CharSequence> adapterSchool;


    EditText txtStudentName;
    EditText txtRollNumber;
    EditText txtPassword;
    EditText txtConfirmPassword;
    EditText txtMailId;
    EditText txtStudentPhone;

    private FirebaseUser user;
    private String uid;

    RadioGroup radioGroupGender;
    RadioButton radioButtonGender;

    String name,phone,roll,school,dept,programme,hostel,gender;

    ProgressDialog loadingBar;

    DatabaseReference reference;
    Toolbar myToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("Student");

        myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Edit Profile");
        setSupportActionBar(myToolbar);

        loadingBar = new ProgressDialog(this);


        schoolSpinner = findViewById(R.id.spinnerSchoolEditProfile);
        hostelSpinner = findViewById(R.id.spinnerHostelEditProfile);
        departmentSpinner = findViewById(R.id.spinnerDeptEditProfile);
        programmeSpinner = findViewById(R.id.spinnerProgrammeEditProfile);
        radioGroupGender = findViewById(R.id.radioGroupGenderEditProfile);

        txtStudentName = findViewById(R.id.editTextPersonNameEditProfile);
        txtRollNumber = findViewById(R.id.editTextRollNumberEditProfile);
        txtStudentPhone = findViewById(R.id.editTextPhoneEditProfile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        display(uid);


        //dropdown school
        adapterSchool = ArrayAdapter.createFromResource(this,R.array.schools,android.R.layout.simple_spinner_item);
        adapterSchool.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(adapterSchool);
        schoolSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);


    }


    //dropdown nested method for department and programme

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //  String text = adapterView.getItemAtPosition(i).toString();

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

        //nested dropdown
        departmentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (departmentSpinner.getSelectedItem().equals("CSE")){
                    adapterProgramme = ArrayAdapter.createFromResource(EditProfileActivity.this,R.array.CSE_Programs,android.R.layout.simple_spinner_item);

                }
                else if (departmentSpinner.getSelectedItem().equals("ECE") || departmentSpinner.getSelectedItem().equals("CIVIL")){
                    adapterProgramme = ArrayAdapter.createFromResource(EditProfileActivity.this,R.array.ECE_Civil_Programs,android.R.layout.simple_spinner_item);

                }
                else if(departmentSpinner.getSelectedItem().equals("EFL") || departmentSpinner.getSelectedItem().equals("Sociology") || departmentSpinner.getSelectedItem().equals("Political Science")){
                    adapterProgramme = ArrayAdapter.createFromResource(EditProfileActivity.this,R.array.Humanities_Programs,android.R.layout.simple_spinner_item);

                }
                else if(departmentSpinner.getSelectedItem().equals("Mathematics") || departmentSpinner.getSelectedItem().equals("Physics") || departmentSpinner.getSelectedItem().equals("Chemistry")){
                    adapterProgramme = ArrayAdapter.createFromResource(EditProfileActivity.this,R.array.Science_Programs,android.R.layout.simple_spinner_item);

                }
                programmeSpinner.setAdapter(adapterProgramme);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //function for gender click
    public void genderClick(View v){
        int genderRadioButtonId = radioGroupGender.getCheckedRadioButtonId();
        radioButtonGender = (RadioButton) findViewById(genderRadioButtonId);
        System.out.println("Radioid:"+genderRadioButtonId);
        //Toast.makeText(getBaseContext(),radioButtonGender.getText(), LENGTH_SHORT).show();

        if(radioButtonGender.getText().equals("Male") ){
            adapterHostel = ArrayAdapter.createFromResource(EditProfileActivity.this,R.array.men_hostels,android.R.layout.simple_spinner_item);

        }
        else {
            adapterHostel = ArrayAdapter.createFromResource(EditProfileActivity.this,R.array.women_hostels,android.R.layout.simple_spinner_item);

        }
        hostelSpinner.setAdapter(adapterHostel);
        //  hostelSpinner.setOnItemSelectedListener(StudentRegistration.this);
    }



    //set user details inside text
    public void display(String uid){

        DatabaseReference dbStudent = FirebaseDatabase.getInstance().getReference("Student").child(uid);

        dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User details = dataSnapshot.getValue(User.class);

                name = details.getStudentName();
                phone = details.getContact();
                roll = details.getRollNumber();
                school = details.getSchool();
                dept = details.getDepartment();
                programme = details.getProgramme();
                hostel = details.getHostel();
                gender = details.getGender();

                txtStudentName.setText(name);
                txtRollNumber.setText(roll);
                txtStudentPhone.setText(phone);

                schoolSpinner.setSelection(adapterSchool.getPosition(school));
                //departmentSpinner.setSelection(adapterDepartment.getPosition(dept));
                //programmeSpinner.setSelection(adapterProgramme.getPosition(programme));
                //hostelSpinner.setSelection(adapterHostel.getPosition(hostel));
                //  radioButtonGender.setText(gender);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //update function
    public void update(View view) {

        loadingBar.setMessage("Please wait..");
        loadingBar.show();

        /*if(isNameChanged() || isRollNumberChanged() || isPhoneNumberChanged()
            || isSchoolChanged() || isDepartmentChanged() || isProgrammeChanged() || isHostelChanged())

        {
            Toast.makeText(EditProfileActivity.this,"Account updated",Toast.LENGTH_SHORT).show();
            Intent gotoProfile = new Intent(EditProfileActivity.this,UserProfile.class);
            gotoProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            gotoProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startActivity(gotoProfile);

        }

         */


        reference.child(uid).child("StudentName").setValue(txtStudentName.getText().toString());
        name = txtStudentName.getText().toString();
        reference.child(uid).child("Contact").setValue(txtStudentPhone.getText().toString());
        phone = txtStudentPhone.getText().toString();
        reference.child(uid).child("RollNumber").setValue(txtRollNumber.getText().toString());
        roll = txtRollNumber.getText().toString();
        reference.child(uid).child("School").setValue(schoolSpinner.getSelectedItem().toString());
        school = schoolSpinner.getSelectedItem().toString();
        //reference.child(uid).child("hostel").setValue(hostelSpinner.getSelectedItem().toString());
        //hostel = hostelSpinner.getSelectedItem().toString();
        reference.child(uid).child("Programme").setValue(programmeSpinner.getSelectedItem().toString());
        programme = programmeSpinner.getSelectedItem().toString();

        reference.child(uid).child("Department").setValue(departmentSpinner.getSelectedItem().toString());
        dept = departmentSpinner.getSelectedItem().toString();

        isHostelChanged();

        loadingBar.dismiss();
        Toast.makeText(EditProfileActivity.this,"Account updated",Toast.LENGTH_SHORT).show();


    }

    private boolean isNameChanged() {
        if (!name.equals(txtStudentName.getText().toString())){
            reference.child(uid).child("studentName").setValue(txtStudentName.getText().toString());
            name = txtStudentName.getText().toString();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isPhoneNumberChanged() {

        if (!phone.equals(txtStudentPhone.getText().toString())){
            reference.child(uid).child("contact").setValue(txtStudentPhone.getText().toString());
            phone = txtStudentPhone.getText().toString();
            return true;
        }else {
            return false;
        }
    }

    private boolean isRollNumberChanged() {
        if (!roll.equals(txtRollNumber.getText().toString())){
            reference.child(uid).child("rollNumber").setValue(txtRollNumber.getText().toString());
            roll = txtRollNumber.getText().toString();
            return true;
        }else {
            return false;
        }
    }


    private boolean isSchoolChanged() {
        if (!school.equals(schoolSpinner.getSelectedItem().toString())){
            reference.child(uid).child("school").setValue(schoolSpinner.getSelectedItem().toString());
            school = schoolSpinner.getSelectedItem().toString();
            return true;
        }
        else {
            return false;
        }
    }
    private boolean isHostelChanged() {
        if (!hostel.equals(hostelSpinner.getSelectedItem().toString())){
            reference.child(uid).child("hostel").setValue(hostelSpinner.getSelectedItem().toString());
            hostel = hostelSpinner.getSelectedItem().toString();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isProgrammeChanged() {
        if (!programme.equals(programmeSpinner.getSelectedItem().toString())){
            reference.child(uid).child("programme").setValue(programmeSpinner.getSelectedItem().toString());
            programme = programmeSpinner.getSelectedItem().toString();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isDepartmentChanged() {
        if (!dept.equals(departmentSpinner.getSelectedItem().toString())){
            reference.child(uid).child("department").setValue(departmentSpinner.getSelectedItem().toString());
            dept = departmentSpinner.getSelectedItem().toString();
            return true;
        }
        else {
            return false;
        }
    }

}