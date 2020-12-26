package com.example.tezu.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tezu.ForumDetailsActivity;
import com.example.tezu.Model.Forum;
import com.example.tezu.Model.User;
import com.example.tezu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder> {


    public List<Forum> mForum;
    public Context mContext;

    private FirebaseUser firebaseUser;

    public ForumAdapter(Context mContext, List<Forum> mForum) {

        this.mContext = mContext;
        this.mForum = mForum;

    }

    @NonNull
    @Override
    public ForumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.forum_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Forum forum = mForum.get(position);

        fetchData(holder.image_profile, holder.username, forum.getUserId());
        if(forum.getDescription().equals("")){
            holder.forumView.setVisibility(View.GONE);

        }
        else {
            holder.forumView.setVisibility(View.VISIBLE);
            holder.forumView.setText(forum.getTitle());
//            System.out.println("TITLE1" + forum.getTitle());

        }
//        isInterested(forum.getForumID(), holder.save);
        forumViewed(forum.getForumID(), holder.forumView);
        totalView(forum.getForumID(), holder.views);

//        holder.save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ( holder.save.getTag().equals("interested")){
//                    FirebaseDatabase.getInstance().getReference().child("Favourite").child(forum.getForumID())
//                            .child(firebaseUser.getUid()).setValue(true);
//                }else
//                {
//                    FirebaseDatabase.getInstance().getReference().child("Favourite").child(forum.getForumID())
//                            .child(firebaseUser.getUid()).removeValue();
//                }
//            }
//        });

        holder.forumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.forumView.getTag().equals("viewed")){
                    FirebaseDatabase.getInstance().getReference().child("View").child(forum.getForumID())
                            .child(firebaseUser.getUid()).setValue(true);
                }
                Intent intent = new Intent(mContext,ForumDetailsActivity.class);

                intent.putExtra("title",mForum.get(position).getTitle());
                intent.putExtra("description",mForum.get(position).getDescription());
                intent.putExtra("forumID",mForum.get(position).getForumID());
                intent.putExtra("userId",mForum.get(position).getUserId()) ;
                long timestamp  = (long) mForum.get(position).getTimestamp();
                intent.putExtra("timestamp",timestamp) ;
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mForum.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView image_profile;
        public TextView forumView,username,views;
//        public ImageView save;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            forumView = itemView.findViewById(R.id.forumView);
            username = itemView.findViewById(R.id.username);
//            save = itemView.findViewById(R.id.save);
            views = itemView.findViewById(R.id.views);
        }
    }

    private void fetchData(final ImageView studentProfilePicture, final TextView username, final String userId) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText("By " + user.getStudentName() + " | ");
                if (user.getProfilePic().equals("Null")) {
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/tezu-33542." +
                            "appspot.com/o/Student%2Fuserphoto.png?alt=media&token=db4d39c7-e8a5-" +
                            "47fe-8624-c40a4d22a626").into(studentProfilePicture);
                } else {
                    Picasso.get().load(user.getProfilePic()).into(studentProfilePicture);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void forumViewed(String forumId, final TextView textView){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("View")
                .child(forumId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){
                    textView.setTag("notviewed");
                }else {
                    textView.setTag("viewed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void totalView(String forumId, final TextView textView){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("View")
                .child(forumId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.getChildrenCount()+" views");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isInterested(String forumId, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Favourite")
                .child(forumId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.saved);
                    imageView.setTag("isinterested");
                }else {
                    imageView.setImageResource(R.drawable.save);
                    imageView.setTag("interested");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}