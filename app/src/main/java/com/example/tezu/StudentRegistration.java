package com.example.tezu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tezu.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_SHORT;

public class StudentRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RadioGroup radioGroupGender;
    RadioButton radioButtonGender;

    EditText txtStudentName;
    EditText txtRollNumber;
    EditText txtPassword;
    EditText txtConfirmPassword;
    EditText txtMailId;
    EditText txtStudentPhone;


    Spinner departmentSpinner;
    Spinner hostelSpinner;
    Spinner schoolSpinner;
    Spinner programmeSpinner;

    ArrayAdapter<CharSequence> adapterDepartment;
    ArrayAdapter<CharSequence> adapterProgramme;
    ArrayAdapter<CharSequence> adapterHostel;

    Button studentRegistrationSubmit;
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    
    ProgressDialog loadingBar;
    Toolbar myToolbar;
    String Roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);


        schoolSpinner = findViewById(R.id.spinnerSchool);
        hostelSpinner = findViewById(R.id.spinnerHostel);
        departmentSpinner = findViewById(R.id.spinnerDept);
        programmeSpinner = findViewById(R.id.spinnerProgramme);

        txtStudentName = findViewById(R.id.editTextPersonName);
        txtMailId = findViewById(R.id.editTextEmailAddress);
        txtRollNumber = findViewById(R.id.editTextRollNumber);
        txtPassword = findViewById(R.id.editRegisterPassword);
        txtConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        txtStudentPhone = findViewById(R.id.editTextPhone);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        studentRegistrationSubmit = findViewById(R.id.buttonSubmitStudentRegistration);


//        myToolbar = findViewById(R.id.my_toolbar);
//        myToolbar.setTitle("Student's Registration");
//        setSupportActionBar(myToolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Student");


        loadingBar = new ProgressDialog(this);

        //dropdown school
        ArrayAdapter<CharSequence> adapterSchool = ArrayAdapter.createFromResource(this,R.array.schools,android.R.layout.simple_spinner_item);
        adapterSchool.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(adapterSchool);
        schoolSpinner.setOnItemSelectedListener(this);


        //submit button
        studentRegistrationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(StudentRegistration.this,"CLICKED", LENGTH_SHORT).show();
//                Roll = check();
                createNewAccount();

            }
        });

        }


    //dropdown nested method for department and programme000
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
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (departmentSpinner.getSelectedItem().equals("CSE")){
                    adapterProgramme = ArrayAdapter.createFromResource(StudentRegistration.this,R.array.CSE_Programs,android.R.layout.simple_spinner_item);

                }
                else if (departmentSpinner.getSelectedItem().equals("ECE") || departmentSpinner.getSelectedItem().equals("CIVIL")){
                    adapterProgramme = ArrayAdapter.createFromResource(StudentRegistration.this,R.array.ECE_Civil_Programs,android.R.layout.simple_spinner_item);

                }
                else if(departmentSpinner.getSelectedItem().equals("EFL") || departmentSpinner.getSelectedItem().equals("Sociology") || departmentSpinner.getSelectedItem().equals("Political Science")){
                    adapterProgramme = ArrayAdapter.createFromResource(StudentRegistration.this,R.array.Humanities_Programs,android.R.layout.simple_spinner_item);

                }
                else if(departmentSpinner.getSelectedItem().equals("Mathematics") || departmentSpinner.getSelectedItem().equals("Physics") || departmentSpinner.getSelectedItem().equals("Chemistry")){
                    adapterProgramme = ArrayAdapter.createFromResource(StudentRegistration.this,R.array.Science_Programs,android.R.layout.simple_spinner_item);

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
        //Toast.makeText(getBaseContext(),radioButtonGender.getText(), LENGTH_SHORT).show();

        if(radioButtonGender.getText().equals("Male") ){
            adapterHostel = ArrayAdapter.createFromResource(StudentRegistration.this,R.array.men_hostels,android.R.layout.simple_spinner_item);

        }
        else {
            adapterHostel = ArrayAdapter.createFromResource(StudentRegistration.this,R.array.women_hostels,android.R.layout.simple_spinner_item);

        }
        hostelSpinner.setAdapter(adapterHostel);
      //  hostelSpinner.setOnItemSelectedListener(StudentRegistration.this);
    }


    //function to create new account
    private void createNewAccount() {

  //      Toast.makeText(StudentRegistration.this,"CLICKED", LENGTH_SHORT).show();
        //getting text from the fields
        final String studentGender = radioButtonGender.getText().toString();
        final String studentName = txtStudentName.getText().toString();
        final String studentRollNumber = txtRollNumber.getText().toString();
        final String studentMail = txtMailId.getText().toString();
        String studentPassword = txtPassword.getText().toString();
        String studentConfirmPassword = txtConfirmPassword.getText().toString();
        final String studentPhone = txtStudentPhone.getText().toString();
        final String studentSchool = schoolSpinner.getSelectedItem().toString();
        final String studentDepartment = departmentSpinner.getSelectedItem().toString();
        final String studentProgramme = programmeSpinner.getSelectedItem().toString();
        final String studentHostel = hostelSpinner.getSelectedItem().toString();
        final String profilePic = "Null";
        final int[] flag = {1};

//        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student");
//
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot data : dataSnapshot.getChildren()){
//                    User user = data.getValue(User.class);
//
//                    String roll = user.getRollNumber().toString();
//
//                    if (TextUtils.equals(studentRollNumber,roll)) {
//                        flag[0] = 0;
//                        System.out.println("flaga "+flag[0]);
////                        txtRollNumber.setError("Roll already present");
////                        txtRollNumber.requestFocus();
//                        return;
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //handle data
//            }
//        });
//
//        System.out.println("flag "+flag[0]);
        //validation
        if (TextUtils.isEmpty(studentName)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            txtStudentName.setError("Enter Name");
            txtStudentName.requestFocus();
            return;

        }
        else if (TextUtils.isEmpty(studentMail)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            txtMailId.setError("Enter Mail id");
            txtMailId.requestFocus();
            return;

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(studentMail).matches()){
            txtMailId.setError("Invalid Mail id");
            txtMailId.requestFocus();
            return;

        }
        else if (TextUtils.isEmpty(studentRollNumber)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            txtRollNumber.setError("Enter Roll Number");
            txtRollNumber.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(studentPassword)){
            Toast.makeText(this,"Enter Password ", LENGTH_SHORT).show();
            txtPassword.requestFocus();
            //txtPassword.setError("Enter password");
            //return;

        }
        else if (studentPassword.length()<6){
            Toast.makeText(this,"minimum 6 letters", LENGTH_SHORT).show();
            txtPassword.requestFocus();

            //txtPassword.setError("minimum 6 letters");
            //return;
        }
        else if (TextUtils.isEmpty(studentConfirmPassword)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            txtConfirmPassword.setError("Enter Password");
            txtConfirmPassword.requestFocus();
            return;

        }
        else if (!studentConfirmPassword.equals(studentPassword)){
            //Toast.makeText(this,"Enter Correct Password ", LENGTH_SHORT).show();
            txtConfirmPassword.setError("Doesn't Match");
            txtConfirmPassword.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(studentPhone)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            txtStudentPhone.setError("Enter Contact Number");
            txtStudentPhone.requestFocus();
            return;

        }
        else {

            loadingBar.setTitle("Creating Your Account");
            loadingBar.setMessage("Please wait while we are creating your Account..");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            firebaseAuth.createUserWithEmailAndPassword(studentMail,studentPassword)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){


                        StudentDetails studentDetails = new StudentDetails(
                                studentGender,
                                studentName,
                                studentRollNumber,
                                studentMail,
                                studentPhone,
                                studentSchool,
                                studentDepartment,
                                studentProgramme,
                                studentHostel,
                                profilePic
                        );


                        FirebaseDatabase.getInstance().getReference("Student")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(studentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(StudentRegistration.this,"Registration Complete", LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent gotoLoginIntent = new Intent(StudentRegistration.this,MainLogin.class);
                                startActivity(gotoLoginIntent);
                                finish();

                            }
                        });

                         }
                    else if (task.getException() instanceof FirebaseAuthUserCollisionException){

                        Toast.makeText(StudentRegistration.this,"You are already Registered", LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                    else {
                        String message = task.getException().getMessage();
                        Toast.makeText(StudentRegistration.this,"Error Occurred"+ message, LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }

    }
}