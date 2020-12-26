package com.example.tezu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.tezu.Model.AdUpload;
import com.example.tezu.Model.Comment;
import com.example.tezu.Model.Forum;
import com.example.tezu.Model.Post;
import com.example.tezu.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_SHORT;

public class UserProfile extends AppCompatActivity {

    Toolbar myToolbar;

    TextView txtstudentName;
    TextView txtstudentSchool;
    TextView txtstudentDepartment;
    TextView txtstudentProgramme;
    TextView txtstudentEmail;
    TextView txtstudentRollNumber;
    TextView txtstudentContact;
    TextView txtstudentGender;
    TextView txtstudentHostel;
    ProgressDialog loadingBar;

    CircleImageView studentProfilePicture;

    String email,password;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private FirebaseUser currentUser;

    StorageTask<UploadTask.TaskSnapshot> uploadTask;

    String myUrl = "";
    static int PReqCode = 1;
    Uri imagePickerUri;
    private String UserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent getIntent = getIntent();
        email = getIntent.getStringExtra("Email");

        loadingBar = new ProgressDialog(this);

        txtstudentName = findViewById(R.id.profileStudentName);
        txtstudentSchool  = findViewById(R.id.profileStudentSchool);
        txtstudentDepartment = findViewById(R.id.profileStudentDepartment);
        txtstudentProgramme = findViewById(R.id.profileStudentProgramme);
        txtstudentEmail = findViewById(R.id.profileStudentEmail);
        txtstudentRollNumber = findViewById(R.id.profileStudentRollNumber);
        txtstudentContact = findViewById(R.id.profileStudentContactNumber);
        txtstudentGender = findViewById(R.id.profileStudentGender);
        txtstudentHostel = findViewById(R.id.profileStudentHostel);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        UserId = currentUser.getUid();
        //image view
        studentProfilePicture = findViewById(R.id.StudentProfilePicture);

        fetchProfilePic(studentProfilePicture);
        studentProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22) {
                        checkAndRequestForPermission();
                }
                else
                {
                    openGallery();
                }
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("Student");
        databaseReference = FirebaseDatabase.getInstance().getReference("Student");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                User user = dataSnapshot.getValue(User.class);
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.child("Email").getValue().equals(email)){


                        txtstudentName.setText(ds.child("StudentName").getValue(String.class));
                        txtstudentSchool.setText(ds.child("School").getValue(String.class));
                        txtstudentDepartment.setText(ds.child("Department").getValue(String.class));
                        txtstudentProgramme.setText(ds.child("Programme").getValue(String.class));
                        txtstudentEmail.setText(email);
                        txtstudentRollNumber.setText(ds.child("RollNumber").getValue(String.class));
                        txtstudentContact.setText(ds.child("Contact").getValue(String.class));
                        txtstudentGender.setText(ds.child("Gender").getValue(String.class));
                        txtstudentHostel.setText(ds.child("Hostel").getValue(String.class));
//                        username.setText(user.getStudentName());
//                        Picasso.get().load(imagePickerUri).into(studentProfilePicture);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void fetchProfilePic(final ImageView studentProfilePicture){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student").child(currentUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getProfilePic().equals("Null"))
                {
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/tezu-33542.appspot.com" +
                            "/o/Student%2Fuserphoto.png?alt=media&token=5de695ae-9bfb-469f-8362-ed2eb5cbd22e").into(studentProfilePicture);
                }
                else {
                    Picasso.get().load(user.getProfilePic()).into(studentProfilePicture);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void openGallery() {

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(UserProfile.this);

//        After this onActivityResult gets called
    }


    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(UserProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(UserProfile.this, "Please accept for required permission",Toast.LENGTH_SHORT).show();

            }
            else
            {
                ActivityCompat.requestPermissions(UserProfile.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else
            openGallery();
    }

    private void uploadProfilePicToDatabase(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if (imagePickerUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+ "."+ getFileExtension(imagePickerUri));
            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            uploadTask = fileReference.putFile(imagePickerUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception{
                    if (!task.isSuccessful()){
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl = Objects.requireNonNull(downloadUri).toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student");
                        reference.child(currentUser.getUid()).child("ProfilePic").setValue(myUrl);

                        progressDialog.dismiss();

//                        startActivity(new Intent(UserProfile.this, UserProfile.class));
                        getApplicationContext();
                        finish();
                    }
                    else{
                        Toast.makeText(UserProfile.this,"Failed", LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserProfile.this, e.getMessage(), LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this,"No Image Selected", LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ){
            //after successfull selection store reference to a URI variable

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imagePickerUri = result.getUri();
//            imagePickerUri = data.getData();   && data != null
            studentProfilePicture.setImageURI(imagePickerUri);
//            Toast.makeText(UserProfile.this,"onActivityResult",Toast.LENGTH_SHORT).show();
            uploadProfilePicToDatabase();
        }

    }


    //Actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_profile_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    // Drawer menu action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.EditProfile:

                Toast.makeText(UserProfile.this,"Edit Profile",Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));
                break;

            case R.id.ChangePassword:

                //Toast.makeText(UserProfile.this,"Reset",Toast.LENGTH_SHORT).show();
                changePasswordDialog();

                break;

            case R.id.DeleteAccount:

                delAcc();
                break;

            case R.id.Logout:

                logout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    //chnage password dialog
    private void changePasswordDialog() {

        View view = LayoutInflater.from(UserProfile.this).inflate(R.layout.dialog_change_password,null);

        final EditText oldPassword = view.findViewById(R.id.editTextOldPassword);
        final EditText newPassword = view.findViewById(R.id.editTextNewPassword);
        Button changePassword = view.findViewById(R.id.btnChangePassword);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        changePassword.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                loadingBar.setMessage("Please wait");
                loadingBar.show();

                String oldPwd = oldPassword.getText().toString().trim();
                String newPwd = newPassword.getText().toString().trim();

                if(TextUtils.isEmpty(oldPwd)){
                    Toast.makeText(UserProfile.this,"Please Enter Current Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPwd) ){
                    Toast.makeText(UserProfile.this,"Please Enter New Password",Toast.LENGTH_SHORT).show();
                    return;

                }
                if( newPassword.length()<6){
                    Toast.makeText(UserProfile.this,"Password length must be more than 6 characters",Toast.LENGTH_SHORT).show();
                    return;

                }

                dialog.dismiss();
                updatePassword(oldPwd,newPwd);
            }
        });
    }

    //update password function
    private void updatePassword(String oldPwd, final String newPwd) {

        AuthCredential authCredential = EmailAuthProvider.getCredential(currentUser.getEmail(),oldPwd);
        currentUser.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        currentUser.updatePassword(newPwd)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loadingBar.dismiss();
                                        Toast.makeText(UserProfile.this,"Password Updated",Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loadingBar.dismiss();
                                        Toast.makeText(UserProfile.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(UserProfile.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void delAcc() {

//        deleteEventInterested(UserId);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfile.this);
        dialog.setTitle("Delete Account");
        dialog.setMessage("Are you sure you want to delete your account permanently?");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                loadingBar.setTitle("Deleting Your Account");
                loadingBar.setMessage("Please wait while we are deleting your Account..");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                deleteComment(UserId);
                deleteForumComment(UserId);
                deleteEventInterested(UserId);
                deleteAds(UserId); //delete ads posted by user
                deleteEvents(UserId);
                deleteForums(UserId);

//                deleteUser(UserId);
                currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

//                        deleteComment(UserId);
//                        deleteAds(UserId); //delete ads posted by user
//                        deleteEvents(UserId);
//                        deleteForums(UserId);
//                        deleteUser(UserId); //delete user database

                        if (task.isSuccessful()){
                            try {
                                TimeUnit.SECONDS.sleep(3);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(UserProfile.this,"Account Deleted Successfully",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent goToLogin = new Intent(UserProfile.this,MainLogin.class);
                            goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goToLogin);

                        }
                        else {
                            Toast.makeText(UserProfile.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    }
                });
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        //return dialog;
    }

    private void deleteForumComment(final String userId) {
        final DatabaseReference dbForumComment = FirebaseDatabase.getInstance().getReference("ForumComments");

        dbForumComment.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final String Key = snapshot.getKey();
                final DatabaseReference reference = dbForumComment.child(Key);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            Comment com = data.getValue(Comment.class);
                            String comUserId = com.getPublisher();
                            if (comUserId.equals(userId)){

                                reference.child(Key).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteEventInterested(final String userId) {

        final DatabaseReference eventInterested = FirebaseDatabase.getInstance().getReference("EventInterested");

        eventInterested.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    String Key = data.getKey();
                    eventInterested.child(Key).child(userId).removeValue();
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //handle data
            }
        });
    }

    private void deleteComment(final String userId) {

        final DatabaseReference dbcomment = FirebaseDatabase.getInstance().getReference("Comments");

        dbcomment.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String Key = snapshot.getKey();
                final DatabaseReference reference = dbcomment.child(Key);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            Comment com = data.getValue(Comment.class);
                            String comuserid = com.getPublisher();
                            if (comuserid.equals(userId)){

                                reference.child(com.getCommentId()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void deleteUser(String userId){

        DatabaseReference dbStudent = FirebaseDatabase.getInstance().getReference("Student").child(userId);

        dbStudent.removeValue();
//        logout();

    }
    public void deleteAds(final String userId){

        final DatabaseReference dbAdUploads = FirebaseDatabase.getInstance().getReference("AdUploads");

        dbAdUploads.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    AdUpload ads = data.getValue(AdUpload.class);

                    String adUserId = ads.getAdUserId();

                    if (adUserId.equals(userId)){

                        dbAdUploads.child(ads.getAdUploadId()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //handle data
            }
        });
    }

    private void deleteForums(final String uid) {

        final DatabaseReference dbForumUploads = FirebaseDatabase.getInstance().getReference("Forum");
        dbForumUploads.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Forum forum = data.getValue(Forum.class);

                    String forumUserId = forum.getUserId();

                    if (forumUserId.equals(uid)){
                        final String postId = forum.getPostID();
                        dbForumUploads.child(postId).removeValue();

                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ForumComments");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()){
                                    String key = data.getKey();
                                    if (key.equals(postId)){
                                        ref.child(key).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //handle data
            }
        });
    }

    private void deleteEvents(final String uid) {
        final DatabaseReference dbEventUploads = FirebaseDatabase.getInstance().getReference("Posts");
        dbEventUploads.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Post event = data.getValue(Post.class);

                    String eventUserId = event.getPublisher();

                    if (eventUserId.equals(uid)){
                        final String postid = event.getPostid();
                        dbEventUploads.child(postid).removeValue();


                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comments");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()){
                                    String key = data.getKey();
                                    if (key.equals(postid)){
                                        ref.child(key).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("EventInterested");
                        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()){
                                    String key = data.getKey();
                                    if (key.equals(postid)){
                                        ref1.child(key).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //handle data
            }
        });
    }

    //logout function
    public void logout() {

        FirebaseAuth.getInstance().signOut();
        Intent gotologin = new Intent(getApplicationContext(),MainLogin.class);
        gotologin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotologin);

        finish();
    }
}