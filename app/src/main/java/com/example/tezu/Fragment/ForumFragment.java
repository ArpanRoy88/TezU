package com.example.tezu.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.tezu.Adapter.ForumAdapter;
import com.example.tezu.Adapter.PostAdapter;
import com.example.tezu.Model.Forum;
import com.example.tezu.Model.Post;
import com.example.tezu.Model.User;
import com.example.tezu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class ForumFragment extends Fragment {


    private RecyclerView recyclerView;
    private ForumAdapter forumAdapter;
    private List<Forum> forumList;


    Dialog popupAddForum;
    ImageView popupUserImage,popupAddBtn;
    TextView popupTitle,popupDescription;
    ProgressBar popClickProgress;
    private TextView textView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_forum, container, false);

        textView = view.findViewById(R.id.customText);
        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DrSugiyama-Regular.ttf");
        textView.setTypeface(customFont);

        forumList =  new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        forumAdapter = new ForumAdapter(getContext(), forumList);
        recyclerView.setAdapter(forumAdapter);




        readForums();
        
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniPopup();
//                Snackbar.make(view, "Replace", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                popupAddForum.show();
            }
        });

    //For showing forum


        return view;
    }

    private void readForums() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Forum");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                forumList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Forum forum = snapshot1.getValue(Forum.class);
                    forumList.add(forum);
                }

                forumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void iniPopup() {

        popupAddForum = new Dialog(getContext());
        popupAddForum.setContentView(R.layout.activity_popup_add_forum);
        popupAddForum.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupAddForum.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popupAddForum.getWindow().getAttributes().gravity= Gravity.TOP;


        popupUserImage = popupAddForum.findViewById(R.id.popup_user_image);
        popupTitle = popupAddForum.findViewById(R.id.popup_title);
        popupDescription = popupAddForum.findViewById(R.id.popup_description);
        popupAddBtn = popupAddForum.findViewById(R.id.popup_add);
        popClickProgress = popupAddForum.findViewById(R.id.popup_progressBar);

        fetchData(popupUserImage);

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddBtn.setVisibility(View.INVISIBLE);
                popClickProgress.setVisibility(View.VISIBLE);

                //checking input fields

                if (!popupTitle.getText().toString().isEmpty()
                && !popupDescription.getText().toString().isEmpty())
                {
                    //TO_DO add onsuccesslistener

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Forum");

                    String forumID = reference.push().getKey();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("forumID", forumID);
                    hashMap.put("title", popupTitle.getText().toString());
                    hashMap.put("description", popupDescription.getText().toString());
                    hashMap.put("userId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                    hashMap.put("timestamp", ServerValue.TIMESTAMP);

                    reference.child(Objects.requireNonNull(forumID)).setValue(hashMap);
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popClickProgress.setVisibility(View.INVISIBLE);
                    popupAddForum.dismiss();

                }
                else{
                    Snackbar.make(v, "Please verify all input fields", Snackbar.LENGTH_LONG).show();
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popClickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void fetchData(final ImageView studentProfilePicture){

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student").child(currentUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getProfilePic().equals("Null"))
                {
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/tezu-33542." +
                            "appspot.com/o/Student%2Fuserphoto.png?alt=media&token=db4d39c7-e8a5-" +
                            "47fe-8624-c40a4d22a626").into(studentProfilePicture);
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
}