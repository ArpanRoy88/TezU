package com.example.tezu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tezu.Adapter.CommentAdapter;
import com.example.tezu.Model.Comment;
import com.example.tezu.Model.Forum;
import com.example.tezu.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ForumDetailsActivity extends AppCompatActivity {

    ImageView userProf, currUser;
    TextView forumDesc, forumDate, forumTitle;
    EditText comment;
    Button addComment;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;

    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    String forumId;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_details);

//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getActionBar().hide();


        userProf = findViewById(R.id.forum_detail_user_img);
        currUser = findViewById(R.id.forum_detail_currentuser_img);
        forumDesc = findViewById(R.id.forum_detail_desc);
        forumTitle = findViewById(R.id.forum_detail_title);
        forumDate = findViewById(R.id.forum_detail_date_name);
        comment = findViewById(R.id.forum_detail_comment);
        addComment = findViewById(R.id.forum_detail_add_comment_btn);

        recyclerView = findViewById(R.id.rv_comment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        recyclerView.setAdapter(commentAdapter);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.getText().toString().equals("")){
                    Toast.makeText(ForumDetailsActivity.this,"Type to send comment",Toast.LENGTH_SHORT).show();
                }else {
                    addComment();
                }
            }
        });

        Intent intent = getIntent();
        forumId = intent.getStringExtra("forumID");
        String title = getIntent().getExtras().getString("title");
        forumTitle.setText(title);
        String desc = getIntent().getExtras().getString("description");
        forumDesc.setText(desc);

        id = getIntent().getExtras().getString("userId");
        System.out.print("ID"+id);
        fetchImg(userProf,id);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getImage(currUser);
        readComments();
        forumId = getIntent().getExtras().getString("forumID");

        String date = timestampToString(getIntent().getExtras().getLong("timestamp"));
        forumDate.setText(date);
    }

    private void fetchImg(final ImageView userProf, String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getProfilePic().equals("Null")) {
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/tezu-33542." +
                            "appspot.com/o/Student%2Fuserphoto.png?alt=media&token=db4d39c7-e8a5-" +
                            "47fe-8624-c40a4d22a626").into(userProf);
                } else {
                    Picasso.get().load(user.getProfilePic()).into(userProf);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void addComment(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ForumComments").child(forumId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", comment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());

        reference.push().setValue(hashMap);
        comment.setText("");
    }


    private  String timestampToString(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return  date;
    }
    private void getImage(final ImageView profilePic){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
//              to-do  FETCH PROFILE IMAGE AND LOSD INTO POST_IMAGE
                Picasso.get().load(user.getProfilePic()).into(profilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void readComments(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ForumComments").child(forumId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    commentList.add(comment);

                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}