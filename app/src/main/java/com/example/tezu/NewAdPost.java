package com.example.tezu;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tezu.Fragment.BuySellFragment;
import com.example.tezu.Fragment.ForumFragment;
import com.example.tezu.Model.AdUpload;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class NewAdPost extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Toolbar myToolbar;

    private ImageView adImage,close;
    private EditText adTitle;
    private EditText adDescription;
    private Spinner adCategorySpinner;
    private EditText adPrice;
    private TextView postAdButton;



    private Uri adPostImageUri = null;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Task<Uri> mUploadTask;


    private String current_user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad_post);


        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("AdUploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("AdUploads");
        //get info of current user
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        close = findViewById(R.id.close);
        adImage = findViewById(R.id.postImage);
        adTitle = findViewById(R.id.adTitle);
        adDescription = findViewById(R.id.adDescription);
        adPrice = findViewById(R.id.adPrice);
        postAdButton = findViewById(R.id.postAdButton);


        //ad category spinner
        adCategorySpinner = findViewById(R.id.adCategorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.ad_categopry,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adCategorySpinner.setAdapter(adapter);
        adCategorySpinner.setOnItemSelectedListener(this);

        //ad image
        adImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(NewAdPost.this);



            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewAdPost.this, BuySellFragment.class));
                finish();

            }
        });

        //post ad Button
        postAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if (mUploadTask != null && mUploadTask.isInProgress()){

                if (mUploadTask != null ){
                    Toast.makeText(getApplicationContext(),"Upload in progress",Toast.LENGTH_SHORT).show();

                }
                else {
                    postMyAd();

                }
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    //post ad function
    private void postMyAd() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        final String theTitle = adTitle.getText().toString();
        final String theDesc = adDescription.getText().toString();
        final String theCategory = adCategorySpinner.getSelectedItem().toString();
        final String thePrice = adPrice.getText().toString();

        if(adPostImageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + '.' + getFileExtension(adPostImageUri));

            mUploadTask = fileReference.putFile(adPostImageUri)

                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw Objects.requireNonNull(task.getException());
                            }
                            return fileReference.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //adProgressBar.setVisibility(View.VISIBLE);
                                }
                            },500);


                            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                            Uri downloadUri = task.getResult();
                            String uploadID = databaseReference.push().getKey();

                            AdUpload adUpload = new AdUpload(
                                    theTitle,
                                    theDesc,
                                    theCategory,
                                    thePrice,
                                    Objects.requireNonNull(downloadUri).toString(),
                                    current_user_id,
                                    uploadID
                            );
                            databaseReference.child(uploadID).setValue(adUpload);

                            progressDialog.dismiss();

                            startActivity(new Intent(NewAdPost.this, BuySellFragment.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewAdPost.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });
/*                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress =  (100 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            adProgressBar.setProgress((int)progress);

                            }

                    });


 */
                        //to go to fragment
                        /*BuySellFragment buySellFragment = new BuySellFragment();
                        FragmentManager manager = getSupportFragmentManager();

                        manager.beginTransaction().add(R.id.drawer, buySellFragment).commit();

                         */
                    /*startActivity(new Intent(NewAdPost.this,MainActivity.class));
                    finish();
                    
                     */

        }else {
            Toast.makeText(this,"No File Selected",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){

                adPostImageUri = result.getUri();

                adImage.setImageURI(adPostImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

                Exception error = result.getError();
            }
        }
    }
}