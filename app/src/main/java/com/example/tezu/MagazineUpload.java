package com.example.tezu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tezu.Model.PostArtcle;
import com.example.tezu.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import static android.widget.Toast.LENGTH_SHORT;

public class MagazineUpload extends AppCompatActivity {

    StorageReference storage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button submit,browse;
    EditText articleTitle,sem,selectPdf;
    Uri pdfUrl;
    ProgressDialog loadingBar;

    private Task<Uri> downloadFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine_upload);

        articleTitle=findViewById(R.id.articleTitle);
        submit=findViewById(R.id.submitButton);
        sem=findViewById(R.id.semester);
        selectPdf=findViewById(R.id.selectPdf);

        browse=findViewById(R.id.browseButton);

        loadingBar=new ProgressDialog(this);
        storage=FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("notification");


        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPdf();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                submitArticle(pdfUrl);
            }

        });
    }

    private void selectPdf() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);//To get the file

        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),86);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void submitArticle(final Uri pdfUrl) {






        final String STORAGE_PATH_UPLOADS = "notifications/";
        final String article_title=articleTitle.getText().toString();
        final String purl= selectPdf.getText().toString();
//        final UploadTask purl;

        final String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        final String semester=sem.getText().toString();
        Calendar calendar=Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        final String username = null;

        if (TextUtils.isEmpty(article_title)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            articleTitle.setError("Enter Title");
            articleTitle.requestFocus();
            return;

        }
        else if (TextUtils.isEmpty(purl)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            selectPdf.setError("Select Pdf");
            selectPdf.requestFocus();
            return;

        }
        else if (TextUtils.isEmpty(semester)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            sem.setError("Enter Semester");
            sem.requestFocus();
            return;

        }
        else {


            loadingBar.setTitle("uploading article");
            loadingBar.setMessage("Please wait while we are uploading your Article..");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            databaseReference = FirebaseDatabase.getInstance().getReference("Student").child(userid);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final User user = dataSnapshot.getValue(User.class);
                    String username = user.getStudentName();


                    final StorageReference reference = storage.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf");

                    downloadFile = reference.putFile(pdfUrl).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw Objects.requireNonNull(task.getException());
                            }
                            return reference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri downloadfile = task.getResult();

                            PostArtcle postArtcle = new PostArtcle(
                                    article_title,
                                    Objects.requireNonNull(downloadfile).toString(),
                                    semester,
                                    currentDate,
                                    user.getStudentName(),
                                    FirebaseAuth.getInstance().getUid()

                            );
                            String unique = databaseReference.push().getKey();

                            FirebaseDatabase.getInstance().getReference("notification")
                                    .child(unique)
//                                    (FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(postArtcle).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(MagazineUpload.this, "Article sent", LENGTH_SHORT).show();
                                    loadingBar.dismiss();


                                    Intent gotoLoginIntent = new Intent(MagazineUpload.this, MainActivity.class);
                                    startActivity(gotoLoginIntent);
                                    finish();

                                }
                            });


                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //check whether the user select a pdf or not
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null&&data.getData()!=null) {
            pdfUrl=data.getData();
            selectPdf.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")));

        } else {
            Toast.makeText(MagazineUpload.this, "Please select a file", LENGTH_SHORT).show();
        }

    }
}