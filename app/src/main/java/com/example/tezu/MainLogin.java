package com.example.tezu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainLogin extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin;
    TextView txtNotRegisteredYet;
    TextView txtForgotPassword;

    EditText txtUsername;
    EditText txtPassword;

    FirebaseAuth mAuth;

    ProgressDialog loadingBar;

    //FOR AUTO LOGIN(START)
    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            Intent intent = new Intent(MainLogin.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    //(END)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_login);

        btnLogin = findViewById(R.id.btnLogin);
        txtNotRegisteredYet = findViewById(R.id.txtNotRegisteredYet);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        txtUsername = findViewById(R.id.editTextUsername);
        txtPassword = findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);

        btnLogin.setOnClickListener(this);
        txtNotRegisteredYet.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){


            case R.id.btnLogin:
                userLogin();
                break;


            case R.id.txtNotRegisteredYet:
                Intent intent = new Intent(this,MainRegistration.class);
                startActivity(intent);
                Toast.makeText(this,"Register Now",Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtForgotPassword:
                forgotPasswordDialog();
                break;
        }

    }

    //alert dialog for forgot password
    private void forgotPasswordDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Recover your password");

        LinearLayout linearLayout = new LinearLayout(this);


        final EditText emailEditText = new EditText(this);

        //emailEditText.setWidth(200);
        emailEditText.setHint("please type your email");
        emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        linearLayout.addView(emailEditText);
        //linearLayout.setPadding(20,10,20,10);


        dialog.setView(linearLayout);

        //buttons

        dialog.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                loadingBar.setMessage("Please wait");
                loadingBar.show();

                String email = emailEditText.getText().toString().trim();
                startRecovery(email);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        dialog.create().show();
    }

    //recovery
    private void startRecovery(final String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loadingBar.dismiss();
                    Toast.makeText(MainLogin.this,"Email sent to" + email, Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(MainLogin.this,"Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(MainLogin.this,"" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void userLogin() {

        final String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Enter Username",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Login");
            loadingBar.setMessage("Please wait ..");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


            mAuth.signInWithEmailAndPassword(username,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainLogin.this,"Login Successful",Toast.LENGTH_SHORT).show();

                                Intent gotoMainActivityIntent = new Intent(MainLogin.this, MainActivity.class);
                                gotoMainActivityIntent.putExtra("Email",username);
                                gotoMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(gotoMainActivityIntent);
                                finish();

                                loadingBar.dismiss();
                            }
                            else {
                                String message = task.getException().getMessage();
                                Toast.makeText(MainLogin.this,"Error Occurred"+ message,Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });
        }
    }
}