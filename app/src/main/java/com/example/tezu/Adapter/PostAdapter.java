package com.example.tezu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tezu.CommentActivity;
import com.example.tezu.Fragment.EventFragment;
import com.example.tezu.Model.Post;
import com.example.tezu.Model.User;
import com.example.tezu.R;
import com.example.tezu.UserProfile;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public Context mContext;
    public List<Post> mPost;

    private FirebaseUser firebaseUser;

    String strDate = "";

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_profile, post_image, interested,comment;
        public TextView username, total_interested,description,comments,date_time,title,location;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            post_image = itemView.findViewById(R.id.post_image);
            interested = itemView.findViewById(R.id.post_interested);
            comment = itemView.findViewById(R.id.post_comment);
            total_interested = itemView.findViewById(R.id.no_interested);
            description = itemView.findViewById(R.id.description);
            comments = itemView.findViewById(R.id.comments);
            title = itemView.findViewById(R.id.title);
//            location = itemView.findViewById(R.id.location);
            date_time = itemView.findViewById(R.id.date_time);

        }
    }


    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mPost.get(position);

//        Glide.with(mContext).load(post.getPostimage()).into(holder.post_image);
        Picasso.get().load(post.getPostevent()).into(holder.post_image);

        if(post.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);

        }
        else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());

        }

        holder.date_time.setText(post.getDate_time());
        holder.title.setText(post.getTitle());

//        CALIING FUNCTION
        publisher(holder.image_profile, holder.username, post.getPublisher());

        isInterested(post.getPostid(), holder.interested);
        numberInterested(holder.total_interested, post.getPostid());
        getComments(post.getPostid(), holder.comments);


        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TO DO
            }
        });

        //To-do for date time on click listener

//          date on click listener
        holder.date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strDate = post.getDate_time();

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                Date startDate = null;
                try {
                    startDate = simpleDateFormat.parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);

                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate.getTime());
                intent.putExtra(CalendarContract.Events.ALL_DAY, true);
                intent.putExtra(CalendarContract.Events.TITLE, post.getTitle());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, post.getDescription());
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, post.getLocation());
                mContext.startActivity(intent);

            }
        });


//        FOR INTERESTED PART
        holder.interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.interested.getTag().equals("interested")){
                    FirebaseDatabase.getInstance().getReference().child("EventInterested").child(post.getPostid())
                            .child(firebaseUser.getUid()).setValue(true);
                }else
                {
                    FirebaseDatabase.getInstance().getReference().child("EventInterested").child(post.getPostid())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisherid",post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisherid",post.getPublisher());
                mContext.startActivity(intent);
            }
        });

    }

    private void getComments(String postid, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.setText(snapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void isInterested(String postid, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("EventInterested")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.is_interested);
                    imageView.setTag("notinterested");
                }else {
                    imageView.setImageResource(R.drawable.ic_interested);
                    imageView.setTag("interested");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void numberInterested(final TextView total_interested,String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("EventInterested")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total_interested.setText(snapshot.getChildrenCount()+" interested");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return mPost.size();
    }

    private void publisher(final ImageView image_profile, final TextView username,final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                Picasso.get().load(user.getProfilePic()).into(image_profile);
                username.setText(user.getStudentName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


